package com.ybw.yibai.module.hotscheme;

import android.graphics.Bitmap;
import android.text.TextUtils;

import com.ybw.yibai.base.YiBaiApplication;
import com.ybw.yibai.common.bean.ListBean;
import com.ybw.yibai.common.bean.SceneInfo;
import com.ybw.yibai.common.bean.SimulationData;
import com.ybw.yibai.common.utils.ImageUtil;
import com.ybw.yibai.common.utils.LogUtil;
import com.ybw.yibai.common.utils.TimeUtil;
import com.ybw.yibai.module.hotscheme.HotSchemeContract.CallBack;
import com.ybw.yibai.module.hotscheme.HotSchemeContract.HotSchemeModel;

import org.xutils.DbManager;
import org.xutils.ex.DbException;

import java.util.List;

import static com.ybw.yibai.common.utils.FileUtil.judeFileExists;

/**
 * 热门场景界面Model实现类
 *
 * @author sjl
 * @date 2019/11/11
 */
public class HotSchemeModelImpl implements HotSchemeModel {

    public final String TAG = "HotSchemeModelImpl";

    /**
     * 保存到模拟列表数据
     *
     * @param plantBean 用户当前选择的植物信息
     * @param potBean   用户当前选择的盆器信息
     * @param bitmap    植物与盆栽合成后的位图
     * @param callBack  回调方法
     */
    @Override
    public void saveSimulationData(ListBean plantBean, ListBean potBean, Bitmap bitmap, CallBack callBack) {
        int width = 0;
        int height = 0;
        String picturePath = null;
        if (null != bitmap) {
            width = bitmap.getWidth();
            height = bitmap.getHeight();
            // 保存Bitmap为图片到本地
            picturePath = ImageUtil.saveImage(bitmap, String.valueOf(TimeUtil.getTimeStamp()));
        }

        int productSkuId = plantBean.getSku_id();
        String productName = plantBean.getName();
        double productPrice = plantBean.getPrice();
        double productMonthRent = plantBean.getMonth_rent();
        double productTradePrice = plantBean.getTrade_price();
        String productPic1 = plantBean.getPic1();
        String productPic2 = plantBean.getPic2();
        String productPic3 = plantBean.getPic3();
        double productHeight = plantBean.getHeight();

        int augmentedProductSkuId = potBean.getSku_id();
        String augmentedProductName = potBean.getName();
        double augmentedProductPrice = potBean.getPrice();
        double augmentedProductMonthRent = potBean.getMonth_rent();
        double augmentedProductTradePrice = potBean.getTrade_price();
        String augmentedProductPic1 = potBean.getPic1();
        String augmentedProductPic2 = potBean.getPic2();
        String augmentedProductPic3 = potBean.getPic3();
        double augmentedProductHeight = potBean.getHeight();
        double augmentedProductOffsetRatio = potBean.getOffset_ratio();

        int uid = YiBaiApplication.getUid();
        String finallySkuId = String.valueOf(productSkuId) + augmentedProductSkuId + uid;

        LogUtil.e(TAG, "主产品的款名Id: " + productSkuId);
        LogUtil.e(TAG, "主产品名称: " + productName);
        LogUtil.e(TAG, "主产品价格: " + productPrice);
        LogUtil.e(TAG, "主产品月租: " + productMonthRent);
        LogUtil.e(TAG, "主产品批发价: " + productTradePrice);
        LogUtil.e(TAG, "主产品主图url地址: " + productPic1);
        LogUtil.e(TAG, "主产品模拟图url地址: " + productPic2);
        LogUtil.e(TAG, "主产品配图url地址: " + productPic3);
        LogUtil.e(TAG, "主产品的高度: " + productHeight);

        LogUtil.e(TAG, "附加产品的款名Id: " + augmentedProductSkuId);
        LogUtil.e(TAG, "附加产品名称: " + augmentedProductName);
        LogUtil.e(TAG, "附加产品价格: " + augmentedProductPrice);
        LogUtil.e(TAG, "附加产品月租: " + augmentedProductMonthRent);
        LogUtil.e(TAG, "附加产品批发价: " + augmentedProductTradePrice);
        LogUtil.e(TAG, "附加产品主图url地址: " + augmentedProductPic1);
        LogUtil.e(TAG, "附加产品模拟图url地址: " + augmentedProductPic2);
        LogUtil.e(TAG, "附加产品配图url地址: " + augmentedProductPic3);
        LogUtil.e(TAG, "附加产品的高度: " + augmentedProductHeight);

        LogUtil.e(TAG, "主产品的款名Id+附加产品的款名Id的组合: " + finallySkuId);
        LogUtil.e(TAG, "偏移量(如果该产品是花盆): " + augmentedProductOffsetRatio);
        LogUtil.e(TAG, "保存Bitmap为图片到本地的路径: " + picturePath);

        try {
            DbManager dbManager = YiBaiApplication.getDbManager();
            // 模拟(在这里可以添加finallySkuId相同的盆栽)
            SimulationData simulationData = new SimulationData();
            simulationData.setUid(uid);

            // 查找当前正在编辑的这一个场景
            List<SceneInfo> defaultSceneInfoList = dbManager.selector(SceneInfo.class)
                    .where("uid", "=", uid)
                    .and("editScene", "=", true)
                    .findAll();
            if (null != defaultSceneInfoList && defaultSceneInfoList.size() > 0) {
                long sceneId = defaultSceneInfoList.get(0).getSceneId();
                simulationData.setSceneId(sceneId);
            }

            simulationData.setFinallySkuId(finallySkuId);
            simulationData.setProductSkuId(productSkuId);
            if (!TextUtils.isEmpty(productName)) {
                simulationData.setProductName(productName);
            }
            simulationData.setProductPrice(productPrice);
            simulationData.setProductMonthRent(productMonthRent);
            simulationData.setProductTradePrice(productTradePrice);
            if (!TextUtils.isEmpty(productPic1)) {
                simulationData.setProductPic1(productPic1);
            }
            if (!TextUtils.isEmpty(productPic2)) {
                simulationData.setProductPic2(productPic2);
            }
            if (!TextUtils.isEmpty(productPic3)) {
                simulationData.setProductPic3(productPic3);
            }
            if (0 != productHeight) {
                simulationData.setProductHeight(productHeight);
            }
            /*----------*/
            simulationData.setAugmentedProductSkuId(augmentedProductSkuId);
            if (!TextUtils.isEmpty(augmentedProductName)) {
                simulationData.setAugmentedProductName(augmentedProductName);
            }
            simulationData.setAugmentedProductPrice(augmentedProductPrice);
            simulationData.setAugmentedProductMonthRent(augmentedProductMonthRent);
            simulationData.setAugmentedProductTradePrice(augmentedProductTradePrice);
            if (!TextUtils.isEmpty(augmentedProductPic1)) {
                simulationData.setAugmentedProductPic1(augmentedProductPic1);
            }
            if (!TextUtils.isEmpty(augmentedProductPic2)) {
                simulationData.setAugmentedProductPic2(augmentedProductPic2);
            }
            if (!TextUtils.isEmpty(augmentedProductPic3)) {
                simulationData.setAugmentedProductPic3(augmentedProductPic3);
            }
            if (0 != augmentedProductHeight) {
                simulationData.setAugmentedProductHeight(augmentedProductHeight);
            }
            simulationData.setAugmentedProductOffsetRatio(augmentedProductOffsetRatio);
            /*----------*/
            simulationData.setTimeStamp(TimeUtil.getNanoTime());
            if (judeFileExists(picturePath)) {
                simulationData.setPicturePath(picturePath);
            }
            if (0 < width && 0 < height) {
                simulationData.setWidth(width);
                simulationData.setHeight(height);
            }
            // 默认为1
            simulationData.setxScale(1);
            simulationData.setyScale(1);
            /*----------*/
            dbManager.save(simulationData);
            // 提示用户添加成功
            callBack.onSaveSimulationDataResult(true);
        } catch (DbException e) {
            e.printStackTrace();
            callBack.onSaveSimulationDataResult(false);
        }
    }
}
