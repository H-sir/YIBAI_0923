package com.ybw.yibai.common.bean;

/**
 * 待摆放清单加入到报价位置
 *
 * @author sjl
 */
public class AddQuotationLocation {

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
        private int quote_id;

        public int getQuote_place_id() {
            return quote_place_id;
        }

        public void setQuote_place_id(int quote_place_id) {
            this.quote_place_id = quote_place_id;
        }

        public int getQuote_id() {
            return quote_id;
        }

        public void setQuote_id(int quote_id) {
            this.quote_id = quote_id;
        }
    }
}
