package com.ravenioet.notey.components;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Color;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.Timer;
import java.util.TimerTask;

@SuppressLint("ResourceType")
public class MenuItem extends RelativeLayout {
    public static int TITLE = 3;
    public static int INFO = 4;
    public int getPx(int value){
        Resources r = getResources();
        return Math.round(TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP, value,r.getDisplayMetrics()));
    }
    public MenuItem(Context context, String title, String hint){
        super(context);
        LayoutParams layout_216 = new LayoutParams(LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        layout_216.setMargins(10,10,10,10);
        setPadding(5,5,5,5);
        setLayoutParams(layout_216);
        //setBackgroundColor(Color.DKGRAY);
        TextView titleView = new TextView(context);
        titleView.setId(TITLE);
        titleView.setPadding(5,5,5,5);
        titleView.setText(title);
        titleView.setTextSize(getPx(10));
        LayoutParams titleParams = new LayoutParams(LayoutParams.WRAP_CONTENT,LayoutParams.WRAP_CONTENT);
        titleParams.addRule(RelativeLayout.ALIGN_PARENT_TOP);
        titleView.setLayoutParams(titleParams);
        addView(titleView);

        TextView hintView = new TextView(context);
        hintView.setId(INFO);
        hintView.setPadding(5,1,5,5);
        hintView.setText(hint);
        hintView.setTextSize(getPx(7));

        LayoutParams hintParams = new LayoutParams(LayoutParams.WRAP_CONTENT,LayoutParams.WRAP_CONTENT);
        hintParams.addRule(RelativeLayout.BELOW,titleView.getId());
        hintView.setLayoutParams(hintParams);
        addView(hintView);
    }
    @SuppressLint("ResourceType")
    public MenuItem(Context context) {
        super(context);
    }

    public MenuItem(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public MenuItem(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public MenuItem(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }
    public static int pxToDp(Context context, int px) {
        return (int) (px / context.getResources().getDisplayMetrics().density);
    }

    public static int dpToPx(Context context,int dp) {
        return (int) (dp * context.getResources().getDisplayMetrics().density);
    }
    public void onClick(){
        setBackgroundColor(Color.GRAY);
        new Timer().schedule(
                new TimerTask() {
                    @Override
                    public void run() {
                        setBackgroundColor(Color.WHITE);
                    }
                },
                50
        );
    }
}
