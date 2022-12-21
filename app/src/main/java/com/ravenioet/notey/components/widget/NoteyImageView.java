package com.ravenioet.notey.components.widget;

import android.animation.Animator;
import android.content.Context;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatImageView;

import com.ravenioet.notey.interfaces.AnimationListener;

public class NoteyImageView extends AppCompatImageView {
    private boolean pulsing;
    public NoteyImageView(@NonNull Context context) {
        super(context);
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
                        NoteyImageView.this.postDelayed(() -> pulse(periodMillis), periodMillis);
                    }
                }).start();
            }
        }).start();
    }
}
