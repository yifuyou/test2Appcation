package com.base.common.utils;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Rect;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;

import androidx.annotation.DimenRes;


public class DensityUtil {

    private static int sStatusBarHeight;

    public static int getActionBarSize(Context context) {
        TypedValue tv = new TypedValue();
        if (context.getTheme().resolveAttribute(android.R.attr.actionBarSize, tv, true)) {
            return TypedValue.complexToDimensionPixelSize(tv.data, context.getResources().getDisplayMetrics());
        }
        return DensityUtil.dp2px(50);
    }

    /**
     * StatusBar高度
     *
     * @return
     */
    public static int getStatusBarHeight() {
        if (sStatusBarHeight == 0) {
            int resourceId = UIUtils.getResources().getIdentifier("status_bar_height", "dimen", "android");
            if (resourceId > 0) {
                sStatusBarHeight = UIUtils.getResources().getDimensionPixelSize(resourceId);
            }
        }
        return sStatusBarHeight;
    }

    public static int getNavigationBarHeight() {
        boolean var1 = ViewConfiguration.get(UIUtils.getContext()).hasPermanentMenuKey();
        int var2;
        return (var2 = UIUtils.getResources().getIdentifier("navigation_bar_height", "dimen", "android")) > 0 && !var1 ? UIUtils.getResources().getDimensionPixelSize(var2) : 0;
    }


    /**
     * 获取朴素密度
     *
     * @return
     */
    public static float getDensity(Context mContext) {
        return mContext.getResources().getDisplayMetrics().density;
    }


    /**
     * 将px值转换为dip或dp值，保证尺寸大小不变
     *
     * @param pxValue scale   （DisplayMetrics类中属性density）
     * @return
     */
    public static float px2dip(Context mContext, float pxValue) {
        final float scale = mContext.getResources().getDisplayMetrics().density;
        return (pxValue / scale + 0.5f);
    }

    /**
     * 将dip或dp值转换为px值，保证尺寸大小不变
     *
     * @param dipValue scale    （DisplayMetrics类中属性density）
     * @return
     */
    public static int dp2px(float dipValue) {
        final float scale = UIUtils.getResources().getDisplayMetrics().density;
        return (int) (dipValue * scale + 0.5f);
    }

    public static int getDimens(@DimenRes int dimenRes) {
        return UIUtils.getResources().getDimensionPixelSize(dimenRes);
    }


    /**
     * 将px值转换为sp值，保证文字大小不变
     *
     * @param pxValue fontScale （DisplayMetrics类中属性scaledDensity）
     * @return
     */
    public static int px2sp(Context mContext, int pxValue) {
        final float fontScale = mContext.getResources().getDisplayMetrics().scaledDensity;
        return (int) (pxValue / fontScale + 0.5f);
    }

    /**
     * 将sp值转换为px值，保证文字大小不变
     *
     * @param spValue fontScale （DisplayMetrics类中属性scaledDensity）
     * @return
     */
    public static int sp2px(float spValue) {
        final float fontScale = UIUtils.getResources().getDisplayMetrics().scaledDensity;
        return (int) (spValue * fontScale + 0.5f);
    }


    public static int dimenPixelSize(Context mContext, @DimenRes int id) {
        return mContext.getResources().getDimensionPixelSize(id);
    }

    /**
     * 测量文字的高度
     * --经测试后发现，采用另一种带Rect的方式，获得的数据并不准确。
     * 特别是在一些对文字有一些倾斜处理的时候
     *
     * @param paint
     * @return
     */
    public static float measureTextHeight(Paint paint) {
        float height = 0f;
        if (null == paint) {
            return height;
        }
        Paint.FontMetrics fontMetrics = paint.getFontMetrics();
        height = fontMetrics.descent - fontMetrics.ascent;
        return height;
    }

