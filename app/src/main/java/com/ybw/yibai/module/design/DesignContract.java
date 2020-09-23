package com.ybw.yibai.module.design;

import com.ybw.yibai.base.BaseCallBack;
import com.ybw.yibai.base.BasePresenter;
import com.ybw.yibai.base.BaseView;
import com.ybw.yibai.common.bean.BaseBean;
import com.ybw.yibai.common.bean.DesignList;
import com.ybw.yibai.common.bean.SceneInfo;

import java.util.List;

/**
 * <pre>
 *     author : HKR
 *     time   : 2020/09/02
 *     desc   :
 * </pre>
 */
public interface DesignContract {
    interface DesignView extends BaseView {
        /**
         * 获取设计列表成功的回调
         */
        void onGetDesignListSuccess(DesignList designList);

        /**
         * 获取当前正在编辑的场景成功回调
         */
        void onFindEditSceneInfo(List<SceneInfo> defaultSceneInfoList);

        /**
         * 删除设计成功回调
         */
        void onDeleteDesignSuccess(BaseBean baseBean);
        void onUpdateSceneInfo();
    }

    interface DesignPresenter extends BasePresenter<DesignView> {
        /**
         * 获取设计列表
         */
        void getDesignList();

        /**
         * 获取当前正在编辑的场景
         */
        void getEditSceneInfo();

        /**
         * 删除设计
         */
        void deleteDesign(DesignList.DataBean.ListBean listBean);

        /**
         * 更新场景信息
         */
        void updateSceneInfo(DesignList.DataBean.ListBean.SchemelistBean schemelistBean);
    }

    interface DesignModel {
        /**
         * 获取设计列表
         */
        void getDesignList(CallBack callBack);

        /**
         * 获取当前正在编辑的场景
         */
        void getEditSceneInfo(CallBack callBack);

        /**
         * 删除设计
         */
        void deleteDesign(DesignList.DataBean.ListBean listBean, CallBack callBack);

        void updateSceneInfo(DesignList.DataBean.ListBean.SchemelistBean schemelistBean, CallBack callBack);
    }

    interface CallBack extends BaseCallBack {
        /**
         * 获取设计列表成功的回调
         */
        void onGetDesignListSuccess(DesignList designList);

        /**
         * 获取当前正在编辑的场景成功回调
         */
        void onFindEditSceneInfo(List<SceneInfo> defaultSceneInfoList);

        /**
         * 删除设计成功回调
         */
        void onDeleteDesignSuccess(BaseBean baseBean);

        void onUpdateSceneInfo();
    }
}
