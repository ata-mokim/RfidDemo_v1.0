package com.wsmr.app.barcode;

import java.io.IOException;
import java.util.Locale;
import java.util.concurrent.TimeoutException;

import com.wsmr.app.barcode.dialog.WaitDialog;
import com.wsmr.app.barcode.view.InventoryActivity;
import com.wsmr.app.barcode.view.LockMemoryActivity;
import com.wsmr.app.barcode.view.OptionActivity;
import com.wsmr.app.barcode.view.ReadMemoryActivity;
import com.wsmr.app.barcode.view.WriteMemoryActivity;
import com.wsmr.lib.dev.ATRfidManager;
import com.wsmr.lib.dev.ATRfidReader;
import com.wsmr.lib.dev.event.RfidReaderEventListener;
import com.wsmr.lib.dev.rfid.exception.ATRfidReaderException;
import com.wsmr.lib.dev.rfid.type.ActionState;
import com.wsmr.lib.dev.rfid.type.ConnectionState;
import com.wsmr.lib.dev.rfid.type.ResultCode;
import com.wsmr.lib.diagnostics.ATLog;
import com.wsmr.lib.dialog.MessageDialog;
import com.wsmr.lib.util.SysUtil;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Application.ActivityLifecycleCallbacks;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

@SuppressLint("Wakelock")
public class MainRfidActivity extends Activity implements OnClickListener, RfidReaderEventListener {

    private static final String TAG = MainRfidActivity.class.getSimpleName();
    private static final String LOG_PATH = "Log";
    private static final boolean ENABLE_LOG = true;

    private static final int INVENTORY_VIEW = 0;
    private static final int INVENTORY_EX_VIEW = 1;
    private static final int READ_MEMORY_VIEW = 2;
    private static final int WRITE_MEMORY_VIEW = 3;
    private static final int LOCK_MEMORY_VIEW = 4;
    private static final int OPTION_VIEW = 5;
    private static final int EXIT_VIEW = 6;

    private static final String RFID_DEMO = "RfidDemo";
    private static final String KEY_LOG_LEVEL = "log_level";

    // ------------------------------------------------------------------------
    // Member Variable
    // ------------------------------------------------------------------------

    private ATRfidReader mReader = null;

    private TextView txtDemoVersion;
    private TextView txtFirmwareVersion;
    private Button btnInventory;
    private Button btnReadMemory;
    private Button btnWriteMemory;
    private Button btnLockMemory;
    private Button btnOption;
    private Button btnExit;
    private ImageView imgLogo;

    private ActivityLifecycleManager mCallbacks;


    // ------------------------------------------------------------------------
    // Main Activit Event Handler
    // ------------------------------------------------------------------------

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        mCallbacks = new ActivityLifecycleManager();
        getApplication().registerActivityLifecycleCallbacks(mCallbacks);

        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        //꺼짐방지
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        setContentView(R.layout.activity_main_rfid);

