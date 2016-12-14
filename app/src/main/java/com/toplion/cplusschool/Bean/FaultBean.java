package com.toplion.cplusschool.Bean;

import java.io.Serializable;

/**
 * Created by wangshengbo
 * on 2016/6/30.
 * @des 故障现象bean
 */
public class FaultBean implements Serializable{
    private int DX_ID;//id
    private String DX_DETAIL;//描述
    private String DX_TITLE;//标题

    public int getDX_ID() {
        return DX_ID;
    }

    public void setDX_ID(int DX_ID) {
        this.DX_ID = DX_ID;
    }

    public String getDX_DETAIL() {
        return DX_DETAIL;
    }

    public void setDX_DETAIL(String DX_DETAIL) {
        this.DX_DETAIL = DX_DETAIL;
    }

    public String getDX_TITLE() {
        return DX_TITLE;
    }

    public void setDX_TITLE(String DX_TITLE) {
        this.DX_TITLE = DX_TITLE;
    }
}
