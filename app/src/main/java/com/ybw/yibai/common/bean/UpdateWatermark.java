package com.ybw.yibai.common.bean;

/**
 * 修改水印图片
 *
 * @author sjl
 */
public class UpdateWatermark {

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

        private String watermark_pic;

        public String getWatermark_pic() {
            return watermark_pic;
        }

        public void setWatermark_pic(String watermark_pic) {
            this.watermark_pic = watermark_pic;
        }
    }
}
