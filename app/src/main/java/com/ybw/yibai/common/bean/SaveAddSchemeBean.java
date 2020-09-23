package com.ybw.yibai.common.bean;

public class SaveAddSchemeBean {
    private String  pathName;
    private int productSkuId;
    private int augmentedProductSkuId;

    public String getPathName() {
        return pathName;
    }

    public void setPathName(String pathName) {
        this.pathName = pathName;
    }

    public int getProductSkuId() {
        return productSkuId;
    }

    public void setProductSkuId(int productSkuId) {
        this.productSkuId = productSkuId;
    }

    public int getAugmentedProductSkuId() {
        return augmentedProductSkuId;
    }

    public void setAugmentedProductSkuId(int augmentedProductSkuId) {
        this.augmentedProductSkuId = augmentedProductSkuId;
    }
}
