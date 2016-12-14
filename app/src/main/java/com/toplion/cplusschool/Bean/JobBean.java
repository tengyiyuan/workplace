package com.toplion.cplusschool.Bean;

import java.util.List;

/**
 * Created by wang
 * on 2016/9/8.
 * 职位bean
 */
public class JobBean {
    private int departid;
    private String departname;
    private List<PersonBean> personname;

    public int getDepartid() {
        return departid;
    }

    public void setDepartid(int departid) {
        this.departid = departid;
    }

    public String getDepartname() {
        return departname;
    }

    public void setDepartname(String departname) {
        this.departname = departname;
    }

    public List<PersonBean> getPersonname() {
        return personname;
    }

    public void setPersonname(List<PersonBean> personname) {
        this.personname = personname;
    }
}
