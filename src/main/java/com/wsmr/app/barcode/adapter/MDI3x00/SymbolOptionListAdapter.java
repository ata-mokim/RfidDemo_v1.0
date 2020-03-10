package com.wsmr.app.barcode.adapter.MDI3x00;

import java.util.ArrayList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

import com.wsmr.lib.dev.barcode.opticon.param.OpticonParamName;
import com.wsmr.lib.dev.barcode.opticon.param.OpticonParamValueList;
import com.wsmr.lib.diagnostics.ATLog;
import com.wsmr.app.barcode.R;
import com.wsmr.app.barcode.type.MDI3x00.ScanOpticonSymbolOption;


public class SymbolOptionListAdapter  extends BaseAdapter {

    private static final String TAG = "SymbolOptionListAdapter";

    private Context mContext;
    private LayoutInflater mInflater;
    private ArrayList<SymbolOptionListItem> mList;

    public SymbolOptionListAdapter(Context context) {
        super();

        mContext = context;
        mInflater = (LayoutInflater) mContext
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mList = new ArrayList<SymbolOptionListItem>();
    }

    public void updateList(OpticonParamValueList param) {

        mList.clear();

        if ((Boolean)param.getValueAt(OpticonParamName.Aztec)) {
            mList.add(new SymbolOptionListItem(ScanOpticonSymbolOption.Aztec));
        }

        if ((Boolean)param.getValueAt(OpticonParamName.UPCA)) {
            mList.add(new SymbolOptionListItem(ScanOpticonSymbolOption.UPCA));
        }

        if ((Boolean)param.getValueAt(OpticonParamName.UPCE)) {
            mList.add(new SymbolOptionListItem(ScanOpticonSymbolOption.UPCE));
        }

        if ((Boolean)param.getValueAt(OpticonParamName.EAN13)) {
            mList.add(new SymbolOptionListItem(ScanOpticonSymbolOption.EAN13));
        }

        if ((Boolean)param.getValueAt(OpticonParamName.EAN8)) {
            mList.add(new SymbolOptionListItem(ScanOpticonSymbolOption.EAN8));
        }

        if ((Boolean)param.getValueAt(OpticonParamName.Code39)) {
            mList.add(new SymbolOptionListItem(ScanOpticonSymbolOption.Code39));
        }

        if ((Boolean)param.getValueAt(OpticonParamName.Codabar)) {
            mList.add(new SymbolOptionListItem(ScanOpticonSymbolOption.Codabar));
        }

        if ((Boolean)param.getValueAt(OpticonParamName.Industrial2of5)) {
            mList.add(new SymbolOptionListItem(ScanOpticonSymbolOption.Industrial2of5));
        }

        if ((Boolean)param.getValueAt(OpticonParamName.Interleaved2of5)) {
            mList.add(new SymbolOptionListItem(ScanOpticonSymbolOption.Interleaved2of5));
        }

        if ((Boolean)param.getValueAt(OpticonParamName.SCode)) {
            mList.add(new SymbolOptionListItem(ScanOpticonSymbolOption.SCode));
        }

        if ((Boolean)param.getValueAt(OpticonParamName.Code128)) {
            mList.add(new SymbolOptionListItem(ScanOpticonSymbolOption.Code128));
        }

        if ((Boolean)param.getValueAt(OpticonParamName.Code93)) {
            mList.add(new SymbolOptionListItem(ScanOpticonSymbolOption.Code93));
        }

        if ((Boolean)param.getValueAt(OpticonParamName.IATA)) {
            mList.add(new SymbolOptionListItem(ScanOpticonSymbolOption.IATA));
        }

        if ((Boolean)param.getValueAt(OpticonParamName.MSI_Plessey)) {
            mList.add(new SymbolOptionListItem(ScanOpticonSymbolOption.MSI_Plessey));
        }

        if ((Boolean)param.getValueAt(OpticonParamName.UK_Plessey)) {
            mList.add(new SymbolOptionListItem(ScanOpticonSymbolOption.UK_Plessey));
        }

        if ((Boolean)param.getValueAt(OpticonParamName.Telepen)) {
            mList.add(new SymbolOptionListItem(ScanOpticonSymbolOption.Telepen));
        }

        if ((Boolean)param.getValueAt(OpticonParamName.Code11)) {
            mList.add(new SymbolOptionListItem(ScanOpticonSymbolOption.Code11));
        }

        if ((Boolean)param.getValueAt(OpticonParamName.Matrix2of5)) {
            mList.add(new SymbolOptionListItem(ScanOpticonSymbolOption.Matrix2of5));
        }

        if ((Boolean)param.getValueAt(OpticonParamName.ChinesePost)) {
            mList.add(new SymbolOptionListItem(ScanOpticonSymbolOption.ChinesePost));
        }

        if ((Boolean)param.getValueAt(OpticonParamName.KoreanPostalAuthority)) {
            mList.add(new SymbolOptionListItem(ScanOpticonSymbolOption.KoreanPostalAuthority));
        }

        if ((Boolean)param.getValueAt(OpticonParamName.IntelligentMailBarCode)) {
            mList.add(new SymbolOptionListItem(ScanOpticonSymbolOption.IntelligentMailBarCode));
        }

        if ((Boolean)param.getValueAt(OpticonParamName.POSTNET)) {
            mList.add(new SymbolOptionListItem(ScanOpticonSymbolOption.POSTNET));
        }

        if ((Boolean)param.getValueAt(OpticonParamName.JapanesePost)) {
            mList.add(new SymbolOptionListItem(ScanOpticonSymbolOption.JapanesePost));
        }

        if ((Boolean)param.getValueAt(OpticonParamName.GS1_Databar)) {
            mList.add(new SymbolOptionListItem(ScanOpticonSymbolOption.GS1_Databar));
        }

        if ((Boolean)param.getValueAt(OpticonParamName.PDF417)) {
            mList.add(new SymbolOptionListItem(ScanOpticonSymbolOption.PDF417));
        }

        if ((Boolean)param.getValueAt(OpticonParamName.MicroPDF417)) {
            mList.add(new SymbolOptionListItem(ScanOpticonSymbolOption.MicroPDF417));
        }

        if ((Boolean)param.getValueAt(OpticonParamName.CodablockF)) {
            mList.add(new SymbolOptionListItem(ScanOpticonSymbolOption.CodablockF));
        }

        if ((Boolean)param.getValueAt(OpticonParamName.QRCode)) {
            mList.add(new SymbolOptionListItem(ScanOpticonSymbolOption.QRCode));
        }

        if ((Boolean)param.getValueAt(OpticonParamName.MicroQRCode)) {
            mList.add(new SymbolOptionListItem(ScanOpticonSymbolOption.MicroQR));
        }

        if ((Boolean)param.getValueAt(OpticonParamName.DataMatrix)) {
            mList.add(new SymbolOptionListItem(ScanOpticonSymbolOption.DataMatrix));
        }

        if ((Boolean)param.getValueAt(OpticonParamName.ChineseSensibleCode)) {
            mList.add(new SymbolOptionListItem(ScanOpticonSymbolOption.ChineseSensibleCode));
        }

        if ((Boolean)param.getValueAt(OpticonParamName.MaxiCode)) {
            mList.add(new SymbolOptionListItem(ScanOpticonSymbolOption.Maxicode));
        }


        notifyDataSetChanged();
        ATLog.d(TAG,  "--- updateList");
    }

    @Override
    public int getCount() {
        return mList.size();
    }
    @Override
    public Object getItem(int position) {
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
        private ScanOpticonSymbolOption mOption;

        private View mView;
        private TextView mSymbol;
        private Button mButton;

        public SymbolOptionListItem(ScanOpticonSymbolOption option) {
            mOption = option;
        }

        public ScanOpticonSymbolOption getOption() {
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
