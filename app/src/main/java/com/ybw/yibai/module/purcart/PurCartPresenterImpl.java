package com.ybw.yibai.module.purcart;

import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.provider.Settings;
import android.support.v4.app.Fragment;
import android.widget.LinearLayout;

import com.ybw.yibai.R;
import com.ybw.yibai.base.BasePresenterImpl;
import com.ybw.yibai.common.bean.PurCartBean;
import com.ybw.yibai.common.bean.QuotationData;
import com.ybw.yibai.common.utils.LocationUtil;
import com.ybw.yibai.common.utils.OtherUtil;
import com.ybw.yibai.common.utils.PermissionsUtil;
import com.ybw.yibai.common.widget.CustomDialog;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

import static com.ybw.yibai.common.constants.Encoded.REQUEST_LOCATION_PERMISSIONS_CODE;
import static com.ybw.yibai.common.constants.Encoded.REQUEST_OPEN_GPS_CODE;

/**
 * 进货Fragment Presenter 实现类
 *
 * @author sjl
 * @date 2019/11/5
 */
public class PurCartPresenterImpl extends BasePresenterImpl<PurCartContract.PurCartView> implements PurCartContract.PurCartPresenter, PurCartContract.CallBack {

    /**
     * PresenterImpl 持有 View的接口引用
     */
    private PurCartContract.PurCartView mPurCartView;

    /**
     * PresenterImpl 持有 Model的接口引用
     */
    private PurCartContract.PurCartModel mPurCartModel;

    /**
     * 构造方法
     *
     * @param view View的对象
     */
    public PurCartPresenterImpl(PurCartContract.PurCartView view) {
        super(view);

        // 调用父类的方法获取View的对象
        this.mPurCartView = getView();
        mPurCartModel = new PurCartModelImpl();
    }

    @Override
    public void getPurCartData() {
        mPurCartModel.getPurCartData(this);
    }

    @Override
    public void onGetPurCartDataSuccess(PurCartBean purCartBean) {
        mPurCartView.onGetPurCartDataSuccess(purCartBean);
    }

    @Override
    public void updateCartGate(int cartId, int num) {
        mPurCartModel.updateCartGate(cartId, num, this);
    }

    @Override
    public void onDelCart(int cartId) {
        mPurCartModel.delCart(cartId, this);
    }

    @Override
    public void onDelCartGateSuccess() {
        mPurCartView.onDelCartGateSuccess();
    }

    @Override
    public void updateCartGateCheck(int cartId, int check) {
        mPurCartModel.updateCartGateCheck(cartId, check, this);
    }

    @Override
    public void onUpdateCartGateSuccess(int num) {
        mPurCartView.onUpdateCartGateSuccess(num);
    }

    @Override
    public void upAllCart(String cartIds, int type, int isCheck) {
        mPurCartModel.upAllCart(cartIds, type, isCheck, this);
    }

    @Override
    public void onUpAllCartSuccess(int isCheck) {
        mPurCartView.onUpAllCartSuccess(isCheck);
    }

    @Override
    public void onDeleteSuccess() {
        mPurCartView.onDeleteSuccess();
    }

    @Override
    public void applyPermissions(String[] permissions) {
        Fragment activity = (Fragment) mPurCartView;
        // android6.0 以上才进行动态权限申请
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (!PermissionsUtil.checkPermissionAllGranted(activity, permissions)) {
                PermissionsUtil.startRequestPermission(activity, permissions, REQUEST_LOCATION_PERMISSIONS_CODE);
            } else {
                // 已经获取全部权限
                mPurCartView.applyPermissionsResults(true);
            }
        } else {
            // 6.0以下,默认获取全部权限
            mPurCartView.applyPermissionsResults(true);
        }
    }

    @Override
    public void checkIfGpsOpen() {
        Fragment activity = (Fragment) mPurCartView;
        if (null == activity) {
            return;
        }
        boolean gpsOpenResult = LocationUtil.isGpsOpen(activity.getContext());
        mPurCartView.checkIfGpsOpenResult(gpsOpenResult);
    }

    @Override
    public void displayOpenGpsDialog(LinearLayout rootLayout) {
        Fragment activity = (Fragment) mPurCartView;
        if (null == activity) {
            return;
        }
        OtherUtil.setBackgroundAlpha(activity.getActivity(), 0.6f);
        CustomDialog customDialog = new CustomDialog(activity.getActivity());
        customDialog.setMessage(activity.getResources().getString(R.string.switching_to_the_same_city_source_requires_positioning_service));
        customDialog.setYesOnclickListener(activity.getResources().getString(R.string.determine),
                () -> {
                    OtherUtil.setBackgroundAlpha(activity.getActivity(), 1f);
                    customDialog.dismiss();
                    // 跳转到GPS设置页
                    Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                    activity.startActivityForResult(intent, REQUEST_OPEN_GPS_CODE);
                });
        customDialog.setNoOnclickListener(activity.getResources().getString(R.string.cancel),
                () -> {
                    OtherUtil.setBackgroundAlpha(activity.getActivity(), 1f);
                    customDialog.dismiss();
                });
        customDialog.show();
    }
}
