package com.heke.rihappclient.application;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;

import com.heke.rihappclient.WebService.AppService;
import com.heke.rihappclient.model.userinfo;
import com.heke.rihappclient.shareprefence.ShareprefenceBean;
import com.heke.rihappclient.shareprefence.ShareprefenceBillMessage;
import com.heke.rihappclient.view.Loading;

/**
 * Created by wgmhx on 2017/4/21.
 */

public class BaseActivity extends AppCompatActivity {
    private Dialog mDialog;
    private static long mLastClickTime;
    private boolean isDestroyed = false;
    private static final String TAG = "BaseActivity";
    public userinfo currentuser;
    public SharedPreferences mSharedPreferences;
    public SharedPreferences mSharedPreferencesbill;
    public static String clientid="";
    @Override
    public void onCreate(Bundle savedInstanceState, PersistableBundle persistentState) {
        isDestroyed = false;
        Log.e(TAG,"onCreate");
        super.onCreate(savedInstanceState, persistentState);
        this.currentuser = AppService.getInstance().getCurrentUser();
        this.mSharedPreferences = getSharedPreferences(ShareprefenceBean.SHAREPREFENCE_NAME, 0);
        this.mSharedPreferencesbill = getSharedPreferences(ShareprefenceBillMessage.SHAREPREFENCE_NAME, 0);
        clientid=AppService.getInstance().getDeviceId(this);
    }
    public boolean canUpdateUI(){
        return (!isFinishing());
    }
    public void showLoading(Context context){
        Loading loading = new Loading();
        mDialog = loading.showLoading(context,null,null,Loading.LOGOSTYLE);
    }

    public void showLoading(Context context,String text){
        Loading loading = new Loading();
        mDialog = loading.showLoading(context,text,null,Loading.LOGOSTYLE);
    }

    public void showLoading(Context context,String text,Loading.OnReturnListener listener){
        Loading loading = new Loading();
        mDialog = loading.showLoading(context,text,listener,Loading.LOGOSTYLE);
    }

    public void stopLoading(){
        if (canUpdateUI()){
            Loading loading = new Loading();
            loading.dialogDismiss(mDialog);
        }
    }

    public void setOnCancelListener(DialogInterface.OnCancelListener cancelListener){
        if (mDialog != null){
            mDialog.setOnCancelListener(cancelListener);
        }
    }

    /**
     * 检测是否是双击退出应用程序
     * @return true - 快速双击，间隔不少于1秒  false 不是快速双击
     */
    public synchronized static boolean isFastClick(){
        long time = System.currentTimeMillis();
        if (time - mLastClickTime < 1000){
            return true;
        }
        mLastClickTime = time;
        return false;
    }

    protected InputMethodManager inputMethodManager;

    protected void hideSoftKeyboard() {
        if (getWindow().getAttributes().softInputMode != WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN) {
            if (getCurrentFocus() != null)
                inputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(),
                        InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
        parseIntent();
    }

    private void parseIntent() {
        Intent intent = getIntent();
        /*if (intent.hasExtra(NimIntent.EXTRA_NOTIFY_CONTENT)) {
            // consultService();
            // 最好将intent清掉，以免从堆栈恢复时又打开客服窗口
            setIntent(new Intent());
        }*/
    }
}
