package com.ravenioet.notey.components;

import android.animation.Animator;
import android.content.Context;
import android.util.AttributeSet;
import android.view.animation.Animation;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatButton;

import com.ravenioet.notey.interfaces.AnimationListener;

public class NoteyButton extends AppCompatButton {
    private boolean pulsing;
    public NoteyButton(@NonNull Context context) {
        super(context);
    }

    public NoteyButton(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public NoteyButton(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
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
                        NoteyButton.this.postDelayed(() -> pulse(periodMillis), periodMillis);
                    }
                }).start();
            }
        }).start();
    }
}
