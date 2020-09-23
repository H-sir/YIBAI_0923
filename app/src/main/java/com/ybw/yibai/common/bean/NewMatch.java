package com.ybw.yibai.common.bean;

/**
 * 用户在"更换搭配"界面选择的产品其数据
 *
 * @author sjl
 */
public class NewMatch {

    /**
     * SKU ID
     */
    private int sku_id;

    /**
     * 产品id
     */
    private int product_id;

    /**
     * 产品名称
     */
    private String name;

    /**
     * sku主图(确定sku后用于替换显示产品图片)
     */
    private String pic1;

    /**
     * sku模拟搭配图(没有此图,此sku将不能模拟)
     */
    private String pic2;

    /**
     * sku自由搭配图(没有此图,此sku将不能搭配)
     */
    private String pic3;

    /**
     *
     */
    private String score;

    /**
     * 零售价
     */
    private double price;

    /**
     * 月租
     */
    private double month_rent;

    /**
     * 批发价
     */
    private double trade_price;

    /**
     * 搭配高度
     */
    private double height;

    /**
     * 搭配口径
     */
    private double diameter;

    /**
     * 偏移量(仅花盆类别的才会返回)
     */
    private double offset_ratio;

    /**
     * 当前数据在推荐植物/盆器列表中的位置
     */
    private int position;

    /**
     * 产品code plant植物/pot盆器
     * (非后台给的数据,android 开发人员自定义的字段)
     */
    private String categoryCode;

    public int getSku_id() {
        return sku_id;
    }

    public void setSku_id(int sku_id) {
        this.sku_id = sku_id;
    }

    public int getProduct_id() {
        return product_id;
    }

    public void setProduct_id(int product_id) {
        this.product_id = product_id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPic1() {
        return pic1;
    }

    public void setPic1(String pic1) {
        this.pic1 = pic1;
    }

    public String getPic2() {
        return pic2;
    }

    public void setPic2(String pic2) {
        this.pic2 = pic2;
    }

    public String getPic3() {
        return pic3;
    }

    public void setPic3(String pic3) {
        this.pic3 = pic3;
    }

    public String getScore() {
        return score;
    }

    public void setScore(String score) {
        this.score = score;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public double getMonth_rent() {
        return month_rent;
    }

    public void setMonth_rent(double month_rent) {
        this.month_rent = month_rent;
    }

    public double getTrade_price() {
        return trade_price;
    }

    public void setTrade_price(double trade_price) {
        this.trade_price = trade_price;
    }

    public double getHeight() {
        return height;
    }

    public void setHeight(double height) {
        this.height = height;
    }

    public double getDiameter() {
        return diameter;
    }

    public void setDiameter(double diameter) {
        this.diameter = diameter;
    }

    public double getOffset_ratio() {
        return offset_ratio;
    }

    public void setOffset_ratio(double offset_ratio) {
        this.offset_ratio = offset_ratio;
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public String getCategoryCode() {
        return categoryCode;
    }

    public void setCategoryCode(String categoryCode) {
        this.categoryCode = categoryCode;
    }
}
