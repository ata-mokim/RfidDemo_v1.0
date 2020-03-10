package com.wsmr.app.barcode.option.IT5x80;

import java.nio.charset.Charset;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.SortedMap;

import com.wsmr.lib.dev.ATScanManager;
import com.wsmr.lib.dev.ATScanner;
import com.wsmr.lib.dev.barcode.honeywell.param.HoneywellParamName;
import com.wsmr.lib.dev.barcode.honeywell.param.HoneywellParamValueList;
import com.wsmr.lib.dev.barcode.honeywell.type.AimerMode;
import com.wsmr.lib.dev.barcode.params.ATScanIT5x80Parameter;
import com.wsmr.lib.dev.barcode.type.RangeIntValue;
import com.wsmr.lib.diagnostics.ATLog;
import com.wsmr.app.barcode.R;
import com.wsmr.app.barcode.adapter.ValueAdapter;
import com.wsmr.app.barcode.adapter.ValueItem;
import com.wsmr.app.barcode.widget.ValueSpinner;
import com.wsmr.app.barcode.widget.filter.InputFilterIntMinMax;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

public class OptionGeneralConfigActivity extends Activity implements
        OnClickListener {

    private static final String TAG = "OptionGeneralConfigActivity";

    private ATScanner mScanner;
    private ATScanIT5x80Parameter mParam;

    private EditText edtReadTimeout;
    private EditText edtRereadDelay;
    private CheckBox chkIllumination;
    private ValueSpinner<AimerMode> spnAimerMode;
    private EditText edtAimerDelay;
    private Button btnSetOpton;
    private Spinner spnCharSet;
    private ValueAdapter<String> adpCharSet;

    @SuppressWarnings("rawtypes")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_option_2d_general_config);
        getActionBar().setDisplayHomeAsUpEnabled(true);

        // Initialize Scanner Instance
        this.mScanner = ATScanManager.getInstance(getApplicationContext());
        this.mParam = (ATScanIT5x80Parameter) this.mScanner.getParameter();

        // Initialize Widgets
        this.edtReadTimeout = (EditText) findViewById(R.id.read_timeout);
        this.edtRereadDelay = (EditText) findViewById(R.id.reread_delay);
        this.chkIllumination = (CheckBox) findViewById(R.id.illumination_lights);
        this.spnAimerMode = new ValueSpinner<AimerMode>(this, R.id.aimer_mode,
                AimerMode.values());
        this.edtAimerDelay = (EditText) findViewById(R.id.aimer_delay);
        this.btnSetOpton = (Button) findViewById(R.id.set_option);
        this.btnSetOpton.setOnClickListener(this);

        this.spnCharSet = (Spinner)findViewById(R.id.char_set);
        this.adpCharSet = new ValueAdapter<String>(this);
        SortedMap<String, Charset> charSets = Charset.availableCharsets();
        Set s = charSets.entrySet();
        Iterator i = s.iterator();
        while(i.hasNext()) {
            Map.Entry m = (Map.Entry)i.next();
            String key = (String)m.getKey();
            Charset value = (Charset)m.getValue();
            ATLog.d(TAG, " -> %s", value.name());
            this.adpCharSet.add(new ValueItem<String>(value.name(), key));
        }
        this.spnCharSet.setAdapter(this.adpCharSet);

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
            HoneywellParamValueList paramList = new HoneywellParamValueList();
            int readTimeout = 0;
            int rereadDelay = 0;
            int aimerDealy = 0;

            try {
                readTimeout = Integer.parseInt(this.edtReadTimeout.getText().toString());
            } catch (NumberFormatException e) {
                readTimeout = 0;
            }
            try {
                rereadDelay = Integer.parseInt(this.edtRereadDelay.getText().toString());
            } catch (NumberFormatException e) {
                rereadDelay = 0;
            }
            try {
                aimerDealy = Integer.parseInt(this.edtAimerDelay.getText().toString());
            } catch (NumberFormatException e) {
                aimerDealy = 0;
            }
            paramList.add(HoneywellParamName.ReadTimeout, readTimeout);
            paramList.add(HoneywellParamName.RereadDelay, rereadDelay);
            paramList.add(HoneywellParamName.IlluminationLights, this.chkIllumination.isChecked());
            paramList.add(HoneywellParamName.AimerMode, this.spnAimerMode.getValue());
            paramList.add(HoneywellParamName.AimerDelay, aimerDealy);

            this.mScanner.setCharSetName(this.adpCharSet.getItem(this.spnCharSet.getSelectedItemPosition()));

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
        RangeIntValue value;
        HoneywellParamValueList paramList;

        paramList = mParam.getParamRanges(new HoneywellParamName[] {
                HoneywellParamName.ReadTimeout, HoneywellParamName.RereadDelay,
                HoneywellParamName.AimerDelay });

        value = (RangeIntValue) paramList.getValueAt(HoneywellParamName.ReadTimeout);
        this.edtReadTimeout.setFilters(InputFilterIntMinMax.getFilters(
                value.getMin(), value.getMax()));
        value = (RangeIntValue) paramList.getValueAt(HoneywellParamName.RereadDelay);
        this.edtRereadDelay.setFilters(InputFilterIntMinMax.getFilters(
                value.getMin(), value.getMax()));
        value = (RangeIntValue) paramList.getValueAt(HoneywellParamName.AimerDelay);
        this.edtAimerDelay.setFilters(InputFilterIntMinMax.getFilters(
                value.getMin(), value.getMax()));

        paramList = mParam.getParams(new HoneywellParamName[] {
                HoneywellParamName.ReadTimeout, HoneywellParamName.RereadDelay,
                HoneywellParamName.IlluminationLights, HoneywellParamName.AimerMode,
                HoneywellParamName.AimerDelay });

        this.edtReadTimeout.setText(""
                + (Integer) paramList.getValueAt(HoneywellParamName.ReadTimeout));
        this.edtRereadDelay.setText(""
                + (Integer) paramList.getValueAt(HoneywellParamName.RereadDelay));
        this.chkIllumination.setChecked((Boolean) paramList
                .getValueAt(HoneywellParamName.IlluminationLights));
        this.spnAimerMode.setValue(paramList.getValueAt(HoneywellParamName.AimerMode));
        this.edtAimerDelay.setText(""
                + (Integer) paramList.getValueAt(HoneywellParamName.AimerDelay));

        this.spnCharSet.setSelection(
                this.adpCharSet.getPosition(
                        this.mScanner.getCharSetName()
                )
        );
    }
}
