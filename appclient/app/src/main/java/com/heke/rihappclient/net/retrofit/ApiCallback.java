package com.heke.rihappclient.net.retrofit;

/**
 * Created by wgmhx on 2017/4/21.
 */

public interface ApiCallback<T> {

    /**
     * 返回数据成功时回调
     * @param retCode
     */
    void onSuccess(T retCode);

    /**
     * 返回数据失败时回调
     */
    void onError(int err_code,String err_string);

    /**
     * 网络请求失败
     */
    void onFailure();

}

