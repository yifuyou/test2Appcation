package com.base.common.view.date.view;

import android.content.Context;
import android.util.AttributeSet;

import androidx.annotation.Nullable;


import java.util.ArrayList;
import java.util.List;


public class MinutePicker extends WheelPicker<String> {
    private OnMinuteSelectedListener mOnMinuteSelectedListener;

    public MinutePicker(Context context) {
        this(context, null);
    }

    public MinutePicker(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public MinutePicker(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setItemMaximumWidthText("00分");
        updateMinute();
        setOnWheelChangeListener(new OnWheelChangeListener<String>() {
            @Override
            public void onWheelSelected(String item, int position) {
                if (mOnMinuteSelectedListener != null) {
                    mOnMinuteSelectedListener.onMinuteSelected(position);
                }
            }
        });
    }

    private void updateMinute() {
        List<String> list = new ArrayList<>();
        for (int i = 0; i < 60; i++) {
            list.add(i+"分");
        }
        setDataList(list);
    }

    public void setSelectedMinute(int hour) {
        setSelectedMinute(hour, true);
    }

    public void setSelectedMinute(int hour, boolean smootScroll) {
        setCurrentPosition(hour, smootScroll);
    }

    public void setOnMinuteSelectedListener(OnMinuteSelectedListener onMinuteSelectedListener) {
        mOnMinuteSelectedListener = onMinuteSelectedListener;
    }

    public interface OnMinuteSelectedListener {
        void onMinuteSelected(int hour);
    }
}
