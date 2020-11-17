package com.ybw.yibai.module.browser;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.graphics.Bitmap;
import android.net.http.SslError;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.SslErrorHandler;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.ybw.yibai.R;
import com.ybw.yibai.module.main.MainActivity;

import static android.webkit.WebSettings.MIXED_CONTENT_ALWAYS_ALLOW;

/**
 * <pre>
 *     author : HKR
 *     time   : 2020/11/11
 *     desc   :
 * </pre>
 */
public class AgentWebActivity extends AppCompatActivity {

    private WebView webView;
    private static final String url = "https://mybw.100ybw.com/index/detailed.html?number=2020111799100485";
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_webview_agent);
        init();
        assignViews();
    }

    @SuppressLint("NewApi")
    private void init() {
        webView = (WebView) findViewById(R.id.webview);
        progressDialog = new ProgressDialog(AgentWebActivity.this);
        webView.setWebViewClient(new WebViewClient() {
            @Override
            public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError error) {
//                super.onReceivedSslError(view, handler, error);//一定要注释否则不起作用
// TODO Auto-generated method stub
// handler.cancel();// Android默认的处理方式
                handler.proceed();// 接受所有网站的证书
// handleMessage(Message msg);// 进行其他处理
            }
        });

        WebSettings settings = webView.getSettings();
        settings.setJavaScriptEnabled(true);
        webView.requestFocusFromTouch();              //如果webView中需要用户手动输入用户名、密码或其他，则webview必须设置支持获取手势焦点
        settings.setDomStorageEnabled(true);
        settings.setBlockNetworkImage(false);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            settings.setMixedContentMode(WebSettings.MIXED_CONTENT_ALWAYS_ALLOW);
        }

        settings.setUseWideViewPort(true);                                      //设置此属性，可任意比例缩放
        settings.setLoadWithOverviewMode(true);
        settings.setSupportZoom(true);                                          //便页面支持缩放
//        settings.setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);           //优先使用缓存
//        settings.setCacheMode(WebSettings.LOAD_NO_CACHE);                     //不使用缓存：
        settings.setJavaScriptEnabled(true);                                    //支持js
        settings.setUseWideViewPort(false);                                     //将图片调整到适合webview的大小
        settings.setSupportZoom(true);                                          //支持缩放
        settings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN); //支持内容重新布局
        settings.supportMultipleWindows();                                      //多窗口
        settings.setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);             //关闭webview中缓存
        settings.setAllowFileAccess(true);                                      //设置可以访问文件
        settings.setNeedInitialFocus(true);                                     //当webview调用requestFocus时为webview设置节点
        settings.setBuiltInZoomControls(true);                                  //设置支持缩放
        settings.setJavaScriptCanOpenWindowsAutomatically(true);                //支持通过JS打开新窗口
        settings.setLoadWithOverviewMode(true);                                 // 缩放至屏幕的大小
        settings.setLoadsImagesAutomatically(true);                             //支持自动加载图片

    }

    /**
     * 初始化控件
     */
    private void assignViews() {
        webView.loadUrl(url);
        webView.setWebChromeClient(new WebChromeClient() {
            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                // TODO Auto-generated method stub
                if (newProgress == 100) {
                    // 网页加载完成
                    progressDialog.dismiss();
                } else {
                    progressDialog.show();
                }
            }
        });
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        // TODO Auto-generated method stub
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (webView.canGoBack()) {
                webView.goBack();//返回上一页面
                return true;
            } else {
                System.exit(0);//退出程序
            }
        }
        return super.onKeyDown(keyCode, event);
    }
}
