package com.toplion.cplusschool.Bean;

import java.io.Serializable;

/**
 * @author wang
 *         Created by toplion on 2016/3/24.
 */
public class CommonBean implements Serializable {
    private String id;//问题id
    private String other;
    private String des;//描述

    public CommonBean() {
    }

    public CommonBean(String id, String des) {
        this.id = id;
        this.des = des;
    }

    public CommonBean(String id, String other, String des) {
        this.id = id;
        this.des = des;
        this.other = other;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getOther() {
        return other;
    }

    public void setOther(String other) {
        this.other = other;
    }

    public String getDes() {
        return des;
    }

    public void setDes(String des) {
        this.des = des;
    }
}
