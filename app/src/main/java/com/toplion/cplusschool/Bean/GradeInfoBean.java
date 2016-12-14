package com.toplion.cplusschool.Bean;

import java.io.Serializable;

/**
 * Created by wang
 * on 2016/5/26.
 * 成绩详细信息bean
 */
public class GradeInfoBean implements Serializable{
    /**
     *  "ZCJ": 71,
     "SFCYXFJJS": "1",
     "KCM": "光电子技术",
     "KCXZMC": "选修",
     "SFYX": "1",
     "SFJG": "1",
     "XF": 2

     "PJZT": 0,
     "PJKG": 1,
     "CKZT": 0,
     */
    private double ZCJ;//总成绩
    private String SFCYXFJJS;//是否参与学分计算
    private String KCM;//课程名
    private String KCXZMC;//课程性质代码
    private String SFYX;//是否有效
    private String SFJG;//是否及格
    private double XF;//学分

    private int  PJZT;//评教状态（ 0表示未评教1表示已评教）
    private int PJKG;//评教开关（ 1表示未开始评教，0开始评教）
    private int CKZT;//查看状态（ 0时表示可以查看成绩1表示不可以）

    public double getZCJ() {
        return ZCJ;
    }

    public void setZCJ(double ZCJ) {
        this.ZCJ = ZCJ;
    }

    public String getSFCYXFJJS() {
        return SFCYXFJJS;
    }

    public void setSFCYXFJJS(String SFCYXFJJS) {
        this.SFCYXFJJS = SFCYXFJJS;
    }

    public String getKCM() {
        return KCM;
    }

    public void setKCM(String KCM) {
        this.KCM = KCM;
    }

    public String getKCXZMC() {
        return KCXZMC;
    }

    public void setKCXZMC(String KCXZMC) {
        this.KCXZMC = KCXZMC;
    }

    public String getSFYX() {
        return SFYX;
    }

    public void setSFYX(String SFYX) {
        this.SFYX = SFYX;
    }

    public String getSFJG() {
        return SFJG;
    }

    public void setSFJG(String SFJG) {
        this.SFJG = SFJG;
    }

    public double getXF() {
        return XF;
    }

    public void setXF(double XF) {
        this.XF = XF;
    }

    public int getPJZT() {
        return PJZT;
    }

    public void setPJZT(int PJZT) {
        this.PJZT = PJZT;
    }

    public int getPJKG() {
        return PJKG;
    }

    public void setPJKG(int PJKG) {
        this.PJKG = PJKG;
    }

    public int getCKZT() {
        return CKZT;
    }

    public void setCKZT(int CKZT) {
        this.CKZT = CKZT;
    }
}
