package com.ravenioet.notey.components.widget;

import android.animation.Animator;
import android.content.Context;
import android.os.Handler;
import android.util.AttributeSet;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatTextView;

import com.ravenioet.notey.interfaces.AnimationListener;

public class NoteyTextView extends AppCompatTextView {
    private boolean pulsing;

    public NoteyTextView(@NonNull Context context) {
        super(context);
    }

    public NoteyTextView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public NoteyTextView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    private Handler mHandler = new Handler();
    private CharSequence mText;
    private int mIndex;
    private long mDelay = 50;
    private Runnable characterAdder = new Runnable() {
        @Override
        public void run() {
            setText(mText.subSequence(0, mIndex++));
            if(mIndex <= mText.length()) {
                mHandler.postDelayed(characterAdder, mDelay);
            }
        }
    };

    public void animateText(CharSequence text) {
        mText = text;
        mIndex = 0;

        setText("");
        mHandler.removeCallbacks(characterAdder);
        mHandler.postDelayed(characterAdder, mDelay);
    }

    public void setCharacterDelay(long millis) {
        mDelay = millis;
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
                        NoteyTextView.this.postDelayed(() -> pulse(periodMillis), periodMillis);
                    }
                }).start();
            }
        }).start();
    }
}
