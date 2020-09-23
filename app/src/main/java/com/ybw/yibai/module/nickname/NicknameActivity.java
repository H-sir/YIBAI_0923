package com.ybw.yibai.module.nickname;

import android.content.SharedPreferences;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.ybw.yibai.R;
import com.ybw.yibai.base.BaseActivity;
import com.ybw.yibai.common.bean.EditUserInfo;
import com.ybw.yibai.common.bean.EditUserInfo.DataBean;
import com.ybw.yibai.common.bean.NetworkType;
import com.ybw.yibai.common.utils.ExceptionUtil;
import com.ybw.yibai.common.utils.MessageUtil;
import com.ybw.yibai.common.utils.OtherUtil;
import com.ybw.yibai.common.widget.WaitDialog;
import com.ybw.yibai.module.nickname.NicknameContract.NicknamePresenter;
import com.ybw.yibai.module.nickname.NicknameContract.NicknameView;

import static com.ybw.yibai.common.constants.Encoded.CODE_SUCCEED;
import static com.ybw.yibai.common.constants.Preferences.NICK_NAME;
import static com.ybw.yibai.common.constants.Preferences.USER_INFO;

/**
 * 昵称
 *
 * @author sjl
 * @date 2019/10/29
 */
public class NicknameActivity extends BaseActivity implements NicknameView, View.OnClickListener {

    /**
     * 用户的昵称
     */
    private String mNickName;

    /**
     * 返回
     */
    private ImageView mBackImageView;

    /**
     * 提交按钮
     */
    private Button mSubmitButton;

    /**
     * 编辑
     */
    private EditText mEditText;

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
     *
     */
    private SharedPreferences mPreferences;

    /**
     * View 持有 Presenter的接口引用
     * <p>
     * PresenterImpl 作用为触发加载数据
     */
    private NicknamePresenter mNicknamePresenter;

    @Override
    protected int setLayout() {
        return R.layout.activity_nickname;
    }

    @Override
    protected void initView() {
        mBackImageView = findViewById(R.id.backImageView);
        mSubmitButton = findViewById(R.id.submitButton);
        mEditText = findViewById(R.id.nicknameEditText);
        mWaitDialog = new WaitDialog(this);
        // 设置状态栏成白色的背景,字体颜色为黑色
        OtherUtil.setStatusBarColor(this, ContextCompat.getColor(this, android.R.color.white));
    }

    @Override
    protected void initData() {
        mPreferences = getSharedPreferences(USER_INFO, MODE_PRIVATE);
        mNickName = mPreferences.getString(NICK_NAME, null);
        if (!TextUtils.isEmpty(mNickName)) {
            mEditText.setText(mNickName);
            mEditText.setSelection(mNickName.length());
        }
    }

    @Override
    protected void initEvent() {
        mNicknamePresenter = new NicknamePresenterImpl(this);
        mBackImageView.setOnClickListener(this);
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

        // 提交按钮
        if (id == R.id.submitButton) {
            String nickName = mEditText.getText().toString().trim();
            mNicknamePresenter.editUserInfo(nickName);
        }
    }

    /**
     * 修改用户昵称成功时回调
     *
     * @param editUserInfo 修改用户基础信息时服务器端返回的数据
     */
    @Override
    public void onEditUserInfoSuccess(EditUserInfo editUserInfo) {
        MessageUtil.showMessage(editUserInfo.getMsg());
        if (CODE_SUCCEED == editUserInfo.getCode()) {
            DataBean data = editUserInfo.getData();
            if (null == data) {
                return;
            }
            String nickname = data.getNickname();
            if (TextUtils.isEmpty(nickname)) {
                return;
            }
            // 保存修改后的昵称
            SharedPreferences.Editor edit = mPreferences.edit();
            edit.putString(NICK_NAME, nickname);
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
        if (null != mNicknamePresenter) {
            mNicknamePresenter.onDetachView();
            mNicknamePresenter = null;
        }
    }
}
