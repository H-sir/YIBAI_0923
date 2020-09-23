package com.ybw.yibai.common.bean;

import java.util.Map;

import okhttp3.RequestBody;

/**
 * 客户信息的bean类
 * 创建客户时用到
 *
 * @author sjl
 */
public class CustomersInfo {

    /**
     * 客户ID
     */
    private int id;

    /**
     * 客户logo
     */
    private String logo;

    /**
     * 客户名称
     */
    private String name;

    /**
     * 客户手机
     */
    private String phone;

    /**
     * 客户地址
     */
    private String address;

    private Map<String, RequestBody> params;

    public CustomersInfo(int id) {
        this.id = id;
    }

    public CustomersInfo(int id, String name, String logo) {
        this.id = id;
        this.name = name;
        this.logo = logo;
    }

    public CustomersInfo(String logo, String name, String phone, String address) {
        this.logo = logo;
        this.name = name;
        this.phone = phone;
        this.address = address;
    }

    public CustomersInfo(int id, String logo, String name, String phone, String address) {
        this.id = id;
        this.logo = logo;
        this.name = name;
        this.phone = phone;
        this.address = address;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getLogo() {
        return logo;
    }

    public void setLogo(String logo) {
        this.logo = logo;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public Map<String, RequestBody> getParams() {
        return params;
    }

    public void setParams(Map<String, RequestBody> params) {
        this.params = params;
    }
}
