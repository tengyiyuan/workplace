package com.toplion.cplusschool.Bean;

import java.io.Serializable;

/**
 * Created by wang
 * on 2016/11/26.
 * 照片评论bean
 */

public class PingLunBean implements Serializable{
    /**
     * pwe_id 评论编号
     pwe_content 评论内容
     nc  评论人昵称
     txdz 评论人头像地址
     pwe_createtime 评论时间
     */
    private int pwe_id;
    private String pwe_content;
    private String nc;
    private String txdz;
    private String pwe_createtime;

    public int getPwe_id() {
        return pwe_id;
    }

    public void setPwe_id(int pwe_id) {
        this.pwe_id = pwe_id;
    }

    public String getPwe_content() {
        return pwe_content;
    }

    public void setPwe_content(String pwe_content) {
        this.pwe_content = pwe_content;
    }

    public String getNc() {
        return nc;
    }

    public void setNc(String nc) {
        this.nc = nc;
    }

    public String getTxdz() {
        return txdz;
    }

    public void setTxdz(String txdz) {
        this.txdz = txdz;
    }

    public String getPwe_createtime() {
        return pwe_createtime;
    }

    public void setPwe_createtime(String pwe_createtime) {
        this.pwe_createtime = pwe_createtime;
    }
}
