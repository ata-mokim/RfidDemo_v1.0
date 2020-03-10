package com.wsmr.app.barcode.option.SE4710;

import com.wsmr.lib.dev.ATScanManager;
import com.wsmr.lib.dev.ATScanner;
import com.wsmr.lib.dev.barcode.motorola.param.SSIParamName;
import com.wsmr.lib.dev.barcode.motorola.param.SSIParamValueList;
import com.wsmr.lib.dev.barcode.params.ATScanSE4710Parameter;
import com.wsmr.lib.dev.barcode.motorola.type.Gs1DataBarLimitedSecurityLevel;
import com.wsmr.app.barcode.R;
import com.wsmr.app.barcode.adapter.ValueAdapter;
import com.wsmr.app.barcode.adapter.ValueItem;
import com.wsmr.app.barcode.type.SE4710.Scan1dSymbolOption;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.Spinner;
import android.widget.Toast;

public class OptionSymbolRssActivity extends Activity implements
        OnClickListener {

    @SuppressWarnings("unused")
    private static final String TAG = "OptionSymbolRssActivity";
    private ATScanner mScanner;
    private ATScanSE4710Parameter mParam;
    @SuppressWarnings("unused")
    private Scan1dSymbolOption symbol_type;

    private CheckBox chkRss14Enable;
    private CheckBox chkRssLimitedEnable;
    private CheckBox chkRssExpandedEnable;
    private CheckBox chkConvertRssToUpcEanEnable;
    private Spinner spnRssSecurityLevel;
    private ValueAdapter<Gs1DataBarLimitedSecurityLevel> adpRssSecurityLevel;
    private Button btnSetOption;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_option_2d_symbol_rss);
        getActionBar().setDisplayHomeAsUpEnabled(true);
        this.setTitle(R.string.symbol_rss_name);
        // Initialize Scanner Instance
        this.mScanner = ATScanManager.getInstance(getApplicationContext());
        this.mParam = (ATScanSE4710Parameter)this.mScanner.getParameter();
        this.symbol_type = (Scan1dSymbolOption)getIntent().getSerializableExtra("Symbol_Type");

        this.adpRssSecurityLevel = new ValueAdapter<Gs1DataBarLimitedSecurityLevel>(this);
        for (Gs1DataBarLimitedSecurityLevel item : Gs1DataBarLimitedSecurityLevel.values()) {
            this.adpRssSecurityLevel.add(new ValueItem<Gs1DataBarLimitedSecurityLevel>(item.toString(), item));
        }
        this.spnRssSecurityLevel = (Spinner)findViewById(R.id.rss_limited_security_level);
        this.spnRssSecurityLevel.setAdapter(adpRssSecurityLevel);

        this.chkRss14Enable = (CheckBox)findViewById(R.id.rss_14_enable);
        this.chkRssLimitedEnable = (CheckBox)findViewById(R.id.rss_limited_enable);
        this.chkRssExpandedEnable = (CheckBox)findViewById(R.id.rss_expanded_enable);
        this.chkConvertRssToUpcEanEnable = (CheckBox)findViewById(R.id.convert_rss_to_upcean);

        this.btnSetOption = (Button) findViewById(R.id.set_option);
        this.btnSetOption.setOnClickListener(this);

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

            paramList.add(SSIParamName.RSS_14, this.chkRss14Enable.isChecked());
            paramList.add(SSIParamName.RSS_Limited, this.chkRssLimitedEnable.isChecked());
            paramList.add(SSIParamName.RSS_Expanded, this.chkRssExpandedEnable.isChecked());
            paramList.add(SSIParamName.Convert_GS1_DataBar_to_UPC_EAN, this.chkConvertRssToUpcEanEnable.isChecked());
            paramList.add(SSIParamName.RSS_Limited_Security_Level,
                    this.adpRssSecurityLevel.getItem(this.spnRssSecurityLevel.getSelectedItemPosition()));

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

    private void initSymbolStateList() {
        SSIParamValueList paramList = this.mParam.getParams(new SSIParamName[]
                { SSIParamName.RSS_14, SSIParamName.RSS_Limited, SSIParamName.RSS_Expanded,
                        SSIParamName.RSS_Limited_Security_Level, SSIParamName.Convert_GS1_DataBar_to_UPC_EAN
                }
        );

        this.chkRss14Enable.setChecked((Boolean) paramList.getValueAt(SSIParamName.RSS_14));
        this.chkRssLimitedEnable.setChecked((Boolean) paramList.getValueAt(SSIParamName.RSS_Limited));
        this.chkRssExpandedEnable.setChecked((Boolean) paramList.getValueAt(SSIParamName.RSS_Expanded));
        this.chkConvertRssToUpcEanEnable.setChecked((Boolean) paramList.getValueAt(SSIParamName.Convert_GS1_DataBar_to_UPC_EAN));
        this.spnRssSecurityLevel.setSelection(this.adpRssSecurityLevel.getPosition((Gs1DataBarLimitedSecurityLevel)paramList.getValueAt(SSIParamName.RSS_Limited_Security_Level)));
    }
}
