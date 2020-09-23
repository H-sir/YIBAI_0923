package com.ybw.yibai.module.product;

import com.ybw.yibai.base.BasePresenterImpl;
import com.ybw.yibai.module.product.ProductContract.CallBack;
import com.ybw.yibai.module.product.ProductContract.ProductModel;
import com.ybw.yibai.module.product.ProductContract.ProductPresenter;
import com.ybw.yibai.module.product.ProductContract.ProductView;

/**
 * 产品Presenter实现类
 *
 * @author sjl
 * @date 2019/9/5
 */
public class ProductPresenterImpl extends BasePresenterImpl<ProductView> implements ProductPresenter, CallBack {

    /**
     * PresenterImpl 持有 View的接口引用
     */
    private ProductView mProductView;

    /**
     * PresenterImpl 持有 Model的接口引用
     */
    private ProductModel mProductModel;

    /**
     * 构造方法
     *
     * @param view View的对象
     */
    public ProductPresenterImpl(ProductView view) {
        super(view);

        // 调用父类的方法获取View的对象
        this.mProductView = getView();
        mProductModel = new ProductModelImpl();
    }
}
