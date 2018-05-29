package com.heke.rihappclient.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.heke.rihappclient.R;
import com.lzy.okgo.OkGo;

public class PlantMainActivity extends AppCompatActivity {

    private String type;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_plantmain);
        Intent localIntent = getIntent();
        this.type = localIntent.getStringExtra("type");
        this.initView();
        this.init();
    }
    public void init()
    {
        dongtaiFirst();
        this.tabNumber = 1;
    }

    public void initView()
    {
        this.contentpagerlayout = ((FrameLayout)findViewById(R.id.contentpager));
    }
    public void dongtaiFirst()
    {
        FragmentTransaction localFragmentTransaction = getSupportFragmentManager().beginTransaction();
        if(type.equals("2")) {
            localFragmentTransaction.replace(R.id.contentpager, new PlantActivity());
        }
        if(type.equals("1")) {
            localFragmentTransaction.replace(R.id.contentpager, new Plant1Activity());
        }
        if(type.equals("3")) {
            localFragmentTransaction.replace(R.id.contentpager, new Plant3Activity());
        }
        if(type.equals("4")) {
            localFragmentTransaction.replace(R.id.contentpager, new Plant4Activity());
        }
        if(type.equals("5")) {
            localFragmentTransaction.replace(R.id.contentpager, new Plant5Activity());
        }
        localFragmentTransaction.commit();
    }
    /**
     * 重写onBackPressed方法用于提示用户是否再次退出
     */
//    @Override
//    public void onBackPressed() {
//        Log.e(TAG, System.currentTimeMillis() + "");
//        Log.e(TAG, mExitTime + "");
//
//        if ((System.currentTimeMillis() - mExitTime) > 2000) {
//            Toast.makeText(this, "再按一次退出", Toast.LENGTH_SHORT)
//                    .show();
//            mExitTime = System.currentTimeMillis();
//        } else {
//            //this.mSharedPreferences.edit().putInt(ShareprefenceBean.IS_LOGIN, -1).commit();
//            finish();
//            // System.exit(0);
//        }
//    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        //Activity销毁时，取消网络请求
        OkGo.getInstance().cancelAll();
    }


    private static final String TAG = "HomeActivity";
    private FrameLayout contentpagerlayout;
    private SharedPreferences mSharedPreferences;
    private LayoutInflater inflater;
    private int tabNumber = 1;
    // 保存用户按返回键的时间
    private long mExitTime = 0;
}
