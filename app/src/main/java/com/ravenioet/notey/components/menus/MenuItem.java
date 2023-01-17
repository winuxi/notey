package com.ravenioet.notey.components.menus;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.Resources;
import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.ravenioet.notey.components.drawable.RoundCorner;
import com.ravenioet.notey.components.provider.ThemeProvider;
import com.ravenioet.notey.components.widget.NoteyTextView;

import java.util.Timer;
import java.util.TimerTask;

@SuppressLint("ResourceType")
public class MenuItem extends RelativeLayout {
    public static int TITLE = 3;
    public static int INFO = 4;
    private NoteyTextView titleView;
    private NoteyTextView hintView;
    private AppCompatActivity rootActivity;
    LayoutParams layout_216;
    public int getPx(int value){
        Resources r = getResources();
        return Math.round(TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP, value,r.getDisplayMetrics()));
    }

    public MenuItem(AppCompatActivity activity){
        super(activity);
        rootActivity = activity;
        initViews(activity);
    }
    private void initViews(Context context){
        Log.d("menu-item","init");
        layout_216 = new LayoutParams(LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        layout_216.setMargins(5,5,5,5);
        setPadding(5,5,5,5);
        setLayoutParams(layout_216);
        RoundCorner roundCorner = new RoundCorner(ThemeProvider.getMainTheme());
        setBackground(roundCorner.apply());
        titleView = new NoteyTextView(context);
        titleView.setId(TITLE);
        titleView.setPadding(5,5,5,5);
        titleView.setTextSize(getPx(8));
        LayoutParams titleParams = new LayoutParams(LayoutParams.WRAP_CONTENT,LayoutParams.WRAP_CONTENT);
        //titleParams.addRule(RelativeLayout.ALIGN_PARENT_TOP);
        titleView.setLayoutParams(titleParams);
        titleView.setVisibility(GONE);
        addView(titleView);

        hintView = new NoteyTextView(context);
        hintView.setId(INFO);
        hintView.setPadding(5,1,5,5);
        //hintView.setTextSize(getPx(8));

        LayoutParams hintParams = new LayoutParams(LayoutParams.WRAP_CONTENT,LayoutParams.WRAP_CONTENT);
        hintParams.addRule(RelativeLayout.BELOW,titleView.getId());
        hintView.setLayoutParams(hintParams);
        hintView.setVisibility(GONE);
        addView(hintView);
    }
    public void setTitle(String title){
        if(titleView!= null){
            titleView.setText(title);
            titleView.setVisibility(VISIBLE);
        }
    }
    public void setHint(String title){
        if(hintView != null){
            hintView.setText(title);
            hintView.setVisibility(VISIBLE);
        }
    }
    public void below(int id){
        layout_216.addRule(RelativeLayout.BELOW,id);
    }

    public static int pxToDp(Context context, int px) {
        return (int) (px / context.getResources().getDisplayMetrics().density);
    }

    public static int dpToPx(Context context,int dp) {
        return (int) (dp * context.getResources().getDisplayMetrics().density);
    }
    public void onClick(String data){
        //setBackground(ThemeProvider.getTheme());
        new Timer().schedule(
                new TimerTask() {
                    @Override
                    public void run() {
                        rootActivity.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                hintView.setVisibility(VISIBLE);
                                hintView.animateText(data);
                            }
                        });
                    }
                },
                50
        );
    }

    public MenuItem(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initViews(context);
    }

    public MenuItem(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initViews(context);
    }

}
