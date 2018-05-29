package com.heke.rihappclient.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.heke.rihappclient.Config.Consts;
import com.heke.rihappclient.R;
import com.heke.rihappclient.application.BaseActivity;
import com.heke.rihappclient.helper.DownloadUtils;
import com.heke.rihappclient.shareprefence.ShareprefenceBean;
import com.heke.rihappclient.utils.UIUtil;
import com.heke.rihappclient.view.TitleView;

public class ServerUrlActivity extends BaseActivity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_server_url);
        this.mSharedPreferences = getSharedPreferences(ShareprefenceBean.SHAREPREFENCE_NAME, 0);
        initControl();
        Intent localIntent = getIntent();
    }
    private void initControl() {
        mTitleBar = (TitleView) findViewById(R.id.reset_pwd_titleBar);
        this.serverurl = ((EditText) findViewById(R.id.serverTxt));
        this.updateTv=(TextView)findViewById(R.id.tv_update);
        this.ok = ((Button)findViewById(R.id.ok));
        this.ok.setOnClickListener(this);
        this.updateTv.setOnClickListener(this);
        String serverurl =  mSharedPreferences.getString(ShareprefenceBean.SERVER_URL,"");

        if(!serverurl.equals(""))
            this.serverurl.setText(serverurl);
        else{
            this.serverurl.setText(Consts.API_SERVICE_HOST.replace("/KLApi/appServer",""));
            mSharedPreferences.edit().putString(ShareprefenceBean.SERVER_URL, this.serverurl.getText().toString()).commit();
        }
        mTitleBar.setTitle("服务器地址设置");
        mTitleBar.setLeftButtonAsFinish(this);
    }
    @Override
    public void onClick(View paramView) {
        switch (paramView.getId()) {
            default:
                return;
            case R.id.ok:
                if (this.serverurl.getText().toString().equals("")) {
                    UIUtil.showToast("请输入服务器地址!");
                    return;
                }
                mSharedPreferences.edit().putString(ShareprefenceBean.SERVER_URL, this.serverurl.getText().toString()).commit();
                Consts.API_SERVICE_HOST=serverurl.getText().toString()+"/KLApi/appServer";
                UIUtil.showToast("服务器地址设置成功!");
                Intent intent = new Intent();
                intent.putExtra("result", "OK");
                setResult(101, intent);
                this.finish();
                return;
            case R.id.tv_update:
                downloadUtils =   new DownloadUtils(ServerUrlActivity.this);
                downloadUtils.downloadAPK("http://220.178.112.106:8010/apk/rhapp.apk", "rhapp.apk");
                break;
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
         if (requestCode == 100) {

        }
        super.onActivityResult(requestCode, resultCode, data);
    }
    private TitleView mTitleBar;
    private EditText serverurl;
    private Button ok;
    private TextView updateTv;
    private DownloadUtils downloadUtils;
}
