package com.ybw.yibai.module.customerlist;

import com.ybw.yibai.base.BasePresenterImpl;
import com.ybw.yibai.common.bean.CustomerList;
import com.ybw.yibai.module.customerlist.CustomerListContract.CallBack;
import com.ybw.yibai.module.customerlist.CustomerListContract.CustomerListModel;
import com.ybw.yibai.module.customerlist.CustomerListContract.CustomerListPresenter;
import com.ybw.yibai.module.customerlist.CustomerListContract.CustomerListView;

/**
 * 客户列表Presenter实现类
 *
 * @author sjl
 * @date 2019/10/14
 */
public class CustomerListPresenterImpl extends BasePresenterImpl<CustomerListView> implements CustomerListPresenter, CallBack {

    /**
     * PresenterImpl 持有 View的接口引用
     */
    private CustomerListView mCustomerListView;

    /**
     * PresenterImpl 持有 Model的接口引用
     */
    private CustomerListModel mCustomerListModel;

    /**
     * 构造方法
     *
     * @param view View的对象
     */
    public CustomerListPresenterImpl(CustomerListView view) {
        super(view);

        // 调用父类的方法获取View的对象
        this.mCustomerListView = getView();
        mCustomerListModel = new CustomerListModelImpl();
    }

    /**
     * 获取客户列表
     */
    @Override
    public void getCustomerList() {
        mCustomerListModel.getCustomerList(this);
    }

    /**
     * 在获取客户列表成功时回调
     *
     * @param customerList 客户列表数据
     */
    @Override
    public void onGetCustomerListSuccess(CustomerList customerList) {
        mCustomerListView.onGetCustomerListSuccess(customerList);
    }
}
