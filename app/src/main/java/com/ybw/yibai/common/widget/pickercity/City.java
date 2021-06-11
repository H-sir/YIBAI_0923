package com.ybw.yibai.common.widget.pickercity;

/**
 * Created by HKR on 2021/6/11.
 */

public class City {
    public String name;
    public String pinyi;
    public String code;

    public City(String name,String code, String pinyi) {
        super();
        this.name = name;
        this.pinyi = pinyi;
        this.code = code;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public City() {
        super();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPinyi() {
        return pinyi;
    }

    public void setPinyi(String pinyi) {
        this.pinyi = pinyi;
    }
}
