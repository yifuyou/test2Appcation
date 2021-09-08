package com.base.common.utils;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ActivityManager;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.provider.Settings;
import android.telephony.PhoneNumberUtils;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.webkit.MimeTypeMap;
import android.widget.Toast;

import androidx.core.app.ActivityCompat;
import androidx.core.content.FileProvider;

import com.base.common.app.AppManager;
import com.base.common.app.BaseConstant;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import static android.content.Context.CLIPBOARD_SERVICE;


public class SystemUtils {

    //系统剪贴板-复制:   s为内容
    public static void copy(String s) {
        // 获取系统剪贴板
        ClipboardManager clipboard = (ClipboardManager) UIUtils.getContext().getSystemService(Context.CLIPBOARD_SERVICE);
        // 创建一个剪贴数据集，包含一个普通文本数据条目（需要复制的数据）
        ClipData clipData = ClipData.newPlainText(null, s);
        // 把数据集设置（复制）到剪贴板
        clipboard.setPrimaryClip(clipData);
    }


    /**
     * 获取剪贴板内容
     *
     * @param mContext
     * @return
     */
    public String getClipData(Context mContext) {
        ClipboardManager cm = (ClipboardManager) mContext.getSystemService(CLIPBOARD_SERVICE);
        ClipData data = cm.getPrimaryClip();
        if (data == null || data.getItemCount() == 0) return null;
        ClipData.Item item = data.getItemAt(0);
        String content = item.getText().toString();
        return content;
    }

    // 获取当前版本的版本号
    public static String getVersionName() {
        try {
            PackageManager packageManager = UIUtils.getContext().getPackageManager();
            PackageInfo packageInfo = packageManager.getPackageInfo(UIUtils.getContext().getPackageName(), 0);
            return packageInfo.versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
            return "";
        }
    }

    // 获取当前应用的版本号
    public static int getVersionCode() {
        try {
            PackageManager packageManager = UIUtils.getContext().getApplicationContext().getPackageManager();
            PackageInfo packInfo = packageManager.getPackageInfo(UIUtils.getContext().getApplicationContext().getPackageName(), 0);
            return packInfo.versionCode;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 1;
    }


    // 获取手机系统的版本号
    public static String getSystemVersion() {
        return Build.VERSION.RELEASE;
    }

    // 获取手机型号
    public static String getPhoneModel() {
        return Build.MODEL;
    }

    // 获取手机sdk版本
    public static int getPhoneSDKVersion() {
        return Build.VERSION.SDK_INT;
    }

    // 获取手机厂商
    public static String getPhoneSeller() {
        return Build.MANUFACTURER;
    }

    public static boolean isNetworkConnected(Context context) {
        if (context != null) {
            ConnectivityManager manager = (ConnectivityManager) context
                    .getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo mNetworkInfo = manager.getActiveNetworkInfo();
            if (mNetworkInfo != null) {
                return mNetworkInfo.isAvailable();
            }
        }
        return false;
    }


    // IMEI码
    //        <!--读取手机信息权限 -->
    //    <uses-permission android:name="android.permission.READ_PHONE_STATE" />
    @SuppressLint("HardwareIds")
    public static String getDeviceId() {
        return getAndroidId();
//        TelephonyManager mTelephony = (TelephonyManager) UIUtils.getContext().getSystemService(Context.TELEPHONY_SERVICE);
//        if (mTelephony == null || ActivityCompat.checkSelfPermission(UIUtils.getContext(), Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
//            return   return mTelephony.getDeviceId();
//        } else
    }

    public static Integer getDeviceIdInt() throws Settings.SettingNotFoundException {
        return Settings.Secure.getInt(UIUtils.getContext().getContentResolver(), Settings.Secure.ANDROID_ID);
    }

    //获取androidid
    @SuppressLint("HardwareIds")
    private static String getAndroidId() {
        return Settings.Secure.getString(UIUtils.getContext().getContentResolver(), Settings.Secure.ANDROID_ID);
    }


    public static void callPhone(Activity activity, String phoneNo) {
        if (phoneNo == null || phoneNo.equals("")) return;
        /** 拨打电话*/
        Intent intent = new Intent(Intent.ACTION_DIAL);//需要客户自己拨打
        //intent = new Intent(Intent.ACTION_CALL);//帮客户拨通
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        if (!phoneNo.startsWith("tel:")) {
            phoneNo = "tel:" + phoneNo;
        }
//        phoneNo = phoneNo.replaceAll("-", "");
        Uri data = Uri.parse(phoneNo);
        intent.setData(data);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ActivityCompat.checkSelfPermission(activity, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                activity.requestPermissions(new String[]{Manifest.permission.CALL_PHONE}, 1);
            } else activity.startActivity(intent);

        } else activity.startActivity(intent);

    }

