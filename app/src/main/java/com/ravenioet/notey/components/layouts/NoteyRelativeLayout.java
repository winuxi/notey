package com.ravenioet.notey.components.layouts;

import android.content.Context;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.ravenioet.notey.components.drawable.RoundCorner;
import com.ravenioet.notey.components.provider.ThemeProvider;
import com.ravenioet.notey.components.theme.NoteyTheme;
import com.ravenioet.notey.components.widget.NoteyTextView;

public class NoteyRelativeLayout extends RelativeLayout {
    LayoutParams rootLayParams;
    public NoteyRelativeLayout(Context context){
        super(context);
        RoundCorner roundCorner = new RoundCorner(ThemeProvider.getMainTheme());
        rootLayParams = new LayoutParams(LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        rootLayParams.setMargins(5,5,5,5);
        setPadding(15,15,15,15);
        setLayoutParams(rootLayParams);
        setBackground(roundCorner.apply());
    }
    public LayoutParams getLayParams(){
        return rootLayParams;
    }
}
