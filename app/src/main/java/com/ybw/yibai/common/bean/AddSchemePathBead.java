package com.ybw.yibai.common.bean;

/**
 * <pre>
 *     author : HKR
 *     time   : 2020/10/10
 *     desc   :
 * </pre>
 */
public class AddSchemePathBead {

   private String pathName;
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
