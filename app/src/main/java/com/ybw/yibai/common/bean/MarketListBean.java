package com.ybw.yibai.common.bean;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * <pre>
 *     author : HKR
 *     time   : 2020/10/14
 *     desc   :
 * </pre>
 */
public class MarketListBean {

    /**
     * code : 200
     * msg : ok
     * data : {"address":"陕西省西安市新城区西五路56号","list":[{"market_id":8,"name":"含元花卉市场","distance":"约11.2kg","address":"陕西省西安市新城区含元路12号","logo":"https://images.100ybw.com/supplier/2020/11/09/20201109034608445fa8f3c07c137_L.jpg"},{"market_id":9,"name":"西仓花卉市场","distance":"约30.1kg","address":"陕西省西安市莲湖区西仓北巷5号","logo":"https://images.100ybw.com/supplier/2020/11/09/20201109035234575fa8f542c3972_L.jpg"}]}
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
         * address : 陕西省西安市新城区西五路56号
         * list : [{"market_id":8,"name":"含元花卉市场","distance":"约11.2kg","address":"陕西省西安市新城区含元路12号","logo":"https://images.100ybw.com/supplier/2020/11/09/20201109034608445fa8f3c07c137_L.jpg"},{"market_id":9,"name":"西仓花卉市场","distance":"约30.1kg","address":"陕西省西安市莲湖区西仓北巷5号","logo":"https://images.100ybw.com/supplier/2020/11/09/20201109035234575fa8f542c3972_L.jpg"}]
         */

        @SerializedName("address") private String address;
        @SerializedName("list") private List<ListBean> list;

        public String getAddress() {
            return address;
        }

        public void setAddress(String address) {
            this.address = address;
        }

        public List<ListBean> getList() {
            return list;
        }

        public void setList(List<ListBean> list) {
            this.list = list;
        }

        public static class ListBean {
            /**
             * market_id : 8
             * name : 含元花卉市场
             * distance : 约11.2kg
             * address : 陕西省西安市新城区含元路12号
             * logo : https://images.100ybw.com/supplier/2020/11/09/20201109034608445fa8f3c07c137_L.jpg
             */

            @SerializedName("market_id") private int marketId;
            @SerializedName("name") private String name;
            @SerializedName("distance") private String distance;
            @SerializedName("address") private String address;
            @SerializedName("logo") private String logo;
            private boolean isCheck = false;

            public boolean isCheck() {
                return isCheck;
            }

            public void setCheck(boolean check) {
                isCheck = check;
            }

            public int getMarketId() {
                return marketId;
            }

            public void setMarketId(int marketId) {
                this.marketId = marketId;
            }

            public String getName() {
                return name;
            }

            public void setName(String name) {
                this.name = name;
            }

            public String getDistance() {
                return distance;
            }

            public void setDistance(String distance) {
                this.distance = distance;
            }

            public String getAddress() {
                return address;
            }

            public void setAddress(String address) {
                this.address = address;
            }

            public String getLogo() {
                return logo;
            }

            public void setLogo(String logo) {
                this.logo = logo;
            }
        }
    }
}
