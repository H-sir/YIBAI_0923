package com.ybw.yibai.module.imagecontrast;

import android.view.View;
import android.widget.RadioGroup;

import com.ybw.yibai.base.BaseCallBack;
import com.ybw.yibai.base.BasePresenter;
import com.ybw.yibai.base.BaseView;
import com.ybw.yibai.common.bean.ListBean;
import com.ybw.yibai.common.bean.Recommend;
import com.ybw.yibai.common.bean.SimulationData;

/**
 * 合约模式,将三个Model,View,Presenter接口以及CallBack接口放在一个Contract接口里面方便统一管理
 *
 * @author sjl
 * @date 2019/11/8
 */
public interface ImageContrastContract {

    interface ImageContrastView extends BaseView {

        /**
         * 换搭配获取植物花盆列表成功时回调
         *
         * @param recommend 植物花盆列表
         */
        void onGetRecommendSuccess(Recommend recommend);

        /**
         * 在PopupWindow里面的RadioButton被选中时回调
         *
         * @param group     被点击的RadioGroup
         * @param checkedId 被选中的RadioButton ID
         */
        void onCheckedChanged(RadioGroup group, int checkedId);

        /**
         * 添加到模拟列表操作的结果
         *
         * @param result true操作成功,false操作失败
         */
        void onAddSimulationDataResult(boolean result);
    }

    interface ImageContrastPresenter extends BasePresenter<ImageContrastView> {

        /**
         * 换搭配获取植物花盆列表
         *
         * @param cateCode   类别:plant,获取植物 pot获取盆
         * @param plantSkuId 植物SkuId
         * @param potSkuId   盆SkuId
         * @param specType   大中小id(此参数仅在获取花盆时有效)
         * @param pCid       分类id(多个用逗号拼接)
         * @param specId     规格id(同类别规格用逗号分隔,不同类别用|分隔) 例:specId=11,12|15,16
         * @param attrId     属性id,(格式同规格)
         * @param height     搭配高度范围,用|分隔 例:height=5|20
         * @param diameter   搭配口径范围,格式同上
         */
        void getRecommend(String cateCode, Integer plantSkuId, Integer potSkuId, Integer specType, Integer pCid,
                          String specId, String attrId, String height, String diameter);

        /**
         * 显示盆器对比/植物对比切换的PopupWindow
         *
         * @param view 要显示的位置
         */
        void displayComparisonPopupWindow(View view);

        /**
         * 添加到模拟列表
         *
         * @param simulationData 贴纸数据
         * @param productInfo    推荐产品
         * @param picturePath    组合图片在本地保存的路径
         */
        void addSimulationData(SimulationData simulationData, ListBean productInfo, String picturePath);
    }

    interface ImageContrastModel {

        /**
         * 换搭配获取植物花盆列表
         *
         * @param cateCode   类别:plant,获取植物 pot获取盆
         * @param plantSkuId 植物SkuId
         * @param potSkuId   盆SkuId
         * @param specType   大中小id(此参数仅在获取花盆时有效)
         * @param pCid       分类id(多个用逗号拼接)
         * @param specId     规格id(同类别规格用逗号分隔,不同类别用|分隔) 例:specId=11,12|15,16
         * @param attrId     属性id,(格式同规格)
         * @param height     搭配高度范围,用|分隔 例:height=5|20
         * @param diameter   搭配口径范围,格式同上
         * @param callBack   回调方法
         */
        void getRecommend(String cateCode, Integer plantSkuId, Integer potSkuId, Integer specType, Integer pCid,
                          String specId, String attrId, String height, String diameter, CallBack callBack);

        /**
         * 添加到模拟列表
         *
         * @param simulationData 贴纸数据
         * @param productInfo    推荐产品
         * @param picturePath    组合图片在本地保存的路径
         * @param callBack       回调方法
         */
        void addSimulationData(SimulationData simulationData, ListBean productInfo, String picturePath, CallBack callBack);
    }

    interface CallBack extends BaseCallBack {

        /**
         * 换搭配获取植物花盆列表成功时回调
         *
         * @param recommend 植物花盆列表
         */
        void onGetRecommendSuccess(Recommend recommend);

        /**
         * 添加到模拟列表操作的结果
         *
         * @param result true操作成功,false操作失败
         */
        void onAddSimulationDataResult(boolean result);
    }
}
