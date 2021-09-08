package com.base.common.utils;

import android.content.Context;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkInfo;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;

import static android.content.Context.CONNECTIVITY_SERVICE;

public class NetworkUtils {

    public static final int NETWORK_STATE_BREAK = -1;//网络断开
    public static final int NETWORK_STATE_CONNET = 0;//wifi移动都连接
    public static final int NETWORK_STATE_MOBILE = 1;//移动数据连接
    public static final int NETWORK_STATE_WIFI = 2;//wifi


    public static int networkState = -1;//当前网络状态

    public static int getNetworkType(Context context) {

        ConnectivityManager manager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (manager != null) {

            int state = 0;//连接状态  0 未连接  1移动数据 2wifi  3 wifi和移动数据

            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
                //获取所有网络连接的信息
                Network[] networks = manager.getAllNetworks();
                //通过循环将网络信息逐个取出来
                for (int i = 0; i < networks.length; i++) {
                    //获取ConnectivityManager对象对应的NetworkInfo对象
                    NetworkInfo networkInfo = manager.getNetworkInfo(networks[i]);
                    if (networkInfo != null) {
                        if (networkInfo.getType() == ConnectivityManager.TYPE_MOBILE) {
                            state += 1;
                        } else if (networkInfo.getType() == ConnectivityManager.TYPE_WIFI) {
                            state += 2;
                        }
                    }

                }

            } else {
                //获取ConnectivityManager对象对应的NetworkInfo对象
                //获取WIFI连接的信息
                NetworkInfo wifiNetworkInfo = manager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
                //获取移动数据连接的信息
                NetworkInfo dataNetworkInfo = manager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);

                if (dataNetworkInfo != null && dataNetworkInfo.isConnected()) {
                    state += 1;
                }
                if (wifiNetworkInfo != null && wifiNetworkInfo.isConnected()) {
                    state += 2;
                }

            }


            switch (state) {
                case 0:
                    return NETWORK_STATE_BREAK;
                case 1:
                    return NETWORK_STATE_MOBILE;
                case 2:
                    return NETWORK_STATE_WIFI;
                case 3:
                    return NETWORK_STATE_CONNET;
            }

        }

        return NETWORK_STATE_BREAK;
    }





    /**
     * 检测网络是否连接
     *
     * @param context
     * @return
     */
    public static boolean isNetworkAvailable(Context context) {
        return (isWiFiActive(context) || isNetAvailable(context));
    }

    /**
     * 判断移动网络是否已经连接
     *
     * @param context
     * @return
     */
    private static boolean isNetAvailable(Context context) {
        ConnectivityManager connectivity = (ConnectivityManager) context.getSystemService(CONNECTIVITY_SERVICE);
        if (connectivity == null) {
            return false;
        } else {
            NetworkInfo info = connectivity.getActiveNetworkInfo();
            if (info != null && info.isConnected()) {
                if (info.getState() == NetworkInfo.State.CONNECTED) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * 判断wifi是否已经连接
     *
     * @param context
     * @return
     */
    public static boolean isWiFiActive(Context context) {
        WifiManager mWifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
        WifiInfo wifiInfo = mWifiManager.getConnectionInfo();
        int ipAddress = wifiInfo == null ? 0 : wifiInfo.getIpAddress();
        if (mWifiManager.isWifiEnabled() && ipAddress != 0) {
            return true;
        } else {
            return false;
        }
    }




    public static boolean isWifiConnected(Context context) {
        if (context != null) {
            ConnectivityManager manager = (ConnectivityManager) context
                    .getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo mNetworkInfo = manager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
            if (mNetworkInfo != null) {
                return mNetworkInfo.isAvailable();
            }
        }

        return false;
    }

    public static boolean isConnected(Context context) {
        if (context != null) {
            ConnectivityManager manager = (ConnectivityManager) context
                    .getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo mNetworkInfo = manager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
            if (mNetworkInfo != null) {
                return mNetworkInfo.isAvailable();
            }
        }

        return false;
    }

    /**
     * 判断GPS是否开启，GPS或者AGPS开启一个就认为是开启的
     *
     * @param context
     * @return true 表示开启
     */
    public static final boolean isOpenGPS(final Context context) {
        LocationManager locationManager
                = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
        // 通过GPS卫星定位，定位级别可以精确到街（通过24颗卫星定位，在室外和空旷的地方定位准确、速度快）
        boolean gps = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        if (gps) {
            return true;
        }

        return false;
    }
}
