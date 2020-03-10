package com.wsmr.app.barcode.adapter.IT5x80;

import java.util.ArrayList;

import com.wsmr.lib.dev.barcode.honeywell.param.HoneywellParamName;
import com.wsmr.lib.dev.barcode.honeywell.param.HoneywellParamValueList;
import com.wsmr.lib.dev.barcode.honeywell.type.OcrType;
import com.wsmr.app.barcode.R;
import com.wsmr.app.barcode.type.IT5x80.Scan2dSymbolOption;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

public class SymbolOptionListAdapter extends BaseAdapter {

    @SuppressWarnings("unused")
    private static final String TAG = "SymbolOptionListAdapter";

    private Context context;
    private LayoutInflater inflater;
    private ArrayList<SymbolOptionListItem> list;

    // ------------------------------------------------------------------------
    // Constructor SymbolOptionListAdapter
    // ------------------------------------------------------------------------

    public SymbolOptionListAdapter(Context context) {
        super();

        this.context = context;
        this.inflater = (LayoutInflater) this.context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.list = new ArrayList<SymbolOptionListItem>();
    }

    public void updateList(HoneywellParamValueList param) {
        this.list.clear();

        // 2019.01.21
        Object obj = null;

        // is Aztec Code Option
        obj = param.getValueAt(HoneywellParamName.AztecCode);
        if(obj != null) {
            if ((Boolean)obj) {
                this.list.add(new SymbolOptionListItem(Scan2dSymbolOption.AztecCode));
            }
        }
        // is China Post Option
        obj = param.getValueAt(HoneywellParamName.ChinaPost);
        if(obj != null) {
            if ((Boolean)obj) {
                this.list.add(new SymbolOptionListItem(Scan2dSymbolOption.ChinaPost));
            }
        }
        // is Codabar Option
        obj = param.getValueAt(HoneywellParamName.Codabar);
        if(obj != null) {
            if ((Boolean)obj) {
                this.list.add(new SymbolOptionListItem(Scan2dSymbolOption.Codabar));
            }
        }
        // is Codablock F Option
        obj = param.getValueAt(HoneywellParamName.CodablockF);
        if(obj != null) {
            if ((Boolean)obj) {
                this.list.add(new SymbolOptionListItem(Scan2dSymbolOption.CodablockF));
            }
        }
        // is Code 11 Option
        obj = param.getValueAt(HoneywellParamName.Code11);
        if(obj != null) {
            if ((Boolean)obj) {
                this.list.add(new SymbolOptionListItem(Scan2dSymbolOption.Code11));
            }
        }
        // is Code 128 Option
        obj = param.getValueAt(HoneywellParamName.Code128);
        if(obj != null) {
            if ((Boolean)obj) {
                this.list.add(new SymbolOptionListItem(Scan2dSymbolOption.Code128));
            }
        }
        // is Code 16K Option
        obj = param.getValueAt(HoneywellParamName.Code16K);
        if(obj != null) {
            if ((Boolean)obj) {
                this.list.add(new SymbolOptionListItem(Scan2dSymbolOption.Code16K));
            }
        }
        // is Code 39 Option
        obj = param.getValueAt(HoneywellParamName.Code39);
        if(obj != null) {
            if ((Boolean)obj) {
                this.list.add(new SymbolOptionListItem(Scan2dSymbolOption.Code39));
            }
        }
        // is Code 49 Option
        obj = param.getValueAt(HoneywellParamName.Code49);
        if(obj != null) {
            if ((Boolean)obj) {
                this.list.add(new SymbolOptionListItem(Scan2dSymbolOption.Code49));
            }
        }
        // is Code 93 Option
        obj = param.getValueAt(HoneywellParamName.Code93);
        if(obj != null) {
            if ((Boolean)obj) {
                this.list.add(new SymbolOptionListItem(Scan2dSymbolOption.Code93));
            }
        }
        // is Data Matrix Option
        obj = param.getValueAt(HoneywellParamName.Matrix);
        if(obj != null) {
            if ((Boolean)obj) {
                this.list.add(new SymbolOptionListItem(Scan2dSymbolOption.Matrix));
            }
        }
        // is EAN/JAP-13 Option
        obj = param.getValueAt(HoneywellParamName.EAN13);
        if(obj != null) {
            if ((Boolean)obj) {
                this.list.add(new SymbolOptionListItem(Scan2dSymbolOption.EAN13));
            }
        }
        // is EAN/JAP-8 Option
        obj = param.getValueAt(HoneywellParamName.EAN8);
        if(obj != null) {
            if ((Boolean)obj) {
                this.list.add(new SymbolOptionListItem(Scan2dSymbolOption.EAN8));
            }
        }
        // is EAN/UCC Composite Code Option
        obj = param.getValueAt(HoneywellParamName.ComCode);
        if(obj != null) {
            if ((Boolean)obj) {
                this.list.add(new SymbolOptionListItem(Scan2dSymbolOption.ComCode));
            }
        }
        // is Interleaved 2 of 5 Option
        obj = param.getValueAt(HoneywellParamName.I2of5);
        if(obj != null) {
            if ((Boolean)obj) {
                this.list.add(new SymbolOptionListItem(Scan2dSymbolOption.I2of5));
            }
        }
        // is Korea Post Option
        obj = param.getValueAt(HoneywellParamName.KoreaPost);
        if(obj != null) {
            if ((Boolean)obj) {
                this.list.add(new SymbolOptionListItem(Scan2dSymbolOption.KoreaPost));
            }
        }
        // is Matrix 2 of 5 Option
        obj = param.getValueAt(HoneywellParamName.X2of5);
        if(obj != null) {
            if ((Boolean)obj) {
                this.list.add(new SymbolOptionListItem(Scan2dSymbolOption.X2of5));
            }
        }
        // is MaxiCode Option
        obj = param.getValueAt(HoneywellParamName.MaxiCode);
        if(obj != null) {
            if ((Boolean)obj) {
                this.list.add(new SymbolOptionListItem(Scan2dSymbolOption.MaxiCode));
            }
        }
        // is MicroPDF 417 Option
        obj = param.getValueAt(HoneywellParamName.MicroPDF);
        if(obj != null) {
            if ((Boolean)obj) {
                this.list.add(new SymbolOptionListItem(Scan2dSymbolOption.MicroPDF));
            }
        }
        // is MSI Option
        obj = param.getValueAt(HoneywellParamName.MSI);
        if(obj != null) {
            if ((Boolean)obj) {
                this.list.add(new SymbolOptionListItem(Scan2dSymbolOption.MSI));
            }
        }
        // is OCR Option
        obj = param.getValueAt(HoneywellParamName.OCR);
        if(obj != null) {
            if ((OcrType)obj != OcrType.OffAll) {
                this.list.add(new SymbolOptionListItem(Scan2dSymbolOption.OCR));
            }
        }
        // is PDF 417 Option
        obj = param.getValueAt(HoneywellParamName.PDF417);
        if(obj != null) {
            if ((Boolean)obj) {
                this.list.add(new SymbolOptionListItem(Scan2dSymbolOption.PDF417));
            }
        }
        // is Planet Option
        obj = param.getValueAt(HoneywellParamName.Planet);
        if(obj != null) {
            if ((Boolean)obj) {
                this.list.add(new SymbolOptionListItem(Scan2dSymbolOption.Planet));
            }
        }
        // is Plessey Code Option
        obj = param.getValueAt(HoneywellParamName.PlesseyCode);
        if(obj != null) {
            if ((Boolean)obj) {
                this.list.add(new SymbolOptionListItem(Scan2dSymbolOption.PlesseyCode));
            }
        }
        // is PosiCode Option
        obj = param.getValueAt(HoneywellParamName.PosiCode);
        if(obj != null) {
            if ((Boolean)obj) {
                this.list.add(new SymbolOptionListItem(Scan2dSymbolOption.PosiCode));
            }
        }
        // is Postnet Option
        obj = param.getValueAt(HoneywellParamName.Postnet);
        if(obj != null) {
            if ((Boolean)obj) {
                this.list.add(new SymbolOptionListItem(Scan2dSymbolOption.Postnet));
            }
        }
        // is QR Code Option
        obj = param.getValueAt(HoneywellParamName.QRCode);
        if(obj != null) {
            if ((Boolean)obj) {
                this.list.add(new SymbolOptionListItem(Scan2dSymbolOption.QRCode));
            }
        }
        // is RSS Expanded Option
        obj = param.getValueAt(HoneywellParamName.RSSExp);
        if(obj != null) {
            if ((Boolean)obj) {
                this.list.add(new SymbolOptionListItem(Scan2dSymbolOption.RSSExp));
            }
        }
        // is Straight 2 of 5 IATA Option
        obj = param.getValueAt(HoneywellParamName.A2of5);
        if(obj != null) {
            if ((Boolean)obj) {
                this.list.add(new SymbolOptionListItem(Scan2dSymbolOption.A2of5));
            }
        }
        // is Straight 2 of 5 Industrial Option
        obj = param.getValueAt(HoneywellParamName.R2of5);
        if(obj != null) {
            if ((Boolean)obj) {
                this.list.add(new SymbolOptionListItem(Scan2dSymbolOption.R2of5));
            }
        }
        // is Telepen Option
        obj = param.getValueAt(HoneywellParamName.Telepen);
        if(obj != null) {
            if ((Boolean)obj) {
                this.list.add(new SymbolOptionListItem(Scan2dSymbolOption.Telepen));
            }
        }
        // is UPC-A Option
        obj = param.getValueAt(HoneywellParamName.UPCA);
        if(obj != null) {
            if ((Boolean)obj) {
                this.list.add(new SymbolOptionListItem(Scan2dSymbolOption.UPCA));
            }
        }
        // is UPC-E0 Option
        obj = param.getValueAt(HoneywellParamName.UPCE0);
        if(obj != null) {
            if ((Boolean)obj) {
                this.list.add(new SymbolOptionListItem(Scan2dSymbolOption.UPCE0));
            }
        }
        this.notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return this.list.size();
    }

