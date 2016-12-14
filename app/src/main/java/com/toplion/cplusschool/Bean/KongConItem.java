package com.toplion.cplusschool.Bean;

import java.io.Serializable;

/**
 * Created by toplion on 2016/9/13.
 */
public class KongConItem  implements Serializable {

    private String yuefen;
    private String jihua;
    private String jindu;
    private String baifenbi;

    public String getYuefen() {
        return yuefen;
    }

    public void setYuefen(String yuefen) {
        this.yuefen = yuefen;
    }

    public String getBaifenbi() {
        return baifenbi;
    }

    public void setBaifenbi(String baifenbi) {
        this.baifenbi = baifenbi;
    }

    public String getJindu() {
        return jindu;
    }

    public void setJindu(String jindu) {
        this.jindu = jindu;
    }

    public String getJihua() {
        return jihua;
    }

    public void setJihua(String jihua) {
        this.jihua = jihua;
    }
}
