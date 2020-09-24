package com.ybw.yibai.module.designdetails;

import com.ybw.yibai.base.BaseCallBack;
import com.ybw.yibai.base.BasePresenter;
import com.ybw.yibai.base.BaseView;
import com.ybw.yibai.common.bean.BaseBean;
import com.ybw.yibai.common.bean.DesignDetails;

/**
 * <pre>
 *     author : HKR
 *     time   : 2020/09/02
 *     desc   :
 * </pre>
 */
public interface DesignDetailsContract {
    interface DesignDetailsView extends BaseView {
        /**
         * 获取设计详情成功回调
         */
        void onGetDesignDetailsSuccess(DesignDetails designDetails);

        /**
         * 删除设计图片成功回调
         *
         * @param deleteBase
         */
        void onDeleteSchemePicSuccess(String deleteBase);

        /**
         * 删除场景成功回调
         *
         * @param schemelistBean 删除的场景
         */
        void onDeleteSchemeSuccess(DesignDetails.DataBean.SchemelistBean schemelistBean);

        /**
         * 删除设计成功回调
         */
        void onDeleteDesignSuccess(BaseBean baseBean);

        void editSceneNameSuccess();

    }

    interface DesignDetailsPresenter extends BasePresenter<DesignDetailsView> {

        /**
         * 获取设计详情
         */
        void getDesignDetails(String designNumber);

        /**
         * 删除设计图片
         */
        void deleteSchemePic(String picId);

        /**
         * 删除场景
         */
        void deleteScheme(DesignDetails.DataBean.SchemelistBean schemelistBean);

        /**
         * 删除设计
         */
        void deleteDesign(DesignDetails designDetails);

        /**
         * 修改场景名称
         * */
        void editSceneName(String scnenName, DesignDetails.DataBean.SchemelistBean schemelistBean);
    }

    interface DesignDetailsModel {

        /**
         * 获取设计详情
         */
        void getDesignDetails(String designNumber, CallBack callBack);

        /**
         * 删除设计图片
         */
        void deleteSchemePic(String picId, CallBack callBack);

        /**
         * 删除场景
         */
        void deleteScheme(DesignDetails.DataBean.SchemelistBean schemelistBean, CallBack callBack);

        /**
         * 删除设计
         */
        void deleteDesign(DesignDetails designDetails, CallBack callBack);

        void editSceneName(String scnenName, DesignDetails.DataBean.SchemelistBean schemelistBean, CallBack callBack);
    }

    interface CallBack extends BaseCallBack {

        /**
         * 获取设计详情成功回调
         */
        void onGetDesignDetailsSuccess(DesignDetails designDetails);

        /**
         * 删除设计图片成功回调
         *
         * @param pic
         */
        void onDeleteSchemePicSuccess(String pic);

        /**
         * 删除场景成功回调
         *
         * @param schemelistBean 删除的场景
         */
        void onDeleteSchemeSuccess(DesignDetails.DataBean.SchemelistBean schemelistBean);

        /**
         * 删除设计成功回调
         */
        void onDeleteDesignSuccess(BaseBean baseBean);

        void editSceneNameSuccess();
    }
}
