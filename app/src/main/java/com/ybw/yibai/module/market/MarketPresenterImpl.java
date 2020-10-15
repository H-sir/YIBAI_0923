package com.ybw.yibai.module.market;

import com.ybw.yibai.base.BasePresenterImpl;
import com.ybw.yibai.common.bean.SkuMarketBean;

/**
 * <pre>
 *     author : HKR
 *     time   : 2020/09/02
 *     desc   :多货源Presenter实现类
 * </pre>
 */
public class MarketPresenterImpl extends BasePresenterImpl<MarketContract.MarketView>
        implements MarketContract.MarketPresenter, MarketContract.CallBack {

    private static final String TAG = "MarketPresenterImpl";

    private MarketContract.MarketView mMarketView;
    private MarketContract.MarketModel mMarketModel;

    /**
     * 构造方法
     *
     * @param view View的对象
     */
    public MarketPresenterImpl(MarketContract.MarketView view) {
        super(view);
        // 调用父类的方法获取View的对象
        this.mMarketView = getView();
        mMarketModel = new MarketModelImpl();
    }

    @Override
    public void getSkuMarket(int productSkuId) {
        mMarketModel.getSkuMarket(productSkuId, this);
    }

    @Override
    public void onGetSkuMarketSuccess(SkuMarketBean skuMarketBean) {
        mMarketView.onGetSkuMarketSuccess(skuMarketBean);
    }

    @Override
    public void addPurcart(int productSkuId, int gateProductId) {
        mMarketModel.addPurcart(productSkuId,gateProductId,this);
    }

    @Override
    public void onAddPurcartSuccess() {
        mMarketView.onAddPurcartSuccess();
    }
}
