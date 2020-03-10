package com.wsmr.app.barcode.option.IT5x80;

import com.wsmr.lib.dev.ATScanManager;
import com.wsmr.lib.dev.ATScanner;

import com.wsmr.lib.dev.barcode.honeywell.param.HoneywellParamName;
import com.wsmr.lib.dev.barcode.honeywell.param.HoneywellParamValueList;
import com.wsmr.lib.dev.barcode.params.ATScanIT5x80Parameter;
import com.wsmr.app.barcode.R;
import com.wsmr.app.barcode.adapter.IT5x80.SymbolStateListAdapter;

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
    private ATScanIT5x80Parameter mParam;

    private ListView lstSymbols;
    private Button btnSetOpton;

    private SymbolStateListAdapter adpSymbols;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_option_enable_state);
        getActionBar().setDisplayHomeAsUpEnabled(true);

        // Initialize Scanner Instance
        this.mScanner = ATScanManager.getInstance(getApplicationContext());
        this.mParam = (ATScanIT5x80Parameter) this.mScanner.getParameter();

        // Initialize Widgets
        this.lstSymbols = (ListView) findViewById(R.id.symbols);
        this.adpSymbols = new SymbolStateListAdapter(this);
        this.lstSymbols.setAdapter(this.adpSymbols);

        this.btnSetOpton = (Button) findViewById(R.id.set_option);
        this.btnSetOpton.setOnClickListener(this);

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
            HoneywellParamValueList param = this.adpSymbols.getList();
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
        HoneywellParamValueList paramList = this.mParam.getParams(new HoneywellParamName[] {
                HoneywellParamName.Codabar, HoneywellParamName.Code39, HoneywellParamName.I2of5,
                HoneywellParamName.Code93, HoneywellParamName.R2of5, HoneywellParamName.A2of5,
                HoneywellParamName.X2of5, HoneywellParamName.Code11, HoneywellParamName.Code128,
                HoneywellParamName.Telepen, HoneywellParamName.UPCA, HoneywellParamName.UPCE0,
                HoneywellParamName.UPCE1, HoneywellParamName.EAN13, HoneywellParamName.EAN8,
                HoneywellParamName.MSI, HoneywellParamName.PlesseyCode, HoneywellParamName.RSS14,
                HoneywellParamName.RSSLimit, HoneywellParamName.RSSExp, HoneywellParamName.PosiCode,
                HoneywellParamName.TriopticCode, HoneywellParamName.CodablockF,
                HoneywellParamName.Code16K, HoneywellParamName.Code49, HoneywellParamName.PDF417,
                HoneywellParamName.MicroPDF, HoneywellParamName.ComCode, HoneywellParamName.TLC39,
                HoneywellParamName.Postnet, HoneywellParamName.Planet,
                HoneywellParamName.BritishPost, HoneywellParamName.CanadianPost,
                HoneywellParamName.KixPost, HoneywellParamName.AustralianPost,
                HoneywellParamName.JapanesePost, HoneywellParamName.ChinaPost,
                HoneywellParamName.KoreaPost, HoneywellParamName.QRCode, HoneywellParamName.Matrix,
                HoneywellParamName.MaxiCode, HoneywellParamName.AztecCode, HoneywellParamName.OCR });
        this.adpSymbols.initList(paramList);
    }
}
