package com.wsmr.app.barcode.option.SE955;

import com.wsmr.lib.dev.ATScanManager;
import com.wsmr.lib.dev.ATScanner;
import com.wsmr.lib.dev.barcode.motorola.param.SSIParamName;
import com.wsmr.lib.dev.barcode.motorola.param.SSIParamValueList;
import com.wsmr.lib.dev.barcode.params.ATScanSE955Parameter;
import com.wsmr.app.barcode.R;
import com.wsmr.app.barcode.adapter.SE955.SymbolStateListAdapter;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

public class OptionEnableStateActivity extends Activity implements
        OnClickListener {

    @SuppressWarnings("unused")
    private static final String TAG = "OptionEnableStateActivity";

    private ATScanner mScanner;
    private ATScanSE955Parameter mParam;

    private ListView lstSymbols;
    private Button btnSetOption;

    private SymbolStateListAdapter adpSymbols;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_option_enable_state);
        getActionBar().setDisplayHomeAsUpEnabled(true);

        // Initialize Scanner Instance
        this.mScanner = ATScanManager.getInstance(getApplicationContext());
        this.mParam = (ATScanSE955Parameter)this.mScanner.getParameter();

        // Initialize Widgets
        this.lstSymbols = (ListView) findViewById(R.id.symbols);
        this.adpSymbols = new SymbolStateListAdapter(this);
        this.lstSymbols.setAdapter(this.adpSymbols);

        this.btnSetOption = (Button) findViewById(R.id.set_option);
        this.btnSetOption.setOnClickListener(this);

        // Initialize Symbol State List
        initSymbolStateList();
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
            SSIParamValueList param = this.adpSymbols.getList();
            // Set Scanner Parameter
            if (this.mParam.setParams(param)) {
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
        // Get Scanner Parameter
        SSIParamValueList paramList = this.mParam.getParams(new SSIParamName[] {
                SSIParamName.UPC_A, SSIParamName.UPC_E, SSIParamName.UPC_E1,
                SSIParamName.EAN_8, SSIParamName.EAN_13, SSIParamName.Bookland_EAN,
                SSIParamName.Code128, SSIParamName.UCC_EAN_128, SSIParamName.ISBT128,
                SSIParamName.Code39, SSIParamName.Code93, SSIParamName.Code11,
                SSIParamName.I2of5, SSIParamName.D2of5, SSIParamName.Ch2of5,
                SSIParamName.Codabar, SSIParamName.MSI, SSIParamName.RSS_14,
                SSIParamName.RSS_Limited, SSIParamName.RSS_Expanded });
        this.adpSymbols.initList(paramList);
    }
}
