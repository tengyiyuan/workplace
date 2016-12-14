package com.toplion.cplusschool.Bean;

import java.io.Serializable;

/**
 * Created by wangshengbo
 * on 2016/4/20.
 * 楼层bean
 */
public class FloorBean  implements Serializable {
    /**
     * "WID": "1",
     "JXLH": "BW",
     "JXLM": "博文馆",
     "KJSSL": 78
     */
    private String WID;//唯一标识
    private String LCZS;//楼层总数
    private String JXLH;//教学楼号
    private String JXLM;//教学楼名
    private int KJSSL;//空教室数量

    public String getWID() {
        return WID;
    }

    public void setWID(String WID) {
        this.WID = WID;
    }

    public String getLCZS() {
        return LCZS;
    }

    public void setLCZS(String LCZS) {
        this.LCZS = LCZS;
    }

    public String getJXLH() {
        return JXLH;
    }

    public void setJXLH(String JXLH) {
        this.JXLH = JXLH;
    }

    public String getJXLM() {
        return JXLM;
    }

    public void setJXLM(String JXLM) {
        this.JXLM = JXLM;
    }

    public int getKJSSL() {
        return KJSSL;
    }

    public void setKJSSL(int KJSSL) {
        this.KJSSL = KJSSL;
    }
}
