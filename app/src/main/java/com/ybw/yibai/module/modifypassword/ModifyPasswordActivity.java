package com.ybw.yibai.module.modifypassword;

import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.ybw.yibai.R;
import com.ybw.yibai.base.BaseActivity;
import com.ybw.yibai.common.bean.ModifyPassword;
import com.ybw.yibai.common.bean.NetworkType;
import com.ybw.yibai.common.bean.VerificationCode;
import com.ybw.yibai.common.utils.ExceptionUtil;
import com.ybw.yibai.common.utils.MessageUtil;
import com.ybw.yibai.common.utils.OtherUtil;
import com.ybw.yibai.common.widget.WaitDialog;
import com.ybw.yibai.module.modifypassword.ModifyPasswordContract.*;

import static com.ybw.yibai.common.constants.Encoded.CODE_SUCCEED;

/**
 * 通过手机号码修改密码
 *
 * @author sjl
 * @date 2019/11/27
 */
public class ModifyPasswordActivity extends BaseActivity implements ModifyPasswordView, View.OnClickListener {

    /**
     * 返回
     */
    private ImageView mBackImageView;

    /**
     * 手机号码
     */
    private EditText mPhoneNumberEditText;

    /**
     * 验证码
     */
    private EditText mVerificationCodeEditText;

    /**
     * 新密码
     */
    private EditText mNewPasswordEditText;

    /**
     * 确认新密码
     */
    private EditText mDetermineNewPasswordEditText;

    /**
     * 发送验证码
     */
    private TextView mSendVerificationCodeTextView;

    /**
     * 提交
     */
    private Button mSubmitButton;

    /**
     * 自定义等待Dialog
     */
    private WaitDialog mWaitDialog;

    /*
      /\_/\
    =( °w° )=
      )   (
     (__ __)
    */

    /**
     * View 持有 Presenter的接口引用
     * <p>
     * PresenterImpl 作用为触发加载数据
     */
    private ModifyPasswordPresenter mModifyPasswordPresenter;

    @Override
    protected int setLayout() {
        return R.layout.activity_modify_password;
    }

    @Override
    protected void initView() {
        mBackImageView = findViewById(R.id.backImageView);
        mPhoneNumberEditText = findViewById(R.id.phoneNumberEditText);
        mVerificationCodeEditText = findViewById(R.id.verificationCodeEditText);
        mNewPasswordEditText = findViewById(R.id.newPasswordEditText);
        mDetermineNewPasswordEditText = findViewById(R.id.determineNewPasswordEditText);
        mSendVerificationCodeTextView = findViewById(R.id.sendVerificationCodeTextView);
        mSubmitButton = findViewById(R.id.submitButton);

        mWaitDialog = new WaitDialog(this);
        // 设置状态栏成白色的背景,字体颜色为黑色
        OtherUtil.setStatusBarColor(this, ContextCompat.getColor(this, android.R.color.white));

        mNewPasswordEditText.setOnTouchListener(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                // et.getCompoundDrawables()得到一个长度为4的数组，分别表示左右上下四张图片
                Drawable drawable = mNewPasswordEditText.getCompoundDrawables()[2];
                //如果右边没有图片，不再处理
                if (drawable == null)
                    return false;
                //如果不是按下事件，不再处理
                if (event.getAction() != MotionEvent.ACTION_UP)
                    return false;
                if (event.getX() > mNewPasswordEditText.getWidth()
                        - mNewPasswordEditText.getPaddingRight()
                        - drawable.getIntrinsicWidth()){
                    mNewPasswordEditText.setText("");
                }
                return false;
            }
        });
        mDetermineNewPasswordEditText.setOnTouchListener(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                // et.getCompoundDrawables()得到一个长度为4的数组，分别表示左右上下四张图片
                Drawable drawable = mDetermineNewPasswordEditText.getCompoundDrawables()[2];
                //如果右边没有图片，不再处理
                if (drawable == null)
                    return false;
                //如果不是按下事件，不再处理
                if (event.getAction() != MotionEvent.ACTION_UP)
                    return false;
                if (event.getX() > mDetermineNewPasswordEditText.getWidth()
                        - mDetermineNewPasswordEditText.getPaddingRight()
                        - drawable.getIntrinsicWidth()){
                    mDetermineNewPasswordEditText.setText("");
                }
                return false;
            }
        });

    }

    @Override
    protected void initData() {

    }

    @Override
    protected void initEvent() {
        mModifyPasswordPresenter = new ModifyPasswordPresenterImpl(this);

        mBackImageView.setOnClickListener(this);
        mSendVerificationCodeTextView.setOnClickListener(this);
        mSubmitButton.setOnClickListener(this);
    }

    @Override
    public void onNetworkStateChange(NetworkType networkType) {

    }

    @Override
    public void onClick(View v) {
        int id = v.getId();

        // 返回
        if (id == R.id.backImageView) {
            onBackPressed();
        }

        // 发送验证码
        if (id == R.id.sendVerificationCodeTextView) {
            String phoneNumber = mPhoneNumberEditText.getText().toString().trim();
            mModifyPasswordPresenter.getVerificationCode(mSendVerificationCodeTextView, phoneNumber);
        }

        // 提交
        if (id == R.id.submitButton) {
            String phoneNumber = mPhoneNumberEditText.getText().toString().trim();
            String verificationCode = mVerificationCodeEditText.getText().toString().trim();
            String newPassword = mNewPasswordEditText.getText().toString().trim();
            String determineNewPassword = mDetermineNewPasswordEditText.getText().toString().trim();
            mModifyPasswordPresenter.modifyPassword(phoneNumber, verificationCode, newPassword, determineNewPassword);
        }
    }

    /**
     * 在获取验证码成功时回调
     *
     * @param verificationCode 验证码
     */
    @Override
    public void onGetVerificationCodeSuccess(VerificationCode verificationCode) {
        if (!TextUtils.isEmpty(verificationCode.getMsg())) {
            MessageUtil.showMessage(verificationCode.getMsg());
        }
    }

    /**
     * 在修改账号密码成功时回调
     *
     * @param modifyPassword 在修改账号密码成功时服务器端返回的数据
     */
    @Override
    public void onModifyPasswordSuccess(ModifyPassword modifyPassword) {
        MessageUtil.showMessage(modifyPassword.getMsg());
        if (CODE_SUCCEED == modifyPassword.getCode()) {
            finish();
        }
    }

    /**
     * 在请求网络数据之前显示Loading界面
     */
    @Override
    public void onShowLoading() {
        if (!mWaitDialog.isShowing()) {
            mWaitDialog.setWaitDialogText(getResources().getString(R.string.loading));
            mWaitDialog.show();
        }
    }

    /**
     * 在请求网络数据完成隐藏Loading界面
     */
    @Override
    public void onHideLoading() {
        if (mWaitDialog.isShowing()) {
            mWaitDialog.dismiss();
        }
    }

    /**
     * 在请求网络数据失败时进行一些操作,如显示错误信息...
     *
     * @param throwable 异常类型
     */
    @Override
    public void onLoadDataFailure(Throwable throwable) {
        ExceptionUtil.handleException(throwable);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (null != mModifyPasswordPresenter) {
            mModifyPasswordPresenter.onDetachView();
            mModifyPasswordPresenter = null;
        }
    }
}
