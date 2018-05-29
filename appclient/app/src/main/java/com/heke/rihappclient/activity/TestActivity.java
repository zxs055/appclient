package com.heke.rihappclient.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.heke.rihappclient.R;
import com.heke.rihappclient.WebService.AppService;
import com.heke.rihappclient.application.BaseActivity;
import com.heke.rihappclient.model.rukuinfo;
import com.heke.rihappclient.net.NetworkStateService;
import com.heke.rihappclient.net.okgo.JsonCallback;
import com.heke.rihappclient.net.okgo.LslResponse;
import com.heke.rihappclient.shareprefence.ShareprefenceBean;
import com.heke.rihappclient.utils.UIUtil;
import com.heke.rihappclient.view.TitleView;

import okhttp3.Call;
import okhttp3.Response;

public class TestActivity extends BaseActivity implements View.OnClickListener{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);
        this.mSharedPreferences = getSharedPreferences(ShareprefenceBean.SHAREPREFENCE_NAME, 0);
        this.currentuser = AppService.getInstance().getCurrentUser();
        clientid=AppService.getInstance().getDeviceId(this);
        if(!this.currentuser.usercode.toLowerCase().equals("t002"))
        {
            Toast.makeText(this, "权限验证失败!", Toast.LENGTH_SHORT).show();
            return;
        }
        this.initControl();
    }
    private void initControl() {
        this.mTitleBar = (TitleView) findViewById(R.id.titleBar);
        this.pandcountExt = ((EditText) findViewById(R.id.pandiancount));
        this.xiaoscountExt = ((EditText) findViewById(R.id.xiaoshoucount));
        this.caigcountExt = ((EditText) findViewById(R.id.caigoucount));
        this.pandbtnTxt=(TextView)findViewById(R.id.pandianbtn);
        this.xiaosbtnTxt=(TextView)findViewById(R.id.xiaoshoubtn);
        this.caigbtnTxt=(TextView)findViewById(R.id.caigoubtn);
       this.pandbtnTxt.setOnClickListener(this);
        this.xiaosbtnTxt.setOnClickListener(this);
        this.caigbtnTxt.setOnClickListener(this);
        mTitleBar.setTitle("数据压力测试");
        mTitleBar.setLeftButtonAsFinish(this);
    }
    public void onClick(View paramView) {
        Intent localIntent = new Intent();
        switch (paramView.getId()) {
            case R.id.pandianbtn:
                final String pds=this.pandcountExt.getText().toString();
                if (TextUtils.isEmpty(pds)) {
                    Toast.makeText(this, "数据不能为空!", Toast.LENGTH_SHORT).show();
                    return;
                }
                int pdcount = Integer.parseInt(pds);
                new android.app.AlertDialog.Builder(this)
                        .setMessage("是否确定提交盘点单？")
                        .setPositiveButton("确定",
                                new DialogInterface.OnClickListener() {

                                    @Override
                                    public void onClick(DialogInterface dialog,
                                                        int which) {
                                        dialog.dismiss();
                                        SaveForm(pds);
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
            case R.id.layout3:
                UIUtil.showToast("待启用!");
                break;
            case R.id.layout4:
                //UIUtil.showToast("待启用!");
                //启动服务
                //Intent service = new Intent(AboutActivity.this,UpdateService.class);
                //startService(service);
                break;
        }
    }

    void SaveForm(String tcounts)
    {
        if (!NetworkStateService.isNetworkAvailable(this)) {
            UIUtil.showToast("网络连接不可用，请检查！");
            return;
        }
        //
        rukuinfo pd=new rukuinfo();
        pd.userid = this.currentuser.userid;
        pd.kucnum=tcounts;
        this.showLoading(this, "正在提交数据……");
        AppService.getInstance().SavePandianTestAsync(pd,clientid, new JsonCallback<LslResponse<rukuinfo>>() {
            @Override
            public void onSuccess(LslResponse<rukuinfo> userLslResponse, Call call, Response response) {
                stopLoading();
                if (userLslResponse.code == LslResponse.RESPONSE_OK) {
                    rukuinfo pdh = userLslResponse.data ;
                    UIUtil.showToast("盘点单据提交成功，请扫码盘点商品！");
                } else {
                    UIUtil.showToast(userLslResponse.msg);
                }
            }
        });
    }
    private TitleView mTitleBar;
    public EditText pandcountExt;
    public EditText xiaoscountExt;
    public EditText caigcountExt;
    public TextView pandbtnTxt;
    public TextView xiaosbtnTxt;
    public TextView caigbtnTxt;
}
