package com.ybw.yibai.module.collection;

import android.graphics.Bitmap;

import com.ybw.yibai.base.BaseCallBack;
import com.ybw.yibai.base.BasePresenter;
import com.ybw.yibai.base.BaseView;
import com.ybw.yibai.common.bean.CollectionListBean;
import com.ybw.yibai.common.bean.ProductScreeningParam;
import com.ybw.yibai.common.bean.SkuDetailsBean;

import java.util.List;

/**
 * 合约模式,将三个Model,View,Presenter接口以及CallBack接口放在一个Contract接口里面方便统一管理
 *
 * @author sjl
 * @date 2019/12/10
 */
public interface CollectionLayoutContract {

    interface CollectionLayoutView extends BaseView {

        void onCollectionListBeanSuccess(CollectionListBean collectionListBean);

        void onDeleteCollectionListSuccess(List<String> skuOrCollectId);

        void onGetProductDetailsSuccess(SkuDetailsBean skuDetailsBean);

        void onSaveSimulationResult(boolean result);
    }

    interface CollectionLayoutPresenter extends BasePresenter<CollectionLayoutView> {
        /**
         * 获取收藏列表
         *
         * @param type 1单品 2组合产品(默认为1)
         * @param page 分页数
         */
        void getCollect(int type, int page);

        /**
         * 根据ID删除
         */
        void deleteCollection(List<String> skuOrCollectId);

        /**
         * 更新skuid的收藏
         */
        void upuseskuCollection(List<String> skuOrCollectId);

        void getSkuListIds(String sku_id, String pot_sku_id);

        void saveSimulation(SkuDetailsBean.DataBean.ListBean listBean);

        void saveSimulation(SkuDetailsBean.DataBean.ListBean plant, SkuDetailsBean.DataBean.ListBean pot);
    }

    interface CollectionLayoutModel {

        void getCollect(int type, int page, CallBack callBack);

        void deleteCollection(List<String> skuOrCollectId, CallBack callBack);

        void upuseskuCollection(List<String> skuOrCollectId, CallBack callBack);

        void getSkuListIds(String sku_id, String pot_sku_id, CallBack callBack);

        void saveSimulation(SkuDetailsBean.DataBean.ListBean listBean, Bitmap bitmap, CallBack callBack);

        void saveSimulationData(SkuDetailsBean.DataBean.ListBean plantBean, SkuDetailsBean.DataBean.ListBean potBean, Bitmap bitmap, CallBack callBack);
    }

    interface CallBack extends BaseCallBack {

        void onCollectionListBeanSuccess(CollectionListBean collectionListBean);

        void onDeleteCollectionListSuccess(List<String> skuOrCollectId);

        void onGetProductDetailsSuccess(SkuDetailsBean skuDetailsBean);

        void onSaveSimulationResult(boolean result);
    }
}
