package com.example.extrememe.utils;

import android.content.res.Resources;
import android.graphics.Color;

public class ColorUtils {
    private static ColorUtils instance;
    private Resources resources;

    private ColorUtils(Resources resources) {
        this.resources = resources;
    }

    public static ColorUtils getInstance(Resources resources) {
        if(instance == null) {
            instance = new ColorUtils(resources);
        }

        return instance;
    }

    public int getColorByResourceId(int resourceId) {
        return Color.parseColor(this.resources.getString(resourceId));
    }
}
