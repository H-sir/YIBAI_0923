package com.ybw.yibai.module.quotationset;

import com.ybw.yibai.base.BasePresenterImpl;
import com.ybw.yibai.common.bean.SetIncrease;
import com.ybw.yibai.module.quotationset.QuotationSetContract.CallBack;
import com.ybw.yibai.module.quotationset.QuotationSetContract.QuotationSetModel;
import com.ybw.yibai.module.quotationset.QuotationSetContract.QuotationSetPresenter;
import com.ybw.yibai.module.quotationset.QuotationSetContract.QuotationSetView;

/**
 * 报价设置Presenter实现类
 *
 * @author sjl
 * @date 2019/12/2
 */
public class QuotationSetPresenterImpl extends BasePresenterImpl<QuotationSetView> implements QuotationSetPresenter, CallBack {

    /**
     * PresenterImpl 持有 View的接口引用
     */
    private QuotationSetView mQuotationSetView;

    /**
     * PresenterImpl 持有 Model的接口引用
     */
    private QuotationSetModel mQuotationSetModel;

    /**
     * 构造方法
     *
     * @param view View的对象
     */
    public QuotationSetPresenterImpl(QuotationSetView view) {
        super(view);

        // 调用父类的方法获取View的对象
        this.mQuotationSetView = getView();
        mQuotationSetModel = new QuotationSetModelImpl();
    }

    /**
     * 设置价格增幅
     *
     * @param increaseRent 租价幅度%
     * @param increaseSell 售价幅度%
     */
    @Override
    public void setIncrease(String increaseRent, String increaseSell) {
        mQuotationSetModel.setIncrease(increaseRent, increaseSell, this);
    }

    /**
     * 在设置设置价格增幅成功时回调
     *
     * @param setIncrease 设置价格增幅时服务器端返回的数据
     */
    @Override
    public void onSetIncreaseSuccess(SetIncrease setIncrease) {
        mQuotationSetView.onSetIncreaseSuccess(setIncrease);
    }
}
