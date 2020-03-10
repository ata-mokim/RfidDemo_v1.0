package com.wsmr.app.barcode.adapter.SE4710;

import java.util.ArrayList;
import com.wsmr.lib.dev.barcode.motorola.param.SSIParamName;
import com.wsmr.lib.dev.barcode.motorola.param.SSIParamValueList;
import com.wsmr.lib.diagnostics.ATLog;
import com.wsmr.app.barcode.R;
import com.wsmr.app.barcode.type.SE4710.Scan1dSymbolOption;
import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

@SuppressLint("ViewHolder")
public class SymbolOptionListAdapter extends BaseAdapter {

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

    public void updateList(SSIParamValueList param) {

        ATLog.d(TAG, "+++ updateList");
        ATLog.d(TAG, "param Count : " + param.getCount());

        this.list.clear();

        // is UPC/EAN Option
        if ((Boolean)param.getValueAt(SSIParamName.UPC_A)
                || (Boolean)param.getValueAt(SSIParamName.UPC_E)
                || (Boolean)param.getValueAt(SSIParamName.UPC_E1)
                || (Boolean)param.getValueAt(SSIParamName.EAN_8)
                || (Boolean)param.getValueAt(SSIParamName.EAN_13)
                || (Boolean)param.getValueAt(SSIParamName.Bookland_EAN)) {
            ATLog.d(TAG, "-> UpcEan");
            this.list.add(new SymbolOptionListItem(Scan1dSymbolOption.UpcEan));
        }
        // is Code 128 Option
        if ((Boolean)param.getValueAt(SSIParamName.Code128)) {
            this.list.add(new SymbolOptionListItem(Scan1dSymbolOption.Code128));
            ATLog.d(TAG, "-> Code128");
        }
        // is Code 39 Option
        if ((Boolean)param.getValueAt(SSIParamName.Code39)) {
            this.list.add(new SymbolOptionListItem(Scan1dSymbolOption.Code39));
            ATLog.d(TAG, "-> Code39");
        }
        // is Code 93 Option
        if ((Boolean)param.getValueAt(SSIParamName.Code93)) {
            this.list.add(new SymbolOptionListItem(Scan1dSymbolOption.Code93));
            ATLog.d(TAG, "-> Code93");
        }
        // is Code 11 Option
        if ((Boolean)param.getValueAt(SSIParamName.Code11)) {
            this.list.add(new SymbolOptionListItem(Scan1dSymbolOption.Code11));
            ATLog.d(TAG, "-> Code11");
        }
        // is Interleaved 2 of 5 Option
        if ((Boolean)param.getValueAt(SSIParamName.I2of5)) {
            this.list.add(new SymbolOptionListItem(Scan1dSymbolOption.I2of5));
            ATLog.d(TAG, "-> I2of5");
        }
        // is Discrete 2 of 5 Option
        if ((Boolean)param.getValueAt(SSIParamName.D2of5)) {
            this.list.add(new SymbolOptionListItem(Scan1dSymbolOption.D2of5));
            ATLog.d(TAG, "-> D2of5");
        }
        // is Chinese 2 of 5Option
        if ((Boolean)param.getValueAt(SSIParamName.Ch2of5)) {
            this.list.add(new SymbolOptionListItem(Scan1dSymbolOption.Ch2of5));
            ATLog.d(TAG, "-> Ch2of5");
        }
        // is Codabar Option
        if ((Boolean)param.getValueAt(SSIParamName.Codabar)) {
            this.list.add(new SymbolOptionListItem(Scan1dSymbolOption.Codabar));
            ATLog.d(TAG, "-> Codabar");
        }
        // is MSI Option
        if ((Boolean)param.getValueAt(SSIParamName.MSI)) {
            this.list.add(new SymbolOptionListItem(Scan1dSymbolOption.Msi));
            ATLog.d(TAG, "-> Msi");
        }

        // is Aztec Option
        if ((Boolean)param.getValueAt(SSIParamName.Aztec)) {
            this.list.add(new SymbolOptionListItem(Scan1dSymbolOption.Aztec));
            ATLog.d(TAG, "-> Aztec");
        }
        // is Data Matrix Option
        if ((Boolean)param.getValueAt(SSIParamName.Data_Matrix)) {
            this.list.add(new SymbolOptionListItem(Scan1dSymbolOption.Data_Matrix));
            ATLog.d(TAG, "-> Data_Matrix");
        }
        // is Maxicode Option
        if ((Boolean)param.getValueAt(SSIParamName.Maxicode)) {
            this.list.add(new SymbolOptionListItem(Scan1dSymbolOption.Maxicode));
            ATLog.d(TAG, "-> Maxicode");
        }
        // is MicroPDF417 Option
        if ((Boolean)param.getValueAt(SSIParamName.MicroPDF417)) {
            this.list.add(new SymbolOptionListItem(Scan1dSymbolOption.MicroPDF417));
            ATLog.d(TAG, "-> MicroPDF417");
        }
        // is MicroQR Option
        if ((Boolean)param.getValueAt(SSIParamName.MicroQR)) {
            this.list.add(new SymbolOptionListItem(Scan1dSymbolOption.MicroQR));
            ATLog.d(TAG, "-> MicroQR");
        }
        // is PDF417 Option
        if ((Boolean)param.getValueAt(SSIParamName.PDF417)) {
            this.list.add(new SymbolOptionListItem(Scan1dSymbolOption.PDF417));
            ATLog.d(TAG, "-> PDF417");
        }
        // is QRCode Option
        if ((Boolean)param.getValueAt(SSIParamName.QRCode)) {
            this.list.add(new SymbolOptionListItem(Scan1dSymbolOption.QRCode));
            ATLog.d(TAG, "-> QRCode");
        }
        // is Matrix 2 of 5 Option
        if ((Boolean)param.getValueAt(SSIParamName.Matrix2of5)) {
            this.list.add(new SymbolOptionListItem(Scan1dSymbolOption.Matrix2of5));
            ATLog.d(TAG, "-> Matrix2of5");
        }
        // is Korea 3 of 5 Option
        if ((Boolean)param.getValueAt(SSIParamName.Korea_3of5)) {
            this.list.add(new SymbolOptionListItem(Scan1dSymbolOption.Korea_3of5));
            ATLog.d(TAG, "-> Korea_3of5");
        }
        // is US Postnet Option
        if ((Boolean)param.getValueAt(SSIParamName.US_Postnet)) {
            this.list.add(new SymbolOptionListItem(Scan1dSymbolOption.US_Postnet));
            ATLog.d(TAG, "-> US_Postnet");
        }
        // is UK Postal Option
        if ((Boolean)param.getValueAt(SSIParamName.UK_Postal)) {
            this.list.add(new SymbolOptionListItem(Scan1dSymbolOption.UK_Postal));
            ATLog.d(TAG, "-> UK Postal");
        }
        // is US Planet Option
        if ((Boolean)param.getValueAt(SSIParamName.US_Planet)) {
            this.list.add(new SymbolOptionListItem(Scan1dSymbolOption.US_Planet));
            ATLog.d(TAG, "-> US_Planet");
        }
        // is Japan Postal Option
        if ((Boolean)param.getValueAt(SSIParamName.Japan_Postal)) {
            this.list.add(new SymbolOptionListItem(Scan1dSymbolOption.Japan_Postal));
            ATLog.d(TAG, "-> Japan_Postal");
        }
        // is Australia Post Option
        if ((Boolean)param.getValueAt(SSIParamName.Australia_Post)) {
            this.list.add(new SymbolOptionListItem(Scan1dSymbolOption.Australia_Post));
            ATLog.d(TAG, "-> Australia_Post");
        }
        // is Netherlands KIX Code Option
        if ((Boolean)param.getValueAt(SSIParamName.Netherlands_KIX_Code)) {
            this.list.add(new SymbolOptionListItem(Scan1dSymbolOption.Netherlands_KIX_Code));
            ATLog.d(TAG, "-> Netherlands_KIX_Code");
        }
        // is USPS_4CB One Code Inteligent Mail Option
        if ((Boolean)param.getValueAt(SSIParamName.USPS_4CB_OneCode_Intelligent_Mail)) {
            this.list.add(new SymbolOptionListItem(Scan1dSymbolOption.USPS_4CB_OneCode_Inteligent_Mail));
            ATLog.d(TAG, "-> USPS_4CB_OneCode_Inteligent_Mail");
        }
        // is UPU FICS Postral Option
        if ((Boolean)param.getValueAt(SSIParamName.UPU_FICS_Postal)) {
            this.list.add(new SymbolOptionListItem(Scan1dSymbolOption.UPU_FICS_Postral));
            ATLog.d(TAG, "-> UPU_FICS_Postral");
        }
        // is Composite CC-C Option
        if ((Boolean)param.getValueAt(SSIParamName.Composite_CC_C)
                || (Boolean)param.getValueAt(SSIParamName.Composite_CC_AB)
                || (Boolean)param.getValueAt(SSIParamName.Composite_TLC_39)) {
            this.list.add(new SymbolOptionListItem(Scan1dSymbolOption.Composite));
            ATLog.d(TAG, "-> Composite");
        }
        // is ISSN EAN Option
        if ((Boolean)param.getValueAt(SSIParamName.ISSN_EAN)) {
            this.list.add(new SymbolOptionListItem(Scan1dSymbolOption.ISSN_EAN));
            ATLog.d(TAG, "-> ISSN_EAN");
        }/*
		// is GS1 128 Option
		if ((Boolean)param.getValueAt(SSIParamName.GS1_128)) {
			this.list.add(new SymbolOptionListItem(Scan1dSymbolOption.GS1_128));
		}
		// is ISBT 128 Option
		if ((Boolean)param.getValueAt(SSIParamName.ISBT_128)) {
			this.list.add(new SymbolOptionListItem(Scan1dSymbolOption.ISBT_128));
		}*/
        // is HanXin Option
        if ((Boolean)param.getValueAt(SSIParamName.HanXin)) {
            this.list.add(new SymbolOptionListItem(Scan1dSymbolOption.HanXin));
            ATLog.d(TAG, "-> HanXin");
        }

        if ((Boolean)param.getValueAt(SSIParamName.RSS_14) ||
                (Boolean)param.getValueAt(SSIParamName.RSS_Limited) ||
                (Boolean)param.getValueAt(SSIParamName.RSS_Expanded)) {
            this.list.add(new SymbolOptionListItem(Scan1dSymbolOption.RSS_14));
            ATLog.d(TAG, "-> RSS_14");
        }
		/*
		if ((Boolean)param.getValueAt(SSIParamName.RSS_Limited)) {
			this.list.add(new SymbolOptionListItem(Scan1dSymbolOption.RSS_Limited));
			ATLog.d(TAG, "-> RSS_Limited");
		}

		if ((Boolean)param.getValueAt(SSIParamName.RSS_Expanded)) {
			this.list.add(new SymbolOptionListItem(Scan1dSymbolOption.RSS_Expanded));
			ATLog.d(TAG, "-> RSS_Expanded");
		}*/

        this.notifyDataSetChanged();
        ATLog.d(TAG, "--- updateList");
    }

    @Override
    public int getCount() {
        return this.list.size();
    }

    @Override
    public Scan1dSymbolOption getItem(int position) {
        return this.list.get(position).getOption();
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        SymbolOptionListItem item = this.list.get(position);

        // 2019.01.27
        //if (null == convertView) {
        convertView = inflater.inflate(R.layout.item_symbol_option_list,
                parent, false);
        item.setView(convertView);
        //}


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
        private Scan1dSymbolOption option;

        private View view;
        private TextView symbol;
        private Button button;

        public SymbolOptionListItem(Scan1dSymbolOption option) {
            this.option = option;
        }

        public Scan1dSymbolOption getOption() {
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
            this.symbol.setText(option.getName());
            this.button.setTag(this.option);
        }
    }
}
