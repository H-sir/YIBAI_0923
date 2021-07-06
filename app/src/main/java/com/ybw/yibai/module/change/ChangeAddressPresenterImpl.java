package com.ybw.yibai.module.change;

import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.provider.Settings;
import android.support.v4.app.Fragment;
import android.view.View;

import com.ybw.yibai.R;
import com.ybw.yibai.base.BasePresenterImpl;
import com.ybw.yibai.common.bean.CityListBean;
import com.ybw.yibai.common.utils.LocationUtil;
import com.ybw.yibai.common.utils.OtherUtil;
import com.ybw.yibai.common.utils.PermissionsUtil;
import com.ybw.yibai.common.widget.CustomDialog;

import static com.ybw.yibai.common.constants.Encoded.REQUEST_LOCATION_PERMISSIONS_CODE;
import static com.ybw.yibai.common.constants.Encoded.REQUEST_OPEN_GPS_CODE;

/**
 * 城市界面Presenter实现类
 *
 * @author sjl
 * @date 2019/5/16
 */
public class ChangeAddressPresenterImpl extends BasePresenterImpl<ChangeAddressContract.ChangeAddressView> implements ChangeAddressContract.ChangeAddressPresenter, ChangeAddressContract.CallBack {

    /**
     * PresenterImpl 持有 View的接口引用
     */
    private ChangeAddressContract.ChangeAddressView mChangeAddressView;

    /**
     * PresenterImpl 持有 Model的接口引用
     */
    private ChangeAddressContract.ChangeAddressModel mChangeAddressModel;

    /**
     * 构造方法
     *
     * @param view View的对象
     */
    public ChangeAddressPresenterImpl(ChangeAddressContract.ChangeAddressView view) {
        super(view);
        // 调用父类的方法获取View的对象
        this.mChangeAddressView = getView();
        mChangeAddressModel = new ChangeAddressModelImpl();
    }


    @Override
    public void getCity() {
        mChangeAddressModel.getCity(this);
    }

    @Override
    public void onGetCitySuccess(CityListBean cityListBean) {
        mChangeAddressView.onGetCitySuccess(cityListBean);
    }

    /**
     * 申请权限
     *
     * @param permissions 要申请的权限列表
     */
    @Override
    public void applyPermissions(String[] permissions) {
        Activity activity = (Activity) mChangeAddressView;
        // android6.0 以上才进行动态权限申请
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (!PermissionsUtil.checkPermissionAllGranted(activity, permissions)) {
                PermissionsUtil.startRequestPermission(activity, permissions, REQUEST_LOCATION_PERMISSIONS_CODE);
            } else {
                // 已经获取全部权限
                mChangeAddressView.applyPermissionsResults(true);
            }
        } else {
            // 6.0以下,默认获取全部权限
            mChangeAddressView.applyPermissionsResults(true);
        }
    }

    @Override
    public void checkIfGpsOpen() {
        Activity activity = (Activity) mChangeAddressView;
        if (null == activity) {
            return;
        }
        boolean gpsOpenResult = LocationUtil.isGpsOpen(activity);
        mChangeAddressView.checkIfGpsOpenResult(gpsOpenResult);
    }

    /**
     * 显示打开GPS功能的Dialog
     *
     * @param rootView 页面根布局
     */
    @Override
    public void displayOpenGpsDialog(View rootView) {
        Activity activity = (Activity) mChangeAddressView;
        if (null == activity) {
            return;
        }
        OtherUtil.setBackgroundAlpha(activity, 0.6f);
        CustomDialog customDialog = new CustomDialog(activity);
        customDialog.setMessage(activity.getResources().getString(R.string.switching_to_the_same_city_source_requires_positioning_service));
        customDialog.setYesOnclickListener(activity.getResources().getString(R.string.determine),
                () -> {
                    OtherUtil.setBackgroundAlpha(activity, 1f);
                    customDialog.dismiss();
                    // 跳转到GPS设置页
                    Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                    activity.startActivityForResult(intent, REQUEST_OPEN_GPS_CODE);
                });
        customDialog.setNoOnclickListener(activity.getResources().getString(R.string.cancel),
                () -> {
                    OtherUtil.setBackgroundAlpha(activity, 1f);
                    customDialog.dismiss();
                });
        customDialog.show();
    }
}
