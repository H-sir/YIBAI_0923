package com.ybw.yibai.module.productusestate;

import com.ybw.yibai.base.BasePresenterImpl;
import com.ybw.yibai.common.bean.ProductScreeningParam;
import com.ybw.yibai.module.productusestate.ProductUseStateContract.*;

/**
 * 产品使用状态Presenter实现类
 *
 * @author sjl
 * @date 2019/9/5
 */
public class ProductUseStatePresenterImpl extends BasePresenterImpl<ProductUseStateView>
        implements ProductUseStatePresenter, CallBack {

    /**
     * PresenterImpl 持有 View的接口引用
     */
    private ProductUseStateView mProductUseStateView;

    /**
     * PresenterImpl 持有 Model的接口引用
     */
    private ProductUseStateModel mProductUseStateModel;

    /**
     * 构造方法
     *
     * @param view View的对象
     */
    public ProductUseStatePresenterImpl(ProductUseStateView view) {
        super(view);

        // 调用父类的方法获取View的对象
        this.mProductUseStateView = getView();
        mProductUseStateModel = new ProductUseStateModelImpl();
    }

    /**
     * 获取产品筛选参数
     */
    @Override
    public void getProductScreeningParam() {
        mProductUseStateModel.getProductScreeningParam(this);
    }

    /**
     * 在获取产品筛选参数成功时回调
     *
     * @param productScreeningParam 获取产品筛选参数时服务器端返回的数据
     */
    @Override
    public void onGetProductScreeningParamSuccess(ProductScreeningParam productScreeningParam) {
        mProductUseStateView.onGetProductScreeningParamSuccess(productScreeningParam);
    }
}
