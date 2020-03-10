package com.wsmr.app.barcode.widget.filter;

import android.text.InputFilter;
import android.text.Spanned;

public class InputFilterIntMinMax implements InputFilter {

    private int mMin;
    private int mMax;

    public InputFilterIntMinMax(int min, int max) {
        this.mMin = min;
        this.mMax = max;
    }

    public InputFilterIntMinMax(String min, String max) {
        try {
            this.mMin = Integer.parseInt(min);
        } catch (NumberFormatException e) {
            this.mMin = 0;
        }
        try {
            this.mMax = Integer.parseInt(max);
        } catch (NumberFormatException e) {
            this.mMax = 0;
        }
    }

    @Override
    public CharSequence filter(CharSequence source, int start, int end,
                               Spanned dest, int dstart, int dend) {
        String input = "";
        int value = 0;
        try {
            input += dest.subSequence(0, dstart);
            input += source.subSequence(start, end);
            input += dest.subSequence(dend, dest.length());
            value = Integer.parseInt(input);
        } catch (NumberFormatException e) {
            value = 0;
        }
        if (this.mMin > 0 && this.mMax > 0 && this.mMin < this.mMax) {
            if (value >= this.mMin && value <= this.mMax) {
                return null;
            } else if (value < this.mMin) {
                return "" + this.mMin;
            } else if (value > this.mMax) {
                return "" + this.mMax;
            }
        } else if (this.mMin > 0 && this.mMax > 0 && this.mMin > this.mMax) {
            if (value <= this.mMin && value >= this.mMax) {
                return null;
            } else if (value > this.mMin) {
                return "" + this.mMin;
            } else if (value < this.mMax) {
                return "" + this.mMax;
            }
        } else if (this.mMin > 0 && this.mMax > 0 && this.mMin == this.mMax) {
            if (value == this.mMin && value == this.mMax) {
                return null;
            } else {
                return "" + this.mMax;
            }
        } else if (this.mMin == 0 && this.mMax > 0) {
            if (value <= this.mMax) {
                return null;
            } else if (value > this.mMax) {
                return "" + this.mMax;
            }
        } else if (this.mMin > 0 && this.mMax == 0) {
            if (value >= this.mMin) {
                return null;
            } else if (value < this.mMin) {
                return "" + this.mMin;
            }
        } else {
            return null;
        }
        return "";
    }

    public static InputFilter[] getFilters(int min, int max) {
        return new InputFilter[] { new InputFilterIntMinMax(min, max) };
    }
}
