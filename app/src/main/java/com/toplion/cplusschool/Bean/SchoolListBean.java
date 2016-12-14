package com.toplion.cplusschool.Bean;

import java.util.List;

/**
 * Created by wang
 * on 2016/9/1.
 */
public class SchoolListBean {
    private String schoolVersion;//版本
    private List<SchoolBean> schoolInfo;//学校列表

    public String getSchoolVersion() {
        return schoolVersion;
    }

    public void setSchoolVersion(String schoolVersion) {
        this.schoolVersion = schoolVersion;
    }

    public List<SchoolBean> getSchoolInfo() {
        return schoolInfo;
    }

    public void setSchoolInfo(List<SchoolBean> schoolInfo) {
        this.schoolInfo = schoolInfo;
    }
}
