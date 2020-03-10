package com.wsmr.app.barcode;

/*
 * 1.00.2019070100 : Skylark Project Change.
*/

import com.wsmr.lib.dev.ATScanManager;
import com.wsmr.lib.dev.ATScanner;
import com.wsmr.lib.dev.barcode.type.BarcodeType;
import com.wsmr.lib.dev.barcode.type.EventType;
//import com.wsmr.lib.dev.boradcastreceiver.DeviceBroadCastReceiver;
import com.wsmr.lib.dev.event.BarcodeEventListener;
import com.wsmr.lib.diagnostics.ATLog;
import com.wsmr.lib.dialog.WaitDialog;
import com.wsmr.lib.util.SysUtil;

import com.wsmr.app.barcode.adapter.BarcodeListAdapter;
import com.wsmr.app.barcode.option.IT5x80.OptionActivityIT5x80;
import com.wsmr.app.barcode.option.MDI3x00.OptionActivityMDI3x00;
import com.wsmr.app.barcode.option.SE4710.OptionActivitySE4710;
import com.wsmr.app.barcode.option.SE955.OptionActivitySE955;
import com.wsmr.app.barcode.util.SoundPlay;
import com.wsmr.lib.system.ModuleControl;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Vibrator;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Application.ActivityLifecycleCallbacks;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.content.BroadcastReceiver;

