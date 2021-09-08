package com.base.common.service;

import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.Network;
import android.os.Build;

import androidx.annotation.NonNull;
import androidx.core.app.JobIntentService;
//import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.base.common.app.BaseConstant;
import com.base.common.utils.SystemUtils;
import com.jeremyliao.liveeventbus.LiveEventBus;


/**
 * @date 2019-04-22 13:34
 */
public class NetworkService extends JobIntentService {

    private final static int JOB_ID = 1;
    private NetworkBroadCastReceiver mReceiver;

    @Override
    public void onDestroy() {
        super.onDestroy();
        unregisterNetwork(this);
    }

    public static void enqueueWork(Context context) {
        enqueueWork(context, NetworkService.class, JOB_ID, new Intent());
    }

    @Override
    protected void onHandleWork(@NonNull Intent intent) {
        registerNetwork(this);
    }

    private void registerNetwork(Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivityManager != null) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {

                connectivityManager.registerDefaultNetworkCallback(new ConnectivityManager.NetworkCallback() {
                    @Override
                    public void onAvailable(@NonNull Network network) {
                        super.onAvailable(network);
                        LiveEventBus.get(BaseConstant.NetworkEvent, Boolean.class).post(true);
                    }

                    @Override
                    public void onLost(@NonNull Network network) {
                        super.onLost(network);
                        LiveEventBus.get(BaseConstant.NetworkEvent, Boolean.class).post(false);
                    }
                });
            } else {
                IntentFilter filter = new IntentFilter();
                filter.addAction(ConnectivityManager.CONNECTIVITY_ACTION);
                mReceiver = new NetworkBroadCastReceiver();
//                LocalBroadcastManager.getInstance(context).registerReceiver(mReceiver, filter);
            }
        } else {
            LiveEventBus.get(BaseConstant.NetworkEvent, Boolean.class).post(false);
        }
    }

    private void unregisterNetwork(Context context) {

        if (mReceiver != null) {
//            LocalBroadcastManager.getInstance(context).unregisterReceiver(mReceiver);
        }
    }


}
