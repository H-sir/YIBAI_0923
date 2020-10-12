package com.ybw.yibai.module.welcome;

import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.view.KeyEvent;

import com.ybw.yibai.R;
import com.ybw.yibai.base.BaseActivity;
import com.ybw.yibai.common.bean.NetworkType;
import com.ybw.yibai.common.utils.PermissionsUtil;
import com.ybw.yibai.module.login.AccountLoginActivity;
import com.ybw.yibai.module.main.MainActivity;
import com.ybw.yibai.module.welcome.WelcomeContract.WelComePresenter;
import com.ybw.yibai.module.welcome.WelcomeContract.WelComeView;

import static com.ybw.yibai.common.constants.Preferences.TOKEN;
import static com.ybw.yibai.common.constants.Preferences.USER_INFO;

/**
 * 欢迎界面
 *
 * @author sjl
 * @date 2018/12/10
 */
public class WelcomeActivity extends BaseActivity implements WelComeView {

    /**
     * 在用户登陆时获得的Token
     */
    private String token;

    /**
     * 要申请的权限(
     * 允许程序访问手机状态和身份权限
     * 允许程序读取外部存储权限
     * 允许程序写入外部存储权限
     * )
     */
    private String[] permissions = {
            Manifest.permission.READ_PHONE_STATE,
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
    };

    /**
     * View 持有 Presenter的接口引用
     * <p>
     * PresenterImpl 作用为触发加载数据
     */
    private WelComePresenter mWelComePresenter;

    @Override
    protected int setLayout() {
        return R.layout.activity_welcome;
    }

    @Override
    protected void initView() {

    }

    @Override
    protected void initData() {
        SharedPreferences preferences = getSharedPreferences(USER_INFO, MODE_PRIVATE);
        token = preferences.getString(TOKEN, null);
    }

    @Override
    protected void initEvent() {
        mWelComePresenter = new WelcomePresenterImpl(this);
        // 申请权限
        mWelComePresenter.applyPermissions(permissions);
        mWelComePresenter.getDataByGet(getApplication());
    }

    @Override
    public void onNetworkStateChange(NetworkType networkType) {

    }

    /**
     * 申请权限的结果
     *
     * @param b true 已经获取全部权限,false 没有获取全部权限
     */
    @Override
    public void applyPermissionsResults(boolean b) {
        if (b) {
            mWelComePresenter.getToken(token);
        }
    }

    /**
     * 请求权限的结果的回调
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        // 获得申请的全部权限
        if (PermissionsUtil.allPermissionsAreGranted(grantResults)) {
            mWelComePresenter.getToken(token);
        } else {
            PermissionsUtil.showApplicationLacksPermissions(this);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }

    /**
     * 跳转到登陆界面
     */
    @Override
    public void startLoginActivity() {
        // 延时0.5跳转
        new Handler().postDelayed(() -> {
            Intent intent = new Intent(this, AccountLoginActivity.class);
            startActivity(intent);
            finish();

            // 淡入淡出动画效果
            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
        }, 500);
    }

    /**
     * 跳转到主界面
     */
    @Override
    public void startMainActivity() {
        // 延时0.5跳转
        new Handler().postDelayed(() -> {
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
            finish();

            // 淡入淡出动画效果
            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
        }, 500);
    }

    /**
     * 在请求网络数据之前显示Loading界面
     */
    @Override
    public void onShowLoading() {

    }

    /**
     * 在请求网络数据完成隐藏Loading界面
     */
    @Override
    public void onHideLoading() {

    }

    /**
     * 在请求网络数据失败时进行一些操作,如隐藏Loading界面and显示错误信息...
     *
     * @param throwable 异常类型
     */
    @Override
    public void onLoadDataFailure(Throwable throwable) {

    }

    /**
     * 屏蔽返回键,防止用户在进入欢迎页面时按下返回键后又回回到欢迎界面的错误
     */
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (event.getKeyCode() == KeyEvent.KEYCODE_BACK) {
            // 返回为true,就无法执行父类的 onBackPressed(); 返回
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (null != mWelComePresenter) {
            mWelComePresenter.onDetachView();
            mWelComePresenter = null;
        }
    }
}