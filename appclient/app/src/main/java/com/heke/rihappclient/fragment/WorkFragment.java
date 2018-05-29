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
import android.widget.LinearLayout;

import com.heke.rihappclient.R;
import com.heke.rihappclient.WebService.AppService;
import com.heke.rihappclient.activity.AboutActivity;
import com.heke.rihappclient.application.App;
import com.heke.rihappclient.model.userinfo;
import com.heke.rihappclient.net.NetworkStateService;
import com.heke.rihappclient.shareprefence.ShareprefenceBean;
import com.heke.rihappclient.utils.UIUtil;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link WorkFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link WorkFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class WorkFragment extends Fragment implements View.OnClickListener{
    @Override
    public void onCreate(Bundle paramBundle)
    {
        super.onCreate(paramBundle);
    }
    @Override
    public View onCreateView(LayoutInflater paramLayoutInflater, ViewGroup paramViewGroup, Bundle paramBundle)
    {
        View localView = paramLayoutInflater.inflate(R.layout.fragment_work, null);
       /* this.cgorderTxtView = ((TextView)localView.findViewById(R.id.today_cgorder));
        this.saleordermnyTxtView = ((TextView)localView.findViewById(R.id.saleordermny));
        this.kucunTxtView = ((TextView)localView.findViewById(R.id.kucunnum));
        this.saleordernumTxtView=(TextView)localView.findViewById(R.id.saleordernum);*/
        this.dcdiaobodanLayout=(LinearLayout)localView.findViewById(R.id.dcdiaobodanLayout);
        this.mddiaobodanLayout=(LinearLayout)localView.findViewById(R.id.mddiaobodanLayout);
        this.cgorderLayout = ((LinearLayout)localView.findViewById(R.id.cgorderLayout));
        this.xsorderLayout = ((LinearLayout)localView.findViewById(R.id.xsorderLayout));
        this.pandianLayout = ((LinearLayout)localView.findViewById(R.id.pandianLayout));
        this.myorderLayout = ((LinearLayout)localView.findViewById(R.id.myorderLayout));
       /* if (App.getInstance().getSharedPreferences().getString(ShareprefenceBean.IS_LOGIN, "-1") == "1")
        {*/
            //this.today_cgorderTxtView.setOnClickListener(this);
            //this.checkTxtView.setOnClickListener(this);
            //this.kucunTxtView.setOnClickListener(this);
            //this.completeTxtView.setOnClickListener(this);
            this.cgorderLayout.setOnClickListener(this);
            this.xsorderLayout.setOnClickListener(this);
            this.pandianLayout.setOnClickListener(this);
            this.myorderLayout.setOnClickListener(this);
        this.dcdiaobodanLayout.setOnClickListener(this);
        this.mddiaobodanLayout.setOnClickListener(this);
        this.currentuser = AppService.getInstance().getCurrentUser();
        //loadtodaydata();
       // }
        return localView;
    }
@Override
    public void onStart()
    {
        super.onStart();
        this.mSharedPreferences = App.getInstance().getSharedPreferences(ShareprefenceBean.SHAREPREFENCE_NAME, 0);
        if (!NetworkStateService.isNetworkAvailable(this.context)) {
            UIUtil.showToast("网络连接不可用，请检查！");
            return;
        }
    }
    @Override
    public void onActivityCreated(Bundle paramBundle)
    {
        super.onActivityCreated(paramBundle);
    }
@Override
    public void onAttach(Activity paramActivity)
    {
        super.onAttach(paramActivity);
        this.context = paramActivity;
    }
    @Override
    public void onClick(View paramView) {
        Intent localIntent = new Intent();
        switch (paramView.getId()) {
            case R.id.pandianLayout:
                localIntent.setClass(this.context, AboutActivity.class);
                startActivity(localIntent);
                break;
            case R.id.cgorderLayout:
                localIntent.setClass(this.context, AboutActivity.class);
                startActivity(localIntent);
                break;
            case R.id.xsorderLayout:
                localIntent.setClass(this.context, AboutActivity.class);
                startActivity(localIntent);
                break;
            case R.id.dcdiaobodanLayout:
                localIntent.setClass(this.context, AboutActivity.class);
                startActivity(localIntent);
                break;
            case R.id.mddiaobodanLayout:
                localIntent.setClass(this.context, AboutActivity.class);
                startActivity(localIntent);
                break;
        }
    }

    /*public void loadtodaydata() {
        saleordernumTxtView.setText("0.00");
        saleordermnyTxtView.setText("0.00");
        cgorderTxtView.setText("0.00");
        kucunTxtView.setText("0.00");
        String clientid= AppService.getInstance().getDeviceId(WorkFragment.this.context);
       // this.pro_dialog = ProgressDialog.show(WorkFragment.this.context, "", "正在加载数据...");
        AppService.getInstance().gettodayinfoAsync(this.currentuser.userid, clientid, new JsonCallback<LslResponse<rukuinfo>>() {
            @Override
            public void onSuccess(LslResponse<rukuinfo> listLslResponse, Call call, Response response) {
                //WorkFragment.this.pro_dialog.dismiss();
                if (listLslResponse.code == LslResponse.RESPONSE_ERROR) {
                    UIUtil.showToast(listLslResponse.msg);
                } else if (listLslResponse.code == LslResponse.RESPONSE_OK) {
                    rukuinfo rinfo=listLslResponse.data;
                    if(rinfo!=null && listLslResponse.data!=null) {
                        saleordernumTxtView.setText(rinfo.quantity);
                        saleordermnyTxtView.setText(rinfo.amount);
                        cgorderTxtView.setText(rinfo.cost);
                        kucunTxtView.setText(rinfo.kucnum);
                    }
                } else {
                    UIUtil.showToast(listLslResponse.msg);
                }
            }
        });
    }*/
    private LinearLayout cgorderLayout;
    private Context context;
    private LinearLayout myorderLayout;
    private LinearLayout dcdiaobodanLayout;
    private LinearLayout mddiaobodanLayout;
   // private TextView saleordermnyTxtView;
   // private TextView kucunTxtView;
   // private TextView saleordernumTxtView;
   // private TextView cgorderTxtView;
    private LinearLayout xsorderLayout;
    private LinearLayout pandianLayout;
    public SharedPreferences mSharedPreferences;
    private ProgressDialog pro_dialog;
    public userinfo currentuser;

}
