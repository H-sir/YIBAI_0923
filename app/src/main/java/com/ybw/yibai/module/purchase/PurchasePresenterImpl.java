package com.ybw.yibai.module.purchase;

import android.app.Activity;
import android.support.v4.app.Fragment;

import com.ybw.yibai.R;
import com.ybw.yibai.base.BasePresenterImpl;
import com.ybw.yibai.common.bean.QuotationData;
import com.ybw.yibai.common.utils.OtherUtil;
import com.ybw.yibai.common.widget.CustomDialog;
import com.ybw.yibai.module.purchase.PurchaseContract.CallBack;
import com.ybw.yibai.module.purchase.PurchaseContract.PurchaseModel;
import com.ybw.yibai.module.purchase.PurchaseContract.PurchasePresenter;
import com.ybw.yibai.module.purchase.PurchaseContract.PurchaseView;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

/**
 * 进货Fragment Presenter 实现类
 *
 * @author sjl
 * @date 2019/11/5
 */
public class PurchasePresenterImpl extends BasePresenterImpl<PurchaseView> implements PurchasePresenter, CallBack {

    /**
     * PresenterImpl 持有 View的接口引用
     */
    private PurchaseView mPurchaseView;

    /**
     * PresenterImpl 持有 Model的接口引用
     */
    private PurchaseModel mPurchaseModel;

    /**
     * 构造方法
     *
     * @param view View的对象
     */
    public PurchasePresenterImpl(PurchaseView view) {
        super(view);

        // 调用父类的方法获取View的对象
        this.mPurchaseView = getView();
        mPurchaseModel = new PurchaseModelImpl();
    }

    /**
     * 获取用户保存的"报价"数据
     */
    @Override
    public void getQuotationData() {
        mPurchaseModel.getQuotationData(this);
    }

    /**
     * 在获取用户保存的"报价"数据成功时回调
     *
     * @param quotationDataList 用户保存的"报价"数据列表
     */
    @Override
    public void onGetQuotationDataSuccess(List<QuotationData> quotationDataList) {
        mPurchaseView.onGetQuotationDataSuccess(quotationDataList);
    }

    /**
     * 更新用户保存的"报价"数据
     *
     * @param quotationData 需要更新的"报价"数据
     */
    @Override
    public void updateQuotationData(QuotationData quotationData) {
        mPurchaseModel.updateQuotationData(quotationData, this);
    }

    /**
     * 在更新用户保存的"报价"数据完成时回调
     *
     * @param result 结果 true成功/ false失败
     */
    @Override
    public void onUpdateQuotationDataFinish(boolean result) {
        mPurchaseView.onUpdateQuotationDataFinish(result);
    }

    /**
     * 显示是否删除该盆栽
     *
     * @param quotationData 该盆栽数据
     */
    @Override
    public void displayAreYouSureToDeleteTheBonsaiDialog(QuotationData quotationData) {
        Fragment fragment = (Fragment) mPurchaseView;
        Activity activity = fragment.getActivity();
        if (null != activity) {
            String name = quotationData.getProductName();
            OtherUtil.setBackgroundAlpha(activity, 0.6f);
            CustomDialog customDialog = new CustomDialog(activity);
            customDialog.setMessage(activity.getResources().getString(R.string.are_you_sure_to_delete) + name);
            customDialog.setYesOnclickListener(activity.getResources().getString(R.string.determine),
                    () -> {
                        OtherUtil.setBackgroundAlpha(activity, 1f);
                        customDialog.dismiss();
                        mPurchaseModel.deleteQuotationData(quotationData, this);
                    });
            customDialog.setNoOnclickListener(activity.getResources().getString(R.string.cancel),
                    () -> {
                        OtherUtil.setBackgroundAlpha(activity, 1f);
                        customDialog.dismiss();
                    });
            customDialog.show();
        }
    }

    /**
     * 在删除用户保存的"报价"数据完成时回调
     *
     * @param result 结果 true成功/ false失败
     */
    @Override
    public void onDeleteQuotationDataFinish(boolean result) {
        mPurchaseView.onDeleteQuotationDataFinish(result);
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
        mPurchaseView.onGetTotalTradePriceSuccess(v);
    }
}
