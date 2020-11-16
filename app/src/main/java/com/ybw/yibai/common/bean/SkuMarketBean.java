package com.ybw.yibai.common.bean;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class SkuMarketBean {


    /**
     * code : 200
     * msg : ok
     * data : {"product_name":"富贵竹笼","self_info":{"price":"","month_rent":""},"gate_info":[{"gate_id":0,"gate_name":"平台直供","gate_add":"官方优选货源","gate_sku":[{"gate_product_id":0,"subtitle":"","pic_arr":["http://images.100ybw.com/product/2020/09/04/20200904041636835f51f7e42a895.png"],"price":129,"uptime":"10/09 14:51:03","stock":0,"delivery":"广州发货至广州市:预计1天后到货"}]}]}
     */

    @SerializedName("code") private int code;
    @SerializedName("msg") private String msg;
    @SerializedName("data") private DataBean data;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
        this.data = data;
    }

    public static class DataBean {
        /**
         * product_name : 富贵竹笼
         * self_info : {"price":"","month_rent":""}
         * gate_info : [{"gate_id":0,"gate_name":"平台直供","gate_add":"官方优选货源","gate_sku":[{"gate_product_id":0,"subtitle":"","pic_arr":["http://images.100ybw.com/product/2020/09/04/20200904041636835f51f7e42a895.png"],"price":129,"uptime":"10/09 14:51:03","stock":0,"delivery":"广州发货至广州市:预计1天后到货"}]}]
         */

        @SerializedName("product_name") private String productName;
        @SerializedName("self_info") private SelfInfoBean selfInfo;
        @SerializedName("gate_info") private List<GateInfoBean> gateInfo;

        public String getProductName() {
            return productName;
        }

        public void setProductName(String productName) {
            this.productName = productName;
        }

        public SelfInfoBean getSelfInfo() {
            return selfInfo;
        }

        public void setSelfInfo(SelfInfoBean selfInfo) {
            this.selfInfo = selfInfo;
        }

        public List<GateInfoBean> getGateInfo() {
            return gateInfo;
        }

        public void setGateInfo(List<GateInfoBean> gateInfo) {
            this.gateInfo = gateInfo;
        }

        public static class SelfInfoBean {
            /**
             * price :
             * month_rent :
             */

            @SerializedName("price") private String price;
            @SerializedName("month_rent") private String monthRent;

            public String getPrice() {
                return price;
            }

            public void setPrice(String price) {
                this.price = price;
            }

            public String getMonthRent() {
                return monthRent;
            }

            public void setMonthRent(String monthRent) {
                this.monthRent = monthRent;
            }
        }

        public static class GateInfoBean {
            /**
             * gate_id : 0
             * gate_name : 平台直供
             * gate_add : 官方优选货源
             * gate_sku : [{"gate_product_id":0,"subtitle":"","pic_arr":["http://images.100ybw.com/product/2020/09/04/20200904041636835f51f7e42a895.png"],"price":129,"uptime":"10/09 14:51:03","stock":0,"delivery":"广州发货至广州市:预计1天后到货"}]
             */

            @SerializedName("gate_id") private int gateId;
            @SerializedName("gate_name") private String gateName;
            @SerializedName("gate_add") private String gateAdd;
            @SerializedName("gate_logo") private String gateLogo;
            @SerializedName("gate_info_url") private String gateInfoUrl;
            @SerializedName("gate_sku") private List<GateSkuBean> gateSku;


            public String getGateLogo() {
                return gateLogo;
            }

            public void setGateLogo(String gateLogo) {
                this.gateLogo = gateLogo;
            }

            public String getGateInfoUrl() {
                return gateInfoUrl;
            }

            public void setGateInfoUrl(String gateInfoUrl) {
                this.gateInfoUrl = gateInfoUrl;
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

            public String getGateAdd() {
                return gateAdd;
            }

            public void setGateAdd(String gateAdd) {
                this.gateAdd = gateAdd;
            }

            public List<GateSkuBean> getGateSku() {
                return gateSku;
            }

            public void setGateSku(List<GateSkuBean> gateSku) {
                this.gateSku = gateSku;
            }

            public static class GateSkuBean {
                /**
                 * gate_product_id : 0
                 * subtitle :
                 * pic_arr : ["http://images.100ybw.com/product/2020/09/04/20200904041636835f51f7e42a895.png"]
                 * price : 129
                 * uptime : 10/09 14:51:03
                 * stock : 0
                 * delivery : 广州发货至广州市:预计1天后到货
                 */

                @SerializedName("gate_product_id") private int gateProductId;
                @SerializedName("subtitle") private String subtitle;
                @SerializedName("price") private float price;
                @SerializedName("uptime") private String uptime;
                @SerializedName("stock") private int stock;
                @SerializedName("delivery") private String delivery;
                @SerializedName("pic_arr") private List<String> picArr;
                private boolean select;

                public boolean isSelect() {
                    return select;
                }

                public void setSelect(boolean select) {
                    this.select = select;
                }

                public int getGateProductId() {
                    return gateProductId;
                }

                public void setGateProductId(int gateProductId) {
                    this.gateProductId = gateProductId;
                }

                public String getSubtitle() {
                    return subtitle;
                }

                public void setSubtitle(String subtitle) {
                    this.subtitle = subtitle;
                }

                public float getPrice() {
                    return price;
                }

                public void setPrice(float price) {
                    this.price = price;
                }

                public String getUptime() {
                    return uptime;
                }

                public void setUptime(String uptime) {
                    this.uptime = uptime;
                }

                public int getStock() {
                    return stock;
                }

                public void setStock(int stock) {
                    this.stock = stock;
                }

                public String getDelivery() {
                    return delivery;
                }

                public void setDelivery(String delivery) {
                    this.delivery = delivery;
                }

                public List<String> getPicArr() {
                    return picArr;
                }

                public void setPicArr(List<String> picArr) {
                    this.picArr = picArr;
                }
            }
        }
    }
}
