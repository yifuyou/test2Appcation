package com.base.common.app;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.util.Log;

import com.base.common.utils.LogUtil;

/**
 * home键的监听
 * 好像已经没啥用了，好多手机都没有home键了
 */
public class HomeKeyListener {
    static final String TAG = "HomeKeyWatcher";
    private Context mContext;
    private IntentFilter mFilter;
    private OnHomePressedListener mListener;
    private HomeKeyRecevier mRecevier;

    // 回调接口
    public interface OnHomePressedListener {
        void onHomePressed();

        void onHomeLongPressed();
    }

    public HomeKeyListener(Context context) {
        mContext = context;
        mFilter = new IntentFilter(Intent.ACTION_CLOSE_SYSTEM_DIALOGS);
    }

    /**
     * 设置监听
     *
     * @param listener
     */
    public void setOnHomePressedListener(OnHomePressedListener listener) {
        mListener = listener;
        mRecevier = new HomeKeyRecevier();
    }

    /**
     * 开始监听，注册广播
     */
    public void startWatch() {
        if (mRecevier != null) {
            LogUtil.d(this.getClass().getSimpleName(), "startWatch() ");
            mContext.registerReceiver(mRecevier, mFilter);
        }
    }

    /**
     * 停止监听，注销广播
     */
    public void stopWatch() {
        if (mRecevier != null) {
            LogUtil.d(this.getClass().getSimpleName(), "stopWatch() ");
            mContext.unregisterReceiver(mRecevier);
        }
    }

    /**
     * 广播接收者
     */
    class HomeKeyRecevier extends BroadcastReceiver {
        final String SYSTEM_DIALOG_REASON_KEY = "reason";
        final String SYSTEM_DIALOG_REASON_GLOBAL_ACTIONS = "globalactions";
        final String SYSTEM_DIALOG_REASON_RECENT_APPS = "recentapps";
        final String SYSTEM_DIALOG_REASON_HOME_KEY = "homekey";

        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (action.equals(Intent.ACTION_CLOSE_SYSTEM_DIALOGS)) {
                String reason = intent.getStringExtra(SYSTEM_DIALOG_REASON_KEY);
                if (reason != null) {
                    Log.e(TAG, "action:" + action + ",reason:" + reason);
                    if (mListener != null) {
                        if (reason.equals(SYSTEM_DIALOG_REASON_HOME_KEY)) {
                            // 短按home键
                            mListener.onHomePressed();
                        } else if (reason
                                .equals(SYSTEM_DIALOG_REASON_RECENT_APPS)) {
                            // 长按home键
                            mListener.onHomeLongPressed();
                        }
                    }
                }
            }
        }
    }

}
