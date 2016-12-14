package com.toplion.cplusschool.Bean;

import java.io.Serializable;

/**
 * Created by wang
 * on 2016/11/18.
 * 城市bean
 */

public class CityBean implements Serializable{
    /**
     *   "code": "110000",
     "name": "北京市",
     "father_code": "
     */
    private String code;//城市代码
    private String name;//名字

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

}
