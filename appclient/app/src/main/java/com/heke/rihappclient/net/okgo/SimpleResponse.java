package com.heke.rihappclient.net.okgo;

import java.io.Serializable;

/**
 * Created by wgmhx on 2017/4/21.
 */

public class SimpleResponse implements Serializable {

    public int code;
    public String msg;

    public LslResponse toResponse() {
        LslResponse LslResponse = new LslResponse();
        LslResponse.code = code;
        LslResponse.msg = msg;
        return LslResponse;
    }
}
