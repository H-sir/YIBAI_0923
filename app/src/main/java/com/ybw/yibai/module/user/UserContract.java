package com.ybw.yibai.module.user;

import com.ybw.yibai.base.BaseCallBack;
import com.ybw.yibai.base.BasePresenter;
import com.ybw.yibai.base.BaseView;
import com.ybw.yibai.common.bean.SceneInfo;

import java.util.List;

/**
 * 合约模式,将三个Model,View,Presenter接口以及CallBack接口放在一个Contract接口里面方便统一管理
 */
public interface UserContract {

    interface UserView extends BaseView {

        /**
         * 在查找用户的场景信息成功时回调
         *
         * @param sceneInfoList 用户场景信息列表
         */
        void onFindUserSceneInfoSuccess(List<SceneInfo> sceneInfoList);

        /**
         * 更新用户的场景信息成功时回调
         */
        void onUpdateUserSceneListSuccess();

        /**
         * 更新用户单个的场景信息启动编辑成功时回调
         */
        void onUpdateUserSceneSuccess();
    }

    interface UserPresenter extends BasePresenter<UserView> {

        /**
         * 查找用户的场景信息
         */
        void findUserSceneListInfo();

        /**
         * 更新用户的场景信息
         */
        void updateUserSceneList(List<SceneInfo> sceneInfoList);

        /**
         * 更新用户单个的场景信息启动编辑
         */
        void updateUserScene(SceneInfo sceneInfo);
    }

    interface UserModel {

        /**
         * 查找用户的场景信息
         *
         * @param callBack 回调方法
         */
        void findUserSceneListInfo(CallBack callBack);

        /**
         * 更新用户的场景信息
         */
        void updateUserSceneList(List<SceneInfo> sceneInfoList, CallBack callBack);

        /**
         * 更新用户单个的场景信息启动编辑
         */
        void updateUserScene(SceneInfo sceneInfo, CallBack callBack);
    }

    interface CallBack extends BaseCallBack {

        /**
         * 在查找用户的场景信息成功时回调
         *
         * @param sceneInfoList 用户场景信息列表
         */
        void onFindUserUserInfoSuccess(List<SceneInfo> sceneInfoList);

        /**
         * 更新用户的场景信息成功时回调
         */
        void onUpdateUserSceneListSuccess();

        /**
         * 更新用户单个的场景信息启动编辑成功时回调
         */
        void onUpdateUserSceneSuccess();
    }
}
