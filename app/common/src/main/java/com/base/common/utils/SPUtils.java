package com.base.common.utils;

import android.content.Context;
import android.content.SharedPreferences;

import com.base.common.model.http.jackSon.JacksonUtils;

import java.util.ArrayList;
import java.util.List;


/**
 * SharedPreferences存储工具类
 */
public class SPUtils {

    private static final String CONFIG = "config";
    private static final String ISOPEN = "isopen";

    /**
     * 获取SharedPreferences实例对象
     *
     * @param fileName
     */
    private static SharedPreferences getSharedPreference(String fileName) {
        return UIUtils.getContext().getSharedPreferences(fileName, Context.MODE_PRIVATE);
    }

    /**
     * 保存一个String类型的值！
     */
    public static void putString(String key, String value) {
        SharedPreferences.Editor editor = getSharedPreference(CONFIG).edit();
        editor.putString(key, value).apply();
    }

    /**
     * 获取String的value
     */
    public static String getString(String key, String defValue) {
        SharedPreferences sharedPreference = getSharedPreference(CONFIG);
        return sharedPreference.getString(key, defValue);
    }


    public static <T> T getBean(String key, Class<T> clazz) {
        SharedPreferences sharedPreference = getSharedPreference(CONFIG);
        String string = sharedPreference.getString(key, "");
        if (!string.isEmpty()) {
            T bean = JacksonUtils.getJsonBean(string, clazz);
            return bean;
        }
        return null;
    }

    public static <T> List<T> getList(String key, Class<T> clazz) {
        SharedPreferences sharedPreference = getSharedPreference(CONFIG);
        String string = sharedPreference.getString(key, "");
        if (!string.isEmpty()) {
            List<T> list = JacksonUtils.getConverList(string, clazz);
            return list;
        }
        return null;
    }

    public static void putBean(String key, Object value) {
        SharedPreferences.Editor editor = getSharedPreference(CONFIG).edit();
        if (value != null) {
            editor.putString(key, JacksonUtils.transBean2Json(value)).apply();
        }

    }


    /**
     * 保存一个Boolean类型的值！
     */
    public static void putBoolean(String key, Boolean value) {
        SharedPreferences.Editor editor = getSharedPreference(CONFIG).edit();
        editor.putBoolean(key, value).apply();
    }

    /**
     * 获取boolean的value
     */
    public static boolean getBoolean(String key, Boolean defValue) {
        SharedPreferences sharedPreference = getSharedPreference(CONFIG);
        return sharedPreference.getBoolean(key, defValue);
    }

    /**
     * 保存一个int类型的值！
     */
    public static void putInt(String key, int value) {
        SharedPreferences.Editor editor = getSharedPreference(CONFIG).edit();
        editor.putInt(key, value).apply();
    }

    /**
     * 获取int的value
     */
    public static int getInt(String key, int defValue) {
        SharedPreferences sharedPreference = getSharedPreference(CONFIG);
        return sharedPreference.getInt(key, defValue);
    }

    /**
     * 保存一个float类型的值！
     */
    public static void putFloat(String fileName, String key, float value) {
        SharedPreferences.Editor editor = getSharedPreference(fileName).edit();
        editor.putFloat(key, value).apply();
    }

    /**
     * 获取float的value
     */
    public static float getFloat(String key, Float defValue) {
        SharedPreferences sharedPreference = getSharedPreference(CONFIG);
        return sharedPreference.getFloat(key, defValue);
    }

    /**
     * 保存一个long类型的值！
     */
    public static void putLong(String key, long value) {
        SharedPreferences.Editor editor = getSharedPreference(CONFIG).edit();
        editor.putLong(key, value).apply();
    }

    /**
     * 获取long的value
     */
    public static long getLong(String key, long defValue) {
        SharedPreferences sharedPreference = getSharedPreference(CONFIG);
        return sharedPreference.getLong(key, defValue);
    }

    /**
     * 取出List<String>
     *
     * @param key List<String> 对应的key
     * @return List<String>
     */
    public static List<String> getStrListValue(String key) {
        List<String> strList = new ArrayList<String>();
        int size = getInt(key + "size", 0);
        //Log.d("sp", "" + size);
        for (int i = 0; i < size; i++) {
            strList.add(getString(key + i, null));
        }
        return strList;
    }

    /**
     * 存储List<String>
     *
     * @param
     * @param key     List<String>对应的key
     * @param strList 对应需要存储的List<String>
     */
    public static void putStrListValue(String key, List<String> strList) {
        if (null == strList) {
            return;
        }
        // 保存之前先清理已经存在的数据，保证数据的唯一性
        removeStrList(key);
        int size = strList.size();
        putInt(key + "size", size);
        for (int i = 0; i < size; i++) {
            putString(key + i, strList.get(i));
        }
    }

    /**
     * 清空List<String>所有数据
     *
     * @param key List<String>对应的key
     */
    public static void removeStrList(String key) {
        int size = getInt(key + "size", 0);
        if (0 == size) {
            return;
        }
        remove(key + "size");
        for (int i = 0; i < size; i++) {
            remove(key + i);
        }
    }

    /**
     * 清空对应key数据
     */
    public static void remove(String key) {
        SharedPreferences.Editor editor = getSharedPreference(CONFIG).edit();
        editor.remove(key).apply();
    }

    /**
     * 清空所有数据
     */
    public static void removeAll() {
        SharedPreferences.Editor editor = getSharedPreference(CONFIG).edit();
        editor.clear().apply();
    }

    public static void setIsOpen(boolean value) {
        getSharedPreference(CONFIG).edit().putBoolean(ISOPEN, value).apply();
    }

    public static boolean getIsOpen() {
        return getSharedPreference(CONFIG).getBoolean(ISOPEN, false);
    }

}
