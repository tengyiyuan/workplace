package com.toplion.cplusschool.Bean;

import java.io.Serializable;

/**
 * Created by toplion on 2016/9/12.
 */
public class Kongitem implements Serializable {
    private  String kongid;
    private String toptitle;
    private String state;
    private String bumen;


    public String getToptitle() {
        return toptitle;
    }

    public void setToptitle(String toptitle) {
        this.toptitle = toptitle;
    }

    public String getBumen() {
        return bumen;
    }

    public void setBumen(String bumen) {
        this.bumen = bumen;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getKongid() {
        return kongid;
    }

    public void setKongid(String kongid) {
        this.kongid = kongid;
    }
}
