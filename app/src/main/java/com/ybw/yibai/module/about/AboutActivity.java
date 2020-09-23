package com.ybw.yibai.module.about;

import android.content.Intent;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.ybw.yibai.R;
import com.ybw.yibai.base.BaseActivity;
import com.ybw.yibai.common.bean.AppUpdate;
import com.ybw.yibai.common.bean.NetworkType;
import com.ybw.yibai.common.utils.AppUtil;
import com.ybw.yibai.common.utils.ExceptionUtil;
import com.ybw.yibai.common.utils.MessageUtil;
import com.ybw.yibai.common.utils.OtherUtil;
import com.ybw.yibai.common.widget.WaitDialog;
import com.ybw.yibai.module.about.AboutUsContract.AboutUsPresenter;
import com.ybw.yibai.module.about.AboutUsContract.AboutUsView;
import com.ybw.yibai.module.browser.BrowserActivity;

import static com.ybw.yibai.common.constants.Encoded.CODE_SUCCEED;
import static com.ybw.yibai.common.constants.HttpUrls.AGREEMENT_URL;
import static com.ybw.yibai.common.constants.Preferences.URL;
/**
 * 关于
 *
 * @author sjl
 * @date 2019/10/24
 */
public class AboutActivity extends BaseActivity implements View.OnClickListener, AboutUsView {

    /**
     * 返回
     */
    private ImageView mBackImageView;

    /**
     * 版本名称
     */
    private TextView mVersionsNameTextView;

    /**
     * 服务协议
     */
    private TextView mServiceAgreementTextView;

    /**
     * 版本升级
     */
    private TextView mVersionUpgradeTextView;

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
    private AboutUsPresenter mAboutUsPresenter;

    @Override
    protected int setLayout() {
        return R.layout.activity_about;
    }

    @Override
    protected void initView() {
        mBackImageView = findViewById(R.id.backImageView);
        mVersionsNameTextView = findViewById(R.id.versionsNameTextView);
        mServiceAgreementTextView = findViewById(R.id.serviceAgreementTextView);
        mVersionUpgradeTextView = findViewById(R.id.versionUpgradeTextView);

        mWaitDialog = new WaitDialog(this);
        // 设置状态栏成白色的背景,字体颜色为黑色
        OtherUtil.setStatusBarColor(this, ContextCompat.getColor(this, android.R.color.white));
    }

    @Override
    protected void initData() {
        String versionName = AppUtil.getVersionName(this);
        if (!TextUtils.isEmpty(versionName)) {
            String s = "V" + versionName;
            mVersionsNameTextView.setText(s);
        }
    }

    @Override
    protected void initEvent() {
        mAboutUsPresenter = new AboutUsPresenterImpl(this);
        mBackImageView.setOnClickListener(this);
        mServiceAgreementTextView.setOnClickListener(this);
        mVersionUpgradeTextView.setOnClickListener(this);
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

        // 服务协议
        if (id == R.id.serviceAgreementTextView) {
            Intent intent = new Intent(this, BrowserActivity.class);
            intent.putExtra(URL, AGREEMENT_URL);
            startActivity(intent);
        }

        // 版本升级
        if (id == R.id.versionUpgradeTextView) {
            mAboutUsPresenter.appUpdate();
        }
    }

    /**
     * 请求应用更新成功时回调
     *
     * @param appUpdate 请求应用更新成功时服务器端返回的数据
     */
    @Override
    public void onAppUpdateSuccess(AppUpdate appUpdate) {
        if (CODE_SUCCEED != appUpdate.getCode()) {
            MessageUtil.showMessage(appUpdate.getMsg());
            return;
        }
        if (1 == appUpdate.getData().getRenew()) {
            mAboutUsPresenter.downloadApp(appUpdate);
        } else {
            MessageUtil.showMessage(getResources().getString(R.string.the_latest_version));
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
        if (null != mAboutUsPresenter) {
            mAboutUsPresenter.onDetachView();
            mAboutUsPresenter = null;
        }
    }
}
