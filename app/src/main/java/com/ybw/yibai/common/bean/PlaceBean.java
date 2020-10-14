package com.ybw.yibai.common.bean;

import com.google.gson.annotations.SerializedName;

/**
 * <pre>
 *     author : HKR
 *     time   : 2020/10/14
 *     desc   :
 * </pre>
 */
public class PlaceBean {

    /**
     * code : 200
     * msg : ok
     * data : {"address":"浙江省绍兴市嵊州市双塔路128号","cityname":"绍兴市","citycode":"330600"}
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
         * address : 浙江省绍兴市嵊州市双塔路128号
         * cityname : 绍兴市
         * citycode : 330600
         */

        @SerializedName("address") private String address;
        @SerializedName("cityname") private String cityname;
        @SerializedName("citycode") private String citycode;

        public String getAddress() {
            return address;
        }

        public void setAddress(String address) {
            this.address = address;
        }

        public String getCityname() {
            return cityname;
        }

        public void setCityname(String cityname) {
            this.cityname = cityname;
        }

        public String getCitycode() {
            return citycode;
        }

        public void setCitycode(String citycode) {
            this.citycode = citycode;
        }
    }
}
