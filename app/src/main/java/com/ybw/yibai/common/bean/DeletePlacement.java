package com.ybw.yibai.common.bean;

import java.util.List;

/**
 * 删除待摆放清单产品
 *
 * @author sjl
 */
public class DeletePlacement {
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
        private List<?> quote_ids;

        public List<?> getQuote_ids() {
            return quote_ids;
        }

        public void setQuote_ids(List<?> quote_ids) {
            this.quote_ids = quote_ids;
        }
    }
}
