package com.base.common.utils;

import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Color;

import androidx.annotation.ColorInt;
import androidx.annotation.ColorRes;
import androidx.annotation.IntRange;
import androidx.core.content.ContextCompat;

/**
 * Generate thumb and background color state list use tintColor
 * 颜色透明度16进制对照表
 * 100% — FF
 * 99% — FC
 * 98% — FA
 * 97% — F7
 * 96% — F5
 * 95% — F2
 * 94% — F0
 * 93% — ED
 * 92% — EB
 * 91% — E8
 * 90% — E6
 * 89% — E3
 * 88% — E0
 * 87% — DE
 * 86% — DB
 * 85% — D9
 * 84% — D6
 * 83% — D4
 * 82% — D1
 * 81% — CF
 * 80% — CC
 * 79% — C9
 * 78% — C7
 * 77% — C4
 * 76% — C2
 * 75% — BF
 * 74% — BD
 * 73% — BA
 * 72% — B8
 * 71% — B5
 * 70% — B3
 * 69% — B0
 * 68% — AD
 * 67% — AB
 * 66% — A8
 * 65% — A6
 * 64% — A3
 * 63% — A1
 * 62% — 9E
 * 61% — 9C
 * 60% — 99
 * 59% — 96
 * 58% — 94
 * 57% — 91
 * 56% — 8F
 * 55% — 8C
 * 54% — 8A
 * 53% — 87
 * 52% — 85
 * 51% — 82
 * 50% — 80
 * 49% — 7D
 * 48% — 7A
 * 47% — 78
 * 46% — 75
 * 45% — 73
 * 44% — 70
 * 43% — 6E
 * 42% — 6B
 * 41% — 69
 * 40% — 66
 * 39% — 63
 * 38% — 61
 * 37% — 5E
 * 36% — 5C
 * 35% — 59
 * 34% — 57
 * 33% — 54
 * 32% — 52
 * 31% — 4F
 * 30% — 4D
 * 29% — 4A
 * 28% — 47
 * 27% — 45
 * 26% — 42
 * 25% — 40
 * 24% — 3D
 * 23% — 3B
 * 22% — 38
 * 21% — 36
 * 20% — 33
 * 19% — 30
 * 18% — 2E
 * 17% — 2B
 * 16% — 29
 * 15% — 26
 * 14% — 24
 * 13% — 21
 * 12% — 1F
 * 11% — 1C
 * 10% — 1A
 * 9% — 17
 * 8% — 14
 * 7% — 12
 * 6% — 0F
 * 5% — 0D
 * 4% — 0A
 * 3% — 08
 * 2% — 05
 * 1% — 03
 * 0% — 00
 */
public class ColorUtils {


    /**
     * 获取颜色
     */
    public static int getColor(@ColorRes int resId) {
        return ContextCompat.getColor(UIUtils.getContext(), resId);
    }

    public static String getColorString(@ColorRes int resId) {
        int color = getColor(resId);
        return getColorStringInt(color);
    }

    public static String getColorStringInt(@ColorInt int color) {
//        int red = (color >> 16) & 0xff;
//        int green = (color >> 8) & 0xff;
//        int blue = color & 0xff;
//        String str = Integer.toHexString(red) + Integer.toHexString(green) + Integer.toHexString(blue);
//        return "#" + str;
        return String.format("#%06X", (0xFFFFFF & color));
    }


    public static int getColor(
            @IntRange(from = 0, to = 255) int alpha,
            @IntRange(from = 0, to = 255) int red,
            @IntRange(from = 0, to = 255) int green,
            @IntRange(from = 0, to = 255) int blue) {

        return Color.argb(alpha, red, green, blue);
    }


    /**
     * 获取颜色选择
     */
    public static ColorStateList getColorStateList(@ColorRes int resId) {
        return ContextCompat.getColorStateList(UIUtils.getContext(), resId);
    }

    /**
     * 获取颜色透明度
     */
    public static int getAlphaRes(@ColorRes int resId) {
        return getAlphaInt(getColor(resId));
    }

    /**
     * 获取颜色透明度
     */
    public static int getAlphaInt(@ColorInt int colorInt) {
        return Color.alpha(colorInt);
    }

