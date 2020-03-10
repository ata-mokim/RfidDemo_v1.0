package com.wsmr.app.barcode.option.SE4710;

import com.wsmr.lib.dev.ATScanManager;
import com.wsmr.lib.dev.ATScanner;
import com.wsmr.lib.dev.barcode.params.ATScanSE4710Parameter;
import com.wsmr.lib.diagnostics.ATLog;
import com.wsmr.lib.dev.barcode.motorola.param.SSIParamName;
import com.wsmr.lib.dev.barcode.motorola.param.SSIParamValueList;
import com.wsmr.app.barcode.R;
import com.wsmr.app.barcode.adapter.ValueAdapter;
import com.wsmr.app.barcode.adapter.ValueItem;
import com.wsmr.app.barcode.type.SE4710.Scan1dSymbolOption;
import com.wsmr.app.barcode.widget.SymbolLength;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Toast;

public class OptionSymbolCode128Activity extends Activity implements OnClickListener {

    private static final String TAG = "OptionSymbolCode128Activity";

    private ATScanner mScanner;
    private ATScanSE4710Parameter mParam;

    private CheckBox chkEnable;
    private CheckBox chkOption1;
    private CheckBox chkOption2;
    private CheckBox chkOption3;
    private CheckBox chkOption4;
    private LinearLayout IsbtConcatenationRedundancy_lbl;
    private Spinner spnIsbtConcatenationRedundancy;
    private SymbolLength wgtLength;
    private Button btnSetOption;

