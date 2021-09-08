package com.base.common.app;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import java.util.Arrays;
import java.util.List;
import java.util.Stack;

/**
 * @date 2019-04-23 13:46
 * Activity 的管理类 ，
 */
public class AppManager {

    private final Stack<Activity> mActivities = new Stack<>();

    private static class Holder {
        private static final AppManager INSTANCE = new AppManager();
    }

    public static AppManager getInstance() {
        return Holder.INSTANCE;
    }

    public void addActivity(Activity activity) {
        mActivities.add(activity);
    }

    public void removeActivity(Activity activity) {
//        hideSoftKeyBoard(activity);
//        activity.finish();
        mActivities.remove(activity);
    }


    public void closeActivity(Class<? extends Activity> tClass) {
        if (tClass != null) {
            for (Activity activity : mActivities) {
                if (activity.getClass() == tClass) {
                    hideSoftKeyBoard(activity);
                    activity.finish();
                    break;
                }
            }
        }
    }


    public void closeAllActivity() {
        for (Activity activity : mActivities) {
            hideSoftKeyBoard(activity);
            activity.finish();
        }
//        mActivities.clear();
    }

    /**
     * @param tClass 除了这个activity  都去掉
     */
    public void closeAllActivity(Class<? extends Activity> tClass) {

        if (tClass != null) {
            for (Activity activity : mActivities) {
                if (tClass != activity.getClass()) {
                    hideSoftKeyBoard(activity);
                    activity.finish();
                }
            }
        }

//        mActivities.clear();
    }


    public <T extends Activity> boolean hasActivity(Class<T> tClass) {
        for (Activity activity : mActivities) {
            if (tClass.getName().equals(activity.getClass().getName())) {
                return !activity.isDestroyed() || !activity.isFinishing();
            }
        }
        return false;
    }

    public boolean isAppRunning() {
        return mActivities.size() > 0;
    }

    public boolean hasActivity(String tClassName) {
        for (Activity activity : mActivities) {
            if (tClassName.equals(activity.getClass().getSimpleName())) {
                return true;
            }
        }
        return false;
    }

    public Activity getActivity(String tClassName) {
        for (Activity activity : mActivities) {
            if (tClassName.equals(activity.getClass().getSimpleName())) {
                return activity;
            }
        }
        return null;
    }

    public Activity getTopActivity() {
        if (mActivities.size() == 0) return null;
        return mActivities.peek();
    }

    /**
     * @param tClass 上面的都关闭
     */
    public void setTopActivity(Class<? extends Activity> tClass) {
        if (hasActivity(tClass)) {
            for (int size = mActivities.size(); size > 0; size--) {
                Activity activity = mActivities.peek();
                if (!tClass.getName().equals(activity.getClass().getName())) {
                    activity.finish();
                } else {
                    break;
                }
            }
        }
    }


    public void hideSoftKeyBoard(Activity activity) {
        View localView = activity.getCurrentFocus();
        InputMethodManager imm = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
        if (localView != null && imm != null) {
            imm.hideSoftInputFromWindow(localView.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }


}
