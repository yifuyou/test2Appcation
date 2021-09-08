package com.base.common.view.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.MotionEvent;

import androidx.appcompat.widget.AppCompatTextView;

import com.base.common.R;
import com.base.common.view.roundview.RoundTextView;

/**
 * 可以设置Drawable 的宽高
 */
public class DrawableTextView extends RoundTextView {
    public DrawableTextView(Context context) {
        this(context, null);
    }

    public DrawableTextView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public DrawableTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(attrs);
    }

    //设置方向
    private static final int LEFT = 1, TOP = 2, RIGHT = 3, BOTTOM = 4;

    //从attrs中获取期望的drawable的宽高，资源，上下左右4个位置
    private void init(AttributeSet attrs) {
        if (attrs != null) {
            TypedArray a = getContext().obtainStyledAttributes(attrs, R.styleable.DrawableTextView);
            int mWidth = a.getDimensionPixelSize(R.styleable.DrawableTextView_dr_width, 0);
            int mHeight = a.getDimensionPixelSize(R.styleable.DrawableTextView_dr_height, 0);
            Drawable mDrawable = a.getDrawable(R.styleable.DrawableTextView_dr_src);
            int mLocation = a.getInt(R.styleable.DrawableTextView_dr_location, LEFT);
            boolean deleteLine = a.getBoolean(R.styleable.DrawableTextView_dr_delete_line, false);
            if (deleteLine) setDeleteLine();
            boolean underLine = a.getBoolean(R.styleable.DrawableTextView_dr_under_line, false);
            if (underLine) setUnderLine();
            a.recycle();
            drawDrawable(mDrawable, mWidth, mHeight, mLocation);
        }

    }


    public void setDeleteLine() {
        getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG | Paint.ANTI_ALIAS_FLAG);
    }

    public void setUnderLine() {
        getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG | Paint.ANTI_ALIAS_FLAG);
    }


    private void drawDrawable(Drawable mDrawable, int mWidth, int mHeight, int mLocation) {
        if (mDrawable != null) {
            if (mWidth != 0 && mHeight != 0) {
                mDrawable.setBounds(0, 0, mWidth, mHeight);
            }
            switch (mLocation) {
                case LEFT:
                    this.setCompoundDrawables(mDrawable, null, null, null);
                    break;
                case TOP:
                    this.setCompoundDrawables(null, mDrawable, null, null);
                    break;
                case RIGHT:
                    this.setCompoundDrawables(null, null, mDrawable, null);
                    break;
                case BOTTOM:
                    this.setCompoundDrawables(null, null, null, mDrawable);
                    break;
            }
        }
    }


}
