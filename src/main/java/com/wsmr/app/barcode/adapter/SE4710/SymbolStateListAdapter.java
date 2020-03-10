package com.wsmr.app.barcode.adapter.SE4710;

import com.wsmr.app.barcode.R;
import com.wsmr.lib.dev.barcode.motorola.param.SSIParamName;
import com.wsmr.lib.dev.barcode.motorola.param.SSIParamValue;
import com.wsmr.lib.dev.barcode.motorola.param.SSIParamValueList;
import com.wsmr.lib.diagnostics.ATLog;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.TextView;

public class SymbolStateListAdapter extends BaseAdapter {

    private static final String TAG = "SymbolStateListAdapter";

    private LayoutInflater inflater;
    private SSIParamValueList list;

    public SymbolStateListAdapter(Context context) {
        super();

        this.inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.list = new SSIParamValueList();
    }

    public void initList(SSIParamValueList param) {
        SSIParamValue value;

        for (int i = 0; i < param.getCount(); i++) {
            value = param.get(i);
            SSIParamName name = value.getName();
            ATLog.d(TAG, "name : %s", name.toString());
            if (SSIParamName.UPC_A == name || SSIParamName.UPC_E == name
                    || SSIParamName.UPC_E1 == name || SSIParamName.EAN_8 == name
                    || SSIParamName.EAN_13 == name || SSIParamName.Bookland_EAN == name
                    || SSIParamName.Code128 == name || SSIParamName.Code39 == name
                    || SSIParamName.Code93 == name || SSIParamName.Code11 == name
                    || SSIParamName.I2of5 == name || SSIParamName.D2of5 == name
                    || SSIParamName.Ch2of5 == name || SSIParamName.Codabar == name
                    || SSIParamName.MSI == name || SSIParamName.RSS_14 == name
                    || SSIParamName.RSS_Limited == name || SSIParamName.RSS_Expanded == name
                    || SSIParamName.Aztec == name || SSIParamName.Data_Matrix == name
                    || SSIParamName.Maxicode == name || SSIParamName.MicroQR == name
                    || SSIParamName.MicroPDF417 == name || SSIParamName.PDF417 == name
                    || SSIParamName.QRCode == name || SSIParamName.Matrix2of5 == name
                    || SSIParamName.Korea_3of5 == name || SSIParamName.US_Postnet == name
                    || SSIParamName.US_Planet == name || SSIParamName.Japan_Postal == name
                    || SSIParamName.Australia_Post == name || SSIParamName.Netherlands_KIX_Code == name
                    || SSIParamName.USPS_4CB_OneCode_Intelligent_Mail == name || SSIParamName.UPU_FICS_Postal == name
                    || SSIParamName.Composite_CC_C == name || SSIParamName.Composite_CC_AB == name
                    || SSIParamName.Composite_TLC_39 == name || SSIParamName.ISSN_EAN == name
                    || SSIParamName.UCC_EAN_128 == name || SSIParamName.ISBT128 == name
                    || SSIParamName.HanXin == name || SSIParamName.UK_Postal == name)
            {
                this.list.add(value);
            }
        }
        this.notifyDataSetChanged();
    }

    public SSIParamValueList getList() {
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

        private SSIParamValue item;

        public SymbolStateViewHolder(View parent) {
            this.state = (CheckBox) parent.findViewById(R.id.state);
            this.state.setOnCheckedChangeListener(this);
            this.name = (TextView) parent.findViewById(R.id.name);
            parent.setTag(this);
        }

        public void setItem(SSIParamValue item) {
            this.item = item;
            this.state.setChecked((Boolean)this.item.getValue());
            this.name.setText(this.item.getName().toString());
        }

        @Override
        public void onCheckedChanged(CompoundButton buttonView,
                                     boolean isChecked) {
            this.item.setValue(isChecked);
        }
    }
}
