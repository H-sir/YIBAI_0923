package com.ybw.yibai.module.change;

import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.provider.Settings;
import android.view.Gravity;
import android.view.View;
import android.widget.CheckBox;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;

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
import com.ybw.yibai.module.city.CityModelImpl;

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
}
