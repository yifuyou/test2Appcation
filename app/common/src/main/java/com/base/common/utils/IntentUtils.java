package com.base.common.utils;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;

import java.io.File;
import java.security.InvalidParameterException;

/**
 * 返回常用Intent
 * Created by maoqi on 2018/7/13.
 */
public class IntentUtils {

    /**
     * 安装Apk Intent
     *
     * @param context
     * @param file
     * @return
     */
    public static Intent buildInstallApkIntent(Context context, File file) {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.N) {
            intent.setDataAndType(Uri.fromFile(file), "application/vnd.android.package-archive");
        } else {
            intent.setDataAndType(UriPathUtils.getUriFromFile(context, file), "application/vnd.android.package-archive");
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        }
        return intent;
    }

    /***
     * 照相Intent
     * @return
     */
    public static Intent buildTakePhotoIntent(Uri uri) {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            intent.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        }
        intent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
        return intent;
    }

    /***
     * 打开通知Intent
     * @return
     */
    public static Intent buildOpenPushIntent(Context context) {
        Intent intent = new Intent();
        //android 8.0引导
        if (Build.VERSION.SDK_INT >= 26) {
            intent.setAction("android.settings.APP_NOTIFICATION_SETTINGS");
            intent.putExtra("android.provider.extra.APP_PACKAGE", context.getPackageName());
        }
        //android 5.0-7.0
        if (Build.VERSION.SDK_INT >= 21 && Build.VERSION.SDK_INT < 26) {
            intent.setAction("android.settings.APP_NOTIFICATION_SETTINGS");
            intent.putExtra("app_package", context.getPackageName());
            intent.putExtra("app_uid", context.getApplicationInfo().uid);
        }
        //其他
        if (Build.VERSION.SDK_INT < 21) {
            intent.setAction("android.settings.APPLICATION_DETAILS_SETTINGS");
            intent.setData(Uri.fromParts("package", context.getPackageName(), null));
        }
        return intent;
    }

    /**
     * 打开外部浏览器
     *
     * @return
     */
    public static Intent buildOpenBrowser(String url) {
        Uri content_url = Uri.parse(url);
        Intent intent = new Intent(Intent.ACTION_VIEW, content_url);
        return intent;
    }

    /***
     * 进入相册选择图片Intent
     * @return
     */
    public static Intent buildChoosePhotoAlbumIntent() {
        Intent intent = new Intent();
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        intent.setType("image/*");
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.KITKAT) {
            intent.setAction(Intent.ACTION_GET_CONTENT);
        } else {
            intent.setAction(Intent.ACTION_OPEN_DOCUMENT);
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            intent.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        }

        return intent;
    }

    /***
     * 进入相册选择视频Intent
     * @return
     */
    public static Intent buildChooseVideoAlbumIntent() {
        Intent intent = new Intent();
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        intent.setType("video/*");
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.KITKAT) {
            intent.setAction(Intent.ACTION_GET_CONTENT);
        } else {
            intent.setAction(Intent.ACTION_OPEN_DOCUMENT);
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            intent.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        }

        return intent;
    }

    public static CropPhotoBuilder cropPhotoBuilder(Uri uri) {
        return new CropPhotoBuilder(uri);
    }

    public static class CropPhotoBuilder {
        private Uri uri;
        private int aspectX = 1;
        private int aspectY = 1;
        private int outputX;
        private int outputY;
        private boolean scale = true;
        private boolean return_data;
        private File extra_output;

        private CropPhotoBuilder(Uri uri) {
            this.uri = uri;
        }

        public CropPhotoBuilder aspect(int x, int y) {
            this.aspectX = x;
            this.aspectY = y;
            return this;
        }

        public CropPhotoBuilder output(int x, int y) {
            this.outputX = x;
            this.outputY = y;
            return this;
        }

        public CropPhotoBuilder scale(boolean scale) {
            this.scale = scale;
            return this;
        }

        public CropPhotoBuilder returnBitmapData() {
            this.return_data = true;
            return this;
        }

        public CropPhotoBuilder returnFileData(File file) {
            this.return_data = false;
            this.extra_output = file;
            return this;
        }

        public Intent build() {

            Intent intent = new Intent("com.android.camera.action.CROP");
            intent.setDataAndType(uri, "image/*");
            intent.putExtra("crop", "true");
            intent.putExtra("aspectX", aspectX);
            intent.putExtra("aspectY", aspectY);
            if (outputX == 0 || outputY == 0) {
                throw new InvalidParameterException("outputX和outputY为必须设置项");
            }
            intent.putExtra("outputX", outputX);
            intent.putExtra("outputY", outputY);
            intent.putExtra("scale", scale);
            intent.putExtra("return-data", return_data);
            //这里必须 Uri.fromFile(巨坑)
            if (extra_output == null) {
                if (!return_data) {
                    throw new InvalidParameterException("return-data为false时必须指定接收的file");
                }
            }
            intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(extra_output));
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                intent.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
                intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            }
            return intent;
        }

    }
}
