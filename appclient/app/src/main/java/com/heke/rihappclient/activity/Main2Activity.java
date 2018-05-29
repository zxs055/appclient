package com.heke.rihappclient.activity;

import android.content.SharedPreferences;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.heke.rihappclient.R;
import com.lzy.okgo.OkGo;

public class Main2Activity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
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
        localFragmentTransaction.replace(R.id.contentpager, new WorkActivity());
        localFragmentTransaction.commit();
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
            //this.mSharedPreferences.edit().putInt(ShareprefenceBean.IS_LOGIN, -1).commit();
            finish();
            // System.exit(0);
        }
    }
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
