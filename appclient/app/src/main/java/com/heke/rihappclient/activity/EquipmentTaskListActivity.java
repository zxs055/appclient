package com.heke.rihappclient.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.heke.rihappclient.R;
import com.heke.rihappclient.WebService.AppService;
import com.heke.rihappclient.adapter.GoodsDetailsDacdiaoboListAdapter;
import com.heke.rihappclient.application.BaseActivity;
import com.heke.rihappclient.model.dacdiaobodan1;
import com.heke.rihappclient.shareprefence.ShareprefenceBean;
import com.heke.rihappclient.utils.UIUtil;

import java.util.ArrayList;
import java.util.List;

public class EquipmentTaskListActivity extends BaseActivity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_equipment_task_list);
        this.mSharedPreferences = getSharedPreferences(ShareprefenceBean.SHAREPREFENCE_NAME, 0);
        this.currentuser = AppService.getInstance().getCurrentUser();
        clientid = AppService.getInstance().getDeviceId(this);
        this.initView();
        Intent localIntent = getIntent();
        this.processname = localIntent.getStringExtra("processname");
        this.prodectcenter = localIntent.getStringExtra("prodectcenter");
        this.loaddata();



    }

    public void initView() {
        this.tv_shebei=(TextView)findViewById(R.id.tv_shebei);
        this.tv_sczx=(TextView)findViewById(R.id.tv_sczx);
        this.billscan_linear1=(LinearLayout)findViewById(R.id.billscan_linear1);
        this.billscan_linear1.setOnClickListener(this);
        this.mListView = (ListView) findViewById(R.id.listView);
        backImgBtn=(ImageButton)findViewById(R.id.back);
        this.titiltxt=(TextView)findViewById(R.id.titletxt);
        this.titiltxt.setText("故障维修列表");
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
                    if(dacdiaobodan1.billState!=null&&dacdiaobodan1.billState.equals("Y")){
                        AlertDialog.Builder builder = new AlertDialog.Builder(EquipmentTaskListActivity.this);
                        builder.setMessage("设备故障任务【" + dacdiaobodan1.barCode + "】确定取消维修?");
                        builder.setTitle("提示");

                        //添加AlertDialog.Builder对象的setPositiveButton()方法
                        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
//                            inttoint();
                                dacdiaobodan1.billState="N";
                                mordergoodListAdapter.notifyDataSetChanged();

                            }
                        });

                        //添加AlertDialog.Builder对象的setNegativeButton()方法
                        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {

                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        });
                        builder.create().show();
                    }else{
                        AlertDialog.Builder builder = new AlertDialog.Builder(EquipmentTaskListActivity.this);
                        builder.setMessage("设备故障任务【" + dacdiaobodan1.barCode + "】确定开始维修?");
                        builder.setTitle("提示");

                        //添加AlertDialog.Builder对象的setPositiveButton()方法
                        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
    //                            inttoint();
                                dacdiaobodan1.billState="Y";
                                mordergoodListAdapter.notifyDataSetChanged();

                            }
                        });

                        //添加AlertDialog.Builder对象的setNegativeButton()方法
                        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {

                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        });
                        builder.create().show();
                    }
                }
            }
        });
        this.mListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                final dacdiaobodan1 dacdiaobodan1 = orderdetailslist.get(position);
                AlertDialog.Builder builder = new AlertDialog.Builder(EquipmentTaskListActivity.this);
                builder.setMessage("设备故障任务【" + dacdiaobodan1.barCode + "】确定完成维修?");
                builder.setTitle("提示");

                //添加AlertDialog.Builder对象的setPositiveButton()方法
                builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        inttoint();
                    }
                });

                //添加AlertDialog.Builder对象的setNegativeButton()方法
                builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                builder.create().show();
                return false;
            }
        });

    }

    private void inttoint() {
        Intent newInt=new Intent(this,EquipmentTaskFinishActivity.class);
        startActivity(newInt);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == SCANNIN_GREQUEST_CODE) {
            if (resultCode == Activity.RESULT_OK && data != null) {
                String result = data.getStringExtra("scan_result");
                Log.i("", "scan result:" + result);
                if (!result.equals("")) {
                    if (result.length() > 0) {
                        ArrayList<dacdiaobodan1> fileterList = (ArrayList<dacdiaobodan1>) search(result);
                        mordergoodListAdapter.updateListView(fileterList);
                    } else {
                        mordergoodListAdapter.updateListView(orderdetailslist);
                    }
                } else {
                    UIUtil.showToast("提示：没有信息");
                }
            }
        }else if(requestCode == SELECT_GREQUEST_CODE){
            if(resultCode==Activity.RESULT_OK && data!=null){
                prodectcenter=data.getStringExtra("prodectcenter");
                processname=data.getStringExtra("processname");
                Log.i("","result:"+prodectcenter+"/"+processname);
                tv_sczx.setText(prodectcenter);
                tv_shebei.setText(processname);
            }else{
                UIUtil.showToast("提示：没有选择任何项！");
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
    public void onClick(View paramView) {
        switch (paramView.getId()) {
            case R.id.back:
                this.finish();
                break;
            case R.id.billscan_linear1:
                Intent newInt=new Intent(this,EquipmentTaskIndexActivity.class);
                startActivityForResult(newInt,SELECT_GREQUEST_CODE);
                break;
        }
    }
    public void loaddata() {
        orderdetailslist=new ArrayList<dacdiaobodan1>();
        dacdiaobodan1 ddbd=new dacdiaobodan1();
        ddbd.barCode="JL 1611-0003-18032111171";
        ddbd.quantity="成缆";
        ddbd.goodsName="绝缘半成品";
        ddbd.goodsScope="3063";
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
        dacdiaobodan1 ddbd7=new dacdiaobodan1();
        ddbd7.barCode="JL 1611-0003-18032111173";
        ddbd7.quantity="成缆";
        ddbd7.goodsName="绝缘半成品";
        ddbd7.goodsScope="3063";
        orderdetailslist.add(ddbd7);
        dacdiaobodan1 ddbd3=new dacdiaobodan1();
        ddbd3.barCode="JL 1611-0003-18032111173";
        ddbd3.quantity="成缆";
        ddbd3.goodsName="绝缘半成品";
        ddbd3.goodsScope="3063";
        orderdetailslist.add(ddbd3);
        dacdiaobodan1 ddbd4=new dacdiaobodan1();
        ddbd4.barCode="JL 1611-0003-18032111173";
        ddbd4.quantity="成缆";
        ddbd4.goodsName="绝缘半成品";
        ddbd4.goodsScope="3063";
        orderdetailslist.add(ddbd4);
        dacdiaobodan1 ddbd5=new dacdiaobodan1();
        ddbd5.barCode="JL 1611-0003-18032111173";
        ddbd5.quantity="成缆";
        ddbd5.goodsName="绝缘半成品";
        ddbd5.goodsScope="3063";
        orderdetailslist.add(ddbd5);
        dacdiaobodan1 ddbd6=new dacdiaobodan1();
        ddbd6.barCode="JL 1611-0003-18032111173";
        ddbd6.quantity="成缆";
        ddbd6.goodsName="绝缘半成品";
        ddbd6.goodsScope="3063";
        orderdetailslist.add(ddbd6);
        mordergoodListAdapter=new GoodsDetailsDacdiaoboListAdapter(EquipmentTaskListActivity.this,orderdetailslist);
        mListView.setAdapter(mordergoodListAdapter);
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
    private String prodectcenter,processname;
    private List<dacdiaobodan1> orderdetailslist;
    private GoodsDetailsDacdiaoboListAdapter mordergoodListAdapter;
    private final static int SCANNIN_GREQUEST_CODE = 101;
    private final static int SELECT_GREQUEST_CODE= 250;
    private static Double sumnum,summny;
    private LinearLayout billscan_linear1;
    private  TextView tv_sczx;
    private TextView tv_shebei;


}
