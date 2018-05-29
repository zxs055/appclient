package com.heke.rihappclient.net.retrofit;

import com.lzy.okgo.adapter.Call;

import okhttp3.ResponseBody;
import retrofit2.http.GET;
import retrofit2.http.Url;

/**
 * Created by wgmhx on 2017/4/21.
 */

public interface ApiStore {

    @GET
    Call<ResponseBody> downloadFile(@Url String fileUrl);
}
