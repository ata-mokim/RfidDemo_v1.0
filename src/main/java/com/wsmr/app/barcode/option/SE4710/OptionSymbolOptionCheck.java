package com.wsmr.app.barcode.option.SE4710;

import com.wsmr.lib.dev.ATScanManager;
import com.wsmr.lib.dev.ATScanner;
import com.wsmr.lib.dev.barcode.params.ATScanSE4710Parameter;
import com.wsmr.lib.diagnostics.ATLog;
import com.wsmr.lib.dev.barcode.motorola.param.SSIParamName;
import com.wsmr.lib.dev.barcode.motorola.param.SSIParamValueList;

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
import android.widget.TextView;
import android.widget.Toast;
import com.wsmr.lib.dev.barcode.honeywell.type.EncodingType;
import com.wsmr.lib.dev.barcode.motorola.type.UpcCompositeMode;
import com.wsmr.lib.dev.barcode.motorola.type.CompositeBeepMode;
import com.wsmr.lib.dev.barcode.motorola.type.SSIInverseType;
import com.wsmr.app.barcode.R;
import com.wsmr.app.barcode.adapter.ValueAdapter;
import com.wsmr.app.barcode.adapter.ValueItem;
import com.wsmr.app.barcode.type.SE4710.Scan1dSymbolOption;

public class OptionSymbolOptionCheck extends Activity implements OnClickListener {

    private static final String TAG = "OptionSymbolOptionCheck";
    private ATScanner mScanner;
    private ATScanSE4710Parameter mParam;

    private CheckBox chkOption1;
    private CheckBox chkOption2;
    private CheckBox chkOption3;
    private LinearLayout lnlOption;
    private TextView txtOption4;
    private Spinner spnOption4;
    private Button btnSetOption;

    private TextView txtUpcCompositeMode;
    private TextView txtCompositeBeepMode;
    private CheckBox chkGS1EmulMode;
    private Spinner spnCompositeMode;
    private ValueAdapter<UpcCompositeMode> adpCompositeMode;
    private Spinner spnCompositeBeepMode;
    private ValueAdapter<CompositeBeepMode> adpCompositeBeepMode;

