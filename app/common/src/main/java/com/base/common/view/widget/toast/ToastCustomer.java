package com.base.common.view.widget.toast;


import android.content.Context;
import android.os.CountDownTimer;
import android.os.Handler;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;


import android.widget.Toast;

import androidx.annotation.DrawableRes;
import androidx.core.app.NotificationManagerCompat;

import com.base.common.R;
import com.base.common.utils.JavaMethod;
import com.base.common.utils.OnClickCheckedUtil;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

public class ToastCustomer {

    private boolean canceled = true;
    private Handler handler;
    private Toast toast;
    private TimeCount time;
    private TextView toast_content;
    private ImageView ivImg;

    private static ToastCustomer instance;

    public static ToastCustomer getInstance(Context context) {
        if (instance == null) {
            instance = new ToastCustomer(context);
        }
        return instance;
    }

    private ToastCustomer(Context context) {
        this(context, new Handler());
    }

    private ToastCustomer(Context context, Handler handler) {
        this.handler = handler;

        View layout = LayoutInflater.from(context).inflate(R.layout.toast_customer_succ, null, false);
        layout.findViewById(R.id.toast_custom_parent).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hide();
            }
        });
        toast_content = (TextView) layout.findViewById(R.id.tvToastContent);
        ivImg = layout.findViewById(R.id.ivImg);
        if (toast == null) {
            toast = new Toast(context);
        }
        toast.setGravity(Gravity.CENTER, 0, 0);
        toast.setDuration(Toast.LENGTH_LONG);
        toast.setView(layout);

//        if (!isNotificationEnabled(context)) {
//            setSystemToast(toast);
//        }

        //mTN  反射不到
//        try {
//            Object mTN;
//            mTN = JavaMethod.getFieldValue(toast, "mTN");
//            Object obj = Toast.class.getDeclaredField("mTN");
//            if (mTN != null) {
//                Object mParams = JavaMethod.getFieldValue(mTN, "mParams");
//                if (mParams instanceof WindowManager.LayoutParams) {
//                    WindowManager.LayoutParams params = (WindowManager.LayoutParams) mParams;
//                    //显示与隐藏动画
////                    params.windowAnimations = R.style.ClickToast;
//                    //Toast可点击
//                    params.flags = WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON;
//
//                    //设置viewgroup宽高
////                    params.width = WindowManager.LayoutParams.MATCH_PARENT; //设置Toast宽度为屏幕宽度
////                    params.height = WindowManager.LayoutParams.WRAP_CONTENT; //设置高度
//                }
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//        }

    }

    /**
     * @param text     要显示的内容
     * @param duration 显示的时间长
     *                 根据LENGTH_MAX进行判断
     *                 如果不匹配，进行系统显示
     *                 如果匹配，永久显示，直到调用hide()
     */
    public void show(String text, int duration) {
        time = new TimeCount(duration, 1000);//1000是消失渐变时间
        toast_content.setText(text);
        ivImg.setImageResource(R.mipmap.ic_toast_succ);
        if (canceled) {
            time.start();
            canceled = false;
            showUntilCancel();
        }
    }

    public void show(String text, @DrawableRes int res, int duration) {
        time = new TimeCount(duration, 1000);//1000是消失渐变时间
        toast_content.setText(text);
        ivImg.setImageResource(res);
        if (canceled) {
            time.start();
            canceled = false;
            showUntilCancel();
        }
    }

    public void showClose(String text, int duration) {
        time = new TimeCount(duration, 1000);//1000是消失渐变时间
        toast_content.setText(text);
        ivImg.setImageResource(R.mipmap.ic_toast_close);
        if (canceled) {
            time.start();
            canceled = false;
            showUntilCancel();
        }
    }


    /**
     * 隐藏Toast
     */
    public void hide() {
        if (toast != null) {
            toast.cancel();
        }
        canceled = true;
    }

    private void showUntilCancel() {
        if (canceled) {
            return;
        }
        toast.show();
        handler.postDelayed(new Runnable() {
            public void run() {
                showUntilCancel();
            }
        }, 3000);
    }

    /**
     * 计时器
     */
    class TimeCount extends CountDownTimer {
        public TimeCount(long millisInFuture, long countDownInterval) {
            super(millisInFuture, countDownInterval); // 总时长,计时的时间间隔
        }

        @Override
        public void onFinish() { // 计时完毕时触发
            hide();
        }

        @Override
        public void onTick(long millisUntilFinished) { // 计时过程显示
        }

    }

    private static Object iNotificationManagerObj;

    /**
     * 显示系统Toast
     */
    private static void setSystemToast(Toast toast) {
        try {
            Method getServiceMethod = JavaMethod.getMethod(toast, "getService");
            getServiceMethod.setAccessible(true);
            //hook INotificationManager
            if (iNotificationManagerObj == null) {
                iNotificationManagerObj = getServiceMethod.invoke(null);

                Class iNotificationManagerCls = Class.forName("android.app.INotificationManager");
                Object iNotificationManagerProxy = Proxy.newProxyInstance(toast.getClass().getClassLoader(), new Class[]{iNotificationManagerCls}, new InvocationHandler() {
                    @Override
                    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
                        //强制使用系统Toast
                        if ("enqueueToast".equals(method.getName())
                                || "enqueueToastEx".equals(method.getName())) {  //华为p20 pro上为enqueueToastEx
                            args[0] = "android";
                        }
                        return method.invoke(iNotificationManagerObj, args);
                    }
                });
                Field sServiceFiled = Toast.class.getDeclaredField("sService");
                sServiceFiled.setAccessible(true);
                sServiceFiled.set(null, iNotificationManagerProxy);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 消息通知是否开启
     *
     * @return
     */
    private static boolean isNotificationEnabled(Context context) {
        NotificationManagerCompat notificationManagerCompat = NotificationManagerCompat.from(context);
        boolean areNotificationsEnabled = notificationManagerCompat.areNotificationsEnabled();
        return areNotificationsEnabled;
    }

}
