package com.heke.rihappclient.activity;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.heke.rihappclient.R;
import com.heke.rihappclient.WebService.AppService;
import com.heke.rihappclient.adapter.GoodsDetailsListAdapter;
import com.heke.rihappclient.application.BaseActivity;
import com.heke.rihappclient.helper.ConvertUtil;
import com.heke.rihappclient.model.rukuinfo;
import com.heke.rihappclient.net.okgo.JsonCallback;
import com.heke.rihappclient.net.okgo.LslResponse;
import com.heke.rihappclient.shareprefence.ShareprefenceBean;
import com.heke.rihappclient.utils.UIUtil;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Response;

public class PandiandetailsListActivity extends BaseActivity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pandiandetails_list);
        this.mSharedPreferences = getSharedPreferences(ShareprefenceBean.SHAREPREFENCE_NAME, 0);
        this.currentuser = AppService.getInstance().getCurrentUser();
        clientid = AppService.getInstance().getDeviceId(this);
        this.initView();
        Intent localIntent = getIntent();
        this.billid = localIntent.getStringExtra("billid");
        this.billcode = localIntent.getStringExtra("billcode");
        this.billdate=localIntent.getStringExtra("billdate");
        this.billcodeTxtView.setText(this.billcode);
        this.billdateTxtView.setText(this.billdate);
        this.loaddata();
    }
    public void initView() {
        this.backImgBtn = ((ImageButton) findViewById(R.id.back));
        this.barcodeExtText = ((EditText) findViewById(R.id.et_barcode));
        this.billcodeTxtView = ((TextView) findViewById(R.id.tv_billcode));
        this.billdateTxtView=(TextView)findViewById(R.id.tv_billdate);
        this.scanImgView=(ImageView)findViewById(R.id.scanImgBtn) ;
        this.mListView = (ListView) findViewById(R.id.listView);
        this.emptyTxtView = (TextView) findViewById(R.id.empty_list_view1);
        this.allnumTxtView=(TextView)findViewById(R.id.allnums);
        this.allmoneyTxtView=(TextView)findViewById(R.id.allmoney);
        this.backImgBtn.setOnClickListener(this);
        scanImgView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent it = new Intent(PandiandetailsListActivity.this, CaptureActivity.class);
                startActivityForResult(it, SCANNIN_GREQUEST_CODE);
            }
        });
        barcodeExtText.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {

            }

            @Override
            public void beforeTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {

            }

            @Override
            public void afterTextChanged(Editable e) {
                sumnum=0.00;
                summny=0.00;
                String content = barcodeExtText.getText().toString();
                if (content.length() > 0) {
                    ArrayList<rukuinfo> fileterList = (ArrayList<rukuinfo>) search(content);
                    mordergoodListAdapter.updateListView(fileterList);
                    //mAdapter.updateData(mContacts);
                } else {
                    mordergoodListAdapter.updateListView(orderdetailslist);
                    for (rukuinfo contact : orderdetailslist) {
                        sumnum+= ConvertUtil.convertToDouble(contact.quantity,0);
                        summny+=ConvertUtil.convertToDouble(contact.amount,0);
                    }
                }
                mListView.setSelection(0);
                DecimalFormat df   =   new   DecimalFormat("#####0.00");
                allnumTxtView.setText(df.format(sumnum));
                allmoneyTxtView.setText(df.format(summny));
            }

        });
        this.mListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view,
                                           final int position, long id) {
                //定义AlertDialog.Builder对象，当长按列表项的时候弹出确认删除对话框
                final rukuinfo rukuInfo = orderdetailslist.get(position);
                if(rukuInfo==null || rukuInfo.billbid=="" || rukuInfo.billbid=="0"){
                    UIUtil.showToast("提示：请选择盘点信息");
                    return false;
                }
                else {
                    AlertDialog.Builder builder = new AlertDialog.Builder(PandiandetailsListActivity.this);
                    builder.setMessage("商品盘点【" + rukuInfo.invname + "】确定删除?");
                    builder.setTitle("提示");

                    //添加AlertDialog.Builder对象的setPositiveButton()方法
                    builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            AppService.getInstance().deletepandianbodyAsync(rukuInfo.billid.toString(), rukuInfo.billbid, currentuser.userid, clientid, new JsonCallback<LslResponse<Object>>() {
                                @Override
                                public void onSuccess(LslResponse<Object> userLslResponse, Call call, Response response) {
                                    if (userLslResponse.code == LslResponse.RESPONSE_OK) {
                                        Toast.makeText(getBaseContext(), "删除成功", Toast.LENGTH_SHORT).show();
                                        loaddata();
                                    }
                                }
                            });

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
                        ArrayList<rukuinfo> fileterList = (ArrayList<rukuinfo>) search(result);
                        mordergoodListAdapter.updateListView(fileterList);
                    } else {
                        mordergoodListAdapter.updateListView(orderdetailslist);
                    }
                } else {
                    UIUtil.showToast("提示：没有该标签盘点信息");
                }

            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
    public void onClick(View paramView) {
        Intent localIntent = new Intent();
        switch (paramView.getId()) {
            case R.id.back:
                this.finish();
                break;
        }
    }
    public void loaddata() {
        sumnum=0.00;
        summny=0.00;
        showLoading(this, "正在加载数据...");
        AppService.getInstance().getpandianmxlistAsync(this.currentuser.userid, clientid,this.billid, new JsonCallback<LslResponse<List<rukuinfo>>>() {
            @Override
            public void onSuccess(LslResponse<List<rukuinfo>> listLslResponse, Call call, Response response) {
                stopLoading();
                if (listLslResponse.code == LslResponse.RESPONSE_ERROR) {
                    UIUtil.showToast(listLslResponse.msg);
                } else if (listLslResponse.code == LslResponse.RESPONSE_OK) {
                    orderdetailslist=listLslResponse.data;
                    if(listLslResponse.data!=null) {
                        mordergoodListAdapter=new GoodsDetailsListAdapter(PandiandetailsListActivity.this,orderdetailslist);
                        mListView.setAdapter(mordergoodListAdapter);
                        for (rukuinfo contact : orderdetailslist) {
                            sumnum+= ConvertUtil.convertToDouble(contact.quantity,0);
                            summny+=ConvertUtil.convertToDouble(contact.amount,0);
                        }
                        DecimalFormat df   =   new   DecimalFormat("#####0.00");
                        allnumTxtView.setText(df.format(sumnum));
                        allmoneyTxtView.setText(df.format(summny));
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
    private List<rukuinfo> search(String str) {
        List<rukuinfo> filterList = new ArrayList<rukuinfo>();//过滤后的list
        if (!str.isEmpty() && this.orderdetailslist!=null && this.orderdetailslist.size()>0) {//正则表达式 匹配号码
            for (rukuinfo contact : this.orderdetailslist) {
                if (contact.barcode != null) {
                    if (contact.barcode.contains(str)) {
                        if (!filterList.contains(contact)) {
                            filterList.add(contact);
                            sumnum+=Double.parseDouble(contact.quantity);
                            summny+=Double.parseDouble(contact.amount);
                        }
                    }
                }
            }
        }
        return filterList;
    }
    private ImageButton backImgBtn;
    private TextView billcodeTxtView;
    private TextView billdateTxtView;
    private EditText barcodeExtText;
    private ImageView scanImgView;
    private ListView mListView;
    private TextView emptyTxtView;
    private TextView allnumTxtView;
    private TextView allmoneyTxtView;
    private String billid,billcode,billdate;
    private List<rukuinfo> orderdetailslist;
    private GoodsDetailsListAdapter mordergoodListAdapter;
    private final static int SCANNIN_GREQUEST_CODE = 101;
    private static Double sumnum,summny;

}
