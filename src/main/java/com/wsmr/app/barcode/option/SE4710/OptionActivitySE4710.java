package com.wsmr.app.barcode.option.SE4710;

import com.wsmr.lib.dev.ATScanManager;
import com.wsmr.lib.dev.ATScanner;
import com.wsmr.lib.dev.barcode.motorola.param.SSIParamName;
import com.wsmr.lib.dev.barcode.motorola.param.SSIParamValue;
import com.wsmr.lib.dev.barcode.motorola.param.SSIParamValueList;
import com.wsmr.lib.dev.barcode.params.ATScanSE4710Parameter;
import com.wsmr.lib.diagnostics.ATLog;
import com.wsmr.lib.dev.barcode.motorola.type.UpcCompositeMode;
import com.wsmr.app.barcode.R;
import com.wsmr.app.barcode.adapter.SE4710.SymbolOptionListAdapter;
import com.wsmr.app.barcode.type.SE4710.Scan1dSymbolOption;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

public class OptionActivitySE4710 extends Activity implements Button.OnClickListener {
    private static final String TAG = "Option2dActivitySE4710";

    public static final int VIEW_ENABLE_STATE_SYMBOL = 1;
    public static final int VIEW_UPC_EAN_SYMBOL = 2;
    public static final int VIEW_CODE39_SYMBOL = 3;
    public static final int VIEW_CODE93_SYMBOL = 4;
    public static final int VIEW_CODE11_SYMBOL = 5;
    public static final int VIEW_I2OF5_SYMBOL = 6;
    public static final int VIEW_D2OF5_SYMBOL = 7;
    public static final int VIEW_CODABAR_SYMBOL = 8;
    public static final int VIEW_MSI_SYMBOL = 9;
    public static final int VIEW_GENERAL = 10;
    public static final int VIEW_CODE128_SYMBOL = 11;
    public static final int VIEW_AZTEC_SYMBOL = 12;
    public static final int VIEW_DATA_MATRIX_SYMBOL = 13;
    public static final int VIEW_MAXICODE_SYMBOL = 14;
    public static final int VIEW_MICRO_PDF417_SYMBOL = 15;
    public static final int VIEW_QR_CODE_SYMBOL = 16;
    public static final int VIEW_MATRIX_2OF5_SYMBOL = 17;
    public static final int VIEW_KOREA_3OF5_SYMBOL = 18;
    public static final int VIEW_US_POSTNET_SYMBOL = 19;
    public static final int VIEW_US_PLANET_SYMBOL = 20;
    public static final int VIEW_JAPAN_POSTAL_SYMBOL = 21;
    public static final int VIEW_AUSTRALIA_POSTAL_SYMBOL = 22;
    public static final int VIEW_NETHERLAND_KIX_CODE_SYMBOL = 23;
    public static final int VIEW_USPS_4CB_SYMBOL = 24;
    public static final int VIEW_UPU_FICS_SYMBOL = 25;
    public static final int VIEW_COMPOSITE_CC_C_SYMBOL = 26;
    public static final int VIEW_COMPOSITE_CC_AB_SYMBOL = 27;
    public static final int VIEW_COMPOSITE_TLC_39_SYMBOL = 28;
    public static final int VIEW_ISSN_EAN_SYMBOL = 29;
    public static final int VIEW_GS1_128_SYMBOL = 30;
    public static final int VIEW_ISBT_128_SYMBOL = 31;
    public static final int VIEW_MICRO_QR_SYMBOL = 32;
    public static final int VIEW_PDF417_SYMBOL = 33;
    public static final int VIEW_USPS_4CB_ONE_CODE_INTELIGENT_MAIL_SYMBOL = 34;
    public static final int VIEW_UPU_FICS_POSTRAL_SYMBOL = 35;
    public static final int VIEW_HAN_XIN_SYMBOL = 36;
    public static final int VIEW_CHINESE_2OF5_SYMBOL = 37;
    public static final int VIEW_RSS_14_SYMBOL = 38;
    public static final int VIEW_UK_POSTAL_SYMBOL = 39;
    private boolean isSubActivityShown;
    private ATScanner mScanner;
    private ATScanSE4710Parameter mParam;

