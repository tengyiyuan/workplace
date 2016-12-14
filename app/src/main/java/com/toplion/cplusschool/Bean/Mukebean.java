package com.toplion.cplusschool.Bean;

import java.io.Serializable;

/**
 * Created by toplion on 2016/11/4.
 */
public class Mukebean implements Serializable {
    private String ctImgUrl;
    private String name;
    private String ctUrl;
    private long ctStartTime;
    private long ctEndTime;
    private int enrollCount;

    public String getCtImgUrl() {
        return ctImgUrl;
    }

    public void setCtImgUrl(String ctImgUrl) {
        this.ctImgUrl = ctImgUrl;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCtUrl() {
        return ctUrl;
    }

    public void setCtUrl(String ctUrl) {
        this.ctUrl = ctUrl;
    }

    public long getCtStartTime() {
        return ctStartTime;
    }

    public void setCtStartTime(long ctStartTime) {
        this.ctStartTime = ctStartTime;
    }

    public long getCtEndTime() {
        return ctEndTime;
    }

    public void setCtEndTime(long ctEndTime) {
        this.ctEndTime = ctEndTime;
    }

    public int getEnrollCount() {
        return enrollCount;
    }

    public void setEnrollCount(int enrollCount) {
        this.enrollCount = enrollCount;
    }
}
