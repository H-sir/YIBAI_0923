package com.ybw.yibai.module.welcome;

import android.app.Activity;
import android.content.Context;
import android.os.Build;
import android.text.TextUtils;

import com.ybw.yibai.base.BasePresenterImpl;
import com.ybw.yibai.common.interfaces.ApiService;
import com.ybw.yibai.common.utils.PermissionsUtil;
import com.ybw.yibai.common.utils.RetrofitManagerUtil;
import com.ybw.yibai.module.welcome.WelcomeContract.WelComePresenter;
import com.ybw.yibai.module.welcome.WelcomeContract.WelComeView;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

/**
 * 欢迎界面Presenter实现类
 *
 * @author sjl
 * @date 2018/12/10
 */
public class WelcomePresenterImpl extends BasePresenterImpl<WelComeView> implements WelComePresenter {

    /**
     * PresenterImpl 持有 View的接口引用
     */
    private WelComeView mWelComeView;

    /**
     * 构造方法
     *
     * @param view View的对象
     */
    public WelcomePresenterImpl(WelComeView view) {
        super(view);

        // 调用父类的方法获取View的对象
        this.mWelComeView = getView();
    }

    /**
     * 申请权限
     *
     * @param permissions 要申请的权限列表
     */
    @Override
    public void applyPermissions(String[] permissions) {
        Activity activity = (Activity) mWelComeView;
        // android6.0 以上才进行动态权限申请
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (!PermissionsUtil.checkPermissionAllGranted(activity, permissions)) {
                PermissionsUtil.startRequestPermission(activity, permissions);
            } else {
                // 已经获取全部权限
                mWelComeView.applyPermissionsResults(true);
            }
        } else {
            // 6.0以下,默认获取全部权限
            mWelComeView.applyPermissionsResults(true);
        }
    }

    /**
     * 获取用户的Token判断用户是不是已经登录过了
     *
     * @param token 用户登陆时获得的token
     */
    @Override
    public void getToken(String token) {
        if (!TextUtils.isEmpty(token)) {
            // 用户有登陆,跳转到主界面
            mWelComeView.startMainActivity();
        } else {
            // 用户没有登陆,跳转到登陆界面
            mWelComeView.startLoginActivity();
        }
    }

    @Override
    public void getDataByGet(Context context) {
    }
}
