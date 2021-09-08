package com.base.common.utils;

import android.app.Activity;
import android.app.Application;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Build;
import android.os.Handler;
import android.os.Looper;
import android.util.LongSparseArray;
import android.util.SparseArray;
import android.util.SparseBooleanArray;
import android.util.SparseIntArray;
import android.util.SparseLongArray;
import android.util.TypedValue;

import androidx.annotation.ArrayRes;
import androidx.annotation.ColorRes;
import androidx.annotation.DimenRes;
import androidx.annotation.DrawableRes;
import androidx.annotation.StringRes;
import androidx.collection.SimpleArrayMap;
import androidx.core.content.ContextCompat;

import com.base.common.app.BaseApp;
import com.base.common.view.widget.toast.ToastCustomer;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Array;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import static android.content.Context.CLIPBOARD_SERVICE;






/*     TextUtils中常用的方法
       Log.e("tag",TextUtils.isEmpty(a)+"");// 字符串是否为null或“”
       Log.e("tag",TextUtils.isDigitsOnly(c)+"");// 字符串是否全是数字
       Log.e("tag",TextUtils.isGraphic(d)+"");// 字符串是否含有可打印的字符
       Log.e("tag",TextUtils.concat(a,b)+"");// 拼接多个字符串
       Log.e("tag",TextUtils.getTrimmedLength(b)+"");// 去掉字符串前后两端空格(相当于trim())之后的长度
       Log.e("tag",TextUtils.getReverse(b,0,b.length())+"");// 在字符串中，start和end之间字符的逆序
       Log.e("tag",TextUtils.join("-",list));// 在数组中每个元素之间使用“-”来连接
       String[]arr=TextUtils.split(e,"-");// 以"-"来分组
       Log.e("tag",TextUtils.htmlEncode(f)+"");// 使用html编码字符串*/


public class UIUtils {


    public static SharedPreferences getSharedPreferences(String filename) {
        return getContext().getSharedPreferences(filename, Context.MODE_PRIVATE);
    }

    /**
     * 获取上下
     *
     * @
     */
    public static Application getContext() {
        return BaseApp.getApplication();
    }

    public static String getPackageName() {
        return getContext().getPackageName();
    }

    /**
     * 获取主线程
     *
     * @
     */
    public static Thread getMainThread() {
        return BaseApp.getMainThread();
    }

    /**
     * 获取主线程id
     *
     * @
     */
    public static long getMainThreadId() {
        return BaseApp.getMainThreadId();
    }

    /**
     * 获取到主线程的looper
     *
     * @
     */
    public static Looper getMainThreadLooper() {
        return BaseApp.getMainThreadLooper();
    }

    /**
     * 获取主线程的handler
     */
    public static Handler getHandler() {
        return BaseApp.getMainThreadHandler();
    }

    /**
     * 延时在主线程执行runnable
     */
    public static boolean postDelayed(Runnable runnable, long delayMillis) {
        return getHandler().postDelayed(runnable, delayMillis);
    }

    /**
     * 在主线程执行runnable
     */
    public static boolean post(Runnable runnable) {
        return getHandler().post(runnable);
    }

    /**
     * 从主线程looper里面移除runnable
     */
    public static void removeCallbacks(Runnable runnable) {
        getHandler().removeCallbacks(runnable);
    }


