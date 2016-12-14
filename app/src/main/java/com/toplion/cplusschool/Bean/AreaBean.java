package com.toplion.cplusschool.Bean;

import java.io.Serializable;

/**
 * Created by wangshengbo
 * on 2016/6/30.
 * @des区域bean
 */
public class AreaBean implements Serializable {
    /**
     *  "CA_ID： 标识Id
     DPC_NAME:创建人
     */
    private int CA_ID;//ID
    private String CA_NAME;//区域名称

    public int getCA_ID() {
        return CA_ID;
    }

    public void setCA_ID(int CA_ID) {
        this.CA_ID = CA_ID;
    }

    public String getCA_NAME() {
        return CA_NAME;
    }

    public void setCA_NAME(String CA_NAME) {
        this.CA_NAME = CA_NAME;
    }
}
