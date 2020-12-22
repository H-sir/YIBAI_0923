package com.ybw.yibai.common.bean;

import com.google.gson.annotations.SerializedName;

/**
 * <pre>
 *     author : HKR
 *     time   : 2020/12/17
 *     desc   :
 * </pre>
 */
public class CheckShareBean {

    @SerializedName("code") private int code;
    @SerializedName("msg") private String msg;
    @SerializedName("data") private DataBean data;

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
         * desing_number : 2020082810110099
         * scheme_name : yy
         * scheme_id : 489
         * imglist : {"id":1292,"pic":"http://images.100ybw.com/design/2020/08/28/20200828115345795f487fc9142dc.jpg"}
         */

        @SerializedName("status") private int status;

        public int getStatus() {
            return status;
        }

        public void setStatus(int status) {
            this.status = status;
        }
    }
}
