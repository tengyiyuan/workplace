package com.toplion.cplusschool.Bean;

/**
 * @des 考试bean
 * Created by wang
 * on 2016/6/12.
 */
public class TestBean {
    /**
     * "KCM": "马克思主义基本原 理",
     "KSRQ": "Jan 11, 2016",
     "XSYXMC": "土木工程学院",
     "XM": "刘延东",
     "KSSJMS": "2016-01-11 13:10-15:10",
     "ZWH": "49",
     "JSMC": "博文馆105[媒159]",
     "JXLM": "博文馆",
     "KSMC": "期末考 试",
     "XF": 3
     */
    private String KCM;//课程名
    private String KSRQ;//考试日期
    private String XSYXMC;//学生院系名称
    private String XM;//姓名
    private String KSSJMS;//考试时间描述
    private String ZWH;//座位号
    private String JSMC;//教室名称
    private String JXLM;//教学楼名
    private String KSMC;//考试名称
    private double XF;//学分
    public String getKCM() {
        return KCM;
    }

    public void setKCM(String KCM) {
        this.KCM = KCM;
    }

    public String getKSRQ() {
        return KSRQ;
    }

    public void setKSRQ(String KSRQ) {
        this.KSRQ = KSRQ;
    }

    public String getXSYXMC() {
        return XSYXMC;
    }

    public void setXSYXMC(String XSYXMC) {
        this.XSYXMC = XSYXMC;
    }

    public String getXM() {
        return XM;
    }

    public void setXM(String XM) {
        this.XM = XM;
    }

    public String getKSSJMS() {
        return KSSJMS;
    }

    public void setKSSJMS(String KSSJMS) {
        this.KSSJMS = KSSJMS;
    }

    public String getZWH() {
        return ZWH;
    }

    public void setZWH(String ZWH) {
        this.ZWH = ZWH;
    }

    public String getJSMC() {
        return JSMC;
    }

    public void setJSMC(String JSMC) {
        this.JSMC = JSMC;
    }

    public String getJXLM() {
        return JXLM;
    }

    public void setJXLM(String JXLM) {
        this.JXLM = JXLM;
    }

    public String getKSMC() {
        return KSMC;
    }

    public void setKSMC(String KSMC) {
        this.KSMC = KSMC;
    }

    public double getXF() {
        return XF;
    }

    public void setXF(double XF) {
        this.XF = XF;
    }


}
