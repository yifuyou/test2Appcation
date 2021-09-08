package com.base.common.utils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;

public class NoticeUtils {

    public static final String FORM_NOTICE_OPEN = "form_notice_open";  //标记从通知栏点击进入
    public static final String FORM_NOTICE_OPEN_DATA = "form_notice_open_data"; // 需要传递Intent的的参数
    public static final String CALL_TO_ACTIVITY = "call_to_activity";  //要跳转的activity  class

    /**
     * 自动判断appUI进程是否已在运行，设置跳转信息  在BroadcastReceiver中用
     *
     * @param context
     * @param intent  //要跳转的Intent
     */
    public static void startActivityWithAppIsRuning(Context context, Intent intent, String packageName) {
        boolean isAppRuning = SystemUtils.isAppRunning();
        if (isAppRuning) {
            Intent newIntent = new Intent(context, (Class<?>) intent.getExtras().getSerializable(CALL_TO_ACTIVITY));
            newIntent.putExtras(intent.getExtras());
            newIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(newIntent);
            return;
        }
        // 如果app进程已经被杀死，先重新启动app，将DetailActivity的启动参数传入Intent中，参数经过
        // SplashActivity传入MainActivity，此时app的初始化已经完成，在MainActivity中就可以根据传入
        // 参数跳转到DetailActivity中去了
        Intent launchIntent = context.getPackageManager().getLaunchIntentForPackage(packageName);
        launchIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED);
        launchIntent.putExtra(FORM_NOTICE_OPEN, true);
        launchIntent.putExtra(FORM_NOTICE_OPEN_DATA, intent);
        context.startActivity(launchIntent);
    }


    /**
     * 在StartActivity 启动MainActivity时用
     * 启动App时，为跳转到主页MainActivity的Intent写入打开通知的Intent，如果有通知的情况下
     *
     * @param appStartActivity        app启动的第一个activity，在配置文件中设置的mainactivity
     * @param startMainActivityIntent
     */
    public static void startAppMainActivitySetNoticeIntent(Activity appStartActivity, Intent startMainActivityIntent) {
        /**
         * 如果启动app的Intent中带有额外的参数，表明app是从点击通知栏的动作中启动的 将参数取出，传递到MainActivity中
         */
        try {
            if (appStartActivity.getIntent().getExtras() != null) {
                if (appStartActivity.getIntent().getExtras().getBoolean(FORM_NOTICE_OPEN, false)) {
                    startMainActivityIntent.putExtra(FORM_NOTICE_OPEN, true);
                    startMainActivityIntent.putExtra(FORM_NOTICE_OPEN_DATA, (Intent) appStartActivity.getIntent().getExtras().getParcelable(FORM_NOTICE_OPEN_DATA));
                }
            }
        } catch (Exception e) {

        }
    }

    /**
     * 在MainActivity 中用
     * 判断是否是点击消息通知栏跳转过来的
     *
     * @param mainActivity 主页
     */
    public static void isAppWithNoticeOpen(Activity mainActivity) {
        try {
            if (mainActivity.getIntent().getExtras() != null) {
                if (mainActivity.getIntent().getExtras().getBoolean(FORM_NOTICE_OPEN, false)) {
                    Intent intent = mainActivity.getIntent().getExtras().getParcelable(FORM_NOTICE_OPEN_DATA);
                    if (intent == null || intent.getExtras() == null) return;
                    Intent newIntent = new Intent(mainActivity, (Class<?>) intent.getExtras().getSerializable(CALL_TO_ACTIVITY));
                    newIntent.putExtras(intent.getExtras());
                    mainActivity.startActivity(newIntent);
                }
            }
        } catch (Exception e) {

        }
    }

}
