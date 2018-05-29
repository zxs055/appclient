package com.heke.rihappclient.WebService;

import android.content.Context;

import com.google.gson.Gson;
import com.heke.rihappclient.Config.Consts;
import com.heke.rihappclient.model.JL_APP_OutStoreDetail;
import com.heke.rihappclient.model.KL_outstore_model;
import com.heke.rihappclient.model.KL_outstore_model_detail;
import com.heke.rihappclient.model.baseinfo;
import com.heke.rihappclient.model.rukuinfo;
import com.heke.rihappclient.model.userinfo;
import com.heke.rihappclient.model.dacdiaobodan1;
import com.heke.rihappclient.model.dacdiaobodan;
import com.heke.rihappclient.net.okgo.JsonCallback;
import com.heke.rihappclient.net.okgo.LslResponse;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.model.HttpHeaders;
import com.lzy.okgo.model.HttpParams;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;

/**
 * Created by wgmhx on 2017/4/21.
 */

public class AppService {
    private static AppService instance;
    private userinfo mCurrentUser;


    private AppService(){
    }

    public static AppService getInstance(){
        if (instance == null){
            instance = new AppService();
        }
        return instance;
    }

    /**
     * 退出登录时重置此类
     */
    public static void resetInstance(){
        instance = null;
    }

    /**
     * 获取当前登录的用户
     * @return  当前用户
     */
    public userinfo getCurrentUser(){
        return mCurrentUser;
    }

    public void setCurrentUser(userinfo user){
        this.mCurrentUser = user;
    }
   /* public void setToken(String _token){this.token=_token;}
    public String getToken(){return token;};*/

    public static String getDeviceId(Context context) {
        return android.provider.Settings.Secure.getString(
                context.getContentResolver(),
                android.provider.Settings.Secure.ANDROID_ID);
    }

    /***************    用户系统 Begin    ******************/



    /**
     * 异步用户登录
     * @param username  用户名
     * @param password  用户密码
     * @param clientid    客户端ID
     * @param callback  回调
     */
    public void loginAsync(Context context,String username,String password,String clientid,JsonCallback<LslResponse<userinfo>> callback){
        String url = Consts.API_SERVICE_HOST + "/Login/CheckLogin";
        HashMap<String,String> postParams = new HashMap<>();
        postParams.put("usercode",username);
        postParams.put("userpwd",password);
        postParams.put("clienttag",clientid);
        OkGo.post(url).tag(context).params(postParams).execute(callback);
    }
    /**
     * 异步获取服务器列表
     * @param callback
     */
    public void getServerlistAsync(JsonCallback<LslResponse<List<baseinfo>>>  callback){
        String url = Consts.API_SERVICE_HOST+"/Login/GetOrganiseList";
        OkGo.get(url).execute(callback);
    }
    /**
     * 异步获取班次列表
     * @param callback
     */
    public void getbancilistAsync(String token,JsonCallback<LslResponse<List<baseinfo>>>  callback){
        String url = Consts.API_SERVICE_HOST+"/Task/GetBanciList";
        OkgoAddHeader(token);
        OkGo.get(url).execute(callback);
    }
    /**
     * 异步获取生产中心列表
     * @param callback
     */
    public void getproductcenterlistAsync(String token,JsonCallback<LslResponse<List<baseinfo>>>  callback){
        String url = Consts.API_SERVICE_HOST+"/Task/GetProductCenterList";
        OkgoAddHeader(token);
        OkGo.get(url).execute(callback);
    }

    /**
     * 获取设备列表
     * @param uid
     * @param clientid
     * @param callback
     */
    public void getshebeilistAsync(String token,String clientid,String pCenterID,JsonCallback<LslResponse<List<baseinfo>>> callback){
        String url = Consts.API_SERVICE_HOST+"/Task/GetShebeiList?clientid="+clientid+"&pcenterid="+pCenterID;
        OkgoAddHeader(token);
        OkGo.get(url).execute(callback);
    }
    /**
     * 获取发货单列表
     * @param uid
     * @param clientid
     * @param callback
     */
    public void getfahuobilllistAsync(String token,String clientid,JsonCallback<LslResponse<List<KL_outstore_model>>> callback){
        String url = Consts.API_SERVICE_HOST+"/Task/GetfahuobillList?clientid="+clientid;
        OkgoAddHeader(token);
        OkGo.get(url).execute(callback);
    }
    /**
     * 获取发货单列表
     * @param uid
     * @param clientid
     * @param callback
     */
    public void getfahuoDetaillistAsync(String token,String billid,String clientid,JsonCallback<LslResponse<List<KL_outstore_model_detail>>> callback){
        String url = Consts.API_SERVICE_HOST+"/Task/GetfahuoDetailList?clientid="+clientid+"&billid="+billid;
        OkgoAddHeader(token);
        OkGo.get(url).execute(callback);
    }
    /**
     * 提交发货数据
     */
    public void commitOutStroeListAsyn(String token, String userid, List<JL_APP_OutStoreDetail> list, JsonCallback<LslResponse<Object>> callback){
        String url = Consts.API_SERVICE_HOST + "/Task/CommitOutStoreList";
        Gson gson =new Gson();
        String keys=gson.toJson(list);
        HttpParams par=new HttpParams();
        par.put("",keys);
        OkgoAddHeader(token);
//        OkGo.getInstance().addCommonParams(par);
        HashMap<String,String> postParams = new HashMap<>();
        postParams.put("code",keys);
        OkGo.post(url).params(postParams).execute(callback);
    }

    /**
     * 获取到货单列表
     * @param uid
     * @param clientid
     * @param callback
     */
    public void getdaohuobilllistAsync(String token,String clientid,JsonCallback<LslResponse<List<KL_outstore_model>>> callback){
        String url = Consts.API_SERVICE_HOST+"/InStore/GetdaohuobillList?clientid="+clientid;
        OkgoAddHeader(token);
        OkGo.get(url).execute(callback);
    }
    /**
     * 获取到货单列表
     * @param uid
     * @param clientid
     * @param callback
     */
    public void getdaohuoDetaillistAsync(String token,String billid,String clientid,JsonCallback<LslResponse<List<KL_outstore_model_detail>>> callback){
        String url = Consts.API_SERVICE_HOST+"/InStore/GetdaohuoDetailList?clientid="+clientid+"&billid="+billid;
        OkgoAddHeader(token);
        OkGo.get(url).execute(callback);
    }
    /**
     * 提交到货通知验货数据
     */
    public void commitInStroeListAsyn(String token, String userid, List<JL_APP_OutStoreDetail> list, JsonCallback<LslResponse<Object>> callback){
        String url = Consts.API_SERVICE_HOST + "/InStore/CommitInStoreList";
        Gson gson =new Gson();
        String keys=gson.toJson(list);
        HashMap<String,String> postParams = new HashMap<>();
        postParams.put("code",keys);
        OkgoAddHeader(token);
        OkGo.post(url).params(postParams).execute(callback);
    }
    /**
     * 采购申请单商品资料
     * @param userid
     * @param clientid
     * @param barcode
     * @param callback
     */
    public void getstoreplistAsync(String userid,String clientid,String token,JsonCallback<LslResponse<List<baseinfo>>>  callback){
        String url = Consts.API_SERVICE_HOST+"/InStore/Getstoreplist?userid="+userid+"&clientid="+clientid;
        OkgoAddHeader(token);
        OkGo.get(url).execute(callback);
    }