    /**
     * 设置颜色透明度
     */
    @ColorInt
    public static int setColorAlpha(@ColorInt int colorInt, int alpha) {
        if (alpha < 0) alpha = 0;
        if (alpha > 255) alpha = 255;
        return Color.argb(alpha, Color.red(colorInt), Color.green(colorInt), Color.blue(colorInt));
    }

    @ColorInt
    public static int setColorAlpha(@ColorInt int colorInt, float percentage) {
        if (percentage < 0) percentage = 0;
        if (percentage > 1) percentage = 1;
        return Color.argb((int) (255 * percentage), Color.red(colorInt), Color.green(colorInt), Color.blue(colorInt));
    }

    /**
     * 添加或减少颜色透明度
     *
     * @param colorInt
     * @param alphaChanges 透明度添加减少多少0-255
     * @return
     */
    public static int changeAlpha(@ColorInt int colorInt, int alphaChanges) {
        return setColorAlpha(colorInt, Color.alpha(colorInt) + alphaChanges);
    }

    /**
     * 添加或减少颜色透明度
     *
     * @param alphaChanges 透明度添加减少多少0-255
     */
    public static int changeAlphaRes(@ColorRes int colorInt, int alphaChanges) {
        return setColorAlpha(getColor(colorInt), Color.alpha(colorInt) + alphaChanges);
    }

    /**
     * 当前颜色透明度 的百分比  比如，蓝色30%透明度
     */
    public static int percentageAlpha(@ColorInt int colorInt, float percentage) {
        return setColorAlpha(colorInt, (int) (Color.alpha(colorInt) * percentage));
    }

    /**
     * 当前颜色透明度 的百分比  比如，蓝色30%透明度
     */
    public static int getPercentageAlphaRes(@ColorRes int colorRes, float percentage) {
        return setColorAlpha(getColor(colorRes), (int) (Color.alpha(getColor(colorRes)) * percentage));
    }


    /**
     * 合成新的颜色值
     *
     * @param fraction   颜色取值的级别 (0.0f ~ 1.0f)
     * @param startValue 开始显示的颜色
     * @param endValue   结束显示的颜色
     * @return 返回生成新的颜色值
     */
    public static int getNewColorByStartEndColor(Context context, float fraction, @ColorRes int startValue, @ColorRes int endValue) {
        return evaluate(fraction, context.getResources().getColor(startValue), context.getResources().getColor(endValue));
    }

    /**
     * 合成新的颜色值
     *
     * @param fraction   颜色取值的级别 (0.0f ~ 1.0f)
     * @param startValue 开始显示的颜色
     * @param endValue   结束显示的颜色
     * @return 返回生成新的颜色值
     */
    public static int evaluate(float fraction, @ColorInt int startValue, @ColorInt int endValue) {
        int startA = (startValue >> 24) & 0xff;
        int startR = (startValue >> 16) & 0xff;
        int startG = (startValue >> 8) & 0xff;
        int startB = startValue & 0xff;

        int endA = (endValue >> 24) & 0xff;
        int endR = (endValue >> 16) & 0xff;
        int endG = (endValue >> 8) & 0xff;
        int endB = endValue & 0xff;

        return ((startA + (int) (fraction * (endA - startA))) << 24) | ((startR + (int) (fraction * (endR - startR))) << 16) | ((startG + (int) (fraction * (endG - startG))) << 8) | ((startB + (int) (fraction * (endB - startB))));
    }

    /**
     * 计算两个颜色是否相近
     *
     * @param colorInt1
     * @param colorInt2
     * @return
     */
    public static boolean isColorNear(@ColorInt int colorInt1, @ColorInt int colorInt2) {
        int thresholdSum = 15;//总共的阀值
        int threshold = 8; //单个的阀值

        int aR = (colorInt1 >> 16) & 0xff;
        int aG = (colorInt1 >> 8) & 0xff;
        int aB = colorInt1 & 0xff;

        int bR = (colorInt2 >> 16) & 0xff;
        int bG = (colorInt2 >> 8) & 0xff;
        int bB = colorInt2 & 0xff;

        double r = aR - bR;
        double g = aG - bG;
        double b = aB - bB;
        if (Math.abs(r) > threshold || Math.abs(g) > threshold || Math.abs(b) > threshold) {
            return false;
        }
        double sum = Math.sqrt(r * r + g * g + b * b);
        return sum < thresholdSum;
    }


}
