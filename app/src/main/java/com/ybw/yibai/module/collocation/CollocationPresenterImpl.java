package com.ybw.yibai.module.collocation;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.ViewGroup;

import com.ybw.yibai.R;
import com.ybw.yibai.base.BasePresenterImpl;
import com.ybw.yibai.common.bean.AddQuotation;
import com.ybw.yibai.common.bean.ProductData;
import com.ybw.yibai.common.bean.SKUList;
import com.ybw.yibai.common.bean.SimilarPlantSKU;
import com.ybw.yibai.common.utils.FileUtil;
import com.ybw.yibai.common.utils.ImageUtil;
import com.ybw.yibai.common.utils.MessageUtil;
import com.ybw.yibai.common.utils.TimeUtil;
import com.ybw.yibai.module.collocation.CollocationContract.CallBack;
import com.ybw.yibai.module.collocation.CollocationContract.CollocationModel;
import com.ybw.yibai.module.collocation.CollocationContract.CollocationPresenter;
import com.ybw.yibai.module.collocation.CollocationContract.CollocationView;

import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.MediaType;
import okhttp3.RequestBody;

import static com.ybw.yibai.common.constants.Folder.PICTURES_PATH;

/**
 * 搭配界面Presenter实现类
 *
 * @author sjl
 * @date 2019/9/20
 */
@SuppressWarnings("all")
public class CollocationPresenterImpl extends BasePresenterImpl<CollocationView> implements CollocationPresenter, CallBack {

    private static final String TAG = "CollocationPresenterImpl";

    /**
     * "搭配图片的布局"的宽度和高度占屏幕宽度比例因子
     */
    private float divisor = 0.8f;

    /**
     * PresenterImpl 持有 View的接口引用
     */
    private CollocationView mCollocationView;

    /**
     * PresenterImpl 持有 Model的接口引用
     */
    private CollocationModel mCollocationModel;

    /**
     * 构造方法
     *
     * @param view View的对象
     */
    public CollocationPresenterImpl(CollocationView view) {
        super(view);

        // 调用父类的方法获取View的对象
        this.mCollocationView = getView();
        mCollocationModel = new CollocationModelImpl();
    }

    /**
     * 获取搭配时植物sku推荐列表
     *
     * @param skuId 植物SKUid
     */
    @Override
    public void getSimilarPlantSKUList(int skuId) {
        mCollocationModel.getSimilarPlantSKUList(skuId, this);
    }

    /**
     * 在获取搭配时植物sku推荐列表成功时回调
     *
     * @param similarPlantSKU 获取搭配时植物sku推荐列表时服务器端返回的数据
     */
    @Override
    public void onGetSimilarPlantSKUListSuccess(SimilarPlantSKU similarPlantSKU) {
        mCollocationView.onGetSimilarPlantSKUListSuccess(similarPlantSKU);
    }

    /**
     * 获取搭配时盆器sku推荐列表
     *
     * @param cateCode    产品类别
     * @param curDiameter 当前口径:搭配口径+边距*2的值
     * @param diameter    搭配口径:格式同上
     */
    @Override
    public void getSimilarPotSKUList(String cateCode, double curDiameter, double diameter) {
        mCollocationModel.getSimilarPotSKUList(cateCode, curDiameter, diameter, this);
    }

    /**
     * 在获取搭配时盆器sku推荐列表成功时回调
     *
     * @param similarPotSKU 获取搭配时盆器sku推荐列表时服务器端返回的数据
     */
    @Override
    public void onGetSimilarPotSKUListSuccess(SKUList similarPotSKU) {
        mCollocationView.onGetSimilarPotSKUListSuccess(similarPotSKU);
    }

    /**
     * 动态设置"搭配图片的布局"的宽度和高度
     *
     * @param collocationLayout 搭配图片布局的容器
     */
    @Override
    public void setCollocationRootLayoutParams(View collocationLayout) {
        // 1:获取屏幕的宽
        Activity activity = (Activity) mCollocationView;
        DisplayMetrics dm = activity.getResources().getDisplayMetrics();
        int width = (int) (dm.widthPixels * divisor);
        // 2:将屏幕的宽设置为"搭配图片的布局"的宽度和高度
        ViewGroup.LayoutParams params = collocationLayout.getLayoutParams();
        params.width = width;
        params.height = width;
        collocationLayout.setLayoutParams(params);
    }