    /**
     * 获取盘点单列表
     * @param uid
     * @param clientid
     * @param callback
     */
    public void getpandianbilllistAsync(String token,String clientid,JsonCallback<LslResponse<List<KL_outstore_model>>> callback){
        String url = Consts.API_SERVICE_HOST+"/PanDian/GetpandianbillList?clientid="+clientid;
        OkgoAddHeader(token);
        OkGo.get(url).execute(callback);
    }
    /**
     * 获取盘点单明细列表
     * @param uid
     * @param clientid
     * @param callback
     */
    public void getpandianDetaillistAsync(String token,String billid,String clientid,JsonCallback<LslResponse<List<KL_outstore_model_detail>>> callback){
        String url = Consts.API_SERVICE_HOST+"/PanDian/GetpandianDetailList?clientid="+clientid+"&billid="+billid;
        OkgoAddHeader(token);
        OkGo.get(url).execute(callback);
    }

    /**
     * 盘点单商品资料
     * @param token
     * @param billid
     * @param barcode
     * @param callback
     */
    public void getpandisnstoregoodslistAsync(String token,String billid,String barcode,JsonCallback<LslResponse<List<KL_outstore_model_detail>>>  callback){
        String url = Consts.API_SERVICE_HOST+"/PanDian/Getpandiantoregoodslist?billid="+billid+"&barcode="+barcode;
        OkgoAddHeader(token);
        OkGo.get(url).execute(callback);
    }
    /**
     * 获取车间发料列表
     * @param uid
     * @param clientid
     * @param callback
     * @param banci 班次ID
     * @param processid 设备ID
     */
    public void getplantOutStoreListAsync(String banci,String processid,String token,String clientid,JsonCallback<LslResponse<List<KL_outstore_model>>> callback){
        String url = Consts.API_SERVICE_HOST+"/PlantOutStore/GetPlantOutStoreList?banci="+banci+"&processid="+processid;
        OkgoAddHeader(token);
        OkGo.get(url).execute(callback);
    }
    /**
     * 提交到货通知验货数据
     */
    public void commitpandianItemAsyn(String token, String userid, String billitemid,double allnum, JsonCallback<LslResponse<Object>> callback){
        String url = Consts.API_SERVICE_HOST + "/PanDian/CommitPandianItem?userid="+userid+"&billitemid="+billitemid+"&allnum="+allnum;
        OkgoAddHeader(token);
        OkGo.get(url).execute(callback);
    }




























    /**
     * 异步用户修改密码
     * @param userid  用户名
     * @param password  新密码
     * @param callback  回调
     */
    public void resetPwdAsync(String userid,String password,String newpassword,String clientid,JsonCallback<LslResponse<userinfo>> callback){
        String url = Consts.API_SERVICE_HOST + "/Login/ModifyPwd";
        HashMap<String,String> postParams = new HashMap<>();
        postParams.put("userid",userid);
        postParams.put("userpwd",password);
        postParams.put("newuserpwd",newpassword);
        postParams.put("clienttag",clientid);
        OkGo.post(url).params(postParams).execute(callback);
    }

    /**
     * 用户退出
     * @param callback
     */
    public void getUserExitAsync(String userid,String clientid,JsonCallback<LslResponse<userinfo>>  callback){
        String url = Consts.API_SERVICE_HOST + "/Login/UserExit?userid="+userid+"&clientid="+clientid;
        OkGo.get(url).execute(callback);
    }

    /**
     * 异步获取通知列表
     * @param callback
     */
    public void getNewslistAsync(String userid,String clientid,JsonCallback<LslResponse<List<baseinfo>>>  callback){
        String url = Consts.API_SERVICE_HOST+"/Base/getnewslist?userid="+userid+"&clientid="+clientid;
        OkGo.get(url).execute(callback);
    }
    /***************    用户系统 End    ******************/

    /***************    信息系統 Begin    ******************/

    /**
     * 异步获取仓库基础信息信息
     * @param callback  回调
     */
    public void getStordocAsync(String userid,JsonCallback<LslResponse<List<baseinfo>>>  callback){
        String url = Consts.API_SERVICE_HOST+"/Base/GetCangkuList?userid="+userid;
        OkGo.get(url).execute(callback);
    }

    /**
     * 异步获取客户基础信息信息
     * @param callback  回调
     */
    public void getInvdocAsync(String userid,String clientid,String barcode,JsonCallback<LslResponse<baseinfo>>  callback){
        String url = Consts.API_SERVICE_HOST+"/Base/Getinvdoc?userid="+userid+"&clientid="+clientid+"&barcode="+barcode;
        OkGo.get(url).execute(callback);
    }


    /**
     * 异步获取商品基础信息
     * @param callback  回调
     */
    public void getInvdoclistAsync(String userid,String clientid,String barcode,JsonCallback<LslResponse<List<rukuinfo>>>  callback){
        String url = Consts.API_SERVICE_HOST+"/Base/Getinvdoclist?userid="+userid+"&clientid="+clientid+"&barcode="+barcode;
        OkGo.get(url).execute(callback);
    }
    /**
     * 异步获取存货基础信息信息模糊查询条码
     * @param callback  回调
     */
    public void getInvdoclikelistAsync(String userid,String clientid,String barcode,JsonCallback<LslResponse<List<rukuinfo>>>  callback){
        String url = Consts.API_SERVICE_HOST+"/Base/Getinvdoclikelist?userid="+userid+"&clientid="+clientid+"&barcode="+barcode;
        OkGo.get(url).execute(callback);
    }
    /**
     * 异步获取商品基础信息+库存（商品不限门店）
     * @param callback  回调
     */
    public void getInvdockclistAsync(String userid,String clientid,String barcode,JsonCallback<LslResponse<List<rukuinfo>>>  callback){
        String url = Consts.API_SERVICE_HOST+"/Base/Getinvdockclist?userid="+userid+"&clientid="+clientid+"&barcode="+barcode;
        OkGo.get(url).execute(callback);
    }

