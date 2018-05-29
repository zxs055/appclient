package com.heke.rihappclient.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.heke.rihappclient.R;
import com.heke.rihappclient.WebService.AppService;
import com.heke.rihappclient.application.BaseActivity;
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

public class PurchaseDetailActivity extends BaseActivity implements View.OnClickListener{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_purchase_details);
        this.mSharedPreferences = getSharedPreferences(ShareprefenceBean.SHAREPREFENCE_NAME, 0);
        this.currentuser = AppService.getInstance().getCurrentUser();
        clientid=AppService.getInstance().getDeviceId(this);
        this.initView();
        this.init();
    }
    public void init() {
        Intent localIntent = getIntent();
        this.billcode = localIntent.getStringExtra("billcode");
        this.storename = localIntent.getStringExtra("bususer");
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
        this.goodsspec=(TextView)findViewById(R.id.goodsspec);
        this.yipandnum=(TextView)findViewById(R.id.yipandnum);
        this.batchCodeTxt=(TextView)findViewById(R.id.batchCodeTxt);
        this.pandnum=(EditText)findViewById(R.id.pandnum);
        this.jiahao=(ImageButton)findViewById(R.id.jiahao);
        this.jianhao=(ImageButton)findViewById(R.id.jianhao);
        this.jiahao.setOnClickListener(this);
        this.jianhao.setOnClickListener(this);
        this.pandnum.setOnClickListener(this);
        this.batchCodeTxt.setOnClickListener(this);

        //公共
        this.commitBtn = ((Button) findViewById(R.id.commitBtn));
        this.cancelBtn = ((Button) findViewById(R.id.cancelBtn));
        this.titiltxt=(TextView)findViewById(R.id.titletxt);
        this.titiltxt.setText("到货通知单明细");
        this.commitBtn.setOnClickListener(this);
        this.cancelBtn.setOnClickListener(this);
        this.scanImgBtn=(ImageView)findViewById(R.id.scanImgBtn);
        this.scanImgBtn.setOnClickListener(this);
        scanImgBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent it = new Intent(PurchaseDetailActivity.this, CaptureActivity.class);
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
                    //this.setTextNull();
                    UIUtil.showToast("提示：没有该标签信息");
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
            case R.id.invcodelayout:
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

    public void loaddata(String barcode) {
        this.batchCodeTxt.setText("20180327");
        this.goodScope.setText("3*2.5");
        this.goodsname.setText("电缆KM");
        this.goodsspec.setText("KL-P001");
    }
    void setinvcode(List<rukuinfo> list){
        invcodemaplist.clear();
        if(list.size()>0) {
            for (int i = 0; i < list.size(); i++) {
                HashMap<String, Object> map = new HashMap<String, Object>();
                rukuinfo rinfo = list.get(i);
                String invname = rinfo.invname + "/" + rinfo.invspec+ "/" + rinfo.kuweiname+ "/" + rinfo.remarks;
                map.put("invname", invname);
                map.put("rinfo", rinfo);
                invcodemaplist.add(map);
            }
            if(list.size()==1){
                rukuinfo binfo = list.get(0);
                if (binfo != null) {
                    //invid = binfo.invid;
                }
            }
            else{
            }

        }
    }


    //提交
    public void commitdata() {
        Toast.makeText(this, "提交成功", Toast.LENGTH_SHORT).show();
    }
    private ImageButton backImgBtn;
    private List<rukuinfo> invcodelist;
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
    private TextView goodsspec;
    private TextView yipandnum;
    private EditText pandnum;
    private ImageButton jiahao;
    private ImageButton jianhao;
    private TextView batchCodeTxt;


    //公共
    private Button commitBtn;
    private Button cancelBtn;
    private TextView titiltxt;
    private ImageView scanImgBtn;
}
