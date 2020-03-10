package com.wsmr.app.barcode.option.IT5x80;

import com.wsmr.lib.dev.ATScanManager;
import com.wsmr.lib.dev.barcode.honeywell.param.HoneywellParamName;
import com.wsmr.lib.dev.barcode.honeywell.param.HoneywellParamValueList;
import com.wsmr.lib.dev.barcode.honeywell.type.OcrType;
import com.wsmr.app.barcode.R;
import com.wsmr.app.barcode.widget.ValueSpinner;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class OptionSymbolOCR extends OptionSymbol implements OnClickListener {

    private ValueSpinner<OcrType> spnOcrType;
    private EditText edtOcrTemplate;
    private EditText edtVariableG;
    private EditText edtVariableH;
    private EditText edtCheckChar;
    private Button btnSetOption;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_option_2d_symbol_ocr);
        getActionBar().setDisplayHomeAsUpEnabled(true);

        // Initialize Widgets
        this.spnOcrType = new ValueSpinner<OcrType>(this, R.id.ocr_type);
        this.spnOcrType.fill(new OcrType[] { OcrType.OcrA, OcrType.OcrB,
                OcrType.USCurrency, OcrType.MicrE13B, OcrType.SemiFont });
        this.edtOcrTemplate = (EditText) findViewById(R.id.ocr_template);
        this.edtVariableG = (EditText) findViewById(R.id.user_defined_variable_g);
        this.edtVariableH = (EditText) findViewById(R.id.user_defined_variable_h);
        this.edtCheckChar = (EditText) findViewById(R.id.check_character);
        this.btnSetOption = (Button) findViewById(R.id.set_option);
        this.btnSetOption.setOnClickListener(this);

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

            paramList.add(HoneywellParamName.OCR, this.spnOcrType.getValue());
            paramList.add(HoneywellParamName.OCRTemplate, this.edtOcrTemplate.getText().toString());
            paramList.add(HoneywellParamName.OCRVarG, this.edtVariableG.getText().toString());
            paramList.add(HoneywellParamName.OCRVarH, this.edtVariableH.getText().toString());
            paramList.add(HoneywellParamName.OCRCheckChar, this.edtCheckChar.getText().toString());

            if (this.mParam.setParams(paramList)) {
                this.setResult(RESULT_OK);
                this.finish();
            } else {
                Toast.makeText(this, R.string.faile_to_set_symbologies,
                        Toast.LENGTH_LONG).show();
            }
        }
    }

    // Load Scanner Symbol Detail Option
    private void loadOption() {
        HoneywellParamValueList paramList = this.mParam.getParams(new HoneywellParamName[] {
                HoneywellParamName.OCR, HoneywellParamName.OCRTemplate, HoneywellParamName.OCRVarG,
                HoneywellParamName.OCRVarH, HoneywellParamName.OCRCheckChar });

        OcrType ocrType = (OcrType)paramList.getValueAt(HoneywellParamName.OCR);
        boolean enabled = ocrType != OcrType.OffAll;
        if (enabled) {
            this.spnOcrType.setValue(ocrType);
            this.edtOcrTemplate.setText((String)paramList.getValueAt(HoneywellParamName.OCRTemplate));
            this.edtVariableG.setText((String)paramList.getValueAt(HoneywellParamName.OCRVarG));
            this.edtVariableH.setText((String)paramList.getValueAt(HoneywellParamName.OCRVarH));
            this.edtCheckChar.setText((String)paramList.getValueAt(HoneywellParamName.OCRCheckChar));
        }
        this.spnOcrType.setEnabled(enabled);
        this.edtOcrTemplate.setEnabled(enabled);
        this.edtVariableG.setEnabled(enabled);
        this.edtVariableH.setEnabled(enabled);
        this.edtCheckChar.setEnabled(enabled);
    }
}
