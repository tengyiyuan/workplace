package com.toplion.cplusschool.Bean;

import com.ab.db.orm.annotation.Column;
import com.ab.db.orm.annotation.Table;

import java.io.Serializable;

/**
 * Created by wang
 * on 2016/8/15.
 * @通讯录详细信息
 */
@Table(name = "ContactsBean")
public class ContactsBean implements Serializable{
    /**
     *    "XH": "20140415002",
     "SJH": "17865167107",
     "JTDZ": "山东省莱州市朱桥镇桑家村152 号",
     "LXDH": "17865167107",
     "XM": "桑雯倩",
     "JTDH": "0535-2557855"
     */
    @Column(name = "PinYin")
    private String PinYin;

    @Column(name = "FirstPinYin")
    private String FirstPinYin;

    @Column(name = "PinYinHeadChar")
    private String PinYinHeadChar;//每个汉字的首字母

    @Column(name = "XH")
    private String XH;//学号

    @Column(name = "SJH")
    private String SJH;//手机号

    @Column(name = "JTDZ")
    private String JTDZ;//家庭住址

    @Column(name = "LXDH")
    private String LXDH;//联系电

    @Column(name = "XM")
    private String XM;//姓名

    @Column(name = "XMPY")
    private String XMPY;//姓名拼音

    @Column(name = "JTDH")
    private String JTDH;//家庭电话

    public String getXH() {
        return XH;
    }

    public void setXH(String XH) {
        this.XH = XH;
    }

    public String getSJH() {
        return SJH;
    }

    public void setSJH(String SJH) {
        this.SJH = SJH;
    }

    public String getJTDZ() {
        return JTDZ;
    }

    public void setJTDZ(String JTDZ) {
        this.JTDZ = JTDZ;
    }

    public String getLXDH() {
        return LXDH;
    }

    public void setLXDH(String LXDH) {
        this.LXDH = LXDH;
    }

    public String getXM() {
        return XM;
    }

    public void setXM(String XM) {
        this.XM = XM;
    }

    public String getJTDH() {
        return JTDH;
    }

    public void setJTDH(String JTDH) {
        this.JTDH = JTDH;
    }

    public String getXMPY() {
        return XMPY;
    }

    public void setXMPY(String XMPY) {
        this.XMPY = XMPY;
    }

    public String getPinYin() {
        return PinYin;
    }

    public void setPinYin(String pinYin) {
        PinYin = pinYin;
    }

    public String getFirstPinYin() {
        return FirstPinYin;
    }

    public void setFirstPinYin(String firstPinYin) {
        FirstPinYin = firstPinYin;
    }

    public String getPinYinHeadChar() {
        return PinYinHeadChar;
    }

    public void setPinYinHeadChar(String pinYinHeadChar) {
        PinYinHeadChar = pinYinHeadChar;
    }
}
