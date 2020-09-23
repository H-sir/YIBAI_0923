package com.ybw.yibai.module.product;

import com.ybw.yibai.base.BaseCallBack;
import com.ybw.yibai.base.BasePresenter;
import com.ybw.yibai.base.BaseView;

/**
 * 合约模式,将三个Model,View,Presenter接口以及CallBack接口放在一个Contract接口里面方便统一管理
 *
 * @author sjl
 * @date 2019/5/16
 */
public interface ProductContract {

    interface ProductView extends BaseView {

    }

    interface ProductPresenter extends BasePresenter<ProductView> {

    }

    interface ProductModel {

    }

    interface CallBack extends BaseCallBack {

    }
}
