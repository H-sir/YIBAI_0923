package com.ybw.yibai.common.bean;

/**
 * 用户信息
 *
 * @author sjl
 */
public class UserInfo {

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
         * 账号id
         */
        private int uid;

        /**
         * 账号
         */
        private String username;

        /**
         * 昵称
         */
        private String nickname;

        /**
         * 真实姓名
         */
        private String truename;

        /**
         * 角色(除了service以外,进货改价需验证密码)
         */
        private String role;

        /**
         * 电话
         */
        private String telephone;

        /**
         * 头像
         */
        private String head;

        /**
         * 邮箱
         */
        private String email;

        /**
         * 地址
         */
        private String address;

        /**
         * 公司名称
         */
        private String company;

        /**
         * 公司LOGO
         */
        private String company_logo_pic;

        /**
         * 公司简介(html代码,需base64解码)
         */
        private String company_details;

        /**
         * 公司简介图片
         */
        private String company_details_pic;

        /**
         * 是否绑定了微信0否;1是
         */
        private int isbindwx;

        /**
         * 会员等级(1体验2专业3终生旗舰)
         */
        private int vip_level;

        /**
         * 邀请背景图片
         */
        private String yqbg;

        /**
         * 个人中心活动邀请的背景图
         */
        private String yqhybg;

        /**
         * 邀请规则图片
         */
        private String xxgzbg;

        /**
         * 邀请注册链接
         */
        private String invite_url;

        /**
         * 邀请记录地址
         */
        private String record_url;

        /**
         * 用户升级vip页面地址
         */
        private String uservip_url;

        /**
         * 我的钱包地址
         */
        private String mywallet_url;

        /**
         * 余额提现地址
         */
        private String takecash_url;

        /**
         * 水印图片
         */
        private String watermark_pic;

        /**
         * 结算货币符号
         */
        private String currency;

        /**
         * 租价幅度%
         */
        private int increase_rent;

        /**
         * 售价幅度%
         */
        private int increase_sell;

        /**
         * 当前货源城市名称
         */
        private String city_name;

        public int getUid() {
            return uid;
        }

        public void setUid(int uid) {
            this.uid = uid;
        }

        public String getUsername() {
            return username;
        }

        public void setUsername(String username) {
            this.username = username;
        }

        public String getNickname() {
            return nickname;
        }

        public void setNickname(String nickname) {
            this.nickname = nickname;
        }

        public String getTruename() {
            return truename;
        }

        public void setTruename(String truename) {
            this.truename = truename;
        }

        public String getRole() {
            return role;
        }

        public void setRole(String role) {
            this.role = role;
        }

        public String getTelephone() {
            return telephone;
        }

        public void setTelephone(String telephone) {
            this.telephone = telephone;
        }

        public String getHead() {
            return head;
        }

        public void setHead(String head) {
            this.head = head;
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

        public String getCompany() {
            return company;
        }

        public void setCompany(String company) {
            this.company = company;
        }

        public String getCompany_logo_pic() {
            return company_logo_pic;
        }

        public void setCompany_logo_pic(String company_logo_pic) {
            this.company_logo_pic = company_logo_pic;
        }

        public String getCompany_details() {
            return company_details;
        }

        public void setCompany_details(String company_details) {
            this.company_details = company_details;
        }

        public String getCompany_details_pic() {
            return company_details_pic;
        }

        public void setCompany_details_pic(String company_details_pic) {
            this.company_details_pic = company_details_pic;
        }

        public int getIsbindwx() {
            return isbindwx;
        }

        public void setIsbindwx(int isbindwx) {
            this.isbindwx = isbindwx;
        }

        public int getVip_level() {
            return vip_level;
        }

        public void setVip_level(int vip_level) {
            this.vip_level = vip_level;
        }

        public String getYqbg() {
            return yqbg;
        }

        public void setYqbg(String yqbg) {
            this.yqbg = yqbg;
        }

        public String getYqhybg() {
            return yqhybg;
        }

        public void setYqhybg(String yqhybg) {
            this.yqhybg = yqhybg;
        }

        public String getXxgzbg() {
            return xxgzbg;
        }

        public void setXxgzbg(String xxgzbg) {
            this.xxgzbg = xxgzbg;
        }

        public String getInvite_url() {
            return invite_url;
        }

        public void setInvite_url(String invite_url) {
            this.invite_url = invite_url;
        }

        public String getRecord_url() {
            return record_url;
        }

        public void setRecord_url(String record_url) {
            this.record_url = record_url;
        }

        public String getUservip_url() {
            return uservip_url;
        }

        public void setUservip_url(String uservip_url) {
            this.uservip_url = uservip_url;
        }

        public String getMywallet_url() {
            return mywallet_url;
        }

        public void setMywallet_url(String mywallet_url) {
            this.mywallet_url = mywallet_url;
        }

        public String getTakecash_url() {
            return takecash_url;
        }

        public void setTakecash_url(String takecash_url) {
            this.takecash_url = takecash_url;
        }

        public String getWatermark_pic() {
            return watermark_pic;
        }

        public void setWatermark_pic(String watermark_pic) {
            this.watermark_pic = watermark_pic;
        }

        public String getCurrency() {
            return currency;
        }

        public void setCurrency(String currency) {
            this.currency = currency;
        }

        public int getIncrease_rent() {
            return increase_rent;
        }

        public void setIncrease_rent(int increase_rent) {
            this.increase_rent = increase_rent;
        }

        public int getIncrease_sell() {
            return increase_sell;
        }

        public void setIncrease_sell(int increase_sell) {
            this.increase_sell = increase_sell;
        }

        public String getCity_name() {
            return city_name;
        }

        public void setCity_name(String city_name) {
            this.city_name = city_name;
        }
    }
}
