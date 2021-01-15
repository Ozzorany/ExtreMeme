package com.example.extrememe.utils;

import android.content.Context;
import android.content.res.Resources;
import android.widget.Button;
import android.widget.LinearLayout;

import com.example.extrememe.model.Category;

public class CategoryViewUtils {
    public Button generateCategoryButton(Category category, Context context, Resources resources, LinearLayout parentLayout) {
        final int categoryButtonWidth = 110;
        final int categoryButtonHeight = 50;
        final int margin = 5;

        Button myButton = new Button(context);
        myButton.setText(category.getName());
        myButton.setWidth(LayoutUnitUtils.getInstance()
                .convertPixelToDpUnit(categoryButtonWidth, resources.getDisplayMetrics()));
        myButton.setHeight(LayoutUnitUtils.getInstance()
                .convertPixelToDpUnit(categoryButtonHeight, resources.getDisplayMetrics()));

        int marginInDP = LayoutUnitUtils.getInstance()
                .convertPixelToDpUnit(margin, resources.getDisplayMetrics());
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        lp.setMargins(marginInDP, marginInDP, marginInDP, marginInDP);

        myButton.setTag(category.getId());
        parentLayout.addView(myButton, lp);

        return myButton;
    }
}
