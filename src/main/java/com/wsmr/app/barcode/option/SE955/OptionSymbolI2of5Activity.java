package com.wsmr.app.barcode.option.SE955;

import com.wsmr.lib.dev.ATScanManager;
import com.wsmr.lib.dev.ATScanner;
import com.wsmr.lib.dev.barcode.motorola.param.SSIParamName;
import com.wsmr.lib.dev.barcode.motorola.param.SSIParamValueList;
import com.wsmr.lib.dev.barcode.params.ATScanSE955Parameter;
import com.wsmr.lib.dev.barcode.motorola.type.I2of5CheckDigitVerification;
import com.wsmr.app.barcode.R;
import com.wsmr.app.barcode.adapter.ValueAdapter;
import com.wsmr.app.barcode.adapter.ValueItem;
import com.wsmr.app.barcode.widget.SymbolLength;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.Spinner;
import android.widget.Toast;

public class OptionSymbolI2of5Activity extends Activity implements
        OnClickListener {

    @SuppressWarnings("unused")
    private static final String TAG = "OptionSymbolI2of5Activity";

    private ATScanner mScanner;
    private ATScanSE955Parameter mParam;

    private CheckBox chkCheckDigit;
    private CheckBox chkConvert;
    private Spinner chkCheckDigitVerify;
    private SymbolLength wgtLength;
    private Button btnSetOption;

    private ValueAdapter<I2of5CheckDigitVerification> adpCheckDigitVerify;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_option_1d_symbol_interleaved2of5);
        getActionBar().setDisplayHomeAsUpEnabled(true);

        // Initialize Scanner Instance
        this.mScanner = ATScanManager.getInstance(getApplicationContext());
        this.mParam = (ATScanSE955Parameter) this.mScanner.getParameter();

        // Initialize Widgets
        this.chkCheckDigit = (CheckBox) findViewById(R.id.transmit_i2of5_check_digit);
        this.chkConvert = (CheckBox) findViewById(R.id.convert_i2of5_to_ean_13);
        this.chkCheckDigitVerify = (Spinner) findViewById(R.id.i2of5_check_digit_verification);
        this.adpCheckDigitVerify = new ValueAdapter<I2of5CheckDigitVerification>(
                this);
        for (I2of5CheckDigitVerification item : I2of5CheckDigitVerification
                .values()) {
            this.adpCheckDigitVerify
                    .add(new ValueItem<I2of5CheckDigitVerification>(item
                            .toString(), item));
        }
        this.chkCheckDigitVerify.setAdapter(this.adpCheckDigitVerify);
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

            paramList.add(SSIParamName.Transmit_I2of5_CheckDigit,
                    this.chkCheckDigit.isChecked());
            paramList.add(SSIParamName.Convert_I2of5_To_EAN_13,
                    this.chkConvert.isChecked());
            paramList.add(SSIParamName.I2of5_CheckDigitVerification,
                    I2of5CheckDigitVerification
                            .valueOf((byte) this.chkCheckDigitVerify
                                    .getSelectedItemPosition()));
            paramList.add(SSIParamName.I2of5_Length_Min,
                    this.wgtLength.getLength1());
            paramList.add(SSIParamName.I2of5_Length_Max,
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
                SSIParamName.Transmit_I2of5_CheckDigit,
                SSIParamName.Convert_I2of5_To_EAN_13,
                SSIParamName.I2of5_CheckDigitVerification,
                SSIParamName.I2of5_Length_Min, SSIParamName.I2of5_Length_Max });
        this.chkCheckDigit.setChecked((Boolean) paramList
                .getValueAt(SSIParamName.Transmit_I2of5_CheckDigit));
        this.chkConvert.setChecked((Boolean) paramList
                .getValueAt(SSIParamName.Convert_I2of5_To_EAN_13));
        this.chkCheckDigitVerify.setSelection(this.adpCheckDigitVerify
                .getPosition((I2of5CheckDigitVerification) paramList
                        .getValueAt(SSIParamName.I2of5_CheckDigitVerification)));
        this.wgtLength.setLength(
                (Integer) paramList.getValueAt(SSIParamName.I2of5_Length_Min),
                (Integer) paramList.getValueAt(SSIParamName.I2of5_Length_Max));
    }
}
