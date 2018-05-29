package com.heke.rihappclient.model;

import java.io.Serializable;

/**
 * Created by xssx5 on 2018-04-10.
 */

public class KL_outstore_model_detail implements IJsonModel,Serializable{
    public String billid ;
    public String billcode ;
    public String billItemId ;
    public String custjsStandard ;//客戶規格型號
    public String jsStandard ;//內部規格型號
    public String jsorderno ;
    public String batchno ;//規格說明
    public String dishno ;//盤號
    public String dishname;//盤具
    public String batchcode ;//
    public String outStockQty ;//數量
    public String storeP ;//倉庫倉位
    public String goodsid ;
    public String goodscode;
    public String goodsname ;
    public String storeID;
    public String storeAreaID;
    public String dishid;
    public String yipannumber;
    public String JInventoryTime;
    public String pandianperson;

    public String getPandianperson() {
        return pandianperson;
    }

    public void setPandianperson(String pandianperson) {
        this.pandianperson = pandianperson;
    }

    public String getJInventoryTime() {

        return JInventoryTime;
    }

    public void setJInventoryTime(String JInventoryTime) {
        this.JInventoryTime = JInventoryTime;
    }

    public String getYipannumber() {
        return yipannumber;
    }

    public void setYipannumber(String yipannumber) {
        this.yipannumber = yipannumber;
    }

    public String getGoodscode() {
        return goodscode;
    }

    public void setGoodscode(String goodscode) {
        this.goodscode = goodscode;
    }

    public String getDishid() {
        return dishid;
    }

    public void setDishid(String dishid) {
        this.dishid = dishid;
    }

    public String getStoreAreaID() {
        return storeAreaID;
    }

    public void setStoreAreaID(String storeAreaID) {
        this.storeAreaID = storeAreaID;
    }

    public String getStoreID() {
        return storeID;
    }

    public void setStoreID(String storeID) {
        this.storeID = storeID;
    }

    public String getCaijiQty() {
        return caijiQty;
    }

    public void setCaijiQty(String caijiQty) {
        this.caijiQty = caijiQty;
    }

    public String caijiQty;//采集数量

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

    public String getCustjsStandard() {
        return custjsStandard;
    }

    public void setCustjsStandard(String custjsStandard) {
        this.custjsStandard = custjsStandard;
    }

    public String getJsStandard() {
        return jsStandard;
    }

    public void setJsStandard(String jsStandard) {
        this.jsStandard = jsStandard;
    }

    public String getJsorderno() {
        return jsorderno;
    }

    public void setJsorderno(String jsorderno) {
        this.jsorderno = jsorderno;
    }

    public String getBatchno() {
        return batchno;
    }

    public void setBatchno(String batchno) {
        this.batchno = batchno;
    }

    public String getDishno() {
        return dishno;
    }

    public void setDishno(String dishno) {
        this.dishno = dishno;
    }

    public String getDishname() {
        return dishname;
    }

    public void setDishname(String dishname) {
        this.dishname = dishname;
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

    public String getStoreP() {
        return storeP;
    }

    public void setStoreP(String storeP) {
        this.storeP = storeP;
    }

    public String getGoodsid() {
        return goodsid;
    }

    public void setGoodsid(String goodsid) {
        this.goodsid = goodsid;
    }

    public String getGoodsname() {
        return goodsname;
    }

    public void setGoodsname(String goodsname) {
        this.goodsname = goodsname;
    }

    public String getBillcode() {

        return billcode;
    }

    public void setBillcode(String billcode) {
        this.billcode = billcode;
    }
}
