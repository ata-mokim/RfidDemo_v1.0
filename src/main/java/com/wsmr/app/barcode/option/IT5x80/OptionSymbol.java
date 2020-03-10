package com.wsmr.app.barcode.option.IT5x80;

import com.wsmr.lib.dev.ATScanManager;
import com.wsmr.lib.dev.ATScanner;
import com.wsmr.lib.dev.barcode.params.ATScanIT5x80Parameter;
import com.wsmr.app.barcode.R;
import com.wsmr.app.barcode.type.IT5x80.Scan2dSymbolOption;

import android.app.Activity;
import android.os.Bundle;
import android.view.MenuItem;

public class OptionSymbol extends Activity {

    public static final String SYMBOL_TYPE = "symbol_type";

    protected ATScanner mScanner;
    protected ATScanIT5x80Parameter mParam;

    protected Scan2dSymbolOption mSymbolType;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        this.mSymbolType = (Scan2dSymbolOption) getIntent()
                .getSerializableExtra(SYMBOL_TYPE);

        String title = this.mSymbolType.toString() + " "
                + getResources().getString(R.string.symbol_config);
        this.setTitle(title);

        // Initialize Scanner Instance
        this.mScanner = ATScanManager.getInstance(getApplicationContext());
        this.mParam = (ATScanIT5x80Parameter) this.mScanner.getParameter();
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

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                this.finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }


}
