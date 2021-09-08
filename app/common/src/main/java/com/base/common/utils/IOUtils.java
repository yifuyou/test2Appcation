package com.base.common.utils;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Environment;
import android.os.StatFs;
import android.text.TextUtils;

import androidx.annotation.NonNull;

import com.base.common.app.BaseConstant;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.RandomAccessFile;
import java.util.ArrayList;
import java.util.List;


public class IOUtils {


    public static void initRootDirectory() {
        //创建更新文件目录和图片存放目录
        IOUtils.makeRootDirectory(BaseConstant.APKSaveDir);
        IOUtils.makeRootDirectory(BaseConstant.imageSaveDir);
        IOUtils.makeRootDirectory(BaseConstant.backupSaveDir);
        IOUtils.makeRootDirectory(BaseConstant.logSaveDir);
        IOUtils.makeRootDirectory(BaseConstant.voice);
    }


    //  获取当前程序路径
    public static String getApplicationPath() {
        String path = UIUtils.getContext().getFilesDir().getAbsolutePath();
        LogUtil.d("path", path);
        return path;
    }

    //  获取该程序的安装包路径
    public static String getPackageResourcePath() {
        return UIUtils.getContext().getPackageResourcePath();
    }


    //  获取程序默认数据库路径
    public static String getDatabasePath() {
        return UIUtils.getContext().getDatabasePath("com_tdj").getAbsolutePath();
    }

