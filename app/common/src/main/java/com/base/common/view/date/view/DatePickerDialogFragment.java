package com.base.common.view.date.view;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.databinding.DataBindingUtil;

import com.base.common.R;
import com.base.common.databinding.DatePickerDfBinding;
import com.base.common.utils.DateUtils;
import com.base.common.view.base.BaseDialogFragment;
import com.base.common.viewmodel.BaseViewModel;

import java.util.Calendar;


public class DatePickerDialogFragment extends BaseDialogFragment<DatePickerDfBinding, BaseViewModel> {


    public static DatePickerDialogFragment getInstance() {
        return new DatePickerDialogFragment();
    }


    private int mSelectedYear = -1, mSelectedMonth = -1, mSelectedDay = -1;

    private OnDateChooseListener mOnDateChooseListener;
    private OnDateChooseInterface mOnDateChooseInterface;

    private int hourtime = 0;

    private boolean isLunar = false;//是否阴历?

    private boolean isShowDay = true;//是否显示天

    private boolean isShowTime = false;//是否显示时间? 时 分

    private boolean isShowMinute = false;//是否显分钟

    public void setOnDateChooseInterface(OnDateChooseInterface mOnDateChooseInterface) {
        this.mOnDateChooseInterface = mOnDateChooseInterface;
    }

    public void setOnDateChooseListener(OnDateChooseListener onDateChooseListener) {
        mOnDateChooseListener = onDateChooseListener;
    }


    @Override
    protected DatePickerDfBinding initDataBinding(LayoutInflater inflater, ViewGroup container) {
        return DataBindingUtil.inflate(inflater, R.layout.date_picker_df, container, false);
    }


    @Override
    public void initView() {
        super.initView();
        setLunarCalendar(isLunar);
        if (mSelectedYear <= 0) {
            setSelectedDate(System.currentTimeMillis());
        }
        setSelectedDate();

        if (isShowTime) {
            binding.hourPickerDialog.setVisibility(View.VISIBLE);
            if (isShowMinute) {
                binding.minutePickDialog.setVisibility(View.VISIBLE);
            } else {
                binding.minutePickDialog.setVisibility(View.GONE);
            }
        } else {
            binding.hourPickerDialog.setVisibility(View.GONE);
            binding.minutePickDialog.setVisibility(View.GONE);
        }

        binding.dayPickerDialog.setDayPickerVisibility(isShowDay);

        binding.hourPickerDialog.setOnHourSelectedListener(new HourPicker.OnHourSelectedListener() {
            @Override
            public void onHourSelected(int hour) {
                hourtime = hour;
            }
        });

        binding.btnDialogDateCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });


        binding.btnDialogDateDecide.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mOnDateChooseListener != null) {
                    mOnDateChooseListener.onDateChoose(binding.dayPickerDialog.getYear(), binding.dayPickerDialog.getMonth(), binding.dayPickerDialog.getDay());
                }
                if (mOnDateChooseInterface != null) {
                    Calendar ca = Calendar.getInstance();
                    ca.set(binding.dayPickerDialog.getYear(), binding.dayPickerDialog.getMonth() - 1, binding.dayPickerDialog.getDay(), hourtime, 0);

                    mOnDateChooseInterface.onDateChoose(ca.getTimeInMillis());
                }
                dismiss();
            }
        });

    }

    public void setLunarCalendar(boolean isLunar) {
        binding.dayPickerDialog.setLunarCalendar(isLunar);
    }

    public void setLunar(boolean lunar) {
        isLunar = lunar;
    }

    public void setSelectedDate(int year, int month, int day) {
        mSelectedYear = year;
        mSelectedMonth = month;
        mSelectedDay = day;
//        setSelectedDate();
    }

    /**
     * @param data 2019-10-11
     */
    public void setSelectedDate(String data) {
        setSelectedDate(DateUtils.dateStringToLong(data));
    }

    public void setSelectedDate(long date) {
        Calendar ca = Calendar.getInstance();
        ca.setTimeInMillis(date);

        mSelectedYear = ca.get(Calendar.YEAR);
        mSelectedMonth = ca.get(Calendar.MONTH) + 1;
        mSelectedDay = ca.get(Calendar.DAY_OF_MONTH);
        hourtime = ca.get(Calendar.HOUR_OF_DAY);

//        setSelectedDate();
    }

    private void setSelectedDate() {
        binding.dayPickerDialog.setDate(mSelectedYear, mSelectedMonth, mSelectedDay, false);
        binding.hourPickerDialog.setSelectedHour(hourtime);
    }

    public void setShowTime(boolean showTime) {
        isShowTime = showTime;
    }

    public void setShowMinute(boolean showMinute) {
        isShowMinute = showMinute;
    }

    public void setShowDay(boolean showDay) {
        isShowDay = showDay;
    }

    public interface OnDateChooseListener {
        void onDateChoose(int year, int month, int day);
    }

    public interface OnDateChooseInterface {
        void onDateChoose(long date);
    }

}
