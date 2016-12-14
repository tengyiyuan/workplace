package com.toplion.cplusschool.Bean;

/**
 * Created by toplion on 2016/4/28.
 */
public class PhoneInfo {
    private String name;
    private String number;
    public PhoneInfo(String name, String number) {
        this.name = name;
        this.number = number;
    }
    public String getName() {
        return name;
    }
    public String getNumber() {
        return number;
    }
}