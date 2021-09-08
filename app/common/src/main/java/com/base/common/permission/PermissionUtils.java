package com.base.common.permission;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;

import com.tbruyelle.rxpermissions2.Permission;
import com.tbruyelle.rxpermissions2.RxPermissions;

import io.reactivex.Observable;

/**
 * 权限申请
 */
public class PermissionUtils {


    public static Observable<Boolean> initGps(@NonNull FragmentActivity activity) {
        return new RxPermissions(activity).request(
                Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION
        );
    }


    public static Observable<Boolean> initRECORD_AUDIO(@NonNull FragmentActivity activity) {

        return new RxPermissions(activity).request(
                Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.RECORD_AUDIO
        );
    }


    /**
     * 申请照相机权限
     *
     * @param fragment
     * @return
     */
    public static Observable<Boolean> initCAMERAPermission(@NonNull Fragment fragment) {

        return new RxPermissions(fragment).request(
                Manifest.permission.CAMERA
                , Manifest.permission.WRITE_EXTERNAL_STORAGE
                , Manifest.permission.READ_EXTERNAL_STORAGE
        );
    }


    public static Observable<Boolean> initCAMERAPermission(@NonNull FragmentActivity activity) {
        return new RxPermissions(activity).request(
                Manifest.permission.CAMERA
                , Manifest.permission.WRITE_EXTERNAL_STORAGE
                , Manifest.permission.READ_EXTERNAL_STORAGE
        );
    }

    /**
     * 申请储存权限
     *
     * @param fragment
     * @return
     */
    public static Observable<Boolean> initStoragePermission(@NonNull Fragment fragment) {

        return new RxPermissions(fragment).request(
                Manifest.permission.WRITE_EXTERNAL_STORAGE
                , Manifest.permission.READ_EXTERNAL_STORAGE
        );
    }

    /**
     * 申请储存权限
     *
     * @param activity
     * @return
     */
    public static Observable<Boolean> initStoragePermission(@NonNull FragmentActivity activity) {

        return new RxPermissions(activity).request(
                Manifest.permission.WRITE_EXTERNAL_STORAGE
                , Manifest.permission.READ_EXTERNAL_STORAGE
        );
    }

    public static Observable<Boolean> initPhoneCallPermission(@NonNull FragmentActivity activity) {

        return new RxPermissions(activity).request(
                Manifest.permission.READ_PHONE_STATE, Manifest.permission.CALL_PHONE
                , Manifest.permission.READ_CALL_LOG, Manifest.permission.WRITE_CALL_LOG,
                Manifest.permission.ADD_VOICEMAIL, Manifest.permission.USE_SIP, Manifest.permission.PROCESS_OUTGOING_CALLS
        );
    }


    public static Observable<Permission> takePermissionArray(FragmentActivity activity, String... permissions) {
        return new RxPermissions(activity)
                .requestEach(permissions);
    }

    public static Observable<Boolean> takePermission(FragmentActivity activity, String... permissions) {
        return new RxPermissions(activity)
                .request(permissions);
    }


    public static boolean isStoragePermissionEnabel(Context context) {
        return isPermissionEnabel(context, Manifest.permission.WRITE_EXTERNAL_STORAGE);
    }

    public static boolean isPermissionEnabel(Context context, String permission) {
        return Build.VERSION.SDK_INT < Build.VERSION_CODES.M || ContextCompat.checkSelfPermission(context, permission) == PackageManager.PERMISSION_GRANTED;
    }


}
