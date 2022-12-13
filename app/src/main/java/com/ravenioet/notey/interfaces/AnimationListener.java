package com.ravenioet.notey.interfaces;

import android.animation.Animator;

public abstract class AnimationListener implements Animator.AnimatorListener {
  @Override
  public final void onAnimationStart(Animator animation) {}

  @Override
  public abstract void onAnimationEnd(Animator animation);

  @Override
  public final void onAnimationCancel(Animator animation) {}
  @Override
  public final void onAnimationRepeat(Animator animation) {}
}