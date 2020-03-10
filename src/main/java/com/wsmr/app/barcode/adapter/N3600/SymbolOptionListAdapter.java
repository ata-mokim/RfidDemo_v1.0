package com.wsmr.app.barcode.adapter.N3600;

import java.util.ArrayList;

import com.wsmr.app.barcode.R;
import com.wsmr.app.barcode.type.N3600.Scan2dSymbolOption;
import com.wsmr.lib.dev.barcode.honeywell.param.HoneywellParamName;
import com.wsmr.lib.dev.barcode.honeywell.param.HoneywellParamValueList;
import com.wsmr.lib.dev.barcode.honeywell.type.PostalCodes2D;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

public class SymbolOptionListAdapter extends BaseAdapter  {

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

        // is Codabar Option
        if ((Boolean)param.getValueAt(HoneywellParamName.Codabar)) {
            this.list.add(new SymbolOptionListItem(Scan2dSymbolOption.Codabar));
        }
        // is Code 39 Option
        if ((Boolean)param.getValueAt(HoneywellParamName.Code39)) {
            this.list.add(new SymbolOptionListItem(Scan2dSymbolOption.Code39));
        }
        // is Interleaved 2 of 5 Option
        if ((Boolean)param.getValueAt(HoneywellParamName.I2of5)) {
            this.list.add(new SymbolOptionListItem(Scan2dSymbolOption.I2of5));
        }
        // is Code 93 Option
        if ((Boolean)param.getValueAt(HoneywellParamName.Code93)) {
            this.list.add(new SymbolOptionListItem(Scan2dSymbolOption.Code93));
        }
        // is Straight 2 of 5 IATA Option
        if ((Boolean)param.getValueAt(HoneywellParamName.A2of5)) {
            this.list.add(new SymbolOptionListItem(Scan2dSymbolOption.A2of5));
        }
        // is Code 11 Option
        if ((Boolean)param.getValueAt(HoneywellParamName.Code11)) {
            this.list.add(new SymbolOptionListItem(Scan2dSymbolOption.Code11));
        }
        // is Code 128 Option
        if ((Boolean)param.getValueAt(HoneywellParamName.Code128)) {
            this.list.add(new SymbolOptionListItem(Scan2dSymbolOption.Code128));
        }
        // is UPC-A Option
        if ((Boolean)param.getValueAt(HoneywellParamName.UPCA)) {
            this.list.add(new SymbolOptionListItem(Scan2dSymbolOption.UPCA));
        }
        // is UPC-E0 Option
        if ((Boolean)param.getValueAt(HoneywellParamName.UPCE0)) {
            this.list.add(new SymbolOptionListItem(Scan2dSymbolOption.UPCE0));
        }
        // is EAN/JAP-13 Option
        if ((Boolean)param.getValueAt(HoneywellParamName.EAN13)) {
            this.list.add(new SymbolOptionListItem(Scan2dSymbolOption.EAN13));
        }
        // is EAN/JAP-8 Option
        if ((Boolean)param.getValueAt(HoneywellParamName.EAN8)) {
            this.list.add(new SymbolOptionListItem(Scan2dSymbolOption.EAN8));
        }
        // is MSI Option
        if ((Boolean)param.getValueAt(HoneywellParamName.MSI)) {
            this.list.add(new SymbolOptionListItem(Scan2dSymbolOption.MSI));
        }
        // is Aztec Code Option
        if ((Boolean)param.getValueAt(HoneywellParamName.AztecCode)) {
            this.list.add(new SymbolOptionListItem(Scan2dSymbolOption.AztecCode));
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
