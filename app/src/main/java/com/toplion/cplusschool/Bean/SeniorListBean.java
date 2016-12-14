package com.toplion.cplusschool.Bean;

import java.util.List;

/**
 * Created by wang
 * on 2016/9/8.
 * 部门listbean
 */
public class SeniorListBean {
    private String code;
    private String msg;
    private List<SeniorBean> data;

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

    public List<SeniorBean> getData() {
        return data;
    }

    public void setData(List<SeniorBean> data) {
        this.data = data;
    }
}
