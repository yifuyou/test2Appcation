package com.base.common.view.date.view;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.databinding.DataBindingUtil;

import com.base.common.R;
import com.base.common.databinding.DatePickerTimeDfBinding;
import com.base.common.view.base.BaseDialogFragment;
import com.base.common.viewmodel.BaseViewModel;

import java.util.Calendar;

public class DatePickerTimeDialogFragment extends BaseDialogFragment<DatePickerTimeDfBinding, BaseViewModel> {


    public static DatePickerTimeDialogFragment getInstance() {
        return new DatePickerTimeDialogFragment();
    }


    @Override
    protected DatePickerTimeDfBinding initDataBinding(LayoutInflater inflater, ViewGroup container) {
        return DataBindingUtil.inflate(inflater, R.layout.date_picker_time_df, container, false);
    }


    private int hourTime = -1, minuteTime = -1;

    private HourAndMinutePicker.OnTimeSelectedListener mOnTimeSelectedListener;

    @Override
    public void initView() {
        super.initView();
        if (hourTime <= 0) {
            setSelectedDate(System.currentTimeMillis());
        }
        setSelectedDate();


        binding.hamPicker.setOnTimeSelectedListener(new HourAndMinutePicker.OnTimeSelectedListener() {
            @Override
            public void onTimeSelected(int hour, int minute) {
                hourTime = hour;
                minuteTime = minute;
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
                if (mOnTimeSelectedListener != null) {
                    mOnTimeSelectedListener.onTimeSelected(hourTime, minuteTime);
                }
                dismiss();
            }
        });

    }

    public void setSelectedDate(long date) {
        Calendar ca = Calendar.getInstance();
        ca.setTimeInMillis(date);
        hourTime = ca.get(Calendar.HOUR_OF_DAY);
        minuteTime = ca.get(Calendar.MINUTE);
    }

    private void setSelectedDate() {
        binding.hamPicker.setTime(hourTime, minuteTime);
    }


    public void setOnTimeSelectedListener(HourAndMinutePicker.OnTimeSelectedListener mOnTimeSelectedListener) {
        this.mOnTimeSelectedListener = mOnTimeSelectedListener;
    }
}
