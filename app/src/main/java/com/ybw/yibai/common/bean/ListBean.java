package com.ybw.yibai.common.bean;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;

/**
 * 产品信息
 *
 * @author sjl
 */
public class ListBean implements Serializable, Parcelable {

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
     *
     */
    private String thumb_pic1;

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

    private SourceBean source;

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
     * 组合模式 1单图模式,2搭配上部,3搭配下部
     */
    private int comtype;

    /**
     * 售价代码
     */
    private String price_code;

    /**
     * 批发价代码
     */
    private String trade_price_code;

    /**
     * 产品code plant植物/pot盆器
     * (非后台给的数据,android 开发人员自定义的字段)
     */
    private String categoryCode;

    /**
     * 是否选中该数据
     * (非后台给的数据,android 开发人员自定义的字段)
     */
    private boolean select;


    public SourceBean getSource() {
        return source;
    }

    public void setSource(SourceBean source) {
        this.source = source;
    }

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

    public String getThumb_pic1() {
        return thumb_pic1;
    }

    public void setThumb_pic1(String thumb_pic1) {
        this.thumb_pic1 = thumb_pic1;
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

    public int getComtype() {
        return comtype;
    }

    public void setComtype(int comtype) {
        this.comtype = comtype;
    }

    public String getPrice_code() {
        return price_code;
    }

    public void setPrice_code(String price_code) {
        this.price_code = price_code;
    }

    public String getTrade_price_code() {
        return trade_price_code;
    }

    public void setTrade_price_code(String trade_price_code) {
        this.trade_price_code = trade_price_code;
    }

    public String getCategoryCode() {
        return categoryCode;
    }

    public void setCategoryCode(String categoryCode) {
        this.categoryCode = categoryCode;
    }

    public boolean isSelect() {
        return select;
    }

    public void setSelect(boolean select) {
        this.select = select;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.sku_id);
        dest.writeInt(this.product_id);
        dest.writeString(this.name);
        dest.writeString(this.pic1);
        dest.writeString(this.pic2);
        dest.writeString(this.pic3);
        dest.writeString(this.score);
        dest.writeDouble(this.price);
        dest.writeDouble(this.month_rent);
        dest.writeDouble(this.trade_price);
        dest.writeDouble(this.height);
        dest.writeDouble(this.diameter);
        dest.writeDouble(this.offset_ratio);
        dest.writeInt(this.comtype);
        dest.writeString(this.price_code);
        dest.writeString(this.trade_price_code);
        dest.writeString(this.categoryCode);
        dest.writeByte(this.select ? (byte) 1 : (byte) 0);
    }

    public ListBean() {
    }

    protected ListBean(Parcel in) {
        this.sku_id = in.readInt();
        this.product_id = in.readInt();
        this.name = in.readString();
        this.pic1 = in.readString();
        this.pic2 = in.readString();
        this.pic3 = in.readString();
        this.score = in.readString();
        this.price = in.readDouble();
        this.month_rent = in.readDouble();
        this.trade_price = in.readDouble();
        this.height = in.readDouble();
        this.diameter = in.readDouble();
        this.offset_ratio = in.readDouble();
        this.comtype = in.readInt();
        this.price_code = in.readString();
        this.trade_price_code = in.readString();
        this.categoryCode = in.readString();
        this.select = in.readByte() != 0;
    }

    public static final Creator<ListBean> CREATOR = new Creator<ListBean>() {
        @Override
        public ListBean createFromParcel(Parcel source) {
            return new ListBean(source);
        }

        @Override
        public ListBean[] newArray(int size) {
            return new ListBean[size];
        }
    };

    public static class SourceBean {
        private String delivery_day;
        private String delivery;

        public String getDelivery_day() {
            return delivery_day;
        }

        public void setDelivery_day(String delivery_day) {
            this.delivery_day = delivery_day;
        }

        public String getDelivery() {
            return delivery;
        }

        public void setDelivery(String delivery) {
            this.delivery = delivery;
        }
    }
}
