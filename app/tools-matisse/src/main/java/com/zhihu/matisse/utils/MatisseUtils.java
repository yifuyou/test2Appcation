package com.zhihu.matisse.utils;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.net.Uri;
import android.util.Log;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentActivity;

import com.base.common.permission.PermissionUtils;
import com.base.common.utils.IOUtils;
import com.base.common.utils.LogUtil;
import com.base.common.utils.matisse.ImageEditUtils;
import com.base.common.utils.UIUtils;
import com.base.common.viewmodel.BaseRxObserver;
import com.base.common.viewmodel.RxSchedulerTransformer;
import com.yalantis.ucrop.UCrop;
import com.zhihu.matisse.Matisse;

import com.zhihu.matisse.MimeType;
import com.zhihu.matisse.R;
import com.zhihu.matisse.SelectionCreator;
import com.zhihu.matisse.engine.impl.GlideEngine;
import com.zhihu.matisse.filter.Filter;
import com.zhihu.matisse.internal.entity.CaptureStrategy;
import com.zhihu.matisse.internal.entity.SelectionSpec;

import java.io.File;
import java.util.List;
import java.util.UUID;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import me.kareluo.imaging.IMGEditActivity;

import static android.app.Activity.RESULT_OK;


/**
 * Matisse
 * .from(mActivity)
 * //选择视频和图片
 * .choose(MimeType.ofAll())
 * //选择图片
 * .choose(MimeType.ofImage())
 * //选择视频
 * .choose(MimeType.ofVideo())
 * //自定义选择选择的类型
 * .choose(MimeType.of(MimeType.JPEG,MimeType.AVI))
 * //是否只显示选择的类型的缩略图，就不会把所有图片视频都放在一起，而是需要什么展示什么
 * .showSingleMediaType(true)
 * //这两行要连用 是否在选择图片中展示照相 和适配安卓7.0 FileProvider
 * .capture(true)
 * .captureStrategy(new CaptureStrategy(true,"PhotoPicker"))
 * //有序选择图片 123456...
 * .countable(true)
 * //最大选择数量为9
 * .maxSelectable(9)
 * //选择方向
 * .restrictOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT)
 * //界面中缩略图的质量
 * .thumbnailScale(0.8f)
 * //蓝色主题
 * .theme(R.style.Matisse_Zhihu)
 * //黑色主题
 * .theme(R.style.Matisse_Dracula)
 * //Glide加载方式
 * .imageEngine(new GlideEngine())
 * //Picasso加载方式
 * .imageEngine(new PicassoEngine())
 * //请求码
 * .forResult(REQUEST_CODE_CHOOSE);
 */


/**
 * 图片选择器
 */
public class MatisseUtils {


    private static final int REQUEST_CODE_CHOOSE = 0x9321;
    private static final int REQ_IMAGE_EDIT = 0x9333;


    private boolean isCropImage = false;//是否剪切？  未封装完毕
    private FragmentActivity activity;
    private int maxCount;//最大选择数量
    private boolean isSelectedGif = true;//是否可选动图


    private static ObservableEmitter<String> emitterPrivate = null;
    private static File mImageFile;//缓存编辑后的图片
    private static MatisseUtils matisseUtils;

    public void setCropImage(boolean cropImage) {
        isCropImage = cropImage;
    }

    public void setActivity(FragmentActivity activity) {
        this.activity = activity;
    }

    public void setMaxCount(int maxCount) {
        this.maxCount = maxCount;
    }


    public synchronized static MatisseUtils getInstance() {
        if (matisseUtils == null) {
            matisseUtils = new MatisseUtils();
        }
        return matisseUtils;
    }

    public void onDestroy() {
        ImageEditUtils.getInstance().setImageCallBack(null);
        emitterPrivate = null;
        activity = null;
        mImageFile = null;
        matisseUtils = null;

    }


    public MatisseUtils setSelectedGif(boolean selectedGif) {
        isSelectedGif = selectedGif;
        return this;
    }

