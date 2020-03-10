package com.wsmr.app.barcode.option.MDI3x00;

import java.nio.charset.Charset;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.SortedMap;

import com.wsmr.app.barcode.R;
import com.wsmr.app.barcode.adapter.ValueAdapter;
import com.wsmr.app.barcode.adapter.ValueItem;
import com.wsmr.lib.dev.ATScanManager;
import com.wsmr.lib.dev.ATScanner;
import com.wsmr.lib.dev.barcode.params.ATScanMDI3x00Parameter;
import com.wsmr.lib.diagnostics.ATLog;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Spinner;

public class OptionGeneralConfigActivity extends Activity implements OnClickListener {

    private static final String TAG = "OptionGeneralConfigActivity";
    private ATScanner mScanner;
    @SuppressWarnings("unused")
    private ATScanMDI3x00Parameter mParam;

    private Button btnSetOption;
    private Spinner spnCharSet;
    private ValueAdapter<String> adpCharSet;

    @SuppressWarnings("rawtypes")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_option_mdi3x00_general_config);
        getActionBar().setDisplayHomeAsUpEnabled(true);

        // Initialize Scanner Instance
        this.mScanner = ATScanManager.getInstance(getApplicationContext());
        this.mParam = (ATScanMDI3x00Parameter) this.mScanner.getParameter();

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

        this.btnSetOption = (Button) findViewById(R.id.set_option);
        this.btnSetOption.setOnClickListener(this);

        loadOption();
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
    public void onClick(View v) {
        if (R.id.set_option == v.getId()) {
            this.mScanner.setCharSetName(this.adpCharSet.getItem(this.spnCharSet.getSelectedItemPosition()));
            this.setResult(RESULT_OK);
            this.finish();
        }

    }

    // Load Scanner Symbol Detail Option
    private void loadOption() {
        this.spnCharSet.setSelection(
                this.adpCharSet.getPosition(
                        this.mScanner.getCharSetName()
                )
        );
    }
}
