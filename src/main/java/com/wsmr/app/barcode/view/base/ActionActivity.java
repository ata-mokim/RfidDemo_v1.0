package com.wsmr.app.barcode.view.base;

import com.wsmr.app.barcode.R;
import com.wsmr.app.barcode.dialog.CommonDialog;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.Locale;
import java.util.Properties;

import com.wsmr.app.barcode.util.SoundPlay;
import com.wsmr.app.barcode.view.SelectionMask6cActivity;
import com.wsmr.lib.dev.rfid.exception.ATRfidReaderException;
import com.wsmr.lib.dev.rfid.param.QValue;
import com.wsmr.lib.dev.rfid.param.RangeValue;
import com.wsmr.lib.dev.rfid.type.ActionState;
import com.wsmr.lib.dev.rfid.type.ConnectionState;
import com.wsmr.lib.dev.rfid.type.GlobalBandType;
import com.wsmr.lib.dev.rfid.type.InventorySession;
import com.wsmr.lib.dev.rfid.type.InventoryTarget;
import com.wsmr.lib.dev.rfid.type.ResultCode;
import com.wsmr.lib.dev.rfid.type.TagType;
import com.wsmr.lib.diagnostics.ATLog;
import com.wsmr.lib.system.device.type.RfidModuleType;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.SystemClock;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

import org.ini4j.Ini;
import org.ini4j.Wini;

import static java.nio.file.Paths.get;

public abstract class ActionActivity extends ReaderActivity implements OnClickListener {

    private static final String TAG = ActionActivity.class.getSimpleName();

    private static final int MAX_POWER_LEVEL = 300;

    private static final int SELECTION_MASK_VIEW = 6;

    private static final long SKIP_KEY_EVENT_TIME = 1000;

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

    private TextView txtPower;
    private TextView txtOperationTime;
    private TextView txtTagType;

    private Button btnClear;
    private Button btnMask;

    /*private RangeValue mPowerRange;

    private int mPowerLevel;
    private int mOperationTime;*/
    private TagType mTagType;
    private long mTick;
    private long mElapsedTick;

    private SoundPlay mSound;

    // ------------------------------------------------------------------------
    // Constructor
    // ------------------------------------------------------------------------

    public ActionActivity() {
        super();

        mQValue = new QValue();

        mPowerRange = null;
        mPowerLevel = MAX_POWER_LEVEL;
        mOperationTime = 0;
        mTagType = TagType.Tag6C;
        mTick = 0;
        mElapsedTick = 0;
    }

    // ------------------------------------------------------------------------
    // Activity Event Handler
    // ------------------------------------------------------------------------

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mSound = new SoundPlay(this);

        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        // Clear Activity
        try {
            mPowerRange = mReader.getPowerRange();
        } catch (ATRfidReaderException e) {
            ATLog.e(TAG, e, "ERROR. initReader() - Failed to get power range");
        }
        clear();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        enableWidgets(true);

        super.onActivityResult(requestCode, resultCode, data);

        Set_IniFile();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        //
        if ((keyCode == KeyEvent.KEYCODE_SOFT_RIGHT || keyCode == KeyEvent.KEYCODE_SHIFT_RIGHT
                || keyCode == KeyEvent.KEYCODE_SHIFT_LEFT
                || keyCode == KeyEvent.KEYCODE_F7 || keyCode == KeyEvent.KEYCODE_F8 || keyCode == KeyEvent.KEYCODE_F9 || keyCode == KeyEvent.KEYCODE_F10) && event.getRepeatCount() <= 0
                && mReader.getAction() == ActionState.Stop && mReader.getState() == ConnectionState.Connected) {

            ATLog.i(TAG, "INFO. onKeyDown(%d, %d)", keyCode, event.getAction());

            mElapsedTick = SystemClock.elapsedRealtime() - mTick;
            if(mTick == 0 || mElapsedTick > SKIP_KEY_EVENT_TIME) {
                startAction();
                mTick = SystemClock.elapsedRealtime();
            } else {
                ATLog.e(TAG, "INFO. Skip key down event(elapsed:" + mElapsedTick + ")");
                return super.onKeyDown(keyCode, event);
            }

            return true;
        }

