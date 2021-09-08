package com.base.common.view.adapter.ada;

import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;

import com.base.common.R;
import com.base.common.app.BaseApp;
import com.base.common.databinding.ItemAddedPicturesBinding;
import com.base.common.databinding.ItemAddedPicturesEntranceBinding;
import com.base.common.databinding.ItemAddedVideoBinding;
import com.base.common.model.bean.ADInfo;
import com.base.common.model.http.upLoad.LoadCallBack;
import com.base.common.viewmodel.BaseRxObserver;
import com.base.common.utils.UIUtils;
import com.base.common.utils.mimeType.MimeType;
import com.base.common.view.adapter.connector.BaseItemMultiType;
import com.base.common.view.adapter.bean.FooterBean;
import com.base.common.view.adapter.connector.BaseViewHolder;
import com.base.common.view.adapter.connector.BaseItemTypeInterface;
import com.base.common.view.base.BaseActivity;
import com.base.common.view.widget.imageView.GlideImageView;
import com.base.common.view.widget.imageView.ImageLoaderUtils;
import com.base.common.view.widget.imageView.ImageUpLoadState;
import com.base.common.view.widget.nineImageView.ImagesActivity;

import java.lang.ref.WeakReference;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observable;

public abstract class ImageAddRecyAdapter extends GeneralRecyclerAdapter {


    private WeakReference<BaseActivity> activity;

    private int maxCount = 3;
    private int videoCount = 1;//最多选取几个视频
    private boolean isSelectedGif = true;//是否选动图

    private UploadImageCallBack uploadImageCallBack;//数据源异步上传的回调
    private boolean isAutoUpload = false;//是否自动上传

    public void setAutoUpload(boolean autoUpload) {
        isAutoUpload = autoUpload;
    }

    public void setMaxCount(int maxCount) {
        this.maxCount = maxCount;
    }

    public void setVideoCount(int videoCount) {
        this.videoCount = videoCount;
    }

    public void setSelectedGif(boolean isSelectedGif) {
        this.isSelectedGif = isSelectedGif;
    }

    /**
     * 获取上传图片的网络请求
     * return viewModel.getUpLoadFilesObservable(path);
     *
     * @param path
     * @return
     */
    public abstract Observable<Object> getObservable(String path);

    /**
     * 图片选取
     *
     * @param activity
     * @param maxCount           图片的 最大数量
     * @param videoCount         视频的最大数量
     * @param isSelectedGif_Crop isSelectedGif_Crop[0] 是否选取gif  isSelectedGif_Crop[1] 是否剪切
     * @return
     */
    public Observable<String> observable(FragmentActivity activity, int maxCount, int videoCount, boolean... isSelectedGif_Crop) {
        return null;
    }

    public void startPlayer(String url, View view) {

    }

    public void setUploadImageCallBack(UploadImageCallBack uploadImageCallBack) {
        this.uploadImageCallBack = uploadImageCallBack;
    }

    private ImageAddRecyAdapter() {
    }


    public ImageAddRecyAdapter(BaseActivity activity) {
        if (activity != null) {
            this.activity = new WeakReference<>(activity);
        }
        init();
    }

    @Override
    public void initMultiItemType() {


        putMultiItemType(BaseItemTypeInterface.TYPE_CHILD, new BaseItemMultiType<ADInfo, ItemAddedPicturesBinding>() {
            @Override
            public int getLayoutId() {
                return R.layout.item_added_pictures;
            }

            @Override
            public boolean isCurrentChildItemType(int position, @NonNull ADInfo bean) {
                if (UIUtils.isNotEmpty(bean.getImagePatch())) {
                    return MimeType.isImageType(bean.getImagePatch());
                } else if (UIUtils.isNotEmpty(bean.getImageUrl())) {
                    return MimeType.isImageType(bean.getImageUrl());
                }
                return super.isCurrentChildItemType(position, bean);
            }

            @Override
            public void onBindViewHolder(ItemAddedPicturesBinding binding, int position, ADInfo bean) {
                super.onBindViewHolder(binding, position, bean);
                if (UIUtils.isEmpty(bean.getImageUrl()) && !UIUtils.isEmpty(bean.getImagePatch())) {
                    ImageLoaderUtils.loadImage(binding.givImage, bean.getImagePatch());
                } else {
                    ImageLoaderUtils.loadImage(binding.givImage, bean.getImageUrl());
                }
                binding.givImage.setUpLoadState(bean.getUpLoadState());
            }
        });

        putMultiItemType(BaseItemTypeInterface.TYPE_CHILD_ONE, new BaseItemMultiType<ADInfo, ItemAddedVideoBinding>() {

            @Override
            public int getLayoutId() {
                return R.layout.item_added_video;
            }

            @Override
            public void onBindViewHolder(ItemAddedVideoBinding binding, int position, ADInfo bean) {
                super.onBindViewHolder(binding, position, bean);
                if (UIUtils.isEmpty(bean.getImageUrl()) && !UIUtils.isEmpty(bean.getImagePatch())) {
                    ImageLoaderUtils.loadImage(binding.givImage, bean.getImagePatch());
                } else {
                    ImageLoaderUtils.loadImage(binding.givImage, bean.getImageUrl());
                }
                binding.givImage.setUpLoadState(bean.getUpLoadState());
            }

        });


        putMultiItemType(BaseItemTypeInterface.TYPE_FOOT, new BaseItemMultiType<FooterBean, ItemAddedPicturesEntranceBinding>() {

            @Override
            public int getLayoutId() {
                return R.layout.item_added_pictures_entrance;
            }

            @Override
            public void onBindViewHolder(ItemAddedPicturesEntranceBinding binding, int position, FooterBean bean) {
                super.onBindViewHolder(binding, position, bean);
                if (UIUtils.isNotEmpty(bean.getContent())) {
//                    binding.tvTitle.setText(bean.getContent());
//                    binding.tvTitle.setVisibility(View.VISIBLE);
                } else {
//                    binding.tvTitle.setVisibility(View.GONE);
                }
            }

            @Override
            public boolean onItemTypeClick(View view, BaseRVAdapter adapter, int onclickType, int position, FooterBean bean) {

                addImages();
                return super.onItemTypeClick(view, adapter, onclickType, position, bean);
            }


        });


    }