    /**
     * 异步获取商品基础信息+库存+只显示本门店的库存
     * @param callback  回调
     */
    public void getInvdocstorelistAsync(String userid,String clientid,String barcode,JsonCallback<LslResponse<List<rukuinfo>>>  callback){
        String url = Consts.API_SERVICE_HOST+"/Base/Getinvdocstorelist?userid="+userid+"&clientid="+clientid+"&barcode="+barcode;
        OkGo.get(url).execute(callback);
    }
    /**
     * 异步获取商品基础信息盘点信息库存信息
     * @param callback  回调
     */
    public void getInvdocpdlistAsync(String userid,String clientid,String barcode,String billcode,JsonCallback<LslResponse<List<rukuinfo>>>  callback){
        String url = Consts.API_SERVICE_HOST+"/Pandian/Getinvdocpdlist?userid="+userid+"&clientid="+clientid+"&barcode="+barcode+"&billcode="+billcode;
        OkGo.get(url).execute(callback);
    }
    /**
     * 异步获取盘点商品模糊查询条码
     * @param callback  回调
     */
    public void getpandbarcodelistAsync(String userid,String clientid,String barcode,JsonCallback<LslResponse<List<rukuinfo>>>  callback){
        String url = Consts.API_SERVICE_HOST+"/Pandian/Getpandbarcodelist?userid="+userid+"&clientid="+clientid+"&barcode="+barcode;
        OkGo.get(url).execute(callback);
    }
    /**
     * 异步插入数据
     * @param rukuInfo  盘点单头
     * @param callback  回调
     */
    public void SavePandianheadAsync(rukuinfo rukuInfo, String clientid, JsonCallback<LslResponse<rukuinfo>> callback){
        String url = Consts.API_SERVICE_HOST+"/Pandian/SavePandianhead";
        HashMap<String,String> postParams = new HashMap<>();
        postParams.put("clientid",clientid);
        postParams.put("userid",rukuInfo.userid);
        postParams.put("billdate",rukuInfo.billdate);
        postParams.put("storeid",rukuInfo.storeid);
        postParams.put("remarks",rukuInfo.remarks);
        OkGo.post(url).params(postParams).execute(callback);
    }
    /**
     * 异步插入数据
     * @param rukuInfo  盘点单表体
     * @param callback  回调
     */
    public void SavePandianbodyAsync(rukuinfo rukuInfo, String clienid, JsonCallback<LslResponse<Object>> callback){
        String url = Consts.API_SERVICE_HOST+"/Pandian/SavePandianbody";
        HashMap<String,String> postParams = new HashMap<>();
        postParams.put("clientid",clienid);
        postParams.put("userid",rukuInfo.userid);
        postParams.put("billid",rukuInfo.billid);
        postParams.put("invid",rukuInfo.invid);
        postParams.put("barcode",rukuInfo.barcode);
        postParams.put("quantity",rukuInfo.quantity);
        postParams.put("price",rukuInfo.price);
        postParams.put("kucnum",rukuInfo.kucnum);
        OkGo.post(url).params(postParams).execute(callback);
    }
    /**
     * 获取盘点单
     * @param userid
     * @param billcode
     * @param clientid
     * @param callback
     */
    public void getpandianlistAsync(String userid,String clientid,String billcode,JsonCallback<LslResponse<List<rukuinfo>>> callback){
        String url = Consts.API_SERVICE_HOST+"/Pandian/Getpandianlist?userid="+userid+"&clientid="+clientid+"&billcode="+billcode;
        OkGo.get(url).execute(callback);
    }

    /**
     * 获取盘点单头
     * @param userid
     * @param clientid
     * @param callback
     */
    public void getpandianheadlistAsync(String userid,String clientid,JsonCallback<LslResponse<List<rukuinfo>>> callback){
        String url = Consts.API_SERVICE_HOST+"/Pandian/getpandianheadlist?userid="+userid+"&clientid="+clientid;
        OkGo.get(url).execute(callback);
    }

    /**
     * 获取盘点明细单
     * @param userid
     * @param billId
     * @param callback
     */
    public void getpandianmxlistAsync(String userid,String clientid,String billId,JsonCallback<LslResponse<List<rukuinfo>>> callback){
        String url = Consts.API_SERVICE_HOST+"/Pandian/Getpandianmxlist?userid="+userid+"&clientid="+clientid+"&billId="+billId;
        OkGo.get(url).execute(callback);
    }

    /**
     * 获取盘点单期数
     * @param userid
     * @param clientid
     * @param callback
     */
    public void getpandianheadAsync(String userid,String clientid,JsonCallback<LslResponse<rukuinfo>> callback){
        String url = Consts.API_SERVICE_HOST+"/Pandian/Getpandianhead?userid="+userid+"&clientid="+clientid+"";
        OkGo.get(url).execute(callback);
    }
    /**
     * 删除盘点单表头
     * @param billid    盘点单id
     * @param callback  回调接口
     */
    public void deletepandianheadAsync(String billid,String userid,String clientid,JsonCallback<LslResponse<Object>> callback){
        String url = Consts.API_SERVICE_HOST+"/Pandian/Deletepandianhead?billid="+billid+"&userid="+userid+"&clientid="+clientid+"";
        OkGo.get(url).execute(callback);
    }
    /**
     * 删除盘点单表体
     * @param billid    盘点单id
     * @param callback  回调接口
     */
    public void deletepandianbodyAsync(String billid,String billbid,String userid,String clientid,JsonCallback<LslResponse<Object>> callback){
        String url = Consts.API_SERVICE_HOST+"/Pandian/Deletepandianbody?billid="+billid+"&billbid="+billbid+"&userid="+userid+"&clientid="+clientid+"";
        OkGo.get(url).execute(callback);
    }

    /**
     * 获取采购单
     * @param uid
     * @param clientid
     * @param callback
     */
    public void getcgorderheadAsync(String userid,String clientid,String billstate,JsonCallback<LslResponse<List<rukuinfo>>> callback){
        String url = Consts.API_SERVICE_HOST+"/cgorder/Getcgorderhead?userid="+userid+"&clientid="+clientid+"&billstate="+billstate;
        OkGo.get(url).execute(callback);
    }
    /**
     * 获取采购单表体
     * @param uid
     * @param clientid
     * @param callback
     */
    public void getcgorderbodyAsync(String billid,String userid,String clientid,JsonCallback<LslResponse<List<rukuinfo>>> callback){
        String url = Consts.API_SERVICE_HOST+"/cgorder/Getcgorderbody?billid="+billid+"&userid="+userid+"&clientid="+clientid+"";
        OkGo.get(url).execute(callback);
    }

    /**
     * 采购单商品资料
     * @param userid
     * @param clientid
     * @param barcode
     * @param callback
     */
    public void getcgInvdocstorelistAsync(String userid,String clientid,String barcode,JsonCallback<LslResponse<List<rukuinfo>>>  callback){
        String url = Consts.API_SERVICE_HOST+"/cgorder/Getinvdocstorelist?userid="+userid+"&clientid="+clientid+"&barcode="+barcode;
        OkGo.get(url).execute(callback);
    }
    /**
     * 异步插入数据
     * @param rukuInfo  采购单头
     * @param callback  回调
     */
    public void SavecgorderheadAsync(rukuinfo rukuInfo, String clientid, JsonCallback<LslResponse<rukuinfo>> callback){
        String url = Consts.API_SERVICE_HOST+"/cgorder/Savecgorderhead";
        HashMap<String,String> postParams = new HashMap<>();
        postParams.put("clientid",clientid);
        postParams.put("userid",rukuInfo.userid);
        postParams.put("billdate",rukuInfo.billdate);
        postParams.put("storeid",rukuInfo.storeid);
        postParams.put("remarks",rukuInfo.remarks);
        OkGo.post(url).params(postParams).execute(callback);
    }
    /**
     * 异步插入数据
     * @param rukuInfo  采购单表体
     * @param callback  回调
     */
    public void SavecgorderbodyAsync(rukuinfo rukuInfo, String clientid, JsonCallback<LslResponse<Object>> callback){
        String url = Consts.API_SERVICE_HOST+"/cgorder/Savecgorderbody";
        HashMap<String,String> postParams = new HashMap<>();
        postParams.put("clientid",clientid);
        postParams.put("userid",rukuInfo.userid);
        postParams.put("billid",rukuInfo.billid);
        postParams.put("invid",rukuInfo.invid);
        postParams.put("barcode",rukuInfo.barcode);
        postParams.put("quantity",rukuInfo.quantity);
        postParams.put("price",rukuInfo.price);
        postParams.put("invcode",rukuInfo.invcode);
        postParams.put("invname",rukuInfo.invname);
        postParams.put("invspec",rukuInfo.invspec);
        postParams.put("kuweiid",rukuInfo.kuweiid);
        postParams.put("remarks",rukuInfo.remarks);
        OkGo.post(url).params(postParams).execute(callback);
    }

