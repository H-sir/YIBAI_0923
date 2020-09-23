package com.ybw.yibai.module.market;

import com.ybw.yibai.base.BaseCallBack;
import com.ybw.yibai.base.BasePresenter;
import com.ybw.yibai.base.BaseView;
import com.ybw.yibai.common.bean.SkuMarketBean;

/**
 * <pre>
 *     author : HKR
 *     time   : 2020/09/02
 *     desc   :
 * </pre>
 */
public interface MarketContract {
    interface MarketView extends BaseView {
        /**
         * 多货源查询成功回调
         */
        void onGetSkuMarketSuccess(SkuMarketBean designDetails);
    }

    interface MarketPresenter extends BasePresenter<MarketView> {

        /**
         * 多货源查询
         */
        void getSkuMarket(int productSkuId);
    }

    interface MarketModel {
        /**
         * 多货源查询
         */
        void getSkuMarket(int productSkuId, CallBack callBack);
    }

    interface CallBack extends BaseCallBack {
        /**
         * 多货源查询成功回调
         */
        void onGetSkuMarketSuccess(SkuMarketBean designDetails);
    }
}
