package com.wsmr.app.barcode.view;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Locale;
import java.util.Properties;

import com.wsmr.app.barcode.R;
import com.wsmr.app.barcode.dialog.CommonDialog;
import com.wsmr.app.barcode.view.base.AccessActivity;
import com.wsmr.lib.dev.rfid.exception.ATRfidReaderException;
import com.wsmr.lib.dev.rfid.param.EpcMatchParam;
import com.wsmr.lib.dev.rfid.param.LockParam;
import com.wsmr.lib.dev.rfid.param.QValue;
import com.wsmr.lib.dev.rfid.param.RangeValue;
import com.wsmr.lib.dev.rfid.type.ActionState;
import com.wsmr.lib.dev.rfid.type.BankType;
import com.wsmr.lib.dev.rfid.type.GlobalBandType;
import com.wsmr.lib.dev.rfid.type.InventorySession;
import com.wsmr.lib.dev.rfid.type.InventoryTarget;
import com.wsmr.lib.dev.rfid.type.LockType;
import com.wsmr.lib.dev.rfid.type.ResultCode;
import com.wsmr.lib.dev.rfid.type.TagType;
import com.wsmr.lib.diagnostics.ATLog;

import android.content.DialogInterface;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import org.ini4j.Ini;
import org.ini4j.Wini;

public class LockMemoryActivity extends AccessActivity implements OnClickListener {

    private static final String TAG = LockMemoryActivity.class.getSimpleName();

    private static final int OFFSET_ACCESS_PASSWORD = 2;

    private static final int TYPE_KILL_PASSWORD = 0;
    private static final int TYPE_ACCESS_PASSWORD = 1;
    private static final int TYPE_EPC = 2;
    private static final int TYPE_TID = 3;
    private static final int TYPE_USER = 4;

    // ------------------------------------------------------------------------
    // Member Variable
    // ------------------------------------------------------------------------
    protected Ini ini;
    protected String tempOperationTime;
    protected String tempGlobalBand;
    protected String tempInventoryTime;
    protected String tempIdleTime;
    protected String tempSession;
    protected String tempTarget;
    protected String tempStartQ;
    protected String tempMaxQ;
    protected String tempMinQ;
    protected String tempPowerGain;

    private static int InitFlag = 0;

    private RangeValue mPowerRange;
    private GlobalBandType mGlobalBand;
    private int mOperationTime;
    private int mInventoryTime;
    private int mIdleTime;
    private int mPowerLevel;

    private String[] mFreqChanNames;
    private boolean[] mFreqChanUses;
    private InventorySession mInventorySession;
    private InventoryTarget mInventoryTarget;
    private QValue mQValue;

    private TextView txtKillPassword;
    private TextView txtAccessPassword;
    private TextView txtEpc;
    private TextView txtTid;
    private TextView txtUser;
    private Button btnAction;
    private Button btnSetPassword;

    private LockParam mLock;

    // ------------------------------------------------------------------------
    // Constructor
    // ------------------------------------------------------------------------

    public LockMemoryActivity() {
        super();

        mQValue = new QValue();

        mView = R.layout.activity_lock_memory;
        mLock = new LockParam();
    }

    // ------------------------------------------------------------------------
    // Activity Event Handler
    // ------------------------------------------------------------------------


    @Override
    public void onDestroy() {
        super.onDestroy();

        Set_IniFile();
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);

