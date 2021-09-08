package com.base.common.utils;

import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Shader;
import android.graphics.drawable.GradientDrawable;
import android.widget.TextView;

import androidx.annotation.ColorInt;


/**
 * * 自定义背景
 * * GradientDrawable background = new GradientDrawable();
 * * background.setShape(GradientDrawable.OVAL);
 * * background.setStroke(10,Color.RED);//设置宽度为10px的红色描边
 * * background.setGradientType(GradientDrawable.LINEAR_GRADIENT);//设置线性渐变，除此之外还有：GradientDrawable.SWEEP_GRADIENT（扫描式渐变），GradientDrawable.RADIAL_GRADIENT（圆形渐变）
 * * background.setColors(new int[]{Color.RED,Color.BLUE});//增加渐变效果需要使用setColors方法来设置颜色（中间可以增加多个颜色值）
 * * view.setBackgroundDrawable(background);
 * *
 * * Selector选择器代码中动态设置
 * *
 * * //背景1 背景红色 边框宽度为1，黑色
 * *  GradientDrawable drawable=new GradientDrawable();
 * *  drawable.setColor(Color.rgb(255,0,0));
 * *  drawable.setStroke(1,Color.rgb(0,0,0));
 * *  drawable.setCornerRadius(20);
 * *  drawable.setShape(GradientDrawable.RECTANGLE);
 * *
 * * //背景2  背景蓝色  边框宽度为1，黑色
 * * GradientDrawable drawable2=new GradientDrawable();
 * * drawable2.setColor(Color.rgb(0,0,0));
 * * drawable2.setStroke(1,Color.rgb(0,0,0));
 * * drawable2.setCornerRadius(20);
 * * drawable2.setShape(GradientDrawable.RECTANGLE);
 * *
 * * //动态生成Selector
 * * int pressed = android.R.attr.state_pressed; //取负值就表示pressed为false的意思
 * * StateListDrawable drawable3= new StateListDrawable();
 * * drawable3.addState(new int[]{pressed},drawable);//  状态  , 设置按下的图片
 * * drawable3.addState(new int[]{-pressed}, drawable2);//默认状态,默认状态下的图片
 */
public class DrawableShapeUtils {
    /**
     * 文字渐变色
     *
     * @param textView
     */
    public static void setTextViewStyles(TextView textView, @ColorInt int color0, @ColorInt int color1) {
        LinearGradient mLinearGradient = new LinearGradient(0, 0, 0, textView.getPaint().getTextSize() * textView.getText().length(), color0, color1, Shader.TileMode.CLAMP);
        textView.getPaint().setShader(mLinearGradient);
        textView.invalidate();
    }


    //<item android:width="375dp" android:height="667dp">
//  <shape android:shape="rectangle">
//    <gradient android:type="radial"
//    android:useLevel="true"
//    android:startColor="#ffffb773"
//    android:centerColor="#ff1f5c5e"
//    android:endColor="#ff011727"
//    android:centerX="0"
//    android:centerY="0"
//    android:gradientRadius="222dp" />
//  </shape>
//</item>

    /**
     * @param cornerRadius_TL 左上角的圆角
     * @param cornerRadius_TR 右上角
     * @param cornerRadius_BL 左下角
     * @param cornerRadius_BR 右下角
     * @param colors          渐变的颜色
     * @return
     */
    public static GradientDrawable getGradientDrawableLL(int cornerRadius_TL, int cornerRadius_TR, int cornerRadius_BL, int cornerRadius_BR, @ColorInt int... colors) {
        float[] radii = new float[8];
        radii[0] = cornerRadius_TL;
        radii[1] = cornerRadius_TL;
        radii[2] = cornerRadius_TR;
        radii[3] = cornerRadius_TR;
        radii[4] = cornerRadius_BR;
        radii[5] = cornerRadius_BR;
        radii[6] = cornerRadius_BL;
        radii[7] = cornerRadius_BL;

        GradientDrawable drawable = new GradientDrawable();
        drawable.setColors(colors);
        drawable.setCornerRadii(radii);
        drawable.setOrientation(GradientDrawable.Orientation.LEFT_RIGHT);
        drawable.setGradientType(GradientDrawable.LINEAR_GRADIENT);
        return drawable;
    }