    /**
     * 商品采购条码
     * @param userid
     * @param clientid
     * @param barcode
     * @param callback
     */
    public void GetcgorderbarcodelistAsync(String userid,String clientid,String barcode,JsonCallback<LslResponse<List<baseinfo>>>  callback){
        String url = Consts.API_SERVICE_HOST+"/cgorder/Getcgorderbarcodelist?userid="+userid+"&clientid="+clientid+"&barcode="+barcode;
        OkGo.get(url).execute(callback);
    }
    /**
     * 提交采购单
     * @param userid
     * @param clientid
     * @param billid
     * @param callback
     */
    public void getcommitcgAsync(String userid,String clientid,String billid,JsonCallback<LslResponse<Object>>  callback){
        String url = Consts.API_SERVICE_HOST+"/cgorder/Getcommitcgorder?userid="+userid+"&clientid="+clientid+"&billid="+billid;
        OkGo.get(url).execute(callback);
    }
    /**
     * 删除采购单
     * @param billid    采购单id
     * @param callback  回调接口
     */
    public void deletecgorderheadAsync(String billid,String userid,String clientid,JsonCallback<LslResponse<Object>> callback){
        String url = Consts.API_SERVICE_HOST+"/cgorder/Deletecgorderhead";
        HashMap<String,String> postParams = new HashMap<>();
        postParams.put("userid",userid);
        postParams.put("billid",billid);
        postParams.put("clientid",clientid);
        OkGo.post(url).params(postParams).execute(callback);
    }
    /**
     * 删除采购单表体
     * @param billid    采购单id
     * @param callback  回调接口
     */
    public void deletecgorderbodyAsync(String billid,String billbid,String userid,String clientid,JsonCallback<LslResponse<Object>> callback){
        String url = Consts.API_SERVICE_HOST+"/cgorder/Deletecgorderbody";
        HashMap<String,String> postParams = new HashMap<>();
        postParams.put("userid",userid);
        postParams.put("billid",billid);
        postParams.put("billbid",billbid);
        postParams.put("clientid",clientid);
        OkGo.post(url).params(postParams).execute(callback);
    }

    /**
     * 异步插入数据
     * @param rukuInfo  销售单头
     * @param callback  回调
     */
    public void SavesaleheadAsync(rukuinfo rukuInfo, String clientid, JsonCallback<LslResponse<rukuinfo>> callback){
        String url = Consts.API_SERVICE_HOST+"/Sale/Savesalehead";
        HashMap<String,String> postParams = new HashMap<>();
        postParams.put("clientid",clientid);
        postParams.put("userid",rukuInfo.userid);
        postParams.put("billdate",rukuInfo.billdate);
        postParams.put("storeid",rukuInfo.storeid);
        postParams.put("remarks",rukuInfo.remarks);
        OkGo.post(url).tag(this).params(postParams).execute(callback);
    }

    /**
     * 异步插入数据
     * @param rukuInfo  销售单表体
     * @param callback  回调
     */
    public void SaveSalebodyAsync(rukuinfo rukuInfo, String clientid, JsonCallback<LslResponse<Object>> callback){
        String url = Consts.API_SERVICE_HOST+"/Sale/Savesalebody";
        HashMap<String,String> postParams = new HashMap<>();
        postParams.put("clientid",clientid);
        postParams.put("userid",rukuInfo.userid);
        postParams.put("billid",rukuInfo.billid);
        postParams.put("invid",rukuInfo.invid);
        postParams.put("barcode",rukuInfo.barcode);
        postParams.put("quantity",rukuInfo.quantity);
        postParams.put("price",rukuInfo.price);
        postParams.put("isSpcPrice",rukuInfo.isSpcPrice);
        postParams.put("storeid",rukuInfo.storeid);
        OkGo.post(url).tag(this).params(postParams).execute(callback);
    }

    /**
     * 删除销售单
     * @param billid    销售单id
     * @param callback  回调接口
     */
    public void deletesaleheadAsync(String billid,String userid,String clientid,JsonCallback<LslResponse<Object>> callback){
        String url = Consts.API_SERVICE_HOST+"/Sale/Deletesalehead";
        HashMap<String,String> postParams = new HashMap<>();
        postParams.put("userid",userid);
        postParams.put("billid",billid);
        postParams.put("clientid",clientid);
        OkGo.post(url).params(postParams).execute(callback);
    }
    /**
     * 删除销售单表体
     * @param billid    销售单id
     * @param billbid    销售单表体id
     * @param callback  回调接口
     */
    public void deletesalebodyAsync(String billid,String billbid,String userid,String clientid,JsonCallback<LslResponse<Object>> callback){
        String url = Consts.API_SERVICE_HOST+"/Sale/Deletesalebody";
        HashMap<String,String> postParams = new HashMap<>();
        postParams.put("userid",userid);
        postParams.put("billid",billid);
        postParams.put("billbid",billbid);
        postParams.put("clientid",clientid);
        OkGo.post(url).params(postParams).execute(callback);
    }

    /**
     * 销售商品条码查询前10条
     * @param userid
     * @param clientid
     * @param barcode
     * @param callback
     */
    public void getsalebarcodelistAsync(String userid,String clientid,String barcode,JsonCallback<LslResponse<List<baseinfo>>>  callback){
        String url = Consts.API_SERVICE_HOST+"/Sale/Getsalebarcodelist?userid="+userid+"&clientid="+clientid+"&barcode="+barcode;
        OkGo.get(url).execute(callback);
    }

    /**
     * 通过编码查询商品
     * @param userid
     * @param clientid
     * @param barcode
     * @param callback
     */
    public void getgoodscodelistAsync(String userid,String clientid,String barcode,JsonCallback<LslResponse<List<baseinfo>>>  callback){
        String url = Consts.API_SERVICE_HOST+"/Sale/Getgoodscodelist?userid="+userid+"&clientid="+clientid+"&barcode="+barcode;
        OkGo.get(url).execute(callback);
    }