    /**
     * 获取图片
     */
    public static Bitmap getAssetsImage(String filename) {
        InputStream ins = null;
        Bitmap bitmap = null;
        try {
            ins = getContext().getAssets().open(filename);
            bitmap = BitmapFactory.decodeStream(ins);
        } catch (IOException e) {
            e.printStackTrace();
            return bitmap;
        } finally {
            if (ins != null) {
                try {
                    ins.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return bitmap;
    }

    /**
     * @param filename /images/app_private.html
     * @return
     */
    public static String getAssetsUrl(String filename) {
        return "file:///android_asset/" + filename;
    }


    public static String[] getAssetsList(String path) {
        if (path == null) path = "";
        try {
            return getContext().getAssets().list(path);
        } catch (IOException e) {
            return null;
        }
    }

    /**
     * 从assets中读取txt
     */
    public static String getAssetsText(String filename) {
        String text;
        try {
            InputStream is = getContext().getAssets().open(filename);
            text = readTextFromInputStream(is);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            text = "";
        }
        return text;
    }

    /**
     * 从raw中读取txt
     */
    public static String getRawText(int resId) {
        String text;
        try {
            InputStream is = getResources().openRawResource(resId);
            text = readTextFromInputStream(is);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            text = "";
        }
        return text;
    }

    /**
     * 从输入流中按行读取txt
     */
    private static String readTextFromInputStream(InputStream is) {
        InputStreamReader reader = new InputStreamReader(is);
        BufferedReader bufferedReader = new BufferedReader(reader);
        StringBuffer buffer = new StringBuffer("");
        String str;
        try {
            while ((str = bufferedReader.readLine()) != null) {
                buffer.append(str);
                buffer.append("\n");
            }
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
            try {
                reader.close();
            } catch (IOException e1) {
                e1.printStackTrace();
            }
        }
        return buffer.toString();
    }

    /**
     * 根据字符串型的资源名获取对应资源id
     *
     * @param defType mipmap
     */

    public static int getResOfString(String resName, String defType) {
        //如果没有在"mipmap"下找到imageName,将会返回0
        return getResources().getIdentifier(resName, defType, getContext().getPackageName());
    }


    /**
     * 获取资源
     */

    public static Resources getResources() {
        return getContext().getResources();
    }

    /**
     * 获取文字
     */
    public static String getString(int resId) {
        return getResources().getString(resId);
    }

    public static int getInteger(int resId) {
        return getResources().getInteger(resId);
    }

    /**
     * 获取文字数组
     */
    public static String[] getStringArray(int resId) {
        return getResources().getStringArray(resId);
    }

    public static int[] getIntArray(int resId) {
        return getResources().getIntArray(resId);
    }

    /**
     * 获取图处数组的资源
     *
     * @param resId
     * @return
     */
    public static TypedArray getTypedArray(@ArrayRes int resId) {
        return getResources().obtainTypedArray(resId);
    }

    /**
     * 获取颜色选择
     */

    public static ColorStateList getColorStateList(int resId) {
        return ContextCompat.getColorStateList(getContext(), resId);
    }

    /**
     * activity MetaData读取
     */
    public static String getActivityMetaData(Activity activity, String keyName) {
        try {
            ActivityInfo info = activity.getPackageManager().getActivityInfo(activity.getComponentName(),
                    PackageManager.GET_META_DATA);
            return info.metaData.getString(keyName, "");
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
            return "";
        }
    }

    /**
     * appliction MetaData读取
     */
    public static String getApplicationMetaData(Context context, String keyName) {
        try {
            ApplicationInfo info = context.getPackageManager().getApplicationInfo(context.getPackageName(),
                    PackageManager.GET_META_DATA);
            return info.metaData.getString(keyName, "");
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
            return "";
        }
    }

    public static boolean isEmpty(Object obj) {
        if (obj == null) {
            return true;
        }
        if (obj instanceof CharSequence && obj.toString().length() == 0) {
            return true;
        }
        if (obj.getClass().isArray() && Array.getLength(obj) == 0) {
            return true;
        }
        if (obj instanceof Collection && ((Collection) obj).isEmpty()) {
            return true;
        }
        if (obj instanceof Map && ((Map) obj).isEmpty()) {
            return true;
        }
        if (obj instanceof SimpleArrayMap && ((SimpleArrayMap) obj).isEmpty()) {
            return true;
        }
        if (obj instanceof SparseArray && ((SparseArray) obj).size() == 0) {
            return true;
        }
        if (obj instanceof SparseBooleanArray && ((SparseBooleanArray) obj).size() == 0) {
            return true;
        }
        if (obj instanceof SparseIntArray && ((SparseIntArray) obj).size() == 0) {
            return true;
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            if (obj instanceof SparseLongArray && ((SparseLongArray) obj).size() == 0) {
                return true;
            }
        }
        if (obj instanceof LongSparseArray && ((LongSparseArray) obj).size() == 0) {
            return true;
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            if (obj instanceof LongSparseArray
                    && ((LongSparseArray) obj).size() == 0) {
                return true;
            }
        }
        return false;
    }


    public static boolean isNotEmpty(Object obj) {
        return !isEmpty(obj);
    }


    public static boolean isNotEmpty(Map obj) {
        return !isEmpty(obj);
    }

    public static boolean isEmpty(String str) {
        return str == null || str.length() == 0;
    }

    public static boolean isNotEmpty(String str) {
        return !isEmpty(str);
    }


    public static boolean isEmpty(List list) {
        return list == null || list.size() == 0;
    }

    public static boolean isNotEmpty(List list) {
        return !isEmpty(list);
    }

    // 判断当前的线程是不是在主线程
    public static boolean isRunInMainThread() {
        return android.os.Process.myTid() == getMainThreadId();
    }

    public static void runInMainThread(Runnable runnable) {
        // 在主线程运行
        if (isRunInMainThread()) {
            runnable.run();
        } else {
            post(runnable);
        }
    }

    /**
     * 对toast的简易封装确线程安全，可以在非UI线程调用
     * 上面的图标是对勾
     */
    public static void showToastSafes(final String str) {
        if (isRunInMainThread()) {
            showToast(str, true);
        } else {
            post(new Runnable() {
                @Override
                public void run() {
                    showToast(str, true);
                }
            });
        }
    }

    public static void showToastSafes(@StringRes int str) {
        if (isRunInMainThread()) {
            showToast(getString(str), true);
        } else {
            post(new Runnable() {
                @Override
                public void run() {
                    showToast(getString(str), true);
                }
            });
        }
    }

    /**
     * @param str
     * @param res 可以更换图标
     */
    public static void showToastSafes(final String str, @DrawableRes int res) {
        if (isRunInMainThread()) {
            showToast(str, res);
        } else {
            post(new Runnable() {
                @Override
                public void run() {
                    showToast(str, res);
                }
            });
        }
    }

    /**
     * 对toast的简易封装确线程安全，可以在非UI线程调用
     * 上面的图标是x图标
     */
    public static void showToastSafesClose(final String str) {
        if (isRunInMainThread()) {
            showToast(str, false);
        } else {
            post(new Runnable() {
                @Override
                public void run() {
                    showToast(str, false);
                }
            });
        }
    }

    public static void showToastSafesClose(@StringRes int str) {
        if (isRunInMainThread()) {
            showToast(getString(str), false);
        } else {
            post(new Runnable() {
                @Override
                public void run() {
                    showToast(getString(str), false);
                }
            });
        }
    }


    private static void showToast(String str, boolean isSuccess) {
        if (isSuccess) {
            ToastCustomer.getInstance(getContext()).show(str, 2000);
        } else {
            ToastCustomer.getInstance(getContext()).showClose(str, 2000);
        }
    }

    private static void showToast(String str, @DrawableRes int res) {
        ToastCustomer.getInstance(getContext()).show(str, res, 2000);
    }


}

