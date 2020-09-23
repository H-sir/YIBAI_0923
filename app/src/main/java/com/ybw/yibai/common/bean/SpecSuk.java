package com.ybw.yibai.common.bean;

import java.util.List;

/**
 * 根据大中小随机获取组合的URL
 *
 * @author sjl
 */
public class SpecSuk {

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

        /**
         * 组合出来的盆栽规格大小
         */
        private int type;

        private List<ListBean> list;

        public int getCount() {
            return count;
        }

        public void setCount(int count) {
            this.count = count;
        }

        public int getType() {
            return type;
        }

        public void setType(int type) {
            this.type = type;
        }

        public List<ListBean> getList() {
            return list;
        }

        public void setList(List<ListBean> list) {
            this.list = list;
        }

        public static class ListBean {

            /**
             * 类型 plant植物 pot盆
             */
            private String type;

            /**
             * SKU ID
             */
            private int sku_id;

            /**
             * 产品id
             */
            private int product_id;

            /**
             * 产品名称
             */
            private String name;

            /**
             * sku主图(确定sku后用于替换显示产品图片)
             */
            private String pic1;

            /**
             * sku模拟搭配图(没有此图,此sku将不能模拟)
             */
            private String pic2;

            /**
             * sku自由搭配图(没有此图,此sku将不能搭配)
             */
            private String pic3;

            /**
             *
             */
            private String score;

            /**
             * 零售价
             */
            private double price;

            /**
             * 月租
             */
            private double month_rent;

            /**
             * 批发价
             */
            private double trade_price;

            /**
             * 搭配高度
             */
            private double height;

            /**
             * 搭配口径
             */
            private double diameter;

            /**
             * 偏移量(仅花盆类别的才会返回)
             */
            private double offset_ratio;

            /**
             * 组合模式1组合2单产品
             */
            private int comtype;

            /**
             * 售价代码
             */
            private String price_code;

            /**
             * 批发价代码
             */
            private String trade_price_code;

            public String getType() {
                return type;
            }

            public void setType(String type) {
                this.type = type;
            }

            public int getSku_id() {
                return sku_id;
            }

            public void setSku_id(int sku_id) {
                this.sku_id = sku_id;
            }

            public int getProduct_id() {
                return product_id;
            }

            public void setProduct_id(int product_id) {
                this.product_id = product_id;
            }

            public String getName() {
                return name;
            }

            public void setName(String name) {
                this.name = name;
            }

            public String getPic1() {
                return pic1;
            }

            public void setPic1(String pic1) {
                this.pic1 = pic1;
            }

            public String getPic2() {
                return pic2;
            }

            public void setPic2(String pic2) {
                this.pic2 = pic2;
            }

            public String getPic3() {
                return pic3;
            }

            public void setPic3(String pic3) {
                this.pic3 = pic3;
            }

            public String getScore() {
                return score;
            }

            public void setScore(String score) {
                this.score = score;
            }

            public double getPrice() {
                return price;
            }

            public void setPrice(double price) {
                this.price = price;
            }

            public double getMonth_rent() {
                return month_rent;
            }

            public void setMonth_rent(double month_rent) {
                this.month_rent = month_rent;
            }

            public double getTrade_price() {
                return trade_price;
            }

            public void setTrade_price(double trade_price) {
                this.trade_price = trade_price;
            }

            public double getHeight() {
                return height;
            }

            public void setHeight(double height) {
                this.height = height;
            }

            public double getDiameter() {
                return diameter;
            }

            public void setDiameter(double diameter) {
                this.diameter = diameter;
            }

            public double getOffset_ratio() {
                return offset_ratio;
            }

            public void setOffset_ratio(double offset_ratio) {
                this.offset_ratio = offset_ratio;
            }

            public int getComtype() {
                return comtype;
            }

            public void setComtype(int comtype) {
                this.comtype = comtype;
            }

            public String getPrice_code() {
                return price_code;
            }

            public void setPrice_code(String price_code) {
                this.price_code = price_code;
            }

            public String getTrade_price_code() {
                return trade_price_code;
            }

            public void setTrade_price_code(String trade_price_code) {
                this.trade_price_code = trade_price_code;
            }
        }
    }
}
