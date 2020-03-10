package com.wsmr.app.barcode.adapter.N3600;

import com.wsmr.app.barcode.R;
import com.wsmr.lib.dev.barcode.honeywell.param.HoneywellParamName;
import com.wsmr.lib.dev.barcode.honeywell.param.HoneywellParamValue;
import com.wsmr.lib.dev.barcode.honeywell.param.HoneywellParamValueList;
import com.wsmr.lib.dev.barcode.honeywell.type.PostalCodes2D;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;
import android.widget.CompoundButton.OnCheckedChangeListener;

public class SymbolStateListAdapter extends BaseAdapter  {

    private LayoutInflater inflater;
    private HoneywellParamValueList list;

    public SymbolStateListAdapter(Context context) {
        super();

        this.inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.list = new HoneywellParamValueList();
    }

    public void initList(HoneywellParamValueList param) {
        HoneywellParamValue value;

        for (int i = 0; i < param.getCount(); i++) {
            value = param.get(i);
            HoneywellParamName name = value.getName();
            if (HoneywellParamName.Codabar == name
                    || HoneywellParamName.Code39 == name
                    || HoneywellParamName.I2of5 == name
                    || HoneywellParamName.Code93 == name
                    || HoneywellParamName.R2of5 == name
                    || HoneywellParamName.A2of5 == name
                    || HoneywellParamName.X2of5 == name
                    || HoneywellParamName.Code11 == name
                    || HoneywellParamName.Code128 == name
                    //	|| HoneywellParamName.Telepen == name 			// not supported
                    || HoneywellParamName.UPCA == name
                    || HoneywellParamName.UPCE0 == name
                    || HoneywellParamName.UPCE1 == name
                    || HoneywellParamName.EAN13 == name
                    || HoneywellParamName.EAN8 == name
                    || HoneywellParamName.MSI == name
                    //	|| HoneywellParamName.PlesseyCode == name		// not supported
                    || HoneywellParamName.RSS14 == name
                    || HoneywellParamName.RSSLimit == name
                    || HoneywellParamName.RSSExp == name
                    //	|| HoneywellParamName.PosiCode == name			// not supported
                    || HoneywellParamName.TriopticCode == name
                    || HoneywellParamName.CodablockA == name
                    || HoneywellParamName.CodablockF == name
                    //	|| HoneywellParamName.Code16K == name			// not supported
                    //	|| HoneywellParamName.Code49 == name 			// not supported
                    || HoneywellParamName.PDF417 == name
                    || HoneywellParamName.MicroPDF == name
                    || HoneywellParamName.ComCode == name
                    || HoneywellParamName.TLC39 == name
                    //	|| HoneywellParamName.Postnet == name			// not supported
                    //	|| HoneywellParamName.Planet == name			// not supported
                    || HoneywellParamName.BritishPost == name
                    || HoneywellParamName.CanadianPost == name
                    || HoneywellParamName.KixPost == name
                    || HoneywellParamName.AustralianPost == name
                    || HoneywellParamName.JapanesePost == name
                    || HoneywellParamName.ChinaPost == name
                    || HoneywellParamName.KoreaPost == name
                    || HoneywellParamName.QRCode == name
                    || HoneywellParamName.Matrix == name
                    || HoneywellParamName.MaxiCode == name
                    || HoneywellParamName.AztecCode == name
                    //	|| HoneywellParamName.OCR == name 				// not supported
                    || HoneywellParamName.HanXinCode == name
                    || HoneywellParamName.NEC2of5 == name
                    || HoneywellParamName.GS1128 == name
                    || HoneywellParamName.MacroPDF417 == name
                    || HoneywellParamName.PostalCodes == name
                    ) {
                this.list.add(value);
            }
        }
        this.notifyDataSetChanged();
    }

    public HoneywellParamValueList getList() {
        return this.list;
    }

    @Override
    public int getCount() {
        return this.list.getCount();
    }

    @Override
    public Object getItem(int position) {
        return this.list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        SymbolStateViewHolder holder = null;

        if (null == convertView) {
            convertView = inflater.inflate(R.layout.item_symbol_state_list,
                    parent, false);
            holder = new SymbolStateViewHolder(convertView);
        } else {
            holder = (SymbolStateViewHolder) convertView.getTag();
        }
        holder.setItem(this.list.get(position));
        return convertView;
    }

    // ------------------------------------------------------------------------
    // Internal class SymbolStateViewHolder
    // ------------------------------------------------------------------------

    private class SymbolStateViewHolder implements OnCheckedChangeListener {
        private CheckBox state;
        private TextView name;

        private HoneywellParamValue item;

        public SymbolStateViewHolder(View parent) {
            this.state = (CheckBox) parent.findViewById(R.id.state);
            this.state.setOnCheckedChangeListener(this);
            this.name = (TextView) parent.findViewById(R.id.name);
            parent.setTag(this);
        }

        public void setItem(HoneywellParamValue item) {
            this.item = item;
            if(this.item.getName() == HoneywellParamName.PostalCodes) {
                this.state.setChecked((PostalCodes2D) this.item.getValue() == PostalCodes2D.Off ? false : true);
            } else {
                this.state.setChecked((Boolean) this.item.getValue());
            }
            this.name.setText(this.item.getName().toString());
        }

        @Override
        public void onCheckedChanged(CompoundButton buttonView,
                                     boolean isChecked) {

            if(this.item.getName() == HoneywellParamName.PostalCodes) {
                this.item.setValue(isChecked ? PostalCodes2D.PlanetAndPostnet : PostalCodes2D.Off);
            } else {
                this.item.setValue(isChecked);
            }
        }
    }
}
