package com.heke.rihappclient.model;

import java.io.Serializable;

/**
 * Created by xssx5 on 2018-04-11.
 */

public class JL_APP_OutStoreDetail implements Serializable {
    /**
     * 序列化的版本号
     */
    private static final long serialVersionUID = 1L;
    /**
     * 主键
     */
    private int ID;
    /**
     * 发货通知单ID
     */
    private String billid;
    /**
     * 发货通知单明细ID
     */
    private String billItemId;
    /**
     * 到货批次
     */
    private String batchcode;
    /**
     * 验货数量
     */
    private String outStockQty;
    /**
     * 盘号
     */
    private String dishno;
    /**
     * 盘具ID
     */
    private String dishid;
    /**
     * 创建人ID
     */
    private String goodsid;
    /**
     * 仓库ID
     */
        private String storeID;
    /**
     * 库位ID
     */
    private String storeAreaID;
    /**
     * 单据类型  1.到货通知 2.发货通知
     */
    private String billtype;

    public String getBilltype() {
        return billtype;
    }

    public void setBilltype(String billtype) {
        this.billtype = billtype;
    }

    /**
     * set方法
     * get方法
     */
    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public String getBillid() {
        return billid;
    }

    public void setBillid(String billid) {
        this.billid = billid;
    }

    public String getBillItemId() {
        return billItemId;
    }

    public void setBillItemId(String billItemId) {
        this.billItemId = billItemId;
    }

    public String getBatchcode() {
        return batchcode;
    }

    public void setBatchcode(String batchcode) {
        this.batchcode = batchcode;
    }

    public String getOutStockQty() {
        return outStockQty;
    }

    public void setOutStockQty(String outStockQty) {
        this.outStockQty = outStockQty;
    }

    public String getDishno() {
        return dishno;
    }

    public void setDishno(String dishno) {
        this.dishno = dishno;
    }

    public String getDishid() {
        return dishid;
    }

    public void setDishid(String dishid) {
        this.dishid = dishid;
    }

    public String getGoodsid() {
        return goodsid;
    }

    public void setGoodsid(String goodsid) {
        this.goodsid = goodsid;
    }

    public String getStoreID() {
        return storeID;
    }

    public void setStoreID(String storeID) {
        this.storeID = storeID;
    }

    public String getStoreAreaID() {
        return storeAreaID;
    }

    public void setStoreAreaID(String storeAreaID) {
        this.storeAreaID = storeAreaID;
    }
}
