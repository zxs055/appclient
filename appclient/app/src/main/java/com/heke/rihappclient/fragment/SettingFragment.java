package com.heke.rihappclient.fragment;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.heke.rihappclient.R;
import com.heke.rihappclient.WebService.AppService;
import com.heke.rihappclient.activity.AboutActivity;
import com.heke.rihappclient.activity.HelperActivity;
import com.heke.rihappclient.activity.LoginActivity;
import com.heke.rihappclient.activity.Main2Activity;
import com.heke.rihappclient.activity.UserPwdActivity;
import com.heke.rihappclient.application.App;
import com.heke.rihappclient.model.userinfo;
import com.heke.rihappclient.shareprefence.ShareprefenceBean;
import com.heke.rihappclient.utils.IntentUtil;
import com.heke.rihappclient.utils.UIUtil;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link SettingFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link SettingFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SettingFragment extends Fragment
        implements View.OnClickListener {
    public void onCreate(Bundle paramBundle)
    {
        super.onCreate(paramBundle);
        this.mSharedPreferences = App.getInstance().getSharedPreferences();
    }

    public View onCreateView(LayoutInflater paramLayoutInflater, ViewGroup paramViewGroup, Bundle paramBundle)
    {
        return paramLayoutInflater.inflate(R.layout.fragment_setting, null);
    }
    public void init() {
        this.dwnameTextView = ((TextView) getView().findViewById(R.id.dwname));
        this.usernameTextView = ((TextView) getView().findViewById(R.id.username));
        this.urlTextView = ((TextView) getView().findViewById(R.id.url));
        this.logoutBtn = ((Button) getView().findViewById(R.id.logout));
        this.backImgbtn=(ImageButton) getView().findViewById(R.id.back);
        this.mimamodifyLayout = ((LinearLayout) getView().findViewById(R.id.mimamodifyLayout));
        this.aboutLayout = ((LinearLayout) getView().findViewById(R.id.aboutLayout));
        this.PersonalCenterLayout = ((LinearLayout) getView().findViewById(R.id.PersonalCenterLayout));
        this.PersonalCenterLayout.setOnClickListener(this);
        this.logoutBtn.setOnClickListener(this);
        this.backImgbtn.setOnClickListener(this);
        this.mimamodifyLayout.setOnClickListener(this);
        this.aboutLayout.setOnClickListener(this);
        this.dwnameTextView.setText("日化行业管理系统");
        currentuser=AppService.getInstance().getCurrentUser();
        //token = AppService.getInstance().getDeviceId(this);
        if(currentuser!=null){
            this.usernameTextView.setText(currentuser.username);
            this.urlTextView.setText(currentuser.storename);
        }
    }
    public void onActivityCreated(Bundle paramBundle)
    {
        super.onActivityCreated(paramBundle);
        init();
    }

    public void onAttach(Activity paramActivity)
    {
        super.onAttach(paramActivity);
        this.context = paramActivity;
    }

    public void onClick(View paramView) {
        Intent localIntent = new Intent();
        switch (paramView.getId()) {
            case R.id.logout:
                SettingFragment.this.mSharedPreferences.edit().putInt(ShareprefenceBean.IS_LOGIN, -1).commit();
                UIUtil.showToast("退出成功！");
                localIntent = new Intent();
                localIntent.setClass(SettingFragment.this.context, LoginActivity.class);
                SettingFragment.this.startActivity(localIntent);
                ((Activity)SettingFragment.this.context).finish();
                break;
            case R.id.mimamodifyLayout:
                IntentUtil.newIntent(SettingFragment.this.context,UserPwdActivity.class);
                break;
            case R.id.aboutLayout:
                IntentUtil.newIntent(SettingFragment.this.context,AboutActivity.class);
                break;
            case R.id.back:
                IntentUtil.newIntent(SettingFragment.this.context, Main2Activity.class);
                ((Activity)SettingFragment.this.context).finish();
                break;
        }
    }
    private LinearLayout PersonalCenterLayout;
    private LinearLayout aboutLayout;
    private ImageButton backImgbtn;
    private Context context;
    private TextView dwnameTextView;
    private Button logoutBtn;
    private LinearLayout mimamodifyLayout;
    private ProgressDialog pro_dialog;
    private TextView urlTextView;
    private TextView usernameTextView;
    private SharedPreferences mSharedPreferences;
    public userinfo currentuser;
    public String token;
}
