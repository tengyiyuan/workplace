package com.toplion.cplusschool.Bean;

/**
 * @des 考试列表bean
 * Created by wangshengbo
 * on 2016/6/12.
 */
public class TestListBean extends BaseListBean<TestBean>{
    private String currenttime;//当前系统时间

    public String getCurrenttime() {
        return currenttime;
    }

    public void setCurrenttime(String currenttime) {
        this.currenttime = currenttime;
    }
}
