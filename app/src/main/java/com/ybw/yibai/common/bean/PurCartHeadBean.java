package com.ybw.yibai.common.bean;

import com.google.gson.annotations.SerializedName;

/**
 * <pre>
 *     author : HKR
 *     time   : 2020/10/16
 *     desc   :
 * </pre>
 */
public class PurCartHeadBean {

    private int num;
    private String pic;
    private int cartId;
    private float price;
    private int checked;
    private DataBean first;
    private DataBean second;
    private int type;

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getNum() {
        return num;
    }

    public void setNum(int num) {
        this.num = num;
    }

    public String getPic() {
        return pic;
    }

    public void setPic(String pic) {
        this.pic = pic;
    }

    public int getCartId() {
        return cartId;
    }

    public void setCartId(int cartId) {
        this.cartId = cartId;
    }

    public float getPrice() {
        return price;
    }

    public void setPrice(float price) {
        this.price = price;
    }

    public int getChecked() {
        return checked;
    }

    public void setChecked(int checked) {
        this.checked = checked;
    }

    public DataBean getFirst() {
        return first;
    }

    public void setFirst(DataBean first) {
        this.first = first;
    }

    public DataBean getSecond() {
        return second;
    }

    public void setSecond(DataBean second) {
        this.second = second;
    }

    public static class DataBean {

        private int skuId;
        private int gateProductId;
        private String price;
        private String name;
        private int gateId;
        private String gateName;

        public int getSkuId() {
            return skuId;
        }

        public void setSkuId(int skuId) {
            this.skuId = skuId;
        }

        public int getGateProductId() {
            return gateProductId;
        }

        public void setGateProductId(int gateProductId) {
            this.gateProductId = gateProductId;
        }

        public String getPrice() {
            return price;
        }

        public void setPrice(String price) {
            this.price = price;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public int getGateId() {
            return gateId;
        }

        public void setGateId(int gateId) {
            this.gateId = gateId;
        }

        public String getGateName() {
            return gateName;
        }

        public void setGateName(String gateName) {
            this.gateName = gateName;
        }
    }
}
