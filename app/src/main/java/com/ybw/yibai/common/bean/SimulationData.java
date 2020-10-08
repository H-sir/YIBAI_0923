package com.ybw.yibai.common.bean;

import org.xutils.db.annotation.Column;
import org.xutils.db.annotation.Table;

import java.io.Serializable;

/**
 * 保存用户的"模拟搭配产品"数据
 *
 * @author sjl
 */
@Table(name = "SimulationData")
public class SimulationData implements Serializable {

    /**
     * 用户登录成功时获得的Uid
     */
    @Column(name = "uid")
    private int uid;

    /**
     * 模拟数据所属的场景id(一般为创建该场景的时间戳)
     */
    @Column(name = "sceneId")
    private long sceneId;

    /**
     * 主产品的款名Id+附加产品的款名Id的组合+用户的uid
     */
    @Column(name = "finallySkuId")
    private String finallySkuId;

    /*----------*/

    /**
     * 主产品的款名Id
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
     * 偏移量(如果该产品是植物)
     */
    @Column(name = "productOffsetRatio")
    private double productOffsetRatio;

    /**
     * 主产品搭配图url地址
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
     * 主产品组合模式: 1单图模式,2搭配上部,3搭配下部
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
     * 附加产品的款名Id
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
     * 附加产品搭配图url地址
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
     * 偏移量(如果该产品是花盆)
     */
    @Column(name = "augmentedProductOffsetRatio")
    private double augmentedProductOffsetRatio;

    /**
     * 附加产品组合模式: 1单图模式,2搭配上部,3搭配下部
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
     * 添加or更新的数据的时间搓
     */
    @Column(name = "timeStamp", isId = true, autoGen = false)
    private Long timeStamp;

    /**
     * 组合图片在本地保存的路径
     */
    @Column(name = "picturePath")
    private String picturePath;

    /**
     * 贴纸左上角距离屏幕左上角X轴的距离
     */
    @Column(name = "x")
    private float x;

    /**
     * 贴纸左上角距离屏幕左上角Y轴的距离
     */
    @Column(name = "y")
    private float y;

    /**
     * 组合图片的宽度
     */
    @Column(name = "width")
    private double width;

    /**
     * 组合图片的高度
     */
    @Column(name = "height")
    private double height;

    /**
     * 组合图片X轴的缩放比例
     * (在"场景"中用户可能会对图片进行放大或者缩小,所以这个值默认为1)
     */
    @Column(name = "xScale")
    private double xScale;

    /**
     * 组合图片Y轴的缩放比例
     * (在"场景"中用户可能会对图片进行放大或者缩小,所以这个值默认为1)
     */
    @Column(name = "yScale")
    private double yScale;

//    @Column(name = "scaleFactorWidth")
//    private double scaleFactorWidth = width;
//
//    @Column(name = "scaleFactorHeight")
//    private double scaleFactorHeight = height;
    /**
     * 某一种"模拟搭配产品"的数量
     * (在"SceneEditFragment场景编辑界面"统计"模拟搭配产品"的数量时用到
     * 如果"finallySkuId"相同,就可以认定为同一个产品）
     */
    private int number;

    /*----------*/


//    public double getScaleFactorWidth() {
//        return scaleFactorWidth;
//    }
//
//    public void setScaleFactorWidth(double scaleFactorWidth) {
//        this.scaleFactorWidth = scaleFactorWidth;
//    }
//
//    public double getScaleFactorHeight() {
//        return scaleFactorHeight;
//    }
//
//    public void setScaleFactorHeight(double scaleFactorHeight) {
//        this.scaleFactorHeight = scaleFactorHeight;
//    }


    /*----------*/


    public double getProductOffsetRatio() {
        return productOffsetRatio;
    }

    public void setProductOffsetRatio(double productOffsetRatio) {
        this.productOffsetRatio = productOffsetRatio;
    }

    public int getUid() {
        return uid;
    }

    public void setUid(int uid) {
        this.uid = uid;
    }

    public long getSceneId() {
        return sceneId;
    }

    public void setSceneId(long sceneId) {
        this.sceneId = sceneId;
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

    public float getX() {
        return x;
    }

    public void setX(float x) {
        this.x = x;
    }

    public float getY() {
        return y;
    }

    public void setY(float y) {
        this.y = y;
    }

    public double getWidth() {
        return width;
    }

    public void setWidth(double width) {
        this.width = width;
    }

    public double getHeight() {
        return height;
    }

    public void setHeight(double height) {
        this.height = height;
    }

    public double getxScale() {
        return xScale;
    }

    public void setxScale(double xScale) {
        this.xScale = xScale;
    }

    public double getyScale() {
        return yScale;
    }

    public void setyScale(double yScale) {
        this.yScale = yScale;
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }
}