    /**
     * 动态设置"搭配图片的布局里面的ViewPager,ViewPager的高度,使其比例与植物高度:盆器高度比例一致
     *
     * @param plantViewPager 放置"植物自由搭配图"的ViewPager
     * @param potViewPager   放置"盆器自由搭配图"的ViewPager
     * @param plantHeight    用户当前选择的植物规格的高度
     * @param potHeight      用户当前选择的盆规格的高度
     * @param offsetRatio    花盆的偏移量
     */
    @Override
    public void setCollocationContentParams(ViewPager plantViewPager, ViewPager potViewPager, double plantHeight, double potHeight, double plantOffsetRatio, double potOffsetRatio) {
        if (0 == plantHeight || 0 == potHeight) {
            // 如果有高度为0,那两者的高度按XML文件里面的来
            return;
        }
        // 1:获取屏幕的宽
        Activity activity = (Activity) mCollocationView;
        DisplayMetrics dm = activity.getResources().getDisplayMetrics();
        int width = (int) (dm.widthPixels * divisor);

        // 将屏幕的宽度/(植物高度 + 盆的高度 - 花盆的偏移量) = 每一份的高度,保留2为小数
        double portionHeight = new BigDecimal((float) width / (plantHeight + potHeight - plantOffsetRatio - potOffsetRatio)).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();

        ViewGroup.LayoutParams plantImageViewParams = plantViewPager.getLayoutParams();
        plantImageViewParams.width = width;
        plantImageViewParams.height = (int) (portionHeight * plantHeight);
        plantViewPager.setLayoutParams(plantImageViewParams);

        ViewGroup.LayoutParams viewPagerParams = potViewPager.getLayoutParams();
        viewPagerParams.width = width;
        viewPagerParams.height = (int) (portionHeight * potHeight);
        potViewPager.setLayoutParams(viewPagerParams);
    }

    /**
     * 添加到模拟列表
     *
     * @param pottingData 产品相关数据
     */
    @Override
    public void saveSimulationData(ProductData productData) {
        downloadAndSynthesis(productData, 1, false);
    }

    /**
     * 加入预选
     *
     * @param pottingData 产品相关数据
     */
    @Override
    public void joinPreselection(ProductData productData) {
        downloadAndSynthesis(productData, 2, false);
    }

    /**
     * 加入相册
     *
     * @param productData 产品相关数据
     */
    @Override
    public void joinPhotoAlbum(ProductData productData) {
        downloadAndSynthesis(productData, 3, true);
    }

    /**
     * 加入进货
     *
     * @param productData 产品相关数据
     */
    @Override
    public void saveQuotationData(ProductData productData) {
        downloadAndSynthesis(productData, 4, false);
    }

    /**
     * 下载图片与合成图片
     *
     * @param productData    产品相关数据
     * @param type           类型 1: 添加到模拟列表 2: 加入预选 3: 加入相册 4: 加入进货
     * @param joinPhotoAlbum 是否将合成的图片添加到相册
     */
    private void downloadAndSynthesis(ProductData productData, int type, boolean joinPhotoAlbum) {
        Activity activity = (Activity) mCollocationView;
        String productPic3 = productData.getProductPic3();
        String augmentedProductPic3 = productData.getAugmentedProductPic3();

        if (!TextUtils.isEmpty(productPic3) && !TextUtils.isEmpty(augmentedProductPic3)) {
            ImageUtil.downloadPicture(activity, new ImageUtil.DownloadCallback() {
                @Override
                public void onDownloadStarted() {

                }

                @Override
                public void onDownloadFinished(List<Bitmap> bitmapList) {
                    handleOnDownloadFinished(productData, type, 2, bitmapList, joinPhotoAlbum);
                }

                @Override
                public void onDownloading(int sumTotal, int successesAmount, int failuresAmount, int completedAmount) {

                }
            }, productPic3, augmentedProductPic3);
        } else if (!TextUtils.isEmpty(productPic3)) {
            ImageUtil.downloadPicture(activity, new ImageUtil.DownloadCallback() {
                @Override
                public void onDownloadStarted() {

                }

                @Override
                public void onDownloadFinished(List<Bitmap> bitmapList) {
                    handleOnDownloadFinished(productData, type, 1, bitmapList, joinPhotoAlbum);
                }

                @Override
                public void onDownloading(int sumTotal, int successesAmount, int failuresAmount, int completedAmount) {

                }
            }, productPic3);
        }
    }

