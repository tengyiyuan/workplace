package com.toplion.cplusschool.Bean;

import com.ab.db.orm.annotation.Column;
import com.ab.db.orm.annotation.Table;

import java.io.Serializable;

/**
 * Created by wang
 * on 2016/9/1.
 * @school  Bean
 */
@Table(name = "SchoolBean")
public class SchoolBean implements Serializable{
    /**
     *  "schoolId": "1000",
     "schoolName": "山东建筑大学",
     "schoolDes": "山东建筑大学",
     "schoolAddress": "临港区凤鸣路",
     "schoolState": "1",
     "serverIpAddress": "123.233.121.17:12100",
     "wifiName": "SDJZU-2.4G,SDJZU-5.8G",
     "schoolCode": "sdjzu",
     "schoolOprTime": null
     */
    @Column(name = "schoolId")
    private String schoolId;//
    @Column(name = "schoolName")
    private String schoolName;//
    @Column(name = "schoolDes")
    private String schoolDes;//
    @Column(name = "schoolAddress")
    private String schoolAddress;//
    @Column(name = "schoolState")
    private String schoolState;//
    @Column(name = "ltServerIpAddress")
    private String ltServerIpAddress;//
    @Column(name = "serverIpAddress")
    private String serverIpAddress;//
    @Column(name = "wifiName")
    private String wifiName;//
    @Column(name = "schoolCode")
    private String schoolCode;//
    private String schoolOprTime;//

    public String getSchoolId() {
        return schoolId;
    }

    public void setSchoolId(String schoolId) {
        this.schoolId = schoolId;
    }

    public String getSchoolName() {
        return schoolName;
    }

    public void setSchoolName(String schoolName) {
        this.schoolName = schoolName;
    }

    public String getSchoolDes() {
        return schoolDes;
    }

    public void setSchoolDes(String schoolDes) {
        this.schoolDes = schoolDes;
    }

    public String getSchoolAddress() {
        return schoolAddress;
    }

    public void setSchoolAddress(String schoolAddress) {
        this.schoolAddress = schoolAddress;
    }

    public String getSchoolState() {
        return schoolState;
    }

    public void setSchoolState(String schoolState) {
        this.schoolState = schoolState;
    }

    public String getServerIpAddress() {
        return serverIpAddress;
    }

    public void setServerIpAddress(String serverIpAddress) {
        this.serverIpAddress = serverIpAddress;
    }

    public String getWifiName() {
        return wifiName;
    }

    public void setWifiName(String wifiName) {
        this.wifiName = wifiName;
    }

    public String getSchoolCode() {
        return schoolCode;
    }

    public void setSchoolCode(String schoolCode) {
        this.schoolCode = schoolCode;
    }

    public String getSchoolOprTime() {
        return schoolOprTime;
    }

    public void setSchoolOprTime(String schoolOprTime) {
        this.schoolOprTime = schoolOprTime;
    }

    public String getLtServerIpAddress() {
        return ltServerIpAddress;
    }

    public void setLtServerIpAddress(String ltServerIpAddress) {
        this.ltServerIpAddress = ltServerIpAddress;
    }
}
