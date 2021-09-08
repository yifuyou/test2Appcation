package com.base.common.app;

import androidx.fragment.app.FragmentActivity;


import io.reactivex.Observable;

/**
 * 公共组件的配置信息
 * 请求地址域名
 */
public interface ConfigCommon {

    //请求地址域名
    String getBaseUrl();



    /**
     * 图片选取
     *
     * @param activity
     * @param maxCount           图片的 最大数量
     * @param videoCount         视频的最大数量
     * @param isSelectedGif_Crop isSelectedGif_Crop[0] 是否选取gif  isSelectedGif_Crop[1] 是否剪切
     * @return
     */
    Observable<String> observable(FragmentActivity activity, int maxCount, int videoCount, boolean... isSelectedGif_Crop);
}
