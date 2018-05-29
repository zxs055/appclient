package com.heke.rihappclient.net.retrofit;

/**
 * Created by wgmhx on 2017/4/21.
 */

public interface FileDownloadCallback {
    /**
     * 下载成功
     */
    void onSuccess();

    /**
     * 下载进度
     */
    void onProgress(long fileSizeDownloaded,long fileSize);

    /**
     * 网络请求失败
     */
    void onFailure();
}
