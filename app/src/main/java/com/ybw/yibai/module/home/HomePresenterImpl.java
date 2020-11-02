package com.ybw.yibai.module.home;

import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.provider.Settings;
import android.support.v4.app.Fragment;
import android.view.View;

import com.ybw.yibai.R;
import com.ybw.yibai.base.BasePresenterImpl;
import com.ybw.yibai.common.bean.CustomerList;
import com.ybw.yibai.common.bean.HotScheme;
import com.ybw.yibai.common.bean.HotSchemeCategory;
import com.ybw.yibai.common.bean.HotSchemes;
import com.ybw.yibai.common.bean.RecommendProductList;
import com.ybw.yibai.common.bean.UserPosition;
import com.ybw.yibai.common.utils.LocationUtil;
import com.ybw.yibai.common.utils.OtherUtil;
import com.ybw.yibai.common.utils.PermissionsUtil;
import com.ybw.yibai.common.widget.CustomDialog;
import com.ybw.yibai.module.home.HomeContract.CallBack;
import com.ybw.yibai.module.home.HomeContract.HomeModel;
import com.ybw.yibai.module.home.HomeContract.HomePresenter;
import com.ybw.yibai.module.home.HomeContract.HomeView;

import static com.ybw.yibai.common.constants.Encoded.REQUEST_LOCATION_PERMISSIONS_CODE;
import static com.ybw.yibai.common.constants.Encoded.REQUEST_OPEN_GPS_CODE;

/**
 * 首页界面Presenter实现类
 *
 * @author sjl
 * @date 2019/5/16
 */
public class HomePresenterImpl extends BasePresenterImpl<HomeView> implements HomePresenter, CallBack {

    /**
     * PresenterImpl 持有 View的接口引用
     */
    private HomeView mHomeView;

    /**
     * PresenterImpl 持有 Model的接口引用
     */
    private HomeModel mHomeModel;

    /**
     * 构造方法
     *
     * @param view View的对象
     */
    public HomePresenterImpl(HomeView view) {
        super(view);

        // 调用父类的方法获取View的对象
        this.mHomeView = getView();
        mHomeModel = new HomeModelImpl();
    }

    /**
     * 获取客户列表
     */
    @Override
    public void getCustomerList() {
        mHomeModel.getCustomerList(this);
    }

    /**
     * 在获取客户列表成功时回调
     *
     * @param customersList 客户列表数据
     */
    @Override
    public void onGetCustomerListSuccess(CustomerList customersList) {
        mHomeView.onGetCustomerListSuccess(customersList);
    }

    /**
     * 获取热门方案分类
     */
    @Override
    public void getHotSchemeCategory() {
        mHomeModel.getHotSchemeCategory(this);
    }

    /**
     * 在获取热门方案分类成功时回调
     *
     * @param hotSchemeCategory 热门方案分类
     */
    @Override
    public void onGetHotSchemeCategorySuccess(HotSchemeCategory hotSchemeCategory) {
        mHomeView.onGetHotSchemeCategorySuccess(hotSchemeCategory);
    }

    /**
     * 获取热门方案列表
     *
     * @param cateId 热门方案分类id
     */
    @Override
    public void getHotScheme(int cateId) {
        mHomeModel.getHotScheme(cateId, this);
    }

    /**
     * 在获取热门方案列表成功时回调
     *
     * @param hotScheme 热门方案列表
     */
    @Override
    public void onGetHotSchemeSuccess(HotScheme hotScheme) {
        mHomeView.onGetHotSchemeSuccess(hotScheme);
    }

    /**
     * 获取新热门方案列表
     *
     * @param cateId 热门方案分类id
     */
    @Override
    public void getHotSchemes(int cateId) {
        mHomeModel.getHotSchemes(cateId, this);
    }

    /**
     * 在获取新热门方案列表成功时回调
     *
     * @param hotSchemes 新热门方案列表
     */
    @Override
    public void onGetHotSchemesSuccess(HotSchemes hotSchemes) {
        mHomeView.onGetHotSchemesSuccess(hotSchemes);
    }

    /**
     * 获取推荐产品列表
     */
    @Override
    public void getRecommendProductList() {
        mHomeModel.getRecommendProductList(this);
    }

