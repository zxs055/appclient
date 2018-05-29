package com.heke.rihappclient.application;

import android.app.ActivityManager;
import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Process;
import android.support.multidex.MultiDex;
import android.widget.Toast;

import com.heke.rihappclient.helper.DemoHelper;
import com.heke.rihappclient.shareprefence.ShareprefenceBean;
import com.lzy.imagepicker.ImagePicker;
import com.lzy.imagepicker.view.CropImageView;
import com.lzy.ninegrid.NineGridView;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.cookie.store.PersistentCookieStore;
import com.squareup.leakcanary.LeakCanary;

import java.util.List;
import java.util.logging.Level;

/**
 * Created by wgmhx on 2017/4/21.
 */

public class App extends Application implements Thread.UncaughtExceptionHandler{

    private static final String MSG_APP_KEY = "16faeb1248a89";// 短信验证的app_key
    private static final String MSG_APP_SECRET = "20d994397ced27b44b48ce80956a6f9d";// 短信验证的app_secret
    private static final String MOB_APP_KEY = "1730bae762bbc";// MobApi的应用app_key
    private static final String TAG = "App";
    private SharedPreferences mSharedPreferences;

    private static App app;

    public static App getInstance() {
        return app;
    }


    @Override
    public void onCreate() {
        super.onCreate();
        app = this;
        this.mSharedPreferences = getSharedPreferences(ShareprefenceBean.SHAREPREFENCE_NAME, 0);

        // LeakCanary
       /* if (LeakCanary.isInAnalyzerProcess(this)) {
            // This process is dedicated to LeakCanary for heap analysis.
            // You should not init your app in this process.
            return;
        }
        LeakCanary.install(this);*/

        initMiPush();
        // 初始化MobApiSDK
       // MobAPI.initSDK(getApplicationContext(), MOB_APP_KEY);

        //init demo helper
        DemoHelper.getInstance().init(App.getAppContext());



        // OkGo初始化
        OkGo.init(this);
        OkGo.getInstance().debug("okgo", Level.WARNING,true)
                .setConnectTimeout(20000)  //全局的连接超时时间
                .setReadTimeOut(20000)     //全局的读取超时时间
                .setWriteTimeOut(20000)    //全局的写入超时时间
                .setCookieStore(new PersistentCookieStore());

        // NineGridView的图片加载方式初始化
        NineGridView.setImageLoader(new PicassoImageLoader());
        //initImagePicker(); // 初始化ImagePicker



    }

    public SharedPreferences getSharedPreferences()
    {
        return this.mSharedPreferences;
    }
//    private void initImagePicker() {
//        ImagePicker imagePicker = ImagePicker.getInstance();
//        imagePicker.setImageLoader(new GlideImageLoader());   //设置图片加载器
//        imagePicker.setShowCamera(true);  //显示拍照按钮
//        imagePicker.setCrop(true);        //允许裁剪（单选才有效）
//        imagePicker.setSaveRectangle(true); //是否按矩形区域保存
//        imagePicker.setSelectLimit(9);    //选中数量限制
//        imagePicker.setStyle(CropImageView.Style.RECTANGLE);  //裁剪框的形状
//        imagePicker.setFocusWidth(800);   //裁剪框的宽度。单位像素（圆形自动取宽高最小值）
//        imagePicker.setFocusHeight(800);  //裁剪框的高度。单位像素（圆形自动取宽高最小值）
//        imagePicker.setOutPutX(1000);//保存文件的宽度。单位像素
//        imagePicker.setOutPutY(1000);//保存文件的高度。单位像素
//    }


    /**
     * 初始化小米推送相关
     */
    private void initMiPush() {
        //初始化push推送服务
        if (shouldInit()) {
            //MiPushClient.registerPush(this, MIPUSH_APP_ID, MIPUSH_APP_KEY);
        }
/*
        LoggerInterface newLogger = new LoggerInterface() {
            @Override
            public void setTag(String tag) {
                // ignore
            }

            @Override
            public void log(String content, Throwable t) {
                Log.d(TAG, content, t);
            }

            @Override
            public void log(String content) {
                Log.d(TAG, content);
            }
        };
        Logger.setLogger(this, newLogger);*/


    }

    private boolean shouldInit() {
        ActivityManager am = ((ActivityManager) getSystemService(Context.ACTIVITY_SERVICE));
        List<ActivityManager.RunningAppProcessInfo> processInfos = am.getRunningAppProcesses();
        String mainProcessName = getPackageName();
        int myPid = Process.myPid();
        for (ActivityManager.RunningAppProcessInfo info : processInfos) {
            if (info.pid == myPid && mainProcessName.equals(info.processName)) {
                return true;
            }
        }
        return false;
    }

    /**
     * 获取Application Context
     */
    public static Context getAppContext() {
        return app != null ? app.getApplicationContext() : null;
    }

    public static String currentUserNick = "";

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }
    public void uncaughtException(Thread paramThread, Throwable paramThrowable)
    {
        Toast.makeText(getApplicationContext(), "程序崩溃！我们一直在尽力完善软件，欢迎您的反馈。", Toast.LENGTH_SHORT).show();
        //new Thread(this.ExitloadRun).start();
    }
}