        return super.onKeyDown(keyCode, event);
    }

    @Override
    public boolean onKeyUp(int keyCode, KeyEvent event) {

        if ((keyCode == KeyEvent.KEYCODE_SOFT_RIGHT || keyCode == KeyEvent.KEYCODE_SHIFT_RIGHT
                || keyCode == KeyEvent.KEYCODE_SHIFT_LEFT
                || keyCode == KeyEvent.KEYCODE_F7 || keyCode == KeyEvent.KEYCODE_F8 || keyCode == KeyEvent.KEYCODE_F9 || keyCode == KeyEvent.KEYCODE_F10) && event.getRepeatCount() <= 0
                && mReader.getAction() != ActionState.Stop && mReader.getState() == ConnectionState.Connected) {

            ATLog.i(TAG, "INFO. onKeyUp(%d, %d)", keyCode, event.getAction());

            stopAction();

            return true;
        }

        return super.onKeyUp(keyCode, event);
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.power_gain:
                ATLog.i(TAG, "INFO. onClick(power_gain)");
                CommonDialog.showPowerGainDialog(this, R.string.power_gain, getPowerLevel(), mPowerRange,     mPowerGainListener);

                break;
            case R.id.operation_time:
                ATLog.i(TAG, "INFO. onClick(operation_time)");
                CommonDialog.showNumberDialog(this, R.string.operation_time, mOperationTime, new RangeValue(0, 100000),
                        "ms", mOperationTimeListener);

                break;
            case R.id.clear:
                ATLog.i(TAG, "INFO. onClick(clear)");
                clear();
                break;
            case R.id.mask:
                ATLog.i(TAG, "INFO. onClick(mask)");
                Intent intent = null;
                enableWidgets(false);
                intent = new Intent(this, SelectionMask6cActivity.class);
                startActivityForResult(intent, SELECTION_MASK_VIEW);
                break;
            case R.id.actionhex:
                ATLog.i(TAG, "INFO. onClick(action)");
                enableWidgets(false);
                if (mReader.getAction() == ActionState.Stop) {
                    startAction();
                } else {
                    stopAction();
                }
                break;
            case R.id.tag_type:
                ATLog.i(TAG, "INFO. onClick(tag_type)");
                CommonDialog.showTagTypeDialog(this, R.string.tag_type, mTagType, mTagTypeListener);
                break;
        }
    }

    // ------------------------------------------------------------------------
    // Override Widgets Control Methods
    // ------------------------------------------------------------------------

    // Clear Widgets
    protected abstract void clear();

    // Initialize Activity Widgets
    @Override
    protected void initWidgets() {

        // Initialize Power Gain
        txtPower = (TextView) findViewById(R.id.power_gain);
        txtPower.setOnClickListener(this);

        // Initialize Operation Time
        txtOperationTime = (TextView) findViewById(R.id.operation_time);
        txtOperationTime.setOnClickListener(this);

        // Initialize Clear
        btnClear = (Button) findViewById(R.id.clear);
        btnClear.setOnClickListener(this);

        // Initialize Mask
        btnMask = (Button) findViewById(R.id.mask);
        btnMask.setOnClickListener(this);

        // Initialize TagType
        txtTagType = (TextView) findViewById(R.id.tag_type);
        txtTagType.setOnClickListener(this);

        Load_IniFile();
    }

    // Eanble Activity Widgets
    @Override
    protected void enableWidgets(boolean enabled) {

        if (mReader.getAction() == ActionState.Stop) {
            txtPower.setEnabled(enabled);
            txtOperationTime.setEnabled(enabled);
            btnClear.setEnabled(enabled);
            if(mReader.getModuleType() == RfidModuleType.I900MA) {
                txtTagType.setEnabled(enabled);
            }
            if(mReader.getModuleType() == RfidModuleType.ATX00S_1) {
                txtTagType.setEnabled(false);
                btnMask.setEnabled(enabled && isMask());
            }
        } else {
            txtPower.setEnabled(false);
            txtOperationTime.setEnabled(false);
            btnClear.setEnabled(false);
            if(mReader.getModuleType() == RfidModuleType.I900MA) {
                txtTagType.setEnabled(false);
            }
            if(mReader.getModuleType() == RfidModuleType.ATX00S_1) {
                txtTagType.setEnabled(false);
                btnMask.setEnabled(false);
            }
        }
    }

    protected boolean isMask() {
        return true;
    }

    // Initialize Reader
    @Override
    protected void initReader() {
        // Get Power Range
        try {
            mPowerRange = mReader.getPowerRange();
        } catch (ATRfidReaderException e) {
            ATLog.e(TAG, e, "ERROR. initReader() - Failed to get power range");
        }
        ATLog.i(TAG, "INFO. initReader() - [Power Range : %d, %d]", mPowerRange.getMin(), mPowerRange.getMax());

        // Get Power Level
        try {
            mPowerLevel = mReader.getPower();
        } catch (ATRfidReaderException e) {
            ATLog.e(TAG, e, "ERROR. initReader() - Failed to get power level");
        }
        ATLog.i(TAG, "INFO. initReader() - [Power Level : %d]", mPowerLevel);

        // Get Operation Time
        try {
            mOperationTime = mReader.getOperationTime();
        } catch (ATRfidReaderException e) {
            ATLog.e(TAG, e, "ERROR. initReader() - Failed to get operation time");
        }
        ATLog.i(TAG, "INFO. initReader() - [Operation Time : %d]", mOperationTime);

        Load_IniFile();
    }

    // Activated Reader
    @Override
    protected void activateReader() {

        // Set Power Level
        setPowerLevel(mPowerLevel);

        // Set Operation Time
        setOperationTime(mOperationTime);

        // Set Tag Type
        setTagType(mTagType);
    }

    // ------------------------------------------------------------------------
    // Override Widgets Access Methods
    // ------------------------------------------------------------------------

    protected int getPowerLevel() {
        return mPowerLevel;
    }

    protected void setPowerLevel(int power) {
        mPowerLevel = power;
        txtPower.setText(String.format(Locale.US, "%.1f dBm", mPowerLevel / 10.0));
    }

    protected int getOperationTime() {
        return mOperationTime;
    }

    protected void setOperationTime(int time) {
        mOperationTime = time;
        txtOperationTime.setText(String.format(Locale.US, "%d ms", mOperationTime));
    }

    protected TagType getTagType() {
        return mTagType;
    }

    protected void setTagType(TagType type) {
        mTagType = type;
        txtTagType.setText(mTagType.toString());
    }

    // Start Action
    protected abstract void startAction();

    // Stop Action
    protected void stopAction() {

        if(mReader.getAction() == ActionState.Stop) {
            ATLog.e(TAG, "ActionState is not busy.");
            return;
        }
        ResultCode res;

        enableWidgets(false);

        if ((res = mReader.stop()) != ResultCode.NoError) {
            ATLog.e(TAG, "ERROR. stopAction() - Failed to stop operation [%s]", res);
            enableWidgets(true);
            return;
        }

        ATLog.i(TAG, "INFO. stopAction()");
    }

    // ------------------------------------------------------------------------
    // Intenal Widgets Control Methods
    // ------------------------------------------------------------------------

    protected void playSuccess() {
        mSound.playSuccess();
    }

    protected void playFail() {
        mSound.playFail();
    }

    // ------------------------------------------------------------------------
    // CommonDialog Result Listener
    // ------------------------------------------------------------------------

    private CommonDialog.IPowerGainDialogListener mPowerGainListener = new CommonDialog.IPowerGainDialogListener() {

        @Override
        public void onSelected(int value, DialogInterface dialog) {
            try {
                mReader.setPower(value);
            } catch (ATRfidReaderException e) {
                ATLog.e(TAG, e,
                        "ERROR. mPowerGainListener.$CommonDialog.IPowerGainDialogListener.onSelected(%d) - Failed to set power gain",
                        value);
                return;
            }
            setPowerLevel(value);
            ATLog.i(TAG, "INFO. mPowerGainListener.$CommonDialog.IPowerGainDialogListener.onSelected(%d)", value);
        }
    };

    private CommonDialog.INumberDialogListener mOperationTimeListener = new CommonDialog.INumberDialogListener() {

        @Override
        public void onConfirm(int value, DialogInterface dialog) {
            try {
                mReader.setOperationTime(value);
            } catch (ATRfidReaderException e) {
                ATLog.i(TAG, e,
                        "ERROR. mOperationTimeListener.$CommonDialog.INumberDialogListener.onConfirm(%d) - Failed to set operation time",
                        value);
                return;
            }
            setOperationTime(value);
            ATLog.i(TAG, "INFO. mOperationTimeListener.$CommonDialog.INumberDialogListener.onConfirm(%d)", value);
        }
    };

    private CommonDialog.ITagTypeListener mTagTypeListener = new CommonDialog.ITagTypeListener() {

        @Override
        public void onSelected(TagType value, DialogInterface dialog) {
            setTagType(value);
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