    private Scan1dSymbolOption symbol_type;
    private ValueAdapter<EncodingType> encodingAdapter;
    private ValueAdapter<SSIInverseType> inverseAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_option_2d_symbol_option_check);
        getActionBar().setDisplayHomeAsUpEnabled(true);

        // Initialize Scanner Instance
        this.mScanner = ATScanManager.getInstance(getApplicationContext());
        this.mParam = (ATScanSE4710Parameter) this.mScanner.getParameter();
        this.symbol_type = (Scan1dSymbolOption) getIntent().getSerializableExtra("Symbol_Type");

        // Initialize Widgets
        this.chkOption1 = (CheckBox) findViewById(R.id.symbol_enable);
        this.chkOption2 = (CheckBox) findViewById(R.id.symbol_opt1);
        this.chkOption3 = (CheckBox) findViewById(R.id.symbol_opt2);
        this.lnlOption = (LinearLayout) findViewById(R.id.symbol_opt3__lnl);
        this.txtOption4 = (TextView) findViewById(R.id.symbol_opt3_txt);
        this.spnOption4 = (Spinner) findViewById(R.id.symbol_opt3);

        ATLog.d(TAG, "symbol_type : %s", symbol_type.toString());
        this.txtUpcCompositeMode = (TextView) findViewById(R.id.symbol_upcCompositeMode);
        this.txtCompositeBeepMode = (TextView) findViewById(R.id.symbol_CompositeBeepMode);
        this.chkGS1EmulMode = (CheckBox) findViewById(R.id.symbol_GS1_128EmulMode);
        this.spnCompositeMode = (Spinner) findViewById(R.id.spinnerUpcCompositeMode);
        this.adpCompositeMode = new ValueAdapter<UpcCompositeMode>(this);
        this.spnCompositeBeepMode = (Spinner) findViewById(R.id.spinnerCompositeBeepMode);
        this.adpCompositeBeepMode = new ValueAdapter<CompositeBeepMode>(this);

        if (symbol_type != Scan1dSymbolOption.Composite) {
            this.txtUpcCompositeMode.setVisibility(View.INVISIBLE);
            this.txtCompositeBeepMode.setVisibility(View.INVISIBLE);
            this.chkGS1EmulMode.setVisibility(View.INVISIBLE);
            this.spnCompositeMode.setVisibility(View.INVISIBLE);
            this.spnCompositeBeepMode.setVisibility(View.INVISIBLE);
        }

        if (symbol_type == Scan1dSymbolOption.Australia_Post) {
            this.encodingAdapter = new ValueAdapter<EncodingType>(this);
            for (EncodingType item : EncodingType.values()) {
                this.encodingAdapter.add(new ValueItem<EncodingType>(item.toString(), item));
            }

            this.encodingAdapter.notifyDataSetChanged();
            this.spnOption4.setAdapter(encodingAdapter);
        } else {
            this.inverseAdapter = new ValueAdapter<SSIInverseType>(this);
            for (SSIInverseType item : SSIInverseType.values()) {
                this.inverseAdapter.add(new ValueItem<SSIInverseType>(item.toString(), item));
            }

            this.inverseAdapter.notifyDataSetChanged();
            this.spnOption4.setAdapter(inverseAdapter);
        }

        this.btnSetOption = (Button) findViewById(R.id.set_option);
        this.btnSetOption.setOnClickListener(this);

        // Initialize Symbol State List
        enableSymbolOptionClear();
        initSymbolStateList();
    }

    @Override
    protected void onDestroy() {
        symbol_type = null;

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
                case Aztec:
                    paramList.add(SSIParamName.Aztec, this.chkOption1.isChecked());
                    paramList.add(SSIParamName.Aztec_Inverse, this.spnOption4.getSelectedItemPosition());
                    break;
                case Australia_Post:
                    paramList.add(SSIParamName.Australia_Post, this.chkOption1.isChecked());
                    paramList.add(SSIParamName.Australia_Post_Format, this.spnOption4.getSelectedItemPosition());
                    break;
                case Data_Matrix:
                    paramList.add(SSIParamName.Data_Matrix, this.chkOption1.isChecked());
                    paramList.add(SSIParamName.Data_Matrix_Inverse, this.spnOption4.getSelectedItemPosition());
                    break;
                case Korea_3of5:
                    paramList.add(SSIParamName.Korea_3of5, this.chkOption1.isChecked());
                    // paramList.add(SSIParamName.Inverse_1D,
                    // this.spnOption4.getSelectedItemPosition());
                    break;
                case QRCode:
                    paramList.add(SSIParamName.QRCode, this.chkOption1.isChecked());
                    paramList.add(SSIParamName.QR_Inverse, this.spnOption4.getSelectedItemPosition());
                    break;
                case Composite:
                    paramList.add(SSIParamName.Composite_CC_C, this.chkOption1.isChecked());
                    paramList.add(SSIParamName.Composite_CC_AB, this.chkOption2.isChecked());
                    paramList.add(SSIParamName.Composite_TLC_39, this.chkOption3.isChecked());
                    paramList.add(SSIParamName.GS1_128_Emulation_Mode_for_Composite_Codes, this.chkGS1EmulMode.isChecked());
                    paramList.add(SSIParamName.UPC_Composite_Mode,
                            this.adpCompositeMode.getItem(this.spnCompositeMode.getSelectedItemPosition()));
                    paramList.add(SSIParamName.Composite_Beep_Mode,
                            this.adpCompositeBeepMode.getItem(this.spnCompositeBeepMode.getSelectedItemPosition()));
                    break;
                case GS1_128:
                    paramList.add(SSIParamName.UCC_EAN_128, this.chkOption1.isChecked());
                    break;
                case ISSN_EAN:
                    paramList.add(SSIParamName.ISSN_EAN, this.chkOption1.isChecked());
                    break;
                case ISBT_128:
                    paramList.add(SSIParamName.ISBT128, this.chkOption1.isChecked());
                    break;
                case Japan_Postal:
                    paramList.add(SSIParamName.Japan_Postal, this.chkOption1.isChecked());
                    break;
                case Maxicode:
                    paramList.add(SSIParamName.Maxicode, this.chkOption1.isChecked());
                    break;
                case MicroPDF417:
                    paramList.add(SSIParamName.MicroPDF417, this.chkOption1.isChecked());
                    break;
                case MicroQR:
                    paramList.add(SSIParamName.MicroQR, this.chkOption1.isChecked());
                    break;
                case Netherlands_KIX_Code:
                    paramList.add(SSIParamName.Netherlands_KIX_Code, this.chkOption1.isChecked());
                    break;
                case PDF417:
                    paramList.add(SSIParamName.PDF417, this.chkOption1.isChecked());
                    break;
                case US_Postnet:
                    paramList.add(SSIParamName.US_Postnet, this.chkOption1.isChecked());
                    this.chkOption1.setChecked((Boolean) paramList.getValueAt(SSIParamName.US_Postnet));
                    break;
                case US_Planet:
                    paramList.add(SSIParamName.US_Planet, this.chkOption1.isChecked());
                    break;
                case USPS_4CB_OneCode_Inteligent_Mail:
                    paramList.add(SSIParamName.USPS_4CB_OneCode_Intelligent_Mail, this.chkOption1.isChecked());
                    break;
                case UPU_FICS_Postral:
                    paramList.add(SSIParamName.UPU_FICS_Postal, this.chkOption1.isChecked());
                    break;
                case HanXin:
                    paramList.add(SSIParamName.HanXin, this.chkOption1.isChecked());
                    paramList.add(SSIParamName.HanXinInverse,
                            this.inverseAdapter.getItem(this.spnOption4.getSelectedItemPosition()));
                    break;
                case Ch2of5:
                    paramList.add(SSIParamName.Ch2of5, this.chkOption1.isChecked());
                    break;
                default:
                    break;
            }

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
        SSIParamValueList paramList = this.mParam.getParams(new SSIParamName[] { SSIParamName.Composite_CC_C,
                SSIParamName.Composite_CC_AB, SSIParamName.Composite_TLC_39, SSIParamName.UCC_EAN_128,
                SSIParamName.ISSN_EAN, SSIParamName.ISBT128, SSIParamName.Japan_Postal, SSIParamName.Maxicode,
                SSIParamName.MicroPDF417, SSIParamName.MicroQR, SSIParamName.Netherlands_KIX_Code, SSIParamName.PDF417,
                SSIParamName.US_Postnet, SSIParamName.US_Planet, SSIParamName.USPS_4CB_OneCode_Intelligent_Mail,
                SSIParamName.UPU_FICS_Postal, SSIParamName.Aztec, SSIParamName.Aztec_Inverse,
                SSIParamName.Australia_Post, SSIParamName.Australia_Post_Format, SSIParamName.Data_Matrix,
                SSIParamName.Data_Matrix_Inverse,
                SSIParamName.Korea_3of5, /* SSIParamName.Inverse_1D, */
                SSIParamName.QRCode, SSIParamName.QR_Inverse, SSIParamName.UPC_Composite_Mode,
                SSIParamName.Composite_Beep_Mode, SSIParamName.HanXin, SSIParamName.HanXinInverse, SSIParamName.Ch2of5,
                SSIParamName.GS1_128_Emulation_Mode_for_Composite_Codes });

        if ((symbol_type == Scan1dSymbolOption.Aztec) || (symbol_type == Scan1dSymbolOption.Australia_Post)
                || (symbol_type == Scan1dSymbolOption.Data_Matrix) /*
																	 * ||
																	 * (symbol_type
																	 * ==
																	 * Scan1dSymbolOption
																	 * .
																	 * Korea_3of5)
																	 */ || (symbol_type == Scan1dSymbolOption.QRCode)
                || (symbol_type == Scan1dSymbolOption.HanXin)) {

            this.chkOption1.setText(R.string.symbol_enable);
            this.chkOption1.setVisibility(View.VISIBLE);
            this.lnlOption.setVisibility(View.VISIBLE);

            switch (symbol_type) {
                case Aztec:
                    this.txtOption4.setText("Aztec Inverse");
                    this.chkOption1.setChecked((Boolean) paramList.getValueAt(SSIParamName.Aztec));
                    this.spnOption4.setSelection((Integer) paramList.getValueAt(SSIParamName.Aztec_Inverse));
                    this.setTitle(R.string.symbol_aztec_name);
                    break;
                case Australia_Post:
                    this.txtOption4.setText("Australia Post Format");
                    this.chkOption1.setChecked((Boolean) paramList.getValueAt(SSIParamName.Australia_Post));
                    this.spnOption4.setSelection((Integer) paramList.getValueAt(SSIParamName.Australia_Post_Format));
                    this.setTitle(R.string.symbol_australiapost_name);
                    break;
                case Data_Matrix:
                    this.txtOption4.setText("Data Matrix Inverse");
                    this.chkOption1.setChecked((Boolean) paramList.getValueAt(SSIParamName.Data_Matrix));
                    this.spnOption4.setSelection((Integer) paramList.getValueAt(SSIParamName.Data_Matrix_Inverse));
                    this.setTitle(R.string.symbol_datamatrix_name);
                    break;
                case Korea_3of5:
                    this.chkOption1.setChecked((Boolean) paramList.getValueAt(SSIParamName.Korea_3of5));
                    this.setTitle(R.string.symbol_korea3of5_name);
                    break;
                case QRCode:
                    this.txtOption4.setText("QR Inverse");
                    this.chkOption1.setChecked((Boolean) paramList.getValueAt(SSIParamName.QRCode));
                    this.spnOption4.setSelection((Integer) paramList.getValueAt(SSIParamName.QR_Inverse));
                    this.setTitle(R.string.symbol_qrcode_name);
                    break;
                case HanXin:
                    this.chkOption1.setChecked((Boolean) paramList.getValueAt(SSIParamName.HanXin));
                    this.txtOption4.setText("Han Xin Inverse");
                    this.spnOption4.setSelection(this.inverseAdapter
                            .getPosition((SSIInverseType) paramList.getValueAt(SSIParamName.HanXinInverse)));
                    this.setTitle(R.string.symbol_hanxin_name);
                    break;
                default:
                    break;
            }
        } else if (symbol_type == Scan1dSymbolOption.Composite) {
            this.chkOption1.setText("Composite CC-C");
            this.chkOption2.setText("Composite CC-AB");
            this.chkOption3.setText("Composite TLC-39");
            this.txtUpcCompositeMode.setText("UPC Composite Mode");
            this.txtCompositeBeepMode.setText("Composite Beep Mode");
            this.chkGS1EmulMode.setText("GS1-128 Emulation Mode for UCC/EAN Composite Codes");
            this.setTitle(R.string.symbol_compostite_name);

            for (UpcCompositeMode item : UpcCompositeMode.values()) {
                this.adpCompositeMode.add(new ValueItem<UpcCompositeMode>(item.toString(), item));
            }
            this.spnCompositeMode.setAdapter(this.adpCompositeMode);

            for (CompositeBeepMode item : CompositeBeepMode.values()) {
                this.adpCompositeBeepMode.add(new ValueItem<CompositeBeepMode>(item.toString(), item));
            }
            this.spnCompositeBeepMode.setAdapter(this.adpCompositeBeepMode);

            this.chkOption1.setVisibility(View.VISIBLE);
            this.chkOption2.setVisibility(View.VISIBLE);
            this.chkOption3.setVisibility(View.VISIBLE);

            this.chkOption1.setChecked((Boolean) paramList.getValueAt(SSIParamName.Composite_CC_C));
            this.chkOption2.setChecked((Boolean) paramList.getValueAt(SSIParamName.Composite_CC_AB));
            this.chkOption3.setChecked((Boolean) paramList.getValueAt(SSIParamName.Composite_TLC_39));

            this.spnCompositeMode.setSelection(this.adpCompositeMode
                    .getPosition((UpcCompositeMode) paramList.getValueAt(SSIParamName.UPC_Composite_Mode)));

            this.spnCompositeBeepMode.setSelection(this.adpCompositeBeepMode
                    .getPosition((CompositeBeepMode) paramList.getValueAt(SSIParamName.Composite_Beep_Mode)));

            this.chkGS1EmulMode.setChecked(
                    (Boolean) paramList.getValueAt(SSIParamName.GS1_128_Emulation_Mode_for_Composite_Codes));

        } else {
            switch (symbol_type) {
                case GS1_128:
                    this.chkOption1.setChecked((Boolean) paramList.getValueAt(SSIParamName.UCC_EAN_128));
                    break;
                case ISSN_EAN:
                    this.chkOption1.setChecked((Boolean) paramList.getValueAt(SSIParamName.ISSN_EAN));
                    this.setTitle(R.string.symbol_issnean_name);
                    break;
                case ISBT_128:
                    this.chkOption1.setChecked((Boolean) paramList.getValueAt(SSIParamName.ISBT128));
                    break;
                case Japan_Postal:
                    this.chkOption1.setChecked((Boolean) paramList.getValueAt(SSIParamName.Japan_Postal));
                    this.setTitle(R.string.symbol_japanpostal_name);
                    break;
                case Maxicode:
                    this.chkOption1.setChecked((Boolean) paramList.getValueAt(SSIParamName.Maxicode));
                    this.setTitle(R.string.symbol_maxicode_name);
                    break;
                case MicroPDF417:
                    this.chkOption1.setChecked((Boolean) paramList.getValueAt(SSIParamName.MicroPDF417));
                    this.setTitle(R.string.symbol_micropdf417_name);
                    break;
                case MicroQR:
                    this.chkOption1.setChecked((Boolean) paramList.getValueAt(SSIParamName.MicroQR));
                    this.setTitle(R.string.symbol_microqr_name);
                    break;
                case Netherlands_KIX_Code:
                    this.chkOption1.setChecked((Boolean) paramList.getValueAt(SSIParamName.Netherlands_KIX_Code));
                    this.setTitle(R.string.symbol_kixcode_name);
                    break;
                case PDF417:
                    this.chkOption1.setChecked((Boolean) paramList.getValueAt(SSIParamName.PDF417));
                    this.setTitle(R.string.symbol_pdf417_name);
                    break;
                case US_Postnet:
                    this.chkOption1.setChecked((Boolean) paramList.getValueAt(SSIParamName.US_Postnet));
                    this.setTitle(R.string.symbol_uspostnet_name);
                    break;
                case US_Planet:
                    this.chkOption1.setChecked((Boolean) paramList.getValueAt(SSIParamName.US_Planet));
                    this.setTitle(R.string.symbol_usplanet_name);
                    break;
                case USPS_4CB_OneCode_Inteligent_Mail:
                    this.chkOption1
                            .setChecked((Boolean) paramList.getValueAt(SSIParamName.USPS_4CB_OneCode_Intelligent_Mail));
                    this.setTitle(R.string.symbol_intelligentmail_name);
                    break;
                case UPU_FICS_Postral:
                    this.chkOption1.setChecked((Boolean) paramList.getValueAt(SSIParamName.UPU_FICS_Postal));
                    this.setTitle(R.string.symbol_ficspostal_name);
                    break;
                case Korea_3of5:
                    this.chkOption1.setChecked((Boolean) paramList.getValueAt(SSIParamName.Korea_3of5));
                    this.setTitle(R.string.symbol_korea3of5_name);
                    break;
                case Ch2of5:
                    this.chkOption1.setChecked((Boolean) paramList.getValueAt(SSIParamName.Ch2of5));
                    this.setTitle(R.string.symbol_ch2of5_name);
                    break;
                default:
                    break;
            }
            this.chkOption1.setText(R.string.symbol_enable);
            this.chkOption1.setVisibility(View.VISIBLE);
        }
    }

    private void enableSymbolOptionClear() {
        this.chkOption1.setText("");
        this.chkOption2.setText("");
        this.chkOption3.setText("");
        this.chkOption1.setChecked(false);
        this.chkOption2.setChecked(false);
        this.chkOption3.setChecked(false);
        this.chkOption1.setVisibility(View.GONE);
        this.chkOption2.setVisibility(View.GONE);
        this.chkOption3.setVisibility(View.GONE);
        this.lnlOption.setVisibility(View.GONE);
        this.spnOption4.setSelection(0);
    }
}
