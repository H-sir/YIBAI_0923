package com.ybw.yibai.common.bean;

import java.io.Serializable;

/**
 * 登陆返回的数据的bean类
 *
 * @author sjl
 */
public class LoginInfo {

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

    public static class DataBean  implements Serializable {

        /**
         * 账号id
         */
        private int uid;

        /**
         * 令牌(只限本次登录有效)
         */
        private String token;

        /**
         * 当且仅当该移动应用已获得该用户的 userinfo 授权时,才会出现该字段
         */
        private String unionid;

        /**
         * 授权用户唯一标识
         */
        private String openid;

        public int getUid() {
            return uid;
        }

        public void setUid(int uid) {
            this.uid = uid;
        }

        public String getToken() {
            return token;
        }

        public void setToken(String token) {
            this.token = token;
        }

        public String getUnionid() {
            return unionid;
        }

        public void setUnionid(String unionid) {
            this.unionid = unionid;
        }

        public String getOpenid() {
            return openid;
        }

        public void setOpenid(String openid) {
            this.openid = openid;
        }
    }
}
