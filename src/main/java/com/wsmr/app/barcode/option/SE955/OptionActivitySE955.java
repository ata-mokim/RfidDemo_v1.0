package com.wsmr.app.barcode.option.SE955;

import com.wsmr.lib.dev.ATScanManager;
import com.wsmr.lib.dev.ATScanner;
import com.wsmr.lib.dev.barcode.motorola.param.SSIParamName;
import com.wsmr.lib.dev.barcode.motorola.param.SSIParamValue;
import com.wsmr.lib.dev.barcode.motorola.param.SSIParamValueList;
import com.wsmr.lib.dev.barcode.params.ATScanSE955Parameter;
import com.wsmr.app.barcode.R;
import com.wsmr.app.barcode.adapter.SE955.SymbolOptionListAdapter;
import com.wsmr.app.barcode.type.SE955.Scan1dSymbolOption;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

public class OptionActivitySE955 extends Activity implements Button.OnClickListener {

    @SuppressWarnings("unused")
    private static final String TAG = "Option1dActivity";

    private static final int VIEW_ENABLE_STATE_SYMBOL = 1;
    private static final int VIEW_UPC_EAN_SYMBOL = 2;
    private static final int VIEW_CODE39_SYMBOL = 3;
    private static final int VIEW_CODE93_SYMBOL = 4;
    private static final int VIEW_CODE11_SYMBOL = 5;
    private static final int VIEW_I2OF5_SYMBOL = 6;
    private static final int VIEW_D2OF5_SYMBOL = 7;
    private static final int VIEW_CODABAR_SYMBOL = 8;
    private static final int VIEW_MSI_SYMBOL = 9;
    private static final int VIEW_GENERAL = 10;

    private ATScanner mScanner;
    private ATScanSE955Parameter mParam;

    private ListView lstOptions;
    private Button btnEnableStatus;
    private Button btnDefaultAllSymbol;
    private Button btnDisableAllSymbol;
    private Button btnEnableAllSymbol;
    private Button btnGeneral;

