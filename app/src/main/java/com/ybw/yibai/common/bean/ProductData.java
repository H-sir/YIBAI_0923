package com.ybw.yibai.common.bean;

/**
 * 产品数据
 *
 * @author sjl
 */
public class ProductData {

    /**
     * 主产品的款名Id
     */
    private int productSkuId;

    /**
     * 主产品名称
     */
    private String productName;

    /**
     * 主产品价格
     */
    private double productPrice;

    /**
     * 主产品月租
     */
    private double productMonthRent;

    /**
     * 主产品批发价
     */
    private double productTradePrice;

    /**
     * 主产品主图url地址
     */
    private String productPic1;

    /**
     * 主产品模拟图url地址
     */
    private String productPic2;

    /**
     * 主产品配图url地址
     */
    private String productPic3;

    /**
     * 偏移量(如果该产品是植物)
     */
    private double productOffsetRatio;

    /**
     * 主产品的规格
     * 例如: 160cm 小型
     */
    private String productSpecText;

    /**
     * 主产品的高度
     */
    private double productHeight;

    /**
     * 主产品组合模式: 1组合2单产品
     */
    private int productCombinationType;

    /**
     * 主产品售价代码
     */
    private String productPriceCode;

    /**
     * 主产品批发价代码
     */
    private String productTradePriceCode;

    /*----------*/

    /**
     * 附加产品的款名Id
     */
    private int augmentedProductSkuId;

    /**
     * 附加产品名称
     */
    private String augmentedProductName;

    /**
     * 附加产品价格
     */
    private double augmentedProductPrice;

    /**
     * 附加产品月租
     */
    private double augmentedProductMonthRent;

    /**
     * 附加产品批发价
     */
    private double augmentedProductTradePrice;

    /**
     * 附加产品主图url地址
     */
    private String augmentedProductPic1;

    /**
     * 附加产品模拟图url地址
     */
    private String augmentedProductPic2;

    /**
     * 附加产品配图url地址
     */
    private String augmentedProductPic3;

    /**
     * 附加产品的规格
     * 例如: 160cm 小型
     */
    private String augmentedProductSpecText;

    /**
     * 附加产品的高度
     */
    private double augmentedProductHeight;

    /**
     * 偏移量(如果该产品是花盆)
     */
    private double augmentedProductOffsetRatio;

    /**
     * 附加产品组合模式: 1组合2单产品
     */
    private int augmentedCombinationType;

    /**
     * 附加产品售价代码
     */
    private String augmentedPriceCode;

    /**
     * 附加产品批发价代码
     */
    private String augmentedTradePriceCode;

    public ProductData(int productSkuId, String productName, double productPrice,
                       double productMonthRent, double productTradePrice, String productPic1,
                       String productPic2, String productPic3, String productSpecText,
                       double productHeight, int productCombinationType,
                       String productPriceCode, String productTradePriceCode) {

        this.productSkuId = productSkuId;
        this.productName = productName;
        this.productPrice = productPrice;
        this.productMonthRent = productMonthRent;
        this.productTradePrice = productTradePrice;
        this.productPic1 = productPic1;
        this.productPic2 = productPic2;
        this.productPic3 = productPic3;
        this.productSpecText = productSpecText;
        this.productHeight = productHeight;
        this.productCombinationType = productCombinationType;
        this.productPriceCode = productPriceCode;
        this.productTradePriceCode = productTradePriceCode;
    }

    public ProductData(int productSkuId, String productName,
                       double productPrice, double productMonthRent, double productTradePrice,
                       String productPic1, String productPic2, String productPic3,
                       double productHeight, double productOffsetRatio,int productCombinationType,
                       String productPriceCode, String productTradePriceCode,

                       int augmentedProductSkuId, String augmentedProductName,
                       double augmentedProductPrice, double augmentedProductMonthRent, double augmentedProductTradePrice,
                       String augmentedProductPic1, String augmentedProductPic2, String augmentedProductPic3,
                       double augmentedProductHeight, double augmentedProductOffsetRatio, int augmentedCombinationType,
                       String augmentedPriceCode, String augmentedTradePriceCode) {

        this.productSkuId = productSkuId;
        this.productName = productName;
        this.productPrice = productPrice;
        this.productMonthRent = productMonthRent;
        this.productTradePrice = productTradePrice;
        this.productPic1 = productPic1;
        this.productPic2 = productPic2;
        this.productPic3 = productPic3;
        this.productHeight = productHeight;
        this.productOffsetRatio = productOffsetRatio;
        this.productCombinationType = productCombinationType;
        this.productPriceCode = productPriceCode;
        this.productTradePriceCode = productTradePriceCode;

        this.augmentedProductSkuId = augmentedProductSkuId;
        this.augmentedProductName = augmentedProductName;
        this.augmentedProductPrice = augmentedProductPrice;
        this.augmentedProductMonthRent = augmentedProductMonthRent;
        this.augmentedProductTradePrice = augmentedProductTradePrice;
        this.augmentedProductPic1 = augmentedProductPic1;
        this.augmentedProductPic2 = augmentedProductPic2;
        this.augmentedProductPic3 = augmentedProductPic3;
        this.augmentedProductHeight = augmentedProductHeight;
        this.augmentedProductOffsetRatio = augmentedProductOffsetRatio;
        this.augmentedCombinationType = augmentedCombinationType;
        this.augmentedPriceCode = augmentedPriceCode;
        this.augmentedTradePriceCode = augmentedTradePriceCode;
    }

