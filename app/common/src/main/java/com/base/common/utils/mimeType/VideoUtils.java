package com.base.common.utils.mimeType;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.media.MediaMetadataRetriever;
import android.media.ThumbnailUtils;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.widget.ImageView;

import com.base.common.R;
import com.base.common.utils.DrawableUtil;
import com.base.common.view.widget.imageView.ImageLoaderUtils;

import java.util.HashMap;

public class VideoUtils {

    /**
     * 获取视频的封面图片  加了播放按扭
     *
     * @param url
     */
    public static Bitmap getVideoThumbnailCover(String url) {
        if (TextUtils.isEmpty(url)) return null;

        Bitmap bitmap = getVideoThumbnail(url, 0, 0);
        if (bitmap != null) {
            Canvas canvas = new Canvas(bitmap);

            Bitmap paly = DrawableUtil.getBitmap(R.drawable.video_play_normal);

            if (paly != null) {
                int let = (bitmap.getWidth() - paly.getWidth()) / 2;
                int top = (bitmap.getHeight() - paly.getHeight()) / 2;
                canvas.drawBitmap(paly, let, top, null);

                return bitmap;
            }
        }

        return bitmap;
    }

    public static void setVideoThumbnail(String url, ImageView imageView) {
        ImageLoaderUtils.loadImage(imageView, url, false);
    }


    /**
     * 获取视频的缩略图
     * 先通过ThumbnailUtils来创建一个视频的缩略图，然后再利用ThumbnailUtils来生成指定大小的缩略图。
     * 如果想要的缩略图的宽和高都小于MICRO_KIND，则类型要使用MICRO_KIND作为kind的值，这样会节省内存。
     *
     * @param url 视频的路径
     *            kind      参照MediaStore.Images.Thumbnails类中的常量MINI_KIND和MICRO_KIND。
     *            其中，MINI_KIND: 512 x 384，MICRO_KIND: 96 x 96
     * @return 指定大小的视频缩略图
     */

    //如果指定的视频的宽高都大于了MICRO_KIND的大小，那么你就使用MINI_KIND就可以了
    public static Bitmap getVideoThumbnail(String url, int width, int height) {


        Bitmap bitmap = null;
        MediaMetadataRetriever retriever = new MediaMetadataRetriever();
        int kind = MediaStore.Video.Thumbnails.MINI_KIND;
        try {
            if (url.startsWith("http")) {
                retriever.setDataSource(url, new HashMap<String, String>());
            } else {
                retriever.setDataSource(url);
            }


            bitmap = retriever.getFrameAtTime();
//                    String rotation = mCoverMedia.extractMetadata(MediaMetadataRetriever.METADATA_KEY_LOCATION);//获取视频时长 毫秒
            //        String rotation = mCoverMedia.extractMetadata(MediaMetadataRetriever.METADATA_KEY_VIDEO_ROTATION);//获取视频方向
//            final Bitmap bitmap = mCoverMedia.getFrameAtTime(1000, MediaMetadataRetriever.OPTION_CLOSEST);

        } catch (IllegalArgumentException ex) {
            // Assume this is a corrupt video file
            if (ex != null) {

            }
        } catch (RuntimeException ex) {
            // Assume this is a corrupt video file.
            if (ex != null) {

            }
        } finally {
            try {
                retriever.release();
            } catch (RuntimeException ex) {
                // Ignore failures while cleaning up.
            }
        }

        // 获取视频的缩略图
//        bitmap = ThumbnailUtils.createVideoThumbnail(videoPath, kind);
        if (width > 0 && height > 0 && bitmap != null) {
            bitmap = ThumbnailUtils.extractThumbnail(bitmap, width, height, ThumbnailUtils.OPTIONS_RECYCLE_INPUT);
        }

        return bitmap;


    }


}