    /**
     * 处理在下载全部图片完成之后的一些事情
     *
     * @param pottingData    产品相关数据
     * @param type           类型 1: 添加到模拟列表 2: 加入预选 3: 加入相册 4: 加入进货
     * @param targetNumber   要成功下载图片的目标数量
     * @param bitmapList     下载完成的图片
     * @param joinPhotoAlbum 是否将合成的图片添加到相册
     */
    private void handleOnDownloadFinished(ProductData productData, int type, int targetNumber, List<Bitmap> bitmapList, boolean joinPhotoAlbum) {
        double productHeight = productData.getProductHeight();
        double productOffsetRatio = productData.getProductOffsetRatio();
        double augmentedProductHeight = productData.getAugmentedProductHeight();
        double augmentedProductOffsetRatio = productData.getAugmentedProductOffsetRatio();

        if (1 == targetNumber) {
            Bitmap bitmap = bitmapList.get(0);
            if (null == bitmap && 1 == type) {
                // 下载图片失败,在添加到模拟列表的情况下,重新就要return了
                return;
            }
            if (joinPhotoAlbum) {
                joinPhotoAlbum(bitmap);
                return;
            }
            mCollocationModel.saveProductData(productData, bitmap, type, this);
        } else if (2 == targetNumber) {
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
            if (isFailed && (1 == type || 4 == type)) {
                //  下载图片失败,在添加到模拟列表/加入进货的情况下,重新就要return了
                return;
            }
            Bitmap bitmap = ImageUtil.pictureSynthesis(productHeight, augmentedProductHeight, productOffsetRatio, augmentedProductOffsetRatio, bitmaps);
            if (null == bitmap && (1 == type || 4 == type)) {
                //  下载图片失败,在添加到模拟列表/加入进货的情况下,重新就要return了
                return;
            }
            if (joinPhotoAlbum) {
                joinPhotoAlbum(bitmap);
                return;
            }
            if (2 != type && 4 != type) {
                mCollocationModel.saveProductData(productData, bitmap, type, this);
                return;
            }

            int productSkuId = productData.getProductSkuId();
            int augmentedProductSkuId = productData.getAugmentedProductSkuId();
            String picturePath = ImageUtil.saveImage(bitmap, String.valueOf(TimeUtil.getTimeStamp()));

            File file = new File(picturePath);
            Map<String, RequestBody> params = new HashMap<>();
            String fileName = file.getName();
            String suffix = fileName.substring(fileName.lastIndexOf(".") + 1);
            RequestBody requestBody = RequestBody.create(MediaType.parse("image/" + suffix), file);
            params.put("com_pic\"; filename=\"" + file.getName(), requestBody);

            if (type == 2)
                mCollocationModel.addQuotation(productSkuId, augmentedProductSkuId, params, this);
            else if (type == 4)
                mCollocationModel.addPurcart(productSkuId, augmentedProductSkuId, params, this);
        }
    }

    /**
     * 在保存"模拟"数据成功时回调
     */
    @Override
    public void onSaveSimulationDataSuccess() {
        mCollocationView.onSaveSimulationDataSuccess();
    }

    /**
     * 在保存"报价"数据成功时回调
     */
    @Override
    public void onSaveQuotationDataDataSuccess() {
        mCollocationView.onSaveQuotationDataDataSuccess();
    }

    /**
     * 在保存"模拟"or"报价"数据失败时回调
     */
    @Override
    public void onSaveSimulationOrQuotationDataFairly() {
        mCollocationView.onSaveSimulationOrQuotationDataFairly();
    }

    /**
     * 在sku加入待摆放清单成功时回调
     *
     * @param addQuotation sku加入待摆放清单时服务器端返回的数据
     */
    @Override
    public void onAddQuotationSuccess(AddQuotation addQuotation) {
        mCollocationView.onAddQuotationSuccess(addQuotation);
    }

    /**
     * 将合成的图片添加到相册
     *
     * @param bitmap 合成的图片
     */
    private void joinPhotoAlbum(Bitmap bitmap) {
        Activity activity = (Activity) mCollocationView;
        String imageFilePath = ImageUtil.saveImage(bitmap, String.valueOf(TimeUtil.getTimeStamp()));
        String path = FileUtil.createExternalStorageFile(PICTURES_PATH);
        if (TextUtils.isEmpty(path)) {
            MessageUtil.showMessage(activity.getResources().getString(R.string.failed_to_save_image));
            return;
        }
        File file = new File(imageFilePath);
        try {
            FileUtil.copyFile(imageFilePath, path + file.getName());
            // 发送广播通知相册更新图片
            Intent intent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
            Uri uri = Uri.fromFile(new File(path + file.getName()));
            intent.setData(uri);
            activity.sendBroadcast(intent);
            MessageUtil.showMessage(activity.getResources().getString(R.string.image_saved_to) + path + activity.getResources().getString(R.string.folder));
        } catch (IOException e) {
            e.printStackTrace();
            MessageUtil.showMessage(activity.getResources().getString(R.string.failed_to_save_image));
        }
    }
}
