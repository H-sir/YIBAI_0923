package com.ybw.yibai.module.product;

import com.ybw.yibai.common.interfaces.ApiService;
import com.ybw.yibai.common.utils.RetrofitManagerUtil;
import com.ybw.yibai.module.product.ProductContract.ProductModel;

/**
 * 产品Model实现类
 *
 * @author sjl
 * @date 2019/9/5
 */
public class ProductModelImpl implements ProductModel {

    private final String TAG = "ProductTypeModelImpl";

    private ApiService mApiService;

    public ProductModelImpl() {
        RetrofitManagerUtil instance = RetrofitManagerUtil.getInstance();
        mApiService = instance.getApiService();
    }
}