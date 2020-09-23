package com.ybw.yibai.common.bean;

/**
 * 创建采购订单时提交到服务器端的数据
 *
 * @author sjl
 */
public class PurchaseOrderData {

    private int goods;

    private int num;

    public PurchaseOrderData(int goods, int num) {
        this.goods = goods;
        this.num = num;
    }

    public int getGoods() {
        return goods;
    }

    public void setGoods(int goods) {
        this.goods = goods;
    }

    public int getNum() {
        return num;
    }

    public void setNum(int num) {
        this.num = num;
    }
}
