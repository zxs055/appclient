package com.heke.rihappclient.model;

import java.io.Serializable;

/**
 * Created by xssx5 on 2017/8/11.
 */

public class dacdiaobodan1 implements IJsonModel,Serializable {
    public String touserid ;//审核人Id
    public String userid ;//用户Id
    public String clientid ;//手机Id
    public String billState ;//单据状态
    public String billDate ;//单据日期
    public String billId ;//单据Id
    public String srcStoreId ;//调出仓库
    public String srcStoreName;//调出仓库名称
    public String itemNo ;//明细编号
    public String goodsId ;//商品Id
    public String billCode ;//商品编号
    public String desStoreId ;//调入仓库Id
    public String desStoreName ;//调入仓库名称
    public String goodsCode ;// 商品编号
    public String goodsName ;// 商品名称
    public String goodsScope ;// 商品规格
    public String barCode ;// 商品条码
    public String quantity ;// 调拨数量
    public String onhandQuantity ;// 库存数量，W_Onhandpositionbatch账面库存数
    public String batchCode ;//批号
    public String positionId ;//库位
    public String positionName;//库位名称
    public String cost; //库存价格
    public String totalQuantity;//总数量
    public  String amount;
    public String remarks;
    public String diaoboquantity;

    private  boolean ischeck;
    public boolean ischeck(){return  ischeck;}
    public void setIscheck(boolean ischeck){this.ischeck=ischeck;}
}
