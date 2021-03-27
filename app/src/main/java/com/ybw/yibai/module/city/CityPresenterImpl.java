package com.ybw.yibai.module.city;

import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.provider.Settings;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.ybw.yibai.R;
import com.ybw.yibai.base.BasePresenterImpl;
import com.ybw.yibai.common.bean.CityListBean;
import com.ybw.yibai.common.bean.EditUserInfo;
import com.ybw.yibai.common.bean.MarketListBean;
import com.ybw.yibai.common.bean.PlaceBean;
import com.ybw.yibai.common.bean.UserPosition;
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
public class CityPresenterImpl extends BasePresenterImpl<CityContract.CityView> implements CityContract.CityPresenter, CityContract.CallBack {

    /**
     * PresenterImpl 持有 View的接口引用
     */
    private CityContract.CityView mCityView;

    /**
     * PresenterImpl 持有 Model的接口引用
     */
    private CityContract.CityModel mCityModel;

    /**
     * 构造方法
     *
     * @param view View的对象
     */
    public CityPresenterImpl(CityContract.CityView view) {
        super(view);

        // 调用父类的方法获取View的对象
        this.mCityView = getView();
        mCityModel = new CityModelImpl();
    }

    /**
     * 申请权限
     *
     * @param permissions 要申请的权限列表
     */
    @Override
    public void applyPermissions(String[] permissions) {
        Activity activity = (Activity) mCityView;
        // android6.0 以上才进行动态权限申请
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (!PermissionsUtil.checkPermissionAllGranted(activity, permissions)) {
                PermissionsUtil.startRequestPermission(activity, permissions, REQUEST_LOCATION_PERMISSIONS_CODE);
            } else {
                // 已经获取全部权限
                mCityView.applyPermissionsResults(true);
            }
        } else {
            // 6.0以下,默认获取全部权限
            mCityView.applyPermissionsResults(true);
        }
    }

    /**
     * 检查手机是否打开GPS功能
     */
    @Override
    public void checkIfGpsOpen() {
        Activity activity = (Activity) mCityView;
        if (null == activity) {
            return;
        }
        boolean gpsOpenResult = LocationUtil.isGpsOpen(activity);
        mCityView.checkIfGpsOpenResult(gpsOpenResult);
    }

    /**
     * 显示打开GPS功能的Dialog
     *
     * @param rootView 页面根布局
     */
    @Override
    public void displayOpenGpsDialog(View rootView) {
        Activity activity = (Activity) mCityView;
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

    /**
     * 设置货源城市
     *
     * @param position 城市字符串（广东省广州市）
     */
    @Override
    public void setUserPosition(String position) {
        mCityModel.setUserPosition(position, this);
    }

    /**
     * 在设置货源城市成功时回调
     *
     * @param userPosition 设置货源城市时服务器端返回的数据
     */
    @Override
    public void onSetUserPositionSuccess(UserPosition userPosition) {
        mCityView.onSetUserPositionSuccess(userPosition);
    }

    @Override
    public void getCity() {
        mCityModel.getCity(this);
    }

    @Override
    public void onGetCitySuccess(CityListBean cityListBean) {
        mCityView.onGetCitySuccess(cityListBean);
    }


    /**
     * 根据经纬度获取定位
     */
    @Override
    public void getLocation(double latitude, double longitude) {
        mCityModel.getLocation(latitude, longitude, this);
    }

    @Override
    public void onGetLocationSuccess(PlaceBean placeBean) {
        mCityView.onGetLocationSuccess(placeBean);
    }

    @Override
    public void onSetProduct(int comOpen) {
        mCityModel.onSetProduct(comOpen, this);
    }

    @Override
    public void onSetProductSuccess(EditUserInfo editUserInfo) {
        mCityView.onSetProductSuccess(editUserInfo);
    }

    @Override
    public void getMarketList(double latitude, double longitude) {
        mCityModel.getMarketList(latitude, longitude, this);
    }

    @Override
    public void onGetMarketListSuccess(MarketListBean marketListBean) {
        mCityView.onGetMarketListSuccess(marketListBean);
    }

    @Override
    public void bindMarket(MarketListBean marketListBean, MarketListBean.DataBean.ListBean listBean) {
        mCityModel.bindMarket(marketListBean,listBean.getMarketId(),this);
    }

    @Override
    public void onBindMarketSuccess(MarketListBean marketListBean) {
        mCityView.onBindMarketSuccess(marketListBean);
    }

    /**
     * 显示产品设置切换的PopupWindow
     *
     * @param rootLayout 界面Root布局
     */
    @Override
    public void selectProductType(View rootLayout, String cityName) {
        Activity activity = (Activity) mCityView;
        if (null == activity) {
            return;
        }

        View view = activity.getLayoutInflater().inflate(R.layout.popup_window_select_type_layout, null);
        PopupWindow mPopupWindow = new PopupWindow(view, RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT);

        CheckBox checkboxAll = view.findViewById(R.id.checkboxAll);
        CheckBox checkboxOne = view.findViewById(R.id.checkboxOne);
        if (cityName.equals("1")) {
            checkboxAll.setChecked(true);
            checkboxOne.setChecked(false);
        } else {
            checkboxAll.setChecked(false);
            checkboxOne.setChecked(true);
        }

        checkboxAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mPopupWindow.isShowing()) {
                    mPopupWindow.dismiss();
                }
                onSetProduct(1);
            }
        });
        checkboxOne.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mPopupWindow.isShowing()) {
                    mPopupWindow.dismiss();
                }
                onSetProduct(2);
            }
        });

        mPopupWindow.setFocusable(true);
        mPopupWindow.setOutsideTouchable(true);
        mPopupWindow.setBackgroundDrawable(new ColorDrawable(0x00000000));

        // 设置一个动画效果
        mPopupWindow.setAnimationStyle(R.style.PopupWindow_Anim);

        // 在弹出PopupWindow设置屏幕透明度
        OtherUtil.setBackgroundAlpha(activity, 0.6f);
        // 添加PopupWindow窗口关闭事件
        mPopupWindow.setOnDismissListener(OtherUtil.popupDismissListener(activity, 1f));
        mPopupWindow.showAtLocation(rootLayout, Gravity.CENTER, 0, 0);
    }
}
