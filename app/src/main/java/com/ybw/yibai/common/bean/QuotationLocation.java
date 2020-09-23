package com.ybw.yibai.common.bean;

import java.util.List;

/**
 * 报价位置明细列表
 *
 * @author sjl
 */
public class QuotationLocation {

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
             * 清单位置id
             */
            private int quote_place_id;

            /**
             * 位置名
             */
            private String quote_place_name;

            /**
             * 设计图
             */
            private String design_pic;

            /**
             * 是否选中
             * <p>
             * android 开发人员自定义的非后台给定的数据
             */
            private boolean select;

            /**
             * 位置明细
             */
            private List<QuotelistBean> quotelist;

            public int getQuote_place_id() {
                return quote_place_id;
            }

            public void setQuote_place_id(int quote_place_id) {
                this.quote_place_id = quote_place_id;
            }

            public String getQuote_place_name() {
                return quote_place_name;
            }

            public void setQuote_place_name(String quote_place_name) {
                this.quote_place_name = quote_place_name;
            }

            public String getDesign_pic() {
                return design_pic;
            }

            public void setDesign_pic(String design_pic) {
                this.design_pic = design_pic;
            }

            public boolean isSelect() {
                return select;
            }

            public void setSelect(boolean select) {
                this.select = select;
            }

            public List<QuotelistBean> getQuotelist() {
                return quotelist;
            }

            public void setQuotelist(List<QuotelistBean> quotelist) {
                this.quotelist = quotelist;
            }

            public static class QuotelistBean {

                /**
                 * 清单id
                 */
                private int quote_id;

                /**
                 * 主产品名
                 */
                private String first_name;

                /**
                 * 附加产品名
                 */
                private String second_name;

                /**
                 * 图片
                 */
                private String pic;

                /**
                 * 售价(单价)
                 */
                private float price;

                /**
                 * 月租(单价)
                 */
                private float month_rent;

                /**
                 * 年租(单价)
                 */
                private float year_rent;

                /**
                 * 数量
                 */
                private int num;

                public QuotelistBean() {
                }

                public QuotelistBean(int quote_id, String first_name, String second_name,
                                     String pic, float price, float month_rent, float year_rent, int num) {
                    this.quote_id = quote_id;
                    this.first_name = first_name;
                    this.second_name = second_name;
                    this.pic = pic;
                    this.price = price;
                    this.month_rent = month_rent;
                    this.year_rent = year_rent;
                    this.num = num;
                }

                public int getQuote_id() {
                    return quote_id;
                }

                public void setQuote_id(int quote_id) {
                    this.quote_id = quote_id;
                }

                public String getFirst_name() {
                    return first_name;
                }

                public void setFirst_name(String first_name) {
                    this.first_name = first_name;
                }

                public String getSecond_name() {
                    return second_name;
                }

                public void setSecond_name(String second_name) {
                    this.second_name = second_name;
                }

                public String getPic() {
                    return pic;
                }

                public void setPic(String pic) {
                    this.pic = pic;
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

                public float getYear_rent() {
                    return year_rent;
                }

                public void setYear_rent(float year_rent) {
                    this.year_rent = year_rent;
                }

                public int getNum() {
                    return num;
                }

                public void setNum(int num) {
                    this.num = num;
                }
            }
        }
    }
}
