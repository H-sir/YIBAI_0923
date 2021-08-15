package com.ybw.yibai.common.bean;

import java.util.List;

public class SkuDetailsBean {

    /**
     * code : 200
     * msg : ok
     * data : {"count":1,"list":[{"sku_id":7915,"product_id":6428,"name":"散尾葵 ","price":240,"month_rent":50,"trade_price":90,"price_code":240,"trade_price_code":"¥90","diameter":35,"outheight":0,"height":200,"offset_ratio":0,"pic1":"https://images.100ybw.com/product/2020/09/12/20200912025653605f5c7135c9145_S.png","pic2":"https://images.100ybw.com/product/2020/09/12/20200912025659505f5c713b43e3d_S.png","pic3":"https://images.100ybw.com/product/2020/09/12/20200912025705445f5c71411a305_M.png","score":0,"mains":0,"comtype":2,"source":{"delivery_day":"","delivery":"暂无货源"},"habit_url":"https://h5.100ybw.com/curing40.html"}]}
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
         * count : 1
         * list : [{"sku_id":7915,"product_id":6428,"name":"散尾葵 ","price":240,"month_rent":50,"trade_price":90,"price_code":240,"trade_price_code":"¥90","diameter":35,"outheight":0,"height":200,"offset_ratio":0,"pic1":"https://images.100ybw.com/product/2020/09/12/20200912025653605f5c7135c9145_S.png","pic2":"https://images.100ybw.com/product/2020/09/12/20200912025659505f5c713b43e3d_S.png","pic3":"https://images.100ybw.com/product/2020/09/12/20200912025705445f5c71411a305_M.png","score":0,"mains":0,"comtype":2,"source":{"delivery_day":"","delivery":"暂无货源"},"habit_url":"https://h5.100ybw.com/curing40.html"}]
         */

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
             * sku_id : 7915
             * product_id : 6428
             * name : 散尾葵
             * price : 240
             * month_rent : 50
             * trade_price : 90
             * price_code : 240
             * trade_price_code : ¥90
             * diameter : 35
             * outheight : 0
             * height : 200
             * offset_ratio : 0
             * pic1 : https://images.100ybw.com/product/2020/09/12/20200912025653605f5c7135c9145_S.png
             * pic2 : https://images.100ybw.com/product/2020/09/12/20200912025659505f5c713b43e3d_S.png
             * pic3 : https://images.100ybw.com/product/2020/09/12/20200912025705445f5c71411a305_M.png
             * score : 0
             * mains : 0
             * comtype : 2
             * source : {"delivery_day":"","delivery":"暂无货源"}
             * habit_url : https://h5.100ybw.com/curing40.html
             */

            private int sku_id;
            private int product_id;
            private String name;
            private float price;
            private float month_rent;
            private float trade_price;
            private float price_code;
            private String trade_price_code;
            private int diameter;
            private int outheight;
            private int height;
            private int offset_ratio;
            private String pic1;
            private String pic2;
            private String pic3;
            private int score;
            private int mains;
            private int comtype;
            private SourceBean source;
            private String habit_url;
            private String categoryCode;

            public String getCategoryCode() {
                return categoryCode;
            }

            public void setCategoryCode(String categoryCode) {
                this.categoryCode = categoryCode;
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

            public float getPrice() {
                return price;
            }

            public void setPrice(float price) {
                this.price = price;
            }

            public float getMonth_rent() {
                return month_rent;
            }

            public void setMonth_rent(float month_rent) {
                this.month_rent = month_rent;
            }

            public float getTrade_price() {
                return trade_price;
            }

            public void setTrade_price(float trade_price) {
                this.trade_price = trade_price;
            }

            public float getPrice_code() {
                return price_code;
            }

            public void setPrice_code(float price_code) {
                this.price_code = price_code;
            }

            public String getTrade_price_code() {
                return trade_price_code;
            }

            public void setTrade_price_code(String trade_price_code) {
                this.trade_price_code = trade_price_code;
            }

            public int getDiameter() {
                return diameter;
            }

            public void setDiameter(int diameter) {
                this.diameter = diameter;
            }

            public int getOutheight() {
                return outheight;
            }

            public void setOutheight(int outheight) {
                this.outheight = outheight;
            }

            public int getHeight() {
                return height;
            }

            public void setHeight(int height) {
                this.height = height;
            }

            public int getOffset_ratio() {
                return offset_ratio;
            }

            public void setOffset_ratio(int offset_ratio) {
                this.offset_ratio = offset_ratio;
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

            public int getScore() {
                return score;
            }

            public void setScore(int score) {
                this.score = score;
            }

            public int getMains() {
                return mains;
            }

            public void setMains(int mains) {
                this.mains = mains;
            }

            public int getComtype() {
                return comtype;
            }

            public void setComtype(int comtype) {
                this.comtype = comtype;
            }

            public SourceBean getSource() {
                return source;
            }

            public void setSource(SourceBean source) {
                this.source = source;
            }

            public String getHabit_url() {
                return habit_url;
            }

            public void setHabit_url(String habit_url) {
                this.habit_url = habit_url;
            }

            public static class SourceBean {
                /**
                 * delivery_day :
                 * delivery : 暂无货源
                 */

                private String delivery_day;
                private String delivery;

                public String getDelivery_day() {
                    return delivery_day;
                }

                public void setDelivery_day(String delivery_day) {
                    this.delivery_day = delivery_day;
                }

                public String getDelivery() {
                    return delivery;
                }

                public void setDelivery(String delivery) {
                    this.delivery = delivery;
                }
            }
        }
    }
}
