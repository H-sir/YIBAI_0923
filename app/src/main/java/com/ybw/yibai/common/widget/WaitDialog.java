package com.ybw.yibai.common.widget;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.ybw.yibai.R;
import com.ybw.yibai.common.utils.LogUtil;

/**
 * 自定义等待Dialog
 *
 * @author sjl
 */
public class WaitDialog extends Dialog {

    private final String TAG = "WaitDialog";

    private Context mContext;
    private TextView mTextView = null;
    private String mWaitingString = null;

    public WaitDialog(Context context) {
        super(context, R.style.CustomDialogTheme);
        mContext = context;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // 设置点击屏幕Dialog不消失
        setCanceledOnTouchOutside(false);

        setContentView(R.layout.wait_dialog_layout);
        mTextView = findViewById(R.id.textView);

        if (!TextUtils.isEmpty(mWaitingString)) {
            mTextView.setVisibility(View.VISIBLE);
            mTextView.setText(mWaitingString);
        } else {
            mTextView.setVisibility(View.GONE);
        }
    }

    public void setWaitDialogText(String text) {
        mWaitingString = text;
        if (mTextView == null) {
            return;
        }
        if (!TextUtils.isEmpty(mWaitingString)) {
            mTextView.setVisibility(View.VISIBLE);
            mTextView.setText(text);
        } else {
            mTextView.setVisibility(View.GONE);
        }
    }

    @Override
    public void show() {
        if (mContext != null && !((Activity) mContext).isFinishing()) {
            try {
                super.show();
            } catch (Exception e) {
                LogUtil.e(TAG, e.toString());
            }
        } else {
            LogUtil.e(TAG, "show 异常");
        }
    }

    /**
     * dismiss() 主要作用是让dialog从屏幕上消失
     * dismiss() 可以在任何线程调用
     */
    @Override
    public void dismiss() {
        if (mContext != null && !((Activity) mContext).isFinishing()) {
            try {
                super.dismiss();
            } catch (Exception e) {
                LogUtil.e(TAG, e.toString());
            }
        } else {
            LogUtil.e(TAG, "dismiss 异常");
        }
    }

    /**
     * cancel()主要作用也是让dialog从屏幕上消失，并且cancel去调用了dismiss（）
     * 如果调用的cancel的话就可以监听DialogInterface.OnCancelListener
     */
    @Override
    public void cancel() {
        if (mContext != null && !((Activity) mContext).isFinishing()) {
            try {
                super.cancel();
            } catch (Exception e) {
                LogUtil.e(TAG, e.toString());
            }
        } else {
            LogUtil.e(TAG, "cancel 异常");
        }
    }
}
