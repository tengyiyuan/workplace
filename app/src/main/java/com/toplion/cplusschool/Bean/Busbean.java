package com.toplion.cplusschool.Bean;

import java.io.Serializable;

/**
 * Created by toplion on 2016/8/17.
 */
public class Busbean implements Serializable {
    private int CITYPE;
    private String TINAME;
    private String CITITLE;
    private String SDSNAME;
    private int CIID;
    private String SDSCODE;

    public int getCITYPE() {
        return CITYPE;
    }

    public void setCITYPE(int CITYPE) {
        this.CITYPE = CITYPE;
    }

    public String getTINAME() {
        return TINAME;
    }

    public void setTINAME(String TINAME) {
        this.TINAME = TINAME;
    }

    public String getCITITLE() {
        return CITITLE;
    }

    public void setCITITLE(String CITITLE) {
        this.CITITLE = CITITLE;
    }

    public String getSDSNAME() {
        return SDSNAME;
    }

    public void setSDSNAME(String SDSNAME) {
        this.SDSNAME = SDSNAME;
    }

    public int getCIID() {
        return CIID;
    }

    public void setCIID(int CIID) {
        this.CIID = CIID;
    }

    public String getSDSCODE() {
        return SDSCODE;
    }

    public void setSDSCODE(String SDSCODE) {
        this.SDSCODE = SDSCODE;
    }
}
