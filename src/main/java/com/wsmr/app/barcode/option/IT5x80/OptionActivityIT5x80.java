package com.wsmr.app.barcode.option.IT5x80;

import com.wsmr.lib.dev.ATScanManager;
import com.wsmr.lib.dev.ATScanner;
import com.wsmr.lib.dev.barcode.honeywell.param.HoneywellParamName;
import com.wsmr.lib.dev.barcode.honeywell.param.HoneywellParamValue;
import com.wsmr.lib.dev.barcode.honeywell.param.HoneywellParamValueList;
import com.wsmr.lib.dev.barcode.honeywell.type.OcrType;
import com.wsmr.lib.dev.barcode.params.ATScanIT5x80Parameter;
import com.wsmr.lib.diagnostics.ATLog;
import com.wsmr.app.barcode.R;
import com.wsmr.app.barcode.adapter.IT5x80.SymbolOptionListAdapter;
import com.wsmr.app.barcode.type.IT5x80.Scan2dSymbolOption;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

public class OptionActivityIT5x80 extends Activity implements OnClickListener {

    private static final int VIEW_ENABLE_STATE_SYMBOL = 1;
    private static final int VIEW_AZTECCODE_SYMBOL = 2;
    private static final int VIEW_CHINAPOST_SYMBOL = 3;
    private static final int VIEW_CODABAR_SYMBOL = 4;
    private static final int VIEW_CODABLOCKF_SYMBOL = 5;
    private static final int VIEW_CODE11_SYMBOL = 6;
    private static final int VIEW_CODE128_SYMBOL = 7;
    private static final int VIEW_CODE16K_SYMBOL = 8;
    private static final int VIEW_CODE39_SYMBOL = 9;
    private static final int VIEW_CODE49_SYMBOL = 10;
    private static final int VIEW_CODE93_SYMBOL = 11;
    private static final int VIEW_MATRIX_SYMBOL = 12;
    private static final int VIEW_EAN13_SYMBOL = 13;
    private static final int VIEW_EAN8_SYMBOL = 14;
    private static final int VIEW_COMCODE_SYMBOL = 15;
    private static final int VIEW_I2OF5_SYMBOL = 16;
    private static final int VIEW_KOREAPOST_SYMBOL = 17;
    private static final int VIEW_X2OF5_SYMBOL = 18;
    private static final int VIEW_MAXICODE_SYMBOL = 19;
    private static final int VIEW_MICROPDF_SYMBOL = 20;
    private static final int VIEW_MSI_SYMBOL = 21;
    private static final int VIEW_OCR_SYMBOL = 22;
    private static final int VIEW_PDF417_SYMBOL = 23;
    private static final int VIEW_PLANET_SYMBOL = 24;
    private static final int VIEW_PLESSEYCODE_SYMBOL = 25;
    private static final int VIEW_POSICODE_SYMBOL = 26;
    private static final int VIEW_POSTNET_SYMBOL = 27;
    private static final int VIEW_QRCODE_SYMBOL = 28;
    private static final int VIEW_RSSEXP_SYMBOL = 29;
    private static final int VIEW_A2OF5_SYMBOL = 30;
    private static final int VIEW_R2OF5_SYMBOL = 31;
    private static final int VIEW_TELEPEN_SYMBOL = 32;
    private static final int VIEW_UPCA_SYMBOL = 33;
    private static final int VIEW_UPCE0_SYMBOL = 34;
    private static final int VIEW_GENERAL = 35;

    private ATScanner mScanner;
    private ATScanIT5x80Parameter mParam;

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
        this.mParam = (ATScanIT5x80Parameter) this.mScanner.getParameter();

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

        // Disable All Widget
        enableAllWidget(true);
        isSubActivityShown = false;
        if (requestCode == VIEW_ENABLE_STATE_SYMBOL && resultCode == RESULT_OK) {
            // Initialize Symbol Option List
            initSymbolOptionList();
        }

