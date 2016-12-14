package com.toplion.cplusschool.PhotoWall.bean;

import java.io.Serializable;

/**
 * Created by toplion on 2016/11/28.
 */

public class PhotoInfoBean implements Serializable{
    /**
     * "PWBFLOWERSNUMBER": 6,
     "PWBCREATETIME": "2016-11-23 14:38:19",
     "PWBURL": "http://123.233.121.17:15100/upfile/sdjzu/20161020/thumb/201610201133361627020161020113449.jpeg",
     "PWBID": 42,
     "YHBH": "10712",
     "SDSCODE": "sdjzu",
     "PWBTOTALNUMBER": 1
     */
    private int PWBFLOWERSNUMBER;//鲜花总数量
    private String PWBCREATETIME;//发布时间
    private String PWBURL;//照片地址
    private int PWBID;//照片编号
    private String YHBH;//用户编号
    private String SDSCODE;
    private String SDS_NAME;
    private int PWBTOTALNUMBER;//评价总数量
    private String NC;//昵称
    private int PHH;//排行号
    private String XBM;//性别
    private String TXDZ;//头像地址

    public int getPWBFLOWERSNUMBER() {
        return PWBFLOWERSNUMBER;
    }

    public void setPWBFLOWERSNUMBER(int PWBFLOWERSNUMBER) {
        this.PWBFLOWERSNUMBER = PWBFLOWERSNUMBER;
    }

    public String getPWBCREATETIME() {
        return PWBCREATETIME;
    }

    public void setPWBCREATETIME(String PWBCREATETIME) {
        this.PWBCREATETIME = PWBCREATETIME;
    }

    public String getPWBURL() {
        return PWBURL;
    }

    public void setPWBURL(String PWBURL) {
        this.PWBURL = PWBURL;
    }

    public int getPWBID() {
        return PWBID;
    }

    public void setPWBID(int PWBID) {
        this.PWBID = PWBID;
    }

    public String getYHBH() {
        return YHBH;
    }

    public void setYHBH(String YHBH) {
        this.YHBH = YHBH;
    }

    public String getSDSCODE() {
        return SDSCODE;
    }

    public void setSDSCODE(String SDSCODE) {
        this.SDSCODE = SDSCODE;
    }

    public String getSDS_NAME() {
        return SDS_NAME;
    }

    public void setSDS_NAME(String SDS_NAME) {
        this.SDS_NAME = SDS_NAME;
    }

    public int getPWBTOTALNUMBER() {
        return PWBTOTALNUMBER;
    }

    public void setPWBTOTALNUMBER(int PWBTOTALNUMBER) {
        this.PWBTOTALNUMBER = PWBTOTALNUMBER;
    }

    public String getNC() {
        return NC;
    }

    public void setNC(String NC) {
        this.NC = NC;
    }

    public int getPHH() {
        return PHH;
    }

    public void setPHH(int PHH) {
        this.PHH = PHH;
    }

    public String getXBM() {
        return XBM;
    }

    public void setXBM(String XBM) {
        this.XBM = XBM;
    }

    public String getTXDZ() {
        return TXDZ;
    }

    public void setTXDZ(String TXDZ) {
        this.TXDZ = TXDZ;
    }
}
