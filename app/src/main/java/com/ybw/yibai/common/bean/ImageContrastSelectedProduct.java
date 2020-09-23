package com.ybw.yibai.common.bean;

/**
 * 多张搭配图片模拟效果对比界面用户点击RecyclerView Item选中的盆栽信息
 *
 * @author sjl
 */
public class ImageContrastSelectedProduct {

    /**
     * 主产品的款名Id
     */
    private int productSkuId;

    /**
     * 附加产品的款名Id
     */
    private int augmentedProductSkuId;

    public ImageContrastSelectedProduct(int productSkuId, int augmentedProductSkuId) {
        this.productSkuId = productSkuId;
        this.augmentedProductSkuId = augmentedProductSkuId;
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
