package com.wsmr.app.barcode;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
//import android.bluetooth.BluetoothAdapter;
import android.app.AlertDialog;
import android.app.Application.ActivityLifecycleCallbacks;
import android.content.ActivityNotFoundException;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.Toast;

import java.util.List;
import java.util.Locale;
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
import com.wsmr.lib.util.SysUtil;import com.wsmr.app.barcode.dialog.CommonDialog;
import com.wsmr.app.barcode.option.IT5x80.OptionActivityIT5x80;
import com.wsmr.app.barcode.option.IT5x80.OptionGeneralConfigActivity;
import com.wsmr.app.barcode.option.IT5x80.OptionSymbol;
import com.wsmr.app.barcode.view.InventoryActivity;
import com.wsmr.app.barcode.view.LockMemoryActivity;
import com.wsmr.app.barcode.view.OptionActivity;
import com.wsmr.app.barcode.view.SelectionMask6cActivity;
import com.wsmr.app.barcode.view.base.ActionActivity;
import com.wsmr.lib.system.ModuleControl;

import static android.support.constraint.Constraints.TAG;

@SuppressLint("Wakelock")
public class MenuActivity extends Activity
{
    Button mBtnRfid;
    Button mBtnBarCode;
    Button mBtnReadMemory;
    Button mBtnWriteMemory;
    Button mBtnLockMemory;
    Button mBtnSelectionMask;
    //    Button mBtnOn;
//    Button mBtnOff;
    Button mBtnExit;
    View.OnClickListener mOnClickListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        // 꺼짐방지
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

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

            }
        }

        //Notification Launcher
        /*Intent i = new Intent(Intent.ACTION_MAIN);
        i.addCategory(Intent.CATEGORY_LAUNCHER);
        i.setPackage("com.wsmr.app.BatteryNotification");
        startActivity(i);*/

        //launchApp("com.wsmr.app.BatteryNotification");

        setContentView(R.layout.activity_menu);
        //final BluetoothAdapter bAdapter = BluetoothAdapter.getDefaultAdapter();

        mOnClickListener = new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                if( v == mBtnBarCode )
                {
                    Intent intent = new Intent(MenuActivity.this, MainBarcodeActivity.class);
                    startActivity(intent);
                }
                if( v == mBtnRfid )
                {
                    Intent intent = new Intent(MenuActivity.this, InventoryActivity.class);
                    startActivity(intent);
                }
                if( v == mBtnReadMemory )
                {
                    Intent intent = new Intent(MenuActivity.this, ReadMemoryActivity.class);
                    startActivity(intent);
                }
                if( v == mBtnWriteMemory )
                {
                    Intent intent = new Intent(MenuActivity.this, WriteMemoryActivity.class);
                    startActivity(intent);
                }
                if( v == mBtnLockMemory )
                {
                    Intent intent = new Intent(MenuActivity.this, LockMemoryActivity.class);
                    startActivity(intent);
                }
                if( v == mBtnSelectionMask )
                {
                    Intent intent = new Intent(MenuActivity.this, SelectionMask6cActivity.class);
                    startActivity(intent);
                }
                //               if( v == mBtnOn )
                //               {
                //                   final int intVal = 1;
                //                   if (bAdapter == null){Toast.makeText(getApplicationContext(),"Bluetooth Not Supported",Toast.LENGTH_SHORT).show();}
                //                   else{if(!bAdapter.isEnabled()){
                //                       startActivityForResult(new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE),1);
                //                       Toast.makeText(getApplicationContext(),"Bluetooth Turned ON",Toast.LENGTH_SHORT).show();}}
//
//                    //Intent eintent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
//                    //startActivityForResult(eintent, intVal);
//                }
//                if( v == mBtnOff )
//                {
//                    BluetoothAdapter bAdapter = BluetoothAdapter.getDefaultAdapter();
//                    bAdapter.disable();
//                    Toast.makeText(getApplicationContext(),"Bluetooth Turned OFF", Toast.LENGTH_SHORT).show();
//                }
                if( v == mBtnExit )
                {
                    ModuleControl.powerRfidModule(false);

                    // 꺼짐방지 해제
                    getWindow().clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

                    finish();
                }
            }
        };

        mBtnBarCode = (Button)findViewById(R.id.btn_barcode);mBtnBarCode.setOnClickListener(mOnClickListener);
        mBtnRfid = (Button)findViewById(R.id.btn_rfid);mBtnRfid.setOnClickListener(mOnClickListener);
        mBtnReadMemory = (Button)findViewById(R.id.btn_readmemory);mBtnReadMemory.setOnClickListener(mOnClickListener);
        mBtnWriteMemory = (Button)findViewById(R.id.btn_writememory);mBtnWriteMemory.setOnClickListener(mOnClickListener);
        mBtnLockMemory = (Button)findViewById(R.id.btn_lockmemory);mBtnLockMemory.setOnClickListener(mOnClickListener);
        mBtnSelectionMask = (Button)findViewById(R.id.btn_selectionmask);mBtnSelectionMask.setOnClickListener(mOnClickListener);
        //       mBtnOn = (Button)findViewById(R.id.btnOn);mBtnOn.setOnClickListener(mOnClickListener);
        //       mBtnOff = (Button)findViewById(R.id.btnOFF);mBtnOff.setOnClickListener(mOnClickListener);
        mBtnExit = (Button)findViewById(R.id.btn_exit);mBtnExit.setOnClickListener(mOnClickListener);
    }

    public void startNewActivity(Context context, String packageName) {
        Intent intent = context.getPackageManager().getLaunchIntentForPackage(packageName);
        if (intent != null) {
            // We found the activity now start the activity
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(intent);
        } else {
            // Bring user to the market or let them choose an app?
            intent = new Intent(Intent.ACTION_VIEW);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            //intent.setData(Uri.parse("market://details?id=" + packageName));
            context.startActivity(intent);
        }
    }

    protected void launchApp(String packageName) {
        Intent mIntent = getPackageManager().getLaunchIntentForPackage(
                packageName);
        if (mIntent != null) {
            try {
                startActivity(mIntent);
            } catch (ActivityNotFoundException err) {
                /*Toast t = Toast.makeText(getApplicationContext(),
                        R.string.app_not_found, Toast.LENGTH_SHORT);
                t.show();*/
            }
        }

/*        final PackageManager pm = getPackageManager();

        List<ApplicationInfo> packages = pm
                .getInstalledApplications(PackageManager.GET_META_DATA);

        for (ApplicationInfo packageInfo : packages) {

            Log.d(TAG, "Installed package :" + packageInfo.packageName);
            *//*Log.d(TAG,
                    "Launch Activity :"
                            + pm.getLaunchIntentForPackage(packageInfo.packageName));*//*

        }*/

    }


}
