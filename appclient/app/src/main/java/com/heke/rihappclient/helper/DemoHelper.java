package com.heke.rihappclient.helper;

import android.content.Context;

import com.heke.rihappclient.im.PreferenceManager;
import com.heke.rihappclient.model.DemoModel;

/**
 * Created by wgmhx on 2017/4/21.
 */

public class DemoHelper {
    protected static final String TAG = "DemoHelper";
    private static DemoHelper instance = null;
    private String username;
    private DemoModel demoModel = null;
    private Context appContext;
   // private AppUI appUI;
    private DemoHelper() {

    }
    public synchronized static DemoHelper getInstance() {
        if (instance == null) {
            instance = new DemoHelper();
        }
        return instance;
    }
    public void init(Context context) {
        demoModel = new DemoModel(context);
        PreferenceManager.init(context);
    }

    /**
     * set current username
     * @param username
     */
    public void setCurrentUserName(String username){
        this.username = username;
        demoModel.setCurrentUserName(username);
    }

    /**
     * get current user's id
     */
    public String getCurrentUserName(){
        if(username == null){
            username = demoModel.getCurrentUsernName();
        }
        return username;
    }
    /**
     * if ever logged in
     *
     * @return
     */
    public boolean isLoggedIn() {
        boolean islogin = false;
        if (this.getCurrentUserName() == null || this.getCurrentUserName() == "") {
            islogin = false;
        }
        else {
            islogin = true;
        }
        return islogin;
    }
    /**
     * logout
     *
     * @param unbindDeviceToken
     *            whether you need unbind your device token
     * @param callback
     *            callback
     */
   /* public void logout(boolean unbindDeviceToken, final EMCallBack callback) {
        endCall();
        Log.d(TAG, "logout: " + unbindDeviceToken);
        EMClient.getInstance().logout(unbindDeviceToken, new EMCallBack() {

            @Override
            public void onSuccess() {
                Log.d(TAG, "logout: onSuccess");
                reset();
                if (callback != null) {
                    callback.onSuccess();
                }

            }

            @Override
            public void onProgress(int progress, String status) {
                if (callback != null) {
                    callback.onProgress(progress, status);
                }
            }

            @Override
            public void onError(int code, String error) {
                Log.d(TAG, "logout: onSuccess");
                reset();
                if (callback != null) {
                    callback.onError(code, error);
                }
            }
        });
    }*/
}

