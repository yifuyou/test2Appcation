package com.base.common.app;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.Application;
import android.content.Context;
import android.content.res.Configuration;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Process;
import android.webkit.WebView;

import androidx.fragment.app.FragmentActivity;
import androidx.multidex.MultiDex;

import com.base.common.utils.IOUtils;
import com.base.common.utils.LanguageManageUtil;
import com.base.common.utils.LogUtil;
import com.base.common.utils.UIUtils;
import com.jeremyliao.liveeventbus.LiveEventBus;

import io.reactivex.Observable;


public     class BaseApp extends Application implements ConfigCommon {

    public static final String TAG = "BaseApp";

    // 获取到主线程的上下文
    private static BaseApp mContext = null;
    // 获取到主线程的handler
    private static Handler mMainThreadHandler = null;
    // 获取到主线程的looper
    private static Looper mMainThreadLooper = null;
    // 获取到主线程
    private static Thread mMainThead = null;
    // 获取到主线程的id
    private static int mMainTheadId;



    //以下的方法需要重写↓---------↓------------↓----------------↓--------↓-------------↓----------↓

    @Override
    public String getBaseUrl() {
        return null;
    }

    @Override
    public Observable<String> observable(FragmentActivity activity, int maxCount, int videoCount, boolean... isSelectedGif_Crop) {
        return null;
    }

    //以上的方法需要重写↑------------↑-----------------↑-----------↑-----------------↑------------↑


    @Override
    public void onCreate() {
        super.onCreate();

        //线程池工具类初始化
        mContext = this;
        mMainThreadHandler = new Handler();
        mMainThreadLooper = getMainLooper();
        mMainThead = Thread.currentThread();
        // android.os.Process.myUid()获取到用户id
        // android.os.Process.myPid();//获取到进程id
        // android.os.Process.myTid()获取到调用线程的id
        mMainTheadId = Process.myTid();// 主线程id

        registerActivityListener();

        IOUtils.initRootDirectory();

        // 跟随系统设置语言
        LanguageManageUtil.setApplicationLanguage();


/**
 * 初始化LiveEventBus
 * 2、配置LifecycleObserver（如Activity）接收消息的模式（默认值true）：
 * true：整个生命周期（从onCreate到onDestroy）都可以实时收到消息
 * false：激活状态（Started）可以实时收到消息，非激活状态（Stoped）无法实时收到消息，需等到Activity重新变成激活状
 * 态，方可收到消息
 * 3、autoClear
 * 配置在没有Observer关联的时候是否自动清除LiveEvent以释放内存（默认值false）
 * */

        LiveEventBus.config()
//                .setJsonConverter(new JsonConverter() {
//                    @Override
//                    public String toJson(Object value) {
//                        return JacksonUtils.transBean2Json(value);
//                    }
//
//                    @Override
//                    public <T> T fromJson(String json, Class<T> classOfT) {
//                        return JacksonUtils.getJsonBean(json, classOfT);
//                    }
//                })
                .lifecycleObserverAlwaysActive(true)
                .autoClear(true);


        //异常日志:Caused by: java.lang.RuntimeException: Using WebView from more than one process at once with the same data directory is not supported. https://crbug.com/558377
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            String processName = getProcessName(this);
            if (UIUtils.getPackageName().equals(processName)) {//判断不等于默认进程名称
                WebView.setDataDirectorySuffix(processName);
            }
        }

        //bugly初始化
        //         调试时，将第三个参数改为true
//        Bugly.init(getApplication(), "2e5b32ab33", true);

    }

    public String getProcessName(Context context) {
        if (context == null) return null;
        ActivityManager manager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningAppProcessInfo processInfo : manager.getRunningAppProcesses()) {
            if (processInfo.pid == android.os.Process.myPid()) {
                return processInfo.processName;
            }
        }
        return null;
    }


    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        // you must install multiDex whatever tinker is installed!
        MultiDex.install(base);

    }


    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        //设置 语言
        //保存系统选择语言
        LanguageManageUtil.onConfigurationChanged(getApplicationContext());
    }


    public void registerActivityListener() {
        registerActivityLifecycleCallbacks(new ActivityLifecycleCallbacks() {
            @Override
            public void onActivityCreated(Activity activity, Bundle savedInstanceState) {
                LogUtil.d("BaseApp  add " + activity.getClass().getSimpleName());
                AppManager.getInstance().addActivity(activity);
            }

            @Override
            public void onActivityStarted(Activity activity) {

            }

            @Override
            public void onActivityResumed(Activity activity) {
            }

            @Override
            public void onActivityPaused(Activity activity) {
            }

            @Override
            public void onActivityStopped(Activity activity) {
            }

            @Override
            public void onActivitySaveInstanceState(Activity activity, Bundle outState) {
            }

            @Override
            public void onActivityDestroyed(Activity activity) {
                LogUtil.d("BaseApp  remove " + activity.getClass().getSimpleName());
                AppManager.getInstance().removeActivity(activity);
            }
        });
    }

    public static BaseApp getApplication() {
        return mContext;
    }

    public static Handler getMainThreadHandler() {
        return mMainThreadHandler;
    }

    public static Looper getMainThreadLooper() {
        return mMainThreadLooper;
    }

    public static Thread getMainThread() {
        return mMainThead;
    }

    public static int getMainThreadId() {
        return mMainTheadId;
    }



}
