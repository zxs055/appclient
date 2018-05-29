package com.heke.rihappclient.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.heke.rihappclient.R;
import com.heke.rihappclient.view.TitleView;

public class HelperActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_helper);
        mTitleBar = (TitleView) findViewById(R.id.titleBar);
        mTitleBar.setTitle("关于");
        mTitleBar.setLeftButtonAsFinish(this);
    }
    private TitleView mTitleBar;
}
