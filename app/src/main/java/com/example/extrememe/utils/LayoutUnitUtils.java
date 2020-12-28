package com.example.extrememe.utils;

import android.util.DisplayMetrics;
import android.util.TypedValue;

public class LayoutUnitUtils {
    private static LayoutUnitUtils instance;

    private LayoutUnitUtils() {}

    public static LayoutUnitUtils getInstance() {
        if(instance == null) {
            instance = new LayoutUnitUtils();
        }

        return instance;
    }

    public int convertPixelToDpUnit(int pixels, DisplayMetrics displayMetrics) {
        return (int)TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, pixels, displayMetrics);
    }
}