        String appName = SysUtil.getAppName(this);
        Log.d("test", "0");
        if (ENABLE_LOG)
            ATLog.startUp(LOG_PATH, appName);
        ///////////////////
        Log.d("test", "--0");


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            int permissionResultWRITE_EXTERNAL_STORAGE = checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE);
            int permissionResultREAD_EXTERNAL_STORAGE = checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE);
            int permissionResultACCESS_CAMERA = checkSelfPermission(Manifest.permission.CAMERA);
            if (permissionResultWRITE_EXTERNAL_STORAGE == PackageManager.PERMISSION_DENIED
                    || permissionResultREAD_EXTERNAL_STORAGE == PackageManager.PERMISSION_DENIED
                    || permissionResultACCESS_CAMERA == PackageManager.PERMISSION_DENIED) {
                Log.d("test", "--0");
                requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE,
                        Manifest.permission.CAMERA}, 1000);
            } else {
                Log.d("test", "99");
                InitProcess();
            }
        }

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
                InitProcess();
            }
        }
    }

    protected void InitProcess() {
        ////////////////////
        // Initialize Widgets
        loadConfig();
        initWidgets();

        WaitDialog.show(this, R.string.connect_module);


            if ((mReader = ATRfidManager.getInstance()) == null) {
                // The PDA is not equipped with a module, or another RFID App is running.
                WaitDialog.hide();

                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setIcon(android.R.drawable.ic_dialog_alert);
                builder.setTitle(R.string.module_error);
                builder.setMessage(R.string.fail_check_module);
                builder.setPositiveButton(R.string.action_ok, new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        finish();
                    }

                });
                builder.show();
            } else {
                    mReader.setLogLevel(GlobalInfo.getReaderLogLevel());
        }

        ATLog.i(TAG, "INFO. onCreate() - RFID Libary Version [%s]", ATRfidManager.getVersion());
        ATLog.i(TAG, "INFO. onCreate()");
    }

    @Override
    protected void onDestroy() {

        // Deinitalize RFID reader Instance
        ATRfidManager.onDestroy();

        // Wake Unlock
        SysUtil.wakeUnlock();

        saveConfig();

        ATLog.d(TAG, "INFO. onDestroy");

        ATLog.shutdown();

        super.onDestroy();

        getApplication().unregisterActivityLifecycleCallbacks(mCallbacks);
    }

    @Override
    protected void onStart() {
        super.onStart();

        if (mReader != null) {
            ATRfidManager.wakeUp();
        }

        ATLog.i(TAG, "INFO. onStart()");
    }

    @Override
    protected void onStop() {

        ATRfidManager.sleep();

        ATLog.i(TAG, "INFO. onStop()");

        super.onStop();
    }

    @Override
    protected void onResume() {
        super.onResume();

        if (mReader != null)
            mReader.setEventListener(this);

        ATLog.d(TAG, "INFO. onResume()");
    }

    @Override
    protected void onPause() {

        if (mReader != null)
            mReader.removeEventListener(this);

        ATLog.i(TAG, "INFO. onPause()");
        super.onPause();
    }

    @Override
    public void onClick(View v) {
        Intent intent = null;

        enableMenuButtons(false);

        switch (v.getId()) {
            case R.id.inventory:
                intent = new Intent(this, InventoryActivity.class);
                startActivityForResult(intent, INVENTORY_VIEW);
                break;
            case R.id.read_memory:
                intent = new Intent(this, ReadMemoryActivity.class);
                // intent.putExtra(AccessActivity.KEY_SELECTED_TAG, false);
                startActivityForResult(intent, READ_MEMORY_VIEW);
                break;
            case R.id.write_memory:
                intent = new Intent(this, WriteMemoryActivity.class);
                // intent.putExtra(AccessActivity.KEY_SELECTED_TAG, false);
                startActivityForResult(intent, WRITE_MEMORY_VIEW);
                break;
            case R.id.lock_memory:
                intent = new Intent(this, LockMemoryActivity.class);
                // intent.putExtra(AccessActivity.KEY_SELECTED_TAG, false);
                startActivityForResult(intent, LOCK_MEMORY_VIEW);
                break;
            case R.id.option:
                intent = new Intent(this, OptionActivity.class);
                startActivityForResult(intent, OPTION_VIEW);
                break;
            case R.id.exit:
                // 꺼짐방지 해제
                getWindow().clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

                finish();
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case INVENTORY_VIEW:
            case INVENTORY_EX_VIEW:
            case READ_MEMORY_VIEW:
            case WRITE_MEMORY_VIEW:
            case LOCK_MEMORY_VIEW:
            case OPTION_VIEW:
            case EXIT_VIEW:
                enableMenuButtons(true);
                break;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    // ------------------------------------------------------------------------
    // Reader Event Handler
    // ------------------------------------------------------------------------

    @Override
    public void onReaderStateChanged(ATRfidReader reader, ConnectionState state) {

        switch (state) {
            case Connected:
                WaitDialog.hide();
                enableMenuButtons(true);
                String version = "";
                try {
                    version = mReader.getFirmwareVersion();
                } catch (ATRfidReaderException e) {
                    ATLog.e(TAG, e, "ERROR. onReaderStateChanged(%s) - Failed to get firmware version", state);
                    version = "";
                    mReader.disconnect();
                }
                txtFirmwareVersion.setText(version);
                //imgLogo.setImageResource(R.drawable.ic_connected_logo);
                break;
            case Disconnected:
                WaitDialog.hide();
                MessageDialog diag = new MessageDialog(this);
                diag.showError("RFID Communication Error. \nPlease Try Again...");
                enableMenuButtons(true);
                //WaitDialog.hide();
                //enableMenuButtons(false);
                //imgLogo.setImageResource(R.drawable.ic_disconnected_logo);
                break;
            case Connecting:
                enableMenuButtons(false);
                //imgLogo.setImageResource(R.drawable.ic_connecting_logo);
                break;
            default:
                break;
        }

        ATLog.i(TAG, "EVENT. onReaderStateChanged(%s)", state);
    }

    @Override
    public void onReaderActionChanged(ATRfidReader reader, ActionState action) {
        ATLog.i(TAG, "EVENT. onReaderActionchanged(%s)", action);
    }

    @Override
    public void onReaderReadTag(ATRfidReader reader, String tag, float rssi, float phase) {
        ATLog.i(TAG, "EVENT. onReaderReadTag([%s], %.2f, %.2f)", tag, rssi, phase);
    }

    @Override
    public void onReaderResult(ATRfidReader reader, ResultCode code, ActionState action, String epc, String data,
                               float rssi, float phase) {
        ATLog.i(TAG, "EVENT. onReaderResult(%s, %s, [%s], [%s], %.2f, %.2f", code, action, epc, data, rssi, phase);
    }

    // ------------------------------------------------------------------------
    // Internal Widget Control Methods
    // ------------------------------------------------------------------------

    // Initialize Main Activity Widgets
    private void initWidgets() {
        String version = SysUtil.getVersion(this);

        txtDemoVersion = (TextView) findViewById(R.id.demo_version);
        txtDemoVersion.setText(version);
        txtFirmwareVersion = (TextView) findViewById(R.id.firmware_version);
        btnInventory = (Button) findViewById(R.id.inventory);
        btnInventory.setOnClickListener(this);
        btnReadMemory = (Button) findViewById(R.id.read_memory);
        btnReadMemory.setOnClickListener(this);
        btnWriteMemory = (Button) findViewById(R.id.write_memory);
        btnWriteMemory.setOnClickListener(this);
        btnLockMemory = (Button) findViewById(R.id.lock_memory);
        btnLockMemory.setOnClickListener(this);
        btnOption = (Button) findViewById(R.id.option);
        btnOption.setOnClickListener(this);
        btnExit = (Button) findViewById(R.id.exit);
        btnExit.setOnClickListener(this);
        //imgLogo = (ImageView) findViewById(R.id.app_logo);
    }

    // Enable/Disable Menu Button
    private void enableMenuButtons(boolean enabled) {
        btnInventory.setEnabled(enabled);
        btnReadMemory.setEnabled(enabled);
        btnWriteMemory.setEnabled(enabled);
        btnLockMemory.setEnabled(enabled);
        btnOption.setEnabled(enabled);
        btnExit.setEnabled(enabled);
    }

    private void loadConfig() {
        SharedPreferences prefs = getSharedPreferences(RFID_DEMO, MODE_PRIVATE);
        GlobalInfo.setLogLevel(prefs.getInt(KEY_LOG_LEVEL, 0));
    }

    private void saveConfig() {
        SharedPreferences prefs = getSharedPreferences(RFID_DEMO, MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putInt(KEY_LOG_LEVEL, GlobalInfo.getLogLevel());
        editor.commit();
    }

    public class ActivityLifecycleManager implements ActivityLifecycleCallbacks {

        private int mRefCount = 0;
        private String _tag = ActivityLifecycleManager.class.getSimpleName();

        @Override
        public void onActivityStarted(Activity activity) {
            ATLog.i(_tag, String.format(Locale.US, "INFO. %s started.", activity.getClass().getSimpleName()));
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
            ATLog.i(_tag, String.format(Locale.US, "INFO. %s stopped.", activity.getClass().getSimpleName()));
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
}
