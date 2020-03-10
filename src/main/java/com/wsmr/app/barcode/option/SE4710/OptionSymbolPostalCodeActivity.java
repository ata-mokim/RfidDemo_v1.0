package com.wsmr.app.barcode.option.SE4710;

import com.wsmr.lib.dev.ATScanManager;
import com.wsmr.lib.dev.ATScanner;
import com.wsmr.lib.dev.barcode.motorola.param.SSIParamName;
import com.wsmr.lib.dev.barcode.motorola.param.SSIParamValueList;
import com.wsmr.lib.dev.barcode.params.ATScanSE4710Parameter;
import com.wsmr.app.barcode.R;
import com.wsmr.app.barcode.type.SE4710.Scan1dSymbolOption;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.Toast;

public class OptionSymbolPostalCodeActivity extends Activity implements
        OnClickListener {

    @SuppressWarnings("unused")
    private static final String TAG = "OptionSymbolPostalCodeActivity";
    private ATScanner mScanner;
    private ATScanSE4710Parameter mParam;
    private Scan1dSymbolOption symbol_type;

    private CheckBox chkSymbolEnable;
    private CheckBox chkTransmitCheckDigit;
    private Button btnSetOption;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_option_2d_symbol_postal);
        getActionBar().setDisplayHomeAsUpEnabled(true);
        this.symbol_type = (Scan1dSymbolOption)getIntent().getSerializableExtra("Symbol_Type");

        if(symbol_type == Scan1dSymbolOption.US_Postnet){
            this.setTitle(R.string.symbol_uspostnet_name);
        }else if(symbol_type == Scan1dSymbolOption.US_Planet){
            this.setTitle(R.string.symbol_usplanet_name);
        }else if(symbol_type == Scan1dSymbolOption.UK_Postal){
            this.setTitle(R.string.symbol_ukpostal_name);
        }else{

        }

        this.chkSymbolEnable = (CheckBox) findViewById(R.id.symbol_enable);
        this.chkTransmitCheckDigit = (CheckBox) findViewById(R.id.transmit_checkdigit);
        this.btnSetOption = (Button) findViewById(R.id.set_option);
        this.btnSetOption.setOnClickListener(this);
        // Initialize Scanner Instance
        this.mScanner = ATScanManager.getInstance(getApplicationContext());
        this.mParam = (ATScanSE4710Parameter)this.mScanner.getParameter();


        initSymbolStateList();
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

            if(symbol_type == Scan1dSymbolOption.US_Postnet){
                paramList.add(SSIParamName.US_Postnet, this.chkSymbolEnable.isChecked());
                paramList.add(SSIParamName.Transmit_US_Postal_Check_Digit, this.chkTransmitCheckDigit.isChecked());
            }else if(symbol_type == Scan1dSymbolOption.US_Planet){
                paramList.add(SSIParamName.US_Planet, this.chkSymbolEnable.isChecked());
                paramList.add(SSIParamName.Transmit_US_Postal_Check_Digit, this.chkTransmitCheckDigit.isChecked());
            }else if(symbol_type == Scan1dSymbolOption.UK_Postal){
                paramList.add(SSIParamName.UK_Postal, this.chkSymbolEnable.isChecked());
                paramList.add(SSIParamName.Transmit_UK_Postal_Check_Digit, this.chkTransmitCheckDigit.isChecked());
            }else{

            }

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

    // Initialize Symbol State List
    private void initSymbolStateList() {
        SSIParamValueList paramList = null;

        if(symbol_type == Scan1dSymbolOption.US_Postnet){
            paramList = this.mParam.getParams(new SSIParamName[]{SSIParamName.US_Postnet,
                    SSIParamName.Transmit_US_Postal_Check_Digit});
            this.chkSymbolEnable.setChecked((Boolean) paramList.getValueAt(SSIParamName.US_Postnet));
            this.chkTransmitCheckDigit.setChecked((Boolean) paramList.getValueAt(SSIParamName.Transmit_US_Postal_Check_Digit));

        }else if(symbol_type == Scan1dSymbolOption.US_Planet){
            paramList = this.mParam.getParams(new SSIParamName[]{SSIParamName.US_Planet,
                    SSIParamName.Transmit_US_Postal_Check_Digit});
            this.chkSymbolEnable.setChecked((Boolean) paramList.getValueAt(SSIParamName.US_Planet));
            this.chkTransmitCheckDigit.setChecked((Boolean) paramList.getValueAt(SSIParamName.Transmit_US_Postal_Check_Digit));

        }else if(symbol_type == Scan1dSymbolOption.UK_Postal){
            paramList = this.mParam.getParams(new SSIParamName[]{SSIParamName.UK_Postal,
                    SSIParamName.Transmit_UK_Postal_Check_Digit});
            this.chkSymbolEnable.setChecked((Boolean) paramList.getValueAt(SSIParamName.UK_Postal));
            this.chkTransmitCheckDigit.setChecked((Boolean) paramList.getValueAt(SSIParamName.Transmit_UK_Postal_Check_Digit));

        }else{

        }
    }


}
