package com.toplion.cplusschool.Bean;

import java.io.Serializable;

/**
 * Created by wang
 * on 2016/9/8.
 * 教职工通讯录具体人员bean
 */
public class PersonBean implements Serializable {
    private String nameid;
    private String name;

    public String getNameid() {
        return nameid;
    }

    public void setNameid(String nameid) {
        this.nameid = nameid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
