package com.ybw.yibai.common.bean;

/**
 * 创建客户的bean类
 *
 * @author sjl
 */
public class CreateCustomers {

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
         * 客户id
         */
        private int id;

        /**
         * 客户名
         */
        private String name;

        /**
         * 客户logo
         */
        private String logo;

        /**
         * 电话
         */
        private String tel;

        /**
         * 邮箱
         */
        private String email;

        /**
         * 地址
         */
        private String address;

        /**
         * 客户编号
         */
        private String number;

        /**
         * 自定义编号
         */
        private String second_number;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getLogo() {
            return logo;
        }

        public void setLogo(String logo) {
            this.logo = logo;
        }

        public String getTel() {
            return tel;
        }

        public void setTel(String tel) {
            this.tel = tel;
        }

        public String getEmail() {
            return email;
        }

        public void setEmail(String email) {
            this.email = email;
        }

        public String getAddress() {
            return address;
        }

        public void setAddress(String address) {
            this.address = address;
        }

        public String getNumber() {
            return number;
        }

        public void setNumber(String number) {
            this.number = number;
        }

        public String getSecond_number() {
            return second_number;
        }

        public void setSecond_number(String second_number) {
            this.second_number = second_number;
        }
    }
}
