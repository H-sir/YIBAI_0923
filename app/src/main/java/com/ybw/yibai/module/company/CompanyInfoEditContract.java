package com.ybw.yibai.module.company;

import com.ybw.yibai.base.BaseCallBack;
import com.ybw.yibai.base.BasePresenter;
import com.ybw.yibai.base.BaseView;
import com.ybw.yibai.common.bean.UpdateCompany;

import java.util.Map;

import okhttp3.RequestBody;

/**
 * 合约模式,将三个Model,View,Presenter接口以及CallBack接口放在一个Contract接口里面方便统一管理
 *
 * @author sjl
 */
public interface CompanyInfoEditContract {

    interface CompanyInfoEditView extends BaseView {

        /**
         * 在修改用户公司信息成功时回调
         *
         * @param updateCompany 修改用户公司信息时服务器端返回的数据
         */
        void onUpdateCompanyInfoSuccess(UpdateCompany updateCompany);
    }

    interface CompanyInfoEditPresenter extends BasePresenter<CompanyInfoEditView> {

        /**
         * 修改用户公司信息
         *
         * @param name           公司名称
         * @param logoPicPath       公司logo图片路径
         * @param detailsPicPath 介绍图图片路径
         * @param details        描述
         */
        void updateCompanyInfo(String name, String logoPicPath, String detailsPicPath, String details);
    }

    interface CompanyInfoEditModel {

        /**
         * 修改用户公司信息
         *
         * @param name             公司名称
         * @param logoParams       公司logo
         * @param detailsPicParams 介绍图
         * @param details          描述
         * @param callBack         回调方法
         */
        void updateCompanyInfo(String name, Map<String, RequestBody> logoParams, Map<String, RequestBody> detailsPicParams, String details, CallBack callBack);
    }

    interface CallBack extends BaseCallBack {

        /**
         * 在修改用户公司信息成功时回调
         *
         * @param updateCompany 修改用户公司信息时服务器端返回的数据
         */
        void onUpdateCompanyInfoSuccess(UpdateCompany updateCompany);
    }
}