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

        //mTN  ????????????
//        try {
//            Object mTN;
//            mTN = JavaMethod.getFieldValue(toast, "mTN");
//            Object obj = Toast.class.getDeclaredField("mTN");
//            if (mTN != null) {
//                Object mParams = JavaMethod.getFieldValue(mTN, "mParams");
//                if (mParams instanceof WindowManager.LayoutParams) {
//                    WindowManager.LayoutParams params = (WindowManager.LayoutParams) mParams;
//                    //?????????????????????
////                    params.windowAnimations = R.style.ClickToast;
//                    //Toast?????????
//                    params.flags = WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON;
//
//                    //??????viewgroup??????
////                    params.width = WindowManager.LayoutParams.MATCH_PARENT; //??????Toast?????????????????????
////                    params.height = WindowManager.LayoutParams.WRAP_CONTENT; //????????????
//                }
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//        }

    }

    /**
     * @param text     ??????????????????
     * @param duration ??????????????????
     *                 ??????LENGTH_MAX????????????
     *                 ????????????????????????????????????
     *                 ??????????????????????????????????????????hide()
     */
    public void show(String text, int duration) {
        time = new TimeCount(duration, 1000);//1000?????????????????????
        toast_content.setText(text);
        ivImg.setImageResource(R.mipmap.ic_toast_succ);
        if (canceled) {
            time.start();
            canceled = false;
            showUntilCancel();
        }
    }

    public void show(String text, @DrawableRes int res, int duration) {
        time = new TimeCount(duration, 1000);//1000?????????????????????
        toast_content.setText(text);
        ivImg.setImageResource(res);
        if (canceled) {
            time.start();
            canceled = false;
            showUntilCancel();
        }
    }

    public void showClose(String text, int duration) {
        time = new TimeCount(duration, 1000);//1000?????????????????????
        toast_content.setText(text);
        ivImg.setImageResource(R.mipmap.ic_toast_close);
        if (canceled) {
            time.start();
            canceled = false;
            showUntilCancel();
        }
    }


    /**
     * ??????Toast
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
     * ?????????
     */
    class TimeCount extends CountDownTimer {
        public TimeCount(long millisInFuture, long countDownInterval) {
            super(millisInFuture, countDownInterval); // ?????????,?????????????????????
        }

        @Override
        public void onFinish() { // ?????????????????????
            hide();
        }

        @Override
        public void onTick(long millisUntilFinished) { // ??????????????????
        }

    }

    private static Object iNotificationManagerObj;

    /**
     * ????????????Toast
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
                        //??????????????????Toast
                        if ("enqueueToast".equals(method.getName())
                                || "enqueueToastEx".equals(method.getName())) {  //??????p20 pro??????enqueueToastEx
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
     * ????????????????????????
     *
     * @return
     */
    private static boolean isNotificationEnabled(Context context) {
        NotificationManagerCompat notificationManagerCompat = NotificationManagerCompat.from(context);
        boolean areNotificationsEnabled = notificationManagerCompat.areNotificationsEnabled();
        return areNotificationsEnabled;
    }

}
