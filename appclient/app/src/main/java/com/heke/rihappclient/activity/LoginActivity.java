package com.heke.rihappclient.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.text.InputType;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.heke.rihappclient.Config.Consts;
import com.heke.rihappclient.R;
import com.heke.rihappclient.WebService.AppService;
import com.heke.rihappclient.application.BaseActivity;
import com.heke.rihappclient.model.baseinfo;
import com.heke.rihappclient.model.userinfo;
import com.heke.rihappclient.net.NetworkStateService;
import com.heke.rihappclient.net.okgo.JsonCallback;
import com.heke.rihappclient.net.okgo.LslResponse;
import com.heke.rihappclient.shareprefence.ShareprefenceBean;
import com.heke.rihappclient.utils.IntentUtil;
import com.heke.rihappclient.utils.UIUtil;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.exception.OkGoException;

import java.net.ConnectException;
import java.net.SocketTimeoutException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Response;

public class LoginActivity extends BaseActivity implements View.OnClickListener, CompoundButton.OnCheckedChangeListener{



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        this.mSharedPreferences = getSharedPreferences(ShareprefenceBean.SHAREPREFENCE_NAME, 0);
        this.initView();
        this.Init();
    }
    public void initView()
    {
        this.serverTxt = ((TextView)findViewById(R.id.serverTxt));
//        this.companyEdit = ((EditText)findViewById(R.id.companyEdit));
        this.usernameAutoCompleteTextView = ((EditText)findViewById(R.id.usernameEdit));
        this.passwordAutoCompleteTextView = ((EditText)findViewById(R.id.passwordEdit));
        this.loginBtn = ((Button)findViewById(R.id.loginBtn));
        this.eyeCheckBox = ((CheckBox)findViewById(R.id.eye));
        this.serverlayout = ((LinearLayout)findViewById(R.id.serverlayout));
        this.setlayout = ((LinearLayout)findViewById(R.id.setlayout));
        this.downImgView = ((ImageView)findViewById(R.id.downImgView));
        this.serverlayout.setOnClickListener(this);
        this.setlayout.setOnClickListener(this);
        this.loginBtn.setOnClickListener(this);
        this.downImgView.setOnClickListener(this);
        this.eyeCheckBox.setOnCheckedChangeListener(this);
        this.eyeCheckBox.setChecked(false);
    }
    private void Init(){

       /* if (this.mSharedPreferences.getString(ShareprefenceBean.SHOWPASS, "true").equals("true"))
        {
            this.eyeCheckBox.setChecked(true);
            return;
        }*/
        String serverurl =  mSharedPreferences.getString(ShareprefenceBean.SERVER_URL,"");
        if(serverurl.equals("")){
            Toast.makeText(this,"请设置服务器地址！", Toast.LENGTH_SHORT).show();
            return;
        }
        //Consts.API_COMSERVICE_HOST=serverurl+"/Api";
        Consts.API_SERVICE_HOST =  serverurl+"/KLApi/appServer";
        this.serverTxt.setText(mSharedPreferences.getString(ShareprefenceBean.SERVICE_NAME1,""));
//        this.companyEdit.setText(mSharedPreferences.getString(ShareprefenceBean.COMPANY_NAME,""));
        this.usernameAutoCompleteTextView.setText(this.mSharedPreferences.getString(ShareprefenceBean.USERCODE, ""));
        //this.passwordAutoCompleteTextView.setText(this.mSharedPreferences.getString(ShareprefenceBean.PASSWORD, ""));
        serverlist = new ArrayList<baseinfo>();
        Log.i("TAG",Consts.API_SERVICE_HOST);
        if (!NetworkStateService.isNetworkAvailable(this)) {
            Toast.makeText(LoginActivity.this,"网络连接不可用，请检查！", Toast.LENGTH_SHORT).show();
            return;
        }
    }

