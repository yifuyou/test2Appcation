package com.base.common.view.date.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.LinearLayout;

import androidx.annotation.ColorInt;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.base.common.R;
import com.base.common.view.date.tools.LunarCalendar;
import com.base.common.view.date.model.ChinaDate;

import java.text.DateFormat;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;


@SuppressWarnings("unused")
public class DatePicker extends LinearLayout implements YearPicker.OnYearSelectedListener,
        MonthPicker.OnMonthSelectedListener, DayPicker.OnDaySelectedListener {

    private YearPicker mYearPicker;
    private MonthPicker mMonthPicker;
    private DayPicker mDayPicker;
    private boolean isLunarCalendar;
    private Long mMaxDate;
    private Long mMinDate;
    private OnDateSelectedListener mOnDateSelectedListener;
    private final int startYear = 1920;
    private final int endYear = 2050;

    /**
     * Instantiates a new Date picker.
     *
     * @param context the context
     */
    public DatePicker(Context context) {
        this(context, null);
    }

    /**
     * Instantiates a new Date picker.
     *
     * @param context the context
     * @param attrs   the attrs
     */
    public DatePicker(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    /**
     * Instantiates a new Date picker.
     *
     * @param context      the context
     * @param attrs        the attrs
     * @param defStyleAttr the def style attr
     */
    public DatePicker(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        LayoutInflater.from(context).inflate(R.layout.date_picker_layout, this);
        initChild();
        initAttrs(context, attrs);
        mYearPicker.setBackgroundDrawable(getBackground());
        mMonthPicker.setBackgroundDrawable(getBackground());
        mDayPicker.setBackgroundDrawable(getBackground());
    }

    private void initAttrs(Context context, @Nullable AttributeSet attrs) {
        if (attrs == null) {
            return;
        }
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.DatePicker);
        int textSize = a.getDimensionPixelSize(R.styleable.DatePicker_itemTextSize,
                (int) getResources().getDimension(R.dimen.font_14));

        int textColor = a.getColor(R.styleable.DatePicker_itemTextColor,
                Color.BLACK);
        boolean isTextGradual = a.getBoolean(R.styleable.DatePicker_textGradual, true);
        boolean isCyclic = a.getBoolean(R.styleable.DatePicker_wheelCyclic, false);
        int halfVisibleItemCount = a.getInteger(R.styleable.DatePicker_halfVisibleItemCount, 2);
        int selectedItemTextColor = a.getColor(R.styleable.DatePicker_selectedTextColor,
                getResources().getColor(R.color.datepicker_selectedTextColor));
        int selectedItemTextSize = a.getDimensionPixelSize(R.styleable.DatePicker_selectedTextSize,
                (int) getResources().getDimension(R.dimen.font_20));
        int itemWidthSpace = a.getDimensionPixelSize(R.styleable.DatePicker_itemWidthSpace,
                (int) getResources().getDimension(R.dimen.dp_32));
        int itemHeightSpace = a.getDimensionPixelSize(R.styleable.DatePicker_itemHeightSpace,
                (int) getResources().getDimension(R.dimen.dp_16));
        boolean isZoomInSelectedItem = a.getBoolean(R.styleable.DatePicker_zoomInSelectedItem, true);
        boolean isShowCurtain = a.getBoolean(R.styleable.DatePicker_wheelCurtain, true);
        int curtainColor = a.getColor(R.styleable.DatePicker_wheelCurtainColor, Color.WHITE);
        boolean isShowCurtainBorder = a.getBoolean(R.styleable.DatePicker_wheelCurtainBorder, true);
        int curtainBorderColor = a.getColor(R.styleable.DatePicker_wheelCurtainBorderColor,
                getResources().getColor(R.color.white));
        a.recycle();

        setTextSize(textSize);
        setTextColor(textColor);
        setTextGradual(isTextGradual);
        setCyclic(isCyclic);
        setHalfVisibleItemCount(halfVisibleItemCount);
        setSelectedItemTextColor(selectedItemTextColor);
        setSelectedItemTextSize(selectedItemTextSize);
        setItemWidthSpace(itemWidthSpace);
        setItemHeightSpace(itemHeightSpace);
        setZoomInSelectedItem(isZoomInSelectedItem);
        setShowCurtain(isShowCurtain);
        setCurtainColor(curtainColor);
        setShowCurtainBorder(isShowCurtainBorder);
        setCurtainBorderColor(curtainBorderColor);
    }

    private void initChild() {
        mYearPicker = findViewById(R.id.yearPicker_layout_date);
        mYearPicker.setOnYearSelectedListener(this);
        mMonthPicker = findViewById(R.id.monthPicker_layout_date);
        mMonthPicker.setOnMonthSelectedListener(this);
        mDayPicker = findViewById(R.id.dayPicker_layout_date);
        mDayPicker.setOnDaySelectedListener(this);
    }


    public void setLunarCalendar(boolean isLunar) {
        // ?????????????????????????????????

        try {
            int year, month, day;
            Calendar calendar = Calendar.getInstance();
            DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            calendar.setTime(dateFormat.parse(getTime()));
            year = calendar.get(Calendar.YEAR);
            month = calendar.get(Calendar.MONTH);
            day = calendar.get(Calendar.DAY_OF_MONTH);
            this.isLunarCalendar = isLunar;
            setChildType(isLunar);
            if (isLunarCalendar) {
                int[] lunar = LunarCalendar.solarToLunar(year, month + 1, day);
                setLunar(lunar[0], lunar[1] - 1, lunar[2], lunar[3] == 1);
            } else {
                setSolar(year, month, day);
            }

        } catch (ParseException e) {
            e.printStackTrace();
        }


    }

    private void setChildType(boolean isLunarCalendar) {
        mYearPicker.setLunarType(isLunarCalendar);
        mMonthPicker.setLunarType(isLunarCalendar);
        mDayPicker.setLunarType(isLunarCalendar);
    }

    public boolean isLunarType() {
        return isLunarCalendar;
    }

    private void setSolar(int year, int month, int day) {
        mYearPicker.updateYear();
        mYearPicker.setCurrentPositionNoSmooth(year - startYear);
        mMonthPicker.updateMonth();
        mMonthPicker.setCurrentPositionNoSmooth(month);
        mDayPicker.updateDay();
        mDayPicker.setCurrentPositionNoSmooth(day - 1);
    }

    private void setLunar(int year, final int month, int day, boolean isLeap) {
        ArrayList<String> years = ChinaDate.getUpYears(startYear, endYear);
        mYearPicker.setDataList(years);
        mYearPicker.setCurrentPositionNoSmooth(year - startYear);
        ArrayList<String> months = ChinaDate.getMonths(year);
        mMonthPicker.setDataList(months);
        int leapMonth = ChinaDate.leapMonth(year);
        //?????????????????????????????????
        if (leapMonth != 0 && (month > leapMonth - 1 || isLeap)) {
            mMonthPicker.setCurrentPositionNoSmooth(month + 1);
        } else {
            mMonthPicker.setCurrentPositionNoSmooth(month);
        }
        if (ChinaDate.leapMonth(year) == 0) {
            mDayPicker.setDataList(ChinaDate.getLunarDays(ChinaDate.monthDays(year, month)));
        } else {
            mDayPicker.setDataList(ChinaDate.getLunarDays(ChinaDate.leapDays(year)));
        }
        mDayPicker.setCurrentPositionNoSmooth(day - 1);
    }

    private String getTime() {
        if (isLunarCalendar) {
            // ??????  ?????????????????????????????????
            StringBuilder stringBuilder = new StringBuilder();
            int pos = mYearPicker.getCurrentPosition();
            int month = 1;
            boolean isLeapMonth = false;
            // ????????????
            if (ChinaDate.leapMonth(pos + startYear) == 0) {
                month = mMonthPicker.getCurrentPosition() + 1;
            } else {
                //  ?????????
                //  ????????????????????????????????????
                if ((mMonthPicker.getCurrentPosition() + 1) - ChinaDate.leapMonth(pos + startYear) <= 0) {
                    month = mMonthPicker.getCurrentPosition() + 1;
                    //  ??????????????????????????????
                } else if ((mMonthPicker.getCurrentPosition() + 1) - ChinaDate.leapMonth(pos + startYear) == 1) {
                    month = mMonthPicker.getCurrentPosition();
                    isLeapMonth = true;
                    //  ?????????????????????????????????
                } else {
                    month = mMonthPicker.getCurrentPosition();
                }
            }
            int day = mDayPicker.getCurrentPosition() + 1;
            int[] solar = LunarCalendar.lunarToSolar(pos + startYear, month, day, isLeapMonth);
            stringBuilder.append(solar[0]).append("-").append(solar[1]).append("-").append(solar[2]);
            return stringBuilder.toString();
        } else {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append(mYearPicker.getCurrentPosition() + startYear).append("-").append(mMonthPicker.getCurrentPosition() + 1).append("-").append(mDayPicker.getCurrentPosition() + 1);
            return stringBuilder.toString();
        }
    }

    /**
     * ??????????????????
     *
     * @return
     */
    public String getLunarTime() {
        String str = null;
        if (isLunarCalendar) {
            str = getLunarStr();
        } else {
            try {
                int year, month, day;
                String yearStr, monthStr, dayStr;
                Calendar calendar = Calendar.getInstance();
                DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
                String time = getTime();
                calendar.setTime(dateFormat.parse(getTime()));
                year = calendar.get(Calendar.YEAR);
                month = calendar.get(Calendar.MONTH);
                day = calendar.get(Calendar.DAY_OF_MONTH);
                int[] lunar = LunarCalendar.solarToLunar(year, month + 1, day);
                year = lunar[0];
                month = lunar[1] - 1;
                day = lunar[2];
                boolean isLeap = lunar[3] == 1;
                ArrayList<String> years = ChinaDate.getUpYears(startYear, endYear);
                yearStr = years.get(year - startYear);
                ArrayList<String> months = ChinaDate.getMonths(year);
                int leapMonth = ChinaDate.leapMonth(year);
                //?????????????????????????????????
                if (leapMonth != 0 && (month > leapMonth - 1 || isLeap)) {
                    monthStr = months.get(month + 1);
                } else {
                    monthStr = months.get(month);
                }
                if (ChinaDate.leapMonth(year) == 0) {
                    ArrayList<String> lunarDays = ChinaDate.getLunarDays(ChinaDate.monthDays(year, month));
                    dayStr = lunarDays.get(day - 1);
                } else {
                    ArrayList<String> lunarDays = ChinaDate.getLunarDays(ChinaDate.leapDays(year));
                    dayStr = lunarDays.get(day - 1);
                }
                str = yearStr + "-" + monthStr + "-" + dayStr;
            } catch (ParseException e) {
                e.printStackTrace();
            }

        }

        return str;
    }

    /**
     * ??????????????????
     *
     * @return
     */
    public String getGregorianTime() {
        String str = "";
        if (!isLunarCalendar) {
            str = getDataFormat();
        } else {
            try {
                Calendar calendar = Calendar.getInstance();
                DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
                String time = getTime();
                calendar.setTime(dateFormat.parse(getTime()));
                int year, month, day;
                year = calendar.get(Calendar.YEAR);
                month = calendar.get(Calendar.MONTH);
                day = calendar.get(Calendar.DAY_OF_MONTH);
                NumberFormat mNumberInstance = NumberFormat.getNumberInstance();
                mNumberInstance.setMinimumIntegerDigits(2);
                str = year + "-" + mNumberInstance.format(month + 1) + "-" + mNumberInstance.format(day);
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        return str;
    }


    public void setDayPickerVisibility(boolean isShow) {
        mDayPicker.setVisibility(isShow ? VISIBLE : GONE);
    }

    @Override
    public void setBackgroundColor(int color) {
        super.setBackgroundColor(color);
        if (mYearPicker != null && mMonthPicker != null && mDayPicker != null) {
            mYearPicker.setBackgroundColor(color);
            mMonthPicker.setBackgroundColor(color);
            mDayPicker.setBackgroundColor(color);
        }
    }

    @Override
    public void setBackgroundResource(int resid) {
        super.setBackgroundResource(resid);
        if (mYearPicker != null && mMonthPicker != null && mDayPicker != null) {
            mYearPicker.setBackgroundResource(resid);
            mMonthPicker.setBackgroundResource(resid);
            mDayPicker.setBackgroundResource(resid);
        }
    }

    @Override
    public void setBackgroundDrawable(Drawable background) {
        super.setBackgroundDrawable(background);
        if (mYearPicker != null && mMonthPicker != null && mDayPicker != null) {
            mYearPicker.setBackgroundDrawable(background);
            mMonthPicker.setBackgroundDrawable(background);
            mDayPicker.setBackgroundDrawable(background);
        }
    }

    private void onDateSelected() {
        if (mOnDateSelectedListener != null) {
            mOnDateSelectedListener.onDateSelected(getYear(), getYearStr(),
                    getMonth(), getMonthStr(), getDay(), getDayStr(), isLunarCalendar);
        }
    }

    public String getLunarStr() {
        return getYearStr() + getMonthStr() + getDayStr();
    }

    private String getDayStr() {
        return mDayPicker.getDayStr();
    }

    private String getMonthStr() {
        return mMonthPicker.getMonthStr();
    }

    private String getYearStr() {
        return mYearPicker.getYearStr();
    }


    @Override
    public void onMonthSelected(int month) {
        mDayPicker.setMonth(mYearPicker.getCurrentPosition() + startYear, month);
        onDateSelected();
    }

    @Override
    public void onDaySelected(int day) {
        onDateSelected();
    }


    @Override
    public void onYearSelected(int year) {
        int month = getMonth();
        mMonthPicker.setYear(year);
        mDayPicker.setMonth(year, month);
        onDateSelected();
    }

    /**
     * Sets date.
     *
     * @param year  the year
     * @param month the month
     * @param day   the day
     */
    public void setDate(int year, int month, int day) {
        setDate(year, month, day, true);
    }

    /**
     * Sets date.
     *
     * @param year         the year
     * @param month        the month
     * @param day          the day
     * @param smoothScroll the smooth scroll
     */
    public void setDate(int year, int month, int day, boolean smoothScroll) {
        mYearPicker.setSelectedYear(year, smoothScroll);
        mMonthPicker.setSelectedMonth(month, smoothScroll);
        mDayPicker.setSelectedDay(day, smoothScroll);
    }

    public void setMaxDate(long date) {
        setCyclic(false);
        mMaxDate = date;
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(date);
        mYearPicker.setEndYear(calendar.get(Calendar.YEAR));
        mMonthPicker.setMaxDate(date);
        mDayPicker.setMaxDate(date);
        mMonthPicker.setYear(mYearPicker.getSelectedYear());
        mDayPicker.setMonth(mYearPicker.getSelectedYear(), mMonthPicker.getSelectedMonth());
    }

    public void setMinDate(long date) {
        setCyclic(false);
        mMinDate = date;
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(date);
        mYearPicker.setStartYear(calendar.get(Calendar.YEAR));
        mMonthPicker.setMinDate(date);
        mDayPicker.setMinDate(date);
        mMonthPicker.setYear(mYearPicker.getSelectedYear());
        mDayPicker.setMonth(mYearPicker.getSelectedYear(), mMonthPicker.getSelectedMonth());
    }

    /**
     * Gets date.
     *
     * @return the date
     */
    public String getDate() {
        DateFormat format = SimpleDateFormat.getDateInstance();
        return getDate(format);
    }

    public String getDataFormat() {
        NumberFormat mNumberInstance = NumberFormat.getNumberInstance();
        mNumberInstance.setMinimumIntegerDigits(2);
        return getYear() + "-" + mNumberInstance.format(getMonth()) + "-" + mNumberInstance.format(getDay());
    }

    /**
     * Gets date.
     *
     * @param dateFormat the date format
     * @return the date
     */
    public String getDate(@NonNull DateFormat dateFormat) {
        int year, month, day;
        year = getYear();
        month = getMonth();
        day = getDay();
        Calendar calendar = Calendar.getInstance();
        calendar.set(year, month - 1, day);

        return dateFormat.format(calendar.getTime());
    }

    /**
     * Gets year.
     *
     * @return the year
     */
    public int getYear() {
        return mYearPicker.getSelectedYear();
    }

    /**
     * Gets month.
     *
     * @return the month
     */
    public int getMonth() {
        return mMonthPicker.getSelectedMonth();
    }

    /**
     * Gets day.
     *
     * @return the day
     */
    public int getDay() {
        return mDayPicker.getSelectedDay();
    }

    /**
     * Gets year picker.
     *
     * @return the year picker
     */
    public YearPicker getYearPicker() {
        return mYearPicker;
    }

    /**
     * Gets month picker.
     *
     * @return the month picker
     */
    public MonthPicker getMonthPicker() {
        return mMonthPicker;
    }

    /**
     * Gets day picker.
     *
     * @return the day picker
     */
    public DayPicker getDayPicker() {
        return mDayPicker;
    }

    /**
     * ???????????????????????????
     *
     * @param textColor ????????????
     */
    public void setTextColor(@ColorInt int textColor) {
        mDayPicker.setTextColor(textColor);
        mMonthPicker.setTextColor(textColor);
        mYearPicker.setTextColor(textColor);
    }

    /**
     * ???????????????????????????
     *
     * @param textSize ????????????
     */
    public void setTextSize(int textSize) {
        mDayPicker.setTextSize(textSize);
        mMonthPicker.setTextSize(textSize);
        mYearPicker.setTextSize(textSize);
    }

    /**
     * ????????????????????????????????????
     *
     * @param selectedItemTextColor ????????????
     */
    public void setSelectedItemTextColor(@ColorInt int selectedItemTextColor) {
        mDayPicker.setSelectedItemTextColor(selectedItemTextColor);
        mMonthPicker.setSelectedItemTextColor(selectedItemTextColor);
        mYearPicker.setSelectedItemTextColor(selectedItemTextColor);
    }

    /**
     * ????????????????????????????????????
     *
     * @param selectedItemTextSize ????????????
     */
    public void setSelectedItemTextSize(int selectedItemTextSize) {
        mDayPicker.setSelectedItemTextSize(selectedItemTextSize);
        mMonthPicker.setSelectedItemTextSize(selectedItemTextSize);
        mYearPicker.setSelectedItemTextSize(selectedItemTextSize);
    }


    /**
     * ??????????????????????????????????????????
     * ?????????????????????????????????,????????????????????????itemCount = mHalfVisibleItemCount * 2 + 1
     *
     * @param halfVisibleItemCount ??????????????????
     */
    public void setHalfVisibleItemCount(int halfVisibleItemCount) {
        mDayPicker.setHalfVisibleItemCount(halfVisibleItemCount);
        mMonthPicker.setHalfVisibleItemCount(halfVisibleItemCount);
        mYearPicker.setHalfVisibleItemCount(halfVisibleItemCount);
    }

    /**
     * Sets item width space.
     *
     * @param itemWidthSpace the item width space
     */
    public void setItemWidthSpace(int itemWidthSpace) {
        mDayPicker.setItemWidthSpace(itemWidthSpace);
        mMonthPicker.setItemWidthSpace(itemWidthSpace);
        mYearPicker.setItemWidthSpace(itemWidthSpace);
    }

    /**
     * ????????????Item???????????????
     *
     * @param itemHeightSpace ?????????
     */
    public void setItemHeightSpace(int itemHeightSpace) {
        mDayPicker.setItemHeightSpace(itemHeightSpace);
        mMonthPicker.setItemHeightSpace(itemHeightSpace);
        mYearPicker.setItemHeightSpace(itemHeightSpace);
    }


    /**
     * Set zoom in center item.
     *
     * @param zoomInSelectedItem the zoom in center item
     */
    public void setZoomInSelectedItem(boolean zoomInSelectedItem) {
        mDayPicker.setZoomInSelectedItem(zoomInSelectedItem);
        mMonthPicker.setZoomInSelectedItem(zoomInSelectedItem);
        mYearPicker.setZoomInSelectedItem(zoomInSelectedItem);
    }

    /**
     * ???????????????????????????
     * set wheel cyclic
     *
     * @param cyclic ????????????????????????
     */
    public void setCyclic(boolean cyclic) {
        mDayPicker.setCyclic(cyclic);
        mMonthPicker.setCyclic(cyclic);
        mYearPicker.setCyclic(cyclic);
    }

    /**
     * ?????????????????????????????????????????????
     * Set the text color gradient
     *
     * @param textGradual ????????????
     */
    public void setTextGradual(boolean textGradual) {
        mDayPicker.setTextGradual(textGradual);
        mMonthPicker.setTextGradual(textGradual);
        mYearPicker.setTextGradual(textGradual);
    }


    /**
     * ????????????Item?????????????????????
     * set the center item curtain cover
     *
     * @param showCurtain ???????????????
     */
    public void setShowCurtain(boolean showCurtain) {
        mDayPicker.setShowCurtain(showCurtain);
        mMonthPicker.setShowCurtain(showCurtain);
        mYearPicker.setShowCurtain(showCurtain);
    }

    /**
     * ??????????????????
     * set curtain color
     *
     * @param curtainColor ????????????
     */
    public void setCurtainColor(@ColorInt int curtainColor) {
        mDayPicker.setCurtainColor(curtainColor);
        mMonthPicker.setCurtainColor(curtainColor);
        mYearPicker.setCurtainColor(curtainColor);
    }

    /**
     * ??????????????????????????????
     * set curtain border
     *
     * @param showCurtainBorder ?????????????????????
     */
    public void setShowCurtainBorder(boolean showCurtainBorder) {
        mDayPicker.setShowCurtainBorder(showCurtainBorder);
        mMonthPicker.setShowCurtainBorder(showCurtainBorder);
        mYearPicker.setShowCurtainBorder(showCurtainBorder);
    }

    /**
     * ?????????????????????
     * curtain border color
     *
     * @param curtainBorderColor ??????????????????
     */
    public void setCurtainBorderColor(@ColorInt int curtainBorderColor) {
        mDayPicker.setCurtainBorderColor(curtainBorderColor);
        mMonthPicker.setCurtainBorderColor(curtainBorderColor);
        mYearPicker.setCurtainBorderColor(curtainBorderColor);
    }

    /**
     * ?????????????????????????????????
     * set indicator text
     *
     * @param yearText  ??????????????????
     * @param monthText ??????????????????
     * @param dayText   ??????????????????
     */
    public void setIndicatorText(String yearText, String monthText, String dayText) {
        mYearPicker.setIndicatorText(yearText);
        mMonthPicker.setIndicatorText(monthText);
        mDayPicker.setIndicatorText(dayText);
    }

    /**
     * ??????????????????????????????
     * set indicator text color
     *
     * @param textColor ????????????
     */
    public void setIndicatorTextColor(@ColorInt int textColor) {
        mYearPicker.setIndicatorTextColor(textColor);
        mMonthPicker.setIndicatorTextColor(textColor);
        mDayPicker.setIndicatorTextColor(textColor);
    }

    /**
     * ??????????????????????????????
     * indicator text size
     *
     * @param textSize ????????????
     */
    public void setIndicatorTextSize(int textSize) {
        mYearPicker.setTextSize(textSize);
        mMonthPicker.setTextSize(textSize);
        mDayPicker.setTextSize(textSize);
    }

    /**
     * Sets on date selected listener.
     *
     * @param onDateSelectedListener the on date selected listener
     */
    public void setOnDateSelectedListener(OnDateSelectedListener onDateSelectedListener) {
        mOnDateSelectedListener = onDateSelectedListener;
    }


    /**
     * The interface On date selected listener.
     */
    public interface OnDateSelectedListener {
        /**
         * On date selected.
         *
         * @param year  the year
         * @param month the month
         * @param day   the day
         */
        void onDateSelected(int year, int month, int day);

        void onDateSelected(int year, int month, int day, int hour, int minute);

        void onDateSelected(int year, String yearStr, int month, String monthStr, int day, String dayStr, boolean isLunarCalendar);
    }
}