    @Override
    public boolean onItemClick(View view, BaseRVAdapter adapter, int onclickType, int itemType, int position, Object beanObj) {
        if (beanObj instanceof ADInfo) {
            ADInfo bean = (ADInfo) beanObj;

            if (view.getId() == R.id.givImageDelete) {
                //取消订阅
                BaseViewHolder viewHolder = getViewHolder(position);
                if (viewHolder != null) {
                    GlideImageView givImage = viewHolder.itemView.findViewById(R.id.givImage);
                    if (givImage != null) {
                        givImage.cancel();
                    }
                }
                remove(position);
                if (getChildCount() < maxCount) {
                    addFoot();
                }
                return true;
            } else if (view.getId() == R.id.givImage) {
                ImagesActivity.startActivity(recyclerView, R.id.givImage, "imagePatch", position);
                return true;
            }
            if (view.getId() == R.id.listItemBtn) {
                String url = UIUtils.isEmpty(bean.getImagePatch()) ? bean.getImageUrl() : bean.getImagePatch();
                startPlayer(url, ((ViewGroup) view.getParent()).findViewById(R.id.givImage));
                return true;
            }

        }
        return false;
    }


    /**
     * 添加图片
     */
    private void addImages() {
        int cc;//视频的数量
        int cou;//图片的数量

        //是否选择了图片 或视频  0  都没有  1选择了图片 2选择了视频
        int s = isSelectedImageOrVideo();
        if (s == 0) {
            cc = videoCount;
            cou = maxCount;
        } else if (s == 1) {
            cc = 0;
            cou = maxCount - getChildCount();
            if (cou <= 0) return;
        } else {
            cc = videoCount - getChildCount();
            if (cc <= 0) return;
            cou = 0;
        }
        if (activity == null || activity.get() == null) return;


        //视频和图片只能选一个
        Observable<String> observable;
        //先获取自身的，是否已实现
        observable = observable(activity.get(), cou, cc, isSelectedGif);
        //如果adapter 本身没有实现，则调用 BaseApp 的接口实现
        if (observable == null) {
            observable = BaseApp.getApplication().observable(activity.get(), cou, cc, isSelectedGif);
        }
        //如果为空则返回
        if (observable == null) {
            UIUtils.showToastSafesClose("没有实现图片选取接口");
            return;
        }


        observable.subscribe(new BaseRxObserver<String>() {
            @Override
            public void onNext(String s) {

                if (MimeType.isVideoType(s)) {
                    if (getChildCount() >= videoCount - 1) {
                        removeFooter(0);
                    }
                } else {
                    if (getChildCount() >= maxCount - 1) {
                        removeFooter(0);
                    }
                }

                addImages(s);
            }

            @Override
            public void onComplete() {
                if (isAutoUpload) {
                    UIUtils.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            upLoadImage();
                        }
                    }, 1000);
                }
            }

        });


    }


    //是否选择了图片 或视频  0  都没有  1选择了图片 2选择了视频
    public int isSelectedImageOrVideo() {
        if (getChildCount() == 0) return 0;

        boolean isVideo = false;
        for (int i = 0; i < getItemCount(); i++) {
            if (getItemBean(i) instanceof ADInfo) {
                ADInfo adInfo = (ADInfo) getItemBean(i);
                String url = UIUtils.isEmpty(adInfo.getImagePatch()) ? adInfo.getImageUrl() : adInfo.getImagePatch();
                if (MimeType.isVideoType(url)) {
                    isVideo = true;
                    break;
                }
            }
        }

        if (isVideo) return 2;
        else return 1;
    }


    public void setList(List<ADInfo> adInfos) {
        if (adInfos == null) adInfos = new ArrayList<>();

        boolean isVideo = false;
        for (ADInfo adInfo : adInfos) {
            if (MimeType.isVideoType(adInfo.getImageUrl())) {
                isVideo = true;
                break;
            }
        }
        int cout;
        if (isVideo) cout = videoCount;
        else cout = maxCount;

        if (adInfos.size() >= cout) {
            clear(getFirstPosition(), getItemCount() - 1);
            setDataList(adInfos.subList(0, cout));
        } else {
            setList(adInfos, getFirstPosition(), getLastPosition());
        }
    }

    public void addImage(String imageUrl) {
        ADInfo adInfo = new ADInfo();
        adInfo.setImagePatch(imageUrl);
        boolean isVideo = MimeType.isVideoType(adInfo.getImagePatch());
        int cout;
        if (isVideo) cout = videoCount;
        else cout = maxCount;
        if (cout > 0) {
            add(adInfo);
            if (cout == 1) {
                removeFooter(0);
            }
        }
    }

    /**
     * @return 0  全部成功   1正在上传  2 上传完毕但是图片上传失败
     */
    public int checkedUpload() {
        if (!isAutoUpload) return 0;
        boolean isLoading = false;
        boolean isErr = false;
        for (int i = 0; i < getItemCount(); i++) {
            if (getItemBean(i) instanceof ADInfo) {
                ADInfo adInfo = (ADInfo) getItemBean(i);
                if (UIUtils.isEmpty(adInfo.getImageUrl())) {
                    if (adInfo.getUpLoadState() == ImageUpLoadState.LoadIng || adInfo.getUpLoadState() == ImageUpLoadState.UNLoad) {
                        isLoading = true;
                    } else if (adInfo.getUpLoadState() == ImageUpLoadState.LoadERR) {
                        isErr = true;
                    }
                }
            }
        }

        if (isLoading) return 1;
        else if (isErr) return 2;//没有正在上传或等待上传，但有错误   上传完毕但有错误
        else return 0;
    }

    public void upLoadImage() {
        for (int i = 0; i < getItemCount(); i++) {
            if (getItemBean(i) instanceof ADInfo) {
                ADInfo adInfo = (ADInfo) getItemBean(i);
                //如果网络路经为空，本地路经不为空，则上传
                if (UIUtils.isEmpty(adInfo.getImageUrl()) && UIUtils.isNotEmpty(adInfo.getImagePatch())) {
                    Observable<Object> observable = getObservable(adInfo.getImagePatch());
                    if (observable == null) {
                        return;
                    }

                    LoadCallBack loadCallBack = observable.subscribeWith(new LoadCallBack() {

                        @Override
                        protected void onStart() {
                            super.onStart();
                            adInfo.setImageUrl("");
                            adInfo.setUpLoadState(ImageUpLoadState.LoadIng);
                        }

                        @Override
                        public void onError(Throwable e) {
                            super.onError(e);
                            adInfo.setUpLoadState(ImageUpLoadState.LoadERR);
                            //查找所在位置，有可能更换了位置
                            for (int i1 = 0; i1 < getChildCount(); i1++) {
                                if (getItemBean(i1) == adInfo) {
                                    notifyItemChanged(i1);
                                    break;
                                }
                            }
                        }

                        @Override
                        protected void onProgress(String percent) {
                            if (uploadImageCallBack != null) {
                                uploadImageCallBack.onProgress(percent);
                            }
                        }

                        @Override
                        protected void onSuccess(String obj) {
                            adInfo.setImageUrl(obj);
                            adInfo.setUpLoadState(ImageUpLoadState.LoadSuccess);
                        }

                        @Override
                        public void onComplete() {
                            super.onComplete();
                            int count = 0;
                            for (int i = 0; i < getItemCount(); i++) {
                                if (getItemBean(i) instanceof ADInfo) {
                                    ADInfo adInfo = (ADInfo) getItemBean(i);
                                    if (UIUtils.isEmpty(adInfo.getImageUrl()) && UIUtils.isNotEmpty(adInfo.getImagePatch())) {
                                        count++;
                                    }
                                }
                            }
                            float size = getChildCount();
                            BigDecimal pro = BigDecimal.valueOf((size - count) / size).setScale(2, RoundingMode.HALF_UP);
                            if (uploadImageCallBack != null) {
                                uploadImageCallBack.onProgressSum(pro.toString());
                                int err = checkedUpload();
                                if (err == 0) {
                                    uploadImageCallBack.onComplete(true);
                                } else if (err == 2) {
                                    uploadImageCallBack.onComplete(false);
                                }
                            }

                            upLoadImage();//递归回调
                        }
                    });
                    break;
                }
            }
        }


    }

    private void addImages(String path) {
        ADInfo adInfo = new ADInfo();
        adInfo.setImagePatch(path);
        add(adInfo);
    }

    public void addFoot() {
        addFooterView(new FooterBean(), BaseItemTypeInterface.TYPE_FOOT);
    }

    public void addFoot(String content) {
        addFooterView(new FooterBean(0, content), BaseItemTypeInterface.TYPE_FOOT);
    }


    public interface UploadImageCallBack {

        /**
         * 需重写此方法，更新进度
         *
         * @param percentSum 总进度
         */
        void onProgressSum(String percentSum);

        /**
         * @param percent 单图片进度
         */
        void onProgress(String percent);

        /**
         * @param success 是否全总上传成功
         */
        void onComplete(boolean success);
    }


}
