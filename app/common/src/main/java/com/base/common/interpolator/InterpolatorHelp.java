package com.base.common.interpolator;

/**
 * 动画插值器
 */
public interface InterpolatorHelp {
    /**
     * android:interpolator="@android:anim/accelerate_interpolator" 设置动画为加速动画(动画播放中越来越快)
     *
     * android:interpolator="@android:anim/decelerate_interpolator" 设置动画为减速动画(动画播放中越来越慢)
     *
     * android:interpolator="@android:anim/accelerate_decelerate_interpolator" 设置动画为先加速在减速(开始速度最快 逐渐减慢)
     *
     * android:interpolator="@android:anim/anticipate_interpolator" 先反向执行一段，然后再加速反向回来（相当于我们弹簧，先反向压缩一小段，然后在加速弹出）
     *
     * android:interpolator="@android:anim/anticipate_overshoot_interpolator" 同上先反向一段，然后加速反向回来，执行完毕自带回弹效果（更形象的弹簧效果）
     *
     * android:interpolator="@android:anim/bounce_interpolator" 执行完毕之后会回弹跳跃几段（相当于我们高空掉下一颗皮球，到地面是会跳动几下）
     *
     * android:interpolator="@android:anim/cycle_interpolator" 循环，动画循环一定次数，值的改变为一正弦函数：Math.sin(2* mCycles* Math.PI* input)
     *
     * android:interpolator="@android:anim/linear_interpolator" 线性均匀改变
     *
     * android:interpolator="@android:anim/overshoot_interpolator" 加速执行，结束之后回弹
     */

    /**
     * animation.setInterpolator(new AccelerateInterpolator());
     *
     * animation.setInterpolator(new DecelerateInterpolator());
     *
     * animation.setInterpolator(new AccelerateDecelerateInterpolator());
     *
     * animation.setInterpolator(new AnticipateInterpolator());
     *
     * animation.setInterpolator(new AnticipateOvershootInterpolator());
     *
     * animation.setInterpolator(new BounceInterpolator());
     *
     * animation.setInterpolator(new CycleInterpolator(2));
     *
     * animation.setInterpolator(new LinearInterpolator());
     *
     * animation.setInterpolator(new OvershootInterpolator());
     */

}
