package com.toplion.cplusschool.Bean;

import java.io.Serializable;
import java.util.List;

/**
 * Created by wang
 * on 2016/5/26.
 * 成绩总数信息
 */
public class TotalGradeInfoBean implements Serializable{
    private String code;//返回码
    private String msg;//显示信息
    private int pass_count;// 通过的数量，
    private double pass_score;//获得的学分；
    private int nopass_count ;//未通过的数量，
    private double nopass_score ;//未获得的学分
    private List<GradeInfoBean> data;//成绩具体信息

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

    public int getPass_count() {
        return pass_count;
    }

    public void setPass_count(int pass_count) {
        this.pass_count = pass_count;
    }

    public double getPass_score() {
        return pass_score;
    }

    public void setPass_score(int pass_score) {
        this.pass_score = pass_score;
    }

    public int getNopass_count() {
        return nopass_count;
    }

    public void setNopass_count(int nopass_count) {
        this.nopass_count = nopass_count;
    }

    public double getNopass_score() {
        return nopass_score;
    }

    public void setNopass_score(int nopass_score) {
        this.nopass_score = nopass_score;
    }

    public List<GradeInfoBean> getData() {
        return data;
    }

    public void setData(List<GradeInfoBean> data) {
        this.data = data;
    }
}
