package com.heke.rihappclient.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.heke.rihappclient.R;
import com.heke.rihappclient.WebService.AppService;
import com.heke.rihappclient.adapter.GoodsDetailsDacdiaoboListAdapter;
import com.heke.rihappclient.adapter.invListViewAdapter;
import com.heke.rihappclient.application.BaseActivity;
import com.heke.rihappclient.model.dacdiaobodan1;
import com.heke.rihappclient.shareprefence.ShareprefenceBean;
import com.heke.rihappclient.utils.UIUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Semaphore;

public class QC_listActivity extends BaseActivity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qc_list);
        this.mSharedPreferences = getSharedPreferences(ShareprefenceBean.SHAREPREFENCE_NAME, 0);
        this.currentuser = AppService.getInstance().getCurrentUser();
        clientid = AppService.getInstance().getDeviceId(this);
        this.initView();
        Intent localIntent = getIntent();
        this.billcode = localIntent.getStringExtra("billcode");
        this.loaddata();



    }

    public void initView() {
        this.mListView = (ListView) findViewById(R.id.listView);
        this.pandianuserList = new ArrayList<Map<String, Object>>();
        orderdetailslist=new ArrayList<dacdiaobodan1>();
        this.load_layout=(LinearLayout)findViewById(R.id.load_layout);
        this.load_layout.setOnClickListener(this);

        //公共
        this.commitBtn = ((Button) findViewById(R.id.commitBtn));
        this.cancelBtn = ((Button) findViewById(R.id.cancelBtn));
        backImgBtn=(ImageButton)findViewById(R.id.back);
        this.titiltxt=(TextView)findViewById(R.id.titletxt);
        this.titiltxt.setText("工序检验项目");
        this.backImgBtn.setOnClickListener(this);
        this.commitBtn.setOnClickListener(this);
        this.cancelBtn.setOnClickListener(this);
        this.scanImgBtn=(ImageView)findViewById(R.id.scanImgBtn);
        this.scanImgBtn.setOnClickListener(this);
        scanImgBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent it = new Intent(QC_listActivity.this, CaptureActivity.class);
                startActivityForResult(it, SCANNIN_GREQUEST_CODE);
            }
        });
        this.mListView.setOnItemClickListener(new AdapterView.OnItemClickListener()
        {
            public void onItemClick(AdapterView<?> paramAdapterView, View paramView, int paramInt, long paramLong)
            {
                final dacdiaobodan1 dacdiaobodan1 = orderdetailslist.get(paramInt);
                if(dacdiaobodan1==null || dacdiaobodan1.billId=="" || dacdiaobodan1.itemNo=="0"){
                    UIUtil.showToast("提示：请选择");
                }
                else {
                    name=dacdiaobodan1.goodsName;
                    unit=dacdiaobodan1.goodsScope;
                    number=dacdiaobodan1.barCode;
                    inttoint();
                }
            }
        });
    }

    private void inttoint() {
        Intent newInt=new Intent(this,QC_editDetailsActivity.class);
        Bundle localBundle = new Bundle();
        localBundle.putString("name",this.name);
        localBundle.putString("unit", this.unit);
        localBundle.putString("number", this.number);
        newInt.putExtras(localBundle);
        this.startActivityForResult(newInt,100);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == SCANNIN_GREQUEST_CODE) {
            if (resultCode == Activity.RESULT_OK && data != null) {
                String result = data.getStringExtra("scan_result");
                Log.i("", "scan result:" + result);
                if (!result.equals("")) {
                    if (result.length() > 0) {
                        //ArrayList<dacdiaobodan1> fileterList = (ArrayList<dacdiaobodan1>) search(result);
                        //mListAdapter.updateListView(fileterList);
                    } else {
                        //mListAdapter.updateListView(orderdetailslist);
                    }
                } else {
                    UIUtil.showToast("提示：没有该标签入库信息");
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
            case R.id.commitBtn:
                inttoint();
                break;
            case R.id.load_layout:
                loaddetail();
                break;
        }
    }

    private void loaddetail() {
        dacdiaobodan1 ddbd=new dacdiaobodan1();
        ddbd.barCode="JL 1611-0003-18032111171";
        ddbd.quantity="成缆";
        ddbd.goodsName="万能机";
        ddbd.goodsScope="3063";
        orderdetailslist.add(ddbd);
        dacdiaobodan1 ddbd1=new dacdiaobodan1();
        ddbd1.barCode="JL 1611-0003-18032111172";
        ddbd1.quantity="成缆";
        ddbd1.goodsName="拉皮机";
        ddbd1.goodsScope="3063";
        orderdetailslist.add(ddbd1);
        dacdiaobodan1 ddbd2=new dacdiaobodan1();
        ddbd2.barCode="JL 1611-0003-18032111173";
        ddbd2.quantity="成缆";
        ddbd2.goodsName="冷焊机";
        ddbd2.goodsScope="3063";
        orderdetailslist.add(ddbd2);
        mListAdapter= new GoodsDetailsDacdiaoboListAdapter(QC_listActivity.this,orderdetailslist);
        mListView.setAdapter(mListAdapter);
    }



    public void loaddata() {
        HashMap<String, Object> map = new HashMap<String, Object>();
        map.put("invname", "张三");
        map.put("rinfo", 1);
        pandianuserList.add(map);
        HashMap<String, Object> map1 = new HashMap<String, Object>();
        map1.put("invname", "李四");
        map1.put("rinfo", 2);
        pandianuserList.add(map1);
        HashMap<String, Object> map2 = new HashMap<String, Object>();
        map2.put("invname", "王五");
        map2.put("rinfo", 3);
        pandianuserList.add(map2);
    }
    /**
     * 模糊查询
     * @param str
     * @return
     */
    private List<dacdiaobodan1> search(String str) {
        List<dacdiaobodan1> filterList = new ArrayList<dacdiaobodan1>();//过滤后的list
        if (!str.isEmpty() && this.orderdetailslist!=null && this.orderdetailslist.size()>0) {//正则表达式 匹配号码
            for (dacdiaobodan1 contact : this.orderdetailslist) {
                if (contact.barCode != null) {
                    if (contact.barCode.contains(str)) {
                        if (!filterList.contains(contact)) {
                            filterList.add(contact);
                            sumnum+=Double.parseDouble(contact.quantity);
                            //summny+=Double.parseDouble(contact.amount);
                        }
                    }
                }
            }
        }
        return filterList;
    }

    private ListView mListView;
    private LinearLayout load_layout;

    private String billcode,bususer;
    private List<dacdiaobodan1> orderdetailslist;
    private GoodsDetailsDacdiaoboListAdapter mListAdapter;
    private final static int SCANNIN_GREQUEST_CODE = 101;
    private static Double sumnum,summny;
    private volatile Semaphore mSemaphore = new Semaphore(1);
    private List<Map<String, Object>> pandianuserList;
    private AlertDialog dialog;
    private  String name,unit,number;


    //公共
    private Button commitBtn;
    private Button cancelBtn;
    private ImageButton backImgBtn;
    private TextView titiltxt;
    private ImageView scanImgBtn;
}