    /**
     * @param activity
     * @param maxCount 最大选择图片数量
     * @param isSelectedGif_Crop   isSelectedGif_Crop[0]   isSelectedGif_Crop[1]是否剪切
     * @return
     */
    public Observable<String> observable(final FragmentActivity activity, int maxCount, int videoCount, boolean... isSelectedGif_Crop) {
        boolean isSelectedGif = true;
        boolean isCrop = false;

        if (isSelectedGif_Crop.length > 0) {
            isSelectedGif = isSelectedGif_Crop[0];
        }
        if (isSelectedGif_Crop.length > 1) {
            isCrop = isSelectedGif_Crop[1];
        }

        setSelectedGif(isSelectedGif);
        setCropImage(isCrop);

        setActivity(activity);
        setMaxCount(maxCount);

        setImageEditer();
        PermissionUtils.initCAMERAPermission(activity).subscribe(new BaseRxObserver<Boolean>() {
            @Override
            public void onNext(@io.reactivex.annotations.NonNull Boolean aBoolean) {
                if (aBoolean) {
                    IOUtils.initRootDirectory();
                    selectPic(Matisse.from(activity), maxCount, videoCount);
                }
            }
        });

        return observables();
    }


    private static Observable<String> observables() {

        return Observable.create(new ObservableOnSubscribe<String>() {
            @Override
            public void subscribe(ObservableEmitter<String> emitter) throws Exception {
                emitterPrivate = emitter;
            }
        }).compose(new RxSchedulerTransformer<String>());

    }


