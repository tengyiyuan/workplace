package com.toplion.cplusschool.Bean;

import com.ab.db.orm.annotation.Column;
import com.ab.db.orm.annotation.Id;
import com.ab.db.orm.annotation.Table;

import java.io.Serializable;

/**
 * Created by wang
 * on 2016/7/11.
 *
 * @des 部门bean
 */
@Table(name = "Department")
public class DepartmentBean implements Serializable{
    @Id
    @Column(name = "DD_ID")
    private int DD_ID;//部门ID

    @Column(name = "T_COUNT")
    private int T_COUNT;//部门下职位数量

    @Column(name = "DD_NAME")
    private String DD_NAME;//部门名称

    @Column(name = "DD_NAMEPY")
    private String DD_NAMEPY;//部门名称拼音

    @Column(name = "DD_NAME_PYHeadChar")
    private String DD_NAME_PYHeadChar;//每个汉字的首字母

    @Column(name = "DP_PHONE")
    private String DP_PHONE;//部门电话

    @Column(name = "DP_ID")
    private int DP_ID;//电话id

    @Column(name = "FATHER_NAME")
    private String FATHER_NAME;

    @Column(name = "FATHER_NAMEPY")
    private String FATHER_NAMEPY;

    public int getDD_ID() {
        return DD_ID;
    }

    public void setDD_ID(int DD_ID) {
        this.DD_ID = DD_ID;
    }

    public int getT_COUNT() {
        return T_COUNT;
    }

    public void setT_COUNT(int t_COUNT) {
        T_COUNT = t_COUNT;
    }

    public String getDD_NAME() {
        return DD_NAME;
    }

    public void setDD_NAME(String DD_NAME) {
        this.DD_NAME = DD_NAME;
    }

    public String getDP_PHONE() {
        return DP_PHONE;
    }

    public void setDP_PHONE(String DP_PHONE) {
        this.DP_PHONE = DP_PHONE;
    }

    public int getDP_ID() {
        return DP_ID;
    }

    public void setDP_ID(int DP_ID) {
        this.DP_ID = DP_ID;
    }

    public String getFATHER_NAME() {
        return FATHER_NAME;
    }

    public void setFATHER_NAME(String FATHER_NAME) {
        this.FATHER_NAME = FATHER_NAME;
    }

    public String getDD_NAMEPY() {
        return DD_NAMEPY;
    }

    public void setDD_NAMEPY(String DD_NAMEPY) {
        this.DD_NAMEPY = DD_NAMEPY;
    }

    public String getFATHER_NAMEPY() {
        return FATHER_NAMEPY;
    }

    public void setFATHER_NAMEPY(String FATHER_NAMEPY) {
        this.FATHER_NAMEPY = FATHER_NAMEPY;
    }

    public String getDD_NAME_PYHeadChar() {
        return DD_NAME_PYHeadChar;
    }

    public void setDD_NAME_PYHeadChar(String DD_NAME_PYHeadChar) {
        this.DD_NAME_PYHeadChar = DD_NAME_PYHeadChar;
    }
}