    /**
     * getLocationInWindow   getLocationOnScreen得到绝对位置的方法只在有弹出窗时会有区别。
     * getLocationInWindow  相对于窗口
     *
     * @param view
     * @return
     */
    public static Rect getViewRectInWindow(View view) {
        int[] viewLocation = new int[2];
        view.getLocationInWindow(viewLocation);
        int viewX = viewLocation[0]; // x 坐标
        int viewY = viewLocation[1]; // y 坐标
        return new Rect(viewX, viewY, viewX + view.getWidth(), viewY + view.getHeight());
    }

    public static Rect getViewRectOnScreen(View view) {
        int[] viewLocation = new int[2];
        view.getLocationOnScreen(viewLocation);
        int viewX = viewLocation[0]; // x 坐标
        int viewY = viewLocation[1]; // y 坐标
        return new Rect(viewX, viewY, viewX + view.getWidth(), viewY + view.getHeight());
    }


    public static float getScreenWidth_dp(Context mContext) {
        DisplayMetrics dm = new DisplayMetrics();
        ((WindowManager) mContext.getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay().getMetrics(dm);
        return px2dip(mContext, dm.widthPixels);
    }

    public static float getScreenHeight_dp(Context mContext) {
        DisplayMetrics dm = new DisplayMetrics();
        ((WindowManager) mContext.getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay().getMetrics(dm);
        return px2dip(mContext, dm.heightPixels);
    }

    public static int getScreenWidthPixels() {
        DisplayMetrics dm = new DisplayMetrics();
        ((WindowManager) UIUtils.getContext().getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay().getMetrics(dm);
        return dm.widthPixels;
    }

    public static int getScreenHeightPixels() {
        DisplayMetrics dm = new DisplayMetrics();
        ((WindowManager) UIUtils.getContext().getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay().getMetrics(dm);
        return dm.heightPixels;
    }


    /**
     * 获取屏幕宽度和高度，单位为px
     *
     * @param context
     * @return
     */
    public static Point getScreenMetrics(Context context) {
        DisplayMetrics dm = context.getResources().getDisplayMetrics();
        int w_screen = dm.widthPixels;
        int h_screen = dm.heightPixels;
        return new Point(w_screen, h_screen);
    }

    public static boolean checkIsVisible(Context context, View view) {
        Point point = getScreenMetrics(context);
        // 如果已经加载了，判断view是否显示出来，然后曝光
        Rect rect = new Rect(0, 0, point.x, point.y);
//        int[] location = new int[2];
//        view.getLocationInWindow(location);
        if (view.getLocalVisibleRect(rect)) {
            return true;
        } else {
            //view已不在屏幕可见区域;
            return false;
        }
    }


    public static float getScreenDensity(Context context) {
        try {
            DisplayMetrics dm = new DisplayMetrics();
            ((WindowManager) context.getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay().getMetrics(dm);
            return dm.density;
        } catch (Exception e) {
            return DisplayMetrics.DENSITY_DEFAULT;
        }
    }

    public static void setViewSize(View view, int x, int y) {
        ViewGroup.LayoutParams laParams = view.getLayoutParams();
        if (laParams != null) {
            laParams.width = x;
            laParams.height = y;
        } else {
            laParams = new ViewGroup.LayoutParams(x, y);
        }
        view.setLayoutParams(laParams);
    }

    public static void setViewHight(View view, int y) {
        ViewGroup.LayoutParams laParams = view.getLayoutParams();
        if (laParams != null) {
            laParams.height = y;
        } else {
            laParams = new ViewGroup.LayoutParams(view.getMinimumWidth(), y);
        }
        view.setLayoutParams(laParams);
    }

    //在dialog.show()之后调用
    public static void setDialogWindowAttr(Dialog dlg, Context mContext, int width, int height) {
        Window window = dlg.getWindow();

        WindowManager.LayoutParams lp = window.getAttributes();
        lp.gravity = Gravity.CENTER;
        lp.width = width;//宽高可设置具体大小

        if (height == -1)
            lp.height = (int) (getScreenHeightPixels() * 0.9);
        else lp.height = height;

        dlg.getWindow().setAttributes(lp);
    }


}