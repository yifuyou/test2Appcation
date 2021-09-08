package com.base.common.view.widget.imageView;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.widget.ImageView;

import androidx.annotation.DrawableRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.base.common.model.bean.ImageLoaderBean;
import com.base.common.utils.UIUtils;
import com.base.common.utils.ViewUtils;
import com.base.common.utils.mimeType.MimeType;
import com.base.common.view.widget.imageView.progress.CircleProgressView;
import com.base.common.view.widget.imageView.progress.OnGlideImageViewListener;
import com.base.common.view.widget.imageView.progress.OnProgressListener;
import com.base.common.view.widget.imageView.progress.ProgressManager;
import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestBuilder;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.target.Target;
import com.bumptech.glide.request.transition.Transition;

import java.lang.ref.WeakReference;


public class GlideImageLoader {

    private WeakReference<ImageView> mImageView;
    private Object mImageUrlObj;
    private ImageLoaderBean imageLoaderBean;
    private long mTotalBytes = 0;
    private long mLastBytesRead = 0;
    private boolean mLastStatus = false;
    private Handler mMainThreadHandler;

    private OnProgressListener internalProgressListener;
    private OnGlideImageViewListener onGlideImageViewListener;
    private OnProgressListener onProgressListener;

    public static GlideImageLoader create(ImageView imageView) {
        return new GlideImageLoader(imageView);
    }

    private GlideImageLoader(ImageView imageView) {
        mImageView = new WeakReference<>(imageView);
        mMainThreadHandler = new Handler(Looper.getMainLooper());
    }

    public ImageView getImageView() {
        if (mImageView != null) {
            return mImageView.get();
        }
        return null;
    }

    public Context getContext() {
//        if (getImageView() != null) {
//            return getImageView().getContext();
//        }
        return UIUtils.getContext();
    }

    public String getImageUrl() {
        if (mImageUrlObj == null) return "";
        if (mImageUrlObj instanceof String) return (String) mImageUrlObj;
        else return "";
    }

    public RequestOptions requestOptions(@DrawableRes int placeholderResId) {
        return requestOptions(placeholderResId, placeholderResId);
    }

    public RequestOptions requestOptions(@DrawableRes int placeholderResId, @DrawableRes int errorResId) {
        return new RequestOptions().placeholder(placeholderResId).error(errorResId);
    }

    public void load(Object obj, RequestOptions... requestOptions) {
        load(obj, null, requestOptions);
    }


    public void load(Object obj, CircleProgressView progressView, RequestOptions... requestOptions) {
        if (getContext() == null || getImageView() == null) return;
        if (obj == null) obj = "";
        RequestOptions options = requestOptions.length > 0 ? requestOptions[0] : new RequestOptions().skipMemoryCache(true).diskCacheStrategy(DiskCacheStrategy.DATA);
        loadImage(obj, progressView, options);
    }


    //处理进度条
    public RequestBuilder<Drawable> requestBuilder(Object obj, final CircleProgressView progressView, RequestOptions options) {
        if (progressView != null)
            setOnGlideImageViewListener(obj.toString(), new OnGlideImageViewListener() {
                @Override
                public void onProgress(int percent, boolean isDone, GlideException exception) {
                    if (percent > progressView.getProgress()) progressView.setProgress(percent);
                    progressView.setVisibility(isDone ? View.GONE : View.VISIBLE);
                }
            });

        return requestBuilder(obj, options);
    }