    private ListView lstOptions;
    private Button btnEnableStatus;
    private Button btnDefaultAllSymbol;
    private Button btnDisableAllSymbol;
    private Button btnEnableAllSymbol;
    private Button btnGeneral;

    private SymbolOptionListAdapter adpOptions;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_option_main);
        getActionBar().setDisplayHomeAsUpEnabled(true);

        // Initialize Scanner Instance
        this.mScanner = ATScanManager.getInstance(getApplicationContext());
        this.mParam = (ATScanSE4710Parameter) this.mScanner.getParameter();

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

        ATLog.e(TAG, String.format("requestCode:%d,  resultCode:%d", requestCode, resultCode));

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
        int nRequestCode = -1;
        if(isSubActivityShown == true) return;
        isSubActivityShown = true;

        switch (v.getId()) {
            case R.id.detail_option:
                Scan1dSymbolOption option = (Scan1dSymbolOption) v.getTag();
                nRequestCode = symbolIndex(option);

                ATLog.d(TAG, "Symbol : %s", option.toString());

                switch (option) {
                    //case Ch2of5:
                    //break;
                    case Code11:
                        intent = new Intent(this, OptionSymbolCode11Activity.class);
                        startActivityForResult(intent, nRequestCode);
                        break;
                    case Code39:
                        intent = new Intent(this, OptionSymbolCode39Activity.class);
                        startActivityForResult(intent, nRequestCode);
                        break;
                    case Code93:
                        intent = new Intent(this, OptionSymbolCode93Activity.class);
                        startActivityForResult(intent, nRequestCode);
                        break;
                    case D2of5:
                        intent = new Intent(this, OptionSymbolD2of5Activity.class);
                        startActivityForResult(intent, nRequestCode);
                        break;
                    case Codabar:
                        intent = new Intent(this, OptionSymbolCodabarActivity.class);
                        startActivityForResult(intent, nRequestCode);
                        break;
                    case I2of5:
                        intent = new Intent(this, OptionSymbolI2of5Activity.class);
                        startActivityForResult(intent, nRequestCode);
                        break;
                    case Msi:
                        intent = new Intent(this, OptionSymbolMsiActivity.class);
                        startActivityForResult(intent, nRequestCode);
                        break;
                    case UpcEan:
                        intent = new Intent(this, OptionSymbolUpcEanActivity.class);
                        startActivityForResult(intent, nRequestCode);
                        break;
                    case Code128:
                    case Matrix2of5:
                        intent = new Intent(this, OptionSymbolCode128Activity.class);
                        intent.putExtra("Symbol_Type", option);
                        startActivityForResult(intent, nRequestCode);
                        break;
                    case Composite:
                    case GS1_128:
                    case ISSN_EAN:
                    case ISBT_128:
                    case Japan_Postal:
                    case Maxicode:
                    case MicroPDF417:
                    case MicroQR:
                    case Netherlands_KIX_Code:
                    case PDF417:
                    case USPS_4CB_OneCode_Inteligent_Mail:
                    case UPU_FICS_Postral:
                    case Aztec:
                    case Australia_Post:
                    case Data_Matrix:
                    case Korea_3of5:
                    case QRCode:
                    case HanXin:
                    case Ch2of5:
                        intent = new Intent(this, OptionSymbolOptionCheck.class);
                        intent.putExtra("Symbol_Type", option);
                        startActivityForResult(intent, nRequestCode);
                        break;
                    case US_Postnet:
                    case US_Planet:
                    case UK_Postal:
                        intent = new Intent(this, OptionSymbolPostalCodeActivity.class);
                        intent.putExtra("Symbol_Type", option);
                        startActivityForResult(intent, nRequestCode);
                        break;
                    case RSS_14:
                        intent = new Intent(this, OptionSymbolRssActivity.class);
                        intent.putExtra("Symbol_Type", option);
                        startActivityForResult(intent, nRequestCode);
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
                enableAllWidget(false);
                if(defaultSymbol()) {
                    Toast.makeText(this, R.string.succeeded_to_default_all_symbologies,
                            Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(this, R.string.failed_to_default_all_parameter,
                            Toast.LENGTH_LONG).show();
                }
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
                SSIParamName.Code128, SSIParamName.Code39, SSIParamName.Code93,
                SSIParamName.Code11, SSIParamName.I2of5, SSIParamName.D2of5,
                SSIParamName.Ch2of5, SSIParamName.Codabar, SSIParamName.MSI,
                SSIParamName.RSS_14, SSIParamName.RSS_Limited, SSIParamName.RSS_Expanded,

                SSIParamName.Aztec, SSIParamName.Data_Matrix, SSIParamName.Maxicode,
                SSIParamName.MicroQR, SSIParamName.MicroPDF417, SSIParamName.PDF417,
                SSIParamName.QRCode, SSIParamName.Matrix2of5, SSIParamName.Korea_3of5,
                SSIParamName.US_Postnet, SSIParamName.US_Planet, SSIParamName.Japan_Postal,
                SSIParamName.Australia_Post, SSIParamName.Netherlands_KIX_Code, SSIParamName.USPS_4CB_OneCode_Intelligent_Mail,
                SSIParamName.UPU_FICS_Postal, SSIParamName.Composite_CC_C, SSIParamName.Composite_CC_AB,
                SSIParamName.Composite_TLC_39, SSIParamName.ISSN_EAN, SSIParamName.UCC_EAN_128,
                SSIParamName.ISBT128, SSIParamName.HanXin, SSIParamName.UK_Postal,
        });
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

    private boolean defaultSymbol() {
        return this.mParam.defaultParams();
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
                new SSIParamValue(SSIParamName.RSS_Expanded, enabled),
                new SSIParamValue(SSIParamName.Aztec, enabled),
                new SSIParamValue(SSIParamName.Data_Matrix, enabled),
                new SSIParamValue(SSIParamName.Maxicode, enabled),
                new SSIParamValue(SSIParamName.MicroQR, enabled),
                new SSIParamValue(SSIParamName.MicroPDF417, enabled),
                new SSIParamValue(SSIParamName.PDF417, enabled),
                new SSIParamValue(SSIParamName.QRCode, enabled),
                new SSIParamValue(SSIParamName.Matrix2of5, enabled),
                new SSIParamValue(SSIParamName.Korea_3of5, enabled),
                new SSIParamValue(SSIParamName.US_Postnet, enabled),
                new SSIParamValue(SSIParamName.US_Planet, enabled),
                new SSIParamValue(SSIParamName.UK_Postal, enabled),
                new SSIParamValue(SSIParamName.Japan_Postal, enabled),
                new SSIParamValue(SSIParamName.Australia_Post, enabled),
                new SSIParamValue(SSIParamName.Netherlands_KIX_Code, enabled),
                new SSIParamValue(SSIParamName.USPS_4CB_OneCode_Intelligent_Mail, enabled),
                new SSIParamValue(SSIParamName.UPU_FICS_Postal, enabled),
                new SSIParamValue(SSIParamName.Composite_CC_C, enabled),
                new SSIParamValue(SSIParamName.Composite_CC_AB, enabled),
                new SSIParamValue(SSIParamName.UPC_Composite_Mode, UpcCompositeMode.UpcNeverLinked),
                new SSIParamValue(SSIParamName.Composite_TLC_39, enabled),
                new SSIParamValue(SSIParamName.ISSN_EAN, enabled),
                new SSIParamValue(SSIParamName.UCC_EAN_128, enabled),
                new SSIParamValue(SSIParamName.HanXin, enabled),
                new SSIParamValue(SSIParamName.ISBT128, enabled)});
        return this.mParam.setParams(paramList);
    }

    private int symbolIndex(Scan1dSymbolOption select) {
        int nRequestCode = -1;

        switch (select) {
            case UpcEan:
                nRequestCode = VIEW_UPC_EAN_SYMBOL;
                break;
            case Code128:
                nRequestCode = VIEW_CODE128_SYMBOL;
                break;
            case Code39:
                nRequestCode = VIEW_CODE39_SYMBOL;
                break;
            case Code93:
                nRequestCode = VIEW_CODE93_SYMBOL;
                break;
            case Code11:
                nRequestCode = VIEW_CODE11_SYMBOL;
                break;
            case I2of5:
                nRequestCode = VIEW_I2OF5_SYMBOL;
                break;
            case D2of5:
                nRequestCode = VIEW_D2OF5_SYMBOL;
                break;
            case Ch2of5:
                nRequestCode = VIEW_CHINESE_2OF5_SYMBOL;
                break;
            case Codabar:
                nRequestCode = VIEW_CODABAR_SYMBOL;
                break;
            case Msi:
                nRequestCode = VIEW_MSI_SYMBOL;
                break;
            case Matrix2of5:
                nRequestCode = VIEW_MATRIX_2OF5_SYMBOL;
                break;
            case Composite:
                nRequestCode = VIEW_COMPOSITE_CC_C_SYMBOL;
                break;
            case GS1_128:
                nRequestCode = VIEW_GS1_128_SYMBOL;
                break;
            case ISSN_EAN:
                nRequestCode = VIEW_ISSN_EAN_SYMBOL;
                break;
            case ISBT_128:
                nRequestCode = VIEW_ISBT_128_SYMBOL;
                break;
            case Japan_Postal:
                nRequestCode = VIEW_JAPAN_POSTAL_SYMBOL;
                break;
            case Maxicode:
                nRequestCode = VIEW_MAXICODE_SYMBOL;
                break;
            case MicroPDF417:
                nRequestCode = VIEW_MICRO_PDF417_SYMBOL;
                break;
            case MicroQR:
                nRequestCode = VIEW_MICRO_QR_SYMBOL;
                break;
            case Netherlands_KIX_Code:
                nRequestCode = VIEW_NETHERLAND_KIX_CODE_SYMBOL;
                break;
            case PDF417:
                nRequestCode = VIEW_PDF417_SYMBOL;
                break;
            case US_Postnet:
                nRequestCode = VIEW_US_POSTNET_SYMBOL;
                break;
            case US_Planet:
                nRequestCode = VIEW_US_PLANET_SYMBOL;
                break;
            case USPS_4CB_OneCode_Inteligent_Mail:
                nRequestCode = VIEW_USPS_4CB_ONE_CODE_INTELIGENT_MAIL_SYMBOL;
                break;
            case UPU_FICS_Postral:
                nRequestCode = VIEW_UPU_FICS_POSTRAL_SYMBOL;
                break;
            case Aztec:
                nRequestCode = VIEW_AZTEC_SYMBOL;
                break;
            case Australia_Post:
                nRequestCode = VIEW_AUSTRALIA_POSTAL_SYMBOL;
                break;
            case Data_Matrix:
                nRequestCode = VIEW_DATA_MATRIX_SYMBOL;
                break;
            case Korea_3of5:
                nRequestCode = VIEW_KOREA_3OF5_SYMBOL;
                break;
            case QRCode:
                nRequestCode = VIEW_QR_CODE_SYMBOL;
                break;
            case HanXin:
                nRequestCode = VIEW_HAN_XIN_SYMBOL;
                break;
            case UK_Postal:
                nRequestCode = VIEW_UK_POSTAL_SYMBOL;
                break;
            case RSS_14:
                nRequestCode = VIEW_RSS_14_SYMBOL;
                break;
            default:
                return -1;
        }

        return nRequestCode;
    }

}
