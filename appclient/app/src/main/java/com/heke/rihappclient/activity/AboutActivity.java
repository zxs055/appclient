package com.heke.rihappclient.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;

import com.heke.rihappclient.Config.Consts;
import com.heke.rihappclient.R;
import com.heke.rihappclient.application.BaseActivity;
import com.heke.rihappclient.helper.DownloadUtils;
import com.heke.rihappclient.utils.IntentUtil;
import com.heke.rihappclient.utils.UIUtil;
import com.heke.rihappclient.view.TitleView;

public class AboutActivity extends BaseActivity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);
        this.init();
    }
    private void init()
    {
        //this.layout1 = ((LinearLayout)findViewById(R.id.layout1));
        this.layout2 = ((LinearLayout)findViewById(R.id.layout2));
        this.layout3 = ((LinearLayout)findViewById(R.id.layout3));
        this.layout4 = ((LinearLayout)findViewById(R.id.layout4));
        this.mTitleBar = (TitleView) findViewById(R.id.titleBar);
       // this.layout1.setOnClickListener(this);
        this.layout2.setOnClickListener(this);
        this.layout3.setOnClickListener(this);
        this.layout4.setOnClickListener(this);
        mTitleBar.setTitle("关于");
        mTitleBar.setLeftButtonAsFinish(this);
    }
    public void onClick(View paramView) {
        Intent localIntent = new Intent();
        switch (paramView.getId()) {
            /*case R.id.layout1:
                UIUtil.showToast("待启用!");
                break;*/
            case R.id.layout2:
                IntentUtil.newIntent(AboutActivity.this,HelperActivity.class);
                break;
            case R.id.layout3:
                UIUtil.showToast("待启用!");
                break;
            case R.id.layout4:
                //UIUtil.showToast("待启用!");
                //启动服务
                //Intent service = new Intent(AboutActivity.this,UpdateService.class);
                //startService(service);
                downloadUtils =   new DownloadUtils(AboutActivity.this);
                downloadUtils.downloadAPK(Consts.API_SERVICE_HOST+"/apk/rhapp.apk", "rhapp.apk");
                break;
        }
    }
    //private FeedbackAgent agent;
    private TitleView mTitleBar;
    private LinearLayout layout1;
    private LinearLayout layout2;
    private LinearLayout layout3;
    private LinearLayout layout4;
    private DownloadUtils downloadUtils;
}
