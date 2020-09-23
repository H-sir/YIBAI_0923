package com.ybw.yibai.common.widget;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.ybw.yibai.R;

/**
 * 自定义的Dialog
 *
 * @author sjl
 */
public class CustomDialog extends Dialog {

    /**
     * 用户设置的message文本
     */
    private String messageString;

    /**
     * 确定文本和取消文本的显示内容
     */
    private String yesString, noString;

    /**
     * 消息提示文本
     */
    private TextView messageTextView;

    /**
     * 确定按钮
     */
    private Button yesButton;

    /**
     * 取消按钮
     */
    private Button noButton;

    /**
     * 确定按钮被点击了的监听器
     */
    private OnYesClickListener yesOnclickListener;

    /**
     * 取消按钮被点击了的监听器
     */
    private OnNoClickListener noOnclickListener;

    public CustomDialog(Context context) {
        super(context, R.style.CustomDialogTheme);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.custom_dialog_layout);

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
        messageTextView = findViewById(R.id.messageTextView);
        yesButton = findViewById(R.id.yesButton);
        noButton = findViewById(R.id.noButton);
    }

    /**
     * 初始化界面控件的显示数据
     */
    private void initData() {
        // 如果用户设置了title和message
        if (messageString != null) {
            messageTextView.setText(messageString);
        }

        // 如果用户设置了按钮的文字
        if (yesString != null) {
            yesButton.setText(yesString);
        }
        if (noString != null) {
            noButton.setText(noString);
        }
    }

    /**
     * 对外提供设置Dialog的Message的方法
     */
    public void setMessage(String message) {
        messageString = message;
    }

    /**
     * 初始化界面的确定和取消监听器
     */
    private void initEvent() {
        // 设置向外界提供确定按钮被点击后的监听
        yesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != yesOnclickListener) {
                    yesOnclickListener.onYesClick();
                }
            }
        });

        // 设置向外界提供取消按钮被点击后的监听
        noButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != noOnclickListener) {
                    noOnclickListener.onNoClick();
                }
            }
        });
    }

    /**
     * 设置确定按钮的显示内容和监听
     */
    public void setYesOnclickListener(String string, OnYesClickListener onYesClickListener) {
        if (!TextUtils.isEmpty(string)) {
            yesString = string;
        }
        this.yesOnclickListener = onYesClickListener;
    }

    /**
     * 设置取消按钮的显示内容和监听
     */
    public void setNoOnclickListener(String string, OnNoClickListener onNoClickListener) {
        if (!TextUtils.isEmpty(string)) {
            noString = string;
        }
        this.noOnclickListener = onNoClickListener;
    }

    /**
     * 设置确定按钮被点击的接口
     */
    public interface OnYesClickListener {

        /**
         * 确定按钮被点击时
         */
        void onYesClick();
    }

    /**
     * 设置取消按钮被点击的接口
     */
    public interface OnNoClickListener {

        /**
         * 取消按钮被点击时
         */
        void onNoClick();
    }
}
