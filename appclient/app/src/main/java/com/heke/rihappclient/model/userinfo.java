package com.heke.rihappclient.model;

import java.io.Serializable;

/**
 * Created by wgmhx on 2017/4/21.
 */

public class userinfo implements IJsonModel,Serializable {
    public String userid;
    public String usercode;
    public String userpwd;
    public String username;
    public String storeid;
    public String storename;
    public String mobile;
    public String clientid;
    public String newpassword;
    public String limitstag;
    public String token;
}