package com.base.common.model.http.downLoad;

/**
 * Description: 下载进度回调
 */
public interface JsDownloadListener {

    void onStartDownload();

    void onProgress(long downLoadSize, long fileSize);

    void onFinishDownload();

    void onFail(String errorInfo);

}
