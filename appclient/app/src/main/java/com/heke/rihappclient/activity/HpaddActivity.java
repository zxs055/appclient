package com.heke.rihappclient.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.heke.rihappclient.R;
import com.heke.rihappclient.WebService.AppService;
import com.heke.rihappclient.adapter.invListViewAdapter;
import com.heke.rihappclient.application.BaseActivity;
import com.heke.rihappclient.model.rukuinfo;
import com.heke.rihappclient.net.NetworkStateService;
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

public class HpaddActivity extends BaseActivity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hpadd);
        this.mSharedPreferences = getSharedPreferences(ShareprefenceBean.SHAREPREFENCE_NAME, 0);
        this.currentuser = AppService.getInstance().getCurrentUser();
        clientid = AppService.getInstance().getDeviceId(this);
        this.initView();
        this.init();
    }

    public void init() {
        this.setTextNull();
        Intent localIntent = getIntent();
        this.djid = localIntent.getStringExtra("djid");
        this.dh = localIntent.getStringExtra("dh");
        this.billdate = localIntent.getStringExtra("billdate");
        billcodeTxtView.setText(dh);
this.storenameExt.setText(this.currentuser.storename);
        this.invcodemaplist = new ArrayList<Map<String, Object>>();
        this.barcodelist = new ArrayList<String>();
    }

    public void initView() {
        this.backImgBtn = ((ImageButton) findViewById(R.id.back));
        this.commitBtn = ((Button) findViewById(R.id.commitBtn));
        this.cancelBtn = ((Button) findViewById(R.id.cancelBtn));
        this.billcodeTxtView = (TextView) findViewById(R.id.billcode);
        this.invcodeTxtView = (TextView) findViewById(R.id.invcode);
        this.invnameTxtView = (TextView) findViewById(R.id.invname);
        this.invspecTxtView = (TextView) findViewById(R.id.invspec);
        this.kucnumTxtView = (TextView) findViewById(R.id.kucunnum);
        this.barcodeTxtView=(AutoCompleteTextView)findViewById(R.id.barcode);
        this.pandnumExt = (EditText) findViewById(R.id.pandnum);
        //this.pandpriceExt = (EditText) findViewById(R.id.pandprice);
        this.scanImgBtn = ((ImageView) findViewById(R.id.scanImgBtn));
        this.jiahao=((ImageButton)findViewById(R.id.jiahao));
        this.jianhao=((ImageButton)findViewById(R.id.jianhao));
        this.storenameExt = ((TextView)findViewById(R.id.storename));
        this.invcodelayout = ((LinearLayout)findViewById(R.id.invcodelayout));
        this.yipandnumExt=(TextView)findViewById(R.id.yipandnum);
        this.jianhao.setOnClickListener(this);
        this.jiahao.setOnClickListener(this);
        this.scanImgBtn.setOnClickListener(this);
        this.backImgBtn.setOnClickListener(this);
        this.commitBtn.setOnClickListener(this);
        this.cancelBtn.setOnClickListener(this);
        this.invcodelayout.setOnClickListener(this);
        scanImgBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent it = new Intent(HpaddActivity.this, CaptureActivity.class);
                startActivityForResult(it, SCANNIN_GREQUEST_CODE);
            }
        });
        barcodeTxtView.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId,
                                          KeyEvent event) {
                if ((actionId == EditorInfo.IME_ACTION_SEARCH || actionId == EditorInfo.IME_ACTION_UNSPECIFIED)  && event != null) {
                    // 先隐藏键盘
                    ((InputMethodManager) barcodeTxtView.getContext()
                            .getSystemService(Context.INPUT_METHOD_SERVICE))
                            .hideSoftInputFromWindow(HpaddActivity.this
                                            .getCurrentFocus().getWindowToken(),
                                    InputMethodManager.HIDE_NOT_ALWAYS);
                    String keytag = barcodeTxtView.getText().toString().trim();
                    if (TextUtils.isEmpty(keytag)) {
                        UIUtil.showToast("提示：请输入条码信息");
                        return false;
                    }
                    loaddata(keytag);
                   // return true;
                }
                return false;
            }
        });
        barcodeTxtView.addTextChangedListener(new TextWatcher(){

            public void afterTextChanged(Editable editable) {

            }

            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String newText = s.toString();
                new getJson().execute(newText);
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
                    loaddata(result);
                } else {
                    this.setTextNull();
                    UIUtil.showToast("提示：没有该标签入库信息");
                }

            } else if (resultCode == Activity.RESULT_CANCELED) {
                this.setTextNull();
            }
        } else if (requestCode == 100) {
            if (data != null) {
                //UIUtil.showToast(data.getStringExtra("dh"));
                this.dh = data.getStringExtra("dh");
                this.djid = data.getStringExtra("djid");
                this.billdate=data.getStringExtra("billdate");

            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    public void onClick(View paramView) {
        Intent localIntent = new Intent();
        switch (paramView.getId()) {
            case R.id.back:
                Intent intent = new Intent();
                intent.putExtra("result", "OK");
                setResult(1001, intent);
                this.finish();
                break;
            case R.id.commitBtn:
                this.SaveForm();
                break;
            case R.id.cancelBtn:
                localIntent.setClass(this,PandiandetailsListActivity.class);
                localIntent.putExtra("billcode", this.dh);
                localIntent.putExtra("billid", this.djid);
                localIntent.putExtra("billdate",this.billdate);
                startActivity(localIntent);
                break;
            case R.id.jiahao:
                if (this.pandnumExt.getText().toString().equals("")) {
                    this.pandnumExt.setText("1");
                }
                int d2 = Integer.parseInt(this.pandnumExt.getText().toString());
                this.pandnumExt.setText(String.valueOf(d2+1));
                break;
            case R.id.jianhao:
                if (this.pandnumExt.getText().toString().equals("")) {
                    this.pandnumExt.setText("0");
                    return;
                }
                int d1 = Integer.parseInt(this.pandnumExt.getText().toString());
                if (d1 >= 1) {
                    this.pandnumExt.setText(String.valueOf(d1-1));
                    return;
                }
                Toast.makeText(this, "数量不能为负数", Toast.LENGTH_SHORT).show();
                break;
            case R.id.invcodelayout:
                this.loadinvoid();
                break;

        }
    }
void loadinvoid(){
    if (this.invcodemaplist.isEmpty())
    {
        Toast.makeText(this, "商品信息还在获取，稍后再试！", Toast.LENGTH_SHORT).show();
        return;
    }
    AlertDialog.Builder localBuilder = new AlertDialog.Builder(this);
    View localView = LayoutInflater.from(this).inflate(R.layout.popupwindow_list, null);
    ListView localListView = (ListView)localView.findViewById(R.id.popuplist);
    invListViewAdapter localCkListViewAdapter = new invListViewAdapter(this);
    try
    {
        this.mSemaphore.acquire();
        localCkListViewAdapter.setData(this.invcodemaplist);
        this.mSemaphore.release();
        localListView.setAdapter(localCkListViewAdapter);
        localListView.setOnItemClickListener(new AdapterView.OnItemClickListener()
        {
            public void onItemClick(AdapterView<?> paramAdapterView, View paramView, int paramInt, long paramLong)
            {
                Map localMap = (Map)((Adapter)paramAdapterView.getAdapter()).getItem(paramInt);
                rukuinfo binfo=(rukuinfo)localMap.get("rinfo") ;
                HpaddActivity.this.invcodeTxtView.setText(localMap.get("invname").toString());
                HpaddActivity.this.invid = binfo.invid;
                HpaddActivity.this.invcodeTxtView.setText(binfo.invcode);
                HpaddActivity.this.invnameTxtView.setText(binfo.invname);
                HpaddActivity.this.invspecTxtView.setText(binfo.invspec);
                HpaddActivity.this.kucnumTxtView.setText(binfo.kucnum==null?"0":binfo.kucnum);
                HpaddActivity.this.pandnumExt.setText("0");
                HpaddActivity.this.yipandnumExt.setText(binfo.quantity);
                //HpaddActivity.this.pandpriceExt.setText(binfo.cost);
                HpaddActivity.this.dialog.dismiss();
            }
        });
        localBuilder.setView(localView);
        this.dialog = localBuilder.create();
        this.dialog.show();
       // return;
    }
    catch (InterruptedException localInterruptedException)
    {
            localInterruptedException.printStackTrace();
    }
}
    public void loaddata(String barcode) {
        this.invcodelist=new ArrayList<>();
        showLoading(this, "正在加载数据...");
        AppService.getInstance().getInvdocpdlistAsync(this.currentuser.userid, clientid, barcode,dh, new JsonCallback<LslResponse<List<rukuinfo>>>() {
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
                String invname = rinfo.invname + "/" + rinfo.invspec;
                map.put("invname", invname);
                map.put("rinfo", rinfo);
                invcodemaplist.add(map);
            }
            if(list.size()==1){
                rukuinfo binfo = list.get(0);
                if (binfo != null) {
                    invid = binfo.invid;
                    invcodeTxtView.setText(binfo.invcode);
                    invnameTxtView.setText(binfo.invname);
                    invspecTxtView.setText(binfo.invspec);
                    kucnumTxtView.setText(binfo.kucnum==null?"0":binfo.kucnum);
                    pandnumExt.setText("0");
                    yipandnumExt.setText(binfo.quantity);
                    //pandpriceExt.setText(binfo.cost);
                }
            }
            else{
               this.loadinvoid();
            }

        }
    }
    void SaveForm()
    {
        if (!NetworkStateService.isNetworkAvailable(this)) {
            UIUtil.showToast("网络连接不可用，请检查！");
            return;
        }
        if (TextUtils.isEmpty(djid)) {
            Toast.makeText(this, "请选择盘点单据!", Toast.LENGTH_SHORT).show();
            return;
        }
        if (TextUtils.isEmpty(invid)) {
            Toast.makeText(this, "请扫码盘点商品!", Toast.LENGTH_SHORT).show();
            return;
        }
        String ypdnum=this.yipandnumExt.getText().toString();
        String pdnum = this.pandnumExt.getText().toString();
        //String pdprice=this.pandpriceExt.getText().toString();
        if (TextUtils.isEmpty(pdnum) ||  Double.parseDouble(pdnum)<0) {
            Toast.makeText(this, "请输入商品盘点数量!", Toast.LENGTH_SHORT).show();
            return;
        }
        /*if (TextUtils.isEmpty(pdprice)) {
            Toast.makeText(this, "请输入商品盘点单价!", Toast.LENGTH_SHORT).show();
            return;
        }*/
        if (this.currentuser == null) {
            Toast.makeText(this, "当前用户信息加载失败，请重新登录!", Toast.LENGTH_SHORT).show();
            return;
        }
        String userid = this.currentuser.userid;
        String storeid = this.currentuser.storeid;
        if (TextUtils.isEmpty(userid)) {
            Toast.makeText(this, "当前用户信息加载失败，请重新登录!", Toast.LENGTH_SHORT).show();
            return;
        }
        if (TextUtils.isEmpty(storeid)) {
            Toast.makeText(this, "当前用户所属的门店信息加载失败，请联系管理员!", Toast.LENGTH_SHORT).show();
            return;
        }
        this.showLoading(this, "正在提交数据……");
        //Double sumnum=Double.parseDouble(pdnum)+Double.parseDouble(ypdnum);
        rukuinfo pd = new rukuinfo();
        pd.billid = this.djid;
        pd.invid = invid;
        pd.quantity = pdnum;
        pd.price="0";
        pd.kucnum=this.kucnumTxtView.getText().toString();
        pd.userid = userid;
        pd.storeid=storeid;
        pd.clientid = clientid;
        AppService.getInstance().SavePandianbodyAsync(pd,clientid, new JsonCallback<LslResponse<Object>>() {
            @Override
            public void onSuccess(LslResponse<Object> userLslResponse, Call call, Response response) {
                stopLoading();
                if (userLslResponse.code == LslResponse.RESPONSE_OK) {
                    setTextNull();
                    UIUtil.showToast("商品盘点提交成功，请继续扫码盘点商品！");
                } else {
                    UIUtil.showToast(userLslResponse.msg);
                }
            }
        });
    }

    void setTextNull() {
        invid = "";
        barcodeTxtView.setText("");
        invcodeTxtView.setText("");
        invnameTxtView.setText("");
        invspecTxtView.setText("");
        kucnumTxtView.setText("0");
        pandnumExt.setText("0");
        //pandpriceExt.setText("0");
    }

    private ImageButton backImgBtn;
    private Button commitBtn;
    private Button cancelBtn;
    private TextView billcodeTxtView;
    private TextView invcodeTxtView;
    private TextView invnameTxtView;
    private TextView invspecTxtView;
    private TextView kucnumTxtView;
    private AutoCompleteTextView barcodeTxtView;
    private EditText pandnumExt;
    //private EditText pandpriceExt;
    private TextView yipandnumExt;
    private ImageView scanImgBtn;
    private ImageButton jiahao;
    private ImageButton jianhao;
    private TextView storenameExt;
    private LinearLayout invcodelayout;
    private ProgressDialog pro_Dialog;
    private List<rukuinfo> invcodelist;
    private List<Map<String, Object>> invcodemaplist;
    private volatile Semaphore mSemaphore = new Semaphore(1);
    private AlertDialog dialog;
    private final static int SCANNIN_GREQUEST_CODE = 101;
    private String dh = "", djid = "", invid="",billdate="";
    public ArrayAdapter<String> aAdapter;
    public List<String> barcodelist;
    //private static String billcode = "", billid = "";

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
                AppService.getInstance().getpandbarcodelistAsync(currentuser.userid, clientid, newText, new JsonCallback<LslResponse<List<rukuinfo>>>() {
                    @Override
                    public void onSuccess(LslResponse<List<rukuinfo>> infoLslResponse, Call call, Response response) {

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
