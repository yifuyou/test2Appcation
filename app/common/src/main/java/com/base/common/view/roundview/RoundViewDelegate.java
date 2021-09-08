package com.base.common.view.roundview;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.TypedArray;
import android.graphics.BitmapShader;
import android.graphics.Color;
import android.graphics.ComposeShader;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PorterDuff;
import android.graphics.Rect;
import android.graphics.Shader;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.RippleDrawable;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.StateListDrawable;
import android.graphics.drawable.shapes.PathShape;
import android.os.Build;
import android.util.AttributeSet;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.ColorInt;
import androidx.annotation.Px;

import com.base.common.R;
import com.base.common.utils.DrawableShapeUtils;

import java.util.ArrayList;
import java.util.List;

public class RoundViewDelegate {
    private View view;
    private Context context;
    private GradientDrawable gd_background = new GradientDrawable();
    private GradientDrawable gd_background_press = new GradientDrawable();
    private int backgroundColor;
    private int backgroundPressColor;
    private int cornerRadius;

    private int cornerRadius_TL;
    private int cornerRadius_TR;
    private int cornerRadius_BL;
    private int cornerRadius_BR;

    private int strokeWidth;
    private int strokeColor;
    private int strokePressColor;
    private int textPressColor;
    private boolean isRadiusHalfHeight;
    private boolean isWidthHeightEqual;
    private boolean isRippleEnable;
    private float[] radiusArr = new float[8];

    private int height, width;

    private int offsetX;//x轴偏移的距离 >0 顶部往右偏移  <0 底部往右偏移

    //渐变色
    private int startColor;
    private int endColor;
    private int color1;
    private int color2;
    private int color3;
    private int color4;

    private int gradientOrientation;//渐变方向  0 左右 1 上下  2左上到右下
    private int[] colors;

    public RoundViewDelegate(View view, Context context, AttributeSet attrs) {
        this.view = view;
        this.context = context;
        obtainAttributes(context, attrs);
    }

    private void obtainAttributes(Context context, AttributeSet attrs) {
        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.RoundTextView);
        backgroundColor = ta.getColor(R.styleable.RoundTextView_rv_backgroundColor, Color.TRANSPARENT);
        backgroundPressColor = ta.getColor(R.styleable.RoundTextView_rv_backgroundPressColor, Integer.MAX_VALUE);
        cornerRadius = ta.getDimensionPixelSize(R.styleable.RoundTextView_rv_cornerRadius, 0);
        strokeWidth = ta.getDimensionPixelSize(R.styleable.RoundTextView_rv_strokeWidth, 0);

        strokeColor = ta.getColor(R.styleable.RoundTextView_rv_strokeColor, Color.TRANSPARENT);
        strokePressColor = ta.getColor(R.styleable.RoundTextView_rv_strokePressColor, Integer.MAX_VALUE);
        textPressColor = ta.getColor(R.styleable.RoundTextView_rv_textPressColor, Integer.MAX_VALUE);
        isRadiusHalfHeight = ta.getBoolean(R.styleable.RoundTextView_rv_isRadiusHalfHeight, false);
        isWidthHeightEqual = ta.getBoolean(R.styleable.RoundTextView_rv_isWidthHeightEqual, false);

        cornerRadius_TL = ta.getDimensionPixelSize(R.styleable.RoundTextView_rv_cornerRadius_TL, 0);
        cornerRadius_TR = ta.getDimensionPixelSize(R.styleable.RoundTextView_rv_cornerRadius_TR, 0);
        cornerRadius_BL = ta.getDimensionPixelSize(R.styleable.RoundTextView_rv_cornerRadius_BL, 0);
        cornerRadius_BR = ta.getDimensionPixelSize(R.styleable.RoundTextView_rv_cornerRadius_BR, 0);


        isRippleEnable = ta.getBoolean(R.styleable.RoundTextView_rv_isRippleEnable, true);
        offsetX = ta.getDimensionPixelSize(R.styleable.RoundTextView_rv_offsetX, 0);

        startColor = ta.getColor(R.styleable.RoundTextView_rv_background_startColor, 0);
        endColor = ta.getColor(R.styleable.RoundTextView_rv_background_endColor, 0);

        color1 = ta.getColor(R.styleable.RoundTextView_rv_background_Color1, 0);
        color2 = ta.getColor(R.styleable.RoundTextView_rv_background_Color2, 0);
        color3 = ta.getColor(R.styleable.RoundTextView_rv_background_Color3, 0);
        color4 = ta.getColor(R.styleable.RoundTextView_rv_background_Color4, 0);