    /**
     * 发送短信(掉起发短信页面)
     *
     * @param tel     电话号码
     * @param content 短息内容
     */
    public static void sendMessage(Activity activity, String tel, String content) {

        if (PhoneNumberUtils.isGlobalPhoneNumber(tel)) {
            Intent intent = new Intent(Intent.ACTION_SENDTO, Uri.parse("smsto:" + tel));
            intent.putExtra("sms_body", content);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                if (ActivityCompat.checkSelfPermission(activity, Manifest.permission.SEND_SMS) != PackageManager.PERMISSION_GRANTED) {
                    activity.requestPermissions(new String[]{Manifest.permission.SEND_SMS}, 1);
                } else activity.startActivity(intent);

            } else activity.startActivity(intent);
        }


    }

    public static void callQQ(Context context, String qq) {
        String url = "mqqwpa://im/chat?chat_type=wpa&uin=" + qq;
        context.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(url)));
    }


    /**
     * 判断qq是否可用     *      * @param context     * @return
     */
    public static boolean isQQClientAvailable(Context context) {
        final PackageManager packageManager = context.getPackageManager();
        List<PackageInfo> pinfo = packageManager.getInstalledPackages(0);
        if (pinfo != null) {
            for (int i = 0; i < pinfo.size(); i++) {
                String pn = pinfo.get(i).packageName;
                if (pn.equals("com.tencent.mobileqq")) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * 判断 用户是否安装微信客户端
     */
    public static boolean isWeixinAvilible(Context context) {
        final PackageManager packageManager = context.getPackageManager();// 获取packagemanager
        List<PackageInfo> pinfo = packageManager.getInstalledPackages(0);// 获取所有已安装程序的包信息
        if (pinfo != null) {
            for (int i = 0; i < pinfo.size(); i++) {
                String pn = pinfo.get(i).packageName;
                if (pn.equals("com.tencent.mm")) {
                    return true;
                }
            }
        }
        return false;
    }

    /*
     * 检查手机上是否安装了指定的软件
     *
     * @param context
     *
     * @param packageName：应用包名
     *
     * @return
     */
    public static boolean isAvilible(Context context, String packageName) {
        // 获取packagemanager
        final PackageManager packageManager = context.getPackageManager();
        // 获取所有已安装程序的包信息
        List<PackageInfo> packageInfos = packageManager.getInstalledPackages(0);
        // 用于存储所有已安装程序的包名
        List<String> packageNames = new ArrayList<String>();
        // 从pinfo中将包名字逐一取出，压入pName list中

        PackageManager pm = context.getPackageManager();

        if (packageInfos != null) {
            for (int i = 0; i < packageInfos.size(); i++) {
                String packName = packageInfos.get(i).packageName;
                String versionName = packageInfos.get(i).versionName;

                if (versionName.equals("10.43.7")) {
                    String appName = packageInfos.get(i).applicationInfo.loadLabel(pm).toString();
                    packageNames.add(packName);
                }

            }
        }

        return true;
        // 判断packageNames中是否有目标程序的包名，有TRUE，没有FALSE
//        return packageNames.contains(packageName);
    }


    public static void gotoWazeMaps(Context context, String label, double mLatitude, double mLongitude) {
//        final String uri = String.format(Locale.ENGLISH, "geo:%f,%f", mLatitude, mLongitude);

        final String uri = String.format(Locale.getDefault(), "geo:0,0?q=") + android.net.Uri.encode(String.format("%s@%f,%f", label, mLatitude, mLongitude), "UTF-8");

        final Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(uri));
        context.startActivity(intent);
    }


    public static void gotoGoogleMaps(Context context, double mLatitude, double mLongitude) {
        if (isAvilible(context, "com.google.android.apps.maps")) {


            Uri gmmIntentUri = Uri.parse("google.navigation:q=" + mLatitude + "," + mLongitude);//直接导航的

            Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);

            mapIntent.setPackage("com.google.android.apps.maps");

            context.startActivity(mapIntent);


        } else {
            UIUtils.showToastSafesClose("您尚未安装谷歌地图");


//            Uri uri = Uri.parse("market://details?id=com.google.android.apps.maps");
//            Intent intent = new Intent(Intent.ACTION_VIEW, uri);
//            context.startActivity(intent);
        }

    }


    public static void gotoGoogleMaps(Context context, double startLatitude, double startLongitude, double mLatitude, double mLongitude) {

        Uri gmmIntentUri = Uri.parse("https://www.google.com/maps/dir/?api=1&" +

                "origin=" + startLatitude + "," + startLongitude + "&" +

                "destination=" + mLatitude + "," + mLongitude);

        Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);

        mapIntent.setPackage("com.google.android.apps.maps");

        context.startActivity(mapIntent);
    }


    //获取当前屏幕是否开启旋转  /0为关闭 1为开启

    public static int getScreenState() {
        return Settings.System.getInt(UIUtils.getContext().getContentResolver(), Settings.System.ACCELEROMETER_ROTATION, 0);
    }

    //设置当前屏幕是否开启旋转  /0为关闭 1为开启
    public static void setScreenState(int state) {
        Settings.System.putInt(UIUtils.getContext().getContentResolver(), Settings.System.ACCELEROMETER_ROTATION, state);
    }

    //设置当前屏幕竖屏
    public static void setScreenVertical(Activity activity) {
        //  activity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        //保持竖屏
        if (activity.getRequestedOrientation() != ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE)
            activity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
    }

    //设置当前屏幕横屏
    public static void setScreenHorizontal(Activity activity) {
        activity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
    }

    //设置当前屏幕默认跟随系统
    public static void setScreenDefault(Activity activity) {
        activity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED);
    }

    /**
     * 判断手机设备是否安装指定包名的apk应用程序
     *
     * @param context
     * @param packageName
     * @return
     */
    public static boolean isSpecialApplInstalled(Context context, String packageName) {
        PackageManager packageManager = context.getPackageManager();
        try {
            packageManager.getPackageInfo(packageName, 0);
            return true;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return false;
    }


    public static void install(Context context, File file) {
        Intent var2 = new Intent();
        var2.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        var2.setAction(Intent.ACTION_VIEW);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            Uri uriForFile = FileProvider.getUriForFile(context, BaseConstant.AUTHORITY, file);
            var2.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            var2.setDataAndType(uriForFile, context.getContentResolver().getType(uriForFile));
        } else {
            var2.setDataAndType(Uri.fromFile(file), getMIMEType(file));
        }
        try {
            context.startActivity(var2);
        } catch (Exception var5) {
            var5.printStackTrace();
            UIUtils.showToastSafes("没有找到打开此类文件的程序");
        }

    }


    private static String getMIMEType(File var0) {
        String var1 = "";
        String var2 = var0.getName();
        String var3 = var2.substring(var2.lastIndexOf(".") + 1, var2.length()).toLowerCase();
        var1 = MimeTypeMap.getSingleton().getMimeTypeFromExtension(var3);
        return var1;
    }

    /**
     * 获取apk包的信息：版本号，名称，图标等
     *
     * @param absPath apk包的绝对路径
     */
    public static String getApkVersionName(String absPath) {

        PackageManager pm = UIUtils.getContext().getPackageManager();
        PackageInfo pkgInfo = pm.getPackageArchiveInfo(absPath, PackageManager.GET_ACTIVITIES);

        if (pkgInfo != null) {
            ApplicationInfo appInfo = pkgInfo.applicationInfo;
            /* 必须加这两句，不然下面icon获取是default icon而不是应用包的icon */
            appInfo.sourceDir = absPath;
            appInfo.publicSourceDir = absPath;
            String appName = pm.getApplicationLabel(appInfo).toString();// 得到应用名
            String packageName = appInfo.packageName; // 得到包名
            String version = pkgInfo.versionName; // 得到版本信息

            /* icon1和icon2其实是一样的 */
            //     Drawable icon1 = pm.getApplicationIcon(appInfo);// 得到图标信息
            //    Drawable icon2 = appInfo.loadIcon(pm);
            //    String pkgInfoStr = String.format("PackageName:%s, Vesion: %s, AppName: %s", packageName, version, appName);
            //  Log.i(TAG, String.format("PkgInfo: %s", pkgInfoStr));
            return version;
        }
        return "1.0";
    }

    public static int getApkVersionCode(String absPath) {

        PackageManager pm = UIUtils.getContext().getPackageManager();
        PackageInfo pkgInfo = pm.getPackageArchiveInfo(absPath, PackageManager.GET_ACTIVITIES);

        if (pkgInfo != null) {
            ApplicationInfo appInfo = pkgInfo.applicationInfo;
            /* 必须加这两句，不然下面icon获取是default icon而不是应用包的icon */
            appInfo.sourceDir = absPath;
            appInfo.publicSourceDir = absPath;
            String appName = pm.getApplicationLabel(appInfo).toString();// 得到应用名
            String packageName = appInfo.packageName; // 得到包名
            int version = pkgInfo.versionCode; // 得到版本信息

            /* icon1和icon2其实是一样的 */
            //     Drawable icon1 = pm.getApplicationIcon(appInfo);// 得到图标信息
            //    Drawable icon2 = appInfo.loadIcon(pm);
            //    String pkgInfoStr = String.format("PackageName:%s, Vesion: %s, AppName: %s", packageName, version, appName);
            //  Log.i(TAG, String.format("PkgInfo: %s", pkgInfoStr));
            return version;
        }
        return 0;
    }


    /**
     * 判断某个服务是否正在运行的方法
     *
     * @param context
     * @param className 服务的类名（例如：net.loonggg.testbackstage.TestService）
     * @return true代表正在运行，false代表服务没有正在运行
     */
    public static boolean isServiceExisted(Context context, String className) {
        ActivityManager activityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        if (activityManager == null) return false;
        List<ActivityManager.RunningServiceInfo> serviceList = activityManager.getRunningServices(Integer.MAX_VALUE);

        if (!(serviceList.size() > 0)) {
            return false;
        }

        for (int i = 0; i < serviceList.size(); i++) {
            ActivityManager.RunningServiceInfo serviceInfo = serviceList.get(i);
            ComponentName serviceName = serviceInfo.service;

            if (serviceName.getClassName().equals(className)) {
                return true;
            }
        }
        return false;
    }


    /**
     * 判断某个进程是否正在运行的方法
     *
     * @param context
     * @param proessName 进程的名字
     * @return true代表正在运行，false代表服务没有正在运行
     */
    public static boolean isProessRunning(Context context, String proessName) {

        boolean isRunning = false;
        ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);

        List<ActivityManager.RunningAppProcessInfo> lists = am.getRunningAppProcesses();
        for (ActivityManager.RunningAppProcessInfo info : lists) {
            if (info.processName.equals(proessName)) {
                isRunning = true;
            }
        }

        return isRunning;
    }

    public static boolean isAppRunning() {
        return AppManager.getInstance().hasActivity("MainActivity");
    }

    public static void startAppLocationSetting(Activity context, int... requestCode) {
        Intent locationIntent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
        if (requestCode.length > 0) context.startActivityForResult(locationIntent, requestCode[0]);
        else context.startActivity(locationIntent);
    }

    public static void startAppSettings(Context context) {
        Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        intent.setData(Uri.parse("package:" + context.getPackageName()));
        context.startActivity(intent);
    }

    public static void startManageApplications(Context context) {
        Intent intent = new Intent();
        intent.setAction("android.intent.action.MAIN");
        intent.setClassName("com.android.settings", "com.android.settings.ManageApplications");
        context.startActivity(intent);
    }

    /**
     * 跳转到应用详情页面
     */
    public static void startManageApplicationInfo(Context context) {
        Intent intent = new Intent();
        intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        Uri uri = Uri.fromParts("package", context.getPackageName(), null);
        intent.setData(uri);
        context.startActivity(intent);
    }

    /**
     * 跳转到权限管理页面
     */
    public static void startManageJurisdiction(Activity context, int... location_jurisdiction_requestCode) {

        String model = Build.MODEL; // 手机型号
        String release = Build.VERSION.RELEASE; // android系统版本号
        String brand = Build.BRAND;//手机厂商
        if (TextUtils.equals(brand.toLowerCase(), "redmi") || TextUtils.equals(brand.toLowerCase(), "xiaomi")) {
            gotoMiuiPermission(context, location_jurisdiction_requestCode);//小米
        } else if (TextUtils.equals(brand.toLowerCase(), "meizu")) {
            gotoMeizuPermission(context, location_jurisdiction_requestCode);
        } else if (TextUtils.equals(brand.toLowerCase(), "huawei") || TextUtils.equals(brand.toLowerCase(), "honor")) {
            gotoHuaweiPermission(context, location_jurisdiction_requestCode);
        } else {
            if (location_jurisdiction_requestCode.length > 0)
                context.startActivityForResult(getAppDetailSettingIntent(context), location_jurisdiction_requestCode[0]);
            else context.startActivity(getAppDetailSettingIntent(context));
        }

    }


    /**
     * 跳转到miui的权限管理页面
     */
    private static void gotoMiuiPermission(Activity context, int... location_jurisdiction_requestCode) {
        try { // MIUI 8
            Intent localIntent = new Intent("miui.intent.action.APP_PERM_EDITOR");
            localIntent.setClassName("com.miui.securitycenter", "com.miui.permcenter.permissions.PermissionsEditorActivity");
            localIntent.putExtra("extra_pkgname", context.getPackageName());

            if (location_jurisdiction_requestCode.length > 0)
                context.startActivityForResult(localIntent, location_jurisdiction_requestCode[0]);
            else context.startActivity(localIntent);

        } catch (Exception e) {
            try { // MIUI 5/6/7
                Intent localIntent = new Intent("miui.intent.action.APP_PERM_EDITOR");
                localIntent.setClassName("com.miui.securitycenter", "com.miui.permcenter.permissions.AppPermissionsEditorActivity");
                localIntent.putExtra("extra_pkgname", context.getPackageName());

                if (location_jurisdiction_requestCode.length > 0)
                    context.startActivityForResult(localIntent, location_jurisdiction_requestCode[0]);
                else context.startActivity(localIntent);

            } catch (Exception e1) {
                // 否则跳转到应用详情
                if (location_jurisdiction_requestCode.length > 0)
                    context.startActivityForResult(getAppDetailSettingIntent(context), location_jurisdiction_requestCode[0]);
                else context.startActivity(getAppDetailSettingIntent(context));
            }
        }
    }

    /**
     * 跳转到魅族的权限管理系统
     */
    private static void gotoMeizuPermission(Activity context, int... location_jurisdiction_requestCode) {
        try {
            Intent intent = new Intent("com.meizu.safe.security.SHOW_APPSEC");
            intent.addCategory(Intent.CATEGORY_DEFAULT);
            intent.putExtra("packageName", context.getPackageName());

            if (location_jurisdiction_requestCode.length > 0)
                context.startActivityForResult(intent, location_jurisdiction_requestCode[0]);
            else context.startActivity(intent);

        } catch (Exception e) {
            // 否则跳转到应用详情
            if (location_jurisdiction_requestCode.length > 0)
                context.startActivityForResult(getAppDetailSettingIntent(context), location_jurisdiction_requestCode[0]);
            else context.startActivity(getAppDetailSettingIntent(context));
        }
    }

    /**
     * 华为的权限管理页面
     */
    private static void gotoHuaweiPermission(Activity context, int... location_jurisdiction_requestCode) {
        try {
            Intent intent = new Intent();
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            ComponentName comp = new ComponentName("com.huawei.systemmanager", "com.huawei.permissionmanager.ui.MainActivity");
            //华为权限管理
            intent.setComponent(comp);

            if (location_jurisdiction_requestCode.length > 0)
                context.startActivityForResult(intent, location_jurisdiction_requestCode[0]);
            else context.startActivity(intent);
        } catch (Exception e) {
            // 否则跳转到应用详情
            if (location_jurisdiction_requestCode.length > 0)
                context.startActivityForResult(getAppDetailSettingIntent(context), location_jurisdiction_requestCode[0]);
            else context.startActivity(getAppDetailSettingIntent(context));
        }
    }

    /**
     * 获取应用详情页面intent（如果找不到要跳转的界面，也可以先把用户引导到系统设置页面）     *     * @return
     */
    private static Intent getAppDetailSettingIntent(Context context) {
        Intent localIntent = new Intent();
        localIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        if (Build.VERSION.SDK_INT >= 9) {
            localIntent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
            localIntent.setData(Uri.fromParts("package", context.getPackageName(), null));
        } else if (Build.VERSION.SDK_INT <= 8) {
            localIntent.setAction(Intent.ACTION_VIEW);
            localIntent.setClassName("com.android.settings", "com.android.settings.InstalledAppDetails");
            localIntent.putExtra("com.android.settings.ApplicationPkgName", context.getPackageName());
        }
        return localIntent;
    }

}
