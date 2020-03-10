package com.wsmr.app.barcode.option.SE955;

import com.wsmr.lib.dev.ATScanManager;
import com.wsmr.lib.dev.ATScanner;
import com.wsmr.lib.dev.barcode.motorola.param.SSIParamName;
import com.wsmr.lib.dev.barcode.motorola.param.SSIParamValueList;
import com.wsmr.lib.dev.barcode.params.ATScanSE955Parameter;
import com.wsmr.app.barcode.R;
import com.wsmr.app.barcode.widget.SymbolLength;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Toast;

public class OptionSymbolD2of5Activity extends Activity implements
        OnClickListener {

    @SuppressWarnings("unused")
    private static final String TAG = "OptionSymbolD2of5Activity";

    private ATScanner mScanner;
    private ATScanSE955Parameter mParam;

    private SymbolLength wgtLength;
    private Button btnSetOption;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_option_1d_symbol_discrete2of5);
        getActionBar().setDisplayHomeAsUpEnabled(true);

        // Initialize Scanner Instance
        this.mScanner = ATScanManager.getInstance(getApplicationContext());
        this.mParam = (ATScanSE955Parameter) this.mScanner.getParameter();

        // Initialize Widgets
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

            paramList.add(SSIParamName.D2of5_Length_Min,
                    this.wgtLength.getLength1());
            paramList.add(SSIParamName.D2of5_Length_Max,
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
                SSIParamName.D2of5_Length_Min, SSIParamName.D2of5_Length_Max });
        this.wgtLength.setLength(
                (Integer) paramList.getValueAt(SSIParamName.D2of5_Length_Min),
                (Integer) paramList.getValueAt(SSIParamName.D2of5_Length_Max));
    }
}
