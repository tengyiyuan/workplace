package com.toplion.cplusschool.Bean;

import java.io.Serializable;

/**
 * Created by wangshengbo
 * on 2016/6/30.
 * @des 报修问题bean
 */
public class RepairQuestionBean implements Serializable{
    /**
     CF_ID：ID标识
     CF_TITLE：标题
     CF_DESCRIPTION：描述

     */
    private String CF_DESCRIPTION;//描述
    private int CF_ID;//ID标识
    private String CF_TITLE;//标题

    public String getCF_DESCRIPTION() {
        return CF_DESCRIPTION;
    }

    public void setCF_DESCRIPTION(String CF_DESCRIPTION) {
        this.CF_DESCRIPTION = CF_DESCRIPTION;
    }

    public int getCF_ID() {
        return CF_ID;
    }

    public void setCF_ID(int CF_ID) {
        this.CF_ID = CF_ID;
    }


    public String getCF_TITLE() {
        return CF_TITLE;
    }

    public void setCF_TITLE(String CF_TITLE) {
        this.CF_TITLE = CF_TITLE;
    }

}
