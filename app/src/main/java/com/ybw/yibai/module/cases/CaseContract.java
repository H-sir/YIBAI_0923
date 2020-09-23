package com.ybw.yibai.module.cases;

import com.ybw.yibai.base.BaseCallBack;
import com.ybw.yibai.base.BasePresenter;
import com.ybw.yibai.base.BaseView;
import com.ybw.yibai.common.bean.Case;

/**
 * 合约模式,将三个Model,View,Presenter接口以及CallBack接口放在一个Contract接口里面方便统一管理
 *
 * @author sjl
 * @date 2019/5/16
 */
public interface CaseContract {

    interface CaseView extends BaseView {

        /**
         * 在获取公司案例成功时回调
         *
         * @param case_ 公司案例
         */
        void onGetCaseSuccess(Case case_);
    }

    interface CasePresenter extends BasePresenter<CaseView> {

        /**
         * 获取公司案例
         *
         * @param projectClassifyId 案例分类Id
         */
        void getCase(int projectClassifyId);
    }

    interface CaseModel {

        /**
         * 获取公司案例
         *
         * @param projectClassifyId 案例分类Id
         * @param callBack          回调方法
         */
        void getCase(int projectClassifyId, CallBack callBack);
    }

    interface CallBack extends BaseCallBack {

        /**
         * 在获取公司案例成功时回调
         *
         * @param case_ 公司案例
         */
        void onGetCaseSuccess(Case case_);
    }
}
