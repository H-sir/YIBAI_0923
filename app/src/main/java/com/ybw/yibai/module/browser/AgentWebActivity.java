package com.ybw.yibai.module.browser;

import android.app.ProgressDialog;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
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
    private static final String url = "https://mybw.100ybw.com/index/detailed.html?number=2020102398484998";
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_browser);
        assignViews();
    }

    /**
     * 初始化控件
     */
    private void assignViews() {
        webView = (WebView) findViewById(R.id.webView);
        progressDialog = new ProgressDialog(AgentWebActivity.this);
        showWebview(webView);
    }

    /**
     * 点击进行跳转
     */

    public void showWebview(View view) {
        //获取websetings 设置
        WebSettings settings = webView.getSettings();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            settings.setMixedContentMode(MIXED_CONTENT_ALWAYS_ALLOW);
        }
        settings.setBlockNetworkImage(false);
        settings.setSupportZoom(true);
        //设置浏览器支持javaScript
        settings.setJavaScriptEnabled(true);
        //设置打开自带缩放按钮
        settings.setBuiltInZoomControls(true);
        // 进行跳转用户输入的url地址
        webView.loadUrl(url);
        webView.setWebChromeClient(new WebChromeClient() {
            @Override
            //速度正在改变
            public void onProgressChanged(WebView view, int newProgress) {
                progressDialog.setMessage("加载" + newProgress);
                Log.d("1507", "5");
            }
        });
        webView.setWebViewClient(new WebViewClient() {
            @Override
            // 显示读渠道的内容
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                Log.d("1507", "3");
                return true;
            }

            /**
             * 页面开始的时候 回调此方法
             * @param view
             * @param url
             * @param favicon
             */
            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);
                if (progressDialog == null) {
                    progressDialog.setMessage("加载中。。。。。。。。。。。");
                }
                Log.d("1507", "1");
                progressDialog.show();
            }

            /**
             * 页面结束的时候 回调此方法
             * @param view
             * @param url
             */
            @Override
            public void onPageFinished(WebView view, String url) {
                progressDialog.dismiss();
//                Log.d("1507", "1");
            }
        });
    }

    /**
     * 返回
     *
     * @param keyCode
     * @param event
     * @return
     */
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        /**
         * webview.canGoBack()判断webview能否后退
         */
        if (keyCode == KeyEvent.KEYCODE_BACK && webView.canGoBack()) {
            //webView后退
            webView.goBack();
        }

        return super.onKeyDown(keyCode, event);
    }
}
