package com.toplion.cplusschool.Bean;

import java.io.Serializable;

/**
 * Created by wang
 * on 2016/9/8.
 * 周会议bean
 */
public class WeekMeetingBean implements Serializable{
    /**
     *    "logId": "14",
     "weekId": "3",
     "logDate": "2016-08-31",
     "logWeek": " 三",
     "logTime": "09:00",
     "logAddress": "第二会议室（BG 319）",
     "logName": "校长办公会",
     "logHost": "靳奉祥",
     "logCompany": "校长办公 室",
     "logJoined": "另行通知"
     */

    private String logId;// ID
    private String weekId ;//reportId 关联ID
    private String logDate;//日期
    private String logWeek;// 星期
    private String logTime;//时间
    private String logAddress;//地点
    private String logName;// 会议名称
    private String logHost;// 主持人
    private String logCompany;// 承办单位
    private String logJoined;//参与人
    private boolean isRemind;//是否提醒

    public String getLogId() {
        return logId;
    }

    public void setLogId(String logId) {
        this.logId = logId;
    }

    public String getWeekId() {
        return weekId;
    }

    public void setWeekId(String weekId) {
        this.weekId = weekId;
    }

    public String getLogDate() {
        return logDate;
    }

    public void setLogDate(String logDate) {
        this.logDate = logDate;
    }

    public String getLogWeek() {
        return logWeek;
    }

    public void setLogWeek(String logWeek) {
        this.logWeek = logWeek;
    }

    public String getLogTime() {
        return logTime;
    }

    public void setLogTime(String logTime) {
        this.logTime = logTime;
    }

    public String getLogAddress() {
        return logAddress;
    }

    public void setLogAddress(String logAddress) {
        this.logAddress = logAddress;
    }

    public String getLogName() {
        return logName;
    }

    public void setLogName(String logName) {
        this.logName = logName;
    }

    public String getLogHost() {
        return logHost;
    }

    public void setLogHost(String logHost) {
        this.logHost = logHost;
    }

    public String getLogCompany() {
        return logCompany;
    }

    public void setLogCompany(String logCompany) {
        this.logCompany = logCompany;
    }

    public String getLogJoined() {
        return logJoined;
    }

    public void setLogJoined(String logJoined) {
        this.logJoined = logJoined;
    }

    public boolean isRemind() {
        return isRemind;
    }

    public void setRemind(boolean remind) {
        isRemind = remind;
    }
}
