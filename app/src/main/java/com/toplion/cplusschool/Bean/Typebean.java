package com.toplion.cplusschool.Bean;

import java.io.Serializable;

/**
 * Created by toplion on 2016/8/3.
 */
public class Typebean implements Serializable {
    private String name;
    private String num;


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNum() {
        return num;
    }

    public void setNum(String num) {
        this.num = num;
    }
}
