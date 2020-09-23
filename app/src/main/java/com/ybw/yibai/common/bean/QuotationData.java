package com.ybw.yibai.common.bean;

import android.support.annotation.NonNull;

import org.xutils.db.annotation.Column;
import org.xutils.db.annotation.Table;

/**
 * 保存用户的"报价"数据
 *
 * @author sjl
 */
@Table(name = "QuotationData")
public class QuotationData implements Comparable<QuotationData> {

    /**
     * 用户登录成功时获得的Uid
     */
    @Column(name = "uid")
    private int uid;

    /**
     * 主产品的款名Id+附加产品的款名Id的组合+用户的uid
     */
    @Column(name = "finallySkuId", isId = true, autoGen = false)
    private String finallySkuId;

    /*----------*/

    /**
     * 用户当前选择的"某种规格的产品"的款名Id
     */
    @Column(name = "productSkuId")
    private int productSkuId;

    /**
     * 主产品名称
     */
    @Column(name = "productName")
    private String productName;

    /**
     * 主产品价格
     */
    @Column(name = "productPrice")
    private double productPrice;

    /**
     * 主产品月租
     */
    @Column(name = "productMonthRent")
    private double productMonthRent;

    /**
     * 主产品批发价
     */
    @Column(name = "productTradePrice")
    private double productTradePrice;

    /**
     * 主产品主图url地址
     */
    @Column(name = "productPic1")
    private String productPic1;

    /**
     * 主产品模拟图url地址
     */
    @Column(name = "productPic2")
    private String productPic2;

    /**
     * 主产品配图url地址
     */
    @Column(name = "productPic3")
    private String productPic3;

    /**
     * 组合好的用户选择的"某种产品的规格"
     * 例如: 160cm 小型
     */
    @Column(name = "productSpecText")
    private String productSpecText;

    /**
     * 用户当前选择的"某种规格的产品"的高度
     */
    @Column(name = "productHeight")
    private double productHeight;

    /**
     * 主产品组合模式: 1组合2单产品
     */
    @Column(name = "productCombinationType")
    private int productCombinationType;

    /**
     * 主产品售价代码
     */
    @Column(name = "productPriceCode")
    private String productPriceCode;

    /**
     * 主产品批发价代码
     */
    @Column(name = "productTradePriceCode")
    private String productTradePriceCode;

    /*----------*/

    /**
     * 用户当前选择的"附加产品"的款名Id
     */
    @Column(name = "augmentedProductSkuId")
    private int augmentedProductSkuId;

    /**
     * 附加产品名称
     */
    @Column(name = "augmentedProductName")
    private String augmentedProductName;

    /**
     * 附加产品价格
     */
    @Column(name = "augmentedProductPrice")
    private double augmentedProductPrice;

    /**
     * 附加产品月租
     */
    @Column(name = "augmentedProductMonthRent")
    private double augmentedProductMonthRent;

    /**
     * 附加产品批发价
     */
    @Column(name = "augmentedProductTradePrice")
    private double augmentedProductTradePrice;

    /**
     * 附加产品主图url地址
     */
    @Column(name = "augmentedProductPic1")
    private String augmentedProductPic1;

    /**
     * 附加产品模拟图url地址
     */
    @Column(name = "augmentedProductPic2")
    private String augmentedProductPic2;

    /**
     * 附加产品配图url地址
     */
    @Column(name = "augmentedProductPic3")
    private String augmentedProductPic3;

    /**
     * 组合好的用户选择的"某种附加产品的规格"
     * 例如: 160cm 小型
     */
    @Column(name = "augmentedProductSpecText")
    private String augmentedProductSpecText;

    /**
     * 用户当前选择的"某种规格的附加产品"的高度
     */
    @Column(name = "augmentedProductHeight")
    private double augmentedProductHeight;

    /**
     * 附加产品组合模式: 1组合2单产品
     */
    @Column(name = "augmentedCombinationType")
    private int augmentedCombinationType;

    /**
     * 附加产品售价代码
     */
    @Column(name = "augmentedPriceCode")
    private String augmentedPriceCode;

    /**
     * 附加产品批发价代码
     */
    @Column(name = "augmentedTradePriceCode")
    private String augmentedTradePriceCode;

    /*----------*/

    /**
     * 数量
     */
    @Column(name = "number")
    private int number;

    /**
     * 添加or更新的数据的时间搓
     */
    @Column(name = "timeStamp")
    private Long timeStamp;

    /**
     * 组合图片在本地保存的路径
     */
    @Column(name = "picturePath")
    private String picturePath;

    /*----------*/

    /**
     * 报价方式
     * 0:购买
     * 1:月租
     * 2:年租
     */
    private int mode;

    /*----------*/

    /**
     * 是否显示CheckBox
     */
    private boolean showCheckBox;

    /**
     * 是否选中CheckBox
     */
    private boolean selectCheckBox;

    public int getUid() {
        return uid;
    }

    public void setUid(int uid) {
        this.uid = uid;
    }

    public String getFinallySkuId() {
        return finallySkuId;
    }

    public void setFinallySkuId(String finallySkuId) {
        this.finallySkuId = finallySkuId;
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

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public Long getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(Long timeStamp) {
        this.timeStamp = timeStamp;
    }

    public String getPicturePath() {
        return picturePath;
    }

    public void setPicturePath(String picturePath) {
        this.picturePath = picturePath;
    }

    public int getMode() {
        return mode;
    }

    public void setMode(int mode) {
        this.mode = mode;
    }

    public boolean isShowCheckBox() {
        return showCheckBox;
    }

    public void setShowCheckBox(boolean showCheckBox) {
        this.showCheckBox = showCheckBox;
    }

    public boolean isSelectCheckBox() {
        return selectCheckBox;
    }

    public void setSelectCheckBox(boolean selectCheckBox) {
        this.selectCheckBox = selectCheckBox;
    }

    @Override
    public int compareTo(@NonNull QuotationData o) {
        return o.getTimeStamp().compareTo(this.getTimeStamp());
    }
}
