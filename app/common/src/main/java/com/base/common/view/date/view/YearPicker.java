package com.base.common.view.date.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import androidx.annotation.Nullable;

import com.base.common.R;

import java.util.ArrayList;
import java.util.List;


@SuppressWarnings("unused")
public class YearPicker extends WheelPicker<String> {

    private static final int DEFAULT_YEAR = 1990;
    private int mStartYear, mEndYear;
    public final static char[] upper = "〇一二三四五六七八九".toCharArray();
    private int mSelectedYear;
    private int mCurrentPosition;
    private int mode;
    private List<String> list = new ArrayList<String>();
    private OnYearSelectedListener mOnYearSelectedListener;

    public YearPicker(Context context) {
        this(context, null);
    }

    public YearPicker(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public YearPicker(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initAttrs(context, attrs);
        setItemMaximumWidthText("0000年");
        updateYear();
        setSelectedYear(mSelectedYear, false);
        setOnWheelChangeListener(new OnWheelChangeListener<String>() {
            @Override
            public void onWheelSelected(String item, int position) {
                String s = list.get(position);
                if(s == null){
                    return;
                }
                mSelectedYear = Integer.parseInt(s.substring(0,s.length()-1));
                if (mOnYearSelectedListener != null) {
                    mOnYearSelectedListener.onYearSelected(mSelectedYear);
                }
            }
        });
    }




    private boolean isLunarType;
    public void setLunarType(boolean isLunarType){
        this.isLunarType = isLunarType;
    }

    private void initAttrs(Context context, @Nullable AttributeSet attrs) {
        if (attrs == null) {
            return;
        }

        mSelectedYear = DEFAULT_YEAR;
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.YearPicker);
        mStartYear = a.getInteger(R.styleable.YearPicker_startYear, 1920);
        mEndYear = a.getInteger(R.styleable.YearPicker_endYear, 2050);
        a.recycle();

    }

    public void updateYear() {
        if(list.isEmpty()) {
            for (int i = mStartYear; i <= mEndYear; i++) {
                list.add(i + "年");
            }
        }
        setDataList(list);
    }

    public void setStartYear(int startYear) {
        mStartYear = startYear;
        updateYear();
        if (mStartYear > mSelectedYear) {
            setSelectedYear(mStartYear, false);
        } else {
            setSelectedYear(mSelectedYear, false);
        }
    }

    public void setEndYear(int endYear) {
        mEndYear = endYear;
        updateYear();
        if (mSelectedYear > endYear) {
            setSelectedYear(mEndYear, false);
        } else {
            setSelectedYear(mSelectedYear, false);
        }
    }

    public void setYear(int startYear, int endYear) {
        setStartYear(startYear);
        setEndYear(endYear);
    }

    public void setSelectedYear(int selectedYear) {
        setSelectedYear(selectedYear, true);
    }

    public void setSelectedYear(int selectedYear, boolean smoothScroll) {
        setCurrentPosition(selectedYear - mStartYear, smoothScroll);
    }



    public int getSelectedYear() {
    	return mSelectedYear;
    }

    public void setOnYearSelectedListener(OnYearSelectedListener onYearSelectedListener) {
        mOnYearSelectedListener = onYearSelectedListener;
    }

    public String getYearStr() {
        return getDataList().get(getCurrentPosition());
    }

    public interface OnYearSelectedListener {
        void onYearSelected(int year);
    }

}
