package com.ybw.yibai.module.bindingphone;

import android.content.SharedPreferences;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.ybw.yibai.R;
import com.ybw.yibai.base.BaseActivity;
import com.ybw.yibai.common.bean.BindingPhone;
import com.ybw.yibai.common.bean.NetworkType;
import com.ybw.yibai.common.bean.VerificationCode;
import com.ybw.yibai.common.utils.ExceptionUtil;
import com.ybw.yibai.common.utils.MessageUtil;
import com.ybw.yibai.common.utils.OtherUtil;
import com.ybw.yibai.common.widget.WaitDialog;
import com.ybw.yibai.module.bindingphone.BindingPhoneContract.BindingPhonePresenter;
import com.ybw.yibai.module.bindingphone.BindingPhoneContract.BindingPhoneView;

import static com.ybw.yibai.common.constants.Encoded.CODE_SUCCEED;
import static com.ybw.yibai.common.constants.Preferences.TELEPHOTO;
import static com.ybw.yibai.common.constants.Preferences.USER_INFO;

/**
 * 绑定新手机号码
 *
 * @author sjl
 * @date 2019/10/29
 */
public class BindingPhoneActivity extends BaseActivity implements BindingPhoneView, View.OnClickListener {

    /**
     * 返回
     */
    private ImageView mBackImageView;

    /**
     * 手机号码
     */
    private EditText mPhoneNumberEditText;

    /**
     * 短信验证码
     */
    private EditText mVerificationCodeEditText;

    /**
     * 获取短信验证码
     */
    private TextView mGetVerificationCodeTextView;

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
    private BindingPhonePresenter mBindingPhonePresenter;

    @Override
    protected int setLayout() {
        return R.layout.activity_binding_phone;
    }

    @Override
    protected void initView() {
        mBackImageView = findViewById(R.id.backImageView);
        mPhoneNumberEditText = findViewById(R.id.phoneNumberEditText);
        mVerificationCodeEditText = findViewById(R.id.verificationCodeEditText);
        mGetVerificationCodeTextView = findViewById(R.id.sendVerificationCodeTextView);
        mSubmitButton = findViewById(R.id.submitButton);
        mWaitDialog = new WaitDialog(this);
        // 设置状态栏成白色的背景,字体颜色为黑色
        OtherUtil.setStatusBarColor(this, ContextCompat.getColor(this, android.R.color.white));
    }

    @Override
    protected void initData() {

    }

    @Override
    protected void initEvent() {
        mBindingPhonePresenter = new BindingPhonePresenterImpl(this);
        mBackImageView.setOnClickListener(this);
        mGetVerificationCodeTextView.setOnClickListener(this);
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

        // 获取验证码
        if (id == R.id.sendVerificationCodeTextView) {
            String phoneNumber = mPhoneNumberEditText.getText().toString().trim();
            mBindingPhonePresenter.getVerificationCode(mGetVerificationCodeTextView, phoneNumber);
        }

        // 提交
        if (id == R.id.submitButton) {
            String phoneNumber = mPhoneNumberEditText.getText().toString().trim();
            String verificationCode = mVerificationCodeEditText.getText().toString().trim();
            mBindingPhonePresenter.bindingPhone(phoneNumber, verificationCode);
        }
    }

    /**
     * 在获取验证码成功时回调
     *
     * @param verificationCode 验证码
     */
    @Override
    public void onGetVerificationCodeSuccess(VerificationCode verificationCode) {
        MessageUtil.showMessage(verificationCode.getMsg());
    }

    /**
     * 在绑定手机号成功时回调
     *
     * @param bindingPhone 在绑定手机号成功时服务器端返回的数据
     */
    @Override
    public void onBindingPhoneSuccess(BindingPhone bindingPhone) {
        MessageUtil.showMessage(bindingPhone.getMsg());
        if (CODE_SUCCEED == bindingPhone.getCode()) {
            // 保存修改成功后的电话号码
            String phoneNumber = mPhoneNumberEditText.getText().toString().trim();
            SharedPreferences sharedPreferences = getSharedPreferences(USER_INFO, MODE_PRIVATE);
            SharedPreferences.Editor edit = sharedPreferences.edit();
            edit.putString(TELEPHOTO, phoneNumber);
            edit.apply();
            onBackPressed();
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
        if (null != mBindingPhonePresenter) {
            mBindingPhonePresenter.onDetachView();
            mBindingPhonePresenter = null;
        }
    }
}
