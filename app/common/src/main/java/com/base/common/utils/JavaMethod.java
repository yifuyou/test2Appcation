package com.base.common.utils;

import android.text.TextUtils;

import androidx.annotation.IntRange;
import androidx.annotation.NonNull;

import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class JavaMethod {

    /**
     * @param beanTo     复制后的数据源
     * @param objectFrom 被复制的数据源
     */
    public static void copyValue(Object objectFrom, Object beanTo) {

        if (beanTo == null || objectFrom == null) beanTo = null;
        else {
            Field[] to = beanTo.getClass().getDeclaredFields();
            if (to.length > 0) {
                for (Field field : to) {
                    field.setAccessible(true);
                    Object obj = getFieldValue(objectFrom, field.getName());
                    if (obj != null) {
                        try {
                            field.set(beanTo, transformClass(obj, field.getType()));
                        } catch (IllegalAccessException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        }

    }

    /**
     * @param va
     * @param post 保留多少位  如果为空或0 、保留位数小于当前位数 则不操作，反之则前面补0
     * @return
     */
    public static String intValueOfString(int va, @IntRange(from = 0, to = 32) int... post) {
        int pos = 0;
        if (post.length > 0) {
            pos = post[0];
        }
        String ss = String.valueOf(va);
        if (ss.length() < pos) {
            int count = pos - ss.length();
            String str = "";
            for (int i = 0; i < count; i++) {
                str += "0";
            }
            return str + ss;
        }
        return ss;
    }


    /**
     * 更换url中的参数值
     *
     * @param url
     * @param key
     * @param value
     * @return
     */
    public static String changeParamForKey(String url, String key, String value) {
        if (!TextUtils.isEmpty(url) && !TextUtils.isEmpty(key)) {
            url = url.replaceAll("(" + key + "=[^&]*)", key + "=" + value);
        }
        return url;
    }

    /**
     * 字符串的替换操作  ps:  "1,2,12,11"   去掉为1的字符
     *
     * @param str         要处理的字符串
     * @param split       分隔字符
     * @param regex       要被替换的字段
     * @param replacement 替换的字符
     * @return
     */
    public static String replaceAll(@NonNull String str, String split, String regex, String replacement) {
        StringBuilder sb = new StringBuilder();
        for (String s : str.split(split)) {
            if (s.equals(regex)) {
                sb.append(replacement);
            } else if (UIUtils.isNotEmpty(s)) {
                sb.append(s);
                sb.append(split);
            }

        }
        if (sb.toString().equals(split)) return "";
        return sb.toString();
    }

    /**
     * 对map的key进行排序
     *
     * @param map
     * @return
     */
    public static LinkedHashMap<String, Object> sortMapByKey(@NonNull Map<String, Object> map) {
        List<Map.Entry<String, Object>> list = new ArrayList<>(map.size());
        list.addAll(map.entrySet());
        // 通过Collections.sort()排序
        Collections.sort(list, new Comparator<Map.Entry<String, Object>>() {
            @Override
            public int compare(Map.Entry<String, Object> o1, Map.Entry<String, Object> o2) {
                return o1.getKey().compareTo(o2.getKey());
            }
        });


        LinkedHashMap<String, Object> hashMap = new LinkedHashMap<>();
        for (Map.Entry<String, Object> stringObjectEntry : list) {
            hashMap.put(stringObjectEntry.getKey(), stringObjectEntry.getValue());
        }
        return hashMap;
    }

    public static LinkedHashMap<String, Object> sortMapByKeyDesc(@NonNull Map<String, Object> map) {
        List<Map.Entry<String, Object>> list = new ArrayList<>(map.size());
        list.addAll(map.entrySet());
        // 通过Collections.sort()排序
        Collections.sort(list, new Comparator<Map.Entry<String, Object>>() {
            @Override
            public int compare(Map.Entry<String, Object> o1, Map.Entry<String, Object> o2) {
                return o2.getKey().compareTo(o1.getKey());
            }
        });

        LinkedHashMap<String, Object> hashMap = new LinkedHashMap<>();
        for (Map.Entry<String, Object> stringObjectEntry : list) {
            hashMap.put(stringObjectEntry.getKey(), stringObjectEntry.getValue());
        }
        return hashMap;
    }

    public static String intSimpleParse(int i) {
        String ss = null;
        switch (i) {
            case 0:
                ss = "零";
                break;
            case 1:
                ss = "一";
                break;
            case 2:
                ss = "二";
                break;
            case 3:
                ss = "三";
                break;
            case 4:
                ss = "四";
                break;
            case 5:
                ss = "五";
                break;
            case 6:
                ss = "六";
                break;
            case 7:
                ss = "七";
                break;
            case 8:
                ss = "八";
                break;
            case 9:
                ss = "九";
                break;

        }
        return ss;
    }


    public static BigDecimal getBigDecimal(Object str, BigDecimal... defaultValue) {
        BigDecimal def = defaultValue.length > 0 ? defaultValue[0] : BigDecimal.ZERO;
        if (str == null) return def;
        char[] chr = str.toString().toCharArray();
        StringBuffer sb = new StringBuffer();
        for (char c : chr) {
            if (c >= 48 && c <= 57) {
                sb.append(c);
            } else {
                if (c == '-' || c == '.') {
                    sb.append(c);
                }
            }
        }
        String ss = sb.toString();
        if (ss.contains("-") && !ss.startsWith("-")) {
            ss = ss.substring(ss.indexOf("-"));
        }
        if (ss.equals("-")) return def;

        try {
            return new BigDecimal(ss);
        } catch (NumberFormatException e) {
            return def;
        }
    }

    public static int getInt(Object str, int... defaultValue) {
        int def = defaultValue.length > 0 ? defaultValue[0] : 0;
        if (str == null) return def;
        return getInt(str.toString(), defaultValue);
    }

    public static int getInt(String str, int... defaultValue) {
        int def = defaultValue.length > 0 ? defaultValue[0] : 0;
        if (str == null || str.length() == 0) return def;

        char[] chr = str.toCharArray();
        StringBuffer sb = new StringBuffer();
        for (char c : chr) {
            if (c >= 48 && c <= 57) {
                sb.append(c);
            } else {
                if (c == '-' || c == '.') {
                    sb.append(c);
                    continue;
                }
                if (sb.length() == 0) continue;
                else break;
            }
        }

        String ss = sb.toString();

        if (ss.contains("-") && !ss.startsWith("-")) {
            ss = ss.substring(ss.indexOf("-"));
        }
        if (ss.equals("-")) return def;

        if (ss.length() == 0) return def;
        else {
            return new BigDecimal(ss).intValue();
        }

    }

    public static int getInt(char c) {
        if (c >= 48 && c <= 57) {
            return c - 48;
        }
        return 0;
    }


    /**
     * 判断是否全部是中文
     *
     * @param value
     * @return
     */
    public static boolean isCHString(String value) {
        Pattern p = Pattern.compile("[\u4e00-\u9fa5]");
        Matcher m = p.matcher(value);
        return m.matches();
    }


    /**
     * 判断字符串中是否包含中文
     *
     * @param str 待校验字符串
     * @return 是否为中文
     * @warn 不能校验是否为中文标点符号
     */
    public static boolean isContainChinese(String str) {
        Pattern p = Pattern.compile("[\u4e00-\u9fa5]");
        Matcher m = p.matcher(str);
        if (m.find()) {
            return true;
        }
        return false;
    }

    /**
     * 过滤掉中文
     *
     * @param str 待过滤中文的字符串
     * @return 过滤掉中文后字符串
     */
    public static String filterChinese(String str) {
        // 用于返回结果
        String result = str;
        boolean flag = isContainChinese(str);
        if (flag) {// 包含中文
            // 用于拼接过滤中文后的字符
            StringBuffer sb = new StringBuffer();
            // 用于校验是否为中文
            boolean flag2 = false;
            // 用于临时存储单字符
            char chinese = 0;
            // 5.去除掉文件名中的中文
            // 将字符串转换成char[]
            char[] charArray = str.toCharArray();
            // 过滤到中文及中文字符
            for (int i = 0; i < charArray.length; i++) {
                chinese = charArray[i];
                flag2 = isChinese(chinese);
                if (!flag2) {// 不是中日韩文字及标点符号
                    sb.append(chinese);
                }
            }
            result = sb.toString();
        }
        return result;
    }

    /**
     * 校验一个字符是否是汉字
     *
     * @param c 被校验的字符
     * @return true代表是汉字
     */
    public static boolean isChineseChar(char c) {
        try {
            return String.valueOf(c).getBytes("UTF-8").length > 1;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 判定输入的是否是汉字
     *
     * @param c 被校验的字符
     * @return true代表是汉字
     */
    public static boolean isChinese(char c) {
        Character.UnicodeBlock ub = Character.UnicodeBlock.of(c);
        if (ub == Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS
                || ub == Character.UnicodeBlock.CJK_COMPATIBILITY_IDEOGRAPHS
                || ub == Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS_EXTENSION_A
                || ub == Character.UnicodeBlock.GENERAL_PUNCTUATION
                || ub == Character.UnicodeBlock.CJK_SYMBOLS_AND_PUNCTUATION
                || ub == Character.UnicodeBlock.HALFWIDTH_AND_FULLWIDTH_FORMS) {
            return true;
        }
        return false;
    }


    @SafeVarargs
    public static <T> T getFieldValue(Object bean, String fieldName, Class<T>... cls) {
        if (bean == null) return null;
        Class clazz = bean.getClass();

        try {
            Field field = clazz.getDeclaredField(fieldName);
            field.setAccessible(true);
            Object obj = field.get(bean);
            if (cls.length == 0) return (T) obj;
            else {
                return transformClass(obj, cls[0]);
            }
        } catch (NoSuchFieldException | IllegalAccessException e) {
            if (cls.length == 0) return null;
            else return transformClass(null, cls[0]);
        }

    }

    public static Class getFieldType(@NonNull Class clazz, String fieldName) {
        try {
            Field field = clazz.getDeclaredField(fieldName);
            return field.getType();
        } catch (NoSuchFieldException e) {
            return null;
        }
    }


    /**
     * 获取不相同的集合
     * 以第一个map为参数为基数比较
     */
    public static Map<String, Object> getDiffrentMap(Map<String, Object> newMap, Map<String, Object> oldMap) {
        Map<String, Object> diffrent = new HashMap<>();
        if (newMap == null || oldMap == null) return diffrent;
        for (String key : newMap.keySet()) {
            if (!equals(newMap.get(key), oldMap.get(key))) {
                diffrent.put(key, newMap.get(key));
            }
        }
        return diffrent;
    }

    public static boolean isDiffrentBean(Object newBean, Object oldBean) {
        Field[] fields = newBean.getClass().getDeclaredFields();
        for (Field newField : fields) {
            newField.setAccessible(true);
            try {
                Field oldField = oldBean.getClass().getDeclaredField(newField.getName());
                oldField.setAccessible(true);
                Object newObj = newField.get(newBean);
                Object old = oldField.get(oldBean);
                //list不进行比对,直接为不同的
                if (newObj instanceof List) {
                    return false;
                } else {
                    if (!equals(newObj, old)) return false;
                }

            } catch (IllegalAccessException | NoSuchFieldException e) {
                // e.printStackTrace();
            }
        }
        return true;
    }

    public static Map<String, Object> getDiffrentBean(Object newBean, Object oldBean) {
        Map<String, Object> diffrent = new HashMap<>();
        if (newBean == null || oldBean == null) return diffrent;

        Field[] fields = newBean.getClass().getDeclaredFields();
        for (Field newField : fields) {
            newField.setAccessible(true);
            try {
                Field oldField = oldBean.getClass().getDeclaredField(newField.getName());
                oldField.setAccessible(true);
                Object newObj = newField.get(newBean);
                Object old = oldField.get(oldBean);
                //list不进行比对,直接为不同的
                if (newObj instanceof List) {
                    diffrent.put(newField.getName(), newObj);
                } else {
                    if (!equals(newObj, old)) {
                        diffrent.put(newField.getName(), newObj);
                    }
                }


            } catch (IllegalAccessException | NoSuchFieldException e) {
                // e.printStackTrace();
            }
        }

        return diffrent;
    }

    public static boolean equals(Object o1, Object o2) {
        if (o1 == null && o2 == null) return true;
        else if (o1 == null || o2 == null) return false;
        return o1.toString().compareTo(o2.toString()) == 0;
    }


    /**
     * 根据属性名，调用set方法
     *
     * @param bean
     * @param fieldName
     * @param value
     * @param <T>
     */
    public static <T> void setMethodSetByFieldName(T bean, String fieldName, Object value) {
        if (UIUtils.isEmpty(fieldName) || UIUtils.isEmpty(bean) || UIUtils.isEmpty(value)) return;
        String s = fieldName.substring(0, 1);
        String methodName = "set" + s.toUpperCase() + fieldName.substring(1);
        try {
            Method method = getMethod(bean, methodName, value.getClass());
            try {
                method.invoke(bean, value);
            } catch (IllegalAccessException | InvocationTargetException e) {
//                e.printStackTrace();
            }

        } catch (NoSuchMethodException e) {
//            e.printStackTrace();
        }

    }

    public static Method getMethod(Object bean, String methodName, Class<?>... cls) throws NoSuchMethodException {
        return bean.getClass().getDeclaredMethod(methodName, cls);
    }

    public static <T> void setFieldValue(T bean, String fieldName, Object value) {
        if (bean == null || TextUtils.isEmpty(fieldName)) return;
        Class clazz = bean.getClass();
        try {
            Field field = clazz.getDeclaredField(fieldName);
            field.setAccessible(true);
            field.set(bean, transformClass(value, field.getType()));
        } catch (IllegalAccessException | IllegalArgumentException | NoSuchFieldException e) {
            e.printStackTrace();
        }

    }


    /**
     * 获取javaBean中,包含feildName的的返回值
     *
     * @param javaBean
     * @param feildName
     * @return Map对象
     */
    public static <T> Map<String, Object> getContainsNamesValue(T javaBean, String feildName) {
        if (javaBean == null) return null;
        Map<String, Object> result = new HashMap<>();
        Field[] fields = javaBean.getClass().getDeclaredFields();
        for (Field field : fields) {
            if (TextUtils.isEmpty(feildName) || field.getName().toLowerCase().contains(feildName)) {
                field.setAccessible(true);
                try {
                    Object obj = field.get(javaBean);
                    result.put(field.getName(), obj);
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
            }

        }
        return result;
    }


    public static String join(@NonNull String delimiter, String... tokens) {
        final int length = tokens.length;
        if (length == 0) {
            return "";
        }
        final StringBuilder sb = new StringBuilder();
        sb.append(tokens[0]);
        for (int i = 1; i < length; i++) {
            sb.append(delimiter);
            sb.append(tokens[i]);
        }
        return sb.toString();
    }

    public static String join(@NonNull String delimiter, List<String> tokens) {
        final int length = tokens.size();
        if (length == 0) {
            return "";
        }
        final StringBuilder sb = new StringBuilder();
        sb.append(tokens.get(0));
        for (int i = 1; i < length; i++) {
            sb.append(delimiter);
            sb.append(tokens.get(i));
        }
        return sb.toString();
    }


    /**
     * 非强制转换，比如"true" String转换为boolean的true
     *
     * @param fieldType
     * @param value
     * @param <T>
     * @return
     */

    public static <T> T transformClass(Object value, Class<T> fieldType, Object... defaultValue) {

        Object deaultVa = null;
        if (defaultValue.length > 0) deaultVa = defaultValue[0];

        if (value != null && value.getClass() == fieldType) return (T) value;
        else {
            Object obj;
            if (BigDecimal.class == fieldType) {
                if (value == null || value.equals("")) value = deaultVa == null ? 0 : deaultVa;
                obj = new BigDecimal(String.valueOf(value));
            } else if (String.class == fieldType) {
                if (value == null) value = "";
                obj = String.valueOf(value);
            } else if (fieldType == Date.class) {
                obj = new Date(DateUtils.dateStringToLong(String.valueOf(value)));
            } else if (Integer.class == fieldType || int.class == fieldType) {
                if (value == null || value.equals("")) obj = deaultVa == null ? 0 : deaultVa;
                else obj = Integer.valueOf(value.toString());
            } else if (Long.class == fieldType || long.class == fieldType) {
                if (value == null || value.equals("")) obj = deaultVa == null ? 0 : deaultVa;
                else obj = Long.valueOf(String.valueOf(value));
            } else if (Double.class == fieldType || double.class == fieldType) {
                if (value == null || value.equals("")) obj = deaultVa == null ? 0 : deaultVa;
                else obj = Double.valueOf(String.valueOf(value));
            } else if (Float.class == fieldType || float.class == fieldType) {
                if (value == null || value.equals("")) obj = deaultVa == null ? 0 : deaultVa;
                else obj = Float.valueOf(String.valueOf(value));
            } else if (Boolean.class == fieldType || boolean.class == fieldType) {
                if (value == null || value.equals("")) obj = false;
                else obj = Boolean.valueOf(String.valueOf(value));
            } else if (fieldType == byte[].class) {
                if (value == null) value = "";
                obj = value.toString().getBytes();
            } else if (fieldType == List.class || fieldType == ArrayList.class) {
                if ("".equals(value)) {
                    obj = null;
                } else {
                    obj = value;
                }
            } else {
                if (isObject(fieldType)) {
                    //是对像  但是类型不同  直接置空
                    obj = null;
                } else {
                    obj = value;
                }

            }
            return (T) obj;
        }
    }


    /**
     * @param obj        要调用的实例
     * @param methodName 方法名
     * @param params     参数
     */

    public static void invokeSetMethod(Object obj, String methodName, Object... params) {
        if (obj == null || TextUtils.isEmpty(methodName)) return;
        try {
            Class<?> cls = obj.getClass();

            Class<?>[] cls_ = new Class[params.length];
            for (int i = 0; i < params.length; i++) {
                cls_[i] = params[i].getClass();
            }

            Method method = cls.getDeclaredMethod(methodName, cls_);
            method.setAccessible(true);
            method.invoke(obj, params);
        } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
//            e.printStackTrace();
        }
    }


    /**
     * 获得带有泛型的父类的参数
     * <p>
     * 获取泛型类型 例如List<String>
     * <p>
     * ps: class FriendBirthList extends BaseResponse<List<FriendBirthList.InfoBean>>
     * 只能获取到list   里面的FriendBirthList.InfoBean  则获取不到 ，
     * 该方法只获取一级的参数
     *
     * @param obj
     * @param index ps:List<String> 只有一个参数为0，Map<String,Object>index 0,1两个
     * @return
     */
    public static Class<?> getGenericClass(@NonNull Object obj, int index) {
        return getGenericClass(obj.getClass(), index);
    }


    public static Class<?> getGenericClass(@NonNull Class<?> obj, int index) {
        Class<?>[] classes = getGenericClass(obj);
        if (index > classes.length - 1) return null;
        return classes[index];
    }

    public static Class<?>[] getGenericClass(@NonNull Object obj) {
        return getGenericClass(obj.getClass());
    }

    /**
     * 获得带有泛型的父类的参数
     * ps: class FriendBirthList extends BaseResponse<List<FriendBirthList.InfoBean>>
     * 只能获取到list   里面的FriendBirthList.InfoBean  则获取不到 ，
     * 该方法只获取一级的参数
     *
     * @param clzz
     * @return
     */
    public static Class<?>[] getGenericClass(@NonNull Class<?> clzz) {

        //获得带有泛型的父类
        Type genType = clzz.getGenericSuperclass();
        //ParameterizedType参数化类型，即泛型
        if (genType instanceof ParameterizedType) {
            return getParameterizedClass((ParameterizedType) genType);
        } else {
            return new Class[]{Object.class};
        }
    }

    /**
     * 获取上父类的泛型参数,如果没有,则继续上一级 直到找到
     *
     * @return
     */
    public static Class<?> getSuperGenericClass(@NonNull Object obj, int index) {
        return getSuperGenericClass(obj.getClass(), index);
    }

    public static Class<?> getSuperGenericClass(@NonNull Class<?> obj, int index) {
        Class<?>[] classes = getSuperGenericClass(obj);
        if (index > classes.length - 1) return null;
        return classes[index];
    }

    public static Class<?>[] getSuperGenericClass(@NonNull Class<?> clzz) {

        //获得带有泛型的父类
        Type genType = clzz.getGenericSuperclass();
        //ParameterizedType参数化类型，即泛型
        if (genType instanceof ParameterizedType) {
            return getParameterizedClass((ParameterizedType) genType);
        } else {
            if (genType instanceof Class) {
                return getSuperGenericClass((Class) genType);
            } else {
                return new Class[]{Object.class};
            }

        }
    }


    public static Class<?>[] getParameterizedClass(ParameterizedType genType) {
        //getActualTypeArguments获取参数化类型的数组，泛型可能有多个
        Type[] params = genType.getActualTypeArguments();
        Class<?>[] cls = new Class[params.length];
        for (int i = 0; i < params.length; i++) {
            if (params[i] instanceof ParameterizedType) {
                cls[i] = (Class<?>) ((ParameterizedType) params[i]).getRawType();
            } else {
                if (params[i] instanceof Class) {
                    cls[i] = (Class<?>) params[i];
                } else {
                    cls[i] = Object.class;
                }

            }
        }
        return cls;
    }


    /**
     * 判断一个对象是否是基本类型
     * ps: int ,boolean,char,sort
     */
    public static boolean isPrimitive(@NonNull Object obj) {
        return isPrimitive(obj.getClass());
    }

    /**
     * 判断一个对象是否是基本类型
     * ps: int ,boolean,char,sort
     */

    public static boolean isPrimitive(@NonNull Class<?> cls) {
        return cls.isPrimitive();
    }

    /**
     * 判断一个对象是否是基本类型的封装对像
     * 不包括 String  char的封装对像是 Character
     * int , char, return false;
     * ps:Integer,Boolean    return true;
     */
    public static boolean isPrimitiveObject(@NonNull Class<?> cls) {
        try {
            return ((Class) cls.getField("TYPE").get(null)).isPrimitive();
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * 判断一个对象是否是基本类型或封装对像  包括 String  Date Number
     * <p>
     * ps:Integer,Boolean
     */
    public static boolean isPrimitiveOrObject(Class<?> cls) {
        if (cls == String.class || Number.class.isAssignableFrom(cls) || cls == Date.class) return true;
        try {
            if (cls.isPrimitive()) {
                return true;
            } else {
                return ((Class) cls.getField("TYPE").get(null)).isPrimitive();
            }
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * 判断一个类型是否是对像
     *
     * @param cls
     * @return
     */
    public static boolean isObject(Class<?> cls) {
        return !isPrimitiveOrObject(cls);
    }


}
