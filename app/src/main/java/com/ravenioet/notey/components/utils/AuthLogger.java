package com.ravenioet.notey.components.utils;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.Resources;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

public class AuthLogger extends RelativeLayout {
    public static int RETRY_BUTTON = 4;
    public static int PROGRESS_BAR = 2;
    public static int SCROLL_VIEW = 1;
    public static int MESSAGE = 3;
    public int getPx(int value){
        Resources r = getResources();
        return Math.round(TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP, value,r.getDisplayMetrics()));
    }
    @SuppressLint("ResourceType")
    public AuthLogger(Context context) {
        super(context);
        setId(1);
        setFitsSystemWindows(true);
        LayoutParams layout_216 = new LayoutParams(LayoutParams.MATCH_PARENT,LayoutParams.MATCH_PARENT);
        /*layout_216.width = LayoutParams.MATCH_PARENT;
        layout_216.height = LayoutParams.MATCH_PARENT;*/
        setLayoutParams(layout_216);

        ProgressBar progress_bar = new ProgressBar(context);
        progress_bar.setId(PROGRESS_BAR);
        LayoutParams layout_719 = new LayoutParams(getPx(50),getPx(50));
        /*layout_719.width = (int) 50dp;
        layout_719.height = (int) 50dp;
        */
        layout_719.addRule(RelativeLayout.CENTER_HORIZONTAL);
        progress_bar.setLayoutParams(layout_719);
        addView(progress_bar);

        Button retryBtn = new Button(getContext());
        retryBtn.setId(RETRY_BUTTON);
        LayoutParams layout_710 = new LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
        layout_710.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
        retryBtn.setText("Retry");
        retryBtn.setLayoutParams(layout_710);
        addView(retryBtn);
        ScrollView scrollView_882 = new ScrollView(context);
        scrollView_882.setId(SCROLL_VIEW);
        LayoutParams layout_698 = new LayoutParams(LayoutParams.MATCH_PARENT,LayoutParams.MATCH_PARENT);
        /*layout_698.width = LayoutParams.MATCH_PARENT;
        layout_698.height = LayoutParams.MATCH_PARENT;
        */
        layout_698.addRule(RelativeLayout.BELOW,progress_bar.getId());
        layout_698.addRule(RelativeLayout.ABOVE,retryBtn.getId());
        scrollView_882.setLayoutParams(layout_698);

        TextView message = new TextView(context);
        message.setId(MESSAGE);
        message.setPaddingRelative((int) (10/context.getResources().getDisplayMetrics().density), 0, 0, 0);
        message.setText("init");
        message.setTextSize(getPx(8));
        LayoutParams layout_138 = new LayoutParams(LayoutParams.WRAP_CONTENT,LayoutParams.WRAP_CONTENT);
        /*layout_138.width = LayoutParams.WRAP_CONTENT;
        layout_138.height = LayoutParams.WRAP_CONTENT;*/
        message.setLayoutParams(layout_138);
        scrollView_882.addView(message);
        addView(scrollView_882);
    }

    public AuthLogger(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public AuthLogger(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public AuthLogger(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }
    public static int pxToDp(Context context, int px) {
        return (int) (px / context.getResources().getDisplayMetrics().density);
    }

    public static int dpToPx(Context context,int dp) {
        return (int) (dp * context.getResources().getDisplayMetrics().density);
    }
}
