package com.ybw.yibai.common.bean;

import java.util.List;

/**
 * 摆放清单列表/报价清单列表
 *
 * @author sjl
 */
public class PlacementQrQuotationList {

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
             * 主产品SKU_ID
             */
            private int first_sku_id;

            /**
             * 附加产品SKU_ID
             */
            private int second_sku_id;

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

            /**
             * 主产品code plant植物/pot盆器
             */
            private String first_cate_code;

            /**
             * 组合模式 1单图模式,2搭配上部,3搭配下部
             */
            private int comtype;

            /*----------*/

            /**
             * 报价方式
             * 0:购买
             * 1:月租
             * 2:年租
             * Android 开发人员自定义的数据
             */
            private int mode;

            /**
             * 是否显示CheckBox
             * Android 开发人员自定义的数据
             */
            private boolean showCheckBox;

            /**
             * 是否选中CheckBox
             * Android 开发人员自定义的数据
             */
            private boolean selectCheckBox;

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

            public int getFirst_sku_id() {
                return first_sku_id;
            }

            public void setFirst_sku_id(int first_sku_id) {
                this.first_sku_id = first_sku_id;
            }

            public int getSecond_sku_id() {
                return second_sku_id;
            }

            public void setSecond_sku_id(int second_sku_id) {
                this.second_sku_id = second_sku_id;
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

            public String getFirst_cate_code() {
                return first_cate_code;
            }

            public void setFirst_cate_code(String first_cate_code) {
                this.first_cate_code = first_cate_code;
            }

            public int getComtype() {
                return comtype;
            }

            public void setComtype(int comtype) {
                this.comtype = comtype;
            }

            public int getMode() {
                return mode;
            }

            public void setMode(int mode) {
                this.mode = mode;
            }

            public boolean isShowCheckBox() {
                return showCheckBox;
            }

            public void setShowCheckBox(boolean showCheckBox) {
                this.showCheckBox = showCheckBox;
            }

            public boolean isSelectCheckBox() {
                return selectCheckBox;
            }

            public void setSelectCheckBox(boolean selectCheckBox) {
                this.selectCheckBox = selectCheckBox;
            }
        }
    }
}
