package com.heke.rihappclient.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.heke.rihappclient.R;
import com.heke.rihappclient.WebService.AppService;
import com.heke.rihappclient.adapter.invListViewAdapter;
import com.heke.rihappclient.application.BaseActivity;
import com.heke.rihappclient.model.baseinfo;
import com.heke.rihappclient.model.rukuinfo;
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

public class QC_editDetailsActivity extends BaseActivity implements View.OnClickListener{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qc_edit_details);
        this.mSharedPreferences = getSharedPreferences(ShareprefenceBean.SHAREPREFENCE_NAME, 0);
        this.currentuser = AppService.getInstance().getCurrentUser();
        clientid=AppService.getInstance().getDeviceId(this);
        this.initView();
        this.init();
    }
    public void init() {
        Intent localIntent = getIntent();
        this.billid = localIntent.getStringExtra("billid");
        this.billcode = localIntent.getStringExtra("billcode");
        this.billdate=localIntent.getStringExtra("billdate");
        this.billcode="2000000688";
        this.barcodelist = new ArrayList<String>();
        this.resultmaplist = new ArrayList<Map<String, Object>>();
        HashMap<String, Object> map = new HashMap<String, Object>();
        map.put("invname", "合格");
        map.put("rinfo", 1);
        resultmaplist.add(map);
        HashMap<String, Object> map1 = new HashMap<String, Object>();
        map1.put("invname", "不合格");
        map1.put("rinfo", 2);
        resultmaplist.add(map1);
        HashMap<String, Object> map2 = new HashMap<String, Object>();
        map2.put("invname", "残次品");
        map2.put("rinfo", 3);
        resultmaplist.add(map2);
    }
    public void initView() {
        this.backImgBtn = ((ImageButton) findViewById(R.id.back));
        this.backImgBtn.setOnClickListener(this);
        this.resultlayout=(LinearLayout)findViewById(R.id.result_layout);
        this.resultlayout.setOnClickListener(this);
        this.resulttxt=(TextView) findViewById(R.id.resultxt);

        //公共
        this.commitBtn = ((Button) findViewById(R.id.commitBtn));
        this.cancelBtn = ((Button) findViewById(R.id.cancelBtn));
        this.titiltxt=(TextView)findViewById(R.id.titletxt);
        this.titiltxt.setText("编辑检测项目");
        this.commitBtn.setOnClickListener(this);
        this.cancelBtn.setOnClickListener(this);
        this.scanImgBtn=(ImageView)findViewById(R.id.scanImgBtn);
        this.scanImgBtn.setOnClickListener(this);
        scanImgBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent it = new Intent(QC_editDetailsActivity.this, CaptureActivity.class);
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
                    //barcodeTxtView.setText(result);
                    //loaddata(result);
                } else {
                    //this.setTextNull();
                    UIUtil.showToast("提示：没有该标签入库信息");
                }

            } else if (resultCode == Activity.RESULT_CANCELED) {
                //this.setTextNull();
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
    public void onClick(View paramView) {
        switch (paramView.getId()) {
            case R.id.back:
                Intent intent = new Intent();
                intent.putExtra("result", "OK");
                setResult(1001, intent);
                this.finish();
                break;
            case R.id.result_layout:
                loadlistup(resulttxt,resultmaplist);
                break;
            case R.id.commitBtn:
                if (TextUtils.isEmpty(this.billcode)) {
                    UIUtil.showToast("提示：没有任务");
                    return;
                }
                new AlertDialog.Builder(this)
                        .setMessage("是否提交？")
                        .setPositiveButton("确定",
                                new DialogInterface.OnClickListener() {

                                    @Override
                                    public void onClick(DialogInterface dialog,
                                                        int which) {
                                        dialog.dismiss();
                                        commitdata();

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
                    QC_editDetailsActivity.this.dialog.dismiss();
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

    public void loaddata(String barcode) {
        this.invcodelist=new ArrayList<>();
        this.invcodelist.clear();
        showLoading(this, "正在加载数据...");
        AppService.getInstance().getcgInvdocstorelistAsync(this.currentuser.userid, clientid, barcode, new JsonCallback<LslResponse<List<rukuinfo>>>() {
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
    }



    public void commitdata() {
    }
    private ImageButton backImgBtn;
    private String positionId;
    private List<rukuinfo> invcodelist;
    private volatile Semaphore mSemaphore = new Semaphore(1);
    private AlertDialog dialog;
    private final static int SCANNIN_GREQUEST_CODE = 101;
    private static String billcode = "", billid = "", invid="",billdate="";
    public ArrayAdapter<String> aAdapter;
    public List<String> barcodelist;
    private LinearLayout resultlayout;
    private TextView resulttxt;
    private List<Map<String, Object>> resultmaplist;

    //公共
    private Button commitBtn;
    private Button cancelBtn;
    private TextView titiltxt;
    private ImageView scanImgBtn;

    class getJson extends AsyncTask<String,String,String> {

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
        }

        @Override
        protected void onProgressUpdate(String... values) {
            aAdapter = new ArrayAdapter<String>(getApplicationContext(),R.layout.barcodeitem,barcodelist);
            aAdapter.notifyDataSetChanged();
        }

        @Override
        protected String doInBackground(String... key) {
            String newText = key[0];
            try{
                AppService.getInstance().GetcgorderbarcodelistAsync(currentuser.userid, clientid, newText, new JsonCallback<LslResponse<List<baseinfo>>>() {
                    @Override
                    public void onSuccess(LslResponse<List<baseinfo>> infoLslResponse, Call call, Response response) {

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
