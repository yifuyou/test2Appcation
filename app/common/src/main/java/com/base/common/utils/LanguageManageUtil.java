package com.base.common.utils;

import java.util.Locale;

import android.content.Context;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Build;
import android.os.LocaleList;
import android.util.DisplayMetrics;


/**
 * 系统语言 的Locale配置
 * <p>
 * Created By j on 2019/7/12
 */
public class LanguageManageUtil {

    private static final String TAG = "LocaleManageUtil";
//    private static final Locale systemCurrentLocal = Locale.getDefault();//默认 系统 语言

    private static final String TAG_LANGUAGE = "languageSelect";//当前选择的语言要


    public static final int LOCAL_CHINA = 1;//0跟随系统
    public static final int LOCAL_ENGLISH = 2;

    public static final int LOCAL_FAYU = 3;
    public static final int LOCAL_MALAIYU = 4;
    public static final int LOCAL_YDUYU = 5;
    public static final int LOCAL_TAIYU = 6;

    /**
     * 获取系统的locale
     *
     * @return Locale对象
     */
    public static Locale getSystemLocale() {
        // 默认 系统 语言
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            return UIUtils.getContext().getResources().getConfiguration().getLocales().get(0);
        } else {
            return UIUtils.getContext().getResources().getConfiguration().locale;
        }
//        return Locale.getDefault();
    }

    //当前系统的语言类型
    public static int getSystemLocaleType() {
        Locale locale = getSystemLocale();
        if (locale == Locale.CHINA) {
            return LOCAL_CHINA;
        } else if (locale == Locale.ENGLISH) {
            return LOCAL_ENGLISH;
        }
        return LOCAL_ENGLISH;
    }


    /**
     * 设置系统的Locale
     *
     * @param context
     * @return
     */
    public static Context setLocal(Context context, int LocalType) {
        if (LocalType == LOCAL_CHINA) {
            return updateResources(context, Locale.CHINA);
        } else if (LocalType == LOCAL_ENGLISH) {
            return updateResources(context, Locale.ENGLISH);
        } else {
            return updateResources(context, getSetLanguageLocale());
        }
    }

    /**
     * 保存当前语言类型
     *
     * @param select
     */
    public static void saveSelectLanguage(int select) {
        SPUtils.putInt(TAG_LANGUAGE, select);

        setApplicationLanguage();
    }

    /**
     * 设置系统的Locale
     *
     * @param context
     * @return
     */
    public static Context setLocal(Context context) {
        return updateResources(context, getSetLanguageLocale());
    }

    private static Context updateResources(Context context, Locale locale) {
        Locale.setDefault(locale);

        Resources res = context.getResources();
        Configuration config = new Configuration(res.getConfiguration());
        if (Build.VERSION.SDK_INT >= 17) {
            config.setLocale(locale);
            context = context.createConfigurationContext(config);
        } else {
            config.locale = locale;
            res.updateConfiguration(config, res.getDisplayMetrics());
        }
        return context;
    }

    /**
     * 获取选择的语言设置Locale  默认的是中文
     *
     * @return
     */
    public static Locale getSetLanguageLocale() {

        int languageType = getCurrentLanguageType();

        switch (languageType) {
            case 0:
                //系统语言
                return getSystemLocale();
            case 1:
                //中文
                return Locale.CHINA;
            /*
            case 2:
                return Locale.TAIWAN;
            */
            case 2:
                return Locale.ENGLISH;
            default:
                if (getSystemLocale().getLanguage().contains("zh") || getSystemLocale().getCountry().contains("CN")) {
                    return Locale.CHINA;
                } else return Locale.ENGLISH;

        }
    }

    public static int getCurrentLanguageType() {
        return SPUtils.getInt(TAG_LANGUAGE, 0);
    }

    public static String getSelectLanguage() {
           /*
            <string name="language_cn">中文</string>
            <string name="language_en">ENGLISH</string>
            <string name="language_auto">自動</string>
            <string name="language_traditional">繁体</string>
            */
        int languageType = getCurrentLanguageType();

        switch (languageType) {
            case 0:
                //context.getString(R.string.language_auto)
                return "自動";
            case 1:
                //context.getString(R.string.language_cn)
                return "中文";
            case 2:
                //context.getString(R.string.language_en)
                return "ENGLISH";
            //case 3:return context.getString(R.string.language_traditional);
            default:
                //context.getString(R.string.language_cn)
                return "中文";
        }
    }

    /**
     * 保存系统选择语言
     */
    public static void saveSystemCurrentLanguage() {
        Locale locale;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            locale = LocaleList.getDefault().get(0);
        } else {
            locale = Locale.getDefault();
        }
        LogUtil.d(TAG, locale.getLanguage());


    }


    public static void onConfigurationChanged(Context context) {
        saveSystemCurrentLanguage();
        setLocal(context);
        setApplicationLanguage();
    }


    /**
     * 设置语言类型
     */
    public static void setApplicationLanguage() {
        Resources resources = UIUtils.getResources();
        DisplayMetrics dm = resources.getDisplayMetrics();
        Configuration config = resources.getConfiguration();
        Locale locale = getSetLanguageLocale();
        config.locale = locale;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            LocaleList localeList = new LocaleList(locale);
            LocaleList.setDefault(localeList);
            config.setLocales(localeList);
            UIUtils.getContext().createConfigurationContext(config);
            Locale.setDefault(locale);
        }
        resources.updateConfiguration(config, dm);
    }

}
