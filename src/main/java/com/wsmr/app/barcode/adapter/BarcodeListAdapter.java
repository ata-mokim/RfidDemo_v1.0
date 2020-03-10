package com.wsmr.app.barcode.adapter;

import java.util.ArrayList;

import com.wsmr.app.barcode.R;
import com.wsmr.lib.dev.barcode.type.BarcodeType;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class BarcodeListAdapter extends BaseAdapter {

    @SuppressWarnings("unused")
    private static final String TAG = "BarcodeListAdapter";

    private LayoutInflater inflater;
    private ArrayList<BarcodeListItem> list;

    // ------------------------------------------------------------------------
    // Constructor BarcodeListAdapter
    // ------------------------------------------------------------------------

    public BarcodeListAdapter(Context context) {
        super();

        this.inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.list = new ArrayList<BarcodeListItem>();
    }

    public void clear() {
        this.list.clear();
        this.notifyDataSetChanged();
    }

    public int addItem(BarcodeType type, String barcode) {
        BarcodeListItem item = null;
        int position = -1;

        if ((item = findItem(type, barcode)) != null) {
            item.increaseCount();
            position = this.list.indexOf(item);
        } else {
            item = new BarcodeListItem(type, barcode);
            this.list.add(item);
            position = this.list.size() - 1;
        }
        this.notifyDataSetChanged();
        return position;
    }

    private BarcodeListItem findItem(BarcodeType type, String barcode) {
        for (BarcodeListItem item : this.list) {
            if (item.compare(type, barcode)) {
                return item;
            }
        }
        return null;
    }

    @Override
    public int getCount() {
        return this.list.size();
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
        BarcodeViewHolder holder = null;

        if (null == convertView) {
            convertView = inflater.inflate(R.layout.item_barcode_list, parent,
                    false);
            holder = new BarcodeViewHolder(convertView);
        } else {
            holder = (BarcodeViewHolder) convertView.getTag();
        }
        holder.setItem(this.list.get(position));
        return convertView;
    }

    // ------------------------------------------------------------------------
    // Internal class BarcodeListItem
    // ------------------------------------------------------------------------

    private class BarcodeListItem {
        private BarcodeType type;
        private String barcode;
        private int count;

        public BarcodeListItem(BarcodeType type, String barcode) {
            super();
            this.type = type;
            this.barcode = barcode;
            this.count = 1;
        }

        public BarcodeType getType() {
            return this.type;
        }

        public String getBarcode() {
            return this.barcode;
        }

        public int getCount() {
            return this.count;
        }

        public boolean compare(BarcodeType type, String barcode) {
            return this.type == type && this.barcode.equals(barcode);
        }

        public void increaseCount() {
            this.count++;
        }
    }

    // ------------------------------------------------------------------------
    // Internal class BarcodeListViewHolder
    // ------------------------------------------------------------------------

    private class BarcodeViewHolder {
        private TextView type;
        private TextView barcode;
        private TextView count;
        private View parent;

        public BarcodeViewHolder(View parent) {
            this.type = (TextView) parent.findViewById(R.id.code_type);
            this.barcode = (TextView) parent.findViewById(R.id.barcode);
            this.count = (TextView) parent.findViewById(R.id.count);
            this.parent = parent;
            this.parent.setTag(this);
        }

        public void setItem(BarcodeListItem item) {
            this.type.setText(item.getType().toString());
            this.barcode.setText(item.getBarcode());
            this.count.setText("" + item.getCount());
        }
    }
}
