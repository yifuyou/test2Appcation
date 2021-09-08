package com.base.common.utils.matisse;

import android.content.Intent;
import android.net.Uri;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentActivity;

import java.io.File;
import java.util.HashMap;

import io.reactivex.Observable;

public class ImageEditUtils {

    //i没有任何做用，用于定位更改的位置
    public static void location() {

    }

    //没有任何做用，用于定位更改的位置
    public static int locationInt = 0;


    private ImageEditUtils() {

    }

    private static ImageEditUtils imageEditUtils;

    public static ImageEditUtils getInstance() {
        if (imageEditUtils == null) {
            imageEditUtils = new ImageEditUtils();
        }
        return imageEditUtils;
    }


    private ImageEditInterface imageEditInterface;//图片编辑调用接口
    private ImageEditReturnInterface imageEditReturnInterface;  //图片编辑返回时调用接口

    private ImageCallBack imageCallBack;//图片选取返回

    private HashMap<String, Uri> hashMap = new HashMap<>();


    public void put(String path, Uri uriEdit) {
        hashMap.put(path, uriEdit);
    }

    public Uri get(String path) {
        return hashMap.get(path);
    }

    public void clear() {
        hashMap.clear();
    }


    public void setImageEditInterface(ImageEditInterface imageEditInterface) {
        this.imageEditInterface = imageEditInterface;
    }


    public ImageEditInterface getImageEditInterface() {
        return imageEditInterface;
    }


    public ImageEditReturnInterface getImageEditReturnInterface() {
        return imageEditReturnInterface;
    }

    public void setImageEditReturnInterface(ImageEditReturnInterface imageEditReturnInterface) {
        this.imageEditReturnInterface = imageEditReturnInterface;
    }


    public void setImageCallBack(ImageCallBack imageCallBack) {
        this.imageCallBack = imageCallBack;
    }

    public ImageCallBack getImageCallBack() {
        return imageCallBack;
    }




    /**
     * 选取图片返回
     */
    public interface ImageCallBack {
        void onActivityResult(int requestCode, int resultCode, Intent data);
    }


    public interface ImageEditInterface {

        //编辑的显示文字
        String editString();

        //编辑的点击事件
        void editOnclickListener(View view, Uri image);

        void onActivityResult(int requestCode, int resultCode, @Nullable Intent data);
    }


    public interface ImageEditReturnInterface {
        //图片编辑返回
        void editImageReturn(File mImageFile);

    }


}
