package com.base.common.view.date.view;

import android.content.Context;
import android.util.AttributeSet;

import androidx.annotation.Nullable;

import com.base.common.view.date.model.ChinaDate;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * 日期选择
 */
public class DayPicker extends WheelPicker<String> {

    private static final int DEFAULT_DAY = 1;
    private int mMinDay, mMaxDay;

    private int mSelectedDay;

    private int mYear, mMonth;
    private long mMaxDate, mMinDate;

    private OnDaySelectedListener mOnDaySelectedListener;

    public DayPicker(Context context) {
        this(context, null);
    }

    public DayPicker(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public DayPicker(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
	    setItemMaximumWidthText("00日");


        mMinDay = 1;
        mMaxDay = Calendar.getInstance().getActualMaximum(Calendar.DATE);
        updateDay();
//        mSelectedDay = Calendar.getInstance().get(Calendar.DATE);
        mSelectedDay = DEFAULT_DAY;
        setSelectedDay(mSelectedDay, false);
        setOnWheelChangeListener(new OnWheelChangeListener<String>() {
	        @Override
	        public void onWheelSelected(String item, int position) {
	            mSelectedDay = position + 1;
//	        	mSelectedDay = Integer.parseInt(item.substring(0,item.length()-1));
		        if (mOnDaySelectedListener != null) {
		        	mOnDaySelectedListener.onDaySelected(mSelectedDay);
		        }
	        }
        });
    }

    private boolean isLunarType;
    public void setLunarType(boolean isLunarType){
        this.isLunarType = isLunarType;
    }

    public void setMonth(int year, int month) {
        // 公历
        if(!isLunarType) {
            // 这一段在做了天数限制的情况下 先注释
            Calendar calendar = Calendar.getInstance();
//            calendar.setTimeInMillis(mMaxDate);
//            int maxYear = calendar.get(Calendar.YEAR);
//            int maxMonth = calendar.get(Calendar.MONTH) + 1;
//            int maxDay = calendar.get(Calendar.DAY_OF_MONTH);
//            if (maxYear == year && maxMonth == month) {
//                mMaxDay = maxDay;
//            } else {
                calendar.set(year, month - 1, 1);
                mMaxDay = calendar.getActualMaximum(Calendar.DATE);
//            }
//            calendar.setTimeInMillis(mMinDate);
//            int minYear = calendar.get(Calendar.YEAR);
//            int minMonth = calendar.get(Calendar.MONTH) + 1;
//            int minDay = calendar.get(Calendar.DAY_OF_MONTH);
//            if (minYear == year && minMonth == month) {
//                mMinDay = minDay;
//            } else {
                mMinDay = 1;
//            }
            updateDay();
            if (mSelectedDay < mMinDay) {
                setSelectedDay(mMinDay, false);
            } else if (mSelectedDay > mMaxDay) {
                setSelectedDay(mMaxDay, false);
            } else {
                setSelectedDay(mSelectedDay, false);
            }
        }else{
            // 农历
            int month_num = month -1;
            int currentIndex = getCurrentPosition();
            int maxItem = 29;
            // 当前年份有闰月 当前月份大于闰月  比如闰六月  闰六月-12月
            if (ChinaDate.leapMonth(year) != 0 && month_num > ChinaDate.leapMonth(year) - 1) {
                if (month_num == ChinaDate.leapMonth(year)) {
                    setDataList(ChinaDate.getLunarDays(ChinaDate.leapDays(year)));
                    maxItem = ChinaDate.leapDays(year);
                } else {
                    // 比闰月大的月份  month=7  index=7
                    setDataList(ChinaDate.getLunarDays(ChinaDate.monthDays(year, month_num)));
                    maxItem = ChinaDate.monthDays(year, month_num);
                }
            } else {
                // 没有闰月  或者有闰月但是在闰月之前的月份
                setDataList(ChinaDate.getLunarDays(ChinaDate.monthDays(year, month_num+1)));
                maxItem = ChinaDate.monthDays(year, month_num + 1);
            }

            if (currentIndex > maxItem - 1) {
                setCurrentPosition(maxItem - 1);
            }

        }
    }

    public int getSelectedDay() {
        return mSelectedDay;
    }

    public void setSelectedDay(int selectedDay) {
        setSelectedDay(selectedDay, true);
    }

    public void setSelectedDayNoMove(int mSelectedDay){
        this.mSelectedDay = mSelectedDay;
    }

    public void setSelectedDay(int selectedDay, boolean smoothScroll) {
        setCurrentPosition(selectedDay - mMinDay, smoothScroll);
    }

    public void setMaxDate(long date) {
        mMaxDate = date;
    }

    public void setMinDate(long date) {
        mMinDate = date;
    }

    public void setOnDaySelectedListener(OnDaySelectedListener onDaySelectedListener) {
		mOnDaySelectedListener = onDaySelectedListener;
	}

	public void updateDay() {
        List<String> list = new ArrayList<>();
        for (int i = mMinDay; i <= mMaxDay; i++) {
            list.add(i+"日");
        }
        setDataList(list);
    }

    public String getDayStr() {
        int index = getCurrentPosition();
        if(index >= getDataList().size()){
            index = getDataList().size() - 1;
        }
        return getDataList().get(index);
    }

    public interface OnDaySelectedListener {
    	void onDaySelected(int day);
    }
}
