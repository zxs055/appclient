package com.heke.rihappclient.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.heke.rihappclient.R;
import com.heke.rihappclient.WebService.AppService;
import com.heke.rihappclient.adapter.KLOutStoreListAdapter;
import com.heke.rihappclient.application.BaseActivity;
import com.heke.rihappclient.model.KL_outstore_model;
import com.heke.rihappclient.net.okgo.JsonCallback;
import com.heke.rihappclient.net.okgo.LslResponse;
import com.heke.rihappclient.shareprefence.ShareprefenceBean;
import com.heke.rihappclient.utils.UIUtil;
import com.lzy.okgo.exception.OkGoException;

import java.net.ConnectException;
import java.net.SocketTimeoutException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Response;

public class PlantOutStoreListActivity extends BaseActivity implements View.OnClickListener {

    private ImageView scanImgBtn;
    private Button commitBtn;
    private Button cancelBtn;
    private EditText selectEtxt;
    private String token;
    private String name;
    private String JEmpName;
    private String prodectcenter;
    private String banci;
    private String prodectcentername;
    private String banciname;
    private String processid;
    private String processname;
    private TextView tv_billcode;
    private TextView tv_banci;
    private TextView tv_prodectcenter;
    private TextView tv_shebei;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_plant_outstore_list);
        this.mSharedPreferences = getSharedPreferences(ShareprefenceBean.SHAREPREFENCE_NAME, 0);
        this.token=mSharedPreferences.getString(ShareprefenceBean.TOKEN,"");
        this.currentuser = AppService.getInstance().getCurrentUser();
        clientid = AppService.getInstance().getDeviceId(this);
        this.initView();
        this.init();
    }

    public void init() {
        Intent localIntent=getIntent();
        this.banci=localIntent.getStringExtra("banci");
        this.banciname=localIntent.getStringExtra("banciname");
        this.prodectcentername=localIntent.getStringExtra("prodectcentername");
        this.prodectcenter=localIntent.getStringExtra("prodectcenter");
        this.processname=localIntent.getStringExtra("processname");
        this.processid=localIntent.getStringExtra("processid");
        this.tv_billcode.setText(this.currentuser.username);
        this.tv_banci.setText(this.banciname);
        this.tv_prodectcenter.setText(this.prodectcentername);
        this.tv_shebei.setText(this.processname);
        billlist=new ArrayList<KL_outstore_model>();
        fileterList=new ArrayList<KL_outstore_model>();
        loadbilllist();
    }

    private void loadbilllist() {
        billlist.clear();
        showLoading(this, "正在加载数据...");
        AppService.getInstance().getplantOutStoreListAsync( banci,processid,token,clientid, new JsonCallback<LslResponse<List<KL_outstore_model>>>() {
            @Override
            public void onSuccess(LslResponse<List<KL_outstore_model>> listLslResponse, Call call, Response response) {
                stopLoading();
                if (listLslResponse.code == LslResponse.RESPONSE_ERROR) {
                    UIUtil.showToast(listLslResponse.msg);
                } else if (listLslResponse.code == LslResponse.RESPONSE_OK) {
                    billlist=listLslResponse.data;
                    if(listLslResponse.data!=null) {
                        billListAdapter=new KLOutStoreListAdapter(PlantOutStoreListActivity.this,billlist);
                        mListView.setAdapter(billListAdapter);
                    }
                } else {
                    UIUtil.showToast(listLslResponse.msg);
                }
            }
            @Override
            public void onError(Call call, Response response, Exception e) {
                super.onError(call, response, e);
                stopLoading();
                if (e != null){
                    Log.e("db", "onError: "+e.getMessage() );
                    e.printStackTrace();
                    UIUtil.showToast(e.getMessage());
                }
                if (e instanceof OkGoException){
                    UIUtil.showToast("抱歉，发生了未知错误！");
                } else if (e instanceof SocketTimeoutException){
                    UIUtil.showToast("你的手机网络太慢！");
                } else if (e instanceof ConnectException){
                    UIUtil.showToast("对不起，你的手机没有联网！");
                }
            }

        });
    }

    public void initView() {
        this.tv_billcode=(TextView)findViewById(R.id.tv_billcode);
        this.tv_banci=(TextView)findViewById(R.id.tv_banci);
        this.tv_prodectcenter=(TextView)findViewById(R.id.tv_prodectcenter);
        this.tv_shebei=(TextView)findViewById(R.id.tv_shebei);
        this.selectEtxt=(EditText)findViewById(R.id.selecttxt);
        selectEtxt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                String content=selectEtxt.getText().toString();
                if (content.length() > 0) {
                    fileterList.clear();
                    fileterList = (ArrayList<KL_outstore_model>) search(content);
                    billListAdapter.updateListView(fileterList);
                }else {
                    billListAdapter.updateListView(billlist);
                }
            }
        });
        this.mListView = (ListView) findViewById(R.id.listView);
        this.mListView.setOnItemClickListener(new AdapterView.OnItemClickListener()
        {
            public void onItemClick(AdapterView<?> paramAdapterView, View paramView, int paramInt, long paramLong)
            {
                String seltxt=PlantOutStoreListActivity.this.selectEtxt.getText().toString();
                final KL_outstore_model outstoremodel;
                if(seltxt==null || seltxt.equals("")){
                    outstoremodel = billlist.get(paramInt);
                }else {
                    outstoremodel=fileterList.get(paramInt);
                }

                if(outstoremodel==null || outstoremodel.ID ==""){
                    UIUtil.showToast("提示：请选择");
                }
                else {
                    PlantOutStoreListActivity.this.billid=outstoremodel.ID;
                    PlantOutStoreListActivity.this.billcode=outstoremodel.BillNo;
                    PlantOutStoreListActivity.this.name=outstoremodel.Name;
                    PlantOutStoreListActivity.this.JEmpName=outstoremodel.JEmpName;
                    Intent newInt=new Intent(PlantOutStoreListActivity.this,OutStoreIndexActivity.class);
                    Bundle loaclBundle=new Bundle();
                    loaclBundle.putString("billid",PlantOutStoreListActivity.this.billid);
                    loaclBundle.putString("billcode",PlantOutStoreListActivity.this.billcode);
                    loaclBundle.putString("name",PlantOutStoreListActivity.this.name);
                    loaclBundle.putString("JEmpName",PlantOutStoreListActivity.this.JEmpName);
                    newInt.putExtras(loaclBundle);
                    PlantOutStoreListActivity.this.startActivityForResult(newInt,100);
                }
            }
        });

        //公共
        this.commitBtn = ((Button) findViewById(R.id.commitBtn));
        this.cancelBtn = ((Button) findViewById(R.id.cancelBtn));
        this.titiltxt=(TextView)findViewById(R.id.titletxt);
        this.titiltxt.setText("车间发料列表");
        backImgBtn=(ImageButton)findViewById(R.id.back);
        this.backImgBtn.setOnClickListener(this);
        this.scanImgBtn=(ImageView)findViewById(R.id.scanImgBtn);
        this.scanImgBtn.setOnClickListener(this);
        scanImgBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent it = new Intent(PlantOutStoreListActivity.this, CaptureActivity.class);
                startActivityForResult(it, SCANNIN_GREQUEST_CODE);
            }
        });

    }

    private void inttoint(String billid) {

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode==100){

        }
        if (requestCode == SCANNIN_GREQUEST_CODE) {
            if (resultCode == Activity.RESULT_OK && data != null) {
                String result = data.getStringExtra("scan_result");
                Log.i("", "scan result:" + result);
                int count=0;
                for(KL_outstore_model model:billlist){
                    if(model.BillNo.equals(result)){
                        inttoint(model.BillNo);
                        count++;
                        break;
                    }
                }
                if (count!=0) {
                } else {
                    UIUtil.showToast("提示：没有该标签单据信息");
                }
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
                Intent it = new Intent(PlantOutStoreListActivity.this, CaptureActivity.class);
                startActivityForResult(it, SCANNIN_GREQUEST_CODE);
                break;
        }
    }

    /**
     * 模糊查询
     * @param str
     * @return
     */
    private List<KL_outstore_model> search(String str) {
        List<KL_outstore_model> filterList = new ArrayList<KL_outstore_model>();//过滤后的list
        if (!str.isEmpty() && this.billlist!=null && this.billlist.size()>0) {//正则表达式 匹配号码
            for (KL_outstore_model contact : this.billlist) {
                if (contact.BillNo != null) {
                    if (contact.BillNo.contains(str)) {
                        if (!filterList.contains(contact)) {
                            filterList.add(contact);
                        }
                    }
                }
            }
        }
        return filterList;
    }

    private ListView mListView;
    private ImageButton startImageB;
    private ImageButton backImgBtn;
    private TextView titiltxt;
    private String billid,billcode,billdate;
    private List<KL_outstore_model> billlist;
    private List<KL_outstore_model> fileterList;
    private KLOutStoreListAdapter billListAdapter;
    private final static int SCANNIN_GREQUEST_CODE = 101;
    private static Double sumnum,summny;


}
