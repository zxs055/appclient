package com.heke.rihappclient.activity;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.heke.rihappclient.R;
import com.heke.rihappclient.WebService.AppService;
import com.heke.rihappclient.fragment.MessageFragment;
import com.heke.rihappclient.fragment.SettingFragment;
import com.heke.rihappclient.fragment.WorkFragment;
import com.heke.rihappclient.model.userinfo;
import com.heke.rihappclient.net.okgo.JsonCallback;
import com.heke.rihappclient.net.okgo.LslResponse;
import com.heke.rihappclient.utils.UIUtil;
import com.lzy.okgo.OkGo;

import java.text.SimpleDateFormat;

import okhttp3.Call;
import okhttp3.Response;

public class HomeActivity extends FragmentActivity
        implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        this.inflater = LayoutInflater.from(this);
        initView();
        init();
    }
    public void init()
    {
        this.imgView1.setImageDrawable(getResources().getDrawable(R.mipmap.tab_message_normal));
        this.textView1.setTextColor(getResources().getColor(R.color.theme_normal));
        this.imgView2.setImageDrawable(getResources().getDrawable(R.mipmap.tab_app_press));
        this.textView2.setTextColor(getResources().getColor(R.color.theme_blue));
        //this.imgView3.setImageDrawable(getResources().getDrawable(2130837595));
       // this.textView3.setTextColor(getResources().getColor(2131099717));
        this.imgView4.setImageDrawable(getResources().getDrawable(R.mipmap.tab_me_normal));
        this.textView4.setTextColor(getResources().getColor(R.color.theme_normal));
        dongtaiFirst();
        this.tabNumber = 1;
    }

    public void initView()
    {
        this.imgView1 = ((ImageView)findViewById(R.id.img1));
        this.imgView2 = ((ImageView)findViewById(R.id.img2));
        //this.imgView3 = ((ImageView)findViewById(R.id.img3));
        this.imgView4 = ((ImageView)findViewById(R.id.img4));
        this.textView1 = ((TextView)findViewById(R.id.text1));
        this.textView2 = ((TextView)findViewById(R.id.text2));
        //this.textView3 = ((TextView)findViewById(R.id.text3));
        this.textView4 = ((TextView)findViewById(R.id.text4));
        this.messagePageLayout = ((LinearLayout)findViewById(R.id.messagePage));
        this.workPageLayout = ((LinearLayout)findViewById(R.id.workPage));
        //this.analyticPageLayout = ((LinearLayout)findViewById(R.id.analyticPage));
        this.myinfoPageLayout = ((LinearLayout)findViewById(R.id.myinfoPage));
        this.contentpagerlayout = ((FrameLayout)findViewById(R.id.contentpager));
        this.messagePageLayout.setOnClickListener(this);
        this.workPageLayout.setOnClickListener(this);
        //this.analyticPageLayout.setOnClickListener(this);
        this.myinfoPageLayout.setOnClickListener(this);
        this.formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    }
    public void dongtaiFirst()
    {
        FragmentTransaction localFragmentTransaction = getSupportFragmentManager().beginTransaction();
        localFragmentTransaction.replace(R.id.contentpager, new WorkFragment());
        localFragmentTransaction.commit();
    }
    public void dongtaiTwo()
    {
        FragmentTransaction localFragmentTransaction = getSupportFragmentManager().beginTransaction();
        localFragmentTransaction.replace(R.id.contentpager, new SettingFragment());
        localFragmentTransaction.commit();
    }
    public void dongtaiThree()
    {
        FragmentTransaction localFragmentTransaction = getSupportFragmentManager().beginTransaction();
        localFragmentTransaction.replace(R.id.contentpager, new MessageFragment());
        localFragmentTransaction.commit();
    }
    public void onClick(View paramView) {
        switch (paramView.getId()) {
            case R.id.workPage:
                dongtaiFirst();
                this.tabNumber=1;
                this.imgView1.setImageDrawable(getResources().getDrawable(R.mipmap.tab_message_normal));
                this.textView1.setTextColor(getResources().getColor(R.color.theme_normal));
                this.imgView2.setImageDrawable(getResources().getDrawable(R.mipmap.tab_app_press));
                this.textView2.setTextColor(getResources().getColor(R.color.theme_blue));
                this.imgView4.setImageDrawable(getResources().getDrawable(R.mipmap.tab_me_normal));
                this.textView4.setTextColor(getResources().getColor(R.color.theme_normal));
                break;
            case R.id.messagePage:
                dongtaiThree();
                this.tabNumber=3;
                this.imgView1.setImageDrawable(getResources().getDrawable(R.mipmap.tab_message_press));
                this.textView1.setTextColor(getResources().getColor(R.color.theme_blue));
                this.imgView2.setImageDrawable(getResources().getDrawable(R.mipmap.tab_app_normal));
                this.textView2.setTextColor(getResources().getColor(R.color.theme_normal));
                this.imgView4.setImageDrawable(getResources().getDrawable(R.mipmap.tab_me_normal));
                this.textView4.setTextColor(getResources().getColor(R.color.theme_normal));
                break;
            case R.id.myinfoPage:
                dongtaiTwo();
                this.tabNumber=2;
                this.imgView1.setImageDrawable(getResources().getDrawable(R.mipmap.tab_message_normal));
                this.textView1.setTextColor(getResources().getColor(R.color.theme_normal));
                this.imgView2.setImageDrawable(getResources().getDrawable(R.mipmap.tab_app_normal));
                this.textView2.setTextColor(getResources().getColor(R.color.theme_normal));
                this.imgView4.setImageDrawable(getResources().getDrawable(R.mipmap.tab_me_press));
                this.textView4.setTextColor(getResources().getColor(R.color.theme_blue));
                break;
        }
    }
    /**
     * 重写onBackPressed方法用于提示用户是否再次退出
     */
    @Override
    public void onBackPressed() {
        Log.e(TAG, System.currentTimeMillis() + "");
        Log.e(TAG, mExitTime + "");

        if ((System.currentTimeMillis() - mExitTime) > 2000) {
            Toast.makeText(this, "再按一次退出", Toast.LENGTH_SHORT)
                    .show();
            mExitTime = System.currentTimeMillis();
        } else {
            //finish();
            // System.exit(0);
            exituser();
        }
    }
    public void exituser() {
        String clientid= AppService.getInstance().getDeviceId(this);
        //new BaseActivity().showLoading(this, "正在加载数据...");
        AppService.getInstance().getUserExitAsync(AppService.getInstance().getCurrentUser().userid, clientid, new JsonCallback<LslResponse<userinfo>>() {
            @Override
            public void onSuccess(LslResponse<userinfo> listLslResponse, Call call, Response response) {
                //new BaseActivity().stopLoading();
                if (listLslResponse.code == LslResponse.RESPONSE_ERROR) {
                    UIUtil.showToast(listLslResponse.msg);
                    finish();
                } else if (listLslResponse.code == LslResponse.RESPONSE_OK) {
                   //userinfo u=listLslResponse.data;
                    finish();
                } else {
                    UIUtil.showToast(listLslResponse.msg);
                    finish();
                }
            }
        });
    }
    private ImageView imgView1;
    private ImageView imgView2;
    private ImageView imgView3;
    private ImageView imgView4;
    private LayoutInflater inflater;
    private int tabNumber = 1;
    private TextView textView1;
    private TextView textView2;
    private TextView textView3;
    private TextView textView4;
    private LinearLayout messagePageLayout;
    private LinearLayout workPageLayout;
    private LinearLayout analyticPageLayout;
    private LinearLayout myinfoPageLayout;
    private FrameLayout contentpagerlayout;
    private SimpleDateFormat formatter;
    private static final String TAG = "HomeActivity";
    // 保存用户按返回键的时间
    private long mExitTime = 0;
    @Override
    protected void onDestroy() {
        super.onDestroy();
        //Activity销毁时，取消网络请求
        OkGo.getInstance().cancelAll();
    }
}
