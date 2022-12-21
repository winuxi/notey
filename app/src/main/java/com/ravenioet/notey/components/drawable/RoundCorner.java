package com.ravenioet.notey.components.drawable;

import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.os.Build;

import com.ravenioet.notey.components.theme.ActiveTheme;

public class RoundCorner {
    // prepare
    int strokeWidth = 5; // 5px not dp
    int roundRadius = 15; // 15px not dp
    GradientDrawable gd = new GradientDrawable();
    public RoundCorner(ActiveTheme theme){
        gd.setColor(theme.getPrimaryLight());
        gd.setCornerRadius(roundRadius);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            gd.setPadding(10,10,10,10);
        }
        gd.setStroke(strokeWidth, theme.getPrimaryDark());
    }
    public GradientDrawable apply(){
        return gd;
    }
}