    private void selectPic(@NonNull Matisse matisse, int maxCount, int videoCount) {
        UIUtils.runInMainThread(new Runnable() {
            @Override
            public void run() {
                SelectionCreator selectionCreator;
                //自定义选择选择的类型
//               matisse .choose(MimeType.of(MimeType.JPEG,MimeType.AVI))
                if (maxCount == 0 && videoCount > 0) {
                    selectionCreator = matisse.choose(MimeType.ofVideo()).maxSelectable(videoCount).capture(false);//只选择视频
                } else if (maxCount > 0 && videoCount == 0) {
                    selectionCreator = matisse.choose(isSelectedGif ? MimeType.ofImage() : MimeType.onlyImage()).maxSelectable(maxCount).capture(true);//只选择图片
                } else if (maxCount > 0 && videoCount > 0) {
                    selectionCreator = matisse.choose(MimeType.ofAll()).maxSelectablePerMediaType(maxCount, videoCount).capture(true);//选择视频和图片
                } else {
                    return;
                }


//                if (maxCount > 0) {
//                    selectionCreator = matisse.choose(isSelectedGif ? MimeType.ofImage() :MimeType.onlyImage()).maxSelectable(maxCount).capture(true);//只选择图片
//                } else {
//                    return;
//                }

                //是否只显示选择的类型的缩略图，就不会把所有图片视频都放在一起，而是需要什么展示什么
                selectionCreator
//                        .showSingleMediaType(true)
//                .capture(true)  //是否显示拍摄按钮，默认不显示
                        //.capture(true, CaptureMode.All)//是否显示拍摄按钮，可以同时拍视频和图片
//                .jumpCapture()//直接跳拍摄，默认可以同时拍摄照片和视频
//                        .jumpCapture(CaptureMode.Image)//只拍照片
                        //.jumpCapture(CaptureMode.Video)//只拍视频
//                        .captureStrategy(new CaptureStrategy(true, UIUtils.getPackageName() + ".fileProvider"))
                        .captureStrategy(new CaptureStrategy(false, UIUtils.getPackageName() + ".fileProvider", "matisse"))
                        .isCrop(isCropImage) //开启裁剪
//        R.style.Matisse_Zhihu (light mode)
//        R.style.Matisse_Dracula (dark mode)
                        .theme(R.style.Matisse_Zhihu)
                        //有序选择图片 123456..
                        .countable(true)
//                .maxSelectable(maxCount)
//                .maxSelectablePerMediaType(maxCount, videoCount)

                        .addFilter(new GifSizeFilter(0, 0, 5 * Filter.K * Filter.K))
                        .addFilter(new VideoSizeFilter(15 * Filter.K * Filter.K))
                        .restrictOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT)
                        .thumbnailScale(0.5f)
                        .imageEngine(new GlideEngine())
                        .forResult(REQUEST_CODE_CHOOSE);


                ImageEditUtils.getInstance().setImageCallBack(new ImageEditUtils.ImageCallBack() {
                    @Override
                    public void onActivityResult(int requestCode, int resultCode, Intent data) {

                        //图片剪切返回
                        if (requestCode == UCrop.REQUEST_CROP && resultCode == RESULT_OK) {
                            final Uri resultUri = UCrop.getOutput(data);
                            if (resultUri != null) {
                                //finish with result.
                                Intent result = new Intent();
//                result.putExtra(MatisseConst.EXTRA_RESULT_CROP_PATH, resultUri.getPath());
                                String s = resultUri.getPath();
                                if (emitterPrivate != null) {
                                    emitterPrivate.onNext(s);
                                    LogUtil.d(s);
                                    emitterPrivate.onComplete();
                                    emitterPrivate = null;
                                    LogUtil.d("图片剪切完毕");
                                }
                            } else {
                                Log.e("Matisse", "ucrop occur error: " + UCrop.getError(data).toString());
                            }
                        }
                        //图片选择返回
                        else if (requestCode == REQUEST_CODE_CHOOSE && resultCode == RESULT_OK) {
                            //最新  只能
//            Matisse.obtainResult(data), Matisse.obtainPathResult(data)

                            //图片路径 同样视频地址也是这个 根据requestCode
//            List<String> pathList = Matisse.obtainPathResult(data);

                            List<Uri> pathListUri = Matisse.obtainResult(data);
                            List<String> pathList = Matisse.obtainPathResult(data);
                            if (pathList != null && pathListUri != null) {
                                MatisseUtils matisseUtils = getInstance();
                                if (matisseUtils.isCropImage && pathListUri.size() == 1) {
                                    String s = pathList.get(0);
                                    if (matisseUtils.isCropImage && !com.base.common.utils.mimeType.MimeType.isGifType(s) && com.base.common.utils.mimeType.MimeType.isImageType(s)) {
                                        //开启裁剪
                                        startCrop(getInstance().activity, pathListUri.get(0));
                                        return;
                                    }
                                }

                                if (emitterPrivate != null) {
                                    for (String s : pathList) {
                                        emitterPrivate.onNext(s);
                                        LogUtil.d(s);
                                    }
                                    emitterPrivate.onComplete();
                                    getInstance().onDestroy();
                                    LogUtil.d("图片选取完毕");
                                }

                            }
                        }

                    }
                });
            }
        });
    }


    private static void setImageEditer() {
        //设置图片编辑
        ImageEditUtils.getInstance().setImageEditInterface(new ImageEditUtils.ImageEditInterface() {
            @Override
            public String editString() {
                return "编辑";
            }

            @Override
            public void editOnclickListener(View view, Uri image) {

                mImageFile = new File(IOUtils.getPicturesPath(), UUID.randomUUID().toString() + ".jpg");
                //弹出图片编辑
                Intent intent = new Intent(view.getContext(), IMGEditActivity.class);
                intent.putExtra(IMGEditActivity.EXTRA_IMAGE_URI, image);
                intent.putExtra(IMGEditActivity.EXTRA_IMAGE_SAVE_PATH, mImageFile.getAbsolutePath());
                ((Activity) view.getContext()).startActivityForResult(intent, REQ_IMAGE_EDIT);
            }

            @Override
            public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
                //图片编辑返回
                if (requestCode == REQ_IMAGE_EDIT && resultCode == RESULT_OK) {
                    if (ImageEditUtils.getInstance().getImageEditReturnInterface() != null)
                        ImageEditUtils.getInstance().getImageEditReturnInterface().editImageReturn(mImageFile);
                }
            }


        });
    }


    public static void startCrop(Activity context, Uri source) {
        String destinationFileName = System.nanoTime() + "_crop.jpg";
        UCrop.Options options = new UCrop.Options();
        options.setCompressionQuality(90);

        // Color palette
        TypedArray ta = context.getTheme().obtainStyledAttributes(SelectionSpec.getInstance().themeId,
                new int[]{com.zhihu.matisse.R.attr.colorPrimary, com.zhihu.matisse.R.attr.colorPrimaryDark, com.zhihu.matisse.R.attr.album_element_color});

        int primaryColor = ta.getColor(0, Color.TRANSPARENT);
        options.setToolbarColor(primaryColor);
        options.setStatusBarColor(ta.getColor(1, Color.TRANSPARENT));
        options.setActiveControlsWidgetColor(primaryColor);
        options.setToolbarWidgetColor(ta.getColor(2, 0xffffffff));

        ta.recycle();
        File cacheFile = new File(context.getCacheDir(), destinationFileName);
        UCrop.of(source, Uri.fromFile(cacheFile))
                .withAspectRatio(1, 1)
                .withOptions(options)
                .start(context);
    }


}
