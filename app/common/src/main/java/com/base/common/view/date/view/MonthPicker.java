package com.base.common.view.date.view;

import android.content.Context;
import android.util.AttributeSet;

import androidx.annotation.Nullable;

import com.base.common.view.date.model.ChinaDate;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * 月份选择器
 */
public class MonthPicker extends WheelPicker<String> {

    private static final int DEFAULT_MONTH = 1;
    private static int MAX_MONTH = 12;
    private static int MIN_MONTH = 1;

    private int mSelectedMonth;
    private int mode;
    private OnMonthSelectedListener mOnMonthSelectedListener;
    private int mYear;
    private long mMaxDate, mMinDate;
    private int mMaxYear, mMinYear;
    private int mMinMonth = MIN_MONTH;
    private int mMaxMonth = MAX_MONTH;
    private List<String> list = new ArrayList<>();
    public MonthPicker(Context context) {
        this(context, null);
    }

    public MonthPicker(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public MonthPicker(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
	    setItemMaximumWidthText("00月");
//	    NumberFormat numberFormat = NumberFormat.getNumberInstance();
//	    numberFormat.setMinimumIntegerDigits(2);
//	    setDataFormat(numberFormat);
		Calendar.getInstance().clear();
        mSelectedMonth = DEFAULT_MONTH;
        mYear = Calendar.getInstance().get(Calendar.YEAR);
        updateMonth();
        setSelectedMonth(mSelectedMonth, false);
        setOnWheelChangeListener(new OnWheelChangeListener<String>() {
	        @Override
	        public void onWheelSelected(String item, int position) {
//                String s = list.get(position);
//                if(s == null){
//                    return;
//                }
                mSelectedMonth = position +1;
//                mSelectedMonth = Integer.parseInt(s.substring(0,s.length()-1));
		        if (mOnMonthSelectedListener != null) {
		        	mOnMonthSelectedListener.onMonthSelected(mSelectedMonth);
		        }
	        }
        });
    }

    public void updateMonth() {
        if(list.isEmpty()) {
            for (int i = mMinMonth; i <= mMaxMonth; i++) {
                list.add(i + "月");
            }
        }
        setDataList(list);
    }

    private boolean isLunarType;
    public void setLunarType(boolean isLunarType){
        this.isLunarType = isLunarType;
    }



    public void setMaxDate(long date) {
        mMaxDate = date;
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(date);
        mMaxYear = calendar.get(Calendar.YEAR);
    }

    public void setMinDate(long date) {
        mMinDate = date;
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(date);
        mMinYear = calendar.get(Calendar.YEAR);
    }


    public void setYear(int year) {
        // 公历
        mYear = year;
        if(!isLunarType){
            mMinMonth = MIN_MONTH;
            mMaxMonth = MAX_MONTH;
            if (mMaxDate != 0 && mMaxYear == year) {
                Calendar calendar = Calendar.getInstance();
                calendar.setTimeInMillis(mMaxDate);
                mMaxMonth = calendar.get(Calendar.MONTH) + 1;

            }
            if (mMinDate != 0 && mMinYear == year) {
                Calendar calendar = Calendar.getInstance();
                calendar.setTimeInMillis(mMinDate);
                mMinMonth = calendar.get(Calendar.MONTH) + 1;

            }
            // 公历 切换年份时  月份不会动
//            updateMonth();
//            if (mSelectedMonth > mMaxMonth) {
//                setSelectedMonth(mMaxMonth, false);
//            } else if (mSelectedMonth < mMinMonth) {
//                setSelectedMonth(mMinMonth, false);
//            } else {
//                setSelectedMonth(mSelectedMonth, false);
//            }

            // 农历
        }else{
            ArrayList<String> months = ChinaDate.getMonths(mYear);
            if(mSelectedMonth > months.size()){
                mSelectedMonth = months.size();
                setSelectedMonth(mSelectedMonth);
            }
            setDataList(months);
        }
    }

    public int getSelectedMonth() {
        return mSelectedMonth;
    }

    public void setSelectedMonth(int selectedMonth) {
        setSelectedMonth(selectedMonth, true);
    }

    public void setSelectedMonthNoMove(int mSelectedMonth){
        this.mSelectedMonth = mSelectedMonth;
    }


    public void setSelectedMonth(int selectedMonth, boolean smoothScroll) {

        setCurrentPosition(selectedMonth - mMinMonth, smoothScroll);
    }

	public void setOnMonthSelectedListener(OnMonthSelectedListener onMonthSelectedListener) {
		mOnMonthSelectedListener = onMonthSelectedListener;
	}

    public String getMonthStr() {
        int index = getCurrentPosition();
        if(index >= getDataList().size()){
            index = getDataList().size()-1;
        }
        return getDataList().get(index);
    }

    public interface OnMonthSelectedListener {
    	void onMonthSelected(int month);
    }

}
