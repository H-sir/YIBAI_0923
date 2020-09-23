package com.ybw.yibai.module.nickname;

import com.ybw.yibai.base.BaseCallBack;
import com.ybw.yibai.base.BasePresenter;
import com.ybw.yibai.base.BaseView;
import com.ybw.yibai.common.bean.EditUserInfo;

/**
 * 合约模式,将三个Model,View,Presenter接口以及CallBack接口放在一个Contract接口里面方便统一管理
 *
 * @author sjl
 */
public interface NicknameContract {

    interface NicknameView extends BaseView {

        /**
         * 修改用户昵称成功时回调
         *
         * @param editUserInfo 修改用户基础信息时服务器端返回的数据
         */
        void onEditUserInfoSuccess(EditUserInfo editUserInfo);
    }

    interface NicknamePresenter extends BasePresenter<NicknameView> {

        /**
         * 修改用户昵称
         *
         * @param nickName 昵称
         */
        void editUserInfo(String nickName);
    }

    interface NicknameModel {

        /**
         * 修改用户昵称
         *
         * @param nickName 昵称
         * @param callBack 回调方法
         */
        void editUserInfo(String nickName, CallBack callBack);
    }

    interface CallBack extends BaseCallBack {

        /**
         * 修改用户昵称成功时回调
         *
         * @param editUserInfo 修改用户基础信息时服务器端返回的数据
         */
        void onEditUserInfoSuccess(EditUserInfo editUserInfo);
    }
}
