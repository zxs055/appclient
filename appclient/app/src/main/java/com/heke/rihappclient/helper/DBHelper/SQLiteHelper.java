package com.heke.rihappclient.helper.DBHelper;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.heke.rihappclient.model.JL_APP_OutStoreDetail;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by xssx5 on 2018-04-19.
 */

public class SQLiteHelper extends SQLiteOpenHelper{

    /**
     *
     * @param context 上下文
     * @param name 数据库名称
     * @param factory  游标工厂
     * @param version 数据库版本号
     */
    public SQLiteHelper(Context context, String name, SQLiteDatabase.CursorFactory factory,
                        int version) {
        super(context, name, factory, version);
    }
    //创建数据库
    @Override
    public void onCreate(SQLiteDatabase db) {
        StringBuilder sqloutDetail=new StringBuilder();
        sqloutDetail.append("create table JL_APP_OutStoreDetail(ID integer primary key autoincrement");
        sqloutDetail.append(",billid varchar(20)");
        sqloutDetail.append(",billItemId varchar(20)");
        sqloutDetail.append(",billtype varchar(20)");
        sqloutDetail.append(",batchcode varchar(50)");
        sqloutDetail.append(",outStockQty varchar(20)");
        sqloutDetail.append(",dishno varchar(20)");
        sqloutDetail.append(",dishid varchar(20)");
        sqloutDetail.append(",goodsid varchar(20)");
        sqloutDetail.append(",storeID varchar(20)");
        sqloutDetail.append(",storeAreaID varchar(20))");
        db.execSQL(sqloutDetail.toString());
        Log.e("SQLiteHelper","数据库创建");
    }


    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.e("SQLiteHelper","数据库更新");
    }

    /**
     * 添加出库验货到数据库
     *
     * @param outmodel
     *            JL_APP_OutStoreDetail
     */
    public void addOutStore(JL_APP_OutStoreDetail outmodel) {
        Log.e("SqliteHelper", "插入出库验货");
        SQLiteDatabase db = getWritableDatabase(); // 以读写的形式打开数据库
        db.execSQL(
                "insert into JL_APP_OutStoreDetail(billid,billItemId,billtype,batchcode" +
                        ",outStockQty,dishno,dishid,goodsid,storeID,storeAreaID) values("
                        + String.format("'%s'", outmodel.getBillid()) + ","
                        + String.format("'%s'",outmodel.getBillItemId()) + ","
                        + String.format("'%s'",outmodel.getBilltype()) + ","
                        + String.format("'%s'",outmodel.getBatchcode()) + ","
                        + String.format("'%s'",outmodel.getOutStockQty()) + ","
                        + String.format("'%s'",outmodel.getDishno()) + ","
                        + String.format("'%s'",outmodel.getDishid()) + ","
                        + String.format("'%s'",outmodel.getGoodsid()) + ","
                        + String.format("'%s'",outmodel.getStoreID()) + ","
                        + String.format("'%s'",outmodel.getStoreAreaID()) +
                        ");"
        ); // 插入数据库

        db.close(); // 关闭数据库连接
    }

    /**
     * 删除出库数据
     * @param ID 所用ID
     * @param type 判断条件  0-发货通知单主键 1-发货通知单明细 其他-本表主键
     * @param billtype 单据类型
     */
    public void deleteOutStore(int ID,int type,String billtype) {
        Log.e("SqliteHelper", "删除JL_APP_OutStoreDetail");
        SQLiteDatabase db = getWritableDatabase(); //
        String sql;
        if(type==0){
            sql=" billtype = ? and billid = ?";
        }
        else if(type==1){
            sql=" billtype = ? and billItemId = ?";
        }else {
            sql= " billtype = ? and ID = ?";
        }
        String wheres[] = {billtype, String.valueOf(ID) };
        db.delete("JL_APP_OutStoreDetail", sql, wheres); // 数据库删除
        db.close(); // 关闭数据库
    }
    /**
     * 查询所有的JL_APP_OutStoreDetail的ID
     * @param billtype 单据类型
     * @return 所有JL_APP_OutStoreDetail的ID集合
     */
    public List<JL_APP_OutStoreDetail> queryAllOutStore(String billtype) {
        List<JL_APP_OutStoreDetail> list = new ArrayList<JL_APP_OutStoreDetail>();
        SQLiteDatabase db = getReadableDatabase(); // 以只读的方式打开数据库
        String sql = "select * from JL_APP_OutStoreDetail where billtype=? ;";
        Cursor cursor = db.rawQuery(sql, new String[]{billtype});
        while (cursor.moveToNext()) {
            int ID = cursor.getInt(cursor.getColumnIndex("ID"));
            String billid = cursor.getString(cursor.getColumnIndex("billid"));
            String billItemId = cursor.getString(cursor.getColumnIndex("billItemId"));
            String billtype1 = cursor.getString(cursor.getColumnIndex("billtype"));
            String batchcode = cursor.getString(cursor.getColumnIndex("batchcode"));
            String outStockQty = cursor.getString(cursor.getColumnIndex("outStockQty"));
            String dishno = cursor.getString(cursor.getColumnIndex("dishno"));
            String dishid = cursor.getString(cursor.getColumnIndex("dishid"));
            String goodsid = cursor.getString(cursor.getColumnIndex("goodsid"));
            String storeID = cursor.getString(cursor.getColumnIndex("storeID"));
            String storeAreaID = cursor.getString(cursor.getColumnIndex("storeAreaID"));
            JL_APP_OutStoreDetail outdetail=new JL_APP_OutStoreDetail();
            outdetail.setBillid(billid);
            outdetail.setBillItemId(billItemId);
            outdetail.setBilltype(billtype1);
            outdetail.setBatchcode(batchcode);
            outdetail.setOutStockQty(outStockQty);
            outdetail.setDishno(dishno);
            outdetail.setDishid(dishid);
            outdetail.setGoodsid(goodsid);
            outdetail.setStoreID(storeID);
            outdetail.setStoreAreaID(storeAreaID);
            System.out.println(" ---- 出库明细 = " + billid+"-"+billItemId+"-"+outStockQty);
            list.add(outdetail); // 添加到数组
        }
        cursor.close(); // 关闭游标
        db.close(); // 关闭数据库
        return list;
    }

    /**
     * 根据id查询JL_APP_OutStoreDetail
     *
     * @param ID id
     * @param type 0-发货通知单主键 1-发货通知单明细 其他-本表主键
     * @param billtype 单据类型
     * @return JL_APP_OutStoreDetail
     */
    public List<JL_APP_OutStoreDetail> queryOutStoreById(int ID,int type,String billtype) {
        List<JL_APP_OutStoreDetail> outDetaillist = new ArrayList<JL_APP_OutStoreDetail>();
        SQLiteDatabase db = getReadableDatabase(); // 以只读方式打开数据库
        String[] columns={"ID", "billid", "billItemId", "billtype", "batchcode", "outStockQty", "dishno", "dishid", "goodsid", "storeID", "storeAreaID"};
        String selection;
        if(type==0){
            selection="billid = ? and billtype= ?";
        }
        else if(type==1){
            selection="billItemId = ? and billtype= ?";
        }else {
            selection= "ID = ? and billtype= ?";
        }
        String[] selectionArgs = { String.valueOf(ID),billtype };
        Cursor cursor = db.query("JL_APP_OutStoreDetail", columns, selection, selectionArgs,
                null, null, null);
        if (cursor.moveToNext()) {
            JL_APP_OutStoreDetail outDetail = new JL_APP_OutStoreDetail();
            outDetail.setID(cursor.getInt(cursor.getColumnIndex("ID")));
            outDetail.setBillid(cursor.getString(cursor.getColumnIndex("billid")));
            outDetail.setBillItemId(cursor.getString(cursor.getColumnIndex("billItemId")));
            outDetail.setBilltype(cursor.getString(cursor.getColumnIndex("billtype")));
            outDetail.setBatchcode(cursor.getString(cursor.getColumnIndex("batchcode")));
            outDetail.setOutStockQty(cursor.getString(cursor.getColumnIndex("outStockQty")));
            outDetail.setDishno(cursor.getString(cursor.getColumnIndex("dishno")));
            outDetail.setDishid(cursor.getString(cursor.getColumnIndex("dishid")));
            outDetail.setGoodsid(cursor.getString(cursor.getColumnIndex("goodsid")));
            outDetail.setStoreID(cursor.getString(cursor.getColumnIndex("storeID")));
            outDetail.setStoreAreaID(cursor.getString(cursor.getColumnIndex("storeAreaID")));
            outDetaillist.add(outDetail);
        }
        return outDetaillist;
    }
    /**
     * 更新Person
     *
     * @param model
     *            Person
     */
    public void updateOutstore(JL_APP_OutStoreDetail model) {
        Log.e("SqliteHelper", "更新");
        SQLiteDatabase db = getWritableDatabase(); // 以读写的形式打开数据库
        String sql = "update JL_APP_OutStoreDetail set billid="
                + String.format("'%s'", model.getBillid())
                + ",billItemId=" + String.format("'%s'", model.getBillItemId())
                + ",billtype=" + String.format("'%s'", model.getBilltype())
                + ",batchcode=" + String.format("'%s'", model.getBatchcode())
                + ",outStockQty=" + String.format("'%s'", model.getOutStockQty())
                + ",dishno=" + String.format("'%s'", model.getDishno())
                + ",dishid=" + String.format("'%s'", model.getDishid())
                + ",goodsid=" + String.format("'%s'", model.getGoodsid())
                + ",storeID=" + String.format("'%s'", model.getStoreID())
                + ",storeAreaID=" + String.format("'%s'", model.getStoreAreaID())
                + " where ID=" + model.getID();

        Log.e("updateOutstore", sql);
        db.execSQL(sql); // 更新数据库
        db.close(); // 关闭数据库连接
    }
}
