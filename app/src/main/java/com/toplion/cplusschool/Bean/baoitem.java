package com.toplion.cplusschool.Bean;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by toplion on 2016/8/1.
 */
public class baoitem implements Serializable {
    private int RISTATUS;// 状态
    private String WINAME;// 处理窗口名称
    private String RIID;// 预约号
    private int WIID; //窗口
    private long RISTARTTIME;//开始时间
    private String RTTNAME;//报账类型
    private String RTTID;// 类型编号
    private String RIRESERVATIONZGH;//预约人
    private long RIENDTIME;//结束时间
    private ArrayList<StandardInfo> standardInfo;

    public int getRISTATUS() {
        return RISTATUS;
    }

    public void setRISTATUS(int RISTATUS) {
        this.RISTATUS = RISTATUS;
    }

    public String getWINAME() {
        return WINAME;
    }

    public void setWINAME(String WINAME) {
        this.WINAME = WINAME;
    }

    public String getRIID() {
        return RIID;
    }

    public void setRIID(String RIID) {
        this.RIID = RIID;
    }

    public int getWIID() {
        return WIID;
    }

    public void setWIID(int WIID) {
        this.WIID = WIID;
    }

    public long getRISTARTTIME() {
        return RISTARTTIME;
    }

    public void setRISTARTTIME(long RISTARTTIME) {
        this.RISTARTTIME = RISTARTTIME;
    }

    public String getRTTNAME() {
        return RTTNAME;
    }

    public void setRTTNAME(String RTTNAME) {
        this.RTTNAME = RTTNAME;
    }

    public String getRTTID() {
        return RTTID;
    }

    public void setRTTID(String RTTID) {
        this.RTTID = RTTID;
    }

    public String getRIRESERVATIONZGH() {
        return RIRESERVATIONZGH;
    }

    public void setRIRESERVATIONZGH(String RIRESERVATIONZGH) {
        this.RIRESERVATIONZGH = RIRESERVATIONZGH;
    }

    public long getRIENDTIME() {
        return RIENDTIME;
    }

    public void setRIENDTIME(long RIENDTIME) {
        this.RIENDTIME = RIENDTIME;
    }

    public ArrayList<StandardInfo> getStandardInfo() {
        return standardInfo;
    }

    public void setStandardInfo(ArrayList<StandardInfo> standardInfo) {
        this.standardInfo = standardInfo;
    }
}
