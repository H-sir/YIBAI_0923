package com.ybw.yibai.common.bean;

import com.google.gson.annotations.SerializedName;

/**
 * <pre>
 *     author : HKR
 *     time   : 2020/08/27
 *     desc   : 创建设计返回的数据
 * </pre>
 */
public class DesignCreate {

    /**
     * code : 200
     * msg : ok
     * data : {"isnew":0,"desing_number":"2020082710156555","scheme_id":""}
     */

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
         * isnew : 0
         * desing_number : 2020082710156555
         * scheme_id :
         */

        @SerializedName("isnew") private int isnew;
        @SerializedName("desing_number") private String desingNumber;
        @SerializedName("scheme_id") private String schemeId;

        public int getIsnew() {
            return isnew;
        }

        public void setIsnew(int isnew) {
            this.isnew = isnew;
        }

        public String getDesingNumber() {
            return desingNumber;
        }

        public void setDesingNumber(String desingNumber) {
            this.desingNumber = desingNumber;
        }

        public String getSchemeId() {
            return schemeId;
        }

        public void setSchemeId(String schemeId) {
            this.schemeId = schemeId;
        }
    }
}
