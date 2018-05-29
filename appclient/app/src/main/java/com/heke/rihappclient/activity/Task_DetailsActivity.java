package com.heke.rihappclient.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.heke.rihappclient.R;
import com.heke.rihappclient.WebService.AppService;
import com.heke.rihappclient.application.BaseActivity;
import com.heke.rihappclient.model.baseinfo;
import com.heke.rihappclient.model.rukuinfo;
import com.heke.rihappclient.net.okgo.JsonCallback;
import com.heke.rihappclient.net.okgo.LslResponse;
import com.heke.rihappclient.shareprefence.ShareprefenceBean;
import com.heke.rihappclient.utils.UIUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Semaphore;

import okhttp3.Call;
import okhttp3.Response;

public class Task_DetailsActivity extends BaseActivity implements View.OnClickListener{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task_details);
        this.mSharedPreferences = getSharedPreferences(ShareprefenceBean.SHAREPREFENCE_NAME, 0);
        this.currentuser = AppService.getInstance().getCurrentUser();
        clientid=AppService.getInstance().getDeviceId(this);
        this.initView();
        this.init();
    }
    public void init() {
        Intent localIntent = getIntent();
        this.billid = localIntent.getStringExtra("billid");
        this.billcode = localIntent.getStringExtra("billcode");
        this.goodsScope=localIntent.getStringExtra("goodsScope");
        this.scjhdhEditT.setText("2000000688");
        this.rwqdhEditT.setText(billcode);
        this.invcodemaplist = new ArrayList<Map<String, Object>>();
        this.barcodelist = new ArrayList<String>();
    }
    public void initView() {
        this.backImgBtn = ((ImageButton) findViewById(R.id.back));
        this.scjhdhEditT=(EditText)findViewById(R.id.billcode);
        this.rwqdhEditT=(EditText)findViewById(R.id.storename);
        this.backImgBtn.setOnClickListener(this);

        //公共
        this.commitBtn = ((Button) findViewById(R.id.commitBtn));
        this.cancelBtn = ((Button) findViewById(R.id.cancelBtn));
        this.titiltxt=(TextView)findViewById(R.id.titletxt);
        this.titiltxt.setText("生产任务明细");
        this.commitBtn.setOnClickListener(this);
        this.cancelBtn.setOnClickListener(this);
        this.scanImgBtn=(ImageView)findViewById(R.id.scanImgBtn);
        this.scanImgBtn.setOnClickListener(this);
        scanImgBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent it = new Intent(Task_DetailsActivity.this, CaptureActivity.class);
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
                    View rootview = Task_DetailsActivity.this.getWindow().getDecorView();
                    View et = rootview.findFocus();
                    if(et instanceof EditText){
                        ((EditText)et).setText(result);
                    }

                    //barcodeTxtView.setText(result);
                    //loaddata(result);
                } else {
                    //this.setTextNull();
                    UIUtil.showToast("提示：没有该标签入库信息");
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
                this.finish();
                break;
            case R.id.cancelBtn:
                this.finish();
                break;
            case R.id.commitBtn:
                if (TextUtils.isEmpty(this.billcode)) {
                    UIUtil.showToast("提示：没有任务");
                    return;
                }
                new AlertDialog.Builder(this)
                        .setMessage("是否开工？")
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
        }
    }

    public void loaddata(String barcode) {
        this.invcodelist=new ArrayList<>();
        this.invcodelist.clear();
        showLoading(this, "正在加载数据...");
        AppService.getInstance().getcgInvdocstorelistAsync(this.currentuser.userid, clientid, barcode, new JsonCallback<LslResponse<List<rukuinfo>>>() {
            @Override
            public void onSuccess(LslResponse<List<rukuinfo>> infoLslResponse, Call call, Response response) {
                stopLoading();
                if (infoLslResponse.code == LslResponse.RESPONSE_ERROR) {
                    UIUtil.showToast(infoLslResponse.msg);
                } else if (infoLslResponse.code == LslResponse.RESPONSE_OK) {
                    invcodelist.addAll(infoLslResponse.data);
                    setinvcode(invcodelist);
                } else {
                    UIUtil.showToast(infoLslResponse.msg);
                }

            }
        });
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
                    invid = binfo.invid;
                    positionId=binfo.kuweiid;
                    positionNameTxtView.setText(binfo.kuweiname);
                    batchCodeTxtView.setText(binfo.remarks);
                    invcodeTxtView.setText(binfo.invcode);
                    invnameTxtView.setText(binfo.invname);
                    invspecTxtView.setText(binfo.invspec);
                    kucnumTxtView.setText(binfo.kucnum==null?"0":binfo.kucnum);
                    numberExt.setText(binfo.kucnum==null?"0":binfo.kucnum);
                    priceExt.setText(binfo.cost);
                }
            }
            else{
            }

        }
    }

    void setTextNull() {
        invid = "";
        barcodeTxtView.setText("");
        invcodeTxtView.setText("");
        invnameTxtView.setText("");
        invspecTxtView.setText("");
        positionNameTxtView.setText("");
        batchCodeTxtView.setText("");
        kucnumTxtView.setText("0");
        numberExt.setText("0");
        priceExt.setText("0");
    }

    //提交验货单
    public void commitdata() {
        if(goodsScope.equals("3000")){
            Intent newInt=new Intent(this,RollActivity.class);
            startActivity(newInt);
        }else if(goodsScope.equals("3063")){
            Intent newInt=new Intent(this,SabotActivity.class);
            startActivity(newInt);
            this.finish();
        }
    }
    private ImageButton backImgBtn;
    private ImageButton startTaskImgBtn;
    private EditText scjhdhEditT;private EditText rwqdhEditT;
    private TextView invcodeTxtView;
    private TextView invnameTxtView;
    private TextView invspecTxtView;
    private TextView kucnumTxtView;
    private TextView batchCodeTxtView;
    private TextView positionNameTxtView;
    private String positionId;
    private AutoCompleteTextView barcodeTxtView;
    private EditText numberExt;
    private EditText priceExt;
    private ImageButton jiahao;
    private ImageButton jianhao;
    private EditText storenameExt;
    private LinearLayout invcodelayout;
    private ProgressDialog pro_Dialog;
    private List<rukuinfo> invcodelist;
    private List<Map<String, Object>> invcodemaplist;
    private volatile Semaphore mSemaphore = new Semaphore(1);
    private AlertDialog dialog;
    private final static int SCANNIN_GREQUEST_CODE = 101;
    private static String billcode = "", billid = "", invid="",goodsScope="";
    public ArrayAdapter<String> aAdapter;
    public List<String> barcodelist;
    //private ImageView tipImage;
    private Button mTitleCommit;

    //公共
    private Button commitBtn;
    private Button cancelBtn;
    private TextView titiltxt;
    private ImageView scanImgBtn;

    class getJson extends AsyncTask<String,String,String> {

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
        }

        @Override
        protected void onProgressUpdate(String... values) {
            aAdapter = new ArrayAdapter<String>(getApplicationContext(),R.layout.barcodeitem,barcodelist);
            barcodeTxtView.setAdapter(aAdapter);
            aAdapter.notifyDataSetChanged();
        }

        @Override
        protected String doInBackground(String... key) {
            String newText = key[0];
            try{
                AppService.getInstance().GetcgorderbarcodelistAsync(currentuser.userid, clientid, newText, new JsonCallback<LslResponse<List<baseinfo>>>() {
                    @Override
                    public void onSuccess(LslResponse<List<baseinfo>> infoLslResponse, Call call, Response response) {

                        if (infoLslResponse.code == LslResponse.RESPONSE_ERROR) {
                            UIUtil.showToast(infoLslResponse.msg);
                        } else if (infoLslResponse.code == LslResponse.RESPONSE_OK) {
                            barcodelist=new ArrayList<String>();
                            if(infoLslResponse.data!=null){
                                for(int i=0;i<infoLslResponse.data.size();i++){
                                    barcodelist.add(infoLslResponse.data.get(i).barcode);
                                }
                                publishProgress();
                            }
                        } else {
                            UIUtil.showToast(infoLslResponse.msg);
                        }
                    }
                });


            }catch(Exception e){
                Log.w("Error", e.getMessage());
            }
            return null;
        }

    }
}