    /**
     * 根据商品条码查询商品信息
     * @param userid
     * @param clientid
     * @param barcode
     * @param callback
     */
    public void getsaleinvdocAsync(String userid,String clientid,String barcode,JsonCallback<LslResponse<List<rukuinfo>>>  callback){
        String url = Consts.API_SERVICE_HOST+"/Sale/Getsaleinvdoc?userid="+userid+"&clientid="+clientid+"&barcode="+barcode;
        OkGo.get(url).execute(callback);
    }
    /**
     * 获取销售单
     * @param userid
     * @param clientid
     * @param callback
     */
    public void getsaleheadAsync(String userid,String clientid,String billstate,JsonCallback<LslResponse<List<rukuinfo>>> callback){
        String url = Consts.API_SERVICE_HOST+"/Sale/Getsalehead?userid="+userid+"&clientid="+clientid+"&billstate="+billstate;
        OkGo.get(url).execute(callback);
    }
    /**
     * 获取销售单表体
     * @param userid
     * @param clientid
     * @param callback
     */
    public void getsalebodyAsync(String billid,String userid,String clientid,JsonCallback<LslResponse<List<rukuinfo>>> callback){
        String url = Consts.API_SERVICE_HOST+"/Sale/Getsalebody?billid="+billid+"&userid="+userid+"&clientid="+clientid+"";
        OkGo.get(url).execute(callback);
    }

    /**
     * 异步获取今日统计列表
     * @param callback
     */
    public void gettodayinfoAsync(String userid,String clientid,JsonCallback<LslResponse<rukuinfo>>  callback){
        String url = Consts.API_SERVICE_HOST+"/Base/Gettodayinfo?userid="+userid+"&clientid="+clientid;
        OkGo.get(url).execute(callback);
    }
    /***************    信息系統 End      ******************/

    /**
     * 异步测试数据
     * @param rukuInfo  盘点单表体
     * @param callback  回调
     */
    public void SavePandianTestAsync(rukuinfo rukuInfo, String clienid, JsonCallback<LslResponse<rukuinfo>> callback){
        String url = Consts.API_SERVICE_HOST+"/Test/SavePandianhead";
        HashMap<String,String> postParams = new HashMap<>();
        postParams.put("clientid",clienid);
        postParams.put("userid",rukuInfo.userid);
        postParams.put("kucnum",rukuInfo.kucnum);
        OkGo.post(url).params(postParams).execute(callback);
    }

    /***************    大仓调拨单 start      ******************/
//    /**
//     * 获取门店采购申请单表头
//     * @param userid
//     * @param clientid
//     * @param callback
//     */
//    public void getcommitdcdiaobodanheadAsync(String storeid,String userid,String clientid,String billstate,JsonCallback<LslResponse<List<dacdiaobodan1>>> callback){
//        String url = Consts.API_SERVICE_HOST+"/dacangdiaobodan/Getcommitdcdiaobodanhead?storeid="+storeid+"&billstate="+billstate+"&userid="+userid+"&clientid="+clientid;
//        OkGo.get(url).execute(callback);
//    }
    /**
     * 删除大仓调拨单
     * @param billid    调拨单id
     * @param callback  回调接口
     */
    public void Deletediaobodan1headAsync(String billid,String userid,String clientid,JsonCallback<LslResponse<Object>> callback){
        String url = Consts.API_SERVICE_HOST+"/dacangdiaobodan/DeletedacangdiaobodanHead";
        HashMap<String,String> postParams = new HashMap<>();
        postParams.put("userid",userid);
        postParams.put("billId",billid);
        postParams.put("clientid",clientid);
        OkGo.post(url).params(postParams).execute(callback);
    }
    /**
     * 删除调拨单表体
     * @param billid    销售单id
     * @param billbid    销售单表体id
     * @param callback  回调接口
     */
    public void deletediaobodan1bodyAsync(String billid,String itemNo,String userid,String clientid,JsonCallback<LslResponse<Object>> callback){
        String url = Consts.API_SERVICE_HOST+"/dacangdiaobodan/DeletedacangdiaobodanBody";
        HashMap<String,String> postParams = new HashMap<>();
        postParams.put("userid",userid);
        postParams.put("billId",billid);
        postParams.put("itemNo",itemNo);
        postParams.put("clientid",clientid);
        OkGo.post(url).params(postParams).execute(callback);
    }
    /**
     * 异步插入数据
     * @param dacdiaobodan1  大仓调拨表头
     * @param callback  回调
     */
    public void Savedacdiaobodan1headAsync(dacdiaobodan1 dacdbd, String clientid, JsonCallback<LslResponse<dacdiaobodan1>> callback){
        String url = Consts.API_SERVICE_HOST+"/dacangdiaobodan/SavedacangdiaobodanHead";
        HashMap<String,String> postParams = new HashMap<>();
        postParams.put("clientid",clientid);
        postParams.put("userid",dacdbd.userid);
        postParams.put("billdate",dacdbd.billDate);
        postParams.put("desStoreId",dacdbd.desStoreId);
        postParams.put("srcStoreId",dacdbd.srcStoreId);
        //postParams.put("remarks",dacdbd.remarks);
        OkGo.post(url).tag(this).params(postParams).execute(callback);
    }
    /**
     * 获取大仓调拨单
     * @param userid
     * @param clientid
     * @param callback
     */
    public void getdiaobodan1headAsync(String storeid,String billstate,String userid,String clientid,JsonCallback<LslResponse<List<dacdiaobodan1>>> callback){
        String url = Consts.API_SERVICE_HOST+"/dacangdiaobodan/Getdacangdiaobodanhead?userid="+userid+"&clientid="+clientid+"&billstate="+billstate+"&storeid="+storeid;
        OkGo.get(url).execute(callback);
    }
    /**
     * 获取待审核大仓调拨单
     * @param userid
     * @param clientid
     * @param callback
     */
    public void get101diaobodan1headAsync(String storeid,String billstate,String userid,String clientid,JsonCallback<LslResponse<List<dacdiaobodan1>>> callback){
        String url = Consts.API_SERVICE_HOST+"/dacangdiaobodan/Get101dacangdiaobodanhead?userid="+userid+"&clientid="+clientid+"&billstate="+billstate+"&storeid="+storeid;
        OkGo.get(url).execute(callback);
    }
    /**
     * 获取销售单表体
     * @param userid
     * @param clientid
     * @param callback
     */
    public void getdiaobodan1bodyAsync(String billid,String userid,String clientid,JsonCallback<LslResponse<List<dacdiaobodan1>>> callback){
        String url = Consts.API_SERVICE_HOST+"/dacangdiaobodan/Getdacangdiaobodandetailslist?billid="+billid+"&userid="+userid+"&clientid="+clientid+"";
        OkGo.get(url).execute(callback);
    }
    /**
     * 根据商品条码查询商品信息
     * @param userid
     * @param clientid
     * @param barcode
     * @param callback
     */
    public void getdacdiaobodan1invdocAsync(String userid,String clientid,String barcode,String storeid,JsonCallback<LslResponse<List<dacdiaobodan1>>>  callback){
        String url = Consts.API_SERVICE_HOST+"/dacangdiaobodan/GetdacangdiaobodangoodsBybarCode?userid="+userid+"&clientid="+clientid+"&barcode="+barcode+"&storeid="+storeid;
        OkGo.get(url).execute(callback);
    }

