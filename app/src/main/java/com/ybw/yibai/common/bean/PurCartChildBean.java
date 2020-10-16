package com.ybw.yibai.common.bean;

/**
 * <pre>
 *     author : HKR
 *     time   : 2020/10/15
 *     desc   :
 * </pre>
 */
public class PurCartChildBean {
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
