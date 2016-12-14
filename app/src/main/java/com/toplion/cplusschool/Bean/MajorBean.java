package com.toplion.cplusschool.Bean;

import java.util.List;

/**
 * Created by wang
 * on 2016/8/15.
 * @专业
 */
public class MajorBean {
    private String zydm;//专业代码
    private String zymc;//专业名称
    private List<BanJiBean> classes;//班级列表

    public String getZydm() {
        return zydm;
    }

    public void setZydm(String zydm) {
        this.zydm = zydm;
    }

    public String getZymc() {
        return zymc;
    }

    public void setZymc(String zymc) {
        this.zymc = zymc;
    }

    public List<BanJiBean> getClasses() {
        return classes;
    }

    public void setClasses(List<BanJiBean> classes) {
        this.classes = classes;
    }
}
