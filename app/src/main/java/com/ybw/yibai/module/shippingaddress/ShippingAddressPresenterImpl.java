package com.ybw.yibai.module.shippingaddress;

import com.ybw.yibai.base.BasePresenterImpl;
import com.ybw.yibai.common.bean.Address;
import com.ybw.yibai.module.shippingaddress.ShippingAddressContract.CallBack;
import com.ybw.yibai.module.shippingaddress.ShippingAddressContract.ShippingAddressModel;
import com.ybw.yibai.module.shippingaddress.ShippingAddressContract.ShippingAddressPresenter;
import com.ybw.yibai.module.shippingaddress.ShippingAddressContract.ShippingAddressView;

/**
 * 收货地址界面Presenter实现类
 *
 * @author sjl
 */
public class ShippingAddressPresenterImpl extends BasePresenterImpl<ShippingAddressView> implements ShippingAddressPresenter, CallBack {

    /**
     * PresenterImpl 持有 View的接口引用
     */
    private ShippingAddressView mShippingAddressView;

    /**
     * PresenterImpl 持有 Model的接口引用
     */
    private ShippingAddressModel mShippingAddressModel;

    /**
     * 构造方法
     *
     * @param view View的对象
     */
    public ShippingAddressPresenterImpl(ShippingAddressView view) {
        super(view);

        // 调用父类的方法获取View的对象
        this.mShippingAddressView = getView();
        mShippingAddressModel = new ShippingAddressModelImpl();
    }

    /**
     * 获取收货地址
     */
    @Override
    public void getShippingAddress() {
        mShippingAddressModel.getShippingAddress(this);
    }

    /**
     * 在获取收货地址成功时回调
     *
     * @param shippingAddress 收获地址
     */
    @Override
    public void onGetShippingAddressSuccess(Address shippingAddress) {
        mShippingAddressView.onGetShippingAddressSuccess(shippingAddress);
    }
}
