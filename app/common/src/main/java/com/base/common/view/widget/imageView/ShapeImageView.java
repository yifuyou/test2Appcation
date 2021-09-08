package com.base.common.view.widget.imageView;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.MotionEvent;

import androidx.annotation.ColorInt;
import androidx.annotation.ColorRes;
import androidx.appcompat.widget.AppCompatImageView;

import com.base.common.R;
import com.base.common.utils.DensityUtil;
import com.base.common.utils.UIUtils;

/**
 * 1, 切圆角或圆
 * 2，绘制边框
 * 3, 绘制按下效果
 */
public class ShapeImageView extends AppCompatImageView {

    // 图片的宽高
    protected int width, height;

    private int borderColor = 0x1A000000; // 边框颜色
    private int borderWidth = 0; // 边框宽度
    protected int radius = getContext().getResources().getDimensionPixelSize(R.dimen.dp_3); // 圆角弧度
    protected int siv_radius_TL = 0; // 圆角弧度
    protected int siv_radius_TR = 0; // 圆角弧度
    protected int siv_radius_BL = 0; // 圆角弧度
    protected int siv_radius_BR = 0; // 圆角弧度

    protected int shapeType = ShapeType.RECTANGLE; // 图片类型（ 矩形）

    private Paint pressedPaint; // 按下的画笔
    protected float pressedAlpha = 0.2f; // 按下的透明度
    protected int pressedColor = 0x1A000000; // 按下的颜色

    private Drawable siv_placeholder;//占位符
    private Drawable siv_placeholder_err;//占位符


    private Drawable siv_src_select;//
    private Drawable siv_src_unselect;//

    private boolean siv_need_placeholder = true;//是否需要占位符

    private boolean siv_blur = false;//是否模糊

    private boolean isLongImage = false;//是否长图

    public ShapeImageView(Context context) {
        this(context, null);
    }

    public ShapeImageView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ShapeImageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        if (attrs != null) {
            TypedArray array = context.obtainStyledAttributes(attrs, R.styleable.ShapeImageView);
//            borderWidth = array.getDimensionPixelOffset(R.styleable.ShapeImageView_siv_border_width, borderWidth);
//            borderColor = array.getColor(R.styleable.ShapeImageView_siv_border_color, borderColor);
            radius = array.getDimensionPixelOffset(R.styleable.ShapeImageView_siv_radius, radius);

            siv_radius_TL = array.getDimensionPixelOffset(R.styleable.ShapeImageView_siv_radius_TL, 0);
            siv_radius_TR = array.getDimensionPixelOffset(R.styleable.ShapeImageView_siv_radius_TR, 0);
            siv_radius_BL = array.getDimensionPixelOffset(R.styleable.ShapeImageView_siv_radius_BL, 0);
            siv_radius_BR = array.getDimensionPixelOffset(R.styleable.ShapeImageView_siv_radius_BR, 0);

            pressedAlpha = array.getFloat(R.styleable.ShapeImageView_siv_pressed_alpha, pressedAlpha);
            if (pressedAlpha > 1) pressedAlpha = 1;
            pressedColor = array.getColor(R.styleable.ShapeImageView_siv_pressed_color, pressedColor);
            shapeType = array.getInteger(R.styleable.ShapeImageView_siv_shape_type, shapeType);
            siv_need_placeholder = array.getBoolean(R.styleable.ShapeImageView_siv_need_placeholder, siv_need_placeholder);
            siv_placeholder = array.getDrawable(R.styleable.ShapeImageView_siv_placeholder);
            siv_placeholder_err = array.getDrawable(R.styleable.ShapeImageView_siv_placeholder_err);


            siv_src_select = array.getDrawable(R.styleable.ShapeImageView_siv_src_select);
            siv_src_unselect = array.getDrawable(R.styleable.ShapeImageView_siv_src_unselect);

            siv_blur = array.getBoolean(R.styleable.ShapeImageView_siv_blur, siv_blur);

            array.recycle();
        }