    /**
     * 从左到右的线性渐变，圆角距形
     * LL2RRR
     * Linear  Left 2 Right  RoundRect
     *
     * @param radius 圆角的半径
     * @return
     */
    public static GradientDrawable getGradientDrawableLL2RRR(float[] radius, @ColorInt int... colors) {
        GradientDrawable drawable = new GradientDrawable();
        drawable.setColors(colors);
        drawable.setCornerRadii(radius);
        drawable.setOrientation(GradientDrawable.Orientation.LEFT_RIGHT);
        drawable.setGradientType(GradientDrawable.LINEAR_GRADIENT);
        return drawable;
    }

    public static float[] getCornerRadii(float leftTop, float rightTop, float
            leftBottom, float rightBottom) {
        //这里返回的一个浮点型的数组，一定要有8个元素，不然会报错
        return new float[]{leftTop, leftTop, rightTop, rightTop, leftBottom, leftBottom, rightBottom, rightBottom};
    }

    /**
     * 从左到右的线性渐变，圆角距形
     * LL2RRR
     * Linear  Left 2 Right  RoundRect
     *
     * @param radius 圆角的半径
     * @return
     */
    public static GradientDrawable getGradientDrawableLL2RRR(int radius, @ColorInt int... colors) {
        GradientDrawable drawable = new GradientDrawable();
        drawable.setColors(colors);
        drawable.setCornerRadius(radius);
        drawable.setOrientation(GradientDrawable.Orientation.LEFT_RIGHT);
        drawable.setGradientType(GradientDrawable.LINEAR_GRADIENT);
        return drawable;
    }

    /**
     * 从右到左的线性渐变，圆角距形
     *
     * @param radius 圆角的半径
     * @return
     */
    public static GradientDrawable getGradientDrawableLR2LRR(int radius, @ColorInt int... colors) {

        GradientDrawable drawable = new GradientDrawable();
        drawable.setColors(colors);
        drawable.setCornerRadius(radius);
        drawable.setOrientation(GradientDrawable.Orientation.RIGHT_LEFT);
        drawable.setGradientType(GradientDrawable.LINEAR_GRADIENT);
        return drawable;
    }

    /**
     * 从上到下的线性渐变，圆角距形
     *
     * @param radius 圆角的半径
     * @return
     */
    public static GradientDrawable getGradientDrawableLT2BRR(int radius, @ColorInt int... colors) {
        GradientDrawable drawable = new GradientDrawable();
        drawable.setColors(colors);
        drawable.setCornerRadius(radius);
        drawable.setOrientation(GradientDrawable.Orientation.TOP_BOTTOM);
        drawable.setGradientType(GradientDrawable.LINEAR_GRADIENT);
        return drawable;
    }

    /**
     * 从下到上的线性渐变，圆角距形
     *
     * @param radius 圆角的半径
     *               LBTRR  L线性渐变，B下bottom   T上top    RR roundRect  圆角
     * @return
     */
    public static GradientDrawable getGradientDrawableLB2TRR(int radius, @ColorInt int... colors) {
        GradientDrawable drawable = new GradientDrawable();
        drawable.setColors(colors);
        drawable.setCornerRadius(radius);
        drawable.setOrientation(GradientDrawable.Orientation.BOTTOM_TOP);
        drawable.setGradientType(GradientDrawable.LINEAR_GRADIENT);
        return drawable;
    }


    /**
     * 从下到上的线性渐变，
     *
     * @param orientation 方向  0从上到下，1下到上， 2从左向右  3  从右向左
     * @return
     */
    public static GradientDrawable getGradientDrawableL(int orientation, @ColorInt int... colors) {
        GradientDrawable drawable = new GradientDrawable();
        drawable.setColors(colors);
        switch (orientation) {
            case 0:
                drawable.setOrientation(GradientDrawable.Orientation.TOP_BOTTOM);
                break;
            case 1:
                drawable.setOrientation(GradientDrawable.Orientation.BOTTOM_TOP);
                break;
            case 2:
                drawable.setOrientation(GradientDrawable.Orientation.LEFT_RIGHT);
                break;
            case 3:
                drawable.setOrientation(GradientDrawable.Orientation.RIGHT_LEFT);
                break;
        }

        drawable.setGradientType(GradientDrawable.LINEAR_GRADIENT);
        return drawable;
    }


}
