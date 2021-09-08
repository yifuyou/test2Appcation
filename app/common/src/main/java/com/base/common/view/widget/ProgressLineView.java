package com.base.common.view.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;

import com.base.common.R;
import com.base.common.utils.DensityUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * 比较图
 */
public class ProgressLineView extends View {

//    private int mHeight;//高度

    public ProgressLineView(Context context) {
        this(context, null, 0);
    }

    public ProgressLineView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ProgressLineView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        this(context, attrs, defStyleAttr, 0);
    }

    public ProgressLineView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);

        setWillNotDraw(false);//重写onDraw方法,需要调用这个方法来清除flag


        paint = new Paint();
        paint.setAntiAlias(true);
        paint.setStyle(Paint.Style.FILL);
        paint.setStrokeWidth(1);


        if (attrs == null) return;
        //get layout_height
        String height = attrs.getAttributeValue("http://schemas.android.com/apk/res/android", "layout_height");

        if (height.equals(ViewGroup.LayoutParams.MATCH_PARENT + "")) {
        } else if (height.equals(ViewGroup.LayoutParams.WRAP_CONTENT + "")) {
        } else {
            int[] systemAttrs = {android.R.attr.layout_height};
            TypedArray a = context.obtainStyledAttributes(attrs, systemAttrs);
            hp = a.getDimensionPixelSize(0, ViewGroup.LayoutParams.WRAP_CONTENT);
            a.recycle();
        }

        String width = attrs.getAttributeValue("http://schemas.android.com/apk/res/android", "layout_width");
        if (width.equals(ViewGroup.LayoutParams.MATCH_PARENT + "")) {
        } else if (width.equals(ViewGroup.LayoutParams.WRAP_CONTENT + "")) {
        } else {
            int[] systemAttrs = {android.R.attr.layout_width};
            TypedArray a = context.obtainStyledAttributes(attrs, systemAttrs);
            wp = a.getDimensionPixelSize(0, ViewGroup.LayoutParams.WRAP_CONTENT);
            a.recycle();
        }


        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.ProgressLineView);
        int color1 = a.getColor(R.styleable.ProgressLineView_plv_color1, 0);
        int color2 = a.getColor(R.styleable.ProgressLineView_plv_color2, 0);
        int color3 = a.getColor(R.styleable.ProgressLineView_plv_color3, 0);
        int color4 = a.getColor(R.styleable.ProgressLineView_plv_color4, 0);

        float data1 = a.getFloat(R.styleable.ProgressLineView_plv_data1, 0f);
        float data2 = a.getFloat(R.styleable.ProgressLineView_plv_data2, 0f);
        float data3 = a.getFloat(R.styleable.ProgressLineView_plv_data3, 0f);
        float data4 = a.getFloat(R.styleable.ProgressLineView_plv_data4, 0f);

        //交叉区域的长度  两边各一半  实际是*2
        crossSpec = a.getDimension(R.styleable.ProgressLineView_plv_crossSpec, crossSpec);
        //中间的间的隔 透明色
        space = a.getDimension(R.styleable.ProgressLineView_plv_space, space);
        isLeft = a.getBoolean(R.styleable.ProgressLineView_plv_isLeft, isLeft);
        a.recycle();

        List<Integer> list = new ArrayList<>();
        if (color1 != 0) list.add(color1);
        if (color2 != 0) list.add(color2);
        if (color3 != 0) list.add(color3);
        if (color4 != 0) list.add(color4);
        int size = list.size();
        if (size > 0) {
            colors = new int[size];
            float[] datas = new float[size];
            for (int i = 0; i < size; i++) {
                colors[i] = list.get(i);
                if (i < data.length) {
                    datas[i] = data[i];
                } else {
                    datas[i] = 0f;
                }
            }

            data = datas;
        }
        list = null;

        List<Float> listf = new ArrayList<>();
        if (data1 != 0) listf.add(data1);
        if (data2 != 0) listf.add(data2);
        if (data3 != 0) listf.add(data3);
        if (data4 != 0) listf.add(data4);

        if (listf.size() > 0) {
            data = new float[listf.size()];
            for (int i = 0; i < listf.size(); i++) {
                data[i] = listf.get(i);
            }
        }
        listf = null;

        initData();
    }


    private float[] data = {5, 6, 5}; //数据
    private int[] colors = {0xffFF4949, 0xff3371E8, 0xff68AE7B};//颜色

    private float crossSpec = getResources().getDimensionPixelSize(R.dimen.dp_3);//交叉区域的长度  两边各一半  实际是*2

    private float space = getResources().getDimensionPixelSize(R.dimen.dp_2);//中间的间的隔 透明色

    private boolean isLeft = true;//缺口向左倾斜
    private int hp, wp;//高度和宽度
    private float enableWp;//可用于计算的宽度
    private int rad;  //开始结束时圆弧的半径

    private Paint paint;
    private int sum;//总的数量
    private int startPos, endPos;//开始和结束的位置  在data的 index
    private int maxDataPos;//最大的那个数据的index
    private float leftW, rightW, maxW;//左右两进度条的宽度  最大的那个宽度  计算出来的
    private Path path = new Path();

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        hp = getMeasuredHeight();
        wp = getMeasuredWidth();
        initData();
    }


    private void initData() {
        rad = hp / 2;

        //计算总数
        sum = 0;
        for (float datum : data) {
            sum += datum;
        }
        if (sum == 0) return;

        float temp = 0;
        int spaceCount = 0;
        //找出最大数值的index
        for (int i = 0; i < data.length; i++) {
            if (data[i] > temp) {
                temp = data[i];
                maxDataPos = i;
            }
            if (data[i] > 0) {
                spaceCount++;
            }
        }
        if (spaceCount > 0) spaceCount -= 1;

        //检查data开始结束的位置
        startPos = 0;
        endPos = data.length - 1;
        for (int i = 0; i < data.length; i++) {
            if (data[i] > 0) {
                startPos = i;
                break;
            }
        }
        for (int length = data.length - 1; length >= 0; length--) {
            if (data[length] > 0) {
                endPos = length;
                break;
            }
        }

        enableWp = wp - space * spaceCount - hp;

        leftW = enableWp * data[startPos] / sum;
        rightW = enableWp * data[endPos] / sum;
        maxW = enableWp * data[maxDataPos] / sum;

        int differValue = 0;//需要补齐的缺口  有可能左右两边都需要补齐

        //如果右边的距离小于圆弧半径和交叉区域的长度
        if (rightW < crossSpec) {
            differValue += crossSpec - rightW;
            rightW = crossSpec;
        }

        //如果左边的距离小于圆弧半径和交叉区域的长度
        if (leftW < crossSpec) {
            differValue += crossSpec - leftW;
            leftW = crossSpec;
        }


        //只有一个的情况不用考虑，直接绘制全部
        if (startPos == endPos) {

        }
        //如果最长是startPos、endPos  或只有两个数据时，缺少的部份从头和尾去掉
        else if (startPos == maxDataPos || endPos == maxDataPos || endPos - startPos == 1) {
            if (data[startPos] > data[endPos]) {
                leftW = leftW - differValue;
            } else {
                rightW = rightW - differValue;
            }
        }
        //缺少的部分从最长的扣
        else {
            maxW = maxW - differValue;
        }


    }


    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        int flag = isLeft ? 1 : -1;//符号

        if (sum == 0 || data.length != colors.length) {
            paint.setColor(0xffeeeeee);
            canvas.drawCircle(rad, rad, rad, paint);
            canvas.drawCircle(wp - rad, rad, rad, paint);
            canvas.drawRect(rad, 0, wp - rad, hp, paint);
            return;
        }


        //画一个开始的圆
        paint.setColor(colors[startPos]);
        RectF oval = new RectF(0, 0, hp, hp);
        //当我们设置为true的时候，绘制的时候就经过圆心了
        canvas.drawArc(oval, 90, 180, true, paint);


        //画末尾的圆
        paint.setColor(colors[endPos]);
        oval = new RectF(wp - hp, 0, wp, hp);
        canvas.drawArc(oval, 270, 180, true, paint);


        //如果只有一个
        if (startPos == endPos) {
            paint.setColor(colors[startPos]);
            canvas.drawRect(rad, 0, wp - rad, hp, paint);
        }
        //两个以上
        else {

            //画左边的闭合路径
            path.reset();

            //移动到右上角
            path.moveTo(leftW + rad - crossSpec * flag, 0);
            path.lineTo(rad, 0);
            path.lineTo(rad, hp);
            path.lineTo(leftW + rad + crossSpec * flag, hp);
            path.close();
            paint.setColor(colors[startPos]);
            canvas.drawPath(path, paint);


            //画右边的闭合路径
            path.reset();
            //移动到右边的左上角
            path.moveTo(wp - rightW - rad - crossSpec * flag, 0);
            path.lineTo(wp - rad, 0);
            path.lineTo(wp - rad, hp);
            path.lineTo(wp - rightW - rad + crossSpec * flag, hp);

            path.close();
            paint.setColor(colors[endPos]);
            canvas.drawPath(path, paint);


            if (endPos - startPos > 1) {
                float current = leftW + rad;//当前绘制到的位置
                //画中间的平行四边形
                for (int i = startPos + 1; i < endPos; i++) {
                    //先找到开始绘制的位置
                    if (data[i] == 0) continue;

                    float w = enableWp * data[i] / sum;
                    if (i == maxDataPos) w = maxW;

                    float left = current + space;
                    float right = left + w;

                    path.reset();
                    //移动到左上角
                    path.moveTo(left - crossSpec * flag, 0);
                    path.lineTo(right - crossSpec * flag, 0);
                    path.lineTo(right + crossSpec * flag, hp);
                    path.lineTo(left + crossSpec * flag, hp);
                    path.close();
                    paint.setColor(colors[i]);
                    canvas.drawPath(path, paint);

                    current = right;//记录当前绘制的位置
                }

            }


        }


    }

    public void setLeft(boolean left) {
        isLeft = left;
    }

    public void setSpace(int space) {
        this.space = space;
    }

    public void setData(float... datas) {
        this.data = datas;
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
