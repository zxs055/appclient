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
import com.heke.rihappclient.adapter.SelectOperatorAdapter;
import com.heke.rihappclient.adapter.invListViewAdapter;
import com.heke.rihappclient.application.BaseActivity;
import com.heke.rihappclient.model.dacdiaobodan1;
import com.heke.rihappclient.shareprefence.ShareprefenceBean;
import com.heke.rihappclient.utils.UIUtil;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Semaphore;

public class PandianIndexActivity extends BaseActivity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pandian_index);
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
        this.pandianuser_layout = ((LinearLayout)findViewById(R.id.pandianuser_layout));
        this.pandianuserTxt=(TextView)findViewById(R.id.pandianuserTxt);
        this.pandianuser_layout.setOnClickListener(this);
        this.pandianuserList = new ArrayList<Map<String, Object>>();
        this.load_layout=(LinearLayout)findViewById(R.id.load_layout);
        this.load_layout.setOnClickListener(this);
        orderdetailslist=new ArrayList<dacdiaobodan1>();
        this.pandianCodeTxt=(TextView)findViewById(R.id.pandianCodeTxt);
        this.pandianStoreTxt=(TextView)findViewById(R.id.pandianStoreTxt);
        this.pandiancangweiTxt=(TextView)findViewById(R.id.pandiancangweiTxt);

        //公共
        this.commitBtn = ((Button) findViewById(R.id.commitBtn));
        this.cancelBtn = ((Button) findViewById(R.id.cancelBtn));
        backImgBtn=(ImageButton)findViewById(R.id.back);
        this.titiltxt=(TextView)findViewById(R.id.titletxt);
        this.titiltxt.setText("仓库盘点");
        this.backImgBtn.setOnClickListener(this);
        this.commitBtn.setOnClickListener(this);
        this.cancelBtn.setOnClickListener(this);
        this.scanImgBtn=(ImageView)findViewById(R.id.scanImgBtn);
        this.scanImgBtn.setOnClickListener(this);
        scanImgBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent it = new Intent(PandianIndexActivity.this, CaptureActivity.class);
                startActivityForResult(it, SCANNIN_GREQUEST_CODE);
            }
        });
    }

    private void inttoint() {
        this.billcode=this.pandianCodeTxt.getText().toString();
        this.storename=this.pandianStoreTxt.getText().toString();
        this.kuweiname=this.pandiancangweiTxt.getText().toString();
        Intent newInt=new Intent(this,PandianDetailActivity.class);
        Bundle localBundle = new Bundle();
        localBundle.putString("billcode",this.billcode);
        localBundle.putString("storename", this.storename);
        localBundle.putString("kuweiname", this.kuweiname);
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
            case R.id.pandianuser_layout:
                loaduser();
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
        mListAdapter= new GoodsDetailsDacdiaoboListAdapter(PandianIndexActivity.this,orderdetailslist);
        mListView.setAdapter(mListAdapter);
    }

    private void loaduser() {
        AlertDialog.Builder localBuilder = new AlertDialog.Builder(this);
        View localView = LayoutInflater.from(this).inflate(R.layout.popupwindow_list, null);
        ListView localListView = (ListView)localView.findViewById(R.id.popuplist);
        invListViewAdapter localCkListViewAdapter = new invListViewAdapter(this);
        try {
            this.mSemaphore.acquire();
            localCkListViewAdapter.setData(this.pandianuserList);
            this.mSemaphore.release();
            localListView.setAdapter(localCkListViewAdapter);
            localListView.setOnItemClickListener(new AdapterView.OnItemClickListener()
            {
                public void onItemClick(AdapterView<?> paramAdapterView, View paramView, int paramInt, long paramLong)
                {
                    Map localMap = (Map)((Adapter)paramAdapterView.getAdapter()).getItem(paramInt);
                    String name=(String)localMap.get("invname") ;
                    PandianIndexActivity.this.pandianuserTxt.setText(name);
                    PandianIndexActivity.this.dialog.dismiss();
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
        this.pandianCodeTxt.setText("PD-20180327001");
        this.pandianStoreTxt.setText("大仓");
        this.pandiancangweiTxt.setText("KL-1259");
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
    private LinearLayout pandianuser_layout;
    private TextView pandianuserTxt;
    private TextView pandianCodeTxt;
    private TextView pandianStoreTxt;
    private TextView pandiancangweiTxt;
    private LinearLayout load_layout;

    private ImageButton startImageB;

    private String billcode,storename,kuweiname;
    private List<dacdiaobodan1> orderdetailslist;
    private GoodsDetailsDacdiaoboListAdapter mListAdapter;
    private final static int SCANNIN_GREQUEST_CODE = 101;
    private static Double sumnum,summny;
    private volatile Semaphore mSemaphore = new Semaphore(1);
    private List<Map<String, Object>> pandianuserList;
    private AlertDialog dialog;


    //公共
    private Button commitBtn;
    private Button cancelBtn;
    private ImageButton backImgBtn;
    private TextView titiltxt;
    private ImageView scanImgBtn;
}
