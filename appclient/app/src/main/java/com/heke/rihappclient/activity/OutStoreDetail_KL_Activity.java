package com.heke.rihappclient.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.heke.rihappclient.R;
import com.heke.rihappclient.WebService.AppService;
import com.heke.rihappclient.adapter.OutStoreList_zt_Adapter;
import com.heke.rihappclient.application.BaseActivity;
import com.heke.rihappclient.helper.DBHelper.SQLiteHelper;
import com.heke.rihappclient.model.JL_APP_OutStoreDetail;
import com.heke.rihappclient.model.KL_outstore_model_detail;
import com.heke.rihappclient.model.dacdiaobodan1;
import com.heke.rihappclient.model.rukuinfo;
import com.heke.rihappclient.shareprefence.ShareprefenceBean;
import com.heke.rihappclient.shareprefence.ShareprefenceBillMessage;
import com.heke.rihappclient.utils.UIUtil;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Semaphore;

public class OutStoreDetail_KL_Activity extends BaseActivity implements View.OnClickListener{

    private SQLiteHelper db;
    private KL_outstore_model_detail billdetailmodel;
    private  EditText tv_jsorderno;
    private  EditText tv_goodsname;
    private  EditText tv_jsStandard;
    private  EditText tv_custjsStandard;
    private  EditText tv_guige;
    private  EditText tv_shildnum;
    private  EditText tv_panname;
    private  EditText tv_panhao;
    private  EditText tv_batchno;
    private  EditText tv_storep;
    private  EditText tv_realnum;
    private String billid,billcode,jsorderno,goodsname,billItemId,jsStandard,custjsStandard,batchno,outStockQty;
    private  int  type=1;
    private double d1,d2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_outstore_details_kl);
        db=new SQLiteHelper(this,"jkapp1.db",null,1);
        this.mSharedPreferences = getSharedPreferences(ShareprefenceBean.SHAREPREFENCE_NAME, 0);
        this.currentuser = AppService.getInstance().getCurrentUser();
        clientid=AppService.getInstance().getDeviceId(this);
        Intent localIntent = getIntent();
        //反序列化数据对象
        Serializable se = localIntent.getSerializableExtra("model");
        if(se instanceof KL_outstore_model_detail){
        //获取到携带数据的DataBean对象db
            billdetailmodel = (KL_outstore_model_detail) se;
        }
        this.billcode = billdetailmodel.billcode;
        this.billid = billdetailmodel.billid;
        this.jsorderno = billdetailmodel.jsorderno;
        this.goodsname = billdetailmodel.goodsname;
        this.billItemId = billdetailmodel.billItemId;
        this.jsStandard = billdetailmodel.jsStandard;
        this.custjsStandard = billdetailmodel.custjsStandard;
        this.batchno = billdetailmodel.batchno;
        this.outStockQty = billdetailmodel.outStockQty;
        d1=0;d2=0;
        if(billdetailmodel.outStockQty!=null&&!billdetailmodel.outStockQty.equals("")){
            d1=new Double(billdetailmodel.outStockQty);
        }
        if(billdetailmodel.caijiQty!=null&&!billdetailmodel.caijiQty.equals("")){
            d2=new Double(billdetailmodel.caijiQty);
        }
        this.initView();
        this.tv_jsorderno.setText(this.jsorderno);
        this.tv_goodsname.setText(this.goodsname);
        this.tv_jsStandard.setText(this.jsStandard);
        this.tv_custjsStandard.setText(this.custjsStandard);
        this.tv_guige.setText(this.batchno);
        this.tv_shildnum.setText(String.valueOf(d1-d2));
        this.init();
    }
    public void init() {
        this.invcodemaplist = new ArrayList<Map<String, Object>>();
        this.barcodelist = new ArrayList<String>();
        billlist = new ArrayList<String>();
    }
    public void initView() {
        this.tv_jsorderno=(EditText)findViewById(R.id.tv_jsorderno);
        this.tv_goodsname=(EditText)findViewById(R.id.tv_goodsname);
        this.tv_jsStandard=(EditText)findViewById(R.id.tv_jsStandard);
        this.tv_custjsStandard=(EditText)findViewById(R.id.tv_custjsStandard);
        this.tv_guige=(EditText)findViewById(R.id.tv_guige);
        this.tv_shildnum=(EditText)findViewById(R.id.tv_shildnum);
        this.tv_panname=(EditText)findViewById(R.id.tv_panname);
        this.tv_panhao=(EditText)findViewById(R.id.tv_panhao);
        this.tv_batchno=(EditText)findViewById(R.id.tv_batchno);
        this.tv_storep=(EditText)findViewById(R.id.tv_storep);
        this.tv_realnum=(EditText)findViewById(R.id.tv_realnum);


        //公共
        this.backImgBtn = ((ImageButton) findViewById(R.id.back));
        this.backImgBtn.setOnClickListener(this);
        this.commitBtn = ((Button) findViewById(R.id.commitBtn));
        this.cancelBtn = ((Button) findViewById(R.id.cancelBtn));
        this.titiltxt=(TextView)findViewById(R.id.titletxt);
        this.titiltxt.setText("条码采集");
        this.cancelBtn.setText("扫码");
        this.commitBtn.setOnClickListener(this);
        this.cancelBtn.setOnClickListener(this);
        this.scanImgBtn=(ImageView)findViewById(R.id.scanImgBtn);
        this.scanImgBtn.setOnClickListener(this);
        scanImgBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent it = new Intent(OutStoreDetail_KL_Activity.this, CaptureActivity.class);
                startActivityForResult(it, SCANNIN_GREQUEST_CODE);
            }
        });
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == SCANNIN_GREQUEST_CODE) {
            if (resultCode == Activity.RESULT_OK && data != null) {
                String result = data.getStringExtra("scan_result");
                Log.i("", "scan result:" + result);
                if (!result.equals("")) {
                    loaddata(result);
                } else {
                    //this.setTextNull();
                    UIUtil.showToast("提示：没有该标签信息");
                }

            } else if (resultCode == Activity.RESULT_CANCELED) {
                //this.setTextNull();
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
    public void onClick(View paramView) {
        switch (paramView.getId()) {
            case R.id.back:
                Intent intent = new Intent();
                intent.putExtra("result", "OK");
                setResult(1001, intent);
                this.finish();
                break;
            case R.id.invcodelayout:
                break;
            case R.id.commitBtn:
                if (TextUtils.isEmpty(this.billItemId)) {
                    UIUtil.showToast("提示：没有任务");
                    return;
                }
                new AlertDialog.Builder(this)
                        .setMessage("是否提交？")
                        .setPositiveButton("确定",
                                new DialogInterface.OnClickListener() {

                                    @Override
                                    public void onClick(DialogInterface dialog,
                                                        int which) {
                                        dialog.dismiss();
                                        commitdata();

                                    }
                                })
                        .setNegativeButton("取消",
                                new DialogInterface.OnClickListener() {

                                    @Override
                                    public void onClick(DialogInterface dialog,
                                                        int which) {
                                        //finish();
                                        dialog.dismiss();
                                    }
                                }).setCancelable(false).show();
                break;
            case R.id.cancelBtn:
                Intent it = new Intent(OutStoreDetail_KL_Activity.this, CaptureActivity.class);
                startActivityForResult(it, SCANNIN_GREQUEST_CODE);
                break;
        }
    }

    public void loaddata(String barcode) {
        if(billdetailmodel.batchcode.equals(barcode.trim())){
            this.tv_panhao.setText(billdetailmodel.dishno);
            this.tv_panname.setText(billdetailmodel.dishname);
            this.tv_batchno.setText(billdetailmodel.batchcode);
            this.tv_storep.setText(billdetailmodel.storeP);
            this.tv_realnum.setText(String.valueOf(d1-d2));
        }else{
            Toast.makeText(this,"此到货批次商品不正确！",Toast.LENGTH_LONG).show();
        }

    }

    private void clearScanText() {
        this.tv_panhao.setText("");
        this.tv_panname.setText("");
        this.tv_batchno.setText("");
        this.tv_storep.setText("");
        this.tv_realnum.setText("0");
    }

    void setinvcode(List<rukuinfo> list){
        invcodemaplist.clear();
        if(list.size()>0) {
            for (int i = 0; i < list.size(); i++) {
                HashMap<String, Object> map = new HashMap<String, Object>();
                rukuinfo rinfo = list.get(i);
                String invname = rinfo.invname + "/" + rinfo.invspec+ "/" + rinfo.kuweiname+ "/" + rinfo.remarks;
                map.put("invname", invname);
                map.put("rinfo", rinfo);
                invcodemaplist.add(map);
            }
            if(list.size()==1){
                rukuinfo binfo = list.get(0);
                if (binfo != null) {
                    //invid = binfo.invid;
                }
            }
            else{
            }

        }
    }


    //提交
    public void commitdata() {
        String batchCode=this.tv_batchno.getText().toString();
        String realnum=this.tv_realnum.getText().toString();
        double rnm= new Double(realnum);
        if(batchCode.equals("")||batchCode==null){
            Toast.makeText(this,"请扫盘号！",Toast.LENGTH_SHORT).show();
            return;
        }
        if(realnum.equals("")||batchCode==null||rnm<=0){
            Toast.makeText(this,"实际数量不能小于等于0！",Toast.LENGTH_SHORT).show();
            return;
        }
        String shuidnum=this.tv_shildnum.getText().toString();
        double snm=new Double(shuidnum);
        if(snm<rnm){
            Toast.makeText(this,"应发数量不能小于实际数量！",Toast.LENGTH_SHORT).show();
            return;
        }
        String panhao=this.tv_panhao.getText().toString();
        String panjuID=billdetailmodel.dishid==null?"0":billdetailmodel.dishid;
        String userid=this.currentuser.userid;
        String storekuwei=billdetailmodel.storeP;
        String storeid=billdetailmodel.storeID;
        String storeAreaid=billdetailmodel.storeAreaID;
        JL_APP_OutStoreDetail outmodel=new JL_APP_OutStoreDetail();
        outmodel.setBillid(String.valueOf(billid));
        outmodel.setBillItemId(String.valueOf(billItemId));
        outmodel.setBilltype("2");
        outmodel.setBatchcode(batchCode);
        outmodel.setOutStockQty(realnum);
        outmodel.setDishno(panhao);
        outmodel.setDishid(panjuID);
        outmodel.setGoodsid(userid);
        outmodel.setStoreID(storeid);
        outmodel.setStoreAreaID(storeAreaid);
        db.addOutStore(outmodel);
        Intent intent = new Intent();
        intent.putExtra("result", "OK");
        setResult(121, intent);
        this.finish();
        Toast.makeText(this, "提交成功", Toast.LENGTH_SHORT).show();
    }
    private ImageButton backImgBtn;
    private List<Map<String, Object>> invcodemaplist;
    private volatile Semaphore mSemaphore = new Semaphore(1);
    private AlertDialog dialog;
    private final static int SCANNIN_GREQUEST_CODE = 101;
    public ArrayAdapter<String> aAdapter;
    public List<String> barcodelist;
    public List<String> billlist;


    //公共
    private Button commitBtn;
    private Button cancelBtn;
    private TextView titiltxt;
    private ImageView scanImgBtn;
}


