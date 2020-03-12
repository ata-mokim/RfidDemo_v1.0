package com.wsmr.app.barcode.view;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Locale;
import java.util.Properties;

import com.wsmr.app.barcode.R;
import com.wsmr.app.barcode.adapter.MemoryListAdapter;
import com.wsmr.app.barcode.dialog.CommonDialog;
import com.wsmr.app.barcode.dialog.WaitDialog;
import com.wsmr.app.barcode.view.base.ReadWriteMemoryActivity;
import com.wsmr.lib.dev.ATRfidReader;
import com.wsmr.lib.dev.rfid.ATRfid900MAReader;
import com.wsmr.lib.dev.rfid.ATRfidATX00S1Reader;
import com.wsmr.lib.dev.rfid.exception.ATRfidReaderException;
import com.wsmr.lib.dev.rfid.param.EpcMatchParam;
import com.wsmr.lib.dev.rfid.param.QValue;
import com.wsmr.lib.dev.rfid.param.RangeValue;
import com.wsmr.lib.dev.rfid.param.SelectionMask6b;
import com.wsmr.lib.dev.rfid.type.ActionState;
import com.wsmr.lib.dev.rfid.type.BankType;
import com.wsmr.lib.dev.rfid.type.GlobalBandType;
import com.wsmr.lib.dev.rfid.type.InventorySession;
import com.wsmr.lib.dev.rfid.type.InventoryTarget;
import com.wsmr.lib.dev.rfid.type.MaskMatchingType;
import com.wsmr.lib.dev.rfid.type.ResultCode;
import com.wsmr.lib.dev.rfid.type.TagType;
import com.wsmr.lib.diagnostics.ATLog;
import com.wsmr.lib.system.device.type.RfidModuleType;
import com.wsmr.lib.util.SysUtil;

import android.content.DialogInterface;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.ini4j.Ini;
import org.ini4j.Wini;

public class ReadMemoryActivity extends ReadWriteMemoryActivity {

    private static final String TAG = ReadMemoryActivity.class.getSimpleName();

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


    private ListView lstReadValue;
    private TextView txtLength;

    private Button btnAction;

    private MemoryListAdapter adpReadValue;

    private int mLength;

    // ------------------------------------------------------------------------
    // Constructor
    // ------------------------------------------------------------------------

    public ReadMemoryActivity() {
        super();

        mQValue = new QValue();

        mView = R.layout.activity_read_memory;
        mLength = DEFAULT_LENGTH;
    }

    // ------------------------------------------------------------------------
    // Activity Event Handler
    // ------------------------------------------------------------------------

    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()) {
            case R.id.length:
                ATLog.i(TAG, "INFO. onClick(length)");
                CommonDialog.showNumberDialog(this, R.string.length, mLength, new RangeValue(1, 255), WORD_UNIT,
                        mLengthListener);
                break;
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        Set_IniFile();
    }

    // ------------------------------------------------------------------------
    // Reader Event Handler
    // ------------------------------------------------------------------------

    @Override
    public void onReaderResult(ATRfidReader reader, ResultCode code, ActionState action, String epc, String data,
                               float rssi, float phase) {
        super.onReaderResult(reader, code, action, epc, data, rssi, phase);

        if (code != ResultCode.NoError) {
            adpReadValue.clear();
        } else {
            int offset = getOffset();
            adpReadValue.setOffset(offset);
            adpReadValue.setValue(data);
        }
        ATLog.i(TAG, "EVENT. onReaderResult(%s, %s, [%s], [%s], %.2f, %.2f", code, action, epc, data, rssi, phase);

        //Set_IniFile();
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
        BankType bank = getBank();
        int offset = getOffset();
        int length = getLength();
        String password = getPassword();

        EpcMatchParam epc = getEpc();
        TagType tagType = getTagType();

        clear();
        enableWidgets(false);

        if(tagType == TagType.Tag6C) { // word unit
            if ((res = mReader.readMemory6c(bank, offset, length, password, epc)) != ResultCode.NoError) {
                ATLog.e(TAG, "ERROR. startAction() - Failed to read memory 6C tag [%s]", res);
                enableWidgets(true);
                return;
            }
        } else if(tagType == TagType.Tag6B){ // byte unit

            ATRfid900MAReader MAReader = (ATRfid900MAReader)mReader;
            offset *= 2;
            length *= 2;
            String mask = getSelection();

            SelectionMask6b mask6b = new SelectionMask6b(0, mask, MaskMatchingType.Match);
            if ((res = MAReader.readMemory6b(offset, length, mask6b)) != ResultCode.NoError) {
                ATLog.e(TAG, "ERROR. startAction() - Failed to read memory 6B tag [%s]", res);
                enableWidgets(true);
                return;
            }
        } else {
            Toast.makeText(this, R.string.not_supported, Toast.LENGTH_SHORT).show();
            enableWidgets(true);
        }

        ATLog.i(TAG, "INFO. startAction()");
    }

    // ------------------------------------------------------------------------
    // Override Widgets Control Methods
    // ------------------------------------------------------------------------

    // Clear Widgets
    @Override
    protected void clear() {
        super.clear();

        adpReadValue.clear();
    }

    // Initialize Activity Widgets
    @Override
    protected void initWidgets() {
        //꺼짐방지
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        super.initWidgets();

        // Initialize Read Value
        lstReadValue = (ListView) findViewById(R.id.read_value);
        adpReadValue = new MemoryListAdapter(this);
        lstReadValue.setAdapter(adpReadValue);

        // Initialize Length
        txtLength = (TextView) findViewById(R.id.length);
        txtLength.setOnClickListener(this);

        // Initialize Action Button
        btnAction = (Button) findViewById(R.id.actionhex);
        btnAction.setOnClickListener(this);

        setBank(BankType.EPC);
        setOffset(2);
        setLength(2);

        //Load_IniFile();

    }

    // Eanble Activity Widgets
    @Override
    protected void enableWidgets(boolean enabled) {
        super.enableWidgets(enabled);

        if (mReader.getAction() == ActionState.Stop) {
            lstReadValue.setEnabled(enabled);
            txtLength.setEnabled(enabled);
            btnAction.setText(R.string.action_read);
        } else {
            lstReadValue.setEnabled(false);
            txtLength.setEnabled(false);
            btnAction.setText(R.string.action_stop);
        }
        btnAction.setEnabled(enabled);



    }

    // Get Length
    private int getLength() {
        return mLength;
    }

    private void setLength(int length) {
        mLength = length;
        txtLength.setText(String.format(Locale.US, "%d WORD", mLength));
    }

    // ------------------------------------------------------------------------
    // CommonDialog Result Listener
    // ------------------------------------------------------------------------

    private CommonDialog.INumberDialogListener mLengthListener = new CommonDialog.INumberDialogListener() {

        @Override
        public void onConfirm(int value, DialogInterface dialog) {
            setLength(value);
            ATLog.i(TAG, "INFO. mLengthListener.$CommonDialog.INumberDialogListener.onConfirm(%d)", value);
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
