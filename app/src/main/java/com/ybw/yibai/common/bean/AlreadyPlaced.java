package com.ybw.yibai.common.bean;

/**
 * 某一个场景内摆放的"产品"信息
 * 用于在"将场景中产品一键创建并导入"到服务器端时提交到服务器中使用
 *
 * @author sjl
 */
public class AlreadyPlaced {

    /**
     * 主产品skuid
     */
    private int first_sku_id;

    /**
     * 附加产品skuid
     */
    private int second_sku_id;

    /**
     * 数量
     */
    private int num;

    /**
     * 是否有组合图
     */
    private int havpic;

    public AlreadyPlaced() {
    }

    public AlreadyPlaced(int first_sku_id, int second_sku_id, int num, int havpic) {
        this.first_sku_id = first_sku_id;
        this.second_sku_id = second_sku_id;
        this.num = num;
        this.havpic = havpic;
    }

    public int getFirst_sku_id() {
        return first_sku_id;
    }

    public void setFirst_sku_id(int first_sku_id) {
        this.first_sku_id = first_sku_id;
    }

    public int getSecond_sku_id() {
        return second_sku_id;
    }

    public void setSecond_sku_id(int second_sku_id) {
        this.second_sku_id = second_sku_id;
    }

    public int getNum() {
        return num;
    }

    public void setNum(int num) {
        this.num = num;
    }

    public int getHavpic() {
        return havpic;
    }

    public void setHavpic(int havpic) {
        this.havpic = havpic;
    }
}
