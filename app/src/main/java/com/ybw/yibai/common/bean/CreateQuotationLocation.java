package com.ybw.yibai.common.bean;

/**
 * 创建报价位置
 *
 * @author sjl
 */
public class CreateQuotationLocation {

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

        private int quote_place_id;
        private String quote_place_name;

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
    }
}
