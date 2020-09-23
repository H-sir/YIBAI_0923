package com.ybw.yibai.common.bean;

/**
 * 修改报价位置信息
 *
 * @author sjl
 */
public class UpdateQuotationLocation {

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

        private String name;

        private String design_pic;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getDesign_pic() {
            return design_pic;
        }

        public void setDesign_pic(String design_pic) {
            this.design_pic = design_pic;
        }
    }
}
