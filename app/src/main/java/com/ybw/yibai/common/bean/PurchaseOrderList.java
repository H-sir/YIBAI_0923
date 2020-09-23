package com.ybw.yibai.common.bean;

import java.util.List;

/**
 * 采购单列表
 *
 * @author sjl
 */
public class PurchaseOrderList {

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

        private int count;
        private List<ListBean> list;

        public int getCount() {
            return count;
        }

        public void setCount(int count) {
            this.count = count;
        }

        public List<ListBean> getList() {
            return list;
        }

        public void setList(List<ListBean> list) {
            this.list = list;
        }

        public static class ListBean {

            /**
             * 状态类型
             */
            private int state;

            /**
             * 商品数
             */
            private int goods_num;

            /**
             * 金额
             */
            private double payable_amount;

            /**
             * 订单号
             */
            private String order_number;

            /**
             * 状态内容
             */
            private String state_msg;

            /**
             * 分享地址
             */
            private String share;

            /**
             *
             */
            private List<SkuListBean> sku_list;

            public int getState() {
                return state;
            }

            public void setState(int state) {
                this.state = state;
            }

            public int getGoods_num() {
                return goods_num;
            }

            public void setGoods_num(int goods_num) {
                this.goods_num = goods_num;
            }

            public double getPayable_amount() {
                return payable_amount;
            }

            public void setPayable_amount(double payable_amount) {
                this.payable_amount = payable_amount;
            }

            public String getOrder_number() {
                return order_number;
            }

            public void setOrder_number(String order_number) {
                this.order_number = order_number;
            }

            public String getState_msg() {
                return state_msg;
            }

            public void setState_msg(String state_msg) {
                this.state_msg = state_msg;
            }

            public String getShare() {
                return share;
            }

            public void setShare(String share) {
                this.share = share;
            }

            public List<SkuListBean> getSku_list() {
                return sku_list;
            }

            public void setSku_list(List<SkuListBean> sku_list) {
                this.sku_list = sku_list;
            }

            public static class SkuListBean {

                /**
                 *
                 */
                private int sku_id;

                /**
                 * 数量
                 */
                private int num;

                /**
                 * 价格
                 */
                private double price;

                /**
                 * 名称
                 */
                private String name;

                /**
                 * 图片地址
                 */
                private String pic;

                public int getSku_id() {
                    return sku_id;
                }

                public void setSku_id(int sku_id) {
                    this.sku_id = sku_id;
                }

                public int getNum() {
                    return num;
                }

                public void setNum(int num) {
                    this.num = num;
                }

                public double getPrice() {
                    return price;
                }

                public void setPrice(double price) {
                    this.price = price;
                }

                public String getName() {
                    return name;
                }

                public void setName(String name) {
                    this.name = name;
                }

                public String getPic() {
                    return pic;
                }

                public void setPic(String pic) {
                    this.pic = pic;
                }
            }
        }
    }
}
