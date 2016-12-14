package com.toplion.cplusschool.Bean;

import java.io.Serializable;

/**
 * Created by toplion on 2016/8/17.
 */
public class Xiaolibean implements Serializable {
    private String cicontent;
    private int ciid;
    private String cititle;
    private int citype;
    private String sdscode;
    private String ticode;

    public String getCicontent() {
        return cicontent;
    }

    public void setCicontent(String cicontent) {
        this.cicontent = cicontent;
    }

    public int getCiid() {
        return ciid;
    }

    public void setCiid(int ciid) {
        this.ciid = ciid;
    }

    public String getCititle() {
        return cititle;
    }

    public void setCititle(String cititle) {
        this.cititle = cititle;
    }

    public int getCitype() {
        return citype;
    }

    public void setCitype(int citype) {
        this.citype = citype;
    }

    public String getSdscode() {
        return sdscode;
    }

    public void setSdscode(String sdscode) {
        this.sdscode = sdscode;
    }

    public String getTicode() {
        return ticode;
    }

    public void setTicode(String ticode) {
        this.ticode = ticode;
    }
}