    /**
     * 提交单据
     * @param userid
     * @param clientid
     * @param billid
     * @param callback
     */
    public void getcommitdcdiaoboAsync(String userid,String clientid,String billid,JsonCallback<LslResponse<Object>>  callback){
        String url = Consts.API_SERVICE_HOST+"/dacangdiaobodan/Getcommitdcdiaoborapply?userid="+userid+"&clientid="+clientid+"&billid="+billid;
        OkGo.get(url).execute(callback);
    }

    /**
     * 异步插入数据
     * @param rukuInfo  大仓调拨表体
     * @param callback  回调
     */
    public void Savedacdiaobodan1bodyAsync(dacdiaobodan1 rukuInfo, String clientid, JsonCallback<LslResponse<Object>> callback){
        String url = Consts.API_SERVICE_HOST+"/dacangdiaobodan/Savedacangdiaobodanbody";
        HashMap<String,String> postParams = new HashMap<>();
        postParams.put("clientid",clientid);
        postParams.put("userid",rukuInfo.userid);
        postParams.put("billId",rukuInfo.billId);
        postParams.put("positionId",rukuInfo.positionId);
        postParams.put("batchCode",rukuInfo.batchCode);
        postParams.put("cost",rukuInfo.cost);
        postParams.put("goodsId",rukuInfo.goodsId);
        postParams.put("barcode",rukuInfo.barCode);
        postParams.put("quantity",rukuInfo.quantity);
        postParams.put("remarks",rukuInfo.remarks);
//        postParams.put("price",rukuInfo.price);
        OkGo.post(url).tag(this).params(postParams).execute(callback);
    }

    /**
     * 获取仓库列表
     * @param userid
     * @param clientid
     * @param callback
     */
    public void getcangkulistAsync(String userid,String clientid,JsonCallback<LslResponse<List<baseinfo>>> callback){
        String url = Consts.API_SERVICE_HOST+"/dacangdiaobodan/GetdacangBstoreName?userid="+userid+"&clientid="+clientid;
        OkGo.get(url).execute(callback);
    }

    /**
     * 审核大仓调拨单
     * @param billid    调拨单id
     * @param callback  回调接口
     */
    public void auditdcdiaobodanheadAsync(String billid,String userid,String clientid,JsonCallback<LslResponse<Object>> callback){
        String url = Consts.API_SERVICE_HOST+"/dacangdiaobodan/AuditdcdbHead";
        HashMap<String,String> postParams = new HashMap<>();
        postParams.put("userid",userid);
        postParams.put("billid",billid);
        postParams.put("clientid",clientid);
        OkGo.post(url).params(postParams).execute(callback);

    }
    /**
     * 确认调拨单
     * @param billid    调拨单id
     * @param callback  回调接口
     */
    public void confdcdiaobodanheadAsync(String billid,String userid,String clientid,JsonCallback<LslResponse<Object>> callback) {
        String url = Consts.API_SERVICE_HOST + "/dacangdiaobodan/confdcdiaobodanHead";
        HashMap<String, String> postParams = new HashMap<>();
        postParams.put("userid", userid);
        postParams.put("billid", billid);
        postParams.put("clientid", clientid);
        OkGo.post(url).params(postParams).execute(callback);
    }


        /***************    大仓调拨单 end      ******************/
    /***************    门店调拨单 start      ******************/

    /**
     * 确认调拨单
     * @param billid    调拨单id
     * @param callback  回调接口
     */
    public void confdiaobodanheadAsync(String billid,String userid,String clientid,JsonCallback<LslResponse<Object>> callback){
        String url = Consts.API_SERVICE_HOST+"/Diaobodan/confdiaobodanHead";
        HashMap<String,String> postParams = new HashMap<>();
        postParams.put("userid",userid);
        postParams.put("billid",billid);
        postParams.put("clientid",clientid);
        OkGo.post(url).params(postParams).execute(callback);

    }
    /**
     * 审核调拨单
     * @param billid    调拨单id
     * @param callback  回调接口
     */
    public void auditdiaobodanheadAsync(String billid,String userid,String clientid,JsonCallback<LslResponse<Object>> callback){
        String url = Consts.API_SERVICE_HOST+"/Diaobodan/AuditdiaobodanHead";
        HashMap<String,String> postParams = new HashMap<>();
        postParams.put("userid",userid);
        postParams.put("billid",billid);
        postParams.put("clientid",clientid);
        OkGo.post(url).params(postParams).execute(callback);

    }
    /**
     * 删除调拨单
     * @param billid    调拨单id
     * @param callback  回调接口
     */
    public void deletediaobodanheadAsync(String billid,String userid,String clientid,JsonCallback<LslResponse<Object>> callback){
        String url = Consts.API_SERVICE_HOST+"/Diaobodan/DeletediaobodanHead";
        HashMap<String,String> postParams = new HashMap<>();
        postParams.put("userid",userid);
        postParams.put("billid",billid);
        postParams.put("clientid",clientid);
        OkGo.post(url).params(postParams).execute(callback);

    }
    /**
     * 删除调拨单表体
     * @param billid    调拨单id
     * @param itemNo    明细id
     * @param callback  回调接口
     */
    public void deletediaobodanbodyAsync(String billid,String itemNo,String userid,String clientid,JsonCallback<LslResponse<Object>> callback){
        String url = Consts.API_SERVICE_HOST+"/Diaobodan/DeletediaobodanBody";
        HashMap<String,String> postParams = new HashMap<>();
        postParams.put("userid",userid);
        postParams.put("billid",billid);
        postParams.put("itemNo",itemNo);
        postParams.put("clientid",clientid);
        OkGo.post(url).params(postParams).execute(callback);
    }
    /**
     * 主表保存
     * @param dacdbd  门店调拨表头
     * @param callback  回调
     */
    public void SavedacdiaobodanheadAsync(dacdiaobodan dacdbd, String clientid, JsonCallback<LslResponse<dacdiaobodan>> callback){

        String url = Consts.API_SERVICE_HOST+"/Diaobodan/SavediaobodanHead";
        HashMap<String,String> postParams = new HashMap<>();
        postParams.put("billDate",dacdbd.billDate);
        postParams.put("desStoreId",dacdbd.desStoreId);
        postParams.put("userid",dacdbd.userid);
        postParams.put("srcStoreId",dacdbd.srcStoreId);
        postParams.put("clientid",clientid);
        postParams.put("touserid",dacdbd.touserid);
        //postParams.put("remarks",dacdbd.remarks);
        OkGo.post(url).tag(this).params(postParams).execute(callback);
    }
    /**
     * 明细表保存
     * @param dacdbd  门店调拨表头
     * @param callback  回调
     */
    public void SavedacdiaobodanbodyAsync(dacdiaobodan dacdbd,String clientid, JsonCallback<LslResponse<Object>> callback){
        String url = Consts.API_SERVICE_HOST+"/Diaobodan/Savediaobodanbody";
        HashMap<String,String> postParams = new HashMap<>();

        postParams.put("clientid",clientid);
        postParams.put("userid",dacdbd.userid);
        postParams.put("billId",dacdbd.billId);
        postParams.put("positionId",dacdbd.positionId);
        postParams.put("batchCode",dacdbd.batchCode);
        postParams.put("cost",dacdbd.cost);
        postParams.put("goodsId",dacdbd.goodsId);
        postParams.put("barCode",dacdbd.barCode);
        postParams.put("goodsCode",dacdbd.goodsCode);
        postParams.put("quantity",dacdbd.quantity);
//        postParams.put("price",rukuInfo.price);
        postParams.put("desStoreId",dacdbd.desStoreId);



        OkGo.post(url).tag(this).params(postParams).execute(callback);
    }
    /**
     * 获取门店调拨单表头
     * @param userid
     * @param clientid
     * @param callback
     */
    public void getdiaobodanheadAsync(String storeid,String userid,String clientid,String billstate,JsonCallback<LslResponse<List<dacdiaobodan>>> callback){
        String url = Consts.API_SERVICE_HOST+"/Diaobodan/Getdiaobodanhead?storeid="+storeid+"&billstate="+billstate+"&userid="+userid+"&clientid="+clientid;
        OkGo.get(url).execute(callback);
    }
    /**
     * 获取门店调拨单表头
     * @param userid
     * @param clientid
     * @param callback
     */
    public void getcommitdiaobodanheadAsync(String storeid,String userid,String clientid,String billid,JsonCallback<LslResponse<List<dacdiaobodan>>> callback){
        String url = Consts.API_SERVICE_HOST+"/Diaobodan/Getcommitdiaobodanhead?storeid="+storeid+"&billid="+billid+"&userid="+userid+"&clientid="+clientid;
        OkGo.get(url).execute(callback);
    }

