package com.ybw.yibai.common.bean;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * <pre>
 *     author : HKR
 *     time   : 2020/10/15
 *     desc   :
 * </pre>
 */
public class PurCartBean {

    /**
     * code : 200
     * msg : 获取成功
     * data : {"comlist":[{"num":1,"pic":"http://images.100ybw.com/public/noimg.jpg","cart_id":2,"price":272,"checked":1,"first":{"sku_id":7969,"gate_product_id":2,"price":"188.00","name":"绿宝80","gate_id":4,"gate_name":"Uhh"},"second":{"sku_id":7836,"gate_product_id":6,"fprice":"84.00","name":"3560#","gate_id":5,"gate_name":"悠然梦花屋"}}],"itemlist":[{"num":2,"pic":"http://images.100ybw.com/product/2020/09/12/20200912025624395f5c71188391a.png","cart_id":1,"price":0,"checked":1,"first":{"sku_id":7913,"gate_product_id":1,"price":"0.00","name":"大叶伞","gate_id":0,"gate_name":"平台直供"}}]}
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
        @SerializedName("comlist") private List<ComlistBean> comlist;
        @SerializedName("itemlist") private List<ItemlistBean> itemlist;

        public List<ComlistBean> getComlist() {
            return comlist;
        }

        public void setComlist(List<ComlistBean> comlist) {
            this.comlist = comlist;
        }

        public List<ItemlistBean> getItemlist() {
            return itemlist;
        }

        public void setItemlist(List<ItemlistBean> itemlist) {
            this.itemlist = itemlist;
        }

        public static class ComlistBean {
            /**
             * num : 1
             * pic : http://images.100ybw.com/public/noimg.jpg
             * cart_id : 2
             * price : 272
             * checked : 1
             * first : {"sku_id":7969,"gate_product_id":2,"price":"188.00","name":"绿宝80","gate_id":4,"gate_name":"Uhh"}
             * second : {"sku_id":7836,"gate_product_id":6,"fprice":"84.00","name":"3560#","gate_id":5,"gate_name":"悠然梦花屋"}
             */

            @SerializedName("num") private int num;
            @SerializedName("pic") private String pic;
            @SerializedName("cart_id") private int cartId;
            @SerializedName("price") private float price;
            @SerializedName("checked") private int checked;
            @SerializedName("first") private FirstBean first;
            @SerializedName("second") private SecondBean second;

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

            public FirstBean getFirst() {
                return first;
            }

            public void setFirst(FirstBean first) {
                this.first = first;
            }

            public SecondBean getSecond() {
                return second;
            }

            public void setSecond(SecondBean second) {
                this.second = second;
            }

            public static class FirstBean {
                /**
                 * sku_id : 7969
                 * gate_product_id : 2
                 * price : 188.00
                 * name : 绿宝80
                 * gate_id : 4
                 * gate_name : Uhh
                 */

                @SerializedName("sku_id") private int skuId;
                @SerializedName("gate_product_id") private int gateProductId;
                @SerializedName("price") private String price;
                @SerializedName("name") private String name;
                @SerializedName("gate_id") private int gateId;
                @SerializedName("gate_name") private String gateName;

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

            public static class SecondBean {
                /**
                 * sku_id : 7836
                 * gate_product_id : 6
                 * fprice : 84.00
                 * name : 3560#
                 * gate_id : 5
                 * gate_name : 悠然梦花屋
                 */

                @SerializedName("sku_id") private int skuId;
                @SerializedName("gate_product_id") private int gateProductId;
                @SerializedName("price") private String fprice;
                @SerializedName("name") private String name;
                @SerializedName("gate_id") private int gateId;
                @SerializedName("gate_name") private String gateName;

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

                public String getFprice() {
                    return fprice;
                }

                public void setFprice(String fprice) {
                    this.fprice = fprice;
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

        public static class ItemlistBean {
            /**
             * num : 2
             * pic : http://images.100ybw.com/product/2020/09/12/20200912025624395f5c71188391a.png
             * cart_id : 1
             * price : 0
             * checked : 1
             * first : {"sku_id":7913,"gate_product_id":1,"price":"0.00","name":"大叶伞","gate_id":0,"gate_name":"平台直供"}
             */

            @SerializedName("num") private int num;
            @SerializedName("pic") private String pic;
            @SerializedName("cart_id") private int cartId;
            @SerializedName("price") private float price;
            @SerializedName("checked") private int checked;
            @SerializedName("first") private FirstBeanX first;

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

            public FirstBeanX getFirst() {
                return first;
            }

            public void setFirst(FirstBeanX first) {
                this.first = first;
            }

            public static class FirstBeanX {
                /**
                 * sku_id : 7913
                 * gate_product_id : 1
                 * price : 0.00
                 * name : 大叶伞
                 * gate_id : 0
                 * gate_name : 平台直供
                 */

                @SerializedName("sku_id") private int skuId;
                @SerializedName("gate_product_id") private int gateProductId;
                @SerializedName("price") private String price;
                @SerializedName("name") private String name;
                @SerializedName("gate_id") private int gateId;
                @SerializedName("gate_name") private String gateName;

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
    }
}
