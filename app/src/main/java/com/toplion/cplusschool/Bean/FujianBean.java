package com.toplion.cplusschool.Bean;

import java.io.Serializable;

/**
 * Created by toplion on 2016/6/27.
 */
public class FujianBean implements Serializable {
    private String fujianid;
    private String fujianname;
    private String fujianurl;

    public String getFujianid() {
        return fujianid;
    }

    public void setFujianid(String fujianid) {
        this.fujianid = fujianid;
    }

    public String getFujianurl() {
        return fujianurl;
    }

    public void setFujianurl(String fujianurl) {
        this.fujianurl = fujianurl;
    }

    public String getFujianname() {
        return fujianname;
    }

    public void setFujianname(String fujianname) {
        this.fujianname = fujianname;
    }
}
