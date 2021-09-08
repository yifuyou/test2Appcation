package com.base.common.view.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;

import com.base.common.R;
import com.base.common.utils.DensityUtil;

public class ProgressCircleView extends View {


    public ProgressCircleView(Context context) {
        this(context, null, 0);
    }

    public ProgressCircleView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ProgressCircleView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        this(context, attrs, defStyleAttr, 0);
    }


    private Paint paint;
    private float[] data = {0, 0}; //数据
    private int[] colors = {0xff3E33FF, 0xffFFCD00};//颜色
    private float padding = getResources().getDimensionPixelSize(R.dimen.dp_0);//内边距
    private float strokeWidth = getResources().getDimensionPixelSize(R.dimen.dp_6);//圆环的宽度
    private float spaceAngle = 5;//中间间隔的角度


    private RectF oval = new RectF();//绘制圆的区域
    private float sum;//总的数量
    private float enableWp;//可用于计算的角度
    private int spaceCount = 0;//要绘制的间隔数量  计算所得
    private int startPos;//开始的位置  在data的 index
    private float startAngle = 45;//开始的角度  计算得到
    private float posAngle = 45f; //从哪个角度开始
    private float hp, wp;//高度和宽度

    private int width, height;

    public ProgressCircleView(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);

        setWillNotDraw(false);//重写onDraw方法,需要调用这个方法来清除flag

        if (attrs == null) return;
        //get layout_height
        String heightStr = attrs.getAttributeValue("http://schemas.android.com/apk/res/android", "layout_height");

        if (heightStr.equals(ViewGroup.LayoutParams.MATCH_PARENT + "")) {
        } else if (heightStr.equals(ViewGroup.LayoutParams.WRAP_CONTENT + "")) {
        } else {
            int[] systemAttrs = {android.R.attr.layout_height};
            TypedArray a = context.obtainStyledAttributes(attrs, systemAttrs);
            width = a.getDimensionPixelSize(0, ViewGroup.LayoutParams.WRAP_CONTENT);
            a.recycle();
        }

        String widthStr = attrs.getAttributeValue("http://schemas.android.com/apk/res/android", "layout_width");
        if (widthStr.equals(ViewGroup.LayoutParams.MATCH_PARENT + "")) {
        } else if (widthStr.equals(ViewGroup.LayoutParams.WRAP_CONTENT + "")) {
        } else {
            int[] systemAttrs = {android.R.attr.layout_width};
            TypedArray a = context.obtainStyledAttributes(attrs, systemAttrs);
            height = a.getDimensionPixelSize(0, ViewGroup.LayoutParams.WRAP_CONTENT);
            a.recycle();
        }
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.ProgressCircleView);
        int color1 = a.getColor(R.styleable.ProgressCircleView_pcv_colorLeft, 0);
        int color2 = a.getColor(R.styleable.ProgressCircleView_pcv_colorRight, 0);
        //交叉区域的长度  两边各一半  实际是*2
        padding = a.getDimension(R.styleable.ProgressCircleView_pcv_padding, padding);
        //中间的间的隔 透明色
        strokeWidth = a.getDimension(R.styleable.ProgressCircleView_pcv_strokeWidth, strokeWidth);
        spaceAngle = a.getFloat(R.styleable.ProgressCircleView_pcv_spaceAngle, spaceAngle);
        posAngle = a.getFloat(R.styleable.ProgressCircleView_pcv_startAngle, posAngle);

        float data1 = a.getFloat(R.styleable.ProgressCircleView_pcv_data1, data[0]);
        float data2 = a.getFloat(R.styleable.ProgressCircleView_pcv_data2, data[1]);


        a.recycle();


        if (color1 != 0) colors[0] = color1;
        if (color2 != 0) colors[1] = color2;

        if (data1 != 0) data[0] = data1;
        if (data2 != 0) data[1] = data2;

        initData();
    }


    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        height = getMeasuredHeight() - (int) strokeWidth;
        width = getMeasuredWidth() - (int) strokeWidth;
        initData();
    }

    private void iniPaint() {
        paint = new Paint();
        paint.setAntiAlias(true);
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(strokeWidth);
    }

    private void initData() {
        hp = height - padding * 2;
        wp = width - padding * 2;

        float w = Math.min(hp, wp);

        oval.left = padding + (strokeWidth + wp - w) / 2f;
        oval.right = oval.left + w;
        oval.top = padding + (strokeWidth + hp - w) / 2f;
        oval.bottom = oval.top + w;

        //计算总数
        sum = 0;
        for (float datum : data) {
            sum += datum;
        }
        if (sum == 0) return;


        spaceCount = 0;
        //找出最大数值的index
        for (int i = 0; i < data.length; i++) {
            if (data[i] > 0) {
                spaceCount++;
            }
        }

        enableWp = 360 - spaceAngle * spaceCount;

        //检查data开始结束的位置
        startPos = 0;
        for (int i = 0; i < data.length; i++) {
            if (data[i] > 0) {
                startPos = i;
                break;
            }
        }


        startAngle = posAngle - (data[startPos] / sum * enableWp) / 2f;

    }


    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        iniPaint();
        if (sum == 0) {
            paint.setColor(0xffeeeeee);
            canvas.drawArc(oval, startAngle, 360, false, paint);
            return;
        }
        if (data.length != colors.length) {
            return;
        }

        if (spaceCount < 2) {
            paint.setColor(colors[startPos]);
            canvas.drawArc(oval, startAngle, 360, false, paint);
        } else {
            float current = startAngle;//当前绘制到的位置  角度
            for (int i = startPos; i < data.length; i++) {
                if (data[i] == 0) continue;
                //划过的角度
                float sweepAngle = data[i] / sum * enableWp;
                paint.setColor(colors[i]);
                canvas.drawArc(oval, current, sweepAngle, false, paint);
                current += sweepAngle + spaceAngle;
            }

        }


    }

    public void setStrokeWidth(int px) {
        strokeWidth = px;
        if (paint != null) {
            paint.setStrokeWidth(px);
            invalidate();
        }
    }

    public void setData(float... data) {
        this.data = data;
        initData();
        invalidate();
    }

    /**
     * @param data   数据
     * @param index  放入到几位
     * @param length 数据总长度
     */
    public void setDataForIndex(float data, int index, int length) {
        if (this.data.length != length) {
            this.data = new float[length];
            for (int i = 0; i < length; i++) {
                this.data[i] = 0f;
            }
        }
        if (index < length) {
            this.data[index] = data;
        }
        initData();
        invalidate();
    }

    public void setColors(int... colors) {
        this.colors = colors;
        invalidate();
    }


}
