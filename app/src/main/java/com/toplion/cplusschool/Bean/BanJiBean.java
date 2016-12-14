package com.toplion.cplusschool.Bean;

import java.io.Serializable;

/**
 * Created by wang
 * on 2016/8/15.
 * @班级
 */
public class BanJiBean implements Serializable{
    private String bjdm;//班级代码
    private String bjm;//班级名

    public String getBjdm() {
        return bjdm;
    }

    public void setBjdm(String bjdm) {
        this.bjdm = bjdm;
    }

    public String getBjm() {
        return bjm;
    }

    public void setBjm(String bjm) {
        this.bjm = bjm;
    }
}
