package com.heke.rihappclient.fragment;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.heke.rihappclient.R;
import com.heke.rihappclient.WebService.AppService;
import com.heke.rihappclient.adapter.messagelistAdapter;
import com.heke.rihappclient.application.App;
import com.heke.rihappclient.model.baseinfo;
import com.heke.rihappclient.model.rukuinfo;
import com.heke.rihappclient.model.userinfo;
import com.heke.rihappclient.net.okgo.JsonCallback;
import com.heke.rihappclient.net.okgo.LslResponse;
import com.heke.rihappclient.utils.UIUtil;

import java.util.List;

import okhttp3.Call;
import okhttp3.Response;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link MessageFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link MessageFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MessageFragment extends Fragment {
    @Override
    public void onCreate(Bundle paramBundle)
    {
        super.onCreate(paramBundle);

    }
@Override
    public View onCreateView(LayoutInflater paramLayoutInflater, ViewGroup paramViewGroup, Bundle paramBundle)
    {
        return paramLayoutInflater.inflate(R.layout.fragment_message, null);
    }

    @Override
    public void onActivityCreated(Bundle paramBundle)
    {
        super.onActivityCreated(paramBundle);
        this.mSharedPreferences = App.getInstance().getSharedPreferences();
        this.currentuser= AppService.getInstance().getCurrentUser();
        init();
    }
@Override
    public void onAttach(Activity paramActivity)
    {
        super.onAttach(paramActivity);
        this.context = paramActivity;
    }

    public void onClick(View paramView) {
        Intent localIntent = new Intent();
        switch (paramView.getId()) {

        }
    }
    public void init() {
        this.mlistview=(ListView)getView().findViewById(R.id.noticelistview);
        this.emptyTextView=(TextView)getView().findViewById(R.id.empty_list_view);
        this.emptyTextView.setVisibility(View.VISIBLE);
        loaddata();
    }
    public void loaddata() {
        String clientid= AppService.getInstance().getDeviceId(MessageFragment.this.context);
        this.pro_dialog = ProgressDialog.show(MessageFragment.this.context, "", "正在加载数据...");
        AppService.getInstance().getNewslistAsync(this.currentuser.userid, clientid, new JsonCallback<LslResponse<List<baseinfo>>>() {
            @Override
            public void onSuccess(LslResponse<List<baseinfo>> listLslResponse, Call call, Response response) {
                MessageFragment.this.pro_dialog.dismiss();
                if (listLslResponse.code == LslResponse.RESPONSE_ERROR) {
                    emptyTextView.setVisibility(View.VISIBLE);
                } else if (listLslResponse.code == LslResponse.RESPONSE_OK) {
                    datalist=listLslResponse.data;
                    if(listLslResponse.data!=null) {
                        mlistAdapter=new messagelistAdapter(MessageFragment.this.context,datalist);
                        mlistview.setAdapter(mlistAdapter);
                        emptyTextView.setVisibility(View.GONE);
                    }
                } else {
                    UIUtil.showToast(listLslResponse.msg);
                }
            }
        });
    }
    private  Context context;
    private ListView mlistview;
    private TextView emptyTextView;
    private SharedPreferences mSharedPreferences;
    public userinfo currentuser;
    private List<baseinfo> datalist;
    private messagelistAdapter mlistAdapter;
    private ProgressDialog pro_dialog;
}
