package com.heke.rihappclient.net.retrofit;

/**
 * Created by wgmhx on 2017/4/21.
 */


public class ApiException extends RuntimeException {
    public int retCode;
    public String msg;

    public ApiException(int retCode,String msg){
        this.retCode = retCode;
        this.msg = msg;
    }

    public String getMsg() {
        return msg;
    }

    public int getRetCode() {
        return retCode;
    }
}
