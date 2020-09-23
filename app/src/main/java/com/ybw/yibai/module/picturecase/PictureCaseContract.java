package com.ybw.yibai.module.picturecase;

import com.ybw.yibai.base.BaseCallBack;
import com.ybw.yibai.base.BasePresenter;
import com.ybw.yibai.base.BaseView;
import com.ybw.yibai.common.bean.CaseClassify;

/**
 * 合约模式,将三个Model,View,Presenter接口以及CallBack接口放在一个Contract接口里面方便统一管理
 *
 * @author sjl
 * @date 2019/11/09
 */
public interface PictureCaseContract {

    interface PictureCaseView extends BaseView {

        /**
         * 在获取案例分类成功时回调
         *
         * @param caseClassify 案例分类
         */
        void onGetCaseClassifySuccess(CaseClassify caseClassify);
    }

    interface PictureCasePresenter extends BasePresenter<PictureCaseView> {

        /**
         * 获取公司案例分类
         */
        void getCaseClassify();
    }

    interface PictureCaseModel {

        /**
         * 获取公司案例分类
         *
         * @param callBack 回调方法
         */
        void getCaseClassify(CallBack callBack);
    }

    interface CallBack extends BaseCallBack {

        /**
         * 在获取案例分类成功时回调
         *
         * @param caseClassify 案例分类
         */
        void onGetCaseClassifySuccess(CaseClassify caseClassify);
    }
}
