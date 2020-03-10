package com.wsmr.app.barcode.option.IT5x80;

import com.wsmr.lib.dev.barcode.honeywell.param.HoneywellParamName;
import com.wsmr.app.barcode.R;
import com.wsmr.app.barcode.type.IT5x80.SymbolLength;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Toast;

public class OptionSymbolLength extends OptionSymbol implements OnClickListener {

    private SymbolLength length;
    private Button btnSetOption;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_option_2d_symbol_length);
        getActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    protected void onStart() {
        // TODO Auto-generated method stub
        super.onStart();

        // Initialize Widgets
        this.length = allocLength();
        this.btnSetOption = (Button) findViewById(R.id.set_option);
        this.btnSetOption.setOnClickListener(this);

        // Load Scanner Symbol Detail Option
        loadOption();
    }

    @Override
    protected void onStop() {

        super.onStop();
    }

    @SuppressLint("ShowToast")
    @Override
    public void onClick(View v) {
        if (R.id.set_option == v.getId()) {
            if(this.length.save(this.mParam)){
                this.setResult(RESULT_OK);
                this.finish();
            }else{
                Toast.makeText(this, R.string.faile_to_set_symbologies,
                        Toast.LENGTH_LONG).show();
            }
        }
    }

    // Load Scanner Symbol Detail Option
    private void loadOption() {
        this.length.load(this.mParam);
    }

    private SymbolLength allocLength() {
        switch (this.mSymbolType) {
            case R2of5:
                return new SymbolLength(this, HoneywellParamName.R2of5LengthMin,
                        HoneywellParamName.R2of5LengthMax);
            case A2of5:
                return new SymbolLength(this, HoneywellParamName.A2of5LengthMin,
                        HoneywellParamName.A2of5LengthMax);
            case CodablockF:
                return new SymbolLength(this, HoneywellParamName.CodablockFLengthMin,
                        HoneywellParamName.CodablockFLengthMax);
            case PlesseyCode:
                return new SymbolLength(this, HoneywellParamName.PlesseyCodeLengthMin,
                        HoneywellParamName.PlesseyCodeLengthMax);
            case X2of5:
                return new SymbolLength(this, HoneywellParamName.X2of5LengthMin,
                        HoneywellParamName.X2of5LengthMax);
            case RSSExp:
                return new SymbolLength(this, HoneywellParamName.RSSExpLengthMin,
                        HoneywellParamName.RSSExpLengthMax);
            case Code16K:
                return new SymbolLength(this, HoneywellParamName.Code16KLengthMin,
                        HoneywellParamName.Code16KLengthMax);
            case Code49:
                return new SymbolLength(this, HoneywellParamName.Code49LengthMin,
                        HoneywellParamName.Code49LengthMax);
            case PDF417:
                return new SymbolLength(this, HoneywellParamName.PDF417LengthMin,
                        HoneywellParamName.PDF417LengthMax);
            case MicroPDF:
                return new SymbolLength(this, HoneywellParamName.MicroPDFLengthMin,
                        HoneywellParamName.MicroPDFLengthMax);
            case ChinaPost:
                return new SymbolLength(this, HoneywellParamName.ChinaPostLengthMin,
                        HoneywellParamName.ChinaPostLengthMax);
            case KoreaPost:
                return new SymbolLength(this, HoneywellParamName.KoreaPostLengthMin,
                        HoneywellParamName.KoreaPostLengthMax);
            case QRCode:
                return new SymbolLength(this, HoneywellParamName.QRCodeLengthMin,
                        HoneywellParamName.QRCodeLengthMax);
            case Matrix:
                return new SymbolLength(this, HoneywellParamName.MatrixLengthMin,
                        HoneywellParamName.MatrixLengthMax);
            case MaxiCode:
                return new SymbolLength(this, HoneywellParamName.MaxiCodeLengthMin,
                        HoneywellParamName.MaxiCodeLengthMax);
            default:
                return null;
        }
    }
}
