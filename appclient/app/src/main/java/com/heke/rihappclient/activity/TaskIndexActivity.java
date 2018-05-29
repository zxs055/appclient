package com.heke.rihappclient.activity;


import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.heke.rihappclient.R;
import com.heke.rihappclient.WebService.AppService;
import com.heke.rihappclient.adapter.SelectOperatorAdapter;
import com.heke.rihappclient.adapter.SelectOpreatorShebeiAdapter;
import com.heke.rihappclient.application.BaseActivity;
import com.heke.rihappclient.model.baseinfo;
import com.heke.rihappclient.model.dacdiaobodan1;
import com.heke.rihappclient.net.okgo.JsonCallback;
import com.heke.rihappclient.net.okgo.LslResponse;
import com.heke.rihappclient.shareprefence.ShareprefenceBean;
import com.heke.rihappclient.utils.UIUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Semaphore;

import okhttp3.Call;
import okhttp3.Response;

public class TaskIndexActivity extends BaseActivity implements View.OnClickListener,AdapterView.OnItemClickListener {

    private String systemtype;
    private EditText selectEtxt;
    private String banciId;
    private String prodectcenterId;
    private String token;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.task_index);
        this.mSharedPreferences = getSharedPreferences(ShareprefenceBean.SHAREPREFENCE_NAME, 0);
        this.token=mSharedPreferences.getString(ShareprefenceBean.TOKEN,"");
        this.currentuser = AppService.getInstance().getCurrentUser();
        clientid = AppService.getInstance().getDeviceId(this);

        Intent localIntent = getIntent();
        this.systemtype = localIntent.getStringExtra("systemtype");
        this.initView();
        this.init();
    }

    public void init() {
        shebeilist=new ArrayList<baseinfo>();
        this.bancimaplist = new ArrayList<baseinfo>();
        this.prodectcentermaplist = new ArrayList<baseinfo>();
        loadbanciList();
        loadprodectcenterList();
    }

    public void initView() {
        this.mListView = (ListView) findViewById(R.id.listView);
        this.commitBtn = ((Button) findViewById(R.id.commitBtn));
        this.cancelBtn = ((Button) findViewById(R.id.cancelBtn));
        this.banci_layout = ((LinearLayout)findViewById(R.id.banci_layout));
        this.prodectcenter_layout=(LinearLayout)findViewById(R.id.prodectcenter_layout);
        this.banciTxt=(TextView)findViewById(R.id.banciTxt);
        this.prodectcenterTxt=(TextView)findViewById(R.id.prodectcenterTxt);
        this.selectEtxt=(EditText)findViewById(R.id.selecttxt);
        backImgBtn=(ImageButton)findViewById(R.id.back);
        this.titiltxt=(TextView)findViewById(R.id.titletxt);
        if(systemtype.equals("1")){
            this.titiltxt.setText("数字车间");
        }else if(systemtype.equals("2")){
            this.titiltxt.setText("质检管理");
        }else if(systemtype.equals("3")){
            this.titiltxt.setText("车间发货");
        }

        this.backImgBtn.setOnClickListener(this);
        this.commitBtn.setOnClickListener(this);
        this.cancelBtn.setOnClickListener(this);
        this.banci_layout.setOnClickListener(this);
        this.prodectcenter_layout.setOnClickListener(this);


        this.scanImgBtn=(ImageView)findViewById(R.id.scanImgBtn);
        this.scanImgBtn.setOnClickListener(this);
        scanImgBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent it = new Intent(TaskIndexActivity.this, CaptureActivity.class);
                startActivityForResult(it, SCANNIN_GREQUEST_CODE);
            }
        });

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
                    ArrayList<baseinfo> fileterList = (ArrayList<baseinfo>) search(content);
                    mListAdapter.updateListView(fileterList);
                    //mAdapter.updateData(mContacts);
                }else {
                    mListAdapter.updateListView(shebeilist);
                }
            }
        });
    }

    private void inttoint() {
        List<baseinfo> list=mListAdapter.data;
        String processname="";
        String processid="";
        for(baseinfo model:list){
            if(model.ischeck()){
                processname+=model.basename;
                processid=model.baseid;
                break;
            }
        }

        if(banciId==null||banciId==""){
            Toast.makeText(this, "请选择班次", Toast.LENGTH_SHORT).show();
            return;
        }
        if(prodectcenterId==null||prodectcenterId==""){
            Toast.makeText(this, "请选择生产中心", Toast.LENGTH_SHORT).show();
            return;
        }
        if(processid==""||processname==""){
            Toast.makeText(this, "请选择设备", Toast.LENGTH_SHORT).show();
            return;
        }
        if(systemtype.equals("1")){
            Intent newInt=new Intent(this,TaskListActivity.class);
            newInt.putExtra("banci", banciId);
            newInt.putExtra("prodectcenter",prodectcenterId);
            newInt.putExtra("processname",processname);
            startActivity(newInt);
        }else if(systemtype.equals("2")){
            Intent newInt=new Intent(this,QC_DetailsActivity.class);
            startActivity(newInt);
        }else if(systemtype.equals("3")){
            Intent newInt=new Intent(this,PlantOutStoreListActivity.class);
            String banciname=this.banciTxt.getText().toString();
            String prodectcentername=this.prodectcenterTxt.getText().toString();
            newInt.putExtra("banci", banciId);
            newInt.putExtra("banciname",banciname);
            newInt.putExtra("prodectcentername",prodectcentername);
            newInt.putExtra("prodectcenter",prodectcenterId);
            newInt.putExtra("processname",processname);
            newInt.putExtra("processid",processid);
            startActivity(newInt);
            this.finish();
        }
    }

    //加载班次
    private void loadbanciList(){
        AppService.getInstance().getbancilistAsync( token,new JsonCallback<LslResponse<List<baseinfo>>>() {
            @Override
            public void onSuccess(LslResponse<List<baseinfo>> listLslResponse, Call call, Response response) {

                if (listLslResponse.code == LslResponse.RESPONSE_ERROR) {
                    UIUtil.showToast(listLslResponse.msg);
                } else {
                    bancimaplist.addAll(listLslResponse.data);
                }
            }
        });
    }
    //加载生产车间
    private void loadprodectcenterList(){
        AppService.getInstance().getproductcenterlistAsync(token, new JsonCallback<LslResponse<List<baseinfo>>>() {
            @Override
            public void onSuccess(LslResponse<List<baseinfo>> listLslResponse, Call call, Response response) {
                if (listLslResponse.code == LslResponse.RESPONSE_ERROR) {
                    UIUtil.showToast(listLslResponse.msg);
                } else {
                    prodectcentermaplist.addAll(listLslResponse.data);
                }
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
                    if (result.length() > 0) {
                        //ArrayList<dacdiaobodan1> fileterList = (ArrayList<dacdiaobodan1>) search(result);
                        //mListAdapter.updateListView(fileterList);
                    } else {
                        //mListAdapter.updateListView(shebeilist);
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
            case R.id.banci_layout:
                loadbanci();
                break;
            case R.id.prodectcenter_layout:
                loadprodectcenter();
                break;
            case R.id.commitBtn:
                inttoint();
                break;
            case R.id.cancelBtn:
                this.finish();
                break;
        }
    }

    private void loadprodectcenter() {
        if (prodectcentermaplist != null && prodectcentermaplist.size() > 0) {
            String[] arrayOfString = new String[this.prodectcentermaplist.size()];
            for(int i=0;i<this.prodectcentermaplist.size();i++){
                arrayOfString[i] = prodectcentermaplist.get(i).basename;
            }
            View localView = LayoutInflater.from(this).inflate(R.layout.popupwindow_list, null);
            ListView localListView = (ListView) localView.findViewById(R.id.popuplist);
            ArrayAdapter localArrayAdapter = new ArrayAdapter(this, R.layout.popupwindow_list_textview, arrayOfString);
            localListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                public void onItemClick(AdapterView<?> paramAdapterView, View paramView, int paramInt, long paramLong) {
                    TaskIndexActivity.this.prodectcenterTxt.setText(TaskIndexActivity.this.prodectcentermaplist.get(paramInt).basename);
                    TaskIndexActivity.this.prodectcenterId = TaskIndexActivity.this.prodectcentermaplist.get(paramInt).baseid;
                    TaskIndexActivity.this.mserverPopupWindow.dismiss();
                    changeshebeilist(TaskIndexActivity.this.prodectcenterId);
                }
            });
            localListView.setAdapter(localArrayAdapter);
            this.mserverPopupWindow = new PopupWindow(localView, this.prodectcenterTxt.getWidth(), -2);
            this.mserverPopupWindow.setBackgroundDrawable(new BitmapDrawable());
            this.mserverPopupWindow.setFocusable(true);
            this.mserverPopupWindow.setOutsideTouchable(true);
            this.mserverPopupWindow.update();
            this.mserverPopupWindow.showAsDropDown(this.prodectcenterTxt);

        } else {
            Toast.makeText(this, "数据正在获取，请稍后！", Toast.LENGTH_LONG).show();
        }
    }

    private void loadbanci() {
        if (bancimaplist != null && bancimaplist.size() > 0) {
            String[] arrayOfString = new String[this.bancimaplist.size()];
            for(int i=0;i<this.bancimaplist.size();i++){
                arrayOfString[i] = bancimaplist.get(i).basename;
            }
            View localView = LayoutInflater.from(this).inflate(R.layout.popupwindow_list, null);
            ListView localListView = (ListView) localView.findViewById(R.id.popuplist);
            ArrayAdapter localArrayAdapter = new ArrayAdapter(this, R.layout.popupwindow_list_textview, arrayOfString);
            localListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                public void onItemClick(AdapterView<?> paramAdapterView, View paramView, int paramInt, long paramLong) {
                    TaskIndexActivity.this.banciTxt.setText(TaskIndexActivity.this.bancimaplist.get(paramInt).basename);
                    TaskIndexActivity.this.banciId = TaskIndexActivity.this.bancimaplist.get(paramInt).baseid;
                    TaskIndexActivity.this.mserverPopupWindow.dismiss();

                }
            });
            localListView.setAdapter(localArrayAdapter);
            this.mserverPopupWindow = new PopupWindow(localView, this.banciTxt.getWidth(), -2);
            this.mserverPopupWindow.setBackgroundDrawable(new BitmapDrawable());
            this.mserverPopupWindow.setFocusable(true);
            this.mserverPopupWindow.setOutsideTouchable(true);
            this.mserverPopupWindow.update();
            this.mserverPopupWindow.showAsDropDown(this.banciTxt);

        } else {
            Toast.makeText(this, "数据正在获取，请稍后！", Toast.LENGTH_LONG).show();
        }
    }

    public void changeshebeilist(String pCenterID) {
        shebeilist.clear();
        showLoading(this, "正在加载数据...");
        AppService.getInstance().getshebeilistAsync( token,clientid,pCenterID, new JsonCallback<LslResponse<List<baseinfo>>>() {
            @Override
            public void onSuccess(LslResponse<List<baseinfo>> listLslResponse, Call call, Response response) {
                stopLoading();
                if (listLslResponse.code == LslResponse.RESPONSE_ERROR) {
                    UIUtil.showToast(listLslResponse.msg);
                } else if (listLslResponse.code == LslResponse.RESPONSE_OK) {
                    shebeilist=listLslResponse.data;
                    if(listLslResponse.data!=null) {
                        mListAdapter=new SelectOpreatorShebeiAdapter(shebeilist,TaskIndexActivity.this);
                        mListView.setAdapter(mListAdapter);
                    }
                } else {
                    UIUtil.showToast(listLslResponse.msg);
                }
            }
        });
    }


    /**
     * 模糊查询
     * @param str
     * @return
     */
    private List<baseinfo> search(String str) {
        List<baseinfo> filterList = new ArrayList<baseinfo>();//过滤后的list
        if (!str.isEmpty() && this.shebeilist!=null && this.shebeilist.size()>0) {//正则表达式 匹配号码
            for (baseinfo contact : this.shebeilist) {
                if (contact.basename != null) {
                    if (contact.basename.contains(str)) {
                        if (!filterList.contains(contact)) {
                            filterList.add(contact);
                        }
                    }
                }
            }
        }
        return filterList;
    }

    public void onItemClick(AdapterView<?> paramAdapterView, View paramView, int paramInt, long paramLong)
    {
    }

    private ListView mListView;
    private LinearLayout banci_layout;
    private LinearLayout prodectcenter_layout;
    private TextView banciTxt;
    private TextView prodectcenterTxt;
    private Button commitBtn;
    private Button cancelBtn;
    private ImageButton startImageB;
    private ImageButton backImgBtn;
    private TextView titiltxt;
    private String billid,billcode,billdate;
    private List<baseinfo> shebeilist;
    private SelectOpreatorShebeiAdapter mListAdapter;
    private final static int SCANNIN_GREQUEST_CODE = 101;
    private static Double sumnum,summny;
    private volatile Semaphore mSemaphore = new Semaphore(1);
    private List<baseinfo> bancimaplist;
    private List<baseinfo> prodectcentermaplist;
    private AlertDialog dialog;
    private ImageView scanImgBtn;
    private PopupWindow mserverPopupWindow;



}
