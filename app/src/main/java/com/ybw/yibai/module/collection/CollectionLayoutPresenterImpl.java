package com.ybw.yibai.module.collection;


import com.ybw.yibai.base.BasePresenterImpl;
import com.ybw.yibai.common.bean.CollectionListBean;

import java.util.List;

/**
 * 产品使用状态Presenter实现类
 *
 * @author sjl
 * @date 2019/9/5
 */
public class CollectionLayoutPresenterImpl extends BasePresenterImpl<CollectionLayoutContract.CollectionLayoutView>
        implements CollectionLayoutContract.CollectionLayoutPresenter, CollectionLayoutContract.CallBack {

    /**
     * PresenterImpl 持有 View的接口引用
     */
    private CollectionLayoutContract.CollectionLayoutView mCollectionLayoutView;

    /**
     * PresenterImpl 持有 Model的接口引用
     */
    private CollectionLayoutContract.CollectionLayoutModel mCollectionLayoutModel;

    /**
     * 构造方法
     *
     * @param view View的对象
     */
    public CollectionLayoutPresenterImpl(CollectionLayoutContract.CollectionLayoutView view) {
        super(view);

        // 调用父类的方法获取View的对象
        this.mCollectionLayoutView = getView();
        mCollectionLayoutModel = new CollectionLayoutModelImpl();
    }

    @Override
    public void getCollect(int type, int page) {
        mCollectionLayoutModel.getCollect(type,page,this);
    }

    @Override
    public void onCollectionListBeanSuccess(CollectionListBean collectionListBean) {
        mCollectionLayoutView.onCollectionListBeanSuccess(collectionListBean);
    }

    @Override
    public void deleteCollection(List<String> skuOrCollectId) {
        mCollectionLayoutModel.deleteCollection(skuOrCollectId,this);
    }

    @Override
    public void onDeleteCollectionListSuccess(List<String> skuOrCollectId) {
        mCollectionLayoutView.onDeleteCollectionListSuccess(skuOrCollectId);
    }

    @Override
    public void upuseskuCollection(List<String> skuOrCollectId) {
        mCollectionLayoutModel.upuseskuCollection(skuOrCollectId,this);
    }
}