@SuppressLint("Wakelock")
public class MainBarcodeActivity extends Activity implements Button.OnClickListener,
        BarcodeEventListener {

    private static final String TAG = "MainActivity";
    private static final String LOG_PATH = "Log";
    private static final boolean ENABLE_LOG = false;

    private ATScanner mScanner;

    private SoundPlay mSound;

    private Vibrator mVibrator;

    private TextView txtDemoVersion;
    private TextView txtDeviceRevision;
    private ListView lstBarcodeList;
    private Button btnClear;
    private Button btnScanAction;
    private Button btnBack;
    private BarcodeListAdapter adapterBarcode;
    private IntentFilter mFilter;

    private ActivityLifecycleManager mCallbacks;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        mCallbacks = new ActivityLifecycleManager();
        getApplication().registerActivityLifecycleCallbacks(mCallbacks);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        // 꺼짐방지
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_barcode);

        String appName = SysUtil.getAppName(this);
        mFilter =null;
        if (ENABLE_LOG)
            ATLog.startUp(LOG_PATH, appName);

        ATLog.d(TAG, "+++ onCreate");

        // Initialize Sound Pool
        mSound = new SoundPlay(this);

        // Initialize Vibrator
        mVibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE);

        // Initialize Widgets
        txtDemoVersion = (TextView) findViewById(R.id.demo_version);
        txtDeviceRevision = (TextView) findViewById(R.id.device_revision);
        lstBarcodeList = (ListView) findViewById(R.id.scan_result);
        adapterBarcode = new BarcodeListAdapter(this);
        lstBarcodeList.setAdapter(adapterBarcode);
        lstBarcodeList.setChoiceMode(ListView.CHOICE_MODE_SINGLE);

        btnClear = (Button) findViewById(R.id.clear);
        btnClear.setOnClickListener(this);
        btnScanAction = (Button) findViewById(R.id.scan_action);
        btnScanAction.setOnClickListener(this);
        btnBack = (Button)findViewById(R.id.back);
        btnBack.setOnClickListener(this);

        String versionName = SysUtil.getVersion(this);
        txtDemoVersion.setText(versionName);

        //
        mFilter = new IntentFilter();
        mFilter.addAction(Intent.ACTION_SHUTDOWN);
        mFilter.setPriority(IntentFilter.SYSTEM_HIGH_PRIORITY - 1);
        registerReceiver(broadcastReceiver, mFilter);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(Manifest.permission.BLUETOOTH) == PackageManager.PERMISSION_GRANTED) {
                Log.d("test","Permission is granted");
            }
            Log.d("test", "--1");
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            int permissionResultWRITE_EXTERNAL_STORAGE = checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE);
            int permissionResultREAD_EXTERNAL_STORAGE = checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE);
            if (permissionResultWRITE_EXTERNAL_STORAGE == PackageManager.PERMISSION_DENIED
                    || permissionResultREAD_EXTERNAL_STORAGE== PackageManager.PERMISSION_DENIED) {
                Log.d("test", "--0");
                requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE
                }, 1000);
            } else {
                Log.d("test", "99");
                //InitProcess();
            }
        }


        InitTask onCreateTask = new InitTask();
        onCreateTask.execute(true);

        ATLog.d(TAG, "--- onCreate");
    }


    @Override
    public void onRequestPermissionsResult(int requestCode,  String[] permissions, int[] grantResults) {
        boolean bAllGranted = true;
        if (requestCode == 1000) {
            for(int i = 0; i < grantResults.length ; i++)
            {
                if(grantResults[i] != PackageManager.PERMISSION_GRANTED){
                    Log.d("test", "App 실행에 필요한 권한이 설정 되지 않았습니다.");
                    finishAffinity();
                    bAllGranted = false;
                    break;
                }
            }
            Log.d("test", "1");
            if(bAllGranted) {
                Log.d("test", "2");
                //InitProcess();
            }
        }
    }
    BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {
            String type = intent.getAction();
            if(type.equals(Intent.ACTION_SHUTDOWN)) {
                ATScanManager.finish();
            }
        }
    };

    @Override
    protected void onDestroy() {

        ATLog.d(TAG, "+++ onDestroy");

        // Deinitalize Scanner Instance
        ATScanManager.finish();

        // Setup basic wake up state
        SysUtil.wakeUnlock();

        ATLog.d(TAG, "--- onDestroy");

        ATLog.shutdown();

        if(mFilter !=null)  //2019-06-14 수정
            unregisterReceiver(broadcastReceiver);

        super.onDestroy();

        getApplication().unregisterActivityLifecycleCallbacks(mCallbacks);
    }



    @Override
    protected void onStart() {
        super.onStart();
        ATLog.d(TAG, "+- onStart");

        if(mScanner != null) {
            //ATScanManager.wakeUp();
            InitTask onStartTask = new InitTask();
            onStartTask.execute(false);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

        ATLog.d(TAG, "+- onResume");
    }

    @Override
    protected void onPause() {
        super.onPause();
        ATLog.d(TAG, "+- onPause");
    }

    @Override
    protected void onStop() {

        if(mScanner != null) {
            ATScanManager.sleep();
        }

        super.onStop();
        ATLog.d(TAG, "+- onStop");
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Intent intent = null;

        switch (item.getItemId()) {
            case R.id.scanner_config:
            case R.id.menu_scanner_config:
                ATLog.d(TAG, "scannerType : %s", mScanner.getScannerType());
                switch (mScanner.getScannerType()) {
                    case AT1D955M_1:
                        intent = new Intent(this, OptionActivitySE955.class);
                        startActivity(intent);
                        return true;
                    case AT2D5X80I_1:
                        //case AT2DN3600: // 2019.12.29
                        intent = new Intent(this, OptionActivityIT5x80.class);
                        startActivity(intent);
                        return true;
                    case AT2D4710M_1:
                        intent = new Intent(this, OptionActivitySE4710.class);
                        startActivity(intent);
                        return true;
                    case AT2DMDI3x00:
                        intent = new Intent(this, OptionActivityMDI3x00.class);
                        startActivity(intent);
                        return true;
				/* //rukieboy cons...?
			case AT2DN3600:
				intent = new Intent(this, OptionActivityN3600.class);
				startActivity(intent);
				return true;
				*/
                    default:
                        break;
                }
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.scan_action:
                enableScanButton(false);

                if (mScanner.isDecoding()) {
                    mScanner.stopDecode();
                } else {
                    mScanner.startDecode();
                }
                enableScanButton(true);
                btnScanAction.requestFocus();   // nermy

                break;
            case R.id.clear:
                adapterBarcode.clear();
                btnScanAction.requestFocus();   // nermy
                break;
            case R.id.back:
                finish();
                break;
        }
    }

    @Override
    public void onStateChanged(EventType state) {

        ATLog.d(TAG, "EventType : %s", state);
        if(state == EventType.DeviceDisconnect){
            enableScanButton(true);
        } else if(state == EventType.DeviceConnect) {
            WaitDialog.hide();
        }
    }

    @Override
    public void onDecodeEvent(BarcodeType type, String barcode) {

        ATLog.d(TAG, "onDecodeEvent(%s, [%s])", type, barcode);

        runOnUiThread(new AddBarcode(type, barcode));

//		if(type != BarcodeType.NoRead){
//			int position = adapterBarcode.addItem(type, barcode);
//			lstBarcodeList.setSelection(position);
//			beep(true);
//		}else{
//			beep(false);
//		}
//
//		enableScanButton(true);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {

        if ((keyCode == KeyEvent.KEYCODE_SOFT_RIGHT ||
                keyCode == KeyEvent.KEYCODE_SHIFT_RIGHT ||
                keyCode == KeyEvent.KEYCODE_SHIFT_LEFT ||
                keyCode == KeyEvent.KEYCODE_F7 ||
                keyCode == KeyEvent.KEYCODE_F8 ||
                keyCode == KeyEvent.KEYCODE_F9 ||
                keyCode == KeyEvent.KEYCODE_F10)
                && event.getRepeatCount() <= 0 && !mScanner.isDecoding()) {

            //ATLog.d(TAG, "DEBUG. onKeyDown(%d)", keyCode);

            enableScanButton(false);
            mScanner.startDecode();
            enableScanButton(true);
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public boolean onKeyUp(int keyCode, KeyEvent event) {
        if ((keyCode == KeyEvent.KEYCODE_SOFT_RIGHT ||
                keyCode == KeyEvent.KEYCODE_SHIFT_RIGHT ||
                keyCode == KeyEvent.KEYCODE_SHIFT_LEFT ||
                keyCode == KeyEvent.KEYCODE_F7 ||
                keyCode == KeyEvent.KEYCODE_F8 ||
                keyCode == KeyEvent.KEYCODE_F9 ||
                keyCode == KeyEvent.KEYCODE_F10)
                && mScanner.isDecoding()) {

            //ATLog.d(TAG, "DEBUG. onKeyUp(%d)", keyCode);

            mScanner.stopDecode();
            enableScanButton(true);
        }
        return super.onKeyUp(keyCode, event);
    }

    // Show Error Device Dialog
    private void showDeviceDialog() {
        // Show Error Alert Dialog
        AlertDialog.Builder alert = new AlertDialog.Builder(this);
        alert.setOnCancelListener(new DialogInterface.OnCancelListener() {

            @Override
            public void onCancel(DialogInterface dialog) {
                finish();
            }
        });
        alert.setPositiveButton(R.string.ok_button,
                new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        finish();
                    }
                });
        alert.setIcon(android.R.drawable.ic_dialog_alert);
        alert.setTitle(R.string.device_error);
        alert.setMessage(R.string.filaed_to_device);
        alert.show();
    }

    // Beep & Vibrate
    private void beep(boolean isSuccess) {
        ATLog.d(TAG, "@@@@ DEBUG. Play beep....!!!!");
        ATLog.d(TAG, "@@@@ _______ nermy 1");

        try{
            if(isSuccess){
                ModuleControl.playBeep(true);
            }else{
                ModuleControl.playBeep(false);
            }
        }catch(Exception e){
            ATLog.e(TAG, e, "ERROR. beep(%s)", isSuccess);
        }

/*
        try{
            if(isSuccess){
                mSound.playSuccess();
                mVibrator.vibrate(100);
            }else{
                mSound.playFail();
            }
        }catch(Exception e){
            ATLog.e(TAG, e, "ERROR. beep(%s)", isSuccess);
        }
*/
    }

    // Enable & Disable Scan Button...
    private void enableScanButton(boolean enable) {

        //ATLog.d(TAG, "===>>> isDecoding : %s", mScanner.isDecoding());

        if (enable) {
            if (mScanner.isDecoding()) {
                btnScanAction.setText(R.string.stop_scan_label);
            } else {
                btnScanAction.setText(R.string.start_scan_label);
            }
        }
        btnScanAction.setEnabled(enable);
    }

    public class ActivityLifecycleManager implements ActivityLifecycleCallbacks {

        private int mRefCount = 0;
        private String _tag = ActivityLifecycleManager.class.getSimpleName();

        @Override
        public void onActivityStarted(Activity activity) {
            mRefCount++;
            ATLog.i(_tag, "INFO. mRefCount : " + mRefCount);

            if(mRefCount == 1) {
                // Setup always wake up
                android.content.Context context = activity.getApplicationContext();
                SysUtil.wakeLock(activity.getApplicationContext(), SysUtil.getAppName(context));
            }
        }

        @Override
        public void onActivityStopped(Activity activity) {
            mRefCount--;

            ATLog.i(_tag, "INFO. mRefCount : " + mRefCount);

            if(mRefCount == 0) {
                // release WakeLock.
                SysUtil.wakeUnlock();
            }
        }

        @Override
        public void onActivityCreated(Activity activity, Bundle savedInstanceState) {}
        @Override
        public void onActivityResumed(Activity activity) {}
        @Override
        public void onActivityPaused(Activity activity) {}
        @Override
        public void onActivitySaveInstanceState(Activity activity, Bundle outState) {}
        @Override
        public void onActivityDestroyed(Activity activity) {}

    }

    public class AddBarcode implements Runnable
    {
        private BarcodeType type;
        private String barcode;

        public AddBarcode(BarcodeType _type, String _barcode){
            this.type = _type;
            this.barcode = _barcode;
        }

        @Override
        public void run() {
            if(type != BarcodeType.NoRead){
                int position = adapterBarcode.addItem(type, barcode);
                lstBarcodeList.setSelection(position);
                beep(true);
            }else{
                beep(false);
            }

            enableScanButton(true);
        }

    }

    private class InitTask extends AsyncTask<Boolean, Void, Void> {

        ProgressDialog asyncDialog = new ProgressDialog(
                MainBarcodeActivity.this);

        @Override
        protected void onPreExecute() {
            asyncDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            asyncDialog.setMessage(getString(R.string.wait_for_initialize));
            asyncDialog.setCancelable(false);
            // show dialog
            asyncDialog.show();
            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(Boolean... args) {

            boolean isOnCreate = args[0];

            if(isOnCreate) {
                mScanner = ATScanManager.getInstance(MainBarcodeActivity.this);
                if(mScanner == null) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            showDeviceDialog();
                        }
                    });
                    return null;
                }

                mScanner.setEventListener(MainBarcodeActivity.this);
            }

            // txtDeviceRevision.setText(mScanner.getVersion());  --> Honeywell MiniDB with N6603SR Engine 의 Revision Length 변경
            // 으로 해당 기능 Check 하지 않음 (사유 : 향후 Length 가 매우 가변적임으로 사용하지 않는것으로 판단 확정 적용
            // 2019.09.06 차정호 수정 적용

            ATScanManager.wakeUp();
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    txtDeviceRevision.setText("1.0");
                }
            });

            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            asyncDialog.dismiss();
            super.onPostExecute(result);
        }


    }

}


