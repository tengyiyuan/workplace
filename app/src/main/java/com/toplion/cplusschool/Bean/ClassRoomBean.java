package com.toplion.cplusschool.Bean;

/**
 * Created by wagnshengbo
 * on 2016/6/13.
 * @Des 空教室bean
 */
public class ClassRoomBean {
    /**
     *"LC": 1,
     "JC": 1,
     "JASH": "BW109"
     */
    private int LC;//所属楼层
    private int JC;//所属节次
    private String JASH;//房间号
    private String JSMC;//教室名称

    public int getLC() {
        return LC;
    }

    public void setLC(int LC) {
        this.LC = LC;
    }

    public int getJC() {
        return JC;
    }

    public void setJC(int JC) {
        this.JC = JC;
    }

    public String getJASH() {
        return JASH;
    }

    public void setJASH(String JASH) {
        this.JASH = JASH;
    }

    public String getJSMC() {
        return JSMC;
    }

    public void setJSMC(String JSMC) {
        this.JSMC = JSMC;
    }
}