    private ValueAdapter<Integer> adpIsbtConcatenationRedundancy;
    private Scan1dSymbolOption symbol_type;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_option_2d_symbol_cod128);
        getActionBar().setDisplayHomeAsUpEnabled(true);

        // Initialize Scanner Instance
        this.mScanner = ATScanManager.getInstance(getApplicationContext());
        this.mParam = (ATScanSE4710Parameter) this.mScanner.getParameter();

        // Initialize Widgets
        this.chkEnable = (CheckBox) findViewById(R.id.symbol_enable);
        this.chkOption1 = (CheckBox) findViewById(R.id.symbol_opt1);
        this.chkOption2 = (CheckBox) findViewById(R.id.symbol_opt2);
        this.chkOption3 = (CheckBox) findViewById(R.id.symbol_opt3);
        this.chkOption4 = (CheckBox) findViewById(R.id.symbol_opt4);

        this.IsbtConcatenationRedundancy_lbl = (LinearLayout) findViewById(R.id.IsbtConcatenationRedundancy_lbl);
        this.spnIsbtConcatenationRedundancy = (Spinner) findViewById(R.id.IsbtConcatenationRedundancy);
        this.adpIsbtConcatenationRedundancy = new ValueAdapter<Integer>(this);
        for (int i = 2; i <= 20; i++) {
            adpIsbtConcatenationRedundancy.add(new ValueItem<Integer>("" + i, i));
        }
        this.adpIsbtConcatenationRedundancy.notifyDataSetChanged();
        this.spnIsbtConcatenationRedundancy.setAdapter(adpIsbtConcatenationRedundancy);
        this.wgtLength = (SymbolLength) findViewById(R.id.length);

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

        if (mScanner != null)
            ATScanManager.wakeUp();
    }

    @Override
    protected void onStop() {
        if (mScanner != null)
            ATScanManager.sleep();
        super.onStop();
    }

    @SuppressLint("ShowToast")
    @Override
    public void onClick(View v) {
        if (R.id.set_option == v.getId()) {
            SSIParamValueList paramList = new SSIParamValueList();

            switch (symbol_type) {
                case Code128:
                    paramList.add(SSIParamName.Code128, this.chkEnable.isChecked());
                    paramList.add(SSIParamName.UCC_EAN_128, this.chkOption1.isChecked());
                    paramList.add(SSIParamName.ISBT128, this.chkOption2.isChecked());
                    paramList.add(SSIParamName.ISBT_Concatenation, this.chkOption3.isChecked());
                    paramList.add(SSIParamName.Check_ISBT_Table, this.chkOption4.isChecked());
                    paramList.add(SSIParamName.ISBT_Concatenation_Redundancy,
                            this.spnIsbtConcatenationRedundancy.getSelectedItem());
                    paramList.add(SSIParamName.Code128_Length_Min, this.wgtLength.getLength1());
                    paramList.add(SSIParamName.Code128_Length_Max, this.wgtLength.getLength2());
                    break;
                case Matrix2of5:
                    paramList.add(SSIParamName.Matrix2of5, this.chkEnable.isChecked());
                    paramList.add(SSIParamName.Matrix2of5_Check_Digit, this.chkOption1.isChecked());
                    paramList.add(SSIParamName.Transmit_Matrix2of5_Check_Digit, this.chkOption2.isChecked());
                    paramList.add(SSIParamName.Matrix2of5_Length_Min, this.wgtLength.getLength1());
                    paramList.add(SSIParamName.Matrix2of5_Length_Max, this.wgtLength.getLength2());
                    break;
                default:
                    break;
            }

            // Set Scanner Parameter
            if (this.mParam.setParams(paramList)) {
                this.setResult(RESULT_OK);
                this.finish();
            } else {
                Toast.makeText(this, R.string.faile_to_set_symbologies, Toast.LENGTH_LONG).show();
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

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
        symbol_type = (Scan1dSymbolOption) getIntent().getSerializableExtra("Symbol_Type");

        SSIParamValueList paramList = this.mParam.getParams(new SSIParamName[] { SSIParamName.Code128,
                SSIParamName.UCC_EAN_128, SSIParamName.ISBT128, SSIParamName.ISBT_Concatenation,
                SSIParamName.Check_ISBT_Table, SSIParamName.ISBT_Concatenation_Redundancy,
                SSIParamName.Code128_Length_Min, SSIParamName.Code128_Length_Max, SSIParamName.Matrix2of5,
                SSIParamName.Matrix2of5_Check_Digit, SSIParamName.Transmit_Matrix2of5_Check_Digit,
                SSIParamName.Matrix2of5_Length_Min, SSIParamName.Matrix2of5_Length_Max });

        switch (symbol_type) {
            case Code128:
                this.setTitle(R.string.symbol_code128_name);
                this.chkEnable.setText("Enabled");
                this.chkOption1.setText("GS1-128(formerly UCC/EAN-128");
                this.chkOption2.setText("ISBT 128");
                this.chkOption3.setText("ISBT Concatenation");
                this.chkOption4.setText("Check ISBT Table");

                this.chkEnable.setChecked((Boolean) paramList.getValueAt(SSIParamName.Code128));
                this.chkOption1.setChecked((Boolean) paramList.getValueAt(SSIParamName.UCC_EAN_128));
                this.chkOption2.setChecked((Boolean) paramList.getValueAt(SSIParamName.ISBT128));
                this.chkOption3.setChecked((Boolean) paramList.getValueAt(SSIParamName.ISBT_Concatenation));
                this.chkOption4.setChecked((Boolean) paramList.getValueAt(SSIParamName.Check_ISBT_Table));

                this.spnIsbtConcatenationRedundancy
                        .setSelection((Integer) paramList.getValueAt(SSIParamName.ISBT_Concatenation_Redundancy) - 2);

                ATLog.d(TAG, "min: %d, max: %d", (Integer) paramList.getValueAt(SSIParamName.Code128_Length_Min),
                        (Integer) paramList.getValueAt(SSIParamName.Code128_Length_Max));

                this.wgtLength.setLength((Integer) paramList.getValueAt(SSIParamName.Code128_Length_Min),
                        (Integer) paramList.getValueAt(SSIParamName.Code128_Length_Max));

                break;
            case Matrix2of5:
                this.setTitle(R.string.symbol_matrix2of5_name);
                this.chkEnable.setText("Enabled");
                this.chkOption1.setText("Check Digit");
                this.chkOption2.setText("Transmit Matrix2of5 Check Digit");
                this.chkOption3.setText("");
                this.chkOption4.setText("");
                this.chkOption3.setVisibility(View.GONE);
                this.chkOption4.setVisibility(View.GONE);
                this.IsbtConcatenationRedundancy_lbl.setVisibility(View.GONE);

                this.chkEnable.setChecked((Boolean) paramList.getValueAt(SSIParamName.Matrix2of5));
                this.chkOption1.setChecked((Boolean) paramList.getValueAt(SSIParamName.Matrix2of5_Check_Digit));
                this.chkOption2.setChecked((Boolean) paramList.getValueAt(SSIParamName.Transmit_Matrix2of5_Check_Digit));
                this.wgtLength.setLength((Integer) paramList.getValueAt(SSIParamName.Matrix2of5_Length_Min),
                        (Integer) paramList.getValueAt(SSIParamName.Matrix2of5_Length_Max));

                break;
            default:
                break;
        }
    }
}