        switch (v.getId()) {
            case R.id.kill_password:
                ATLog.i(TAG, "INFO. onClick(kill_password)");
                CommonDialog.showLockDialog(this, R.string.kill_password, TYPE_KILL_PASSWORD, mLock.getKillPassword(),
                        mLockListener);
                break;
            case R.id.access_password:
                ATLog.i(TAG, "INFO. onClick(access_password)");
                CommonDialog.showLockDialog(this, R.string.access_password, TYPE_ACCESS_PASSWORD,
                        mLock.getAccessPassword(), mLockListener);
                break;
            case R.id.epc:
                ATLog.i(TAG, "INFO. onClick(epc)");
                CommonDialog.showLockDialog(this, R.string.epc, TYPE_EPC, mLock.getEPC(), mLockListener);
                break;
            case R.id.tid:
                ATLog.i(TAG, "INFO. onClick(tid)");
                CommonDialog.showLockDialog(this, R.string.tid, TYPE_TID, mLock.getTID(), mLockListener);
                break;
            case R.id.user:
                ATLog.i(TAG, "INFO. onClick(user)");
                CommonDialog.showLockDialog(this, R.string.user, TYPE_USER, mLock.getUser(), mLockListener);
                break;
            case R.id.set_password:
                ATLog.i(TAG, "INFO. onClick(set_password)");
                CommonDialog.showHexPasswordDialog(this, R.string.set_password, "", mSetPasswordListener);
                break;
        }
    }

    // ------------------------------------------------------------------------
    // Reader Control Methods
    // ------------------------------------------------------------------------

    // Start Action
    @Override
    protected void startAction() {

        //꺼짐방지
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);


        ResultCode res;
        String password = getPassword();
        EpcMatchParam epc = getEpc();
        TagType tagType = getTagType();

        if(tagType != TagType.Tag6C) {
            Toast.makeText(this, R.string.not_supported, Toast.LENGTH_LONG).show();
            enableWidgets(true);
            return;
        }

        clear();
        enableWidgets(false);

        if ((res = mReader.lock6c(mLock, password, epc)) != ResultCode.NoError) {
            ATLog.e(TAG, "ERROR. startAction() - Failed to lock 6C tag [%s]", res);
            enableWidgets(true);
            return;
        }

        ATLog.i(TAG, "INFO. startAction()");

        //Set_IniFile();
    }

    // ------------------------------------------------------------------------
    // Override Widgets Control Methods
    // ------------------------------------------------------------------------

    // Initialize Activity Widgets
    @Override
    protected void initWidgets() {
        //꺼짐방지
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        super.initWidgets();

        // Initialize KillPassword Spinner
        txtKillPassword = (TextView) findViewById(R.id.kill_password);
        txtKillPassword.setOnClickListener(this);

        // Initialize AccessPassword Spinner
        txtAccessPassword = (TextView) findViewById(R.id.access_password);
        txtAccessPassword.setOnClickListener(this);

        // Initialize EPC Spinner
        txtEpc = (TextView) findViewById(R.id.epc);
        txtEpc.setOnClickListener(this);

        // Initialize TID Spinner
        txtTid = (TextView) findViewById(R.id.tid);
        txtTid.setOnClickListener(this);

        // Initialize User Spinner
        txtUser = (TextView) findViewById(R.id.user);
        txtUser.setOnClickListener(this);

        // Initialize Action Button
        btnAction = (Button) findViewById(R.id.actionhex);
        btnAction.setOnClickListener(this);

        // Initialize Set Password Button
        btnSetPassword = (Button) findViewById(R.id.set_password);
        btnSetPassword.setOnClickListener(this);

        setLock(TYPE_KILL_PASSWORD, LockType.NoChange);
        setLock(TYPE_ACCESS_PASSWORD, LockType.NoChange);
        setLock(TYPE_EPC, LockType.NoChange);
        setLock(TYPE_TID, LockType.NoChange);
        setLock(TYPE_USER, LockType.NoChange);

        //Load_IniFile();
    }

    // Eanble Activity Widgets
    @Override
    protected void enableWidgets(boolean enabled) {
        super.enableWidgets(enabled);

        if (mReader.getAction() == ActionState.Stop) {
            txtKillPassword.setEnabled(enabled);
            txtAccessPassword.setEnabled(enabled);
            txtEpc.setEnabled(enabled);
            txtTid.setEnabled(enabled);
            txtUser.setEnabled(enabled);
            btnAction.setText(R.string.action_lock);
            btnSetPassword.setEnabled(enabled);
        } else {
            txtKillPassword.setEnabled(false);
            txtAccessPassword.setEnabled(false);
            txtEpc.setEnabled(false);
            txtTid.setEnabled(false);
            txtUser.setEnabled(false);
            btnAction.setText(R.string.action_stop);
            btnSetPassword.setEnabled(false);
        }
        btnAction.setEnabled(enabled);
    }

    private void setLock(int type, LockType lock) {
        switch (type) {
            case TYPE_KILL_PASSWORD:
                mLock.setKillPassword(lock);
                txtKillPassword.setText(mLock.getKillPassword().toString());
                break;
            case TYPE_ACCESS_PASSWORD:
                mLock.setAccessPassword(lock);
                txtAccessPassword.setText(mLock.getAccessPassword().toString());
                break;
            case TYPE_EPC:
                mLock.setEPC(lock);
                txtEpc.setText(mLock.getEPC().toString());
                break;
            case TYPE_TID:
                mLock.setTID(lock);
                txtTid.setText(mLock.getTID().toString());
                break;
            case TYPE_USER:
                mLock.setUser(lock);
                txtUser.setText(mLock.getUser().toString());
                break;
        }
    }

    // ------------------------------------------------------------------------
    // CommonDialog Result Listener
    // ------------------------------------------------------------------------

    private CommonDialog.ILockDialogListener mLockListener = new CommonDialog.ILockDialogListener() {

        @Override
        public void onSelected(int type, LockType lock, DialogInterface dialog) {
            setLock(type, lock);
            ATLog.i(TAG, "INFO. mLockListener.$CommonDialog.ILockDialogListener.onSelected(%d, %s)", type, lock);
        }
    };

    private CommonDialog.IStringDialogListener mSetPasswordListener = new CommonDialog.IStringDialogListener() {

        @Override
        public void onConfirm(String value, DialogInterface dialog) {
            clear();
            ResultCode res;
            int time = getOperationTime();
            String password = getPassword();
            EpcMatchParam epc = getEpc();
            try {
                mReader.setOperationTime(time);
            } catch (ATRfidReaderException e) {
                ATLog.e(TAG, String.format(Locale.US,
                        "ERROR. setAccessPassword() - Failed to set operation time(%d)", time), e);
            }
            if ((res = mReader.writeMemory6c(BankType.Reserved, OFFSET_ACCESS_PASSWORD, value,
                    password, epc)) != ResultCode.NoError) {
                ATLog.e(TAG,
                        String.format(Locale.US,
                                "ERROR. setAccessPassword() - Failed to write memory {[%s], [%s]} - [%s]", value,
                                password, res));
                return;
            }
            ATLog.i(TAG, "INFO. mSetPasswordListener.$CommonDialog.IStringDialogListener.onConfirm([%s])", value);
        }
    };


    // ------------------------------------------------------------------------
    // Ini File Initial Load
    // ------------------------------------------------------------------------

    private void Load_IniFile() {
        File file = new File("/sdcard/RFIDParm.ini");
        Properties prop = new Properties();

        if (file.exists()){

            try {
                prop.load(new FileInputStream(file));
            }catch (IOException e){
                e.printStackTrace();
            }

        }else {
            try {
                prop.setProperty("[RFIDParm]","");
                prop.setProperty("OperationTime","0");
                prop.setProperty("GlobalBand","Korea");
                prop.setProperty("InventoryTime","400");
                prop.setProperty("IdleTime","0");
                prop.setProperty("Session","Session 2");
                prop.setProperty("Target","AB");
                prop.setProperty("StartQ","4");
                prop.setProperty("MaxQ","15");
                prop.setProperty("MinQ","0");
                prop.setProperty("PowerGain","30.0");

                prop.store(new FileOutputStream(file),null);

                try {
                    prop.load(new FileInputStream(file));
                }catch (IOException e){
                    e.printStackTrace();
                }

            } catch (IOException ioe) {
                ioe.printStackTrace();
            }
        }

        tempOperationTime = prop.getProperty("OperationTime");
        mOperationTime = Integer.parseInt(tempOperationTime);

        tempInventoryTime = prop.getProperty("InventoryTime");
        mInventoryTime = Integer.parseInt(tempInventoryTime);

        tempIdleTime = prop.getProperty("IdleTime");
        mIdleTime = Integer.parseInt(tempIdleTime);

        tempStartQ = prop.getProperty("StartQ");
        mQValue.setStratQ(Integer.parseInt(tempStartQ));

        tempMaxQ = prop.getProperty("MaxQ");
        mQValue.setMaxQ(Integer.parseInt(tempMaxQ));

        tempMinQ = prop.getProperty("MinQ");
        mQValue.setMinQ(Integer.parseInt(tempMinQ));

        tempPowerGain = prop.getProperty("PowerGain");
        double f = Double.parseDouble(tempPowerGain) * 10;
        mPowerLevel = (int)f;

        tempSession =prop.getProperty("Session");
        switch (tempSession) {
            case "Session 0":
                mInventorySession = InventorySession.S0;
                break;
            case "Session 1":
                mInventorySession = InventorySession.S1;
                break;
            case "Session 2":
                mInventorySession = InventorySession.S2;
                break;
            case "Session 3":
                mInventorySession = InventorySession.S3;
                break;
        }

        tempTarget = prop.getProperty("Target");
        switch (tempTarget) {
            case "AB":
                mInventoryTarget = InventoryTarget.AB;
                break;
            case "A":
                mInventoryTarget = InventoryTarget.A;
                break;
            case "B":
                mInventoryTarget = InventoryTarget.B;
                break;
        }

        tempGlobalBand = prop.getProperty("GlobalBand");
        mGlobalBand = GlobalBandType.Korea;

        new Thread(new Runnable() {

            @Override
            public void run() {

                // Set Power Level
                try {
                    mReader.setPower(mPowerLevel);
                } catch (ATRfidReaderException e) {
                    ATLog.e(TAG, e, "ERROR. saveOption() - Failed to set power level");

                    runOnUiThread(new Runnable() {

                        @Override
                        public void run() {
                            // WaitDialog.hide();
                            enableWidgets(true);
                        }
                    });
                    return;
                }
                ATLog.i(TAG, "INFO. saveOption() - [Power Level : %d]", mPowerLevel);

                // Set Operation Time
                try {
                    mReader.setOperationTime(mOperationTime);
                } catch (ATRfidReaderException e) {
                    ATLog.e(TAG, e, "ERROR. saveOption() - Failed to set operation Time");

                    runOnUiThread(new Runnable() {

                        @Override
                        public void run() {
                            //WaitDialog.hide();
                            enableWidgets(true);
                        }
                    });
                    return;
                }
                ATLog.i(TAG, "INFO. saveOption() - [Operation Time : %d]", mOperationTime);


                runOnUiThread(new Runnable() {

                    @Override
                    public void run() {

                        setPowerLevel(mPowerLevel);

                        // Set Operation Time
                        setOperationTime(mOperationTime);

                        enableWidgets(true);

                    }
                });
            }
        }).start();
    }


    private void Set_IniFile(){
        File file = new File("/sdcard/RFIDParm.ini");
        Properties prop = new Properties();

        try {
            prop.load(new FileInputStream(file));
        }catch (IOException e){
            e.printStackTrace();
        }


        TextView txtOperationTime = (TextView) findViewById(R.id.operation_time);

        // Initialize Power Gain
        TextView txtPower = (TextView) findViewById(R.id.power_gain);


        String tOperationTime = (String)txtOperationTime.getText();
        String[] words = tOperationTime.split(" ");
        prop.setProperty("OperationTime",words[0]);

        String tPower = (String)txtPower.getText();
        String[] words3 = tPower.split(" ");
        prop.setProperty("PowerGain",words3[0]);

        try {
            prop.store(new FileOutputStream(file),null);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}