package com.base.common.service;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkInfo;

import com.base.common.app.BaseConstant;
import com.base.common.utils.LogUtil;
import com.base.common.utils.SystemUtils;
import com.jeremyliao.liveeventbus.LiveEventBus;


/**
 * @date 2019-04-22 13:50
 */
public class NetworkBroadCastReceiver extends BroadcastReceiver {


    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();
        if (ConnectivityManager.CONNECTIVITY_ACTION.equals(action)) {
            LiveEventBus.get(BaseConstant.NetworkEvent, Boolean.class).post(SystemUtils.isNetworkConnected(context));
        }
    }


}
