package com.heke.rihappclient.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.JsonSyntaxException;
import com.heke.rihappclient.R;
import com.heke.rihappclient.WebService.AppService;
import com.heke.rihappclient.adapter.invListViewAdapter;
import com.heke.rihappclient.application.BaseActivity;
import com.heke.rihappclient.helper.DBHelper.SQLiteHelper;
import com.heke.rihappclient.model.JL_APP_OutStoreDetail;
import com.heke.rihappclient.model.KL_outstore_model;
import com.heke.rihappclient.model.KL_outstore_model_detail;
import com.heke.rihappclient.net.okgo.JsonCallback;
import com.heke.rihappclient.net.okgo.LslResponse;
import com.heke.rihappclient.shareprefence.ShareprefenceBean;
import com.heke.rihappclient.utils.UIUtil;
import com.heke.rihappclient.view.SideslipListView;
import com.lzy.okgo.exception.OkGoException;

import java.net.ConnectException;
import java.net.SocketTimeoutException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Semaphore;

import okhttp3.Call;
import okhttp3.Response;

public class PandianStoreIndexActivity extends BaseActivity implements View.OnClickListener {
    private String name;
    private String JEmpName;
    private String token;
    private EditText selectEtxt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pandianstore_index);
        this.mSharedPreferences = getSharedPreferences(ShareprefenceBean.SHAREPREFENCE_NAME, 0);
        this.token=mSharedPreferences.getString(ShareprefenceBean.TOKEN,"");
        this.currentuser = AppService.getInstance().getCurrentUser();
        clientid = AppService.getInstance().getDeviceId(this);
        this.initView();
        Intent localIntent = getIntent();
        this.billcode = localIntent.getStringExtra("billcode");
        this.billid = localIntent.getStringExtra("billid");
        this.JEmpName = localIntent.getStringExtra("JEmpName");
        this.name = localIntent.getStringExtra("name");
        this.daohuoCodeTxt.setText(billcode);
        this.businessTxt.setText(name);
        this.bususerTxt.setText(JEmpName);
        this.loaddata();



    }

    public void initView() {
        this.mSideslipListView = (SideslipListView) findViewById(R.id.listView);
        this.pandianuser_layout = ((LinearLayout)findViewById(R.id.pandianuser_layout));
        this.pandianuserTxt=(TextView)findViewById(R.id.pandianuserTxt);
        this.pandianuser_layout.setOnClickListener(this);
        this.pandianuserList = new ArrayList<Map<String, Object>>();
        orderdetailslist=new ArrayList<KL_outstore_model_detail>();
        billlist=new ArrayList<String>();
        outstoredetaillist=new ArrayList<JL_APP_OutStoreDetail>();
        fileterList=new ArrayList<KL_outstore_model_detail>();
        this.daohuoCodeTxt=(TextView)findViewById(R.id.daohuoCodeTxt);
        this.businessTxt=(TextView)findViewById(R.id.businessTxt);
        this.bususerTxt=(TextView)findViewById(R.id.bususerTxt);

        //公共
        this.commitBtn = ((Button) findViewById(R.id.commitBtn));
        this.cancelBtn = ((Button) findViewById(R.id.cancelBtn));
        backImgBtn=(ImageButton)findViewById(R.id.back);
        this.titiltxt=(TextView)findViewById(R.id.titletxt);
        this.titiltxt.setText("盘点单明细");
        this.commitBtn.setText("开始盘点");
        this.backImgBtn.setOnClickListener(this);
        this.commitBtn.setOnClickListener(this);
        this.cancelBtn.setOnClickListener(this);
        this.scanImgBtn=(ImageView)findViewById(R.id.scanImgBtn);
        this.scanImgBtn.setOnClickListener(this);
        scanImgBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent it = new Intent(PandianStoreIndexActivity.this, CaptureActivity.class);
                startActivityForResult(it, SCANNIN_GREQUEST_CODE);
            }
        });
        this.selectEtxt=(EditText)findViewById(R.id.selecttxt);
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
                    fileterList.clear();
                    fileterList = (ArrayList<KL_outstore_model_detail>) search(content);
                    mListAdapter.updateListView(fileterList);
                }else {
                    mListAdapter.updateListView(orderdetailslist);
                }
            }
        });
    }

    private void inttoint() {
        outstoredetaillist.clear();
        billlist.clear();
        String userid=this.currentuser.userid;
        AppService.getInstance().commitInStroeListAsyn(token,userid,outstoredetaillist, new JsonCallback<LslResponse<Object>>() {
            @Override
            public void onSuccess(LslResponse<Object> userLslResponse, Call call, Response response) {
                stopLoading();
                if (userLslResponse.code == LslResponse.RESPONSE_OK) {
                    delectmSharedPre();
                    UIUtil.showToast("提交成功！");
                    PandianStoreIndexActivity.this.finish();
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
                }else if(e instanceof JsonSyntaxException){
                    UIUtil.showToast("认证码不存在");
                }
            }
        });
//        Toast.makeText(this,"提交成功！",Toast.LENGTH_SHORT).show();

    }

    private void delectmSharedPre() {

    }

    private void clearAll() {
        this.daohuoCodeTxt.setText("");
        this.businessTxt.setText("");
        this.bususerTxt.setText("");
        orderdetailslist.clear();
        mListAdapter.updateListView(orderdetailslist);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == SCANNIN_GREQUEST_CODE) {
            if (resultCode == Activity.RESULT_OK && data != null) {
                String result = data.getStringExtra("scan_result");
                Log.i("", "scan result:" + result);
                if (!result.equals("")) {
                    if (result.length() > 0) {
//                        View rootview = OutStoreIndexActivity.this.getWindow().getDecorView();
//                        View et = rootview.findFocus();
//                        if(et instanceof EditText){
//                            ((EditText)et).setText(result);
//                            businessTxt.setText("安徽金缆");
//                            bususerTxt.setText("张三");
//                            orderdetailslist.clear();
//                            loaddetail();
//                        }
                    } else {
                        //mListAdapter.updateListView(orderdetailslist);
                    }
                } else {
                    UIUtil.showToast("提示：没有该标签入库信息");
                }
            }
        }
        if (requestCode == 120 && resultCode==1001) {
            this.selectEtxt.setText("");
            loaddetail();//加载单据
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
                new AlertDialog.Builder(this)
                        .setMessage("是否开始盘点？")
                        .setPositiveButton("确定",
                                new DialogInterface.OnClickListener() {

                                    @Override
                                    public void onClick(DialogInterface dialog,
                                                        int which) {
                                        startPandian();
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
                break;
            case R.id.cancelBtn:
                new AlertDialog.Builder(this)
                        .setMessage("是否清除本单采集信息？")
                        .setPositiveButton("确定",
                                new DialogInterface.OnClickListener() {

                                    @Override
                                    public void onClick(DialogInterface dialog,
                                                        int which) {
                                        dialog.dismiss();
                                        canceldata();

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

    //开始盘点
    private void startPandian() {
        Intent newInt = new Intent(PandianStoreIndexActivity.this, PandianStoreDetailActivity.class);
        newInt.putExtra("billid", billid);
        newInt.putExtra("storename", name);
        newInt.putExtra("billcode",billcode);
        startActivityForResult(newInt, 120);
    }


    private void canceldata() {
        this.finish();
        Toast.makeText(this, "清除成功", Toast.LENGTH_SHORT).show();
    }

    //加載發貨明細
    private void loaddetail() {
        orderdetailslist.clear();
        showLoading(this, "正在加载数据...");
        AppService.getInstance().getpandianDetaillistAsync( token,billid,clientid, new JsonCallback<LslResponse<List<KL_outstore_model_detail>>>() {
            @Override
            public void onSuccess(LslResponse<List<KL_outstore_model_detail>> listLslResponse, Call call, Response response) {
                stopLoading();
                if (listLslResponse.code == LslResponse.RESPONSE_ERROR) {
                    UIUtil.showToast(listLslResponse.msg);
                } else if (listLslResponse.code == LslResponse.RESPONSE_OK) {
                    orderdetailslist=listLslResponse.data;
                    selectGatherQty(orderdetailslist);
                    if(listLslResponse.data!=null) {
                        mListAdapter=new InStoreListNewAdapter(PandianStoreIndexActivity.this,orderdetailslist);
                        mSideslipListView.setAdapter(mListAdapter);
                    }
                } else {
                    UIUtil.showToast(listLslResponse.msg);
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
                }else if(e instanceof JsonSyntaxException){
                    UIUtil.showToast("认证码不存在");
                }
            }
        });
    }

    private void selectGatherQty(List<KL_outstore_model_detail> list) {

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
                    PandianStoreIndexActivity.this.pandianuserTxt.setText(name);
                    PandianStoreIndexActivity.this.dialog.dismiss();
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
        loaddetail();
    }
    /**
     * 模糊查询
     * @param str
     * @return
     */
    private List<KL_outstore_model_detail> search(String str) {
        List<KL_outstore_model_detail> filterList = new ArrayList<KL_outstore_model_detail>();//过滤后的list
        if (!str.isEmpty() && this.orderdetailslist!=null && this.orderdetailslist.size()>0) {//正则表达式 匹配号码
            for (KL_outstore_model_detail contact : this.orderdetailslist) {
                if (contact.billItemId != null) {
                    if (contact.batchcode.contains(str)) {
                        if (!filterList.contains(contact)) {
                            filterList.add(contact);
                        }
                    }
                }
            }
        }
        return filterList;
    }

    private SideslipListView mSideslipListView;
    private LinearLayout pandianuser_layout;
    private TextView pandianuserTxt;
    private TextView daohuoCodeTxt;
    private TextView businessTxt;
    private TextView bususerTxt;

    private ImageButton startImageB;

    private String billcode,bususer,billid;
    private List<KL_outstore_model_detail> orderdetailslist;
    private List<KL_outstore_model_detail> fileterList;
    private InStoreListNewAdapter mListAdapter;
    private final static int SCANNIN_GREQUEST_CODE = 101;
    private static Double sumnum,summny;
    private volatile Semaphore mSemaphore = new Semaphore(1);
    private List<Map<String, Object>> pandianuserList;
    private AlertDialog dialog;

    private List<String> billlist;
    private List<JL_APP_OutStoreDetail> outstoredetaillist;


    //公共
    private Button commitBtn;
    private Button cancelBtn;
    private ImageButton backImgBtn;
    private TextView titiltxt;
    private ImageView scanImgBtn;


    class InStoreListNewAdapter extends BaseAdapter {
        private Context context;
        private List<KL_outstore_model_detail> list;

        public InStoreListNewAdapter(Context paramContext, List<KL_outstore_model_detail> paramList)
        {
            this.context = paramContext;
            this.list = paramList;
        }
        /**
         * 当ListView数据发生变化时,调用此方法来更新ListView
         * @param list
         */
        public void updateListView(List<KL_outstore_model_detail> list) {
            if (list == null) {
                this.list = new ArrayList<KL_outstore_model_detail>();
            } else {
                this.list = list;
            }
            notifyDataSetChanged();
        }
        public int getCount()
        {
            return this.list.size();
        }

        public KL_outstore_model_detail getItem(int paramInt)
        {
            return (KL_outstore_model_detail)this.list.get(paramInt);
        }

        public long getItemId(int paramInt)
        {
            return paramInt;
        }

        public List<KL_outstore_model_detail> getList()
        {
            return this.list;
        }

        public View getView(int paramInt, View paramView, ViewGroup paramViewGroup)
        {
            ViewHolder localViewHolder=null;
            if (null == paramView)
            {
                localViewHolder = new ViewHolder();
                LayoutInflater mInflater = LayoutInflater.from(context);
                paramView = mInflater.inflate(R.layout.item_kl_pandianstore_list_details, null);
                localViewHolder.tv_jsStandard=(TextView)paramView.findViewById(R.id.tv_jsStandard);
                localViewHolder.tv_batchcode=(TextView)paramView.findViewById(R.id.tv_batchcode);
                localViewHolder.tv_batchno=(TextView)paramView.findViewById(R.id.tv_batchno);
                localViewHolder.tv_outStockQty=(TextView)paramView.findViewById(R.id.tv_outStockQty);
                localViewHolder.tv_storeP=(TextView)paramView.findViewById(R.id.tv_storeP);
                localViewHolder.tv_caijiQty=(TextView)paramView.findViewById(R.id.tv_caijiQty);
                localViewHolder.lin_layout=(LinearLayout)paramView.findViewById(R.id.lin_layout);
                localViewHolder.txtv_delete = (TextView) paramView.findViewById(R.id.txtv_delete);
                paramView.setTag(localViewHolder);
            }
            else
            {
                localViewHolder = (ViewHolder) paramView.getTag();
            }
            final int pos = paramInt;
            localViewHolder.txtv_delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    UIUtil.showToast(list.get(pos).jsStandard + "所有采集数据清除");
                    deleteBillitem(pos);
                    notifyDataSetChanged();
                    mSideslipListView.turnNormal();
                }
            });
            // set item values to the viewHolder:
            KL_outstore_model_detail markerItem = getItem(paramInt);
            if (null != markerItem)
            {
                double newQty=0;double oldQty=0;
                if(markerItem.outStockQty!=null&&!markerItem.outStockQty.equals("")){
                    oldQty=new Double(markerItem.outStockQty);
                }
                if(markerItem.yipannumber!=null&&!markerItem.yipannumber.equals("")){
                    newQty= new Double(markerItem.yipannumber);
                }
                localViewHolder.tv_jsStandard.setText(markerItem.jsStandard);
                localViewHolder.tv_batchcode.setText(markerItem.batchcode);
                localViewHolder.tv_batchno.setText(markerItem.batchno);
                localViewHolder.tv_outStockQty.setText(markerItem.outStockQty);
                localViewHolder.tv_caijiQty.setText(markerItem.yipannumber);
                localViewHolder.tv_storeP.setText(markerItem.storeP);

                if(newQty==oldQty){
                    localViewHolder.lin_layout.setBackgroundColor(Color.parseColor("#00ff00"));
                }else{
                    localViewHolder.lin_layout.setBackgroundColor(Color.parseColor("#ffffff"));
                }
            }
            return paramView;
        }

        public void setList(List<KL_outstore_model_detail> paramList)
        {
            this.list = paramList;
        }

        private class ViewHolder
        {
            TextView tv_jsStandard;
            TextView tv_batchcode;
            TextView tv_batchno;
            TextView tv_outStockQty;
            TextView tv_caijiQty;
            TextView tv_storeP;
            public TextView txtv_delete;


            LinearLayout lin_layout;
            private ViewHolder()
            {
            }
        }
    }

    private void deleteBillitem(int paramInt) {
        final KL_outstore_model_detail billdetail = orderdetailslist.get(paramInt);
        selectEtxt.setText("");
        loaddetail();
    }
}
