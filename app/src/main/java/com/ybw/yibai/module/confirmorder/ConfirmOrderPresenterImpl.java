package com.ybw.yibai.module.confirmorder;

import com.google.gson.Gson;
import com.ybw.yibai.base.BasePresenterImpl;
import com.ybw.yibai.base.YiBaiApplication;
import com.ybw.yibai.common.bean.Address;
import com.ybw.yibai.common.bean.PurchaseOrder;
import com.ybw.yibai.common.bean.PurchaseOrderData;
import com.ybw.yibai.common.bean.QuotationData;
import com.ybw.yibai.module.confirmorder.ConfirmOrderContract.CallBack;
import com.ybw.yibai.module.confirmorder.ConfirmOrderContract.ConfirmOrderModel;
import com.ybw.yibai.module.confirmorder.ConfirmOrderContract.ConfirmOrderPresenter;
import com.ybw.yibai.module.confirmorder.ConfirmOrderContract.ConfirmOrderView;

import org.xutils.DbManager;
import org.xutils.ex.DbException;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;

import static com.ybw.yibai.common.constants.Encoded.CODE_SUCCEED;

/**
 * 确认进货订单Presenter实现类
 *
 * @author sjl
 * @date 2019/10/10
 */
public class ConfirmOrderPresenterImpl extends BasePresenterImpl<ConfirmOrderView> implements ConfirmOrderPresenter, CallBack {

    /**
     * PresenterImpl 持有 View的接口引用
     */
    private ConfirmOrderView mConfirmOrderView;

    /**
     * PresenterImpl 持有 Model的接口引用
     */
    private ConfirmOrderModel mConfirmOrderModel;

    /**
     * 构造方法
     *
     * @param view View的对象
     */
    public ConfirmOrderPresenterImpl(ConfirmOrderView view) {
        super(view);

        // 调用父类的方法获取View的对象
        this.mConfirmOrderView = getView();
        mConfirmOrderModel = new ConfirmOrderModelImpl();
    }

    /**
     * 计算总批发价格
     *
     * @param quotationDataList 用户保存的"报价"数据列表
     */
    @Override
    public void getTotalTradePrice(List<QuotationData> quotationDataList) {
        // 总批发价格
        double totalTradePrice = 0;

        for (QuotationData quotationData : quotationDataList) {
            // 主产品批发价
            double productTradePrice = quotationData.getProductTradePrice();
            // 附加产品批发价
            double augmentedProductTradePrice = quotationData.getAugmentedProductTradePrice();
            // 数量
            int number = quotationData.getNumber();

            totalTradePrice += (productTradePrice + augmentedProductTradePrice) * number;
        }

        // 保留两位小数
        BigDecimal bigDecimal = new BigDecimal(totalTradePrice);
        double v = bigDecimal.setScale(2, RoundingMode.HALF_EVEN).doubleValue();
        mConfirmOrderView.onGetTotalTradePriceSuccess(v);
    }

    /**
     * 获取收货地址
     */
    @Override
    public void getShippingAddress() {
        mConfirmOrderModel.getShippingAddress(this);
    }

    /**
     * 在获取收货地址成功时回调
     *
     * @param receiptAddress 收获地址
     */
    @Override
    public void onGetShippingAddressSuccess(Address receiptAddress) {
        mConfirmOrderView.onGetShippingAddressSuccess(receiptAddress);
    }

    /**
     * 创建采购订单
     *
     * @param quotationDataList 用户保存的"报价"数据列表
     * @param addressId         收货地址id
     */
    @Override
    public void createPurchaseOrder(List<QuotationData> quotationDataList, int addressId) {
        List<PurchaseOrderData> purchaseOrderDataList = new ArrayList<>();
        for (QuotationData quotationData : quotationDataList) {
            int productSkuId = quotationData.getProductSkuId();
            int augmentedProductSkuId = quotationData.getAugmentedProductSkuId();
            int number = quotationData.getNumber();

            if (0 != productSkuId) {
                purchaseOrderDataList.add(new PurchaseOrderData(productSkuId, number));
            }
            if (0 != augmentedProductSkuId) {
                purchaseOrderDataList.add(new PurchaseOrderData(augmentedProductSkuId, number));
            }
        }
        String json = new Gson().toJson(purchaseOrderDataList);
        mConfirmOrderModel.createPurchaseOrder(json, addressId, this);
    }

    /**
     * 在创建采购订单成功时回调
     *
     * @param purchaseOrder 创建采购订单时服务器端返回的数据
     */
    @Override
    public void onCreatePurchaseOrderSuccess(PurchaseOrder purchaseOrder) {
        // 成功后删除用户保存的"报价"数据
        if (CODE_SUCCEED == purchaseOrder.getCode()) {
            try {
                DbManager dbManager = YiBaiApplication.getDbManager();
                dbManager.delete(QuotationData.class);
            } catch (DbException e) {
                e.printStackTrace();
            }
        }
        mConfirmOrderView.onCreatePurchaseOrderSuccess(purchaseOrder);
    }
}
