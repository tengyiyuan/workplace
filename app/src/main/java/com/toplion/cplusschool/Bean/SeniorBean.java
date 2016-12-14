package com.toplion.cplusschool.Bean;

import java.util.List;

/**
 * Created by wang
 * on 2016/9/8.
 * 教职工通讯录部门bean
 */
public class SeniorBean {
    private int seniorid;
    private String seniorname;
    private List<JobBean> departments;

    public int getSeniorid() {
        return seniorid;
    }

    public void setSeniorid(int seniorid) {
        this.seniorid = seniorid;
    }

    public String getSeniorname() {
        return seniorname;
    }

    public void setSeniorname(String seniorname) {
        this.seniorname = seniorname;
    }

    public List<JobBean> getDepartments() {
        return departments;
    }

    public void setDepartments(List<JobBean> departments) {
        this.departments = departments;
    }
}