    /**
     * 获取门店调拨单表体
     * @param userid
     * @param billId
     * @param callback
     */
    public void getdiaobodandetailslistAsync(String userid,String billId,String clientid,JsonCallback<LslResponse<List<dacdiaobodan>>> callback){
        String url = Consts.API_SERVICE_HOST+"/Diaobodan/Getdiaobodandetailslist?userid="+userid+"&clientid="+clientid+"&billId="+billId;
        OkGo.get(url).execute(callback);
    }
    public void getdiaobodandetailslistAsync(String userid,String billId,String barCode,String clientid,JsonCallback<LslResponse<List<dacdiaobodan>>> callback){
        String url = Consts.API_SERVICE_HOST+"/Diaobodan/getdiaobodandetailslist?billId="+billId+"&barcode"+barCode;
        OkGo.get(url).execute(callback);
    }

    /**
     * 采购经理获取待审调拨单
     * @param userid
     * @param billstate
     * @param callback
     */
    public void getdiaobodanAuditListAsync(String userid,String billstate,String clientid,JsonCallback<LslResponse<List<dacdiaobodan>>> callback){
        String url = Consts.API_SERVICE_HOST+"/Diaobodan/GetdiaobodanAuditList?&userid="+userid+"&billId="+billstate+"&clientid="+clientid;
        OkGo.get(url).execute(callback);
    }

    /**
     * 获取大仓仓库
     * @param userid
     * @param callback
    public void getcangkulistAsync(String userid,String clientid,JsonCallback<LslResponse<List<rukuinfo>>> callback){
    String url = Consts.API_SERVICE_HOST+"/Diaobodan/Getcangkulist?userid="+userid+"&clientid="+clientid;
    OkGo.get(url).execute(callback);
    }*/

    /**
     * 获取门店采购经理角色人员信息
     * @param storeid
     * @param callback
     */
    public void getBusinesStaffNameAsync(String userid,String storeid,String clientid,JsonCallback<LslResponse<List<dacdiaobodan>>> callback){
        String url = Consts.API_SERVICE_HOST+"/Diaobodan/GetmendianBusinesStaff?userid="+userid+"&storeid="+storeid+"&clientid="+clientid;
        OkGo.get(url).execute(callback);
    }
    /**
     * 根据商品条码查询商品信息
     * @param userid
     * @param clientid
     * @param barcode
     * @param callback
     */
    public void getdiaobodangoodsAsync(String userid, String clientid, String barcode, String storeid,JsonCallback<LslResponse<List<dacdiaobodan>>>  callback){
        String url = Consts.API_SERVICE_HOST+"/Diaobodan/GetdiaobodangoodsBybarCode?userid="+userid+"&clientid="+clientid+"&barcode="+barcode+"&storeid="+storeid;
        OkGo.get(url).execute(callback);
    }

    /**
     * 输入：条码，仓库Id
     * 返回：商品库存信息
     * */
    public void getdiaobodankuclistAsync(String userid,String storeid,String barcode,String clientid,JsonCallback<LslResponse<List<dacdiaobodan>>> callback){
        String url = Consts.API_SERVICE_HOST+"/Diaobodan/getdiaobodankuclist?userid="+userid+"&clientid="+clientid+"&barcode="+barcode;
        OkGo.get(url).execute(callback);
    }

    /***************    门店调拨单 end      ******************/


    /***************    门店采购申请单 start      ******************/
    /**
     * 获取采购申请单
     * @param uid
     * @param clientid
     * @param callback
     */
    public void getcgorderapplyheadAsync(String userid,String clientid,String billstate,JsonCallback<LslResponse<List<dacdiaobodan1>>> callback){
        String url = Consts.API_SERVICE_HOST+"/cgorderapply/Getcgorderapplyhead?userid="+userid+"&clientid="+clientid+"&billstate="+billstate;
        OkGo.get(url).execute(callback);
    }
    /**
     * 获取采购申请单表体
     * @param uid
     * @param clientid
     * @param callback
     */
    public void getcgorderapplybodyAsync(String billid,String userid,String clientid,JsonCallback<LslResponse<List<dacdiaobodan1>>> callback){
        String url = Consts.API_SERVICE_HOST+"/cgorderapply/Getcgorderapplybody?billid="+billid+"&userid="+userid+"&clientid="+clientid+"";
        OkGo.get(url).execute(callback);
    }

