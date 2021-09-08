package com.base.common.view.widget.imageView;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;

import com.base.common.R;
import com.base.common.app.AppManager;
import com.base.common.app.BaseConstant;
import com.base.common.permission.PermissionUtils;
import com.base.common.utils.DrawableUtil;
import com.base.common.utils.IOUtils;
import com.base.common.utils.UIUtils;
import com.base.common.view.widget.imageView.progress.CircleProgressView;
import com.base.common.view.widget.imageView.transformation.BlurTransformation;
import com.base.common.viewmodel.BaseRxObserver;
import com.bumptech.glide.Glide;
import com.bumptech.glide.Priority;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;


/**
 * Created by yangkuo on 2018-06-16.
 * <p>
 * 图片加载类，所有的图片
 */
public class ImageLoaderUtils {


    /**
     * @param imageView    需要加载图片的View
     * @param value        图片资源
     * @param placeholders 是否显示占位图 默认显示
     */
    public static void loadImage(@NonNull ImageView imageView, Object value, boolean... placeholders) {
        if (value == null) value = "";
        if (imageView instanceof GlideImageView) {
            loadImage((GlideImageView) imageView, null, value);
            return;
        }

        if (value.getClass() == Integer.class || value.getClass() == int.class) {
            imageView.setImageResource((int) value);
        } else if (value instanceof Bitmap) {
            imageView.setImageBitmap((Bitmap) value);
        } else if (value instanceof Drawable) {
            imageView.setImageDrawable((Drawable) value);
        }

        //加载参数
        RequestOptions requestOptions = new RequestOptions();
        if (imageView.getScaleType().compareTo(ImageView.ScaleType.FIT_CENTER) == 0) {
            requestOptions = requestOptions.fitCenter();
        } else if (imageView.getScaleType().compareTo(ImageView.ScaleType.CENTER_CROP) == 0) {
            requestOptions = requestOptions.centerCrop();
        }

        //默认有占位图
        boolean placeholder = true;
        if (placeholders.length > 0) placeholder = placeholders[0];

        if (placeholder)
            requestOptions = requestOptions.placeholder(R.drawable.bg_default).error(R.drawable.bg_default).priority(Priority.NORMAL);

        if (value instanceof String && ((String) value).startsWith("http")) {
            requestOptions = requestOptions.skipMemoryCache(true).diskCacheStrategy(DiskCacheStrategy.AUTOMATIC);
        }


        GlideImageLoader.create(imageView).loadImage(value, null, requestOptions);

    }


    /**
     * @param imageView    需要加载图片的View
     * @param value        图片资源
     * @param placeholders 是否显示占位图 默认显示
     */
    public static void loadImage(GlideImageView imageView, CircleProgressView progressView, Object value, boolean... placeholders) {
        if (imageView == null) return;
        if (value == null) value = "";
        if (value.getClass() == Integer.class || value.getClass() == int.class) {
            imageView.setImageResource((int) value);
            return;
        } else if (value instanceof Bitmap) {
            imageView.setImageBitmap((Bitmap) value);
            return;
        } else if (value instanceof Drawable) {
            imageView.setImageDrawable((Drawable) value);
            return;
        }

        //是否模糊
        boolean siv_blur = imageView.isSiv_blur();

        //加载参数
        RequestOptions requestOptions = new RequestOptions();
        if (imageView.getScaleType().compareTo(ImageView.ScaleType.FIT_CENTER) == 0) {
            requestOptions = requestOptions.fitCenter();
        } else if (imageView.getScaleType().compareTo(ImageView.ScaleType.CENTER_CROP) == 0) {
            requestOptions = requestOptions.centerCrop();
        }

        if (siv_blur) {
            requestOptions = requestOptions.transform(new BlurTransformation(UIUtils.getContext(), 36, 4));
        } else {
            //默认有占位图
            boolean placeholder = imageView.isSiv_need_placeholder();
            if (placeholders.length > 0) placeholder = placeholders[0];


            //加载时占位图
            Drawable placehol;
            if (imageView.getSiv_placeholder() != null) {
                placehol = imageView.getSiv_placeholder();
            } else {
                placehol = DrawableUtil.getDrawable(R.drawable.bg_default);
            }

            //加载出错时占位图
            Drawable placehol_err;
            if (imageView.getSiv_placeholder_err() != null) {
                placehol_err = imageView.getSiv_placeholder_err();
            } else {
                placehol_err = DrawableUtil.getDrawable(R.drawable.bg_default);
            }
            if (placeholder)
                requestOptions = requestOptions.placeholder(placehol).error(placehol_err).priority(Priority.NORMAL);
        }


        //如果加载的是网络图片，需要在本地缓存
        if (value instanceof String && ((String) value).startsWith("http")) {
            requestOptions = requestOptions.skipMemoryCache(true).diskCacheStrategy(DiskCacheStrategy.AUTOMATIC);
        }

        //加载图片
        imageView.getImageLoader().load(value, progressView, requestOptions);
    }


    public static void saveImage(Object value, final boolean isShow) {
        Activity activity = AppManager.getInstance().getTopActivity();
        if (activity instanceof FragmentActivity) {
            FragmentActivity fragmentActivity = (FragmentActivity) activity;
            PermissionUtils.initStoragePermission(fragmentActivity).subscribe(new BaseRxObserver<Boolean>() {
                @Override
                public void onNext(@io.reactivex.annotations.NonNull Boolean aBoolean) {
                    if (aBoolean) {
                        IOUtils.initRootDirectory();
                        Glide.with(UIUtils.getContext()).asBitmap().load(value).into(new SimpleTarget<Bitmap>() {
                            @Override
                            public void onResourceReady(@NonNull Bitmap resource, Transition<? super Bitmap> transition) {
                                IOUtils.saveFile(UIUtils.getContext(), BaseConstant.imageSaveDir, +System.currentTimeMillis() + ".jpg", resource, isShow);
                            }
                        });
                    }
                }
            });
        }
    }



}
