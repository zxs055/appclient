package com.heke.rihappclient.model;

import java.io.Serializable;

/**
 * Created by wgmhx on 2017/4/21.
 */

public class baseinfo implements Serializable,IJsonModel{
    //ID
    public String baseid;
    //名称
    public String basename;
    public String id ;
    public String code ;
    public String name ;
    public String spec ;
    public String barcode ;
    public String number;

    private  boolean ischeck;
    public boolean ischeck(){return  ischeck;}
    public void setIscheck(boolean ischeck){this.ischeck=ischeck;}

    //old
    public String invid;
    public String invcode;
    public String invname;
    public String invspec;
    public String kucnum;
}
