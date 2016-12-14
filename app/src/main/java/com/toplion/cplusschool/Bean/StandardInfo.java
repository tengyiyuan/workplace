package com.toplion.cplusschool.Bean;

import java.io.Serializable;

/**
 * Created by toplion on 2016/8/4.
 */
public class StandardInfo implements Serializable {
    private String FNAME;
    private int RRNUMBER;
    private int RTID;
    private String RTNAME;
    private int RTPROCESSINGTIME;

    public int getRRNUMBER() {
        return RRNUMBER;
    }

    public void setRRNUMBER(int RRNUMBER) {
        this.RRNUMBER = RRNUMBER;
    }

    public int getRTID() {
        return RTID;
    }

    public void setRTID(int RTID) {
        this.RTID = RTID;
    }

    public String getRTNAME() {
        return RTNAME;
    }

    public void setRTNAME(String RTNAME) {
        this.RTNAME = RTNAME;
    }

    public int getRTPROCESSINGTIME() {
        return RTPROCESSINGTIME;
    }

    public void setRTPROCESSINGTIME(int RTPROCESSINGTIME) {
        this.RTPROCESSINGTIME = RTPROCESSINGTIME;
    }

    public String getFNAME() {
        return FNAME;
    }

    public void setFNAME(String FNAME) {
        this.FNAME = FNAME;
    }
}
