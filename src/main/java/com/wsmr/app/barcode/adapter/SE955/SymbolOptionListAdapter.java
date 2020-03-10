package com.wsmr.app.barcode.adapter.SE955;

import java.util.ArrayList;

import com.wsmr.lib.dev.barcode.motorola.param.SSIParamName;
import com.wsmr.lib.dev.barcode.motorola.param.SSIParamValueList;
import com.wsmr.app.barcode.R;
import com.wsmr.app.barcode.type.SE955.Scan1dSymbolOption;

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

    private Context mContext;
    private LayoutInflater mInflater;
    private ArrayList<SymbolOptionListItem> mList;

    // ------------------------------------------------------------------------
    // Constructor SymbolOptionListAdapter
    // ------------------------------------------------------------------------

    public SymbolOptionListAdapter(Context context) {
        super();

        mContext = context;
        mInflater = (LayoutInflater) mContext
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mList = new ArrayList<SymbolOptionListItem>();
    }

    public void updateList(SSIParamValueList param) {
        mList.clear();

        // is UPC/EAN Option
        if ((Boolean)param.getValueAt(SSIParamName.UPC_A)
                || (Boolean)param.getValueAt(SSIParamName.UPC_E)
                || (Boolean)param.getValueAt(SSIParamName.UPC_E1)
                || (Boolean)param.getValueAt(SSIParamName.EAN_8)
                || (Boolean)param.getValueAt(SSIParamName.EAN_13)
                || (Boolean)param.getValueAt(SSIParamName.Bookland_EAN)) {
            mList.add(new SymbolOptionListItem(Scan1dSymbolOption.UpcEan));
        }
        // is Code 39 Option
        if ((Boolean)param.getValueAt(SSIParamName.Code39)) {
            mList.add(new SymbolOptionListItem(Scan1dSymbolOption.Code39));
        }
        // is Code 93 Option
        if ((Boolean)param.getValueAt(SSIParamName.Code93)) {
            mList.add(new SymbolOptionListItem(Scan1dSymbolOption.Code93));
        }
        // is Code 11 Option
        if ((Boolean)param.getValueAt(SSIParamName.Code11)) {
            mList.add(new SymbolOptionListItem(Scan1dSymbolOption.Code11));
        }
        // is Interleaved 2 of 5 Option
        if ((Boolean)param.getValueAt(SSIParamName.I2of5)) {
            mList.add(new SymbolOptionListItem(Scan1dSymbolOption.I2of5));
        }
        // is Discrete 2 of 5 Option
        if ((Boolean)param.getValueAt(SSIParamName.D2of5)) {
            mList.add(new SymbolOptionListItem(Scan1dSymbolOption.D2of5));
        }
        // is Codabar Option
        if ((Boolean)param.getValueAt(SSIParamName.Codabar)) {
            mList.add(new SymbolOptionListItem(Scan1dSymbolOption.Codabar));
        }
        // is MSI Option
        if ((Boolean)param.getValueAt(SSIParamName.MSI)) {
            mList.add(new SymbolOptionListItem(Scan1dSymbolOption.Msi));
        }
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return mList.size();
    }

    @Override
    public Scan1dSymbolOption getItem(int position) {
        return mList.get(position).getOption();
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        SymbolOptionListItem item = mList.get(position);

        if (null == convertView) {
            convertView = mInflater.inflate(R.layout.item_symbol_option_list,
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

        private Scan1dSymbolOption mOption;

        private View mView;
        private TextView mSymbol;
        private Button mButton;

        public SymbolOptionListItem(Scan1dSymbolOption option) {
            mOption = option;
        }

        public Scan1dSymbolOption getOption() {
            return mOption;
        }

        public View getView() {
            return mView;
        }

        public void setView(View view) {
            mView = view;
            mSymbol = (TextView) mView.findViewById(R.id.symbol);
            mButton = (Button) mView.findViewById(R.id.detail_option);
            mButton.setOnClickListener((Button.OnClickListener)mContext);
            // 2019-12-18 ncsin4 disable symbol option button
            mButton.setVisibility(View.GONE);
        }

        public void displayItem() {
            mSymbol.setText(mOption.getName());
            mButton.setTag(mOption);
        }
    }
}
