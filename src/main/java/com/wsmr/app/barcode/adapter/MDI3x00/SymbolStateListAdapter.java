package com.wsmr.app.barcode.adapter.MDI3x00;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;
import android.widget.CompoundButton.OnCheckedChangeListener;

import com.wsmr.app.barcode.R;
import com.wsmr.lib.dev.barcode.opticon.param.OpticonParamName;
import com.wsmr.lib.dev.barcode.opticon.param.OpticonParamValue;
import com.wsmr.lib.dev.barcode.opticon.param.OpticonParamValueList;
import com.wsmr.lib.diagnostics.ATLog;

public class SymbolStateListAdapter extends BaseAdapter {

    private static final String TAG = "SymbolStateListAdapter";
    private LayoutInflater inflater;
    private OpticonParamValueList list;

    public SymbolStateListAdapter(Context context) {
        super();

        this.inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.list = new OpticonParamValueList();
    }

    public void initList(OpticonParamValueList param) {
        OpticonParamValue value;

        for (int i = 0; i < param.getCount(); i++) {
            value = param.get(i);
            OpticonParamName name = value.getName();
            ATLog.d(TAG, "name : %s", name.toString());

            if(OpticonParamName.UPCA == name || OpticonParamName.UPCE == name || OpticonParamName.EAN13 == name || OpticonParamName.EAN8 == name
                    || OpticonParamName.Code39 == name || OpticonParamName.Codabar == name || OpticonParamName.Industrial2of5 == name
                    || OpticonParamName.Interleaved2of5 == name || OpticonParamName.SCode == name || OpticonParamName.Code128 == name
                    || OpticonParamName.Code93 == name || OpticonParamName.IATA == name || OpticonParamName.MSI_Plessey == name || OpticonParamName.UK_Plessey == name
                    || OpticonParamName.Telepen == name || OpticonParamName.Code11 == name || OpticonParamName.Matrix2of5 == name || OpticonParamName.ChinesePost == name
                    || OpticonParamName.KoreanPostalAuthority == name || OpticonParamName.IntelligentMailBarCode == name || OpticonParamName.POSTNET == name
                    || OpticonParamName.JapanesePost == name || OpticonParamName.GS1_Databar == name || OpticonParamName.PDF417 == name
                    || OpticonParamName.MicroPDF417 == name || OpticonParamName.CodablockF == name || OpticonParamName.QRCode == name
                    || OpticonParamName.MicroQRCode == name || OpticonParamName.DataMatrix == name || OpticonParamName.Aztec == name
                    || OpticonParamName.ChineseSensibleCode == name || OpticonParamName.MaxiCode  == name)
            {
                this.list.add(value);
            }

        }
        this.notifyDataSetChanged();
    }

    public OpticonParamValueList getList() {
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

        private OpticonParamValue item;

        public SymbolStateViewHolder(View parent) {
            this.state = (CheckBox) parent.findViewById(R.id.state);
            this.state.setOnCheckedChangeListener(this);
            this.name = (TextView) parent.findViewById(R.id.name);
            parent.setTag(this);
        }

        public void setItem(OpticonParamValue item) {
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
