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
import android.widget.AutoCompleteTextView;
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
import com.heke.rihappclient.adapter.OutStoreListAdapter;
import com.heke.rihappclient.adapter.OutStoreList_zt_Adapter;
import com.heke.rihappclient.application.BaseActivity;
import com.heke.rihappclient.model.dacdiaobodan1;
import com.heke.rihappclient.model.rukuinfo;
import com.heke.rihappclient.shareprefence.ShareprefenceBean;
import com.heke.rihappclient.shareprefence.ShareprefenceBillMessage;
import com.heke.rihappclient.utils.UIUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Semaphore;

public class OutStoreDetail_zt_Activity extends BaseActivity implements View.OnClickListener{

    private RadioButton nosaoma;
    private RadioButton saomaout;
    private RadioButton saomaoin;
    private RadioGroup rgsaoma;
    private ListView mListView;
    private List<dacdiaobodan1> orderdetailslist;
    private OutStoreList_zt_Adapter mListAdapter;
    private EditText tv_fahuonumber;
    private EditText tv_unit;
    private  int  type=1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_outstore_details_zt);
        this.mSharedPreferences = getSharedPreferences(ShareprefenceBean.SHAREPREFENCE_NAME, 0);
        this.mSharedPreferencesbill=getSharedPreferences(ShareprefenceBillMessage.SHAREPREFENCE_NAME,0);
        this.currentuser = AppService.getInstance().getCurrentUser();
        clientid=AppService.getInstance().getDeviceId(this);
        Intent localIntent = getIntent();
        this.quantity = localIntent.getStringExtra("quantity");
        this.initView();
        this.tv_fahuonumber.setText(quantity);
        this.tv_unit.setText("卷");
        this.init();
    }
    public void init() {
        this.invcodemaplist = new ArrayList<Map<String, Object>>();
        this.barcodelist = new ArrayList<String>();
        mListAdapter= new OutStoreList_zt_Adapter(OutStoreDetail_zt_Activity.this,orderdetailslist);
        mListView.setAdapter(mListAdapter);
        List<String> billlist = new ArrayList<String>();
        int size = mSharedPreferencesbill.getInt(ShareprefenceBillMessage.SIZE, 0);
        for (int i = 0; i < size; i++)
        {
            String environItem = mSharedPreferencesbill.getString("item_"+i, null);
            billlist.add(environItem);
        }
    }
    public void initView() {
        this.mListView = (ListView) findViewById(R.id.listView);
        orderdetailslist=new ArrayList<dacdiaobodan1>();
        this.backImgBtn = ((ImageButton) findViewById(R.id.back));
        this.backImgBtn.setOnClickListener(this);
        this.nosaoma=(RadioButton)findViewById(R.id.nosaoma);
        this.saomaoin=(RadioButton)findViewById(R.id.saomaoin);
        this.saomaout=(RadioButton)findViewById(R.id.saomaout);
        this.rgsaoma=(RadioGroup)findViewById(R.id.rg_saoma);
        rgsaoma.setOnCheckedChangeListener(new MyRadioButtonListener());
        this.tv_fahuonumber=(EditText) findViewById(R.id.tv_fahuonumber);
        this.tv_unit=(EditText)findViewById(R.id.tv_unit);

        //公共
        this.commitBtn = ((Button) findViewById(R.id.commitBtn));
        this.cancelBtn = ((Button) findViewById(R.id.cancelBtn));
        this.titiltxt=(TextView)findViewById(R.id.titletxt);
        this.titiltxt.setText("电缆条码采集");
        this.commitBtn.setOnClickListener(this);
        this.cancelBtn.setOnClickListener(this);
        this.scanImgBtn=(ImageView)findViewById(R.id.scanImgBtn);
        this.scanImgBtn.setOnClickListener(this);
        scanImgBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent it = new Intent(OutStoreDetail_zt_Activity.this, CaptureActivity.class);
                startActivityForResult(it, SCANNIN_GREQUEST_CODE);
            }
        });
    }

    class MyRadioButtonListener implements RadioGroup.OnCheckedChangeListener {

        @Override
        public void onCheckedChanged(RadioGroup group, int checkedId) {
            // 选中状态改变时被触发
            switch (checkedId) {
                case R.id.nosaoma:
                    break;
                case R.id.saomaoin:
                    break;
                case R.id.saomaout:
                    break;
            }
        }
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
                if (TextUtils.isEmpty(this.billcode)) {
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
        }
    }

    public void loaddata(String barcode) {
        dacdiaobodan1 ddbd=new dacdiaobodan1();
        ddbd.goodsName=barcode;
        orderdetailslist.add(ddbd);
        mListAdapter.updateListView(orderdetailslist);
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
        Toast.makeText(this, "提交成功", Toast.LENGTH_SHORT).show();
    }
    private ImageButton backImgBtn;
    private List<rukuinfo> invcodelist;
    private List<Map<String, Object>> invcodemaplist;
    private volatile Semaphore mSemaphore = new Semaphore(1);
    private AlertDialog dialog;
    private final static int SCANNIN_GREQUEST_CODE = 101;
    private String billcode,storename,kuweiname,quantity;
    public ArrayAdapter<String> aAdapter;
    public List<String> barcodelist;


    //公共
    private Button commitBtn;
    private Button cancelBtn;
    private TextView titiltxt;
    private ImageView scanImgBtn;
}


