package com.toplion.cplusschool.Bean;

import java.io.Serializable;

/**
 * Created by wang学期
 * on 2016/5/26.
 * 学期bean
 */
public class TermBean implements Serializable{
    private String XNXQDM;//学期编号
    private String XNXQMC;//

    public String getXNXQDM() {
        return XNXQDM;
    }

    public void setXNXQDM(String XNXQDM) {
        this.XNXQDM = XNXQDM;
    }

    public String getXNXQMC() {
        return XNXQMC;
    }

    public void setXNXQMC(String XNXQMC) {
        this.XNXQMC = XNXQMC;
    }
}