    /**
     * 所有的图片均从这个方法加载
     *
     * @param obj
     * @param options
     * @return
     */
    public RequestBuilder<Drawable> requestBuilder(Object obj, RequestOptions options) {
        Object value;
        if (obj != null) {
            value = obj;
        } else value = "";

        mImageUrlObj = value;
//        getImageView().setAdjustViewBounds(true);//解决图片大小问题  根据图片大小加载


        return Glide.with(getContext())
                .load(value)
//                .thumbnail(0.4f)
                .apply(options).listener(new RequestListener<Drawable>() {
                    @Override
                    public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                        mainThreadCallback(mLastBytesRead, mTotalBytes, true, e);
                        ProgressManager.removeProgressListener(internalProgressListener);
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                        mainThreadCallback(mLastBytesRead, mTotalBytes, true, null);
                        ProgressManager.removeProgressListener(internalProgressListener);
                        return false;
                    }
                });
    }


    public void loadImage(Object obj, final CircleProgressView progressView, RequestOptions options) {
        if (obj instanceof ImageLoaderBean) {
            ImageLoaderBean imageLoaderBean = (ImageLoaderBean) obj;
            this.imageLoaderBean = imageLoaderBean;
            mImageUrlObj = imageLoaderBean.getImageObject();
            if (progressView != null)
                setOnGlideImageViewListener(obj.toString(), new OnGlideImageViewListener() {
                    @Override
                    public void onProgress(int percent, boolean isDone, GlideException exception) {
                        if (percent > progressView.getProgress()) progressView.setProgress(percent);
                        progressView.setVisibility(isDone ? View.GONE : View.VISIBLE);
                    }
                });

            Glide.with(getContext())
                    .asBitmap()
                    .load(mImageUrlObj)
//                .thumbnail(0.4f)
                    .apply(options).listener(new RequestListener<Bitmap>() {
                @Override
                public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Bitmap> target, boolean isFirstResource) {
                    mainThreadCallback(mLastBytesRead, mTotalBytes, true, e);
                    ProgressManager.removeProgressListener(internalProgressListener);
                    return false;
                }

                @Override
                public boolean onResourceReady(Bitmap resource, Object model, Target<Bitmap> target, DataSource dataSource, boolean isFirstResource) {
                    mainThreadCallback(mLastBytesRead, mTotalBytes, true, null);
                    ProgressManager.removeProgressListener(internalProgressListener);
                    return false;
                }
            }).into(new SimpleTarget<Bitmap>() {
                @Override
                public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {

                    //如果加载的是本地路经或网络路经
                    if (getImageView() instanceof GlideImageView) {

                        GlideImageView imageView = (GlideImageView) getImageView();
                        int w = resource.getWidth();
                        int h = resource.getHeight();

                        int x = imageView.getMeasuredWidth();
                        int y = imageView.getMeasuredHeight();

                        if (h >= 2000) {
                            imageView.setLongImage(true);
                        } else {
                            imageView.setLongImage(false);
                        }

                        //图片高度在最小和最大之间  自适应
                        if (h < imageLoaderBean.getMiniHeight()) {
                            y = imageLoaderBean.getMiniHeight();
                        } else y = Math.min(h, imageLoaderBean.getMaxHeight());

                        if (w < imageLoaderBean.getMiniWidth()) {
                            x = imageLoaderBean.getMiniWidth();
                        } else x = Math.min(w, imageLoaderBean.getMaxWidth());


                        ViewUtils.setViewSize(imageView, x, y);


                    }

                    if (getImageView() != null) {
                        if (MimeType.isGifType(mImageUrlObj.toString())) {
                            requestBuilder(mImageUrlObj, options).into(getImageView());
                        } else {
                            getImageView().setImageBitmap(resource);
                        }

                    }

                }
            });

        } else {
            requestBuilder(obj, progressView, options).into(getImageView());
        }
    }


    private void addProgressListener() {
        if (getImageUrl() == null) return;
        final String url = getImageUrl();
        if (!url.startsWith("http")) return;

        internalProgressListener = new OnProgressListener() {
            @Override
            public void onProgress(String imageUrl, long bytesRead, long totalBytes, boolean isDone, GlideException exception) {
                if (totalBytes == 0) return;
                if (!url.equals(imageUrl)) return;
                if (mLastBytesRead == bytesRead && mLastStatus == isDone) return;

                mLastBytesRead = bytesRead;
                mTotalBytes = totalBytes;
                mLastStatus = isDone;
                mainThreadCallback(bytesRead, totalBytes, isDone, exception);

                if (isDone) {
                    ProgressManager.removeProgressListener(this);
                }
            }
        };
        ProgressManager.addProgressListener(internalProgressListener);
    }

    private void mainThreadCallback(final long bytesRead, final long totalBytes, final boolean isDone, final GlideException exception) {
        mMainThreadHandler.post(new Runnable() {
            @Override
            public void run() {
                final int percent = (int) ((bytesRead * 1.0f / totalBytes) * 100.0f);
                if (onProgressListener != null) {
                    onProgressListener.onProgress((String) mImageUrlObj, bytesRead, totalBytes, isDone, exception);
                }

                if (onGlideImageViewListener != null) {
                    onGlideImageViewListener.onProgress(percent, isDone, exception);
                }
            }
        });
    }


    //不要和glide
    public void setOnGlideImageViewListener(String imageUrl, OnGlideImageViewListener onGlideImageViewListener) {
        this.mImageUrlObj = imageUrl;
        this.onGlideImageViewListener = onGlideImageViewListener;
        addProgressListener();
    }

    public void setOnProgressListener(String imageUrl, OnProgressListener onProgressListener) {
        this.mImageUrlObj = imageUrl;
        this.onProgressListener = onProgressListener;
        addProgressListener();
    }
}
