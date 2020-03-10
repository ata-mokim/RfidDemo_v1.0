package com.wsmr.app.barcode.option.SE4710;

import com.wsmr.lib.dev.ATScanManager;
import com.wsmr.lib.dev.ATScanner;
import com.wsmr.lib.dev.barcode.params.ATScanSE4710Parameter;
import com.wsmr.lib.dev.barcode.motorola.param.SSIParamName;
import com.wsmr.lib.dev.barcode.motorola.param.SSIParamValueList;
import com.wsmr.app.barcode.R;
import com.wsmr.app.barcode.widget.SymbolLength;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.Toast;

public class OptionSymbolCode39Activity extends Activity implements
        OnClickListener {

    @SuppressWarnings("unused")
    private static final String TAG = "OptionSymbolCode39Activity";

    private ATScanner mScanner;
    private ATScanSE4710Parameter mParam;

    private CheckBox chkTrioptic;
    private CheckBox chkConvert;
    private CheckBox chkPrefix;
    private CheckBox chkCheckDigitVerify;
    private CheckBox chkCheckDigit;
    private CheckBox chkFullAscii;
    private SymbolLength wgtLength;
    private Button btnSetOption;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_option_1d_symbol_code39);
        getActionBar().setDisplayHomeAsUpEnabled(true);
        this.setTitle(R.string.symbol_code39_name);

        // Initialize Scanner Instance
        this.mScanner = ATScanManager.getInstance(getApplicationContext());
        this.mParam = (ATScanSE4710Parameter) this.mScanner.getParameter();

        // Initialize Widgets
        this.chkTrioptic = (CheckBox) findViewById(R.id.trioptic_code_39);
        this.chkConvert = (CheckBox) findViewById(R.id.convert_code_39_to_code_32);
        this.chkPrefix = (CheckBox) findViewById(R.id.code_32_prefix);
        this.chkCheckDigitVerify = (CheckBox) findViewById(R.id.code_39_check_digit_verification);
        this.chkCheckDigit = (CheckBox) findViewById(R.id.transmit_code_39_check_digit);
        this.chkFullAscii = (CheckBox) findViewById(R.id.code_39_full_ascii);
        this.wgtLength = (SymbolLength) findViewById(R.id.length);
        this.btnSetOption = (Button) findViewById(R.id.set_option);
        this.btnSetOption.setOnClickListener(this);

        // Load Scanner Symbol Detail Option
        loadOption();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    protected void onStart() {
        super.onStart();

        if(mScanner != null)
            ATScanManager.wakeUp();
    }

    @Override
    protected void onStop() {
        if(mScanner != null)
            ATScanManager.sleep();
        super.onStop();
    }

    @SuppressLint("ShowToast")
    @Override
    public void onClick(View v) {
        if (R.id.set_option == v.getId()) {
            SSIParamValueList paramList = new SSIParamValueList();

            paramList.add(SSIParamName.Trioptic_Code39,
                    this.chkTrioptic.isChecked());
            paramList.add(SSIParamName.Convert_Code39_To_Code32,
                    this.chkConvert.isChecked());
            paramList
                    .add(SSIParamName.Code32_Prefix, this.chkPrefix.isChecked());
            paramList.add(SSIParamName.Code39_CheckDigitVerification,
                    this.chkCheckDigitVerify.isChecked());
            paramList.add(SSIParamName.Transmit_Code39_CheckDigit,
                    this.chkCheckDigit.isChecked());
            paramList.add(SSIParamName.Code39_Full_ASCII_Conversion,
                    this.chkFullAscii.isChecked());

            paramList.add(SSIParamName.Code39_Length_Min,
                    this.wgtLength.getLength1());
            paramList.add(SSIParamName.Code39_Length_Max,
                    this.wgtLength.getLength2());

            if (this.mParam.setParams(paramList)) {
                this.setResult(RESULT_OK);
                this.finish();
            } else {
                Toast.makeText(this, R.string.faile_to_set_symbologies,
                        Toast.LENGTH_LONG).show();
            }
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                this.finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    // Load Scanner Symbol Detail Option
    private void loadOption() {
        SSIParamValueList paramList = this.mParam.getParams(new SSIParamName[] {
                SSIParamName.Trioptic_Code39,
                SSIParamName.Convert_Code39_To_Code32,
                SSIParamName.Code32_Prefix,
                SSIParamName.Code39_CheckDigitVerification,
                SSIParamName.Transmit_Code39_CheckDigit,
                SSIParamName.Code39_Full_ASCII_Conversion,
                SSIParamName.Code39_Length_Min, SSIParamName.Code39_Length_Max });
        this.chkTrioptic.setChecked((Boolean) paramList
                .getValueAt(SSIParamName.Trioptic_Code39));
        this.chkConvert.setChecked((Boolean) paramList
                .getValueAt(SSIParamName.Convert_Code39_To_Code32));
        this.chkPrefix.setChecked((Boolean) paramList
                .getValueAt(SSIParamName.Code32_Prefix));
        this.chkCheckDigitVerify.setChecked((Boolean) paramList
                .getValueAt(SSIParamName.Code39_CheckDigitVerification));
        this.chkCheckDigit.setChecked((Boolean) paramList
                .getValueAt(SSIParamName.Transmit_Code39_CheckDigit));
        this.chkFullAscii.setChecked((Boolean) paramList
                .getValueAt(SSIParamName.Code39_Full_ASCII_Conversion));
        this.wgtLength.setLength(
                (Integer) paramList.getValueAt(SSIParamName.Code39_Length_Min),
                (Integer) paramList.getValueAt(SSIParamName.Code39_Length_Max));
    }
}
