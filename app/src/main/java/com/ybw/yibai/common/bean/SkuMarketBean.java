package com.ybw.yibai.common.bean;

import java.util.List;

public class SkuMarketBean {

    /**
     * code : 200
     * msg : ok
     * data : {"product_name":"富贵竹笼","self_info":{"price":"","month_rent":""},"gate_info":[{"gate_id":0,"gate_name":"平台直供","gate_add":"官方优选货源","gate_sku":[{"gate_sku_id":0,"subtitle":"","pic_arr":["http://images.100ybw.com/product/2020/09/04/20200904041636835f51f7e42a895.png"],"trade_price":"129.00","uptime":"09/04 16:16:43","stock":0}]},{"gate_id":1,"gate_name":"爱花坊","gate_add":"广东省-岭南花卉市场","gate_sku":[{"gate_sku_id":"1","subtitle":"富贵竹笼 特级","pic_arr":[],"trade_price":"50.00","uptime":"08/20 00:02:00","stock":"10"},{"gate_sku_id":"3","subtitle":"富贵竹笼 A级","pic_arr":[],"trade_price":"88.00","uptime":"08/20 14:32:31","stock":"100"}]}]}
     */

    private int code;
    private String msg;
    private DataBean data;

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
         * gate_info : [{"gate_id":0,"gate_name":"平台直供","gate_add":"官方优选货源","gate_sku":[{"gate_sku_id":0,"subtitle":"","pic_arr":["http://images.100ybw.com/product/2020/09/04/20200904041636835f51f7e42a895.png"],"trade_price":"129.00","uptime":"09/04 16:16:43","stock":0}]},{"gate_id":1,"gate_name":"爱花坊","gate_add":"广东省-岭南花卉市场","gate_sku":[{"gate_sku_id":"1","subtitle":"富贵竹笼 特级","pic_arr":[],"trade_price":"50.00","uptime":"08/20 00:02:00","stock":"10"},{"gate_sku_id":"3","subtitle":"富贵竹笼 A级","pic_arr":[],"trade_price":"88.00","uptime":"08/20 14:32:31","stock":"100"}]}]
         */

        private String product_name;
        private SelfInfoBean self_info;
        private List<GateInfoBean> gate_info;

        public String getProduct_name() {
            return product_name;
        }

        public void setProduct_name(String product_name) {
            this.product_name = product_name;
        }

        public SelfInfoBean getSelf_info() {
            return self_info;
        }

        public void setSelf_info(SelfInfoBean self_info) {
            this.self_info = self_info;
        }

        public List<GateInfoBean> getGate_info() {
            return gate_info;
        }

        public void setGate_info(List<GateInfoBean> gate_info) {
            this.gate_info = gate_info;
        }

        public static class SelfInfoBean {
            /**
             * price :
             * month_rent :
             */

            private String price;
            private String month_rent;

            public String getPrice() {
                return price;
            }

            public void setPrice(String price) {
                this.price = price;
            }

            public String getMonth_rent() {
                return month_rent;
            }

            public void setMonth_rent(String month_rent) {
                this.month_rent = month_rent;
            }
        }

        public static class GateInfoBean {
            /**
             * gate_id : 0
             * gate_name : 平台直供
             * gate_add : 官方优选货源
             * gate_sku : [{"gate_sku_id":0,"subtitle":"","pic_arr":["http://images.100ybw.com/product/2020/09/04/20200904041636835f51f7e42a895.png"],"trade_price":"129.00","uptime":"09/04 16:16:43","stock":0}]
             */

            private int gate_id;
            private String gate_name;
            private String gate_add;
            private List<GateSkuBean> gate_sku;

            public int getGate_id() {
                return gate_id;
            }

            public void setGate_id(int gate_id) {
                this.gate_id = gate_id;
            }

            public String getGate_name() {
                return gate_name;
            }

            public void setGate_name(String gate_name) {
                this.gate_name = gate_name;
            }

            public String getGate_add() {
                return gate_add;
            }

            public void setGate_add(String gate_add) {
                this.gate_add = gate_add;
            }

            public List<GateSkuBean> getGate_sku() {
                return gate_sku;
            }

            public void setGate_sku(List<GateSkuBean> gate_sku) {
                this.gate_sku = gate_sku;
            }

            public static class GateSkuBean {
                /**
                 * gate_sku_id : 0
                 * subtitle :
                 * pic_arr : ["http://images.100ybw.com/product/2020/09/04/20200904041636835f51f7e42a895.png"]
                 * trade_price : 129.00
                 * uptime : 09/04 16:16:43
                 * stock : 0
                 */

                private int gate_sku_id;
                private String subtitle;
                private String trade_price;
                private String uptime;
                private int stock;
                private boolean isSelect;
                private List<String> pic_arr;


                public boolean isSelect() {
                    return isSelect;
                }

                public void setSelect(boolean select) {
                    isSelect = select;
                }

                public int getGate_sku_id() {
                    return gate_sku_id;
                }

                public void setGate_sku_id(int gate_sku_id) {
                    this.gate_sku_id = gate_sku_id;
                }

                public String getSubtitle() {
                    return subtitle;
                }

                public void setSubtitle(String subtitle) {
                    this.subtitle = subtitle;
                }

                public String getTrade_price() {
                    return trade_price;
                }

                public void setTrade_price(String trade_price) {
                    this.trade_price = trade_price;
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

                public List<String> getPic_arr() {
                    return pic_arr;
                }

                public void setPic_arr(List<String> pic_arr) {
                    this.pic_arr = pic_arr;
                }
            }
        }
    }
}
