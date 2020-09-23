package com.ybw.yibai.module.filter;

import android.view.View;

import com.ybw.yibai.base.BasePresenter;
import com.ybw.yibai.base.BaseView;

import java.util.Map;

/**
 * 合约模式,将三个Model,View,Presenter接口以及CallBack接口放在一个Contract接口里面方便统一管理
 *
 * @author sjl
 * @date 2019/9/12
 */
public interface FilterContract {

    interface FilterView extends BaseView {

        /**
         * 返回用户选中的产品筛选参数
         *
         * @param selectProductParamMap 用户选中的产品筛选参数
         */
        void returnSelectProductParam(Map<String, String> selectProductParamMap);
    }

    interface FilterPresenter extends BasePresenter<FilterView> {

        /**
         * 在单击View时调用
         *
         * @param v 被单击的View
         */
        void onClick(View v);
    }
}