    private SymbolOptionListAdapter adpOptions;
    private boolean isSubActivityShown;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_option_main);
        getActionBar().setDisplayHomeAsUpEnabled(true);

        // Initialize Scanner Instance
        this.mScanner = ATScanManager.getInstance(getApplicationContext());
        this.mParam = (ATScanSE955Parameter)this.mScanner.getParameter();

        // Initialize Widgets
        this.lstOptions = (ListView) findViewById(R.id.symbol_list);
        this.adpOptions = new SymbolOptionListAdapter(this);
        this.lstOptions.setAdapter(this.adpOptions);

        this.btnEnableStatus = (Button) findViewById(R.id.set_enable_status);
        this.btnEnableStatus.setOnClickListener(this);

        this.btnDefaultAllSymbol = (Button) findViewById(R.id.default_all_symbologies);
        this.btnDefaultAllSymbol.setOnClickListener(this);

        this.btnDisableAllSymbol = (Button) findViewById(R.id.disable_all_symbologies);
        this.btnDisableAllSymbol.setOnClickListener(this);

        this.btnEnableAllSymbol = (Button) findViewById(R.id.enable_all_symbologies);
        this.btnEnableAllSymbol.setOnClickListener(this);

        this.btnGeneral = (Button) findViewById(R.id.gerneral_config);
        this.btnGeneral.setOnClickListener(this);

        // Initialize Symbol Option List
        initSymbolOptionList();
        isSubActivityShown = false;
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
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Disable All Widget
        enableAllWidget(true);
        isSubActivityShown = false;
        if (requestCode == VIEW_ENABLE_STATE_SYMBOL && resultCode == RESULT_OK) {
            // Initialize Symbol Option List
            initSymbolOptionList();
        }
    }

    @SuppressLint("ShowToast")
    @Override
    public void onClick(View v) {
        Intent intent;

        if(isSubActivityShown == true) return;
        isSubActivityShown = true;

        // Disable All Widget
        enableAllWidget(false);

        switch (v.getId()) {
            case R.id.detail_option:
                Scan1dSymbolOption option = (Scan1dSymbolOption) v.getTag();
                switch (option) {
                    case UpcEan:
                        intent = new Intent(this, OptionSymbolUpcEanActivity.class);
                        startActivityForResult(intent, VIEW_UPC_EAN_SYMBOL);
                        break;
                    case Code39:
                        intent = new Intent(this, OptionSymbolCode39Activity.class);
                        startActivityForResult(intent, VIEW_CODE39_SYMBOL);
                        break;
                    case Code93:
                        intent = new Intent(this, OptionSymbolCode93Activity.class);
                        startActivityForResult(intent, VIEW_CODE93_SYMBOL);
                        break;
                    case Code11:
                        intent = new Intent(this, OptionSymbolCode11Activity.class);
                        startActivityForResult(intent, VIEW_CODE11_SYMBOL);
                        break;
                    case I2of5:
                        intent = new Intent(this, OptionSymbolI2of5Activity.class);
                        startActivityForResult(intent, VIEW_I2OF5_SYMBOL);
                        break;
                    case D2of5:
                        intent = new Intent(this, OptionSymbolD2of5Activity.class);
                        startActivityForResult(intent, VIEW_D2OF5_SYMBOL);
                        break;
                    case Codabar:
                        intent = new Intent(this, OptionSymbolCodabarActivity.class);
                        startActivityForResult(intent, VIEW_CODABAR_SYMBOL);
                        break;
                    case Msi:
                        intent = new Intent(this, OptionSymbolMsiActivity.class);
                        startActivityForResult(intent, VIEW_MSI_SYMBOL);
                        break;
                    default:
                        return;
                }
                break;
            case R.id.set_enable_status:
                intent = new Intent(this, OptionEnableStateActivity.class);
                startActivityForResult(intent, VIEW_ENABLE_STATE_SYMBOL);
                break;
            case R.id.default_all_symbologies:
                if (this.mParam.defaultParams()) {
                    Toast.makeText(this, R.string.succeeded_to_default_all_symbologies,
                            Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(this, R.string.failed_to_default_all_parameter,
                            Toast.LENGTH_LONG).show();
                }
                // Initialize Symbol Option List
                initSymbolOptionList();
                enableAllWidget(true);
                isSubActivityShown = false;
                break;
            case R.id.disable_all_symbologies:
                if (enableAllSymbol(false)) {
                    Toast.makeText(this, R.string.disabled_all_symbologies,
                            Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(this, R.string.failed_to_disable_all_symbologies,
                            Toast.LENGTH_LONG).show();
                }
                // Initialize Symbol Option List
                initSymbolOptionList();
                enableAllWidget(true);
                isSubActivityShown = false;
                break;
            case R.id.enable_all_symbologies:
                if (enableAllSymbol(true)) {
                    Toast.makeText(this, R.string.enabled_all_symbologies,
                            Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(this, R.string.failed_to_enable_all_symbologies,
                            Toast.LENGTH_LONG).show();
                }
                // Initialize Symbol Option List
                initSymbolOptionList();
                enableAllWidget(true);
                isSubActivityShown = false;
                break;
            case R.id.gerneral_config:
                intent = new Intent(this, OptionGeneralConfigActivity.class);
                startActivityForResult(intent, VIEW_GENERAL);
                break;
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

    // Initialize Symbol Option List
    @SuppressLint("ShowToast")
    private void initSymbolOptionList() {
        SSIParamValueList paramList = this.mParam.getParams(new SSIParamName[] {
                SSIParamName.UPC_A, SSIParamName.UPC_E, SSIParamName.UPC_E1,
                SSIParamName.EAN_8, SSIParamName.EAN_13, SSIParamName.Bookland_EAN,
                SSIParamName.Code39, SSIParamName.Code93, SSIParamName.Code11,
                SSIParamName.I2of5, SSIParamName.D2of5, SSIParamName.Codabar,
                SSIParamName.MSI });
        if (paramList.getCount() <= 0) {
            enableAllWidget(false);
            Toast.makeText(this, R.string.failed_to_get_symbol_parameter,
                    Toast.LENGTH_LONG).show();
            return;
        }
        this.adpOptions.updateList(paramList);
    }

    // Enable/Disable All Widget
    private void enableAllWidget(boolean enabled) {
        this.lstOptions.setEnabled(enabled);
        this.btnEnableStatus.setEnabled(enabled);
        this.btnDefaultAllSymbol.setEnabled(enabled);
        this.btnDisableAllSymbol.setEnabled(enabled);
        this.btnEnableAllSymbol.setEnabled(enabled);
        this.btnGeneral.setEnabled(enabled);
    }

    // Enable/Disable All Symbol
    private boolean enableAllSymbol(boolean enabled) {
        SSIParamValueList paramList = new SSIParamValueList(new SSIParamValue[] {
                new SSIParamValue(SSIParamName.UPC_A, enabled),
                new SSIParamValue(SSIParamName.UPC_E, enabled),
                new SSIParamValue(SSIParamName.UPC_E1, enabled),
                new SSIParamValue(SSIParamName.EAN_8, enabled),
                new SSIParamValue(SSIParamName.EAN_13, enabled),
                new SSIParamValue(SSIParamName.Bookland_EAN, enabled),
                new SSIParamValue(SSIParamName.Code128, enabled),
                new SSIParamValue(SSIParamName.UCC_EAN_128, enabled),
                new SSIParamValue(SSIParamName.ISBT128, enabled),
                new SSIParamValue(SSIParamName.Code39, enabled),
                new SSIParamValue(SSIParamName.Code93, enabled),
                new SSIParamValue(SSIParamName.Code11, enabled),
                new SSIParamValue(SSIParamName.I2of5, enabled),
                new SSIParamValue(SSIParamName.D2of5, enabled),
                new SSIParamValue(SSIParamName.Ch2of5, enabled),
                new SSIParamValue(SSIParamName.Codabar, enabled),
                new SSIParamValue(SSIParamName.MSI, enabled),
                new SSIParamValue(SSIParamName.RSS_14, enabled),
                new SSIParamValue(SSIParamName.RSS_Limited, enabled),
                new SSIParamValue(SSIParamName.RSS_Expanded, enabled) });
        return this.mParam.setParams(paramList);
    }
}
