package com.heke.rihappclient.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
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
import com.heke.rihappclient.model.KL_outstore_model_detail;
import com.heke.rihappclient.model.rukuinfo;
import com.heke.rihappclient.net.okgo.JsonCallback;
import com.heke.rihappclient.net.okgo.LslResponse;
import com.heke.rihappclient.shareprefence.ShareprefenceBean;
import com.heke.rihappclient.utils.UIUtil;
import com.lzy.okgo.exception.OkGoException;

import java.math.BigDecimal;
import java.net.ConnectException;
import java.net.SocketTimeoutException;
import java.text.DecimalFormat;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Semaphore;

import okhttp3.Call;
import okhttp3.Response;

public class PandianStoreDetailActivity extends BaseActivity implements View.OnClickListener{

    private String billid;
    private TextView invcodeTxtView;
    private LinearLayout invcodelayout;
    private String goodsid;
    private String token;
    private KL_outstore_model_detail binfo=new KL_outstore_model_detail();
    private TextView batchCode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pandianstore_details);
        this.mSharedPreferences = getSharedPreferences(ShareprefenceBean.SHAREPREFENCE_NAME, 0);
        this.token=mSharedPreferences.getString(ShareprefenceBean.TOKEN,"");
        this.currentuser = AppService.getInstance().getCurrentUser();
        clientid=AppService.getInstance().getDeviceId(this);
        this.initView();
        this.init();
    }
    public void init() {
        this.setTextNull();
        Intent localIntent = getIntent();
        this.billid = localIntent.getStringExtra("billid");
        this.storename = localIntent.getStringExtra("storename");
        this.billcode=localIntent.getStringExtra("billcode");
        this.billcodeTxt.setText(billcode);
        this.storenameTxt.setText(storename);
        this.invcodemaplist = new ArrayList<Map<String, Object>>();
        this.barcodelist = new ArrayList<String>();
    }
    public void initView() {
        this.backImgBtn = ((ImageButton) findViewById(R.id.back));
        this.backImgBtn.setOnClickListener(this);
        this.billcodeTxt=(TextView)findViewById(R.id.billcode);
        this.storenameTxt=(TextView)findViewById(R.id.storename);
        this.pd_barcode=(AutoCompleteTextView)findViewById(R.id.pd_barcode);
        this.goodScope=(TextView)findViewById(R.id.goodScope);
        this.goodsname=(TextView)findViewById(R.id.goodsname);
        this.storeArea=(TextView)findViewById(R.id.storeArea);
        this.batchCode=(TextView)findViewById(R.id.batchCode);
        this.yipandnum=(TextView)findViewById(R.id.yipandnum);
        this.pandnum=(EditText)findViewById(R.id.pandnum);
        this.jiahao=(ImageButton)findViewById(R.id.jiahao);
        this.jianhao=(ImageButton)findViewById(R.id.jianhao);
        this.invcodeTxtView = (TextView) findViewById(R.id.invcode);
        this.invcodelayout = ((LinearLayout)findViewById(R.id.invcodelayout));
        this.jiahao.setOnClickListener(this);
        this.jianhao.setOnClickListener(this);
        this.pandnum.setOnClickListener(this);
        this.invcodelayout.setOnClickListener(this);

        //公共
        this.commitBtn = ((Button) findViewById(R.id.commitBtn));
        this.cancelBtn = ((Button) findViewById(R.id.cancelBtn));
        this.titiltxt=(TextView)findViewById(R.id.titletxt);
        this.titiltxt.setText("盘点");
        this.cancelBtn.setText("扫码");
        this.commitBtn.setOnClickListener(this);
        this.cancelBtn.setOnClickListener(this);
        this.scanImgBtn=(ImageView)findViewById(R.id.scanImgBtn);
        this.scanImgBtn.setOnClickListener(this);
        scanImgBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent it = new Intent(PandianStoreDetailActivity.this, CaptureActivity.class);
                startActivityForResult(it, SCANNIN_GREQUEST_CODE);
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
                    pd_barcode.setText(result);
                    loaddata(result);
                } else {
                    this.setTextNull();
                    UIUtil.showToast("提示：没有该标签入库信息");
                }

            } else if (resultCode == Activity.RESULT_CANCELED) {
                this.setTextNull();
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    private void setTextNull() {
        goodsid = "";
        pd_barcode.setText("");
        invcodeTxtView.setText("");
        goodsname.setText("");
        storeArea.setText("");
        batchCode.setText("");
        goodScope.setText("");
        yipandnum.setText("0");
        pandnum.setText("0");
    }

    public void onClick(View paramView) {
        switch (paramView.getId()) {
            case R.id.back:
                Intent intent = new Intent();
                intent.putExtra("result", "OK");
                setResult(1001, intent);
                this.finish();
                break;
            case R.id.cancelBtn:
                Intent it = new Intent(PandianStoreDetailActivity.this, CaptureActivity.class);
                startActivityForResult(it, SCANNIN_GREQUEST_CODE);
                break;
            case R.id.invcodelayout:
                this.loadinvoid();
                break;
            case R.id.commitBtn:
                String pandiannum=pandnum.getText().toString();
                if(binfo==null){
                    Toast.makeText(this,"请先选择盘点商品！",Toast.LENGTH_SHORT).show();
                    return;
                }
                if (TextUtils.isEmpty(pandiannum) ||  Double.parseDouble(pandiannum)<=0) {
                    Toast.makeText(this, "请输入盘点数量!", Toast.LENGTH_SHORT).show();
                    return;
                }
                String pandianoldnum=this.yipandnum.getText().toString();
                BigDecimal yipannum=new BigDecimal(pandianoldnum);
                BigDecimal pandiann=new BigDecimal(pandiannum);
                int x=yipannum.compareTo(BigDecimal.ZERO);
                if(x==0){
                    final double allnum=Double.parseDouble(pandiannum);
                    new AlertDialog.Builder(this)
                            .setMessage("本次盘点数量合计为["+allnum+"] 是否确认保存？")
                            .setPositiveButton("确定",
                                    new DialogInterface.OnClickListener() {

                                        @Override
                                        public void onClick(DialogInterface dialog,
                                                            int which) {
                                            commitdata(binfo.billItemId,allnum);
                                            dialog.dismiss();
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
                }else{
                    final double allnum=pandiann.add(yipannum).doubleValue();
                    new AlertDialog.Builder(this)
                            .setMessage("此批次号由["+binfo.pandianperson+"]在["+strToDatestr(binfo.JInventoryTime)+"]已做过数量为["+pandianoldnum+"]的盘点操作。您若确认保存盘点数量将增加本次盘点数量，合计为["+allnum+"] 是否确认保存？")
                            .setPositiveButton("确定",
                                    new DialogInterface.OnClickListener() {

                                        @Override
                                        public void onClick(DialogInterface dialog,
                                                            int which) {
                                            commitdata(binfo.billItemId,allnum);
                                            dialog.dismiss();
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
                }
                break;
            case R.id.jiahao:
                if (this.pandnum.getText().toString().equals("")) {
                    this.pandnum.setText("1");
                }
                int d2 = Integer.parseInt(this.pandnum.getText().toString());
                this.pandnum.setText(String.valueOf(d2+1));
                break;
            case R.id.jianhao:
                if (this.pandnum.getText().toString().equals("")) {
                    this.pandnum.setText("0");
                    return;
                }
                int d1 = Integer.parseInt(this.pandnum.getText().toString());
                if (d1 >= 1) {
                    this.pandnum.setText(String.valueOf(d1-1));
                    return;
                }
                Toast.makeText(this, "数量不能为负数", Toast.LENGTH_SHORT).show();
                break;
        }
    }

    private void loadinvoid() {
        if (this.invcodemaplist.isEmpty())
        {
            Toast.makeText(this, "物料信息还在获取，稍后再试！", Toast.LENGTH_SHORT).show();
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
                    binfo=(KL_outstore_model_detail)localMap.get("rinfo") ;
                    PandianStoreDetailActivity.this.goodsid = binfo.goodsid;
                    PandianStoreDetailActivity.this.invcodeTxtView.setText(binfo.goodscode);
                    PandianStoreDetailActivity.this.goodsname.setText(binfo.goodsname);
                    PandianStoreDetailActivity.this.goodScope.setText(binfo.jsStandard);
                    PandianStoreDetailActivity.this.storeArea.setText(binfo.storeP);
                    PandianStoreDetailActivity.this.batchCode.setText(binfo.batchcode);
                    PandianStoreDetailActivity.this.yipandnum.setText(binfo.yipannumber==null?"0":binfo.yipannumber);
                    BigDecimal yipannum=new BigDecimal(binfo.yipannumber);
                    int x=yipannum.compareTo(BigDecimal.ZERO);
                    if(binfo.yipannumber!=null&& x==1){
                        Toast.makeText(PandianStoreDetailActivity.this,"此批次号由["+binfo.pandianperson+"]在["+strToDatestr(binfo.JInventoryTime)+"]已做过数量为["+binfo.yipannumber+"]的盘点操作",Toast.LENGTH_LONG).show();
                    }
                    PandianStoreDetailActivity.this.pandnum.setText("0");
                    PandianStoreDetailActivity.this.dialog.dismiss();
                }
            });
            localBuilder.setView(localView);
            this.dialog = localBuilder.create();
            this.dialog.show();
            // return;
        }
        catch (InterruptedException localInterruptedException)
        {
            while (true)
                localInterruptedException.printStackTrace();
        }
    }

    public void loaddata(String barcode) {
        this.invcodelist=new ArrayList<>();
        this.invcodelist.clear();
        showLoading(this, "正在加载数据...");
        AppService.getInstance().getpandisnstoregoodslistAsync(token, billid, barcode, new JsonCallback<LslResponse<List<KL_outstore_model_detail>>>() {
            @Override
            public void onSuccess(LslResponse<List<KL_outstore_model_detail>> infoLslResponse, Call call, Response response) {
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
    void setinvcode(List<KL_outstore_model_detail> list){
        invcodemaplist.clear();
        if(list.size()>0) {
            for (int i = 0; i < list.size(); i++) {
                HashMap<String, Object> map = new HashMap<String, Object>();
                KL_outstore_model_detail rinfo = list.get(i);
                String invname = rinfo.goodscode + "/" + rinfo.goodsname + "/" + rinfo.jsStandard+ "/" + rinfo.storeP+"/" + rinfo.batchcode;
                map.put("invname", invname);
                map.put("rinfo", rinfo);
                invcodemaplist.add(map);
            }
            if(list.size()==1){
                binfo = list.get(0);
                if (binfo != null) {
                    goodsid = binfo.goodsid;
                    PandianStoreDetailActivity.this.invcodeTxtView.setText(binfo.goodscode);
                    PandianStoreDetailActivity.this.goodsname.setText(binfo.goodsname);
                    PandianStoreDetailActivity.this.goodScope.setText(binfo.jsStandard);
                    this.storeArea.setText(binfo.storeP);
                    this.batchCode.setText(binfo.batchcode);
                    PandianStoreDetailActivity.this.yipandnum.setText(binfo.yipannumber==null?"0":binfo.yipannumber);
                    BigDecimal yipannum=new BigDecimal(binfo.yipannumber);
                    int x=yipannum.compareTo(BigDecimal.ZERO);
                    if(binfo.yipannumber!=null&& x==1){
                        Toast.makeText(this,"此批次号由["+binfo.pandianperson+"]在["+strToDatestr(binfo.JInventoryTime)+"]已做过数量为["+binfo.yipannumber+"]的盘点操作",Toast.LENGTH_LONG).show();
                    }
                    PandianStoreDetailActivity.this.pandnum.setText("0");
                }
            }
            else{
                this.loadinvoid();
            }

        }
    }


    //提交
    public void commitdata(String billItemid,double allnum) {
        AppService.getInstance().commitpandianItemAsyn(token,this.currentuser.userid,billItemid,allnum, new JsonCallback<LslResponse<Object>>() {
            @Override
            public void onSuccess(LslResponse<Object> userLslResponse, Call call, Response response) {
                stopLoading();
                if (userLslResponse.code == LslResponse.RESPONSE_OK) {
                    binfo=null;
                    invcodemaplist.clear();
                    setTextNull();
                    UIUtil.showToast("提交成功！");
                } else {
                    UIUtil.showToast(userLslResponse.msg);
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
    private ImageButton backImgBtn;
    private List<KL_outstore_model_detail> invcodelist;
    private List<Map<String, Object>> invcodemaplist;
    private volatile Semaphore mSemaphore = new Semaphore(1);
    private AlertDialog dialog;
    private final static int SCANNIN_GREQUEST_CODE = 101;
    private String billcode,storename,kuweiname;
    public ArrayAdapter<String> aAdapter;
    public List<String> barcodelist;
    //private ImageView tipImage;
    private Button mTitleCommit;
    private TextView billcodeTxt;
    private TextView storenameTxt;
    private AutoCompleteTextView pd_barcode;
    private TextView  goodScope;
    private TextView goodsname;
    private TextView storeArea;
    private TextView yipandnum;
    private EditText pandnum;
    private ImageButton jiahao;
    private ImageButton jianhao;


    //公共
    private Button commitBtn;
    private Button cancelBtn;
    private TextView titiltxt;
    private ImageView scanImgBtn;
    /**
     * 将长时间格式字符串转换为时间 yyyy-MM-dd HH:mm:ss
     *
     * @param strDate
     * @return
     */
    public static String strToDatestr(String strDate) {
        String str=strDate.replace("T"," ");
        return str;
    }

}
