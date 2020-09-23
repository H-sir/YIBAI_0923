package com.ybw.yibai.module.purchaseorder;

import com.ybw.yibai.base.BasePresenterImpl;
import com.ybw.yibai.common.bean.PurchaseOrderList;
import com.ybw.yibai.module.purchaseorder.PurchaseOrderContract.CallBack;
import com.ybw.yibai.module.purchaseorder.PurchaseOrderContract.PurchaseOrderModel;
import com.ybw.yibai.module.purchaseorder.PurchaseOrderContract.PurchaseOrderPresenter;
import com.ybw.yibai.module.purchaseorder.PurchaseOrderContract.PurchaseOrderView;

/**
 * 已进货订单Presenter实现类
 *
 * @author sjl
 * @date 2019/10/14
 */
public class PurchaseOrderPresenterImpl extends BasePresenterImpl<PurchaseOrderView> implements PurchaseOrderPresenter, CallBack {

    /**
     * PresenterImpl 持有 View的接口引用
     */
    private PurchaseOrderView mPurchaseOrderView;

    /**
     * PresenterImpl 持有 Model的接口引用
     */
    private PurchaseOrderModel mPurchaseOrderModel;

    /**
     * 构造方法
     *
     * @param view View的对象
     */
    public PurchaseOrderPresenterImpl(PurchaseOrderView view) {
        super(view);

        // 调用父类的方法获取View的对象
        this.mPurchaseOrderView = getView();
        mPurchaseOrderModel = new PurchaseOrderModelImpl();
    }

    /**
     * 获取获取采购单列表
     *
     * @param isAll 0分页1不分页默认为1
     * @param state 状态类型: 0/空获取所有,1待付款,2待配送3待收货4已完成
     */
    @Override
    public void getPurchaseOrderList(int isAll, int state) {
        mPurchaseOrderModel.getPurchaseOrderList(isAll, state, this);
    }

    /**
     * 在获取获取采购单列表成功时回调
     *
     * @param purchaseOrderList 采购单列表
     */
    @Override
    public void onGetPurchaseOrderListSuccess(PurchaseOrderList purchaseOrderList) {
        mPurchaseOrderView.onGetPurchaseOrderListSuccess(purchaseOrderList);
    }
}
