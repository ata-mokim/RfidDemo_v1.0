package com.wsmr.app.barcode.option.IT5x80;

import com.wsmr.lib.dev.ATScanManager;
import com.wsmr.lib.dev.barcode.honeywell.param.HoneywellParamName;
import com.wsmr.lib.dev.barcode.honeywell.param.HoneywellParamValueList;
import com.wsmr.lib.dev.barcode.honeywell.type.CheckCharType;
import com.wsmr.lib.dev.barcode.honeywell.type.CheckDigitRequiredType;
import com.wsmr.lib.dev.barcode.honeywell.type.CodePageType;
import com.wsmr.lib.dev.barcode.honeywell.type.ComCodeEmulationType;
import com.wsmr.lib.dev.barcode.honeywell.type.MSICheckCharType;
import com.wsmr.lib.dev.barcode.honeywell.type.PosiCodeLimitType;
import com.wsmr.lib.dev.barcode.honeywell.type.TelepenOutputType;
import com.wsmr.lib.dev.barcode.honeywell.type.TryStateType;

import com.wsmr.app.barcode.R;
import com.wsmr.app.barcode.type.IT5x80.SymbolLength;
import com.wsmr.app.barcode.widget.ValueSpinner;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.Toast;

public class OptionSymbolComplexLength<T> extends OptionSymbol implements
        OnClickListener {

    private CheckBox chkStartStopCharTransmit;
    private LinearLayout lytCheckChar;
    private ValueSpinner<CheckCharType> spnCheckChar;
    private LinearLayout lytTelepenOutput;
    private ValueSpinner<TelepenOutputType> spnTelepenOutput;
    private LinearLayout lytCheckDigitRequired;
    private ValueSpinner<CheckDigitRequiredType> spnCheckDigitRequired;
    private LinearLayout lytPosicodeLimited;
    private ValueSpinner<PosiCodeLimitType> spnPosicodeLimited;
    private LinearLayout lytEmulation;
    private ValueSpinner<ComCodeEmulationType> spnEmulation;
    private SymbolLength length;
    private LinearLayout lytConcatenation;
    private ValueSpinner<TryStateType> spnConcatenation;
    private CheckBox chkAppend;
    private CheckBox chkPharmaceutical;
    private CheckBox chkFullAscii;
    private CheckBox chkISBTConcatenation;
    private CheckBox chkAztecRunes;
    private LinearLayout lytCodePage;
    private ValueSpinner<CodePageType> spnCodePage;
    private Button btnSetOption;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_option_2d_symbol_complex_length);
        getActionBar().setDisplayHomeAsUpEnabled(true);

        // Initialize Widgets
        this.chkStartStopCharTransmit = (CheckBox) findViewById(R.id.start_stop_char_transmit);
        this.lytCheckChar = (LinearLayout) findViewById(R.id.layout_check_char);
        this.spnCheckChar = new ValueSpinner<CheckCharType>(this, R.id.check_char,
                CheckCharType.values());
        this.lytTelepenOutput = (LinearLayout) findViewById(R.id.layout_telepen_output);
        this.spnTelepenOutput = new ValueSpinner<TelepenOutputType>(this, R.id.telepen_output,
                TelepenOutputType.values());
        this.lytCheckDigitRequired = (LinearLayout) findViewById(R.id.layout_check_digit_required);
        this.spnCheckDigitRequired = new ValueSpinner<CheckDigitRequiredType>(this,
                R.id.check_digit_required, CheckDigitRequiredType.values());
        this.lytPosicodeLimited = (LinearLayout) findViewById(R.id.layout_posicode_limited);
        this.spnPosicodeLimited = new ValueSpinner<PosiCodeLimitType>(this,
                R.id.posicode_limited, PosiCodeLimitType.values());
        this.lytEmulation = (LinearLayout) findViewById(R.id.layout_emulation);
        this.spnEmulation = new ValueSpinner<ComCodeEmulationType>(this, R.id.emulation,
                ComCodeEmulationType.values());
        this.lytConcatenation = (LinearLayout) findViewById(R.id.layout_concatenation);
        this.spnConcatenation = new ValueSpinner<TryStateType>(this, R.id.concatenation,
                TryStateType.values());
        this.chkAppend = (CheckBox) findViewById(R.id.append);
        this.chkPharmaceutical = (CheckBox) findViewById(R.id.pharmaceutical);
        this.chkFullAscii = (CheckBox) findViewById(R.id.full_ascii);
        this.chkISBTConcatenation = (CheckBox) findViewById(R.id.isbt_concatenation);
        this.chkAztecRunes = (CheckBox) findViewById(R.id.aztec_runes);
        this.lytCodePage = (LinearLayout) findViewById(R.id.layout_code_page);
        this.spnCodePage = new ValueSpinner<CodePageType>(this, R.id.code_page,
                CodePageType.values());
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
                case Codabar:
                    paramList.add(HoneywellParamName.CodabarStartStopChar,
                            this.chkStartStopCharTransmit.isChecked());
                    paramList.add(HoneywellParamName.CodabarCheckChar,
                            this.spnCheckChar.getValue());
                    paramList.add(HoneywellParamName.CodabarLengthMin,
                            this.length.getMin().getValue());//2014-11-26 munhg
                    paramList.add(HoneywellParamName.CodabarLengthMax,
                            this.length.getMax().getValue());//2014-11-26 munhg
                    paramList.add(HoneywellParamName.CodabarConcatenation,
                            this.spnConcatenation.getValue());
                    break;
                case Code39:
                    paramList.add(HoneywellParamName.Code39StartStopChar,
                            this.chkStartStopCharTransmit.isChecked());
                    paramList.add(HoneywellParamName.Code39CheckChar,
                            this.spnCheckChar.getValue());
                    paramList
                            .add(HoneywellParamName.Code39LengthMin, this.length.getMin().getValue());//2014-11-26 munhg
                    paramList
                            .add(HoneywellParamName.Code39LengthMax, this.length.getMax().getValue());//2014-11-26 munhg
                    paramList.add(HoneywellParamName.Code39Append,
                            this.chkAppend.isChecked());
                    paramList.add(HoneywellParamName.Code39Pharmaceutical,
                            this.chkPharmaceutical.isChecked());
                    paramList.add(HoneywellParamName.Code39FullAscii,
                            this.chkFullAscii.isChecked());
                    paramList.add(HoneywellParamName.Code39CodePage,
                            this.spnCodePage.getValue());
                    break;
                case Code93:
                    paramList
                            .add(HoneywellParamName.Code93LengthMin, this.length.getMin().getValue());//2014-11-26 munhg
                    paramList
                            .add(HoneywellParamName.Code93LengthMax, this.length.getMax().getValue());//2014-11-26 munhg
                    paramList.add(HoneywellParamName.Code93CodePage,
                            this.spnCodePage.getValue());
                    break;
                case Code128:
                    paramList.add(HoneywellParamName.Code128LengthMin,
                            this.length.getMin().getValue());
                    paramList.add(HoneywellParamName.Code128LengthMax,
                            this.length.getMax().getValue());
                    paramList.add(HoneywellParamName.IsbtConcatenation,
                            this.chkISBTConcatenation.isChecked());
                    paramList.add(HoneywellParamName.Code128CodePage,
                            this.spnCodePage.getValue());
                    break;
                case AztecCode:
                    paramList.add(HoneywellParamName.AztecCodeLengthMin,
                            this.length.getMin().getValue());
                    paramList.add(HoneywellParamName.AztecCodeLengthMax,
                            this.length.getMax().getValue());
                    paramList.add(HoneywellParamName.AztecCodeRunes,
                            this.chkAztecRunes.isChecked());
                    break;
                case I2of5:
                    paramList.add(HoneywellParamName.I2of5CheckDigit,
                            this.spnCheckChar.getValue());
                    paramList.add(HoneywellParamName.I2of5LengthMin, this.length.getMin().getValue());//2014-11-26 munhg
                    paramList.add(HoneywellParamName.I2of5LengthMax, this.length.getMax().getValue());//2014-11-26 munhg
                    break;
                case Telepen:
                    paramList.add(HoneywellParamName.TelepenOutput,
                            this.spnTelepenOutput.getValue());
                    paramList.add(HoneywellParamName.TelepenLengthMin,
                            this.length.getMin().getValue());
                    paramList.add(HoneywellParamName.TelepenLengthMax,
                            this.length.getMax().getValue());
                    break;
                case MSI:
                    paramList.add(HoneywellParamName.MSICheckChar,
                            this.chkStartStopCharTransmit.isChecked());
                    paramList.add(HoneywellParamName.MSILengthMin, this.length.getMin().getValue());//2014-11-26 munhg
                    paramList.add(HoneywellParamName.MSILengthMax, this.length.getMax().getValue());//2014-11-26 munhg
                    break;
                case Code11:
                    paramList.add(HoneywellParamName.Code11CheckDigit,
                            this.spnCheckDigitRequired.getValue());
                    paramList
                            .add(HoneywellParamName.Code11LengthMin, this.length.getMin().getValue());//2014-11-26 munhg
                    paramList
                            .add(HoneywellParamName.Code11LengthMax, this.length.getMax().getValue());//2014-11-26 munhg
                    break;
                case PosiCode:
                    paramList.add(HoneywellParamName.PosiCodeLimit,
                            this.spnPosicodeLimited.getValue());
                    paramList.add(HoneywellParamName.PosiCodeLengthMin,
                            this.length.getMin().getValue());//2014-11-26 munhg
                    paramList.add(HoneywellParamName.PosiCodeLengthMax,
                            this.length.getMax().getValue());//2014-11-26 munhg
                    break;
                case ComCode:
                    paramList.add(HoneywellParamName.ComCodeEmulation,
                            this.spnEmulation.getValue());
                    paramList.add(HoneywellParamName.ComCodeLengthMin,
                            this.length.getMin().getValue());//2014-11-26 munhg
                    paramList.add(HoneywellParamName.ComCodeLengthMax,
                            this.length.getMax().getValue());//2014-11-26 munhg
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
        HoneywellParamValueList paramList;

        switch (this.mSymbolType) {
            case Codabar:
                this.length = new SymbolLength(this, HoneywellParamName.CodabarLengthMin,
                        HoneywellParamName.CodabarLengthMax, this.mParam);

                paramList = this.mParam.getParams(new HoneywellParamName[] {
                        HoneywellParamName.CodabarStartStopChar,
                        HoneywellParamName.CodabarCheckChar, HoneywellParamName.CodabarLengthMin,
                        HoneywellParamName.CodabarLengthMax,
                        HoneywellParamName.CodabarConcatenation });

                this.chkStartStopCharTransmit.setChecked((Boolean) paramList
                        .getValueAt(HoneywellParamName.CodabarStartStopChar));
                this.spnCheckChar.setValue(paramList
                        .getValueAt(HoneywellParamName.CodabarCheckChar));
                this.length.setValue(
                        paramList.getValueAt(HoneywellParamName.CodabarLengthMin),
                        paramList.getValueAt(HoneywellParamName.CodabarLengthMax));
                this.spnConcatenation.setValue(paramList
                        .getValueAt(HoneywellParamName.CodabarConcatenation));

                this.chkStartStopCharTransmit.setVisibility(View.VISIBLE);
                this.lytCheckChar.setVisibility(View.VISIBLE);
                this.lytTelepenOutput.setVisibility(View.GONE);
                this.lytCheckDigitRequired.setVisibility(View.GONE);
                this.lytPosicodeLimited.setVisibility(View.GONE);
                this.lytEmulation.setVisibility(View.GONE);
                this.lytConcatenation.setVisibility(View.VISIBLE);
                this.chkAppend.setVisibility(View.GONE);
                this.chkPharmaceutical.setVisibility(View.GONE);
                this.chkFullAscii.setVisibility(View.GONE);
                this.chkISBTConcatenation.setVisibility(View.GONE);
                this.chkAztecRunes.setVisibility(View.GONE);
                this.lytCodePage.setVisibility(View.GONE);
                break;
            case Code39:
                this.length = new SymbolLength(this, HoneywellParamName.Code39LengthMin,
                        HoneywellParamName.Code39LengthMax, this.mParam);

                paramList = this.mParam.getParams(new HoneywellParamName[] {
                        HoneywellParamName.Code39StartStopChar,
                        HoneywellParamName.Code39CheckChar, HoneywellParamName.Code39LengthMin,
                        HoneywellParamName.Code39LengthMax, HoneywellParamName.Code39Append,
                        HoneywellParamName.Code39Pharmaceutical,
                        HoneywellParamName.Code39FullAscii, HoneywellParamName.Code39CodePage });

                this.chkStartStopCharTransmit.setChecked((Boolean) paramList
                        .getValueAt(HoneywellParamName.Code39StartStopChar));
                this.spnCheckChar.setValue(paramList
                        .getValueAt(HoneywellParamName.Code39CheckChar));
                this.length.setValue(
                        paramList.getValueAt(HoneywellParamName.Code39LengthMin),
                        paramList.getValueAt(HoneywellParamName.Code39LengthMax));
                this.chkAppend.setChecked((Boolean) paramList
                        .getValueAt(HoneywellParamName.Code39Append));
                this.chkPharmaceutical.setChecked((Boolean) paramList
                        .getValueAt(HoneywellParamName.Code39Pharmaceutical));
                this.chkFullAscii.setChecked((Boolean) paramList
                        .getValueAt(HoneywellParamName.Code39FullAscii));
                this.spnCodePage.setValue(paramList
                        .getValueAt(HoneywellParamName.Code39CodePage));

                this.chkStartStopCharTransmit.setVisibility(View.VISIBLE);
                this.lytCheckChar.setVisibility(View.VISIBLE);
                this.lytTelepenOutput.setVisibility(View.GONE);
                this.lytCheckDigitRequired.setVisibility(View.GONE);
                this.lytPosicodeLimited.setVisibility(View.GONE);
                this.lytEmulation.setVisibility(View.GONE);
                this.lytConcatenation.setVisibility(View.GONE);
                this.chkAppend.setVisibility(View.VISIBLE);
                this.chkPharmaceutical.setVisibility(View.VISIBLE);
                this.chkFullAscii.setVisibility(View.VISIBLE);
                this.chkISBTConcatenation.setVisibility(View.GONE);
                this.chkAztecRunes.setVisibility(View.GONE);
                this.lytCodePage.setVisibility(View.VISIBLE);
                break;
            case Code93:
                this.length = new SymbolLength(this, HoneywellParamName.Code93LengthMin,
                        HoneywellParamName.Code93LengthMax, this.mParam);

                paramList = this.mParam.getParams(new HoneywellParamName[] {
                        HoneywellParamName.Code93LengthMin, HoneywellParamName.Code93LengthMax,
                        HoneywellParamName.Code93CodePage });

                this.length.setValue(
                        paramList.getValueAt(HoneywellParamName.Code93LengthMin),
                        paramList.getValueAt(HoneywellParamName.Code93LengthMax));
                this.spnCodePage.setValue(paramList
                        .getValueAt(HoneywellParamName.Code93CodePage));

                this.chkStartStopCharTransmit.setVisibility(View.GONE);
                this.lytCheckChar.setVisibility(View.GONE);
                this.lytTelepenOutput.setVisibility(View.GONE);
                this.lytCheckDigitRequired.setVisibility(View.GONE);
                this.lytPosicodeLimited.setVisibility(View.GONE);
                this.lytEmulation.setVisibility(View.GONE);
                this.lytConcatenation.setVisibility(View.GONE);
                this.chkAppend.setVisibility(View.GONE);
                this.chkPharmaceutical.setVisibility(View.GONE);
                this.chkFullAscii.setVisibility(View.GONE);
                this.chkISBTConcatenation.setVisibility(View.GONE);
                this.chkAztecRunes.setVisibility(View.GONE);
                this.lytCodePage.setVisibility(View.VISIBLE);
                break;
            case Code128:
                this.length = new SymbolLength(this, HoneywellParamName.Code128LengthMin,
                        HoneywellParamName.Code128LengthMax, this.mParam);

                paramList = this.mParam
                        .getParams(new HoneywellParamName[] {
                                HoneywellParamName.Code128LengthMin,
                                HoneywellParamName.Code128LengthMax,
                                HoneywellParamName.IsbtConcatenation,
                                HoneywellParamName.Code128CodePage });

                this.length.setValue(
                        paramList.getValueAt(HoneywellParamName.Code128LengthMin),
                        paramList.getValueAt(HoneywellParamName.Code128LengthMax));
                this.chkISBTConcatenation.setChecked((Boolean) paramList
                        .getValueAt(HoneywellParamName.IsbtConcatenation));
                this.spnCodePage.setValue(paramList
                        .getValueAt(HoneywellParamName.Code128CodePage));

                this.chkStartStopCharTransmit.setVisibility(View.GONE);
                this.lytCheckChar.setVisibility(View.GONE);
                this.lytTelepenOutput.setVisibility(View.GONE);
                this.lytCheckDigitRequired.setVisibility(View.GONE);
                this.lytPosicodeLimited.setVisibility(View.GONE);
                this.lytEmulation.setVisibility(View.GONE);
                this.lytConcatenation.setVisibility(View.GONE);
                this.chkAppend.setVisibility(View.GONE);
                this.chkPharmaceutical.setVisibility(View.GONE);
                this.chkFullAscii.setVisibility(View.GONE);
                this.chkISBTConcatenation.setVisibility(View.VISIBLE);
                this.chkAztecRunes.setVisibility(View.GONE);
                this.lytCodePage.setVisibility(View.VISIBLE);
                break;
            case AztecCode:
                this.length = new SymbolLength(this,
                        HoneywellParamName.AztecCodeLengthMin,
                        HoneywellParamName.AztecCodeLengthMax, this.mParam);

                paramList = this.mParam
                        .getParams(new HoneywellParamName[] {
                                HoneywellParamName.AztecCodeLengthMin,
                                HoneywellParamName.AztecCodeLengthMax,
                                HoneywellParamName.AztecCodeRunes });

                this.length.setValue(
                        paramList.getValueAt(HoneywellParamName.AztecCodeLengthMin),
                        paramList.getValueAt(HoneywellParamName.AztecCodeLengthMax));

                Object obj = paramList.getValueAt(HoneywellParamName.AztecCodeRunes);
                if(obj != null) {
                    this.chkAztecRunes.setChecked((Boolean) obj);
                } else {
                    this.chkAztecRunes.setEnabled(false);
                }

                this.chkStartStopCharTransmit.setVisibility(View.GONE);
                this.lytCheckChar.setVisibility(View.GONE);
                this.lytTelepenOutput.setVisibility(View.GONE);
                this.lytCheckDigitRequired.setVisibility(View.GONE);
                this.lytPosicodeLimited.setVisibility(View.GONE);
                this.lytEmulation.setVisibility(View.GONE);
                this.lytConcatenation.setVisibility(View.GONE);
                this.chkAppend.setVisibility(View.GONE);
                this.chkPharmaceutical.setVisibility(View.GONE);
                this.chkFullAscii.setVisibility(View.GONE);
                this.chkISBTConcatenation.setVisibility(View.GONE);
                this.chkAztecRunes.setVisibility(View.VISIBLE);
                this.lytCodePage.setVisibility(View.GONE);
                break;
            case I2of5:
                this.length = new SymbolLength(this, HoneywellParamName.I2of5LengthMin,
                        HoneywellParamName.I2of5LengthMax, this.mParam);

                paramList = this.mParam.getParams(new HoneywellParamName[] {
                        HoneywellParamName.I2of5CheckDigit, HoneywellParamName.I2of5LengthMin,
                        HoneywellParamName.I2of5LengthMax });

                this.spnCheckChar.setValue(paramList
                        .getValueAt(HoneywellParamName.I2of5CheckDigit));
                this.length.setValue(
                        paramList.getValueAt(HoneywellParamName.I2of5LengthMin),
                        paramList.getValueAt(HoneywellParamName.I2of5LengthMax));

                this.chkStartStopCharTransmit.setVisibility(View.GONE);
                this.lytCheckChar.setVisibility(View.VISIBLE);
                this.lytTelepenOutput.setVisibility(View.GONE);
                this.lytCheckDigitRequired.setVisibility(View.GONE);
                this.lytPosicodeLimited.setVisibility(View.GONE);
                this.lytEmulation.setVisibility(View.GONE);
                this.lytConcatenation.setVisibility(View.GONE);
                this.chkAppend.setVisibility(View.GONE);
                this.chkPharmaceutical.setVisibility(View.GONE);
                this.chkFullAscii.setVisibility(View.GONE);
                this.chkISBTConcatenation.setVisibility(View.GONE);
                this.chkAztecRunes.setVisibility(View.GONE);
                this.lytCodePage.setVisibility(View.GONE);
                break;
            case Telepen:
                this.length = new SymbolLength(this, HoneywellParamName.TelepenLengthMin,
                        HoneywellParamName.TelepenLengthMax, this.mParam);

                paramList = this.mParam.getParams(new HoneywellParamName[] {
                        HoneywellParamName.TelepenOutput, HoneywellParamName.TelepenLengthMin,
                        HoneywellParamName.TelepenLengthMax, });

                this.spnTelepenOutput.setValue(paramList
                        .getValueAt(HoneywellParamName.TelepenOutput));
                this.length.setValue(
                        paramList.getValueAt(HoneywellParamName.TelepenLengthMin),
                        paramList.getValueAt(HoneywellParamName.TelepenLengthMax));

                this.chkStartStopCharTransmit.setVisibility(View.GONE);
                this.lytCheckChar.setVisibility(View.GONE);
                this.lytTelepenOutput.setVisibility(View.VISIBLE);
                this.lytCheckDigitRequired.setVisibility(View.GONE);
                this.lytPosicodeLimited.setVisibility(View.GONE);
                this.lytEmulation.setVisibility(View.GONE);
                this.lytConcatenation.setVisibility(View.GONE);
                this.chkAppend.setVisibility(View.GONE);
                this.chkPharmaceutical.setVisibility(View.GONE);
                this.chkFullAscii.setVisibility(View.GONE);
                this.chkISBTConcatenation.setVisibility(View.GONE);
                this.chkAztecRunes.setVisibility(View.GONE);
                this.lytCodePage.setVisibility(View.GONE);
                break;
            case MSI:
                this.length = new SymbolLength(this, HoneywellParamName.MSILengthMin,
                        HoneywellParamName.MSILengthMax, this.mParam);

                paramList = this.mParam.getParams(new HoneywellParamName[] {
                        HoneywellParamName.MSICheckChar, HoneywellParamName.MSILengthMin,
                        HoneywellParamName.MSILengthMax });

                Object value = paramList.getValueAt(HoneywellParamName.MSICheckChar);
                if(value.getClass() == com.wsmr.lib.dev.barcode.honeywell.type.MSICheckCharType.class) {
                    MSICheckCharType chkCharType = (MSICheckCharType)value;
                    boolean checked = false;
                    if(chkCharType == MSICheckCharType.Validate2Type10CharsAndTransmit || chkCharType == MSICheckCharType.ValidateType10AndTransmit ||
                            chkCharType == MSICheckCharType.ValidateType11ThenType10CharAndTransmit)
                        checked = true;

                    this.chkStartStopCharTransmit.setChecked(checked);
                } else {
                    this.chkStartStopCharTransmit.setChecked((Boolean) value);
                }





                this.length.setValue(
                        paramList.getValueAt(HoneywellParamName.MSILengthMin),
                        paramList.getValueAt(HoneywellParamName.MSILengthMax));

                this.chkStartStopCharTransmit.setVisibility(View.VISIBLE);
                this.lytCheckChar.setVisibility(View.GONE);
                this.lytTelepenOutput.setVisibility(View.GONE);
                this.lytCheckDigitRequired.setVisibility(View.GONE);
                this.lytPosicodeLimited.setVisibility(View.GONE);
                this.lytEmulation.setVisibility(View.GONE);
                this.lytConcatenation.setVisibility(View.GONE);
                this.chkAppend.setVisibility(View.GONE);
                this.chkPharmaceutical.setVisibility(View.GONE);
                this.chkFullAscii.setVisibility(View.GONE);
                this.chkISBTConcatenation.setVisibility(View.GONE);
                this.chkAztecRunes.setVisibility(View.GONE);
                this.lytCodePage.setVisibility(View.GONE);
                break;
            case Code11:
                this.length = new SymbolLength(this, HoneywellParamName.Code11LengthMin,
                        HoneywellParamName.Code11LengthMax, this.mParam);

                paramList = this.mParam.getParams(new HoneywellParamName[] {
                        HoneywellParamName.Code11CheckDigit, HoneywellParamName.Code11LengthMin,
                        HoneywellParamName.Code11LengthMax });

                this.spnCheckDigitRequired.setValue(paramList
                        .getValueAt(HoneywellParamName.Code11CheckDigit));
                this.length.setValue(
                        paramList.getValueAt(HoneywellParamName.Code11LengthMin),
                        paramList.getValueAt(HoneywellParamName.Code11LengthMax));

                this.chkStartStopCharTransmit.setVisibility(View.GONE);
                this.lytCheckChar.setVisibility(View.GONE);
                this.lytTelepenOutput.setVisibility(View.GONE);
                this.lytCheckDigitRequired.setVisibility(View.VISIBLE);
                this.lytPosicodeLimited.setVisibility(View.GONE);
                this.lytEmulation.setVisibility(View.GONE);
                this.lytConcatenation.setVisibility(View.GONE);
                this.chkAppend.setVisibility(View.GONE);
                this.chkPharmaceutical.setVisibility(View.GONE);
                this.chkFullAscii.setVisibility(View.GONE);
                this.chkISBTConcatenation.setVisibility(View.GONE);
                this.chkAztecRunes.setVisibility(View.GONE);
                this.lytCodePage.setVisibility(View.GONE);
                break;
            case PosiCode:
                this.length = new SymbolLength(this, HoneywellParamName.PosiCodeLengthMin,
                        HoneywellParamName.PosiCodeLengthMax, this.mParam);

                paramList = this.mParam.getParams(new HoneywellParamName[] {
                        HoneywellParamName.PosiCodeLimit, HoneywellParamName.PosiCodeLengthMin,
                        HoneywellParamName.PosiCodeLengthMax });

                this.spnPosicodeLimited.setValue(paramList
                        .getValueAt(HoneywellParamName.PosiCodeLimit));
                this.length.setValue(
                        paramList.getValueAt(HoneywellParamName.PosiCodeLengthMin),
                        paramList.getValueAt(HoneywellParamName.PosiCodeLengthMax));

                this.chkStartStopCharTransmit.setVisibility(View.GONE);
                this.lytCheckChar.setVisibility(View.GONE);
                this.lytTelepenOutput.setVisibility(View.GONE);
                this.lytCheckDigitRequired.setVisibility(View.GONE);
                this.lytPosicodeLimited.setVisibility(View.VISIBLE);
                this.lytEmulation.setVisibility(View.GONE);
                this.lytConcatenation.setVisibility(View.GONE);
                this.chkAppend.setVisibility(View.GONE);
                this.chkPharmaceutical.setVisibility(View.GONE);
                this.chkFullAscii.setVisibility(View.GONE);
                this.chkISBTConcatenation.setVisibility(View.GONE);
                this.chkAztecRunes.setVisibility(View.GONE);
                this.lytCodePage.setVisibility(View.GONE);
                break;
            case ComCode:
                this.length = new SymbolLength(this, HoneywellParamName.ComCodeLengthMin,
                        HoneywellParamName.ComCodeLengthMax, this.mParam);

                paramList = this.mParam.getParams(new HoneywellParamName[] {
                        HoneywellParamName.ComCodeEmulation, HoneywellParamName.ComCodeLengthMin,
                        HoneywellParamName.ComCodeLengthMax });

                this.spnEmulation.setValue(paramList
                        .getValueAt(HoneywellParamName.ComCodeEmulation));
                this.length.setValue(
                        paramList.getValueAt(HoneywellParamName.ComCodeLengthMin),
                        paramList.getValueAt(HoneywellParamName.ComCodeLengthMax));

                this.chkStartStopCharTransmit.setVisibility(View.GONE);
                this.lytCheckChar.setVisibility(View.GONE);
                this.lytTelepenOutput.setVisibility(View.GONE);
                this.lytCheckDigitRequired.setVisibility(View.GONE);
                this.lytPosicodeLimited.setVisibility(View.GONE);
                this.lytEmulation.setVisibility(View.VISIBLE);
                this.lytConcatenation.setVisibility(View.GONE);
                this.chkAppend.setVisibility(View.GONE);
                this.chkPharmaceutical.setVisibility(View.GONE);
                this.chkFullAscii.setVisibility(View.GONE);
                this.chkISBTConcatenation.setVisibility(View.GONE);
                this.chkAztecRunes.setVisibility(View.GONE);
                this.lytCodePage.setVisibility(View.GONE);
                break;
            default:
                return;
        }
    }
}
