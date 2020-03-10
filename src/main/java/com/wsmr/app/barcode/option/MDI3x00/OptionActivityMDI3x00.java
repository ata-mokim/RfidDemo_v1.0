package com.wsmr.app.barcode.option.MDI3x00;

import com.wsmr.lib.dev.ATScanManager;
import com.wsmr.lib.dev.ATScanner;
import com.wsmr.lib.dev.barcode.opticon.param.OpticonParamName;
import com.wsmr.lib.dev.barcode.opticon.param.OpticonParamValue;
import com.wsmr.lib.dev.barcode.opticon.param.OpticonParamValueList;
import com.wsmr.lib.dev.barcode.params.ATScanMDI3x00Parameter;
import com.wsmr.lib.diagnostics.ATLog;
import com.wsmr.app.barcode.R;
import com.wsmr.app.barcode.adapter.MDI3x00.SymbolOptionListAdapter;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;


public class OptionActivityMDI3x00 extends Activity implements Button.OnClickListener {

    private static final String TAG = "OptionActivityMDI3x00";
    public static final int VIEW_ENABLE_STATE_SYMBOL = 1;
    public static final int VIEW_GENERAL = 10;

    private SymbolOptionListAdapter adpOptions;
    private ATScanner mScanner;
    private ATScanMDI3x00Parameter mParam;

    private ListView lstOptions;
    private Button btnEnableStatus;
    private Button btnDefaultAllSymbol;
    private Button btnDisableAllSymbol;
    private Button btnEnableAllSymbol;
    private Button btnGeneral;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_option_main);
        getActionBar().setDisplayHomeAsUpEnabled(true);

        // Initialize Scanner Instance
        mScanner = ATScanManager.getInstance(getApplicationContext());
        mParam = (ATScanMDI3x00Parameter) mScanner.getParameter();

        // Initialize Widgets
        lstOptions = (ListView) findViewById(R.id.symbol_list);
        adpOptions = new SymbolOptionListAdapter(this);
        lstOptions.setAdapter(adpOptions);

        btnEnableStatus = (Button) findViewById(R.id.set_enable_status);
        btnEnableStatus.setOnClickListener(this);

        btnDefaultAllSymbol = (Button) findViewById(R.id.default_all_symbologies);
        btnDefaultAllSymbol.setOnClickListener(this);

        btnDisableAllSymbol = (Button) findViewById(R.id.disable_all_symbologies);
        btnDisableAllSymbol.setOnClickListener(this);

        btnEnableAllSymbol = (Button) findViewById(R.id.enable_all_symbologies);
        btnEnableAllSymbol.setOnClickListener(this);

        btnGeneral = (Button) findViewById(R.id.gerneral_config);
        btnGeneral.setOnClickListener(this);
        // 2019-12-18 ncsin4 disable general option
        btnGeneral.setVisibility(View.GONE);

        // Initialize Symbol Option List
        initSymbolOptionList();
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
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Disable All Widget
        enableAllWidget(true);

        if (requestCode == VIEW_ENABLE_STATE_SYMBOL && resultCode == RESULT_OK) {
            // Initialize Symbol Option List
            initSymbolOptionList();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View v) {

        Intent intent;
        OpticonParamValueList paramList;

        switch (v.getId()) {
            case R.id.detail_option:
                ATLog.d(TAG, "detail_option");
                break;
            case R.id.set_enable_status:
                ATLog.d(TAG, "set_enable_status");
                intent = new Intent(this, OptionEnableStateActivity.class);
                startActivityForResult(intent, VIEW_ENABLE_STATE_SYMBOL);
                break;
            case R.id.default_all_symbologies:
                // 이 커맨드를 보내면, prefix, suffix 설정을 다시해야되서 일단 보류.

                paramList = new OpticonParamValueList(new OpticonParamValue[] {
                        new OpticonParamValue(OpticonParamName.BackToCustomDefaults),
                        //new OpticonParamValue(OpticonParamName.SaveSettingsInStartUpSettingArea)
                });

                mParam.setParams(paramList);

                // 2019.07.09 ?????? config ????? ?????? ???? ????????, ??? ????? cahrSet???? ?????.
                mScanner.setCharSetName("GB18030");

                ATLog.d(TAG, "default_all_symbologies");
                break;
            case R.id.disable_all_symbologies:
                paramList = new OpticonParamValueList(new OpticonParamValue[] {
                        new OpticonParamValue(OpticonParamName.AllCodes_Disable),
                        new OpticonParamValue(OpticonParamName.SaveSettingsInStartUpSettingArea)
                });

                mParam.setParams(paramList);
                ATLog.d(TAG, "disable_all_symbologies");
                break;
            case R.id.enable_all_symbologies:
                paramList = new OpticonParamValueList(new OpticonParamValue[] {
                        new OpticonParamValue(OpticonParamName.AllCodes_Multiple),
                        new OpticonParamValue(OpticonParamName.SaveSettingsInStartUpSettingArea)
                });

                mParam.setParams(paramList);
                ATLog.d(TAG, "enable_all_symbologies");
                break;
            case R.id.gerneral_config:
                ATLog.d(TAG, "gerneral_config");
                intent = new Intent(this, OptionGeneralConfigActivity.class);
                startActivityForResult(intent, VIEW_GENERAL);
                break;
        }
    }

    // Enable/Disable All Widget
    private void enableAllWidget(boolean enabled) {
        lstOptions.setEnabled(enabled);
        btnEnableStatus.setEnabled(enabled);
        btnDefaultAllSymbol.setEnabled(enabled);
        btnDisableAllSymbol.setEnabled(enabled);
        btnEnableAllSymbol.setEnabled(enabled);
        btnGeneral.setEnabled(enabled);
    }

    private void initSymbolOptionList() {

        OpticonParamValueList paramList = mParam.getParams(new OpticonParamName[] {
                OpticonParamName.UPCA, OpticonParamName.UPCE, OpticonParamName.EAN13, OpticonParamName.EAN8,
                OpticonParamName.Code39, OpticonParamName.Codabar, OpticonParamName.Industrial2of5,
                OpticonParamName.Interleaved2of5, OpticonParamName.SCode, OpticonParamName.Code128,
                OpticonParamName.Code93, OpticonParamName.IATA, OpticonParamName.MSI_Plessey, OpticonParamName.UK_Plessey,
                OpticonParamName.Telepen, OpticonParamName.Code11, OpticonParamName.Matrix2of5, OpticonParamName.ChinesePost,
                OpticonParamName.KoreanPostalAuthority, OpticonParamName.IntelligentMailBarCode, OpticonParamName.POSTNET,
                OpticonParamName.JapanesePost, OpticonParamName.GS1_Databar, OpticonParamName.PDF417,
                OpticonParamName.MicroPDF417, OpticonParamName.CodablockF, OpticonParamName.QRCode,
                OpticonParamName.MicroQRCode, OpticonParamName.DataMatrix, OpticonParamName.Aztec,
                OpticonParamName.ChineseSensibleCode, OpticonParamName.MaxiCode
        });

        adpOptions.updateList(paramList);
    }
}
