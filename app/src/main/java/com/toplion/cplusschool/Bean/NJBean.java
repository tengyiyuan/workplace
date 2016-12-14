package com.toplion.cplusschool.Bean;

import java.util.List;

/**
 * Created by wang
 * on 2016/8/15.
 * @年级
 */
public class NJBean {
    private int nj;//年级
    private List<MajorBean> major;//年级

    public int getNj() {
        return nj;
    }

    public void setNj(int nj) {
        this.nj = nj;
    }

    public List<MajorBean> getMajor() {
        return major;
    }

    public void setMajor(List<MajorBean> major) {
        this.major = major;
    }
}
