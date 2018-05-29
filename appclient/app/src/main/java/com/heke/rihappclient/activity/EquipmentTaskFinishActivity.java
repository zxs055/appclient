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
import android.widget.RadioButton;
import android.widget.TextView;

import com.heke.rihappclient.R;
import com.heke.rihappclient.WebService.AppService;
import com.heke.rihappclient.adapter.SelectOperatorAdapter;
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

public class EquipmentTaskFinishActivity extends BaseActivity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_equipment_task_finish);
        this.mSharedPreferences = getSharedPreferences(ShareprefenceBean.SHAREPREFENCE_NAME, 0);
        this.currentuser = AppService.getInstance().getCurrentUser();
        clientid = AppService.getInstance().getDeviceId(this);
        this.initView();
        Intent localIntent = getIntent();
        this.billid = localIntent.getStringExtra("billid");
        this.billcode = localIntent.getStringExtra("billcode");
        this.billdate=localIntent.getStringExtra("billdate");
        this.loaddata();



    }

    public void initView() {
        this.mListView = (ListView) findViewById(R.id.listView);
        this.commitBtn = ((Button) findViewById(R.id.commitBtn));
        this.cancelBtn = ((Button) findViewById(R.id.cancelBtn));
        this.prodectcenter_layout=(LinearLayout)findViewById(R.id.prodectcenter_layout);
        this.prodectcenterTxt=(TextView)findViewById(R.id.prodectcenterTxt);
        backImgBtn=(ImageButton)findViewById(R.id.back);
        this.titiltxt=(TextView)findViewById(R.id.titletxt);
        this.titiltxt.setText("故障维修");
        this.backImgBtn.setOnClickListener(this);
        this.commitBtn.setOnClickListener(this);
        this.cancelBtn.setOnClickListener(this);
        this.prodectcenter_layout.setOnClickListener(this);
        this.bancimaplist = new ArrayList<Map<String, Object>>();
        this.prodectcentermaplist = new ArrayList<Map<String, Object>>();
        this.yesornobtn=(RadioButton)findViewById(R.id.OKorNORadiobtn);
        this.yesornobtn.setOnClickListener(this);
        this.addcailiao=(TextView)findViewById(R.id.addcailiao);
        this.addcailiao.setOnClickListener(this);

        this.scanImgBtn=(ImageView)findViewById(R.id.scanImgBtn);
        this.scanImgBtn.setOnClickListener(this);
        scanImgBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent it = new Intent(EquipmentTaskFinishActivity.this, CaptureActivity.class);
                startActivityForResult(it, SCANNIN_GREQUEST_CODE);
            }
        });
    }

    private void inttoint() {
        Intent newInt=new Intent(this,EquipmentTaskListActivity.class);
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
            case R.id.prodectcenter_layout:
                loadprodectcenter();
                break;
            case R.id.commitBtn:
                inttoint();
                break;
            case R.id.addcailiao:
                Intent newInt=new Intent(this,EquipmentAddDetailsActivity.class);
                startActivity(newInt);
                break;
        }
    }

    private void loadprodectcenter() {
        AlertDialog.Builder localBuilder = new AlertDialog.Builder(this);
        View localView = LayoutInflater.from(this).inflate(R.layout.popupwindow_list, null);
        ListView localListView = (ListView)localView.findViewById(R.id.popuplist);
        invListViewAdapter localCkListViewAdapter = new invListViewAdapter(this);
        try {
            this.mSemaphore.acquire();
            localCkListViewAdapter.setData(this.prodectcentermaplist);
            this.mSemaphore.release();
            localListView.setAdapter(localCkListViewAdapter);
            localListView.setOnItemClickListener(new AdapterView.OnItemClickListener()
            {
                public void onItemClick(AdapterView<?> paramAdapterView, View paramView, int paramInt, long paramLong)
                {
                    Map localMap = (Map)((Adapter)paramAdapterView.getAdapter()).getItem(paramInt);
                    String name=(String)localMap.get("invname") ;
                    EquipmentTaskFinishActivity.this.prodectcenterTxt.setText(name);
                    EquipmentTaskFinishActivity.this.dialog.dismiss();
                }
            });
            localBuilder.setView(localView);
            this.dialog = localBuilder.create();
            this.dialog.show();
        }catch (InterruptedException localInterruptedException)
        {
            while (true)
                localInterruptedException.printStackTrace();
        }
    }


    public void loaddata() {
        orderdetailslist=new ArrayList<dacdiaobodan1>();
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
        mListAdapter= new SelectOperatorAdapter(orderdetailslist,EquipmentTaskFinishActivity.this);
        mListView.setAdapter(mListAdapter);
        HashMap<String, Object> map = new HashMap<String, Object>();
        map.put("invname", "正常");
        map.put("rinfo", 1);
        bancimaplist.add(map);
        HashMap<String, Object> map1 = new HashMap<String, Object>();
        map1.put("invname", "故障停机");
        map1.put("rinfo", 2);
        bancimaplist.add(map1);
        HashMap<String, Object> map2 = new HashMap<String, Object>();
        map2.put("invname", "维修");
        map2.put("rinfo", 3);
        bancimaplist.add(map2);

        HashMap<String, Object> map11 = new HashMap<String, Object>();
        map11.put("invname", "正常");
        map11.put("rinfo", 1);
        prodectcentermaplist.add(map11);
        HashMap<String, Object> map111 = new HashMap<String, Object>();
        map111.put("invname", "故障停机");
        map111.put("rinfo", 2);
        prodectcentermaplist.add(map111);
        HashMap<String, Object> map112 = new HashMap<String, Object>();
        map112.put("invname", "维修");
        map112.put("rinfo", 3);
        prodectcentermaplist.add(map112);
        HashMap<String, Object> map113 = new HashMap<String, Object>();
        map113.put("invname", "带病运行");
        map113.put("rinfo", 4);
        prodectcentermaplist.add(map113);
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
    private LinearLayout prodectcenter_layout;
    private TextView prodectcenterTxt;
    private TextView addcailiao;
    private Button commitBtn;
    private Button cancelBtn;
    private ImageButton startImageB;
    private ImageButton backImgBtn;
    private TextView titiltxt;
    private String billid,billcode,billdate;
    private List<dacdiaobodan1> orderdetailslist;
    private SelectOperatorAdapter mListAdapter;
    private final static int SCANNIN_GREQUEST_CODE = 101;
    private static Double sumnum,summny;
    private volatile Semaphore mSemaphore = new Semaphore(1);
    private List<Map<String, Object>> bancimaplist;
    private List<Map<String, Object>> prodectcentermaplist;
    private AlertDialog dialog;
    private ImageView scanImgBtn;
    private RadioButton yesornobtn;

}
