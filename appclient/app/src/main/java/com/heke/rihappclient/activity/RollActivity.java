package com.heke.rihappclient.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.heke.rihappclient.R;
import com.heke.rihappclient.WebService.AppService;
import com.heke.rihappclient.adapter.invListViewAdapter;
import com.heke.rihappclient.adapter.productBarCodeListAdapter;
import com.heke.rihappclient.application.BaseActivity;
import com.heke.rihappclient.model.dacdiaobodan1;
import com.heke.rihappclient.shareprefence.ShareprefenceBean;
import com.heke.rihappclient.utils.UIUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Semaphore;

public class RollActivity extends BaseActivity implements View.OnClickListener {

    private EditText bzbarcode;
    private int lockcount=0;
    private LinearLayout baozhuangType_layout;
    private  TextView baozhuangTypeTxt;
    private List<Map<String, Object>> baozhuangTypemaplist;
    private LinearLayout color_layout;
    private  TextView colorTxt;
    private List<Map<String, Object>> colormaplist;
    private LinearLayout prType_layout;
    private  TextView prTypeTxt;
    private List<Map<String, Object>> prTypemaplist;
    private EditText panshuNum;
    private EditText ztbarcode;
    private LinearLayout detail_layout;
    private final int colorint=0xfd696969;
    private final int colorint1=0xff0099ff;
    private TextView detailtxt;
    private int baoshu=0;
    private int juanshu=0;
    private int baojuanshu=0;
    private View main_btntop;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_roll);
        this.mSharedPreferences = getSharedPreferences(ShareprefenceBean.SHAREPREFENCE_NAME, 0);
        this.currentuser = AppService.getInstance().getCurrentUser();
        clientid = AppService.getInstance().getDeviceId(this);
        this.initView();
        Intent localIntent = getIntent();
        this.billid = localIntent.getStringExtra("billid");
        this.billcode = localIntent.getStringExtra("billcode");
        this.billdate=localIntent.getStringExtra("billdate");
        this.prTypemaplist = new ArrayList<Map<String, Object>>();
        this.colormaplist = new ArrayList<Map<String, Object>>();
        this.baozhuangTypemaplist = new ArrayList<Map<String, Object>>();
        this.loaddata();
    }

    public void initView() {
        this.mListView = (ListView) findViewById(R.id.listView);
        backImgBtn=(ImageButton)findViewById(R.id.back);
        this.backImgBtn.setOnClickListener(this);
        this.bzbarcode=(EditText)findViewById(R.id.bzbarcode);
        this.bzbarcode.setOnClickListener(this);
        this.color_layout=(LinearLayout)findViewById(R.id.color_layout);
        this.colorTxt=(TextView)findViewById(R.id.colorTxt);
        this.color_layout.setOnClickListener(this);
        this.baozhuangType_layout=(LinearLayout)findViewById(R.id.baozhuangType_layout);
        this.baozhuangTypeTxt=(TextView)findViewById(R.id.baozhuangTypeTxt);
        this.baozhuangType_layout.setOnClickListener(this);
        this.prType_layout=(LinearLayout)findViewById(R.id.prType_layout);
        this.prTypeTxt=(TextView)findViewById(R.id.prTypeTxt);
        this.prType_layout.setOnClickListener(this);
        this.ztbarcode=(EditText)findViewById(R.id.ztbarcode);
        this.detail_layout=(LinearLayout)findViewById(R.id.detail_layout);
        this.panshuNum=(EditText)findViewById(R.id.panshuNum);
        this.detailtxt=(TextView)findViewById(R.id.detailtxt);
        this.main_btntop=findViewById(R.id.main_btntop);

        bzbarcode.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if(lockcount==0){
                    lock();
                }
                bzbarcode.clearFocus();

                bzbarcode.setFocusable(false);
                lockcount++;

            }
        });

        //公共
        this.commitBtn = ((Button) findViewById(R.id.commitBtn));
        this.cancelBtn = ((Button) findViewById(R.id.cancelBtn));
        this.titiltxt=(TextView)findViewById(R.id.titletxt);
        this.titiltxt.setText("打卷");
        this.cancelBtn.setText("扫卷码");
        this.commitBtn.setOnClickListener(this);
        this.cancelBtn.setOnClickListener(this);
        this.scanImgBtn=(ImageView)findViewById(R.id.scanImgBtn);
        this.scanImgBtn.setOnClickListener(this);
        scanImgBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent it = new Intent(RollActivity.this, CaptureActivity.class);
                startActivityForResult(it, SCANNIN_GREQUEST_CODE);
            }
        });

    }

    private void inttoint() {
        Intent newInt=new Intent(this,Task_DetailsActivity.class);
        startActivity(newInt);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == SCANNIN_GREQUEST_CODE) {
            if (resultCode == Activity.RESULT_OK && data != null) {
                String result = data.getStringExtra("scan_result");
                Log.i("", "scan result:" + result);
                if (!result.equals("")) {
                        View rootview = RollActivity.this.getWindow().getDecorView();
                        View et = rootview.findFocus();
                        if(et instanceof EditText){
                            ((EditText)et).setText(result);
                        }else{
                            dacdiaobodan1 model=new dacdiaobodan1();
                            model.barCode=result;
                            orderdetailslist.add(model);
                            mordergoodListAdapter.updateListView(orderdetailslist);
                            setListViewHeightBasedOnChildren(mListView);
                            baojuanshu++;
                        }
                    judgenumber();

                } else {
                    UIUtil.showToast("提示：没有该标签信息");
                }
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    private void judgenumber() {

            if(orderdetailslist.size()==Integer.parseInt(panshuNum.getText().toString())){
                clearlistmore();
            }
        detailtxt.setText("当前包卷数："+baojuanshu+";当前盘包数："+baoshu+";当前盘卷数："+juanshu);

    }

    //清空列表和包码
    private void clearlistmore() {
        orderdetailslist.clear();
        mordergoodListAdapter.updateListView(orderdetailslist);
        setListViewHeightBasedOnChildren(mListView);
        ztbarcode.setText("");
        ztbarcode.setFocusable(true);
        ztbarcode.setFocusableInTouchMode(true);
        ztbarcode.setTextColor(colorint1);
        panshuNum.setFocusable(true);
        panshuNum.setFocusableInTouchMode(true);
        panshuNum.setTextColor(colorint1);

    }




    public void onClick(View paramView) {
        switch (paramView.getId()) {
            case R.id.back:
                this.finish();
                break;
            case R.id.commitBtn:
                Intent newInt=new Intent(this,Task_FinishActivity.class);
                startActivity(newInt);
                break;
            case R.id.prType_layout:
                loadlistup(prTypeTxt,prTypemaplist);
                break;
            case R.id.color_layout:
                loadlistup(colorTxt,colormaplist);
                break;
            case R.id.baozhuangType_layout:
                loadlistup(baozhuangTypeTxt,baozhuangTypemaplist);
                break;
            case R.id.cancelBtn:
                ScanJunbarCode();
                break;
        }
    }

    private void ScanJunbarCode() {

        if(bzbarcode.getText().toString()==null||bzbarcode.getText().toString()==""){
            Toast.makeText(this, "请扫单元件号！", Toast.LENGTH_SHORT).show();
            return;
        }
        Intent it = new Intent(RollActivity.this, CaptureActivity.class);
        startActivityForResult(it, SCANNIN_GREQUEST_CODE);
    }

    private void lock(){
        panshuNum.setFocusable(false);
        panshuNum.setFocusableInTouchMode(false);
        panshuNum.setTextColor(colorint);
        ztbarcode.setFocusable(false);
        ztbarcode.setFocusableInTouchMode(false);
        ztbarcode.setTextColor(colorint);
        baozhuangType_layout.setClickable(false);
        baozhuangTypeTxt.setTextColor(colorint);
        prType_layout.setClickable(false);
        prTypeTxt.setTextColor(colorint);
        color_layout.setClickable(false);
        colorTxt.setTextColor(colorint);
    }

    private void loadlistup(final TextView textV, List<Map<String, Object>> list) {
        AlertDialog.Builder localBuilder = new AlertDialog.Builder(this);
        View localView = LayoutInflater.from(this).inflate(R.layout.popupwindow_list, null);
        ListView localListView = (ListView)localView.findViewById(R.id.popuplist);
        invListViewAdapter localCkListViewAdapter = new invListViewAdapter(this);
        try {
            this.mSemaphore.acquire();
            localCkListViewAdapter.setData(list);
            this.mSemaphore.release();
            localListView.setAdapter(localCkListViewAdapter);
            localListView.setOnItemClickListener(new AdapterView.OnItemClickListener()
            {
                public void onItemClick(AdapterView<?> paramAdapterView, View paramView, int paramInt, long paramLong)
                {
                    Map localMap = (Map)((Adapter)paramAdapterView.getAdapter()).getItem(paramInt);
                    String name=(String)localMap.get("invname") ;
                    textV.setText(name);
                    RollActivity.this.dialog.dismiss();
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
        mordergoodListAdapter=new productBarCodeListAdapter(RollActivity.this,orderdetailslist);
        mListView.setAdapter(mordergoodListAdapter);
        setListViewHeightBasedOnChildren(mListView);

        HashMap<String, Object> map11 = new HashMap<String, Object>();
        map11.put("invname", "BV 1.5");
        map11.put("rinfo", 1);
        prTypemaplist.add(map11);
        HashMap<String, Object> map111 = new HashMap<String, Object>();
        map111.put("invname", "BV 2.5");
        map111.put("rinfo", 2);
        prTypemaplist.add(map111);
        HashMap<String, Object> map112 = new HashMap<String, Object>();
        map112.put("invname", "BVR 1.5");
        map112.put("rinfo", 3);
        prTypemaplist.add(map112);

        HashMap<String, Object> c1 = new HashMap<String, Object>();
        c1.put("invname", "普通");
        c1.put("rinfo", 1);
        baozhuangTypemaplist.add(c1);
        HashMap<String, Object> c2 = new HashMap<String, Object>();
        c2.put("invname", "精品");
        c2.put("rinfo", 2);
        baozhuangTypemaplist.add(c2);

        HashMap<String, Object> b1 = new HashMap<String, Object>();
        b1.put("invname", "红");
        b1.put("rinfo", 1);
        colormaplist.add(b1);
        HashMap<String, Object> b2 = new HashMap<String, Object>();
        b2.put("invname", "红/黑");
        b2.put("rinfo", 2);
        colormaplist.add(b2);
        HashMap<String, Object> b3 = new HashMap<String, Object>();
        b3.put("invname", "黑");
        b3.put("rinfo", 3);
        colormaplist.add(b3);

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
    private ImageView scanImgView;
    private String billid,billcode,billdate;
    private List<dacdiaobodan1> orderdetailslist;
    private productBarCodeListAdapter mordergoodListAdapter;
    private final static int SCANNIN_GREQUEST_CODE = 101;
    private static Double sumnum,summny;
    private volatile Semaphore mSemaphore = new Semaphore(1);
    private AlertDialog dialog;

    //公共
    private Button commitBtn;
    private Button cancelBtn;
    private TextView titiltxt;
    private ImageView scanImgBtn;

    public void setListViewHeightBasedOnChildren(ListView listView) {
        ListAdapter listAdapter = listView.getAdapter();
        if (listAdapter == null) {
            return;
        }

        int totalHeight = 0;
        for (int i = 0; i < listAdapter.getCount(); i++) {
            View listItem = listAdapter.getView(i, null, listView);
            listItem.measure(0, 0);  // 获取item高度
            totalHeight += listItem.getMeasuredHeight();
        }

        ViewGroup.LayoutParams params = listView.getLayoutParams();
        // 最后再加上分割线的高度和padding高度，否则显示不完整。
        params.height = totalHeight + (listView.getDividerHeight() * (listAdapter.getCount() - 1))+listView.getPaddingTop()+listView.getPaddingBottom();
        listView.setLayoutParams(params);
    }


}