        initPressedPaint();
    }

    // 初始化按下的画笔
    private void initPressedPaint() {
        pressedPaint = new Paint();
        pressedPaint.setAntiAlias(true);
        pressedPaint.setStyle(Paint.Style.FILL);
        pressedPaint.setColor(pressedColor);
        pressedPaint.setAlpha(0);
        pressedPaint.setFlags(Paint.ANTI_ALIAS_FLAG);
    }


    @Override
    protected void onDraw(Canvas canvas) {
        shearRoundRect(canvas);
        super.onDraw(canvas);

        // 绘制边框
//        drawBorder(canvas);
        // 绘制按下效果
        if (isClickable()) {
            drawPressed(canvas);
        }
        drawLongImage(canvas);
    }


    @Override
    public void setSelected(boolean selected) {
        super.setSelected(selected);
        if (selected) {
            if (siv_src_select != null) {
                setImageDrawable(siv_src_select);
            }
        } else {
            if (siv_src_unselect != null) {
                setImageDrawable(siv_src_unselect);
            }
        }
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        width = getWidth();
        height = getHeight();
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        width = w;
        height = h;
    }

    public void setLongImage(boolean longImage) {
        isLongImage = longImage;
    }

    /**
     * 切圆角
     *
     * @param canvas
     */
    private void shearRoundRect(Canvas canvas) {
        Path path = new Path();
        switch (shapeType) {
            case ShapeType.CIRCLE:
                int w;
                if (width > height) {
                    w = height;
                } else {
                    w = width;
                }
                path.addCircle(w / 2, height / 2, w / 2, Path.Direction.CW);
                canvas.clipPath(path);
                break;
            case ShapeType.ROUNDRECT:
                if (width > radius && height > radius) {
                    int start = getPaddingStart();
                    int top = getPaddingTop();
                    int end = getPaddingEnd();
                    int bottom = getPaddingBottom();
                    RectF rectf = new RectF(start, top, width - end, height - bottom);
                    if (siv_radius_TL > 0
                            || siv_radius_TR > 0
                            || siv_radius_BR > 0
                            || siv_radius_BL > 0) {
                        float[] radiusList = {siv_radius_TL, siv_radius_TL, siv_radius_TR, siv_radius_TR, siv_radius_BR, siv_radius_BR, siv_radius_BL, siv_radius_BL};
                        path.addRoundRect(rectf, radiusList, Path.Direction.CW);
                    } else {//左上角xy半径，右上角，右下角，左下角*/
                        path.addRoundRect(rectf, radius, radius, Path.Direction.CW);
                    }
                    canvas.clipPath(path);
                }
                break;
            default:

                break;
        }


    }


    // 绘制边框
    private void drawBorder(Canvas canvas) {
        if (borderWidth > 0) {
            Paint paint = new Paint();
            paint.setStrokeWidth(borderWidth);
            paint.setStyle(Paint.Style.STROKE);
            paint.setColor(borderColor);
            paint.setAntiAlias(true);

            switch (shapeType) {
                case ShapeType.CIRCLE:
                    canvas.drawCircle(width / 2, height / 2, (width - borderWidth) / 2, paint);
                    break;
                case ShapeType.ROUNDRECT:
                    RectF rectf = new RectF(borderWidth / 2, borderWidth / 2, getWidth() - borderWidth / 2, getHeight() - borderWidth / 2);
                    canvas.drawRoundRect(rectf, radius, radius, paint);
                    break;
                default:
                    RectF rect = new RectF(borderWidth / 2, borderWidth / 2, getWidth() - borderWidth / 2, getHeight() - borderWidth / 2);
                    canvas.drawRect(rect, paint);
                    break;
            }
        }
    }


    // 绘制按下效果
    private void drawPressed(Canvas canvas) {
        switch (shapeType) {
            case ShapeType.CIRCLE:
                canvas.drawCircle(width / 2, height / 2, width / 2, pressedPaint);
                break;
            case ShapeType.ROUNDRECT:
                RectF rectf = new RectF(1, 1, width - 1, height - 1);
                canvas.drawRoundRect(rectf, radius, radius, pressedPaint);
                break;
            default:
                RectF rect = new RectF(1, 1, width - 1, height - 1);
                canvas.drawRect(rect, pressedPaint);
                break;
        }
    }


    private void drawLongImage(Canvas canvas) {
        if (isLongImage) {
            int w = DensityUtil.getDimens(R.dimen.dp_36);
            int h = DensityUtil.getDimens(R.dimen.dp_18);
            RectF rect = new RectF(width - w, height - h, width, height);

            Paint paint = new Paint();
            paint.setAntiAlias(true);
            paint.setStyle(Paint.Style.FILL);
            paint.setColor(0x33000000);
            paint.setFlags(Paint.ANTI_ALIAS_FLAG);


            Path path = new Path();

            float l = siv_radius_TL == 0 ? radius : siv_radius_TL;
            float r = siv_radius_BR == 0 ? radius : siv_radius_BR;
            float[] radiusList = {l, l, 0, 0, r, r, 0, 0};
            path.addRoundRect(rect, radiusList, Path.Direction.CW);
            canvas.drawPath(path, paint);

            paint.setColor(0xffffffff);
            paint.setTextSize(DensityUtil.getDimens(R.dimen.font_11));
            canvas.drawText("长图", rect.left + w / 5f, rect.bottom - h / 4f, paint);
        }
    }


    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                onTouch((int) (pressedAlpha * 255));
                break;
            case MotionEvent.ACTION_MOVE:
                break;
            default:
                onTouch(0);
                break;
        }
        return super.onTouchEvent(event);
    }

    private void onTouch(int alpha) {
        pressedPaint.setAlpha(alpha);
        invalidate();
    }


    // 设置边框颜色
    public void setBorderColor(@ColorRes int id) {
        this.borderColor = getResources().getColor(id);
        invalidate();
    }

    public void setBorderColorInt(@ColorInt int id) {
        this.borderColor = id;
        invalidate();
    }

    // 设置边框宽度
    public void setBorderWidth(int borderWidth) {
        this.borderWidth = borderWidth;
        invalidate();
    }

    public boolean isSiv_need_placeholder() {
        return siv_need_placeholder;
    }

    public Drawable getSiv_placeholder() {
        return siv_placeholder;
    }

    public Drawable getSiv_placeholder_err() {
        return siv_placeholder_err;
    }

    public boolean isSiv_blur() {
        return siv_blur;
    }

    // 设置图片按下颜色透明度
    public void setPressedAlpha(float pressAlpha) {
        this.pressedAlpha = pressAlpha;
    }

    // 设置图片按下的颜色
    public void setPressedColor(@ColorRes int id) {
        this.pressedColor = getResources().getColor(id);
        pressedPaint.setColor(pressedColor);
        pressedPaint.setAlpha(0);
        invalidate();
    }

    // 设置圆角半径
    public void setRadius(int radius) {
        this.radius = radius;
        invalidate();
    }

    public int getRadius() {
        return radius;
    }


    // 设置形状类型
    public void setShapeType(@ShapeType int shapeType) {
        this.shapeType = shapeType;
        invalidate();
    }


    public int getShapeType() {
        return shapeType;
    }


    public void setSiv_need_placeholder(boolean siv_need_placeholder) {
        this.siv_need_placeholder = siv_need_placeholder;
    }

    public void setSiv_placeholder(Drawable siv_placeholder) {
        this.siv_placeholder = siv_placeholder;
    }

    public void setSiv_placeholder_err(Drawable siv_placeholder_err) {
        this.siv_placeholder_err = siv_placeholder_err;
    }


    public void setSiv_src_select(Drawable siv_src_select) {
        this.siv_src_select = siv_src_select;
    }


    public void setSiv_src_unselect(Drawable siv_src_unselect) {
        this.siv_src_unselect = siv_src_unselect;
    }
}
