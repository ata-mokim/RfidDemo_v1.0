package com.wsmr.app.barcode.option.IT5x80;

import com.wsmr.app.barcode.R;
import com.wsmr.lib.dev.ATScanManager;
import com.wsmr.lib.dev.barcode.honeywell.param.HoneywellParamName;
import com.wsmr.lib.dev.barcode.honeywell.param.HoneywellParamValueList;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.Toast;

public class OptionSymbolAddenda extends OptionSymbol implements
        OnClickListener {

    private CheckBox chkCheckDigit;
    private CheckBox chkAddenda2Digit;
    private CheckBox chkAddenda5Digit;
    private CheckBox chkAddendaRequired;
    private CheckBox chkAddendaSeparator;
    private CheckBox chkNumberSystem;
    private CheckBox chkExtCouponCode;
    private CheckBox chkExpand;
    private CheckBox chkISBNTranslate;
    private Button btnSetOption;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_option_2d_symbol_addenda);
        getActionBar().setDisplayHomeAsUpEnabled(true);

        // Initialize Widgets
        this.chkCheckDigit = (CheckBox) findViewById(R.id.check_digit);
        this.chkAddenda2Digit = (CheckBox) findViewById(R.id.addenda_2_digit);
        this.chkAddenda5Digit = (CheckBox) findViewById(R.id.addenda_5_digit);
        this.chkAddendaRequired = (CheckBox) findViewById(R.id.addenda_required);
        this.chkAddendaSeparator = (CheckBox) findViewById(R.id.addenda_separator);
        this.chkNumberSystem = (CheckBox) findViewById(R.id.number_system);
        this.chkExtCouponCode = (CheckBox) findViewById(R.id.ext_coupon_code);
        this.chkExpand = (CheckBox) findViewById(R.id.expand);
        this.chkISBNTranslate = (CheckBox) findViewById(R.id.isbn_translate);
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

            switch (this.mSymbolType) {
                case UPCA:
                    paramList.add(HoneywellParamName.UPCACheckDigit, this.chkCheckDigit.isChecked());
                    paramList.add(HoneywellParamName.UPCA2DigitAdd, this.chkAddenda2Digit.isChecked());
                    paramList.add(HoneywellParamName.UPCA5DigitAdd, this.chkAddenda5Digit.isChecked());
                    paramList.add(HoneywellParamName.UPCAAddReq, this.chkAddendaRequired.isChecked());
                    paramList.add(HoneywellParamName.UPCAAddSep, this.chkAddendaSeparator.isChecked());
                    paramList.add(HoneywellParamName.UPCANumberSystem, this.chkNumberSystem.isChecked());
                    paramList.add(HoneywellParamName.UPCACouponCode, this.chkExtCouponCode.isChecked());
                    break;
                case UPCE0:
                    paramList.add(HoneywellParamName.UPCE0CheckDigit, this.chkCheckDigit.isChecked());
                    paramList.add(HoneywellParamName.UPCE02DigitAdd, this.chkAddenda2Digit.isChecked());
                    paramList.add(HoneywellParamName.UPCE05DigitAdd, this.chkAddenda5Digit.isChecked());
                    paramList.add(HoneywellParamName.UPCE0AddReq, this.chkAddendaRequired.isChecked());
                    paramList.add(HoneywellParamName.UPCE0AddSep, this.chkAddendaSeparator.isChecked());
                    paramList.add(HoneywellParamName.UPCE0NumberSystem, this.chkNumberSystem.isChecked());
                    paramList.add(HoneywellParamName.UPCE0Expand, this.chkExpand.isChecked());
                    break;
                case EAN13:
                    paramList.add(HoneywellParamName.EAN13CheckDigit, this.chkCheckDigit.isChecked());
                    paramList.add(HoneywellParamName.EAN132DigitAdd, this.chkAddenda2Digit.isChecked());
                    paramList.add(HoneywellParamName.EAN135DigitAdd, this.chkAddenda5Digit.isChecked());
                    paramList.add(HoneywellParamName.EAN13AddReq, this.chkAddendaRequired.isChecked());
                    paramList.add(HoneywellParamName.EAN13AddSep, this.chkAddendaSeparator.isChecked());
                    paramList.add(HoneywellParamName.IsbnTranslate, this.chkISBNTranslate.isChecked());
                    break;
                case EAN8:
                    paramList.add(HoneywellParamName.EAN8CheckDigit, this.chkCheckDigit.isChecked());
                    paramList.add(HoneywellParamName.EAN82DigitAdd, this.chkAddenda2Digit.isChecked());
                    paramList.add(HoneywellParamName.EAN85DigitAdd, this.chkAddenda5Digit.isChecked());
                    paramList.add(HoneywellParamName.EAN8AddReq, this.chkAddendaRequired.isChecked());
                    paramList.add(HoneywellParamName.EAN8AddSep, this.chkAddendaSeparator.isChecked());
                    break;
                default:
                    return;
            }

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
        HoneywellParamValueList paramList = null;

        switch (this.mSymbolType) {
            case UPCA:
                this.chkNumberSystem.setVisibility(View.VISIBLE);
                this.chkExtCouponCode.setVisibility(View.VISIBLE);
                this.chkExpand.setVisibility(View.GONE);
                this.chkISBNTranslate.setVisibility(View.GONE);

                paramList = this.mParam.getParams(new HoneywellParamName[] {
                        HoneywellParamName.UPCACheckDigit, HoneywellParamName.UPCA2DigitAdd,
                        HoneywellParamName.UPCA5DigitAdd, HoneywellParamName.UPCAAddReq,
                        HoneywellParamName.UPCAAddSep, HoneywellParamName.UPCANumberSystem,
                        HoneywellParamName.UPCACouponCode });

                this.chkCheckDigit.setChecked((Boolean) paramList
                        .getValueAt(HoneywellParamName.UPCACheckDigit));
                this.chkAddenda2Digit.setChecked((Boolean) paramList
                        .getValueAt(HoneywellParamName.UPCA2DigitAdd));
                this.chkAddenda5Digit.setChecked((Boolean) paramList
                        .getValueAt(HoneywellParamName.UPCA5DigitAdd));
                this.chkAddendaRequired.setChecked((Boolean) paramList
                        .getValueAt(HoneywellParamName.UPCAAddReq));
                this.chkAddendaSeparator.setChecked((Boolean) paramList
                        .getValueAt(HoneywellParamName.UPCAAddSep));
                this.chkNumberSystem.setChecked((Boolean) paramList
                        .getValueAt(HoneywellParamName.UPCANumberSystem));
                this.chkExtCouponCode.setChecked((Boolean) paramList
                        .getValueAt(HoneywellParamName.UPCACouponCode));
                break;
            case UPCE0:
                this.chkNumberSystem.setVisibility(View.VISIBLE);
                this.chkExtCouponCode.setVisibility(View.GONE);
                this.chkExpand.setVisibility(View.VISIBLE);
                this.chkISBNTranslate.setVisibility(View.GONE);

                paramList = this.mParam.getParams(new HoneywellParamName[] {
                        HoneywellParamName.UPCE0CheckDigit, HoneywellParamName.UPCE02DigitAdd,
                        HoneywellParamName.UPCE05DigitAdd, HoneywellParamName.UPCE0AddReq,
                        HoneywellParamName.UPCE0AddSep, HoneywellParamName.UPCE0NumberSystem,
                        HoneywellParamName.UPCE0Expand });

                this.chkCheckDigit.setChecked((Boolean) paramList
                        .getValueAt(HoneywellParamName.UPCE0CheckDigit));
                this.chkAddenda2Digit.setChecked((Boolean) paramList
                        .getValueAt(HoneywellParamName.UPCE02DigitAdd));
                this.chkAddenda5Digit.setChecked((Boolean) paramList
                        .getValueAt(HoneywellParamName.UPCE05DigitAdd));
                this.chkAddendaRequired.setChecked((Boolean) paramList
                        .getValueAt(HoneywellParamName.UPCE0AddReq));
                this.chkAddendaSeparator.setChecked((Boolean) paramList
                        .getValueAt(HoneywellParamName.UPCE0AddSep));
                this.chkNumberSystem.setChecked((Boolean) paramList
                        .getValueAt(HoneywellParamName.UPCE0NumberSystem));
                this.chkExpand.setChecked((Boolean) paramList
                        .getValueAt(HoneywellParamName.UPCE0Expand));
                break;
            case EAN13:
                this.chkNumberSystem.setVisibility(View.GONE);
                this.chkExtCouponCode.setVisibility(View.GONE);
                this.chkExpand.setVisibility(View.GONE);
                this.chkISBNTranslate.setVisibility(View.VISIBLE);

                paramList = this.mParam.getParams(new HoneywellParamName[] {
                        HoneywellParamName.EAN13CheckDigit, HoneywellParamName.EAN132DigitAdd,
                        HoneywellParamName.EAN135DigitAdd, HoneywellParamName.EAN13AddReq,
                        HoneywellParamName.EAN13AddSep, HoneywellParamName.IsbnTranslate });
                this.chkCheckDigit.setChecked((Boolean) paramList
                        .getValueAt(HoneywellParamName.EAN13CheckDigit));
                this.chkAddenda2Digit.setChecked((Boolean) paramList
                        .getValueAt(HoneywellParamName.EAN132DigitAdd));
                this.chkAddenda5Digit.setChecked((Boolean) paramList
                        .getValueAt(HoneywellParamName.EAN135DigitAdd));
                this.chkAddendaRequired.setChecked((Boolean) paramList
                        .getValueAt(HoneywellParamName.EAN13AddReq));
                this.chkAddendaSeparator.setChecked((Boolean) paramList
                        .getValueAt(HoneywellParamName.EAN13AddSep));
                this.chkISBNTranslate.setChecked((Boolean) paramList
                        .getValueAt(HoneywellParamName.IsbnTranslate));
                break;
            case EAN8:
                this.chkNumberSystem.setVisibility(View.GONE);
                this.chkExtCouponCode.setVisibility(View.GONE);
                this.chkExpand.setVisibility(View.GONE);
                this.chkISBNTranslate.setVisibility(View.GONE);

                paramList = this.mParam.getParams(new HoneywellParamName[] {
                        HoneywellParamName.EAN8CheckDigit, HoneywellParamName.EAN82DigitAdd,
                        HoneywellParamName.EAN85DigitAdd, HoneywellParamName.EAN8AddReq,
                        HoneywellParamName.EAN8AddSep });
                this.chkCheckDigit.setChecked((Boolean) paramList
                        .getValueAt(HoneywellParamName.EAN8CheckDigit));
                this.chkAddenda2Digit.setChecked((Boolean) paramList
                        .getValueAt(HoneywellParamName.EAN82DigitAdd));
                this.chkAddenda5Digit.setChecked((Boolean) paramList
                        .getValueAt(HoneywellParamName.EAN85DigitAdd));
                this.chkAddendaRequired.setChecked((Boolean) paramList
                        .getValueAt(HoneywellParamName.EAN8AddReq));
                this.chkAddendaSeparator.setChecked((Boolean) paramList
                        .getValueAt(HoneywellParamName.EAN8AddSep));
                break;
            default:
                break;
        }
    }
}
