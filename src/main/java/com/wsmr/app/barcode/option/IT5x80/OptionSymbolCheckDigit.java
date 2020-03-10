package com.wsmr.app.barcode.option.IT5x80;

import com.wsmr.app.barcode.R;
import com.wsmr.lib.dev.ATScanManager;
import com.wsmr.lib.dev.barcode.honeywell.param.HoneywellParamName;
import com.wsmr.lib.dev.barcode.honeywell.param.HoneywellParamValue;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.Toast;

public class OptionSymbolCheckDigit extends OptionSymbol implements
        OnClickListener {

    private CheckBox chkCheckDigitTransmit;
    private Button btnSetOption;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_option_2d_symbol_check_digit);
        getActionBar().setDisplayHomeAsUpEnabled(true);

        // Initialize Widgets
        this.chkCheckDigitTransmit = (CheckBox) findViewById(R.id.check_digit_transmit);
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
            HoneywellParamValue value = null;

            switch (this.mSymbolType) {
                case Postnet:
                    value = new HoneywellParamValue(HoneywellParamName.PostnetCheckDigit,
                            this.chkCheckDigitTransmit.isChecked());
                    break;
                case Planet:
                    value = new HoneywellParamValue(HoneywellParamName.PlanetCheckDigit,
                            this.chkCheckDigitTransmit.isChecked());
                    break;
                default:
                    return;
            }

            if (this.mParam.setParam(value)) {
                this.setResult(RESULT_OK);
                this.finish();
            } else {
                Toast.makeText(this, R.string.faile_to_set_symbologies,
                        Toast.LENGTH_LONG).show();
            }
        }
    }

    // Load Scanner Symbol Detail Option
    private void loadOption() {
        switch (this.mSymbolType) {
            case Postnet:
                this.chkCheckDigitTransmit.setChecked((Boolean) this.mParam
                        .getParam(HoneywellParamName.PostnetCheckDigit));
                break;
            case Planet:
                this.chkCheckDigitTransmit.setChecked((Boolean) this.mParam
                        .getParam(HoneywellParamName.PlanetCheckDigit));
                break;
            default:
                break;
        }
    }
}