        ATLog.e("OptionActivity", "+- onActivityResult");
    }

    @SuppressLint("ShowToast")
    @Override
    public void onClick(View v) {
        Intent intent;
        int requestCode;

        if(isSubActivityShown == true) return;
        isSubActivityShown = true;

        // Disable All Widget
        enableAllWidget(false);

        switch (v.getId()) {
            case R.id.detail_option:
                Scan2dSymbolOption option = (Scan2dSymbolOption) v.getTag();
                switch (option) {
                    case AztecCode:
                        intent = new Intent(this, OptionSymbolComplexLength.class);
                        requestCode = VIEW_AZTECCODE_SYMBOL;
                        break;
                    case ChinaPost:
                        intent = new Intent(this, OptionSymbolLength.class);
                        requestCode = VIEW_CHINAPOST_SYMBOL;
                        break;
                    case Codabar:
                        intent = new Intent(this, OptionSymbolComplexLength.class);
                        requestCode = VIEW_CODABAR_SYMBOL;
                        break;
                    case CodablockF:
                        intent = new Intent(this, OptionSymbolLength.class);
                        requestCode = VIEW_CODABLOCKF_SYMBOL;
                        break;
                    case Code11:
                        intent = new Intent(this, OptionSymbolComplexLength.class);
                        requestCode = VIEW_CODE11_SYMBOL;
                        break;
                    case Code128:
                        intent = new Intent(this, OptionSymbolComplexLength.class);
                        requestCode = VIEW_CODE128_SYMBOL;
                        break;
                    case Code16K:
                        intent = new Intent(this, OptionSymbolLength.class);
                        requestCode = VIEW_CODE16K_SYMBOL;
                        break;
                    case Code39:
                        intent = new Intent(this, OptionSymbolComplexLength.class);
                        requestCode = VIEW_CODE39_SYMBOL;
                        break;
                    case Code49:
                        intent = new Intent(this, OptionSymbolLength.class);
                        requestCode = VIEW_CODE49_SYMBOL;
                        break;
                    case Code93:
                        intent = new Intent(this, OptionSymbolComplexLength.class);
                        requestCode = VIEW_CODE93_SYMBOL;
                        break;
                    case Matrix:
                        intent = new Intent(this, OptionSymbolLength.class);
                        requestCode = VIEW_MATRIX_SYMBOL;
                        break;
                    case EAN13:
                        intent = new Intent(this, OptionSymbolAddenda.class);
                        requestCode = VIEW_EAN13_SYMBOL;
                        break;
                    case EAN8:
                        intent = new Intent(this, OptionSymbolAddenda.class);
                        requestCode = VIEW_EAN8_SYMBOL;
                        break;
                    case ComCode:
                        intent = new Intent(this, OptionSymbolComplexLength.class);
                        requestCode = VIEW_COMCODE_SYMBOL;
                        break;
                    case I2of5:
                        intent = new Intent(this, OptionSymbolComplexLength.class);
                        requestCode = VIEW_I2OF5_SYMBOL;
                        break;
                    case KoreaPost:
                        intent = new Intent(this, OptionSymbolLength.class);
                        requestCode = VIEW_KOREAPOST_SYMBOL;
                        break;
                    case X2of5:
                        intent = new Intent(this, OptionSymbolLength.class);
                        requestCode = VIEW_X2OF5_SYMBOL;
                        break;
                    case MaxiCode:
                        intent = new Intent(this, OptionSymbolLength.class);
                        requestCode = VIEW_MAXICODE_SYMBOL;
                        break;
                    case MicroPDF:
                        intent = new Intent(this, OptionSymbolLength.class);
                        requestCode = VIEW_MICROPDF_SYMBOL;
                        break;
                    case MSI:
                        intent = new Intent(this, OptionSymbolComplexLength.class);
                        requestCode = VIEW_MSI_SYMBOL;
                        break;
                    case OCR:
                        intent = new Intent(this, OptionSymbolOCR.class);
                        requestCode = VIEW_OCR_SYMBOL;
                        break;
                    case PDF417:
                        intent = new Intent(this, OptionSymbolLength.class);
                        requestCode = VIEW_PDF417_SYMBOL;
                        break;
                    case Planet:
                        intent = new Intent(this, OptionSymbolCheckDigit.class);
                        requestCode = VIEW_PLANET_SYMBOL;
                        break;
                    case PlesseyCode:
                        intent = new Intent(this, OptionSymbolLength.class);
                        requestCode = VIEW_PLESSEYCODE_SYMBOL;
                        break;
                    case PosiCode:
                        intent = new Intent(this, OptionSymbolComplexLength.class);
                        requestCode = VIEW_POSICODE_SYMBOL;
                        break;
                    case Postnet:
                        intent = new Intent(this, OptionSymbolCheckDigit.class);
                        requestCode = VIEW_POSTNET_SYMBOL;
                        break;
                    case QRCode:
                        intent = new Intent(this, OptionSymbolLength.class);
                        requestCode = VIEW_QRCODE_SYMBOL;
                        break;
                    case RSSExp:
                        intent = new Intent(this, OptionSymbolLength.class);
                        requestCode = VIEW_RSSEXP_SYMBOL;
                        break;
                    case A2of5:
                        intent = new Intent(this, OptionSymbolLength.class);
                        requestCode = VIEW_A2OF5_SYMBOL;
                        break;
                    case R2of5:
                        intent = new Intent(this, OptionSymbolLength.class);
                        requestCode = VIEW_R2OF5_SYMBOL;
                        break;
                    case Telepen:
                        intent = new Intent(this, OptionSymbolComplexLength.class);
                        requestCode = VIEW_TELEPEN_SYMBOL;
                        break;
                    case UPCA:
                        intent = new Intent(this, OptionSymbolAddenda.class);
                        requestCode = VIEW_UPCA_SYMBOL;
                        break;
                    case UPCE0:
                        intent = new Intent(this, OptionSymbolAddenda.class);
                        requestCode = VIEW_UPCE0_SYMBOL;
                        break;
                    default:
                        return;
                }
                intent.putExtra(OptionSymbol.SYMBOL_TYPE, option);
                startActivityForResult(intent, requestCode);
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
                    Toast.makeText(this,
                            R.string.failed_to_disable_all_symbologies,
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
        HoneywellParamValueList paramList = this.mParam.getParams(new HoneywellParamName[] {
                HoneywellParamName.AztecCode, HoneywellParamName.ChinaPost,
                HoneywellParamName.Codabar, HoneywellParamName.CodablockF,
                HoneywellParamName.Code11, HoneywellParamName.Code128, HoneywellParamName.Code16K,
                HoneywellParamName.Code39, HoneywellParamName.Code49, HoneywellParamName.Code93,
                HoneywellParamName.Matrix, HoneywellParamName.EAN13, HoneywellParamName.EAN8,
                HoneywellParamName.ComCode, HoneywellParamName.I2of5, HoneywellParamName.KoreaPost,
                HoneywellParamName.X2of5, HoneywellParamName.MaxiCode, HoneywellParamName.MicroPDF,
                HoneywellParamName.MSI, HoneywellParamName.OCR, HoneywellParamName.PDF417,
                HoneywellParamName.Planet, HoneywellParamName.PlesseyCode,
                HoneywellParamName.PosiCode, HoneywellParamName.Postnet, HoneywellParamName.QRCode,
                HoneywellParamName.RSSExp, HoneywellParamName.A2of5, HoneywellParamName.R2of5,
                HoneywellParamName.Telepen, HoneywellParamName.UPCA, HoneywellParamName.UPCE0 });
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
        HoneywellParamValueList paramList = new HoneywellParamValueList(new HoneywellParamValue[] {
                new HoneywellParamValue(HoneywellParamName.AztecCode, enabled),
                new HoneywellParamValue(HoneywellParamName.ChinaPost, enabled),
                new HoneywellParamValue(HoneywellParamName.Codabar, enabled),
                new HoneywellParamValue(HoneywellParamName.CodablockF, enabled),
                new HoneywellParamValue(HoneywellParamName.Code11, enabled),
                new HoneywellParamValue(HoneywellParamName.Code128, enabled),
                new HoneywellParamValue(HoneywellParamName.Code16K, enabled),
                new HoneywellParamValue(HoneywellParamName.Code39, enabled),
                new HoneywellParamValue(HoneywellParamName.Code49, enabled),
                new HoneywellParamValue(HoneywellParamName.Code93, enabled),
                new HoneywellParamValue(HoneywellParamName.Matrix, enabled),
                new HoneywellParamValue(HoneywellParamName.EAN13, enabled),
                new HoneywellParamValue(HoneywellParamName.EAN8, enabled),
                new HoneywellParamValue(HoneywellParamName.ComCode, enabled),
                new HoneywellParamValue(HoneywellParamName.I2of5, enabled),
                new HoneywellParamValue(HoneywellParamName.KoreaPost, enabled),
                new HoneywellParamValue(HoneywellParamName.X2of5, enabled),
                new HoneywellParamValue(HoneywellParamName.MaxiCode, enabled),
                new HoneywellParamValue(HoneywellParamName.MicroPDF, enabled),
                new HoneywellParamValue(HoneywellParamName.MSI, enabled),
                new HoneywellParamValue(HoneywellParamName.OCR, enabled ? OcrType.OcrA : OcrType.OffAll),
                new HoneywellParamValue(HoneywellParamName.PDF417, enabled),
                new HoneywellParamValue(HoneywellParamName.Planet, enabled),
                new HoneywellParamValue(HoneywellParamName.PlesseyCode, enabled),
                new HoneywellParamValue(HoneywellParamName.PosiCode, enabled),
                new HoneywellParamValue(HoneywellParamName.Postnet, enabled),
                new HoneywellParamValue(HoneywellParamName.QRCode, enabled),
                new HoneywellParamValue(HoneywellParamName.RSSExp, enabled),
                new HoneywellParamValue(HoneywellParamName.A2of5, enabled),
                new HoneywellParamValue(HoneywellParamName.R2of5, enabled),
                new HoneywellParamValue(HoneywellParamName.Telepen, enabled),
                new HoneywellParamValue(HoneywellParamName.UPCA, enabled),
                new HoneywellParamValue(HoneywellParamName.UPCE0, enabled),
                new HoneywellParamValue(HoneywellParamName.UPCE1, enabled),
                new HoneywellParamValue(HoneywellParamName.RSS14, enabled),
                new HoneywellParamValue(HoneywellParamName.RSSLimit, enabled),
                new HoneywellParamValue(HoneywellParamName.TriopticCode, enabled),
                new HoneywellParamValue(HoneywellParamName.TLC39, enabled),
                new HoneywellParamValue(HoneywellParamName.BritishPost, enabled),
                new HoneywellParamValue(HoneywellParamName.CanadianPost, enabled),
                new HoneywellParamValue(HoneywellParamName.KixPost, enabled),
                new HoneywellParamValue(HoneywellParamName.AustralianPost, enabled),
                new HoneywellParamValue(HoneywellParamName.JapanesePost, enabled) });
        return this.mParam.setParams(paramList);
    }
}
