package com.heke.rihappclient.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.heke.rihappclient.R;
import com.heke.rihappclient.WebService.AppService;
import com.heke.rihappclient.adapter.TaskListAdapter;
import com.heke.rihappclient.application.BaseActivity;
import com.heke.rihappclient.model.dacdiaobodan1;
import com.heke.rihappclient.shareprefence.ShareprefenceBean;
import com.heke.rihappclient.utils.UIUtil;

import java.util.ArrayList;
import java.util.List;

public class TaskListActivity extends BaseActivity implements View.OnClickListener {

    private String prodectcenter;
    private String banci;
    private String processname;
    private TextView tv_banci;
    private TextView tv_prodectcenter;
    private TextView tv_shebei;
    private ImageView scanImgBtn;
    private String goodsScope;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task_list);
        this.mSharedPreferences = getSharedPreferences(ShareprefenceBean.SHAREPREFENCE_NAME, 0);
        this.currentuser = AppService.getInstance().getCurrentUser();
        clientid = AppService.getInstance().getDeviceId(this);
        this.initView();
        Intent localIntent = getIntent();
        this.banci = localIntent.getStringExtra("banci");
        this.prodectcenter = localIntent.getStringExtra("prodectcenter");
        this.processname=localIntent.getStringExtra("processname");
        this.tv_banci.setText(banci);
        this.tv_prodectcenter.setText(prodectcenter);
        this.tv_shebei.setText(processname);
        this.loaddata();



    }

    public void initView() {
        this.mListView = (ListView) findViewById(R.id.listView);
        this.tv_banci=(TextView)findViewById(R.id.tv_banci);
        this.tv_prodectcenter=(TextView)findViewById(R.id.tv_prodectcenter);
        this.tv_shebei=(TextView)findViewById(R.id.tv_shebei);

        backImgBtn=(ImageButton)findViewById(R.id.back);
        this.titiltxt=(TextView)findViewById(R.id.titletxt);
        this.titiltxt.setText("工艺指令单列表");
        this.backImgBtn.setOnClickListener(this);
        this.mListView.setOnItemClickListener(new AdapterView.OnItemClickListener()
        {
            public void onItemClick(AdapterView<?> paramAdapterView, View paramView, int paramInt, long paramLong)
            {
                final dacdiaobodan1 dacdiaobodan1 = orderdetailslist.get(paramInt);
                if(dacdiaobodan1==null || dacdiaobodan1.billId=="" || dacdiaobodan1.itemNo=="0"){
                    UIUtil.showToast("提示：请选择");
                }
                else {
                    billcode=dacdiaobodan1.barCode;
                    goodsScope=dacdiaobodan1.goodsScope;
                    inttoint(billcode,goodsScope);
                }
            }
        });
        //公共
        this.scanImgBtn=(ImageView)findViewById(R.id.scanImgBtn);
        this.scanImgBtn.setOnClickListener(this);
        scanImgBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent it = new Intent(TaskListActivity.this, CaptureActivity.class);
                startActivityForResult(it, SCANNIN_GREQUEST_CODE);
            }
        });

    }

    private void inttoint(String billcode,String goodsScope) {
        Intent newInt=new Intent(this,Task_DetailsActivity.class);
        newInt.putExtra("billcode",billcode);
        newInt.putExtra("goodsScope",goodsScope);
        startActivity(newInt);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == SCANNIN_GREQUEST_CODE) {
            if (resultCode == Activity.RESULT_OK && data != null) {
                String result = data.getStringExtra("scan_result");
                Log.i("", "scan result:" + result);
                int count=0;
                for(dacdiaobodan1 model:orderdetailslist){
                    if(model.barCode.equals(result)){
                        inttoint(model.billCode,model.goodsScope);
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
        }
    }
    public void loaddata() {
        orderdetailslist=new ArrayList<dacdiaobodan1>();
        if(tv_banci.getText().toString().equals("白班")&&tv_prodectcenter.getText().toString().equals("鼓楼")&&tv_shebei.getText().toString().equals("万能机")){
            dacdiaobodan1 ddbd=new dacdiaobodan1();
            ddbd.barCode="JL 1611-0003-18032111171";
            ddbd.quantity="成缆";
            ddbd.goodsName="绝缘半成品";
            ddbd.goodsScope="3000";
            orderdetailslist.add(ddbd);
            dacdiaobodan1 ddbd1=new dacdiaobodan1();
            ddbd1.barCode="JL 1611-0003-18032111172";
            ddbd1.quantity="成缆";
            ddbd1.goodsName="绝缘半成品";
            ddbd1.goodsScope="3063";
            orderdetailslist.add(ddbd1);
            dacdiaobodan1 ddbd2=new dacdiaobodan1();
            ddbd2.barCode="JL 1611-0003-18032111173";
            ddbd2.quantity="成缆";
            ddbd2.goodsName="绝缘半成品";
            ddbd2.goodsScope="3063";
            orderdetailslist.add(ddbd2);
        }
        taskListAdapter=new TaskListAdapter(TaskListActivity.this,orderdetailslist);
        mListView.setAdapter(taskListAdapter);
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
    private ImageButton startImageB;
    private ImageButton backImgBtn;
    private TextView titiltxt;
    private String billid,billcode,billdate;
    private List<dacdiaobodan1> orderdetailslist;
    private TaskListAdapter taskListAdapter;
    private final static int SCANNIN_GREQUEST_CODE = 101;
    private static Double sumnum,summny;


}
