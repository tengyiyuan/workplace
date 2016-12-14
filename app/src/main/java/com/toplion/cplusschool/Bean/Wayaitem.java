package com.toplion.cplusschool.Bean;

import java.io.Serializable;

/**
 * Created by toplion on 2016/8/10.
 */
public class Wayaitem implements Serializable {
    private String name;
    private String value;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
