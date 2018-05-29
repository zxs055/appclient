package com.heke.rihappclient.net.retrofit;

import android.util.Log;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by wgmhx on 2017/4/21.
 */

public class RetrofitCallback<T> implements Callback<T> {

    private ApiCallback<T> mCallback;
    private static final String TAG = "RetrofitCallback";

    public RetrofitCallback(ApiCallback<T> callback){
        this.mCallback = callback;
    }


    @Override
    public void onResponse(Call<T> call, Response<T> response) {
        if (response.isSuccessful()){
            if (((BaseRetData)response.body()).retCode == BaseRetData.RET_OK){
                mCallback.onSuccess(response.body());
            }else {
                BaseRetData baseRetData = (BaseRetData) response.body();
                mCallback.onError(baseRetData.retCode, baseRetData.msg);
            }
        }else {
            mCallback.onFailure();
        }
    }

    @Override
    public void onFailure(Call<T> call, Throwable t) {
        Log.e(TAG,"api failure,throw="+t.getMessage());
        t.printStackTrace();
        mCallback.onFailure();
    }
}
