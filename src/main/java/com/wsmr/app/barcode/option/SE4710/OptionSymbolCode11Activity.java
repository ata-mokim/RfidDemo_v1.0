package com.wsmr.app.barcode.option.SE4710;

import com.wsmr.lib.dev.ATScanManager;
import com.wsmr.lib.dev.ATScanner;
import com.wsmr.lib.dev.barcode.motorola.param.SSIParamName;
import com.wsmr.lib.dev.barcode.motorola.param.SSIParamValueList;
import com.wsmr.lib.dev.barcode.motorola.type.Code11CheckDigitVerification;
import com.wsmr.lib.dev.barcode.params.ATScanSE4710Parameter;
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

public class OptionSymbolCode11Activity extends Activity implements
        OnClickListener {

    @SuppressWarnings("unused")
    private static final String TAG = "OptionSymbolCode11Activity";

    private ATScanner mScanner;
    private ATScanSE4710Parameter mParam;

    private CheckBox chkCheckDigit;
    private Spinner spnCheckDigitVerify;
    private SymbolLength wgtLength;
    private Button btnSetOption;

    private ValueAdapter<Code11CheckDigitVerification> adpDigitVerify;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_option_1d_symbol_code11);
        getActionBar().setDisplayHomeAsUpEnabled(true);
        this.setTitle(R.string.symbol_code11_name);

        // Initialize Scanner Instance
        this.mScanner = ATScanManager.getInstance(getApplicationContext());
        this.mParam = (ATScanSE4710Parameter) this.mScanner.getParameter();

        // Initialize Widgets
        this.chkCheckDigit = (CheckBox) findViewById(R.id.transmit_code_11_check_digit);
        this.spnCheckDigitVerify = (Spinner) findViewById(R.id.code_11_check_digit_verification);
        this.adpDigitVerify = new ValueAdapter<Code11CheckDigitVerification>(
                this);
        for (Code11CheckDigitVerification item : Code11CheckDigitVerification
                .values()) {
            this.adpDigitVerify
                    .add(new ValueItem<Code11CheckDigitVerification>(item
                            .toString(), item));
        }
        this.spnCheckDigitVerify.setAdapter(this.adpDigitVerify);
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

            paramList.add(SSIParamName.Transmit_Code11_CheckDigit,
                    this.chkCheckDigit.isChecked());
            paramList.add(SSIParamName.Code11_CheckDigitVerification,
                    this.adpDigitVerify.getItem(this.spnCheckDigitVerify
                            .getSelectedItemPosition()));
            paramList.add(SSIParamName.Code11_Length_Min,
                    this.wgtLength.getLength1());
            paramList.add(SSIParamName.Code11_Length_Max,
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
                SSIParamName.Transmit_Code11_CheckDigit,
                SSIParamName.Code11_CheckDigitVerification,
                SSIParamName.Code11_Length_Min, SSIParamName.Code11_Length_Max });
        this.chkCheckDigit.setChecked((Boolean) paramList
                .getValueAt(SSIParamName.Transmit_Code11_CheckDigit));
        this.spnCheckDigitVerify
                .setSelection(this.adpDigitVerify.getPosition((Code11CheckDigitVerification) paramList
                        .getValueAt(SSIParamName.Code11_CheckDigitVerification)));
        this.wgtLength.setLength(
                (Integer) paramList.getValueAt(SSIParamName.Code11_Length_Min),
                (Integer) paramList.getValueAt(SSIParamName.Code11_Length_Max));
    }
}
