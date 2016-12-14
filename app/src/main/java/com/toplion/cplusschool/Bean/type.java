package com.toplion.cplusschool.Bean;

import java.io.Serializable;


/**
 * Created by toplion on 2016/8/1.
 */
public class type implements Serializable {
    private int  RTTYPE;// 是否根节点 1 true, 0 false
    private int RTID;// 类型编号
    private int RTPROCESSINGTIME;// 完成需要分钟数
    private String RTNAME;// 类型名称
    private String CHILDREN;


    public int getRTTYPE() {
        return RTTYPE;
    }

    public void setRTTYPE(int RTTYPE) {
        this.RTTYPE = RTTYPE;
    }

    public int getRTID() {
        return RTID;
    }

    public void setRTID(int RTID) {
        this.RTID = RTID;
    }

    public int getRTPROCESSINGTIME() {
        return RTPROCESSINGTIME;
    }

    public void setRTPROCESSINGTIME(int RTPROCESSINGTIME) {
        this.RTPROCESSINGTIME = RTPROCESSINGTIME;
    }

    public String getRTNAME() {
        return RTNAME;
    }

    public void setRTNAME(String RTNAME) {
        this.RTNAME = RTNAME;
    }

    public String getCHILDREN() {
        return CHILDREN;
    }

    public void setCHILDREN(String CHILDREN) {
        this.CHILDREN = CHILDREN;
    }
}
