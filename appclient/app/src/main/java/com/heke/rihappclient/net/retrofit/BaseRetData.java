package com.heke.rihappclient.net.retrofit;

/**
 * Created by wgmhx on 2017/4/21.
 */

public class BaseRetData<T> {
    public int retCode;
    public String msg;
    public T data;

    public static final int RET_OK = 200;
}
