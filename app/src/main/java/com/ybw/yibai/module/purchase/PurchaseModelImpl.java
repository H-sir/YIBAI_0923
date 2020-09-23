package com.ybw.yibai.module.purchase;

import com.ybw.yibai.base.YiBaiApplication;
import com.ybw.yibai.common.bean.QuotationData;
import com.ybw.yibai.module.purchase.PurchaseContract.CallBack;
import com.ybw.yibai.module.purchase.PurchaseContract.PurchaseModel;

import org.xutils.DbManager;
import org.xutils.ex.DbException;

import java.util.List;

/**
 * 进货Fragment Model 实现类
 *
 * @author sjl
 * @date 2019/11/5
 */
public class PurchaseModelImpl implements PurchaseModel {

    /**
     * 获取用户保存的"报价"数据
     *
     * @param callBack 回调方法
     */
    @Override
    public void getQuotationData(CallBack callBack) {
        try {
            int uid = YiBaiApplication.getUid();
            DbManager dbManager = YiBaiApplication.getDbManager();
            List<QuotationData> quotationDataList = dbManager
                    .selector(QuotationData.class)
                    .where("uid", "=", uid)
                    .findAll();
            callBack.onGetQuotationDataSuccess(quotationDataList);
        } catch (DbException e) {
            e.printStackTrace();
        }
    }

    /**
     * 更新用户保存的"报价"数据
     *
     * @param quotationData 需要更新的"报价"数据
     * @param callBack      回调方法
     */
    @Override
    public void updateQuotationData(QuotationData quotationData, CallBack callBack) {
        try {
            DbManager dbManager = YiBaiApplication.getDbManager();
            dbManager.update(quotationData);
            callBack.onUpdateQuotationDataFinish(true);
        } catch (DbException e) {
            e.printStackTrace();
            callBack.onUpdateQuotationDataFinish(false);
        }
    }

    /**
     * 删除用户保存的"报价"数据
     *
     * @param quotationData 需要删除的"报价"数据
     * @param callBack      回调方法
     */
    @Override
    public void deleteQuotationData(QuotationData quotationData, CallBack callBack) {
        try {
            DbManager dbManager = YiBaiApplication.getDbManager();
            dbManager.delete(quotationData);
            callBack.onDeleteQuotationDataFinish(true);
        } catch (DbException e) {
            e.printStackTrace();
            callBack.onDeleteQuotationDataFinish(false);
        }
    }
}
