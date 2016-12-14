package com.toplion.cplusschool.Bean;

import java.io.Serializable;

/**
 * Created by toplion on 2016/8/15.
 */
public class Menus implements Serializable {
    private String AI_NAME;
    private String NEWICON;
    private int AI_ID;
    private String AI_DESCRIBE;
    private String SDS_CODE;
    private String NEWNAME;
    private String AG_NAME;
    private String AI_DEFAULTICON;
    private int AG_ID;
    private boolean RSA_ENABLE;

    public String getAI_NAME() {
        return AI_NAME;
    }

    public void setAI_NAME(String AI_NAME) {
        this.AI_NAME = AI_NAME;
    }

    public String getNEWICON() {
        return NEWICON;
    }

    public void setNEWICON(String NEWICON) {
        this.NEWICON = NEWICON;
    }

    public int getAI_ID() {
        return AI_ID;
    }

    public void setAI_ID(int AI_ID) {
        this.AI_ID = AI_ID;
    }

    public String getAI_DESCRIBE() {
        return AI_DESCRIBE;
    }

    public void setAI_DESCRIBE(String AI_DESCRIBE) {
        this.AI_DESCRIBE = AI_DESCRIBE;
    }

    public String getSDS_CODE() {
        return SDS_CODE;
    }

    public void setSDS_CODE(String SDS_CODE) {
        this.SDS_CODE = SDS_CODE;
    }

    public String getNEWNAME() {
        return NEWNAME;
    }

    public void setNEWNAME(String NEWNAME) {
        this.NEWNAME = NEWNAME;
    }

    public String getAG_NAME() {
        return AG_NAME;
    }

    public void setAG_NAME(String AG_NAME) {
        this.AG_NAME = AG_NAME;
    }

    public String getAI_DEFAULTICON() {
        return AI_DEFAULTICON;
    }

    public void setAI_DEFAULTICON(String AI_DEFAULTICON) {
        this.AI_DEFAULTICON = AI_DEFAULTICON;
    }

    public int getAG_ID() {
        return AG_ID;
    }

    public void setAG_ID(int AG_ID) {
        this.AG_ID = AG_ID;
    }

    public boolean isRSA_ENABLE() {
        return RSA_ENABLE;
    }

    public void setRSA_ENABLE(boolean RSA_ENABLE) {
        this.RSA_ENABLE = RSA_ENABLE;
    }
}
