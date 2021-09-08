package com.base.common.view.widget.imageView;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;

import androidx.annotation.NonNull;

import com.base.common.model.http.upLoad.LoadCallBack;
import com.base.common.view.roundview.RoundViewDelegate;
import com.base.common.view.widget.imageView.progress.OnGlideImageViewListener;
import com.base.common.view.widget.imageView.progress.OnProgressListener;

import java.math.BigDecimal;

import io.reactivex.Observable;


public class GlideImageView extends ShapeImageView implements RoundViewDelegate.onRoundViewDelegateInter{

    private GlideImageLoader mImageLoader;
    /**
     * 上传图时的参数
     */
    private String localPath = null;//本地图片路经
    private String netPath = null;//网络图片路经
    private ImageUpLoadState upLoadState = ImageUpLoadState.UNLoad;//图片上传状态

    private int errColor = 0x66ff0000;//上传图片错误颜色
    private int progressColor = 0x1A000000;//上传的进度条颜色
    private LoadCallBack loadCallBack;//图片上传的回调
    private boolean isCancel = false;//是否已取消上传
    private Paint progressedPaint; // 上传进度条的画笔

    private int drawHeight;//当前绘制的高度


    private RoundViewDelegate delegate;


    public GlideImageView(Context context) {
        this(context, null);
    }

    public GlideImageView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public GlideImageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
        delegate = new RoundViewDelegate(this, context, attrs);
    }

    /**
     * use delegate to set attr
     */
    public RoundViewDelegate getDelegate() {
        return delegate;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        if (delegate.isWidthHeightEqual() && getWidth() > 0 && getHeight() > 0) {
            int max = Math.max(getWidth(), getHeight());
            int measureSpec = MeasureSpec.makeMeasureSpec(max, MeasureSpec.EXACTLY);
            super.onMeasure(measureSpec, measureSpec);
            return;
        }

        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }


    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        delegate.setHeight(bottom - top);
        delegate.setWidth(right - left);
        if (delegate.isRadiusHalfHeight()) {
            delegate.setCornerRadius(getHeight() / 2);
        } else {
            delegate.setBgSelector();
        }
    }



    private void init() {
        if (isInEditMode()) return;
        mImageLoader = GlideImageLoader.create(this);
        initProgressedPaint();
    }


    /**
     * @param path       本地路经
     * @param observable 上传图片的观察者    的返回值
     */
    public void upLoadImage(@NonNull Observable<Object> observable, @NonNull String path, UpLoadCallback upLoadCallback) {
        localPath = path;
        //先加载本地图片
        ImageLoaderUtils.loadImage(this, path, true);
        isCancel = false;
        postDelayed(new Runnable() {
            @Override
            public void run() {
                if (isCancel) return;
                loadCallBack = observable.subscribeWith(new LoadCallBack() {

                    @Override
                    protected void onStart() {
                        super.onStart();
                        upLoadState = ImageUpLoadState.LoadIng;
                    }

                    @Override
                    public void onError(Throwable e) {
                        super.onError(e);
                        upLoadState = ImageUpLoadState.LoadERR;
                        //绘制进度条
                        drawUpload(0);
                        if (upLoadCallback != null) {
                            upLoadCallback.onError(e);
                        }
                    }

                    @Override
                    protected void onProgress(String percent) {
                        int pro = new BigDecimal(percent).intValue();
                        if (pro <= 1) pro = 0;

                        //绘制进度条
                        drawUpload(pro);
                    }

                    @Override
                    protected void onSuccess(String obj) {
                        netPath = obj;
                        upLoadState = ImageUpLoadState.LoadSuccess;
                        //绘制进度条
                        drawUpload(101);
                        if (upLoadCallback != null) {
                            upLoadCallback.onSuccess(netPath);
                        }
                    }
                });
            }
        }, 1000);


    }


    /**
     * 取消订阅
     */
    public void cancel() {
        if (loadCallBack != null && !loadCallBack.isDisposed()) {
            loadCallBack.dispose();
        } else {
            isCancel = true;
        }
    }


    public String getNetPath() {
        return netPath;
    }


    public GlideImageLoader getImageLoader() {
        if (mImageLoader == null) {
            mImageLoader = GlideImageLoader.create(this);
        }

        return mImageLoader;
    }

    public String getImageUrl() {
        return getImageLoader().getImageUrl();
    }


    public GlideImageView listener(OnGlideImageViewListener listener) {
        getImageLoader().setOnGlideImageViewListener(getImageUrl(), listener);
        return this;
    }

    public GlideImageView listener(OnProgressListener listener) {
        getImageLoader().setOnProgressListener(getImageUrl(), listener);
        return this;
    }


    public void loadUrl(Object obj, boolean... placeholders) {
        ImageLoaderUtils.loadImage(this, obj, placeholders);
    }

    // 初始化绘制进度条的画笔
    private void initProgressedPaint() {
        progressedPaint = new Paint();
        progressedPaint.setAntiAlias(true);
        progressedPaint.setStyle(Paint.Style.FILL);
        progressedPaint.setColor(errColor);
        progressedPaint.setAlpha(0);
        progressedPaint.setFlags(Paint.ANTI_ALIAS_FLAG);
    }

    public ImageUpLoadState getUpLoadState() {
        return upLoadState;
    }

    public void setUpLoadState(ImageUpLoadState upLoadState) {
        this.upLoadState = upLoadState;
        progressedPaint.setColor(errColor);
        progressedPaint.setAlpha((int) (pressedAlpha * 255));
        invalidate();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        drawProgressed(canvas);
    }

    private void drawProgressed(Canvas canvas) {
        switch (shapeType) {
            case ShapeType.CIRCLE:
                canvas.drawCircle(width / 2, drawHeight / 2, width / 2, progressedPaint);
                break;
            case ShapeType.ROUNDRECT:
                RectF rectf = new RectF(1, 1, width - 1, drawHeight - 1);
                canvas.drawRoundRect(rectf, radius, radius, progressedPaint);
                break;
            default:
                RectF rect = new RectF(1, 1, width - 1, drawHeight - 1);
                canvas.drawRect(rect, progressedPaint);
                break;
        }

    }


    //绘制进度条
    private void drawUpload(int progress) {

        //调整绘制的颜色
        if (upLoadState == ImageUpLoadState.LoadERR) {
            progressedPaint.setColor(errColor);
        } else {
            progressedPaint.setColor(progressColor);
        }


        //上传完毕，进行状态调整
        if (progress == 101) {
            progressedPaint.setAlpha(0);
        } else {
            //计算绘制灰么进度条的高度
            int h = (int) (height * (100 - progress) / 100f);

            if (h < drawHeight) {
                drawHeight = h;
            }
            progressedPaint.setAlpha((int) (pressedAlpha * 255));
        }


        invalidate();
    }

    public interface UpLoadCallback {
        void onSuccess(String url);

        void onError(Throwable e);
    }
}
