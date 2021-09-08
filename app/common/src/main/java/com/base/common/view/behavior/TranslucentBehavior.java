package com.base.common.view.behavior;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.coordinatorlayout.widget.CoordinatorLayout;

import com.base.common.utils.LogUtil;
import com.google.android.material.appbar.AppBarLayout;


public class TranslucentBehavior<V extends View> extends CoordinatorLayout.Behavior<V> {


    private int sumHeight = 0;//滑动的总高度

    /**
     * 寻找被观察View
     *
     * @param context
     * @param attrs
     */
    public TranslucentBehavior(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public boolean layoutDependsOn(@NonNull CoordinatorLayout parent, @NonNull V child, @NonNull View dependency) {
        return dependency instanceof AppBarLayout;
    }

    /**
     * 必须要加上  layout_anchor，对方也要layout_collapseMode才能使用
     */
    @Override
    public boolean onDependentViewChanged(@NonNull CoordinatorLayout parent, @NonNull V child, @NonNull View dependency) {

        LogUtil.d("dependency", String.valueOf(dependency.getY()));
        // 初始化高度
        if (sumHeight == 0) {
            int mToolbarHeight = child.getBottom();
            sumHeight = dependency.getHeight() - mToolbarHeight;
        }
        //
        //计算toolbar从开始移动到最后的百分比
        float percent = dependency.getY() / sumHeight;

        //百分大于1，直接赋值为1
        if (percent >= 1) {
            percent = 1f;
        }

        //设置背景颜色
        child.setAlpha(percent);

        return true;
    }

}
