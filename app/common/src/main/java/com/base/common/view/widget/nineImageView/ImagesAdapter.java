package com.base.common.view.widget.nineImageView;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.text.TextUtils;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;

import com.base.common.R;
import com.base.common.utils.ViewUtils;
import com.base.common.view.widget.imageView.GlideImageLoader;
import com.base.common.view.widget.imageView.ImageLoaderUtils;
import com.base.common.view.widget.imageView.progress.CircleProgressView;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.bumptech.glide.request.RequestOptions;
import com.github.chrisbanes.photoview.OnOutsidePhotoTapListener;
import com.github.chrisbanes.photoview.OnPhotoTapListener;
import com.github.chrisbanes.photoview.PhotoView;

import java.util.List;


public class ImagesAdapter extends PagerAdapter implements OnPhotoTapListener, OnOutsidePhotoTapListener {

    private Context mContext;
    private LayoutInflater mInflater;
    private List<ImageAttr> images;
    private SparseArray<PhotoView> photoViews = new SparseArray<>();

    public ImagesAdapter(Context context, @NonNull List<ImageAttr> images) {
        super();
        this.mContext = context;
        this.mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.images = images;
    }

    @Override
    public int getCount() {
        return images.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public void setPrimaryItem(ViewGroup container, int position, Object object) {
        super.setPrimaryItem(container, position, object);
    }

    public PhotoView getPhotoView(int index) {
        return photoViews.get(index);
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        View view = (ViewGroup) mInflater.inflate(R.layout.item_photoview, container, false);
        CircleProgressView progressView = (CircleProgressView) view.findViewById(R.id.progressView);
        PhotoView photoView = (PhotoView) view.findViewById(R.id.photoView);
        photoView.setScaleType(ImageView.ScaleType.FIT_CENTER);
        photoView.setOnPhotoTapListener(this);
        photoView.setOnOutsidePhotoTapListener(this);

        photoViews.put(position, photoView);

        ImageAttr attr = images.get(position);
        String url = TextUtils.isEmpty(attr.thumbnailUrl) ? attr.url : attr.thumbnailUrl;
        Object resObject = attr.resObject;
        if (resObject == null) resObject = url;

        final Object finalResObject = resObject;
        photoView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                if (!(finalResObject instanceof String)) {
                    return false;
                }

                AlertDialog.Builder builder = ViewUtils.showDialog(mContext, "提示", "保存图片");
                builder.setPositiveButton("保存", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        ImageLoaderUtils.saveImage(finalResObject, true);
                        dialog.dismiss();
                    }
                });
                builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                builder.create().show();
                return true;
            }
        });

        GlideImageLoader imageLoader = GlideImageLoader.create(photoView);
        RequestOptions requestOptions = imageLoader.requestOptions(R.color.C_333333).fitCenter().skipMemoryCache(true).diskCacheStrategy(DiskCacheStrategy.DATA);
        imageLoader.requestBuilder(resObject, progressView, requestOptions).transition(DrawableTransitionOptions.withCrossFade()).into(photoView);

        container.addView(view);
        return view;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        photoViews.remove(position);
        container.removeView((View) object);
    }

    @Override
    public void onPhotoTap(ImageView view, float x, float y) {
        ((ImagesActivity) mContext).finishWithAnim();
    }

    @Override
    public void onOutsidePhotoTap(ImageView imageView) {
        ((ImagesActivity) mContext).finishWithAnim();
    }
}