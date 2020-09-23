package com.ybw.yibai.common.bean;

import java.util.List;

/**
 * 报价列表
 *
 * @author sjl
 */
public class QuotationList {

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
             * 税点
             */
            private int tax;

            /**
             * 0/空获取所有,1待客户确定,2待跟进3已成交4已失效
             */
            private int state;

            /**
             * 商品数量
             */
            private int goods_num;

            /**
             * 优惠金额
             */
            private double district_money;

            /**
             * 订单号
             */
            private String order_number;

            /**
             * 客户名称
             */
            private String cname;

            /**
             * 分享地址
             */
            private String share;

            /**
             * 状态内容
             */
            private String statemsg;

            /**
             * 金额
             */
            private String payable_amount;

            /**
             * 设计图列表(如果没有则无此字段)  array
             */
            private List<String> drawing_pic;

            public int getTax() {
                return tax;
            }

            public void setTax(int tax) {
                this.tax = tax;
            }

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

            public double getDistrict_money() {
                return district_money;
            }

            public void setDistrict_money(double district_money) {
                this.district_money = district_money;
            }

            public String getOrder_number() {
                return order_number;
            }

            public void setOrder_number(String order_number) {
                this.order_number = order_number;
            }

            public String getCname() {
                return cname;
            }

            public void setCname(String cname) {
                this.cname = cname;
            }

            public String getShare() {
                return share;
            }

            public void setShare(String share) {
                this.share = share;
            }

            public String getStatemsg() {
                return statemsg;
            }

            public void setStatemsg(String statemsg) {
                this.statemsg = statemsg;
            }

            public String getPayable_amount() {
                return payable_amount;
            }

            public void setPayable_amount(String payable_amount) {
                this.payable_amount = payable_amount;
            }

            public List<String> getDrawing_pic() {
                return drawing_pic;
            }

            public void setDrawing_pic(List<String> drawing_pic) {
                this.drawing_pic = drawing_pic;
            }
        }
    }
}
