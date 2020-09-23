package com.ybw.yibai.common.bean;

/**
 * 修改产品sku使用状态
 *
 * @author sjl
 */
public class UpdateSKUUseState {

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

        private int sku_id;
        private int usestate;

        public int getSku_id() {
            return sku_id;
        }

        public void setSku_id(int sku_id) {
            this.sku_id = sku_id;
        }

        public int getUsestate() {
            return usestate;
        }

        public void setUsestate(int usestate) {
            this.usestate = usestate;
        }
    }
}