    public double getProductOffsetRatio() {
        return productOffsetRatio;
    }

    public void setProductOffsetRatio(double productOffsetRatio) {
        this.productOffsetRatio = productOffsetRatio;
    }

    public int getProductSkuId() {
        return productSkuId;
    }

    public void setProductSkuId(int productSkuId) {
        this.productSkuId = productSkuId;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public double getProductPrice() {
        return productPrice;
    }

    public void setProductPrice(double productPrice) {
        this.productPrice = productPrice;
    }

    public double getProductMonthRent() {
        return productMonthRent;
    }

    public void setProductMonthRent(double productMonthRent) {
        this.productMonthRent = productMonthRent;
    }

    public double getProductTradePrice() {
        return productTradePrice;
    }

    public void setProductTradePrice(double productTradePrice) {
        this.productTradePrice = productTradePrice;
    }

    public String getProductPic1() {
        return productPic1;
    }

    public void setProductPic1(String productPic1) {
        this.productPic1 = productPic1;
    }

    public String getProductPic2() {
        return productPic2;
    }

    public void setProductPic2(String productPic2) {
        this.productPic2 = productPic2;
    }

    public String getProductPic3() {
        return productPic3;
    }

    public void setProductPic3(String productPic3) {
        this.productPic3 = productPic3;
    }

    public String getProductSpecText() {
        return productSpecText;
    }

    public void setProductSpecText(String productSpecText) {
        this.productSpecText = productSpecText;
    }

    public double getProductHeight() {
        return productHeight;
    }

    public void setProductHeight(double productHeight) {
        this.productHeight = productHeight;
    }

    public int getProductCombinationType() {
        return productCombinationType;
    }

    public void setProductCombinationType(int productCombinationType) {
        this.productCombinationType = productCombinationType;
    }

    public String getProductPriceCode() {
        return productPriceCode;
    }

    public void setProductPriceCode(String productPriceCode) {
        this.productPriceCode = productPriceCode;
    }

    public String getProductTradePriceCode() {
        return productTradePriceCode;
    }

    public void setProductTradePriceCode(String productTradePriceCode) {
        this.productTradePriceCode = productTradePriceCode;
    }

    public int getAugmentedProductSkuId() {
        return augmentedProductSkuId;
    }

    public void setAugmentedProductSkuId(int augmentedProductSkuId) {
        this.augmentedProductSkuId = augmentedProductSkuId;
    }

    public String getAugmentedProductName() {
        return augmentedProductName;
    }

    public void setAugmentedProductName(String augmentedProductName) {
        this.augmentedProductName = augmentedProductName;
    }

    public double getAugmentedProductPrice() {
        return augmentedProductPrice;
    }

    public void setAugmentedProductPrice(double augmentedProductPrice) {
        this.augmentedProductPrice = augmentedProductPrice;
    }

    public double getAugmentedProductMonthRent() {
        return augmentedProductMonthRent;
    }

    public void setAugmentedProductMonthRent(double augmentedProductMonthRent) {
        this.augmentedProductMonthRent = augmentedProductMonthRent;
    }

    public double getAugmentedProductTradePrice() {
        return augmentedProductTradePrice;
    }

    public void setAugmentedProductTradePrice(double augmentedProductTradePrice) {
        this.augmentedProductTradePrice = augmentedProductTradePrice;
    }

    public String getAugmentedProductPic1() {
        return augmentedProductPic1;
    }

    public void setAugmentedProductPic1(String augmentedProductPic1) {
        this.augmentedProductPic1 = augmentedProductPic1;
    }

    public String getAugmentedProductPic2() {
        return augmentedProductPic2;
    }

    public void setAugmentedProductPic2(String augmentedProductPic2) {
        this.augmentedProductPic2 = augmentedProductPic2;
    }

    public String getAugmentedProductPic3() {
        return augmentedProductPic3;
    }

    public void setAugmentedProductPic3(String augmentedProductPic3) {
        this.augmentedProductPic3 = augmentedProductPic3;
    }

    public String getAugmentedProductSpecText() {
        return augmentedProductSpecText;
    }

    public void setAugmentedProductSpecText(String augmentedProductSpecText) {
        this.augmentedProductSpecText = augmentedProductSpecText;
    }

    public double getAugmentedProductHeight() {
        return augmentedProductHeight;
    }

    public void setAugmentedProductHeight(double augmentedProductHeight) {
        this.augmentedProductHeight = augmentedProductHeight;
    }

    public double getAugmentedProductOffsetRatio() {
        return augmentedProductOffsetRatio;
    }

    public void setAugmentedProductOffsetRatio(double augmentedProductOffsetRatio) {
        this.augmentedProductOffsetRatio = augmentedProductOffsetRatio;
    }

    public int getAugmentedCombinationType() {
        return augmentedCombinationType;
    }

    public void setAugmentedCombinationType(int augmentedCombinationType) {
        this.augmentedCombinationType = augmentedCombinationType;
    }

    public String getAugmentedPriceCode() {
        return augmentedPriceCode;
    }

    public void setAugmentedPriceCode(String augmentedPriceCode) {
        this.augmentedPriceCode = augmentedPriceCode;
    }

    public String getAugmentedTradePriceCode() {
        return augmentedTradePriceCode;
    }

    public void setAugmentedTradePriceCode(String augmentedTradePriceCode) {
        this.augmentedTradePriceCode = augmentedTradePriceCode;
    }
}