        gradientOrientation = ta.getInt(R.styleable.RoundTextView_rv_gradientOrientation, 0);
        ta.recycle();
    }

    public void setGradientOrientation(int gradientOrientation) {
        this.gradientOrientation = gradientOrientation;
        setBgSelector();
    }

    public void setStartColor(@ColorInt int startColor) {
        this.startColor = startColor;
        setBgSelector();
    }

    public void setEndColor(@ColorInt int endColor) {
        this.endColor = endColor;
        setBgSelector();
    }

    public void setBackgroundColor(@ColorInt int backgroundColor) {
        this.backgroundColor = backgroundColor;
        setBgSelector();
    }

    public void setBackgroundPressColor(int backgroundPressColor) {
        this.backgroundPressColor = backgroundPressColor;
        setBgSelector();
    }


    public void setCornerRadius(@Px int cornerRadius) {
        this.cornerRadius = cornerRadius;
        setBgSelector();
    }

    public void setStrokeWidthPx(@Px int strokeWidth) {
        this.strokeWidth = strokeWidth;
        setBgSelector();
    }

    public void setStrokeWidth(float strokeWidth) {
        this.strokeWidth = dp2px(strokeWidth);
        setBgSelector();
    }


    public void setStrokeColor(int strokeColor) {
        this.strokeColor = strokeColor;
        setBgSelector();
    }

    public void setStrokePressColor(int strokePressColor) {
        this.strokePressColor = strokePressColor;
        setBgSelector();
    }

    public void setTextPressColor(int textPressColor) {
        this.textPressColor = textPressColor;
        setBgSelector();
    }

    public void setIsRadiusHalfHeight(boolean isRadiusHalfHeight) {
        this.isRadiusHalfHeight = isRadiusHalfHeight;
        setBgSelector();
    }

    public void setIsWidthHeightEqual(boolean isWidthHeightEqual) {
        this.isWidthHeightEqual = isWidthHeightEqual;
        setBgSelector();
    }

    public void setCornerRadius_TL(@Px int cornerRadius_TL) {
        this.cornerRadius_TL = cornerRadius_TL;
        setBgSelector();
    }

    public void setCornerRadius_TR(@Px int cornerRadius_TR) {
        this.cornerRadius_TR = cornerRadius_TR;
        setBgSelector();
    }

    public void setCornerRadius_BL(@Px int cornerRadius_BL) {
        this.cornerRadius_BL = cornerRadius_BL;
        setBgSelector();
    }

    public void setCornerRadius_BR(@Px int cornerRadius_BR) {
        this.cornerRadius_BR = cornerRadius_BR;
        setBgSelector();
    }

    public int getBackgroundColor() {
        return backgroundColor;
    }

    public int getBackgroundPressColor() {
        return backgroundPressColor;
    }

    public int getCornerRadius() {
        return cornerRadius;
    }

    public int getStrokeWidth() {
        return strokeWidth;
    }

    public int getStrokeColor() {
        return strokeColor;
    }

    public int getStrokePressColor() {
        return strokePressColor;
    }

    public int getTextPressColor() {
        return textPressColor;
    }

    public boolean isRadiusHalfHeight() {
        return isRadiusHalfHeight;
    }

    public boolean isWidthHeightEqual() {
        return isWidthHeightEqual;
    }

    public int getCornerRadius_TL() {
        return cornerRadius_TL;
    }

    public int getCornerRadius_TR() {
        return cornerRadius_TR;
    }

    public int getCornerRadius_BL() {
        return cornerRadius_BL;
    }

    public int getCornerRadius_BR() {
        return cornerRadius_BR;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    protected int dp2px(float dp) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dp * scale + 0.5f);
    }

    protected int sp2px(float sp) {
        final float scale = this.context.getResources().getDisplayMetrics().scaledDensity;
        return (int) (sp * scale + 0.5f);
    }

    private void setDrawable(GradientDrawable gd, int color, int strokeColor) {
        gd.setColors(colors);
        gd.setGradientType(GradientDrawable.LINEAR_GRADIENT);
        //从上到下
        if (gradientOrientation == 1) {
            gd.setOrientation(GradientDrawable.Orientation.TOP_BOTTOM);
        }
        //从左上到右下
        else if (gradientOrientation == 2) {
            gd.setOrientation(GradientDrawable.Orientation.TL_BR);
        }
        //从左到右
        else {
            gd.setOrientation(GradientDrawable.Orientation.LEFT_RIGHT);
        }

        int bb = cornerRadius_TL + cornerRadius_TR + cornerRadius_BR + cornerRadius_BL;
        if (bb > 0) {
            radiusArr[0] = cornerRadius_TL;
            radiusArr[1] = cornerRadius_TL;
            radiusArr[2] = cornerRadius_TR;
            radiusArr[3] = cornerRadius_TR;
            radiusArr[4] = cornerRadius_BR;
            radiusArr[5] = cornerRadius_BR;
            radiusArr[6] = cornerRadius_BL;
            radiusArr[7] = cornerRadius_BL;
            gd.setCornerRadii(radiusArr);
        } else {
            gd.setCornerRadius(cornerRadius);
        }

        gd.setStroke(strokeWidth, strokeColor);

    }

    //平行四边形
    private Drawable setRhomboidDrawable(int color, int strokeColor) {

        Path path = new Path();

        Rect rect = new Rect();
        rect.left = strokeWidth;
        rect.top = strokeWidth;
        rect.right = rect.left + width - strokeWidth * 2;
        rect.bottom = rect.top + height - strokeWidth * 2;

        if (cornerRadius_TL == 0 && cornerRadius_TR == 0 && cornerRadius_BR == 0 && cornerRadius_BL == 0) {
            if (cornerRadius > 0) {
                cornerRadius_TL = cornerRadius;
                cornerRadius_TR = cornerRadius;
                cornerRadius_BR = cornerRadius;
                cornerRadius_BL = cornerRadius;
            }
        }

        int offsetTop;//顶部的偏移距离
        int offsetBottom;//底部的偏移距离
        if (offsetX >= 0) {
            offsetTop = offsetX;
            offsetBottom = 0;
        } else {
            offsetTop = 0;
            offsetBottom = Math.abs(offsetX);
        }
        path.moveTo(rect.left + offsetTop + cornerRadius_TL, rect.top);
        if (cornerRadius_TR > 0) {
            path.lineTo(rect.right - offsetBottom - cornerRadius_TR, rect.top);
            path.cubicTo(rect.right - offsetBottom - cornerRadius_TR / 2f, rect.top, rect.right - offsetBottom, rect.top + cornerRadius_TR / 2f, rect.right - offsetBottom, rect.top + cornerRadius_TR);
        } else {
            path.lineTo(rect.right - offsetBottom, rect.top);
        }

        if (cornerRadius_BR > 0) {
            path.lineTo(rect.right - offsetTop, rect.bottom - cornerRadius_BR);
            path.cubicTo(rect.right - offsetTop, rect.bottom - cornerRadius_BR / 2f, rect.right - offsetTop - cornerRadius_BR / 2f, rect.bottom, rect.right - offsetTop - cornerRadius_BR, rect.bottom);
        } else {
            path.lineTo(rect.right - offsetTop, rect.bottom);
        }

        if (cornerRadius_BL > 0) {
            path.lineTo(rect.left + offsetBottom + cornerRadius_BL, rect.bottom);
            path.cubicTo(rect.left + offsetBottom + cornerRadius_BL / 2f, rect.bottom, rect.left + offsetBottom, rect.bottom - cornerRadius_BL / 2f, rect.left + offsetBottom, rect.bottom - cornerRadius_BL);
        } else {
            path.lineTo(rect.left + offsetBottom, rect.bottom);
        }

        if (cornerRadius_TL > 0) {
            path.lineTo(rect.left + offsetTop, rect.top + cornerRadius_TL);
            path.cubicTo(rect.left + offsetTop, rect.top + cornerRadius_TL / 2f, rect.left + offsetTop + cornerRadius_TL / 2f, rect.top, rect.left + offsetTop + cornerRadius_TL, rect.top);
        } else {
            path.lineTo(rect.left + offsetTop, rect.top);
        }

        path.close();


        PathShape pathShape = new PathShape(path, width, height);
        ShapeDrawable drawable = new ShapeDrawable(pathShape);
//        drawable.setBounds(0, 0, width, height);
//        Shader shader1 = new LinearGradient(0, 0, width, height, strokeColor, strokeColor, Shader.TileMode.CLAMP);
        Shader shader = null;

        //从上到下
        if (gradientOrientation == 1) {
            shader = new LinearGradient(0, 0, 0, height, colors, null, Shader.TileMode.CLAMP);
        }
        //从左上到右下
        else if (gradientOrientation == 2) {
            shader = new LinearGradient(0, 0, width, height, colors, null, Shader.TileMode.CLAMP);
        }
        //从左到右
        else {
            shader = new LinearGradient(0, 0, width, 0, colors, null, Shader.TileMode.CLAMP);
        }


//        LinearGradient linearGradient = new LinearGradient();
//        ComposeShader composeShader = new ComposeShader(shader1, shader, PorterDuff.Mode.SRC_ATOP);
        drawable.getPaint().setShader(shader);

//        drawable.getPaint().setStrokeWidth(strokeWidth);
//        drawable.getPaint().setColor(strokeColor);
        drawable.getPaint().setStyle(Paint.Style.FILL);

        return drawable;
    }

    private void initGradientColors() {
        List<Integer> list = new ArrayList<>();
        if (startColor == 0 && endColor == 0) {
            if (color1 != 0) list.add(color1);
            if (color2 != 0) list.add(color2);
            if (color3 != 0) list.add(color3);
            if (color4 != 0) list.add(color4);
        } else {
            if (startColor != 0) list.add(startColor);
            if (endColor != 0) list.add(endColor);
        }

        if (list.size() > 0) {
            int count = 2;
            int size = list.size();
            if (size > count) {
                count = list.size();
            }
            colors = new int[count];
            for (int i = 0; i < size; i++) {
                colors[i] = list.get(i);
            }

            if (size < count) {
                for (int i = list.size(); i < count; i++) {
                    colors[i] = colors[i % size];
                }
            }


        } else {
            colors = new int[2];
            colors[0] = backgroundColor;
            colors[1] = backgroundColor;
        }
    }


    public void setBgSelector() {

        initGradientColors();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP && isRippleEnable) {
            if (offsetX == 0) {
                setDrawable(gd_background, backgroundColor, strokeColor);
                RippleDrawable rippleDrawable = new RippleDrawable(getPressedColorSelector(backgroundColor, backgroundPressColor), gd_background, null);
                view.setBackground(rippleDrawable);
            } else {
                Drawable drawable = setRhomboidDrawable(backgroundColor, strokeColor);
                RippleDrawable rippleDrawable = new RippleDrawable(getPressedColorSelector(backgroundColor, backgroundPressColor), drawable, null);
                view.setBackground(rippleDrawable);
            }

        } else {
            StateListDrawable bg = new StateListDrawable();
            if (offsetX == 0) {
                setDrawable(gd_background, backgroundColor, strokeColor);
                bg.addState(new int[]{-android.R.attr.state_pressed}, gd_background);
                if (backgroundPressColor != Integer.MAX_VALUE || strokePressColor != Integer.MAX_VALUE) {
                    setDrawable(gd_background_press, backgroundPressColor == Integer.MAX_VALUE ? backgroundColor : backgroundPressColor,
                            strokePressColor == Integer.MAX_VALUE ? strokeColor : strokePressColor);

                    bg.addState(new int[]{android.R.attr.state_pressed}, gd_background_press);
                }
            } else {
                Drawable drawable = setRhomboidDrawable(backgroundColor, strokeColor);
                bg.addState(new int[]{-android.R.attr.state_pressed}, drawable);
                if (backgroundPressColor != Integer.MAX_VALUE || strokePressColor != Integer.MAX_VALUE) {
                    Drawable drawablePress = setRhomboidDrawable(backgroundPressColor == Integer.MAX_VALUE ? backgroundColor : backgroundPressColor,
                            strokePressColor == Integer.MAX_VALUE ? strokeColor : strokePressColor);
                    bg.addState(new int[]{android.R.attr.state_pressed}, drawablePress);
                }
            }


            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {//16
                view.setBackground(bg);
            } else {
                //noinspection deprecation
                view.setBackgroundDrawable(bg);
            }
        }

        if (view instanceof TextView) {
            if (textPressColor != Integer.MAX_VALUE) {
                ColorStateList textColors = ((TextView) view).getTextColors();
//              Log.d("AAA", textColors.getColorForState(new int[]{-android.R.attr.state_pressed}, -1) + "");
                ColorStateList colorStateList = new ColorStateList(
                        new int[][]{new int[]{-android.R.attr.state_pressed}, new int[]{android.R.attr.state_pressed}},
                        new int[]{textColors.getDefaultColor(), textPressColor});
                ((TextView) view).setTextColor(colorStateList);
            }
        }
    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    private ColorStateList getPressedColorSelector(int normalColor, int pressedColor) {
        return new ColorStateList(
                new int[][]{
                        new int[]{android.R.attr.state_pressed},
                        new int[]{android.R.attr.state_focused},
                        new int[]{android.R.attr.state_activated},
                        new int[]{}
                },
                new int[]{
                        pressedColor,
                        pressedColor,
                        pressedColor,
                        normalColor
                }
        );
    }


    public interface onRoundViewDelegateInter {
        RoundViewDelegate getDelegate();
    }
}
