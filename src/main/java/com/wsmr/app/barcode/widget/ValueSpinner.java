package com.wsmr.app.barcode.widget;

import com.wsmr.app.barcode.adapter.ValueAdapter;
import com.wsmr.app.barcode.adapter.ValueItem;

import android.app.Activity;
import android.widget.Spinner;

public class ValueSpinner<T> {

    private Spinner mSpinner;
    private ValueAdapter<T> mAdapter;

    public ValueSpinner(Activity activity, int id) {
        this.mSpinner = (Spinner)activity.findViewById(id);
        this.mAdapter = new ValueAdapter<T>(activity);
        this.mSpinner.setAdapter(this.mAdapter);
    }

    public ValueSpinner(Activity activity, int id, T[] values) {
        this.mSpinner = (Spinner)activity.findViewById(id);
        this.mAdapter = new ValueAdapter<T>(activity);
        this.fill(values);
        this.mSpinner.setAdapter(this.mAdapter);
    }

    public ValueSpinner(Activity activity, int id, ValueItem<T>[] values) {
        this.mSpinner = (Spinner)activity.findViewById(id);
        this.mAdapter = new ValueAdapter<T>(activity);
        this.fill(values);
        this.mSpinner.setAdapter(this.mAdapter);
    }

    public void fill(T[] values) {
        for (T item : values) {
            this.mAdapter.add(new ValueItem<T>(item.toString(), item));
        }
        this.mAdapter.notifyDataSetChanged();
    }

    public void fill(ValueItem<T>[] values) {
        for (ValueItem<T> item : values) {
            this.mAdapter.add(item);
        }
        this.mAdapter.notifyDataSetChanged();
    }

    public Spinner getSpinner() {
        return this.mSpinner;
    }

    public ValueAdapter<T> getAdapter() {
        return this.mAdapter;
    }

    public void setValue(Object value) {
        @SuppressWarnings("unchecked")
        int position = this.mAdapter.getPosition((T)value);
        this.mSpinner.setSelection(position);
    }

    public T getValue() {
        int position = this.mSpinner.getSelectedItemPosition();
        return this.mAdapter.getItem(position);
    }

    public void setEnabled(boolean enabled) {
        this.mSpinner.setEnabled(enabled);
    }

    public void setVisibility(int visibility) {
        this.mSpinner.setVisibility(visibility);
    }
}
