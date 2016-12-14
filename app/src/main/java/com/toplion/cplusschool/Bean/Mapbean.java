package com.toplion.cplusschool.Bean;

import java.io.Serializable;

/**
 * Created by toplion on 2016/8/9.
 */
public class Mapbean implements Serializable {
    private double MAILONGITUDE;
    private double MAILATITUDE;
    private String DPCTIME;
    private String MAIICONURL;
    private String SDSNAME;
    private int MAIID;
    private String SDSCODE;
    private String MAINAME;
    private int MAITYPE;
    private String MTNAME;

    public double getMAILATITUDE() {
        return MAILATITUDE;
    }

    public void setMAILATITUDE(double MAILATITUDE) {
        this.MAILATITUDE = MAILATITUDE;
    }

    public double getMAILONGITUDE() {
        return MAILONGITUDE;
    }

    public void setMAILONGITUDE(double MAILONGITUDE) {
        this.MAILONGITUDE = MAILONGITUDE;
    }

    public String getDPCTIME() {
        return DPCTIME;
    }

    public void setDPCTIME(String DPCTIME) {
        this.DPCTIME = DPCTIME;
    }

    public String getMAIICONURL() {
        return MAIICONURL;
    }

    public void setMAIICONURL(String MAIICONURL) {
        this.MAIICONURL = MAIICONURL;
    }

    public String getSDSNAME() {
        return SDSNAME;
    }

    public void setSDSNAME(String SDSNAME) {
        this.SDSNAME = SDSNAME;
    }

    public int getMAIID() {
        return MAIID;
    }

    public void setMAIID(int MAIID) {
        this.MAIID = MAIID;
    }

    public String getSDSCODE() {
        return SDSCODE;
    }

    public void setSDSCODE(String SDSCODE) {
        this.SDSCODE = SDSCODE;
    }

    public String getMAINAME() {
        return MAINAME;
    }

    public void setMAINAME(String MAINAME) {
        this.MAINAME = MAINAME;
    }

    public int getMAITYPE() {
        return MAITYPE;
    }

    public void setMAITYPE(int MAITYPE) {
        this.MAITYPE = MAITYPE;
    }

    public String getMTNAME() {
        return MTNAME;
    }

    public void setMTNAME(String MTNAME) {
        this.MTNAME = MTNAME;
    }
}
