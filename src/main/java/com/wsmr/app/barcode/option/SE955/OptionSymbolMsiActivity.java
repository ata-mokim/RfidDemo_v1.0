package com.wsmr.app.barcode.option.SE955;

import com.wsmr.lib.dev.ATScanManager;
import com.wsmr.lib.dev.ATScanner;
import com.wsmr.lib.dev.barcode.motorola.param.SSIParamName;
import com.wsmr.lib.dev.barcode.motorola.param.SSIParamValueList;
import com.wsmr.lib.dev.barcode.params.ATScanSE955Parameter;
import com.wsmr.lib.dev.barcode.motorola.type.MsiCheckDigitAlgorithm;
import com.wsmr.lib.dev.barcode.motorola.type.MsiCheckDigits;
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

public class OptionSymbolMsiActivity extends Activity implements
        OnClickListener {

    @SuppressWarnings("unused")
    private static final String TAG = "OptionSymbolMsiActivity";

    private ATScanner mScanner;
    private ATScanSE955Parameter mParam;

    private Spinner spnCheckDigits;
    private CheckBox chkTransmit;
    private Spinner spnAlgorithm;
    private SymbolLength wgtLength;
    private Button btnSetOption;

    private ValueAdapter<MsiCheckDigits> adpCheckDigits;
    private ValueAdapter<MsiCheckDigitAlgorithm> adpAlgorithm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_option_1d_symbol_msi);
        getActionBar().setDisplayHomeAsUpEnabled(true);

        // Initialize Scanner Instance
        this.mScanner = ATScanManager.getInstance(getApplicationContext());
        this.mParam = (ATScanSE955Parameter) this.mScanner.getParameter();

        // Initialize Widgets
        this.spnCheckDigits = (Spinner) findViewById(R.id.msi_check_digits);
        this.adpCheckDigits = new ValueAdapter<MsiCheckDigits>(this);
        for (MsiCheckDigits item : MsiCheckDigits.values()) {
            this.adpCheckDigits.add(new ValueItem<MsiCheckDigits>(item
                    .toString(), item));
        }
        this.spnCheckDigits.setAdapter(this.adpCheckDigits);
        this.chkTransmit = (CheckBox) findViewById(R.id.transmit_msi_check_digit);
        this.spnAlgorithm = (Spinner) findViewById(R.id.msi_check_digit_algorithm);
        this.adpAlgorithm = new ValueAdapter<MsiCheckDigitAlgorithm>(this);
        for (MsiCheckDigitAlgorithm item : MsiCheckDigitAlgorithm.values()) {
            this.adpAlgorithm.add(new ValueItem<MsiCheckDigitAlgorithm>(item
                    .toString(), item));
        }
        this.spnAlgorithm.setAdapter(this.adpAlgorithm);
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

            paramList.add(SSIParamName.MSI_CheckDigit, MsiCheckDigits
                    .valueOf((byte) this.spnCheckDigits
                            .getSelectedItemPosition()));
            paramList.add(SSIParamName.Transmit_MSI_CheckDigit,
                    this.chkTransmit.isChecked());
            paramList.add(SSIParamName.MSI_CheckDigitAlgorithm,
                    MsiCheckDigitAlgorithm.valueOf((byte) this.spnAlgorithm
                            .getSelectedItemPosition()));
            paramList.add(SSIParamName.MSI_Length_Min,
                    this.wgtLength.getLength1());
            paramList.add(SSIParamName.MSI_Length_Max,
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
                SSIParamName.MSI_CheckDigit,
                SSIParamName.Transmit_MSI_CheckDigit,
                SSIParamName.MSI_CheckDigitAlgorithm,
                SSIParamName.MSI_Length_Min, SSIParamName.MSI_Length_Max });
        this.spnCheckDigits.setSelection(this.adpCheckDigits
                .getPosition((MsiCheckDigits) paramList
                        .getValueAt(SSIParamName.MSI_CheckDigit)));
        this.chkTransmit.setChecked((Boolean) paramList
                .getValueAt(SSIParamName.Transmit_MSI_CheckDigit));
        this.spnAlgorithm.setSelection(this.adpAlgorithm
                .getPosition((MsiCheckDigitAlgorithm) paramList
                        .getValueAt(SSIParamName.MSI_CheckDigitAlgorithm)));
        this.wgtLength.setLength(
                (Integer) paramList.getValueAt(SSIParamName.MSI_Length_Min),
                (Integer) paramList.getValueAt(SSIParamName.MSI_Length_Max));
    }
}