    @Override
    public Scan2dSymbolOption getItem(int position) {
        return this.list.get(position).getOption();
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        SymbolOptionListItem item = this.list.get(position);

        if (null == convertView) {
            convertView = inflater.inflate(R.layout.item_symbol_option_list,
                    parent, false);
            item.setView(convertView);
        }
        if (null == item.getView()) {
            item.setView(convertView);
        }
        item.displayItem();

        return convertView;
    }

    // ------------------------------------------------------------------------
    // Internal class SymbolOptionListItem
    // ------------------------------------------------------------------------

    private class SymbolOptionListItem {
        private Scan2dSymbolOption option;

        private View view;
        private TextView symbol;
        private Button button;

        public SymbolOptionListItem(Scan2dSymbolOption option) {
            this.option = option;
        }

        public Scan2dSymbolOption getOption() {
            return this.option;
        }

        public View getView() {
            return this.view;
        }

        public void setView(View view) {
            this.view = view;
            this.symbol = (TextView) this.view.findViewById(R.id.symbol);
            this.button = (Button) this.view.findViewById(R.id.detail_option);
            this.button.setOnClickListener((Button.OnClickListener)context);
        }

        public void displayItem() {
            this.symbol.setText(option.toString());
            this.button.setTag(this.option);
        }
    }
}
