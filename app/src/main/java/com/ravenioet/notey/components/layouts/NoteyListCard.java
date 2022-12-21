package com.ravenioet.notey.components.layouts;

import android.animation.Animator;
import android.annotation.SuppressLint;
import android.content.Context;
import android.text.TextUtils;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;

import com.ravenioet.notey.components.provider.ThemeProvider;
import com.ravenioet.notey.components.widget.NoteyImageView;
import com.ravenioet.notey.components.widget.NoteyTextView;
import com.ravenioet.notey.interfaces.AnimationListener;

@SuppressLint("ResourceType")
public class NoteyListCard extends CardView {
    private boolean pulsing;
    private NoteyImageView noteyImage;
    private NoteyTextView titleView;
    RelativeLayout.LayoutParams imageParams;
    RelativeLayout.LayoutParams titleParams;
    RelativeLayout.LayoutParams bodyParams;
    private NoteyTextView bodyView;
    RelativeLayout.LayoutParams layout_216;
    RelativeLayout.LayoutParams holder;
    public RelativeLayout.LayoutParams getParams(int width, int height){
        if(width == 1 && height == 1){
            return new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        }else if(width == 1 && height == 0){
            return new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        }else if(width == 0 && height == 0){
            return new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.MATCH_PARENT);
        }else {
            return new RelativeLayout.LayoutParams(width, height);
        }
    }
    public NoteyListCard(@NonNull Context context) {
        super(context);
        layout_216 = getParams(1,0);
        layout_216.setMargins(5,5,5,5);
        setPadding(5,5,5,5);
        setLayoutParams(layout_216);
        setCardBackgroundColor(ThemeProvider.getMainTheme().getPrimaryLight());
        setRadius(15);
        setId(1);
        addView(rootView(context));
    }

    public void below(int id){
        layout_216.addRule(RelativeLayout.BELOW,id);
    }
    NoteyImageView image(Context context){
        noteyImage = new NoteyImageView(context);
        imageParams = getParams(100,100);
        imageParams.setMargins(5,5,5,5);
        noteyImage.setId(2);
        noteyImage.setLayoutParams(imageParams);
        return noteyImage;
    }
    NoteyTextView title(Context context){
        titleView = new NoteyTextView(context);
        titleView.setId(3);
        titleParams = getParams(0,0);
        if(noteyImage != null){
            titleParams.addRule(RelativeLayout.END_OF,noteyImage.getId());
        }
        titleView.setMaxLines(1);
        titleView.setEllipsize(TextUtils.TruncateAt.END);
        titleParams.setMargins(3,15,3,3);
        titleView.setLayoutParams(titleParams);
        return titleView;
    }
    NoteyTextView body(Context context){
        bodyView = new NoteyTextView(context);
        titleView.setId(4);
        bodyParams = getParams(0,0);
        bodyParams.setMargins(3,0,3,3);
        if(titleView != null){
            bodyParams.addRule(RelativeLayout.BELOW,titleView.getId());
            bodyParams.addRule(RelativeLayout.END_OF,noteyImage.getId());
        }
        bodyView.setMaxLines(2);
        bodyView.setEllipsize(TextUtils.TruncateAt.END);
        bodyView.setLayoutParams(bodyParams);
        return bodyView;
    }
    public void setImage(int id){
        noteyImage.setImageResource(id);
    }
    public void setTitle(String title){
        titleView.setText(title);
    }

    public void setBody(String title){
        bodyView.setText(title);
    }

    NoteyRelativeLayout rootView(Context context){
        NoteyRelativeLayout root = new NoteyRelativeLayout(context);
        holder = getParams(1,0);
        root.setLayoutParams(holder);
        root.setId(8);
        root.addView(image(context));
        root.addView(title(context));
        root.addView(body(context));
        return root;
    }
    public void startPulse(long periodMillis) {
        if (!pulsing) {
            pulsing = true;
            pulse(periodMillis);
        }
    }

    public void stopPulse() {
        pulsing = false;
    }

    private void pulse(long periodMillis) {
        if (!pulsing) return;
        this.animate().scaleX(1.2f).scaleY(1.2f).setDuration(150).setListener(new AnimationListener() {
            @Override
            public void onAnimationEnd(Animator animation) {
                clearAnimation();
                animate().scaleX(1.0f).scaleY(1.0f).setDuration(150).setListener(new AnimationListener() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        NoteyListCard.this.postDelayed(() -> pulse(periodMillis), periodMillis);
                    }
                }).start();
            }
        }).start();
    }
}
