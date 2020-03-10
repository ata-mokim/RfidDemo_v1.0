package com.wsmr.app.barcode.option.MDI3x00;

import com.wsmr.lib.dev.ATScanManager;
import com.wsmr.lib.dev.ATScanner;
import com.wsmr.lib.dev.barcode.opticon.param.OpticonParamName;
import com.wsmr.lib.dev.barcode.opticon.param.OpticonParamValue;
import com.wsmr.lib.dev.barcode.opticon.param.OpticonParamValueList;
import com.wsmr.lib.dev.barcode.params.ATScanMDI3x00Parameter;
import com.wsmr.app.barcode.R;
import com.wsmr.app.barcode.adapter.MDI3x00.SymbolStateListAdapter;

import android.app.Activity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

public class OptionEnableStateActivity extends Activity implements
        OnClickListener  {

    @SuppressWarnings("unused")
    private static final String TAG = "OptionEnableStateActivity";

    private ATScanner mScanner;
    private ATScanMDI3x00Parameter mParam;

    private ListView lstSymbols;
    private Button btnSetOption;

    private SymbolStateListAdapter adpSymbols;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_option_enable_state);
        getActionBar().setDisplayHomeAsUpEnabled(true);

        // Initialize Scanner Instance
        this.mScanner = ATScanManager.getInstance(getApplicationContext());
        this.mParam = (ATScanMDI3x00Parameter)this.mScanner.getParameter();

        // Initialize Widgets
        this.lstSymbols = (ListView) findViewById(R.id.symbols);
        this.adpSymbols = new SymbolStateListAdapter(this);
        this.lstSymbols.setAdapter(this.adpSymbols);

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
    public boolean onOptionsItemSelected(MenuItem item) {
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View v) {
        if (R.id.set_option == v.getId()) {
            OpticonParamValueList param = this.adpSymbols.getList();
            OpticonParamValue paramValue;
            boolean enabled = false;
            OpticonParamValueList opvl = new OpticonParamValueList();

            opvl.add(new OpticonParamValue(OpticonParamName.AllCodes_Disable));

            for(int i=0; i< param.getCount(); i++){
                paramValue = param.get(i);
                enabled = (Boolean)paramValue.getValue();
                switch(paramValue.getName()){
                    case UPCA:
                        opvl.add(new OpticonParamValue(enabled ? OpticonParamName.UPC_Multiple : OpticonParamName.UPC_Disable));
                        break;
                    case UPCE:
                        opvl.add(new OpticonParamValue(enabled ? OpticonParamName.UPC_Multiple : OpticonParamName.UPC_Disable));
                        break;
                    case EAN13:
                        opvl.add(new OpticonParamValue(enabled ? OpticonParamName.EAN13_Multiple : OpticonParamName.EAN13_Disable));
                        break;
                    case EAN8:
                        opvl.add(new OpticonParamValue(enabled ? OpticonParamName.EAN8_Multiple : OpticonParamName.EAN8_Disable));
                        break;
                    case Code39:
                        opvl.add(new OpticonParamValue(enabled ? OpticonParamName.Code39_Multiple : OpticonParamName.Code39_Disable));
                        break;
                    case Codabar:
                        opvl.add(new OpticonParamValue(enabled ? OpticonParamName.Codabar_Multiple : OpticonParamName.Codabar_Disable));
                        break;
                    case Industrial2of5:
                        opvl.add(new OpticonParamValue(enabled ? OpticonParamName.Industrial2of5_Multiple : OpticonParamName.Industrial2of5_Disable));
                        break;
                    case Interleaved2of5:
                        opvl.add(new OpticonParamValue(enabled ? OpticonParamName.Interleaved2of5_Multiple : OpticonParamName.Interleaved2of5_Disable));
                        break;
                    case SCode:
                        opvl.add(new OpticonParamValue(enabled ? OpticonParamName.SCode_Multiple : OpticonParamName.SCode_Disable));
                        break;
                    case Matrix2of5:
                        opvl.add(new OpticonParamValue(enabled ? OpticonParamName.Matrix2of5_Multiple : OpticonParamName.Matrix2of5_Disable));
                        break;
                    case ChinesePost:
                        opvl.add(new OpticonParamValue(enabled ? OpticonParamName.ChinesePostMatrix2of5_Multiple : OpticonParamName.ChinesePostMatrix2of5_Disable));
                        break;
                    case IATA:
                        opvl.add(new OpticonParamValue(enabled ? OpticonParamName.IATA_Multiple : OpticonParamName.IATA_Disable));
                        break;
                    case MSI_Plessey:
                        opvl.add(new OpticonParamValue(enabled ? OpticonParamName.MSI_Plessey_Multiple : OpticonParamName.MSI_Plessey_Disable));
                        break;
                    case Telepen:
                        opvl.add(new OpticonParamValue(enabled ? OpticonParamName.Telepen_Multiple : OpticonParamName.Telepen_Disable));
                        break;
                    case UK_Plessey:
                        opvl.add(new OpticonParamValue(enabled ? OpticonParamName.UK_Plessey_Multiple : OpticonParamName.UK_Plessey_Disable));
                        break;
                    case Code128:
                        opvl.add(new OpticonParamValue(enabled ? OpticonParamName.Code128_Multiple : OpticonParamName.Code128_Disable));
                        break;
                    case Code93:
                        opvl.add(new OpticonParamValue(enabled ? OpticonParamName.Code93_Multiple : OpticonParamName.Code93_Disable));
                        break;
                    case Code11:
                        opvl.add(new OpticonParamValue(enabled ? OpticonParamName.Code11_Multiple : OpticonParamName.Code11_Disable));
                        break;
                    case KoreanPostalAuthority:
                        opvl.add(new OpticonParamValue(enabled ? OpticonParamName.KoreanPostalAuthority_Multiple : OpticonParamName.KoreanPostalAuthority_Disable));
                        break;
                    case IntelligentMailBarCode:
                        opvl.add(new OpticonParamValue(enabled ? OpticonParamName.IntelligentMailBarcode_Multiple : OpticonParamName.IntelligentMailBarcode_Disable));
                        break;
                    case POSTNET:
                        opvl.add(new OpticonParamValue(enabled ? OpticonParamName.POSTNET_Multiple : OpticonParamName.POSTNET_Disable));
                        break;
                    case CodablockF:
                        opvl.add(new OpticonParamValue(enabled ? OpticonParamName.CodablockF_Multiple : OpticonParamName.CodablockF_Disable));
                        break;
                    case DataMatrix:
                        opvl.add(new OpticonParamValue(enabled ? OpticonParamName.DataMatrix_ECC200_Multiple : OpticonParamName.DataMatrix_ECC200_Disable));
                        break;
                    case Aztec:
                        opvl.add(new OpticonParamValue(enabled ? OpticonParamName.AztecCode_Multiple : OpticonParamName.AztecCode_Disable));
                        break;
                    case ChineseSensibleCode:
                        opvl.add(new OpticonParamValue(enabled ? OpticonParamName.ChineseSensibleCode_Multiple : OpticonParamName.ChineseSensibleCode_Disable));
                        break;
                    case QRCode:
                        opvl.add(new OpticonParamValue(enabled ? OpticonParamName.QRCode_Multiple : OpticonParamName.QRCode_Disable));
                        break;
                    case MicroQRCode:
                        opvl.add(new OpticonParamValue(enabled ? OpticonParamName.MicroQR_Multiple : OpticonParamName.MicroQR_Disable));
                        break;
                    case MaxiCode:
                        opvl.add(new OpticonParamValue(enabled ? OpticonParamName.MaxiCode_Multiple : OpticonParamName.MaxiCode_Disable));
                        break;
                    case PDF417:
                        opvl.add(new OpticonParamValue(enabled ? OpticonParamName.PDF417_Multiple : OpticonParamName.PDF417_Disable));
                        break;
                    case MicroPDF417:
                        opvl.add(new OpticonParamValue(enabled ? OpticonParamName.MicroPDF417_Multiple : OpticonParamName.MicroPDF417_Disable));
                        break;/*
				case GS1_Databar:
					opvl.add(new OpticonParamValue(enabled ? OpticonParamName.GS1DataBar_Multiple : OpticonParamName.GS1DataBar_Disable));
					break;*/
                    default:
                        break;
                }
            }

            opvl.add(new OpticonParamValue(OpticonParamName.SaveSettingsInStartUpSettingArea));

            // Set Scanner Parameter
            if (this.mParam.setParams(opvl)) {
                this.setResult(RESULT_OK);
                this.finish();
            } else {
                Toast.makeText(this, R.string.faile_to_set_symbologies,
                        Toast.LENGTH_LONG).show();
            }
        }
    }

    // Initialize Symbol State List
    private void initSymbolStateList() {
        // Get Scanner Parameter
        OpticonParamValueList paramList = this.mParam.getParams(new OpticonParamName[] {
                OpticonParamName.UPCA, OpticonParamName.UPCE, OpticonParamName.EAN13, OpticonParamName.EAN8,
                OpticonParamName.Code39, OpticonParamName.Codabar, OpticonParamName.Industrial2of5,
                OpticonParamName.Interleaved2of5, OpticonParamName.SCode, OpticonParamName.Code128,
                OpticonParamName.Code93, OpticonParamName.IATA, OpticonParamName.MSI_Plessey, OpticonParamName.UK_Plessey,
                OpticonParamName.Telepen, OpticonParamName.Code11, OpticonParamName.Matrix2of5, OpticonParamName.ChinesePost,
                OpticonParamName.KoreanPostalAuthority, OpticonParamName.IntelligentMailBarCode, OpticonParamName.POSTNET,
                OpticonParamName.JapanesePost, /*OpticonParamName.GS1_Databar,*/ OpticonParamName.PDF417,
                OpticonParamName.MicroPDF417, OpticonParamName.CodablockF, OpticonParamName.QRCode,
                OpticonParamName.MicroQRCode, OpticonParamName.DataMatrix, OpticonParamName.Aztec,
                OpticonParamName.ChineseSensibleCode, OpticonParamName.MaxiCode
        });

        this.adpSymbols.initList(paramList);
    }
}
