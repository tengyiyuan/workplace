package com.toplion.cplusschool.Bean;

import java.io.Serializable;

/**
 * Created by toplion on 2016/7/4.
 */
public class ZhuanDanBean implements Serializable {
    /**
     * "TOIREASON": "4sdf44",
     * "ZDNAME": "user123",
     * "TOICTIME": "Jun 30, 2016 8:23:11 AM",
     * "JDNAME": "user123"
     */
    private String TOIREASON;// 转单原因
    private String ZDNAME;// 转单人
    private String TOICTIME;//转单时间
    private String JDNAME;//接单人

    public String getTOIREASON() {
        return TOIREASON;
    }

    public void setTOIREASON(String TOIREASON) {
        this.TOIREASON = TOIREASON;
    }

    public String getZDNAME() {
        return ZDNAME;
    }

    public void setZDNAME(String ZDNAME) {
        this.ZDNAME = ZDNAME;
    }

    public String getTOICTIME() {
        return TOICTIME;
    }

    public void setTOICTIME(String TOICTIME) {
        this.TOICTIME = TOICTIME;
    }

    public String getJDNAME() {
        return JDNAME;
    }

    public void setJDNAME(String JDNAME) {
        this.JDNAME = JDNAME;
    }
}
