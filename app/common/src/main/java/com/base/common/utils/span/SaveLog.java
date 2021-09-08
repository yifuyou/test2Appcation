package com.base.common.utils.span;

import android.content.Context;
import android.os.Environment;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;

import com.base.common.BuildConfig;
import com.base.common.permission.PermissionUtils;
import com.base.common.utils.IOUtils;
import com.base.common.viewmodel.BaseRxObserver;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import io.reactivex.annotations.NonNull;

public class SaveLog {
    private static Boolean MYLOG_SWITCH = true; // 日志文件总开关
    private static Boolean MYLOG_WRITE_TO_FILE = true;// 日志写入文件开关
    private static char MYLOG_TYPE = 'v';// 输入日志类型，w代表只输出告警信息等，v代表输出所有信息
    private static String MYLOG_PATH_SDCARD_DIR = "/qiuzy/log";// 日志文件在sdcard中的路径
    private static int SDCARD_LOG_FILE_SAVE_DAYS = 0;// sd卡中日志文件的最多保存天数
    private static String MYLOGFILEName = "Log.txt";// 本类输出的日志文件名称
    private static SimpleDateFormat myLogSdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");// 日志的输出格式
    private static SimpleDateFormat logfile = new SimpleDateFormat("yyyy-MM-dd");// 日志文件格式
    public Context context;

    /**
     * 打开日志文件并写入日志
     *
     * @param mylogtype
     * @param tag
     * @param text
     */
    public static void writeLogToFile(Fragment fragment, String mylogtype, String tag, String text) {// 新建或打开日志文件
        if (!BuildConfig.DEBUG) {
            return;
        }
        PermissionUtils.initStoragePermission(fragment).subscribe(new BaseRxObserver<Boolean>() {
            @Override
            public void onNext(@NonNull Boolean aBoolean) {
                if (aBoolean) {
                    writeLogToFile(mylogtype, tag, text);
                }
            }
        });

    }

    /**
     * 打开日志文件并写入日志
     *
     * @param mylogtype
     * @param tag
     * @param text
     */
    public static void writeLogToFile(FragmentActivity activity, String mylogtype, String tag, String text) {// 新建或打开日志文件
        if (!BuildConfig.DEBUG) {
            return;
        }
        PermissionUtils.initStoragePermission(activity).subscribe(new BaseRxObserver<Boolean>() {
            @Override
            public void onNext(@NonNull Boolean aBoolean) {
                if (aBoolean) {
                    writeLogToFile(mylogtype, tag, text);
                }
            }
        });

    }

    public static void writeLogToFile(String mylogtype, String tag, String text) {
        if (!BuildConfig.DEBUG) {
            return;
        }
        IOUtils.initRootDirectory();

        Date nowtime = new Date();
        String needWriteFiel = logfile.format(nowtime);
        String needWriteMessage = myLogSdf.format(nowtime) + "    " + mylogtype + "    " + tag + "    " + text;
        String parent = Environment.getExternalStorageDirectory().getPath() + MYLOG_PATH_SDCARD_DIR;
        File dirsFile = new File(parent);
        if (!dirsFile.exists()) {
            dirsFile.mkdirs();
        }
        //Log.i("创建文件","创建文件");
        File file = new File(dirsFile.toString(), needWriteFiel + MYLOGFILEName);// MYLOG_PATH_SDCARD_DIR
        if (!file.exists()) {
            try {
                //在指定的文件夹中创建文件
                file.createNewFile();
            } catch (Exception e) {
            }
        }

        try {
            FileWriter filerWriter = new FileWriter(file, true);// 后面这个参数代表是不是要接上文件中原来的数据，不进行覆盖
            BufferedWriter bufWriter = new BufferedWriter(filerWriter);
            bufWriter.write(needWriteMessage);
            bufWriter.newLine();
            bufWriter.close();
            filerWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