    /**
     * 判断SDCard是否存在 [当没有外挂SD卡时，内置ROM也被识别为存在sd卡]
     **/
    public static boolean isSdCardExist() {
        return Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED);
    }

    public static File getFile(String path) {
        return new File(Environment.getExternalStorageDirectory(), path);
    }




    /**
     * 获取SD卡根目录路径
     *
     * @
     */
    public static String getSdCardPath() {
        if (isSdCardExist()) {
//            return Environment.getExternalStorageDirectory().getAbsolutePath();
            return UIUtils.getContext().getExternalCacheDir().getAbsolutePath();
        } else return UIUtils.getContext().getCacheDir().getAbsolutePath();
    }

    /**
     * 获取默认相册路径
     *
     * @
     */
    public static String getPicturesPath() {
        return Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES).getAbsolutePath();
    }

    public static File createFile(String absPath) {
        return createFile(getFolder(absPath), getFileNameAll(absPath));
    }

    public static File createFile(String filePath, String fileName) {
        File file = null;
        if (!filePath.endsWith("/")) {
            filePath += "/";
        }
        //如果目录不存在，则创建
        makeRootDirectory(filePath);

        try {
            file = new File(filePath + fileName);
            if (!file.exists()) {
                file.createNewFile();
            }
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return file;
    }

    public static boolean deleteFile(String absPath) {
        File file = new File(absPath);
        if (file.exists()) {
            return file.delete();
        }
        return false;
    }

    //将字节换为输入流
    public static InputStream byteToInputstream(byte[] bytes) {
        //ByteArrayOutputStream
        return new ByteArrayInputStream(bytes);
    }

    //将输入流转换为字节换
    public static byte[] byteToInputstream(InputStream inStream) throws IOException {
        ByteArrayOutputStream swapStream = new ByteArrayOutputStream();
        byte[] buff = new byte[100];
        int rc = 0;
        while ((rc = inStream.read(buff, 0, 100)) > 0) {
            swapStream.write(buff, 0, rc);
        }
        byte[] in2b = swapStream.toByteArray();
        return in2b;
    }


    /**
     * 往文件内写入数据
     *
     * @param filePath 文件路经
     * @param fileName 文件名字
     * @param content  文件内容
     */
    public static void writeFile_row(@NonNull String filePath, @NonNull String fileName, @NonNull String content) {
        if (!filePath.endsWith("/")) {
            filePath += "/";
        }
        createFile(filePath, fileName);
        try {
            RandomAccessFile raf = new RandomAccessFile(filePath + fileName, "rwd");
            raf.seek(raf.length());
            raf.write(content.getBytes());
            raf.write("\r\n".getBytes());
            raf.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void writeFile(@NonNull String filePath, @NonNull String fileName, @NonNull String content) {
        if (!filePath.endsWith("/")) {
            filePath += "/";
        }
        createFile(filePath, fileName);
        try {
            RandomAccessFile raf = new RandomAccessFile(filePath + fileName, "rwd");
            raf.seek(raf.length());
            raf.write(content.getBytes());
            raf.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     *      * 根据行读取内容
     *      * @return
     *      
     */
    public List<String> Txt(String filePath) {
        //将读出来的一行行数据使用List存储  
        List<String> newList = new ArrayList<>();
        if (TextUtils.isEmpty(filePath)) return newList;
        try {
            File file = new File(filePath);
            int count = 0;//初始化 key值  
            if (file.isFile() && file.exists()) {//文件存在  
                InputStreamReader isr = new InputStreamReader(new FileInputStream(file));
                BufferedReader br = new BufferedReader(isr);
                String lineTxt = null;
                while ((lineTxt = br.readLine()) != null) {
                    if (!"".equals(lineTxt)) {
                        String reds = lineTxt.split("\\+")[0];  //java 正则表达式  
                        newList.add(count, reds);
                        count++;
                    }
                }
                isr.close();
                br.close();
            } else {

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return newList;
    }

    /**
     *      * 根据行读取内容
     *      * @return
     *      
     */
    public static List<String> readTxtFile(String strFilePath) {

        List<String> newList = new ArrayList<>();
        if (TextUtils.isEmpty(strFilePath)) return newList;

        //打开文件
        File file = new File(strFilePath);
        //如果path是传递过来的参数，可以做一个非目录的判断
        if (file.exists()) {
            try {
                InputStream instream = new FileInputStream(file);
                InputStreamReader inputreader = new InputStreamReader(instream);
                BufferedReader buffreader = new BufferedReader(inputreader);
                String line;
                //分行读取
                while ((line = buffreader.readLine()) != null) {
                    newList.add(line);
                }
                instream.close();
            } catch (IOException e) {
            }
        }
        return newList;
    }

    public static List<String> readTxtFile(@NonNull String strFilePath, @NonNull String fileName) {
        if (!strFilePath.endsWith("/")) {
            strFilePath += "/";
        }
        return readTxtFile(strFilePath + fileName);
    }

    public static void makeRootDirectory(String filePath) {
        if (!filePath.endsWith("/")) {
            filePath += "/";
        }
        File file = null;
        try {
            file = new File(filePath);
            if (!file.exists()) {
                file.mkdirs();
            }
        } catch (Exception e) {
            LogUtil.e("IOUtils", e.getMessage());
        }
    }

    /**
     * 获取
     *
     * @param list
     * @param fileDir
     * @param suffixName
     */
    public void getFilesName(@NonNull List<File> list, @NonNull File fileDir, @NonNull String suffixName) {
        if (fileDir.exists()) {
            if (fileDir.isDirectory()) {
                File[] files = fileDir.listFiles();
                if (files != null) {
                    for (File file : files) {
                        getFilesName(list, file, suffixName);
                    }
                }
            } else {
                if (suffixName.contains(IOUtils.getSuffix(fileDir.getPath()))) {
                    list.add(fileDir);
                }
            }
        }
    }

    /*
     * 检查文件是否存在
     *
     */
    public static boolean fileIsExists(String filenmae, String path) {
        File file = new File(path, filenmae);
        return fileIsExists(file);
    }

    public static boolean fileIsExists(File file) {
        return file.exists();
    }

    /**
     * 复制单个文件
     *
     * @param oldPath String 原文件路径 如：c:/fqf.txt
     * @param newPath String 复制后路径 如：f:/fqf.txt
     * @return boolean
     */
    public static boolean copyFile(String oldPath, String newPath) {
        try {
            int bytesum = 0;
            int byteread = 0;
            File oldfile = new File(oldPath);

            if (oldfile.exists()) { //文件存在时
                String folder = getFolder(newPath);
                if (UIUtils.isNotEmpty(folder)) {
                    makeRootDirectory(folder);
                }

                InputStream inStream = new FileInputStream(oldPath); //读入原文件
                FileOutputStream fs = new FileOutputStream(newPath);
                byte[] buffer = new byte[1444];
                while ((byteread = inStream.read(buffer)) != -1) {
                    bytesum += byteread; //字节数 文件大小
                    System.out.println(bytesum);
                    fs.write(buffer, 0, byteread);
                }
                inStream.close();

                return true;
            }
        } catch (Exception e) {
            System.out.println("复制单个文件操作出错");
            e.printStackTrace();

        }
        return false;
    }

    /**
     * 复制整个文件夹内容
     *
     * @param oldPath String 原文件路径 如：c:/fqf
     * @param newPath String 复制后路径 如：f:/fqf/ff
     * @return boolean
     */
    public static void copyFolder(String oldPath, String newPath) {

        try {
            (new File(newPath)).mkdirs(); //如果文件夹不存在 则建立新文件夹
            File a = new File(oldPath);
            String[] file = a.list();
            File temp = null;
            for (int i = 0; i < file.length; i++) {
                if (oldPath.endsWith(File.separator)) {
                    temp = new File(oldPath + file[i]);
                } else {
                    temp = new File(oldPath + File.separator + file[i]);
                }

                if (temp.isFile()) {
                    FileInputStream input = new FileInputStream(temp);
                    FileOutputStream output = new FileOutputStream(newPath + "/" +
                            (temp.getName()).toString());
                    byte[] b = new byte[1024 * 5];
                    int len;
                    while ((len = input.read(b)) != -1) {
                        output.write(b, 0, len);
                    }
                    output.flush();
                    output.close();
                    input.close();
                }
                if (temp.isDirectory()) {//如果是子文件夹
                    copyFolder(oldPath + "/" + file[i], newPath + "/" + file[i]);
                }
            }
        } catch (Exception e) {
            System.out.println("复制整个文件夹内容操作出错");
            e.printStackTrace();

        }

    }


    /**
     * 重命名文件
     *
     * @param oldPath 原来的文件地址
     * @param newPath 新的文件地址
     */
    public static void renameFile(String oldPath, String newPath) {
        File oleFile = new File(oldPath);
        File newFile = new File(newPath);
        //执行重命名
        oleFile.renameTo(newFile);
    }


    //获取文件名  不带后缀
    public static String getFileName(String pathandname) {
        int start = pathandname.lastIndexOf("/");
        int end = pathandname.lastIndexOf(".");
        if (start != -1 && end != -1) {
            return pathandname.substring(start + 1, end);
        } else {
            return null;
        }

    }

    //获取文件名带后缀
    public static String getFileNameAll(String pathandname) {
        int start = pathandname.lastIndexOf("/");
        if (start != -1) {
            return pathandname.substring(start + 1);
        } else {
            return null;
        }
    }

    //获取文件后缀名 .jpg
    public static String getSuffix(String url) {
        if (url == null) {
            return null;
        }
        int dot = url.lastIndexOf(".");
        if (dot >= 0) {
            return url.substring(dot);
        } else {
            // No extension.
            return "";
        }
    }


    //获取最后一级的文件夹
    public static String getFolder(String pathAndName) {
        int start = pathAndName.lastIndexOf("/");
        if (start > 0) {
            return pathAndName.substring(0, start);
        } else {
            return "";
        }
    }



    /*    说明:

            1.硬件上的 block size, 应该是"sector size"，linux的扇区大小是512byte
    2.有文件系统的分区的block size, 是"block size"，大小不一，可以用工具查看
    3.没有文件系统的分区的block size，也叫“block size”，大小指的是1024 byte
    4.Kernel buffer cache 的block size, 就是"block size"，大部分PC是1024
    5.磁盘分区的"cylinder size"，用fdisk 可以查看。
    我们这里的block size是第二种情况，一般SD卡都是fat32的文件系统，block size是4096.
    这样就可以知道手机的内部存储空间和sd卡存储空间的总大小和可用大小了。*/


    /**
     * 获取手机内部剩余存储空间
     *
     * @
     */
    public static long getAvailableInternalMemorySize() {
        File path = Environment.getDataDirectory();
        StatFs stat = new StatFs(path.getPath());
        long blockSize = stat.getBlockSize();
        long availableBlocks = stat.getAvailableBlocks();
        return availableBlocks * blockSize;
    }

    /**
     * 获取手机内部总的存储空间
     *
     * @
     */
    public static long getTotalInternalMemorySize() {
        File path = Environment.getDataDirectory();
        StatFs stat = new StatFs(path.getPath());
        long blockSize = stat.getBlockSize();
        long totalBlocks = stat.getBlockCount();
        return totalBlocks * blockSize;
    }

    /**
     * 获取SDCARD剩余存储空间
     *
     * @
     */
    public static long getAvailableExternalMemorySize() {
        if (isSdCardExist()) {
            File path = Environment.getExternalStorageDirectory();
            StatFs stat = new StatFs(path.getPath());
            long blockSize = stat.getBlockSize();
            long availableBlocks = stat.getAvailableBlocks();
            return availableBlocks * blockSize;
        } else {
            return -1;//这里返回错误信息
        }
    }

    /**
     * 获取SDCARD总的存储空间
     *
     * @
     */
    public static long getTotalExternalMemorySize() {
        if (isSdCardExist()) {
            File path = Environment.getExternalStorageDirectory();
            StatFs stat = new StatFs(path.getPath());
            long blockSize = stat.getBlockSize();
            long totalBlocks = stat.getBlockCount();
            return totalBlocks * blockSize;
        } else {
            return -1;//这里返回错误信息
        }
    }


    public static List<String> getFilesAllName(String path) {
        File file = new File(path);
        File[] files = file.listFiles();
        if (files == null) {
            return null;
        }
        List<String> s = new ArrayList<>();
        for (int i = 0; i < files.length; i++) {
            s.add(files[i].getAbsolutePath());
        }
        return s;
    }

    public static void saveFile(@NonNull byte[] bytes, @NonNull File file) {
        try {
            FileOutputStream out = new FileOutputStream(file);
            out.write(bytes);
            out.flush();
            out.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void saveFile(@NonNull byte[] bytes, @NonNull String filePath, @NonNull String fileName) {
        if (!filePath.endsWith("/")) {
            filePath += "/";
        }
        File file = IOUtils.createFile(filePath, fileName);
        saveFile(bytes, file);
    }

    public static String saveFile(Context context, String dir, String fileName, Bitmap bitmap, boolean isShow) {
        if (bitmap == null) return "";

        try {
            File file = IOUtils.createFile(dir, fileName);
            FileOutputStream out = new FileOutputStream(file);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, out);
            out.flush();
            out.close();
            //保存图片后发送广播通知更新图片数据库
            Uri uri = Uri.fromFile(file);
            context.sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, uri));
            if (isShow) {
                UIUtils.showToastSafes("图片已保存：" + file.getAbsolutePath());
            }
            return file.getAbsolutePath();
        } catch (Exception e) {
            if (isShow)
                UIUtils.showToastSafes("保存失败");
            e.printStackTrace();
        }
        return "";
    }


}
