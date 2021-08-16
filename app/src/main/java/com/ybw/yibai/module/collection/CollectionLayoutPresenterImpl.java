package com.ybw.yibai.module.collection;


import android.app.Activity;
import android.graphics.Bitmap;
import android.support.v4.app.Fragment;
import android.text.TextUtils;

import com.ybw.yibai.base.BasePresenterImpl;
import com.ybw.yibai.common.bean.CollectionListBean;
import com.ybw.yibai.common.bean.SkuDetailsBean;
import com.ybw.yibai.common.utils.ImageUtil;
import com.ybw.yibai.module.hotscheme.HotSchemePresenterImpl;
import com.ybw.yibai.module.producttype.ProductTypePresenterImpl;

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

    @Override
    public void getSkuListIds(String sku_id, String pot_sku_id) {
        mCollectionLayoutModel.getSkuListIds(sku_id,pot_sku_id,this);
    }

    @Override
    public void onGetProductDetailsSuccess(SkuDetailsBean skuDetailsBean) {
        mCollectionLayoutView.onGetProductDetailsSuccess(skuDetailsBean);
    }

    @Override
    public void onSaveSimulationResult(boolean result) {
        mCollectionLayoutView.onSaveSimulationResult(result);
    }

    @Override
    public void saveSimulation(SkuDetailsBean.DataBean.ListBean listBean) {
        Activity activity = (Activity) mCollectionLayoutView;
        String productPic2 = listBean.getPic2();
        if (TextUtils.isEmpty(productPic2)) {
            return;
        }
        ImageUtil.downloadPicture(activity, new ImageUtil.DownloadCallback() {
            @Override
            public void onDownloadStarted() {

            }

            @Override
            public void onDownloadFinished(List<Bitmap> bitmapList) {
                Bitmap bitmap = bitmapList.get(0);
                if (null == bitmap) {
                    // 下载图片失败return
                    return;
                }
                mCollectionLayoutModel.saveSimulation(
                        listBean,
                        bitmap,
                        CollectionLayoutPresenterImpl.this
                );
            }

            @Override
            public void onDownloading(int sumTotal, int successesAmount, int failuresAmount, int completedAmount) {

            }
        }, productPic2);
    }

    @Override
    public void saveSimulation(SkuDetailsBean.DataBean.ListBean plantBean, SkuDetailsBean.DataBean.ListBean potBean) {
        Activity activity = (Activity) mCollectionLayoutView;

        String productPic3 = plantBean.getPic3();
        double productHeight = plantBean.getHeight();
        double productOffsetRatio = plantBean.getOffset_ratio();

        String augmentedProductPic3 = potBean.getPic3();
        double augmentedProductHeight = potBean.getHeight();
        double augmentedProductOffsetRatio = potBean.getOffset_ratio();

        if (TextUtils.isEmpty(productPic3) || TextUtils.isEmpty(augmentedProductPic3)) {
            return;
        }
        ImageUtil.downloadPicture(activity, new ImageUtil.DownloadCallback() {
            @Override
            public void onDownloadStarted() {

            }

            @Override
            public void onDownloadFinished(List<Bitmap> bitmapList) {
                // 标记是否存在下载图片失败的情况
                boolean isFailed = false;
                Bitmap[] bitmaps = new Bitmap[bitmapList.size()];
                for (int i = 0; i < bitmapList.size(); i++) {
                    Bitmap bitmap = bitmapList.get(i);
                    if (null == bitmap) {
                        isFailed = true;
                        break;
                    } else {
                        bitmaps[i] = bitmap;
                    }
                }
                if (isFailed) {
                    //  下载图片失败return
                    return;
                }
                Bitmap bitmap = ImageUtil.pictureSynthesis(
                        productHeight,
                        augmentedProductHeight,
                        productOffsetRatio,
                        augmentedProductOffsetRatio,
                        bitmaps
                );
                if (null == bitmap) {
                    // 合成图片失败return
                    return;
                }
                mCollectionLayoutModel.saveSimulationData(
                        plantBean,
                        potBean,
                        bitmap,
                        CollectionLayoutPresenterImpl.this
                );
            }

            @Override
            public void onDownloading(int sumTotal, int successesAmount, int failuresAmount, int completedAmount) {

            }
        }, productPic3, augmentedProductPic3);
    }
}
