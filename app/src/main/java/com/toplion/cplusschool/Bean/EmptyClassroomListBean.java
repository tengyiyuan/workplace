package com.toplion.cplusschool.Bean;

import java.io.Serializable;
import java.util.List;

/**
 * Created by wangshengob
 * on 2016/6/13.
 * @des 空教室列表和当前楼层所有教室bean
 */
public class EmptyClassroomListBean implements Serializable{
    private String code;
    private String msg;//
    private String currenttime;
    private String currentSection;
    private List<ClassRoomBean> emptyClassroom;//当前被占用的教室
    private List<ClassRoomBean> allClassroom;//当前楼层所有教室

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getCurrenttime() {
        return currenttime;
    }

    public void setCurrenttime(String currenttime) {
        this.currenttime = currenttime;
    }

    public String getCurrentSection() {
        return currentSection;
    }

    public void setCurrentSection(String currentSection) {
        this.currentSection = currentSection;
    }

    public List<ClassRoomBean> getEmptyClassroom() {
        return emptyClassroom;
    }

    public void setEmptyClassroom(List<ClassRoomBean> emptyClassroom) {
        this.emptyClassroom = emptyClassroom;
    }

    public List<ClassRoomBean> getAllClassroom() {
        return allClassroom;
    }

    public void setAllClassroom(List<ClassRoomBean> allClassroom) {
        this.allClassroom = allClassroom;
    }
}