@Override
    protected void onStart()
    {
        super.onStart();
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 100 && resultCode==101) {
            Init();
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
    @SuppressWarnings("unchecked")
    public void initServerPopupWindow() {
        if (serverlist != null && serverlist.size() > 0) {
            String[] arrayOfString = new String[this.serverlist.size()];
           for(int i=0;i<this.serverlist.size();i++){
               arrayOfString[i] = serverlist.get(i).basename;
           }
            View localView = LayoutInflater.from(this).inflate(R.layout.popupwindow_list, null);
            ListView localListView = (ListView) localView.findViewById(R.id.popuplist);
            ArrayAdapter localArrayAdapter = new ArrayAdapter(this, R.layout.popupwindow_list_textview, arrayOfString);
            localListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                public void onItemClick(AdapterView<?> paramAdapterView, View paramView, int paramInt, long paramLong) {
                    LoginActivity.this.serverTxt.setText(LoginActivity.this.serverlist.get(paramInt).basename);
                    LoginActivity.this.selectServerNum = paramInt;
                    String url = serverlist.get(paramInt).baseid;
                    if ((paramInt > -1) && !url.equals("")) {
                        mSharedPreferences.edit().putString(ShareprefenceBean.COMPANY_NAME, serverlist.get(paramInt).basename).commit();
                    }
                    LoginActivity.this.mserverPopupWindow.dismiss();

                }
            });
            localListView.setAdapter(localArrayAdapter);
            this.mserverPopupWindow = new PopupWindow(localView, this.serverTxt.getWidth(), -2);
            this.mserverPopupWindow.setBackgroundDrawable(new BitmapDrawable());
            this.mserverPopupWindow.setFocusable(true);
            this.mserverPopupWindow.setOutsideTouchable(true);
            this.mserverPopupWindow.update();
            this.mserverPopupWindow.showAsDropDown(this.serverTxt);

        } else {
            Toast.makeText(this, "请等待！", Toast.LENGTH_LONG).show();
        }
    }
   @Override
    public void onClick(View paramView) {
        switch (paramView.getId()) {
            case R.id.serverlayout:
                if(serverlist == null || serverlist.size() == 0){
                   int i= setServerCenter();
                    if(i==1){
                        Toast.makeText(this,"获取组织机构失败，请检查服务器地址！",Toast.LENGTH_LONG).show();
                        break;
                    }
                }
                initServerPopupWindow();
                break;
            case R.id.setlayout:
                Intent intent1 = new Intent(this,ServerUrlActivity.class);
                startActivityForResult(intent1, 100);
                break;
            case R.id.loginBtn:
                if (!NetworkStateService.isNetworkAvailable(this)) {
                    Toast.makeText(LoginActivity.this,"网络连接不可用，请检查！", Toast.LENGTH_SHORT).show();
                    return;
                }
                this.username = this.usernameAutoCompleteTextView.getText().toString().trim();
                this.password = this.passwordAutoCompleteTextView.getText().toString().trim();
                if (TextUtils.isEmpty(username)) {
                    UIUtil.showToast("用户名不能为空!");
                    return;
                }
//                if (TextUtils.isEmpty(password)) {
//                    UIUtil.showToast("密码不能为空！");
//                    return;
//                }
                showLoading(this,"登录中");
                String clientid=AppService.getInstance().getDeviceId(this);
                AppService.getInstance().loginAsync(LoginActivity.this,username, password,clientid, new JsonCallback<LslResponse<userinfo>>() {
                    @Override
                    public void onSuccess(LslResponse<userinfo> userLslResponse, Call call, Response response) {
                        if (userLslResponse.code == LslResponse.RESPONSE_ERROR){
                                Toast.makeText(LoginActivity.this,userLslResponse.msg, Toast.LENGTH_SHORT).show();
                                stopLoading();
                        }else{
                            if(userLslResponse.data!=null)
                                setUserInfo(userLslResponse.data);

                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    UIUtil.showToast("登录成功！");
                                    stopLoading();
                                }
                            });
                            IntentUtil.newIntent(LoginActivity.this,Main2Activity.class);
                            finish();
                        }
                    }
                    @Override
                    public void onError(Call call, Response response, Exception e) {
                        super.onError(call, response, e);
                        stopLoading();
                        if (e != null){
                            Log.e("db", "onError: "+e.getMessage() );
                            e.printStackTrace();
                            UIUtil.showToast(e.getMessage());
                        }
                        if (e instanceof OkGoException){
                            UIUtil.showToast("抱歉，发生了未知错误！");
                        } else if (e instanceof SocketTimeoutException){
                            UIUtil.showToast("你的手机网络太慢！");
                        } else if (e instanceof ConnectException){
                            UIUtil.showToast("服务器地址不正确，请重新设置！");
                        }
                    }
                });
                break;
        }
    }
    public int setServerCenter(){
        int i=0;
        try {
            AppService.getInstance().getServerlistAsync(new JsonCallback<LslResponse<List<baseinfo>>>() {
                @Override
                public void onSuccess(LslResponse<List<baseinfo>> listLslResponse, Call call, Response response) {
                    if (listLslResponse.code == LslResponse.RESPONSE_ERROR) {
                        UIUtil.showToast(listLslResponse.msg);
                    } else {
                        serverlist.addAll(listLslResponse.data);
                    }
                }

                @Override
                public void onError(Call call, Response response, Exception e) {
                    super.onError(call, response, e);
                    stopLoading();
                    if (e != null) {
                        Log.e("db", "onError: " + e.getMessage());
                        e.printStackTrace();
                        UIUtil.showToast(e.getMessage());
                    }
                    if (e instanceof OkGoException) {
                        UIUtil.showToast("抱歉，发生了未知错误！");
                    } else if (e instanceof SocketTimeoutException) {
                        UIUtil.showToast("你的手机网络太慢！");
                    } else if (e instanceof ConnectException) {
                        UIUtil.showToast("服务器地址不正确，请重新设置！");
                    }
                }
            });
        }catch (Exception e){

            i=1;
        }
        return i;
    }
    private void setUserInfo(userinfo data) {
        AppService.getInstance().setCurrentUser(data);
        mSharedPreferences.edit().putString(ShareprefenceBean.TOKEN,data.token).commit();
        mSharedPreferences.edit().putString(ShareprefenceBean.USERNAME, data.username).commit();
        mSharedPreferences.edit().putString(ShareprefenceBean.USERID, data.userid).commit();
        mSharedPreferences.edit().putString(ShareprefenceBean.USERCODE, data.usercode).commit();
        mSharedPreferences.edit().putString(ShareprefenceBean.PASSWORD,data.userpwd).commit();
        mSharedPreferences.edit().putString(ShareprefenceBean.IS_LOGIN,"1").commit();
        mSharedPreferences.edit().putString(ShareprefenceBean.COMPANY_ID,data.storeid).commit();
        mSharedPreferences.edit().putString(ShareprefenceBean.LIMITSTAG,data.limitstag).commit();
        mSharedPreferences.edit().putString(ShareprefenceBean.SERVICE_NAME1,serverTxt.getText().toString()).commit();
    }
    public void onCheckedChanged(CompoundButton paramCompoundButton, boolean paramBoolean)
    {
        if(paramCompoundButton.getId()==R.id.eye) {
            if (paramBoolean) {
                this.passwordAutoCompleteTextView.setInputType(InputType.TYPE_TEXT_VARIATION_NORMAL);
                this.eyeCheckBox.setBackground(getResources().getDrawable(R.mipmap.eye_org));
                //mSharedPreferences.edit().putString(ShareprefenceBean.SHOWPASS, "true").commit();
                return;
            }
            this.passwordAutoCompleteTextView.setInputType(InputType.TYPE_TEXT_VARIATION_PASSWORD);
            this.eyeCheckBox.setBackground(getResources().getDrawable(R.mipmap.eye_gray));
            //mSharedPreferences.edit().putString(ShareprefenceBean.SHOWPASS, "false").commit();
        }
    }
//    private EditText companyEdit;
    private ImageView downImgView;
    private PopupWindow downPopupWindow;
    private String dwname = "";
    private CheckBox eyeCheckBox;
    private Boolean isdateup = Boolean.valueOf(true);
    private Button loginBtn;
    private PopupWindow mserverPopupWindow;
    private String password = "";
    private EditText passwordAutoCompleteTextView;
    private ProgressDialog pro_dialog;
    //private PwdHelper pwdhelper = new PwdHelper();
    private String rightString = "";
    private int selectServerNum = 0;
    private String serConfLastModifyTime = "";
    private String[] ser_array;
    private TextView serverTxt;
    private LinearLayout serverlayout;
    private String[] url_array;
    private String username = "";
    private EditText usernameAutoCompleteTextView;
    private List<baseinfo> serverlist;
    private LinearLayout setlayout;

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //Activity销毁时，取消网络请求
        OkGo.getInstance().cancelTag(this);
    }
}
