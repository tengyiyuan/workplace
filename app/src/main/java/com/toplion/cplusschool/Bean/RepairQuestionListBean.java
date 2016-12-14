package com.toplion.cplusschool.Bean;

import java.util.List;

/**
 * Created by toplion on 2016/5/3.
 * 解析问题列表bean
 */
public class RepairQuestionListBean {
    private String code;//返回码
    private String msg;//显示信息
    private List<RepairQuestionBean> data;//问题列表
    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public List<RepairQuestionBean> getData() {
        return data;
    }

    public void setData(List<RepairQuestionBean> data) {
        this.data = data;
    }
}
