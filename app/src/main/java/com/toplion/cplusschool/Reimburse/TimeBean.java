package com.toplion.cplusschool.Reimburse;

import java.io.Serializable;

/**
 * Created by toplion
 * on 2016/9/18.
 * 时间段bean
 */
public class TimeBean implements Serializable{
    private String timeStr;//时间
    private boolean isChecked;//是否选中

    public String getTimeStr() {
        return timeStr;
    }

    public void setTimeStr(String timeStr) {
        this.timeStr = timeStr;
    }

    public boolean isChecked() {
        return isChecked;
    }

    public void setChecked(boolean checked) {
        isChecked = checked;
    }
}
