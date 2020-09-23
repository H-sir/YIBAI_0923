package com.ybw.yibai.common.bean;

/**
 * 创建报价单时需要提交给服务器的产品信息
 *
 * @author sjl
 */
public class ProductInfo {

    /**
     * 主要产品skuId
     */
    private int goods1;

    /**
     * 附加产品skuId
     */
    private int goods2;

    /**
     * 价格
     */
    private double price;

    /**
     * 是否有图1/0(有组合图时,传1,否则传0或不传,会根据状态按顺序查找上传的图片)
     */
    private int pic;

    /**
     * 数量(默认为1)
     */
    private int num;

    public ProductInfo(int goods1, int goods2, double price, int pic, int num) {
        this.goods1 = goods1;
        this.goods2 = goods2;
        this.price = price;
        this.pic = pic;
        this.num = num;
    }

    public int getGoods1() {
        return goods1;
    }

    public void setGoods1(int goods1) {
        this.goods1 = goods1;
    }

    public int getGoods2() {
        return goods2;
    }

    public void setGoods2(int goods2) {
        this.goods2 = goods2;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public int getPic() {
        return pic;
    }

    public void setPic(int pic) {
        this.pic = pic;
    }

    public int getNum() {
        return num;
    }

    public void setNum(int num) {
        this.num = num;
    }
}
