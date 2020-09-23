package com.ybw.yibai.common.widget;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.Button;

import com.ybw.yibai.R;

/**
 * 自定义的APP升级Dialog
 *
 * @author sjl
 */
public class AppUpdateDialog extends Dialog {

    /**
     * 用户设置App升级内容Html字符串
     */
    private String appUpdateContentHtmlString;

    /**
     * 显示升级内容的WebView
     */
    private WebView mWebView;

    /**
     * 确定按钮
     */
    private Button mYesButton;

    /**
     * 取消按钮
     */
    private Button mNoButton;

    /**
     * 确定按钮被点击了的监听器
     */
    private OnYesOnclickListener yesOnclickListener;

    /**
     * 取消按钮被点击了的监听器
     */
    private OnNoOnclickListener noOnclickListener;

    public AppUpdateDialog(Context context) {
        super(context, R.style.CustomDialogTheme);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.app_update_dialog_layout);

        // 初始化界面控件
        initView();

        // 初始化界面数据
        initData();

        // 初始化界面控件的事件
        initEvent();

        // 设置按空白处不能取消Dialog
        setCanceledOnTouchOutside(false);
    }

    /**
     * 初始化界面控件
     */
    private void initView() {
        mWebView = findViewById(R.id.webView);
        mYesButton = findViewById(R.id.yesButton);
        mNoButton = findViewById(R.id.noButton);
    }

    /**
     * 初始化界面控件的显示数据
     */
    private void initData() {
        if (!TextUtils.isEmpty(appUpdateContentHtmlString)) {
            mWebView.loadDataWithBaseURL(null, appUpdateContentHtmlString, "text/html", "utf-8", null);
        }
    }

    /**
     * 对外提供设置Dialog的Message的方法
     */
    public void setAppUpdateContentHtmlString(String appUpdateContentHtmlString) {
        this.appUpdateContentHtmlString = appUpdateContentHtmlString;
    }

    /**
     * 初始化界面的确定和取消监听器
     */
    private void initEvent() {
        // 设置向外界提供确定按钮被点击后的监听
        mYesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (yesOnclickListener != null) {
                    yesOnclickListener.onYesClick();
                }
            }
        });

        // 设置向外界提供取消按钮被点击后的监听
        mNoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (noOnclickListener != null) {
                    noOnclickListener.onNoClick();
                }
            }
        });

        this.setOnDismissListener(dialog -> destroyWebView());
    }

    /**
     * 设置确定按钮的显示内容和监听
     */
    public void setYesOnclickListener(OnYesOnclickListener onYesOnclickListener) {
        this.yesOnclickListener = onYesOnclickListener;
    }

    /**
     * 设置取消按钮的显示内容和监听
     */
    public void setNoOnclickListener(OnNoOnclickListener onNoOnclickListener) {
        this.noOnclickListener = onNoOnclickListener;
    }

    /**
     * 设置确定按钮被点击的接口
     */
    public interface OnYesOnclickListener {

        /**
         * 确定按钮被点击时
         */
        void onYesClick();
    }

    /**
     * 设置取消按钮被点击的接口
     */
    public interface OnNoOnclickListener {

        /**
         * 取消按钮被点击时
         */
        void onNoClick();
    }

    /**
     * 销毁WebView
     */
    private void destroyWebView() {
        // 避免WebView内存泄露(在Activity销毁WebView)的时候,先让WebView加载null内容,然后移除WebView,再销毁WebView最后置空)
        if (null != mWebView) {
            mWebView.loadDataWithBaseURL(null, "", "text/html", "utf-8", null);
            mWebView.clearHistory();
            ((ViewGroup) mWebView.getParent()).removeView(mWebView);
            mWebView.destroy();
            mWebView = null;
        }
    }
}
