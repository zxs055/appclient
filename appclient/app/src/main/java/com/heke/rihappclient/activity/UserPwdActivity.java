package com.heke.rihappclient.activity;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.heke.rihappclient.R;
import com.heke.rihappclient.WebService.AppService;
import com.heke.rihappclient.application.BaseActivity;
import com.heke.rihappclient.model.userinfo;
import com.heke.rihappclient.net.NetworkStateService;
import com.heke.rihappclient.net.okgo.JsonCallback;
import com.heke.rihappclient.net.okgo.LslResponse;
import com.heke.rihappclient.shareprefence.ShareprefenceBean;
import com.heke.rihappclient.utils.IntentUtil;
import com.heke.rihappclient.utils.UIUtil;
import com.heke.rihappclient.view.TitleView;

import okhttp3.Call;
import okhttp3.Response;

public class UserPwdActivity extends BaseActivity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_userpwd);
        this.mSharedPreferences = getSharedPreferences(ShareprefenceBean.SHAREPREFENCE_NAME, 0);
        this.currentuser = AppService.getInstance().getCurrentUser();
        clientid=AppService.getInstance().getDeviceId(this);
        this.initView();
    }
    public void initView()
    {
        this.mTitleBar = (TitleView) findViewById(R.id.titleBar);
        this.usernameTextView = ((TextView)findViewById(R.id.defaultusername));
        this.password1EditText = ((EditText)findViewById(R.id.passwordEdit1));
        this.password2EditText = ((EditText)findViewById(R.id.passwordEdit2));
        this.password3EditText = ((EditText)findViewById(R.id.passwordEdit3));
        this.comfirmBtn = ((Button)findViewById(R.id.confirmBtn1));
        this.usernameTextView.setOnClickListener(this);
        this.comfirmBtn.setOnClickListener(this);
        this.usernameTextView.setText(this.currentuser.username);
        mTitleBar.setTitle("密码修改");
        mTitleBar.setLeftButtonAsFinish(this);
    }
    public void onClick(View paramView) {
        switch (paramView.getId()) {
            case R.id.confirmBtn1:
                if (!NetworkStateService.isNetworkAvailable(this)) {
                    UIUtil.showToast("网络连接不可用，请检查!");
                    return;
                }
                if(TextUtils.isEmpty(this.currentuser.userid)){
                    UIUtil.showToast("当前用户信息加载失败!");
                    return;
                }
                if ((this.password1EditText.getText().toString().trim().equals("")) || (this.password2EditText.getText().toString().trim().equals("")) || (this.password3EditText.getText().toString().trim().equals(""))){
                    UIUtil.showToast("密码不能为空!");
                    return;
                }
                if (!this.password2EditText.getText().toString().trim().equals(this.password3EditText.getText().toString().trim())){
                    UIUtil.showToast("确认密码与新密码不一致!");
                    return;
                }
                String password=this.password1EditText.getText().toString().trim() + "#cd@wgm";
                String newpassword=this.password2EditText.getText().toString().trim()+"#rh@wgm";
                showLoading(this);
                clientid=AppService.getInstance().getDeviceId(this);
                AppService.getInstance().resetPwdAsync(this.currentuser.userid, password,newpassword,clientid, new JsonCallback<LslResponse<userinfo>>() {
                    @Override
                    public void onSuccess(LslResponse<userinfo> userLslResponse, Call call, Response response) {
                        if (userLslResponse.code == LslResponse.RESPONSE_ERROR){
                            UIUtil.showToast(userLslResponse.msg);
                            stopLoading();
                        }else{
                            IntentUtil.newIntent(UserPwdActivity.this,LoginActivity.class);
                            finish();
                        }
                    }
                });
                break;
        }
    }
    private TitleView mTitleBar;
    private Button comfirmBtn;
    private EditText password1EditText;
    private EditText password2EditText;
    private EditText password3EditText;
    private ProgressDialog pro_dialog;
    private TextView usernameTextView;
}
