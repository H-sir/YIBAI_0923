package com.ybw.yibai.module.oldcustomer;

import com.ybw.yibai.base.BasePresenterImpl;
import com.ybw.yibai.common.bean.CustomerList;
import com.ybw.yibai.module.oldcustomer.OldCustomerContract.CallBack;
import com.ybw.yibai.module.oldcustomer.OldCustomerContract.OldCustomerPresenter;
import com.ybw.yibai.module.oldcustomer.OldCustomerContract.OldCustomerView;
import com.ybw.yibai.module.oldcustomer.OldCustomerContract.OldCustomerModel;

/**
 * 老客户界面Presenter实现类
 *
 * @author sjl
 * @date 2019/9/16
 */
public class OldCustomerPresenterImpl extends BasePresenterImpl<OldCustomerView> implements OldCustomerPresenter, CallBack {

    /**
     * PresenterImpl 持有 View的接口引用
     */
    private OldCustomerView mOldCustomersView;

    /**
     * PresenterImpl 持有 Model的接口引用
     */
    private OldCustomerModel mOldCustomersModel;

    /**
     * 构造方法
     *
     * @param view View的对象
     */
    public OldCustomerPresenterImpl(OldCustomerView view) {
        super(view);

        // 调用父类的方法获取View的对象
        this.mOldCustomersView = getView();
        mOldCustomersModel = new OldCustomerModelImpl();
    }

    /**
     * 获取客户列表
     */
    @Override
    public void getCustomerList() {
        mOldCustomersModel.getCustomerList(this);
    }

    /**
     * 在获取客户列表成功时回调
     *
     * @param customersList 客户列表数据
     */
    @Override
    public void onGetCustomerListSuccess(CustomerList customersList) {
        mOldCustomersView.onGetCustomerListSuccess(customersList);
    }
}
