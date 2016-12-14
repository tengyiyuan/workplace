package com.toplion.cplusschool.Bean;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by toplion on 2016/10/10.
 */
public class MarketBean implements Serializable {

    private String  AUITITLE;
    private int AUITYPE;
    private String AUICONTENT;
    private int AUICATEGORY;
    private ArrayList<String>IRIURL;
    private ArrayList<String>ID;
    private String  AUIADDRESS;
    private double  AUIPRICE;
    private String AUIID;
    private String AUIRELEASETIME;
    private int AUISTATUS;
    private int CIID;
    private String CINAME;
    private String AUICONTACTNAME;
    private String AUIPHONE;
    private String AUIQQ;
    private String AUIWEIXIN;
    private String UINAME;//工资结算-单位名称
    private int UIID;//单位id
    private String NC;//昵称
    private String TXDZ;//头像

    public String getAUIWEIXIN() {
        return AUIWEIXIN;
    }

    public void setAUIWEIXIN(String AUIWEIXIN) {
        this.AUIWEIXIN = AUIWEIXIN;
    }

    public String getAUIPHONE() {
        return AUIPHONE;
    }

    public void setAUIPHONE(String AUIPHONE) {
        this.AUIPHONE = AUIPHONE;
    }

    public String getAUIQQ() {
        return AUIQQ;
    }

    public void setAUIQQ(String AUIQQ) {
        this.AUIQQ = AUIQQ;
    }

    public ArrayList<String> getID() {
        return ID;
    }

    public void setID(ArrayList<String> ID) {
        this.ID = ID;
    }

    public String getAUITITLE() {
        return AUITITLE;
    }

    public void setAUITITLE(String AUITITLE) {
        this.AUITITLE = AUITITLE;
    }

    public int getAUITYPE() {
        return AUITYPE;
    }

    public void setAUITYPE(int AUITYPE) {
        this.AUITYPE = AUITYPE;
    }

    public String getAUICONTENT() {
        return AUICONTENT;
    }

    public void setAUICONTENT(String AUICONTENT) {
        this.AUICONTENT = AUICONTENT;
    }

    public int getAUICATEGORY() {
        return AUICATEGORY;
    }

    public void setAUICATEGORY(int AUICATEGORY) {
        this.AUICATEGORY = AUICATEGORY;
    }

    public ArrayList<String> getIRIURL() {
        return IRIURL;
    }

    public void setIRIURL(ArrayList<String> IRIURL) {
        this.IRIURL = IRIURL;
    }



    public double getAUIPRICE() {
        return AUIPRICE;
    }

    public void setAUIPRICE(double AUIPRICE) {
        this.AUIPRICE = AUIPRICE;
    }

    public String getAUIID() {
        return AUIID;
    }

    public void setAUIID(String AUIID) {
        this.AUIID = AUIID;
    }

    public String getAUIRELEASETIME() {
        return AUIRELEASETIME;
    }

    public void setAUIRELEASETIME(String AUIRELEASETIME) {
        this.AUIRELEASETIME = AUIRELEASETIME;
    }

    public int getAUISTATUS() {
        return AUISTATUS;
    }

    public void setAUISTATUS(int AUISTATUS) {
        this.AUISTATUS = AUISTATUS;
    }

    public String getCINAME() {
        return CINAME;
    }

    public void setCINAME(String CINAME) {
        this.CINAME = CINAME;
    }

    public String getAUICONTACTNAME() {
        return AUICONTACTNAME;
    }

    public String getAUIADDRESS() {
        return AUIADDRESS;
    }

    public void setAUIADDRESS(String AUIADDRESS) {
        this.AUIADDRESS = AUIADDRESS;
    }

    public void setAUICONTACTNAME(String AUICONTACTNAME) {
        this.AUICONTACTNAME = AUICONTACTNAME;
    }

    public String getUINAME() {
        return UINAME;
    }

    public void setUINAME(String UINAME) {
        this.UINAME = UINAME;
    }

    public int getUIID() {
        return UIID;
    }

    public void setUIID(int UIID) {
        this.UIID = UIID;
    }

    public String getNC() {
        return NC;
    }

    public void setNC(String NC) {
        this.NC = NC;
    }

    public String getTXDZ() {
        return TXDZ;
    }

    public void setTXDZ(String TXDZ) {
        this.TXDZ = TXDZ;
    }

    public int getCIID() {
        return CIID;
    }

    public void setCIID(int CIID) {
        this.CIID = CIID;
    }
}
