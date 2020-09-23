package com.ybw.yibai.common.bean;

/**
 * APP 更新
 *
 * @author sjl
 */
public class AppUpdate {

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

        /**
         * 新版本号
         */
        private String version;

        /**
         * 最新版本描述(base64解码)
         */
        private String content;

        /**
         * apk下载地址
         */
        private String apk;

        /**
         * 是否需要更新(1更新0否)
         */
        private int renew;

        public String getVersion() {
            return version;
        }

        public void setVersion(String version) {
            this.version = version;
        }

        public String getContent() {
            return content;
        }

        public void setContent(String content) {
            this.content = content;
        }

        public String getApk() {
            return apk;
        }

        public void setApk(String apk) {
            this.apk = apk;
        }

        public int getRenew() {
            return renew;
        }

        public void setRenew(int renew) {
            this.renew = renew;
        }
    }
}