    /**
     * 采购申请单商品资料
     * @param userid
     * @param clientid
     * @param barcode
     * @param callback
     */
    public void getcgapplyInvdocstorelistAsync(String userid,String clientid,String barcode,JsonCallback<LslResponse<List<dacdiaobodan1>>>  callback){
        String url = Consts.API_SERVICE_HOST+"/cgorderapply/Getinvdocstorelist?userid="+userid+"&clientid="+clientid+"&barcode="+barcode;
        OkGo.get(url).execute(callback);
    }
    /**
     * 异步插入数据
     * @param rukuInfo  采购申请单头
     * @param callback  回调
     */
    public void SavecgorderapplyheadAsync(dacdiaobodan1 rukuInfo, String clientid, JsonCallback<LslResponse<dacdiaobodan1>> callback){
        String url = Consts.API_SERVICE_HOST+"/cgorderapply/Savecgorderapplyhead";
        HashMap<String,String> postParams = new HashMap<>();
        postParams.put("clientid",clientid);
        postParams.put("userid",rukuInfo.userid);
        postParams.put("billDate",rukuInfo.billDate);
        postParams.put("desStoreId",rukuInfo.desStoreId);
        postParams.put("remarks",rukuInfo.remarks);
        postParams.put("touserId",rukuInfo.touserid);
        OkGo.post(url).params(postParams).execute(callback);
    }
    /**
     * 异步插入数据
     * @param rukuInfo  采购申请单表体
     * @param callback  回调
     */
    public void SavecgorderapplybodyAsync(dacdiaobodan1 rukuInfo, String clientid, JsonCallback<LslResponse<Object>> callback){
        String url = Consts.API_SERVICE_HOST+"/cgorderapply/Savecgorderapplybody";
        HashMap<String,String> postParams = new HashMap<>();
        postParams.put("clientid",clientid);
        postParams.put("userid",rukuInfo.userid);
        postParams.put("billId",rukuInfo.billId);
        postParams.put("goodsId",rukuInfo.goodsId);
        postParams.put("barCode",rukuInfo.barCode);
        postParams.put("quantity",rukuInfo.quantity);
        postParams.put("cost",rukuInfo.cost);
        postParams.put("goodsCode",rukuInfo.goodsCode);
        postParams.put("goodsName",rukuInfo.goodsName);
        postParams.put("goodsScope",rukuInfo.goodsScope);
        postParams.put("remarks",rukuInfo.remarks);
        OkGo.post(url).params(postParams).execute(callback);
    }

    /**
     * 商品采购申请条码
     * @param userid
     * @param clientid
     * @param barcode
     * @param callback
     */
    public void GetcgorderapplybarcodelistAsync(String userid,String clientid,String barcode,JsonCallback<LslResponse<List<baseinfo>>>  callback){
        String url = Consts.API_SERVICE_HOST+"/cgorderapply/Getcgorderapplybarcodelist?userid="+userid+"&clientid="+clientid+"&barcode="+barcode;
        OkGo.get(url).execute(callback);
    }
    /**
     * 提交采购申请单
     * @param userid
     * @param clientid
     * @param billid
     * @param callback
     */
    public void getcommitcgapplyAsync(String userid,String clientid,String billid,JsonCallback<LslResponse<Object>>  callback){
        String url = Consts.API_SERVICE_HOST+"/cgorderapply/Getcommitcgorderapply?userid="+userid+"&clientid="+clientid+"&billid="+billid;
        OkGo.get(url).execute(callback);
    }
    /**
     * 删除采购申请单
     * @param billid    采购申请单id
     * @param callback  回调接口
     */
    public void deletecgorderapplyheadAsync(String billid,String userid,String clientid,JsonCallback<LslResponse<Object>> callback){
        String url = Consts.API_SERVICE_HOST+"/cgorderapply/Deletecgorderapplyhead";
        HashMap<String,String> postParams = new HashMap<>();
        postParams.put("userid",userid);
        postParams.put("billid",billid);
        postParams.put("clientid",clientid);
        OkGo.post(url).params(postParams).execute(callback);
    }
    /**
     * 删除采购申请单表体
     * @param billid    采购申请单id
     * @param callback  回调接口
     */
    public void deletecgorderapplybodyAsync(String billid,String billbid,String userid,String clientid,JsonCallback<LslResponse<Object>> callback){
        String url = Consts.API_SERVICE_HOST+"/cgorderapply/Deletecgorderapplybody";
        HashMap<String,String> postParams = new HashMap<>();
        postParams.put("userid",userid);
        postParams.put("billId",billid);
        postParams.put("itemNo",billbid);
        postParams.put("clientid",clientid);
        OkGo.post(url).params(postParams).execute(callback);
    }

    /**
     * 获取门店采购申请单表头
     * @param userid
     * @param clientid
     * @param callback
     */
    public void getcommitcgapplyheadAsync(String storeid,String userid,String clientid,String billstate,JsonCallback<LslResponse<List<dacdiaobodan1>>> callback){
        String url = Consts.API_SERVICE_HOST+"/cgorderapply/Getcommitcgapplyhead?storeid="+storeid+"&billstate="+billstate+"&userid="+userid+"&clientid="+clientid;
        OkGo.get(url).execute(callback);
    }

    /**
     * 审核采购申请单
     * @param billid    调拨单id
     * @param callback  回调接口
     */
    public void auditcgapplyheadAsync(String billid,String userid,String clientid,JsonCallback<LslResponse<Object>> callback){
        String url = Consts.API_SERVICE_HOST+"/cgorderapply/AuditcgapplyHead";
        HashMap<String,String> postParams = new HashMap<>();
        postParams.put("userid",userid);
        postParams.put("billid",billid);
        postParams.put("clientid",clientid);
        OkGo.post(url).params(postParams).execute(callback);

    }
    /**
     * 确认采购申请单收货
     * @param billid    调拨单id
     * @param callback  回调接口
     */
    public void confcgapplyheadAsync(String billid,String userid,String clientid,JsonCallback<LslResponse<Object>> callback){
        String url = Consts.API_SERVICE_HOST+"/cgorderapply/confcgapplyHead";
        HashMap<String,String> postParams = new HashMap<>();
        postParams.put("userid",userid);
        postParams.put("billid",billid);
        postParams.put("clientid",clientid);
        OkGo.post(url).params(postParams).execute(callback);

    }
    /***************    门店采购申请单 end      ******************/
    /**
     * 保存销售单
     * @param userid
     * @param clientid
     * @param billid
     * @param callback
     */
    public void getSaveSaleAsync(String userid,String clientid,String billid,JsonCallback<LslResponse<Object>>  callback){
        String url = Consts.API_SERVICE_HOST+"/Sale/GetSaveSale?userid="+userid+"&clientid="+clientid+"&billid="+billid;
        OkGo.get(url).execute(callback);
    }

    /**
     * 获取商品销售单表头
     * @param userid
     * @param clientid
     * @param callback
     */
    public void getcommitsaleapplyheadAsync(String storeid,String userid,String clientid,String billstate,JsonCallback<LslResponse<List<dacdiaobodan1>>> callback){
        String url = Consts.API_SERVICE_HOST+"/Sale/Getcommitsaleapplyhead?storeid="+storeid+"&billstate="+billstate+"&userid="+userid+"&clientid="+clientid;
        OkGo.get(url).execute(callback);
    }

    /**
     * 审核销售单
     * @param billid    调拨单id
     * @param callback  回调接口
     */
    public void auditSaleapplyheadAsync(String billid,String userid,String clientid,JsonCallback<LslResponse<Object>> callback){
        String url = Consts.API_SERVICE_HOST+"/sale/AuditsaleapplyHead";
        HashMap<String,String> postParams = new HashMap<>();
        postParams.put("userid",userid);
        postParams.put("billid",billid);
        postParams.put("clientid",clientid);
        OkGo.post(url).params(postParams).execute(callback);

    }

//添加請求頭
    private void OkgoAddHeader(String token) {
        HttpHeaders head=new HttpHeaders();
        LinkedHashMap<String, String> map=new LinkedHashMap<String, String>();
        map.put("Authorization",token);
        head.headersMap=map;
        OkGo.getInstance().addCommonHeaders(head);
    }

}


