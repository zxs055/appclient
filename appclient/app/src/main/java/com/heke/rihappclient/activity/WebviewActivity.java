package com.heke.rihappclient.activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.webkit.JavascriptInterface;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.TextView;
import android.widget.Toast;

import com.heke.rihappclient.R;

public class WebviewActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);//无标题
        setContentView(R.layout.activity_webview);
        view=(WebView) findViewById(R.id.web);
        progress = findViewById(R.id.layout_progress);
        mprogress=new ProgressDialog(this);
        titleTv=(TextView) findViewById(R.id.text);
        js=new JavaScriptObject(this);
        view.getSettings().setDefaultTextEncodingName("utf-8");
        view.getSettings().setJavaScriptEnabled(true);
        view.addJavascriptInterface(js, "myObj");
        view.loadUrl("file:///android_asset/demo.html");
        view.setBackgroundResource(R.mipmap.login_background);
        view.setBackgroundColor(Color.argb(0,0,0,0));
        view.setWebViewClient(new WebViewClient() {
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return true;
            }
            @Override
            public void onPageStarted (WebView view, String url, Bitmap favicon){
                super.onPageStarted(view,url,favicon);
                mprogress.show();
            }
            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                mprogress.hide();
            }
            // 出现错误是   的回调
            @Override
            public void onReceivedError(WebView view, int errorCode,
                                        String description, String failingUrl) {
                // TODO Auto-generated method stub
                super.onReceivedError(view, errorCode, description, failingUrl);
            }
        });
        view.setWebChromeClient(new WebChromeClient(){
            @Override
            public void onReceivedTitle(WebView view, String title) {
                // 设置标题
                super.onReceivedTitle(view, title);
                if (titleTv != null) {
                    titleTv.setText(title);
                }
            }

            @Override
            public void onShowCustomView(View view, CustomViewCallback callback) {
                // TODO Auto-generated method stub
                super.onShowCustomView(view, callback);
            }

        });
    }
    WebView view;
    View progress;
    ProgressDialog mprogress;
    JavaScriptObject js;
    TextView titleTv;

}

class JavaScriptObject {
    Context mContxt;
    public JavaScriptObject(Context mContxt) {
        this.mContxt = mContxt;
    }
    //JS只能访问带有 @JavascriptInterface注解的Java函数。
    @JavascriptInterface
    public void fun2(String name,String psd) {
        Toast.makeText(mContxt, "账号："+name+" 密码："+psd,Toast.LENGTH_LONG).show();
    }

}