    /**
     * 在获取推荐产品列表成功时回调
     *
     * @param recommendProductList 推荐产品列表
     */
    @Override
    public void onGetRecommendProductListSuccess(RecommendProductList recommendProductList) {
        mHomeView.onGetRecommendProductListSuccess(recommendProductList);
    }

    /**
     * 显示切换货源Dialog
     *
     * @param isNationwide 当前货源是否在全国范围
     */
    @Override
    public void displaySwitchSupplyOfGoodsDialog(boolean isNationwide) {
        Fragment fragment = (Fragment) mHomeView;
        Activity activity = fragment.getActivity();
        if (null != activity) {
            OtherUtil.setBackgroundAlpha(activity, 0.6f);
            CustomDialog customDialog = new CustomDialog(activity);
            if (isNationwide) {
                customDialog.setMessage(activity.getResources().getString(R.string.whether_to_switch_to_the_same_city_supply));
            } else {
                customDialog.setMessage(activity.getResources().getString(R.string.whether_to_switch_to_national_supply));
            }
            customDialog.setYesOnclickListener(activity.getResources().getString(R.string.determine),
                    () -> {
                        OtherUtil.setBackgroundAlpha(activity, 1f);
                        customDialog.dismiss();
                        mHomeView.whetherToSwitchResults(true);
                    });
            customDialog.setNoOnclickListener(activity.getResources().getString(R.string.cancel),
                    () -> {
                        OtherUtil.setBackgroundAlpha(activity, 1f);
                        customDialog.dismiss();
                        mHomeView.whetherToSwitchResults(false);
                    });
            customDialog.show();
        }
    }

    /**
     * 申请权限
     *
     * @param permissions 要申请的权限列表
     */
    @Override
    public void applyPermissions(String[] permissions) {
        Fragment fragment = (Fragment) mHomeView;
        // android6.0 以上才进行动态权限申请
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (!PermissionsUtil.checkPermissionAllGranted(fragment, permissions)) {
                PermissionsUtil.startRequestPermission(fragment, permissions, REQUEST_LOCATION_PERMISSIONS_CODE);
            } else {
                // 已经获取全部权限
                mHomeView.applyPermissionsResults(true);
            }
        } else {
            // 6.0以下,默认获取全部权限
            mHomeView.applyPermissionsResults(true);
        }
    }

    /**
     * 检查手机是否打开GPS功能
     */
    @Override
    public void checkIfGpsOpen() {
        Fragment fragment = (Fragment) mHomeView;
        Activity activity = fragment.getActivity();
        if (null == activity) {
            return;
        }
        boolean gpsOpenResult = LocationUtil.isGpsOpen(activity);
        mHomeView.checkIfGpsOpenResult(gpsOpenResult);
    }

    /**
     * 显示打开GPS功能的Dialog
     *
     * @param rootView 页面根布局
     */
    @Override
    public void displayOpenGpsDialog(View rootView) {
        Fragment fragment = (Fragment) mHomeView;
        Activity activity = fragment.getActivity();
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
                    fragment.startActivityForResult(intent, REQUEST_OPEN_GPS_CODE);
                });
        customDialog.setNoOnclickListener(activity.getResources().getString(R.string.cancel),
                () -> {
                    OtherUtil.setBackgroundAlpha(activity, 1f);
                    customDialog.dismiss();
                });
        customDialog.show();
    }

    /**
     * 设置货源城市
     *
     * @param position 城市字符串（广东省广州市）
     */
    @Override
    public void setUserPosition(String position) {
        mHomeModel.setUserPosition(position, this);
    }

    @Override
    public void findUserSceneListInfo() {
        mHomeModel.findUserSceneListInfo(this);
    }

    /**
     * 在设置货源城市成功时回调
     *
     * @param userPosition 设置货源城市时服务器端返回的数据
     */
    @Override
    public void onSetUserPositionSuccess(UserPosition userPosition) {
        mHomeView.onSetUserPositionSuccess(userPosition);
    }

    @Override
    public void findUserSceneListInfo(boolean flag) {
        mHomeView.findUserSceneListInfo(flag);
    }
}
