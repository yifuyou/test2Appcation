package com.base.common.view.roundview;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.LinearGradient;
import android.graphics.Shader;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.ColorInt;
import androidx.annotation.Px;
import androidx.appcompat.widget.AppCompatTextView;

import com.base.common.R;
import com.base.common.utils.DensityUtil;
import com.base.common.view.roundview.RoundViewDelegate;

/**
 * 用于需要圆角矩形框背景的TextView的情况,减少直接使用TextView时引入的shape资源文件
 */
public class RoundTextView extends AppCompatTextView implements RoundViewDelegate.onRoundViewDelegateInter{
    private RoundViewDelegate delegate;
    private int color0;
    private int color1;
    private int gradientOrientation;//渐变方向  0 左右 1 上下  2左上到右下

    public RoundTextView(Context context) {
        this(context, null);
    }

    public RoundTextView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public RoundTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        delegate = new RoundViewDelegate(this, context, attrs);

        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.RoundTextView);

        color0 = ta.getColor(R.styleable.RoundTextView_rv_text_Color0, 0);
        color1 = ta.getColor(R.styleable.RoundTextView_rv_text_Color1, 0);
        gradientOrientation = ta.getInt(R.styleable.RoundTextView_rv_text_gradientOrientation, 0);
        ta.recycle();
    }

    /**
     * use delegate to set attr
     */
    public RoundViewDelegate getDelegate() {
        return delegate;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        if (delegate.isWidthHeightEqual() && getWidth() > 0 && getHeight() > 0) {
            int max = Math.max(getWidth(), getHeight());
            int measureSpec = MeasureSpec.makeMeasureSpec(max, MeasureSpec.EXACTLY);
            super.onMeasure(measureSpec, measureSpec);
            return;
        }

        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }


    private void setTextViewStyles(@ColorInt int color0, @ColorInt int color1) {
        LinearGradient mLinearGradient = new LinearGradient(0, 0, getPaint().getTextSize() * getText().length(), 0, color0, color1, Shader.TileMode.CLAMP);

        float width = getPaint().measureText(getText().toString());
        float height = DensityUtil.measureTextHeight(getPaint());

        int[] colors = new int[2];
        colors[0] = color0;
        colors[1] = color1;
        //从上到下
        if (gradientOrientation == 1) {
            mLinearGradient = new LinearGradient(0, 0, 0, height, colors, null, Shader.TileMode.CLAMP);
        }
        //从左上到右下
        else if (gradientOrientation == 2) {
            mLinearGradient = new LinearGradient(0, 0, width, height, colors, null, Shader.TileMode.CLAMP);
        }
        //从左到右
        else {
            mLinearGradient = new LinearGradient(0, 0, width, 0, colors, null, Shader.TileMode.CLAMP);
        }


        getPaint().setShader(mLinearGradient);
        invalidate();
    }


    public void setRvTextColor(int color0, int color1) {
        this.color0 = color0;
        this.color1 = color1;
        invalidate();
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        delegate.setHeight(bottom - top);
        delegate.setWidth(right - left);
        if (delegate.isRadiusHalfHeight()) {
            delegate.setCornerRadius(getHeight() / 2);
        } else {
            delegate.setBgSelector();
        }

        if (color0 != 0 && color1 != 0) {
            setTextViewStyles(color0, color1);
        } else {
            if (color0 != 0) {
                setTextColor(color0);
            } else if (color1 != 0) {
                setTextColor(color1);
            }
        }
    }



}
