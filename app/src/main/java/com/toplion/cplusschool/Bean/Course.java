package com.toplion.cplusschool.Bean;

import com.ab.db.orm.annotation.Column;
import com.ab.db.orm.annotation.Id;
import com.ab.db.orm.annotation.Table;

import java.io.Serializable;

//学生课程表
@Table(name = "Course")
public class Course implements Serializable {
    // ID @Id主键,int类型,数据库建表时此字段会设为自增长
    @Id
    @Column(name = "_id")
    private int _id;
    // 星期几
    @Column(name = "days")
    private String days;
    // 课程名称
    @Column(name = "name")
    private String name;
    // 上课教室
    @Column(name = "room")
    private String room;
    //上课教室名称
    @Column(name = "roomname")
    private String roomname;
    // 授课老师
    @Column(name = "teach")
    private String teach;
    // 编号
    @Column(name = "bid")
    private String bid;
    // 开始课时
    @Column(name = "start")
    private int start;
    // 几节课
    @Column(name = "step")
    private int step;
    //楼号
    @Column(name = "floorno")
    private String floorno;
    //房号
    @Column(name = "roomno")
    private String roomno;

    //备用
    @Column(name = "byone")
    private String byone;
    //备用
    @Column(name = "bytwo")
    private String bytwo;

    public int get_id() {
        return _id;
    }

    public void set_id(int _id) {
        this._id = _id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getRoom() {
        return room;
    }

    public void setRoom(String room) {
        this.room = room;
    }

    public String getTeach() {
        return teach;
    }

    public void setTeach(String teach) {
        this.teach = teach;
    }


    public int getStart() {
        return start;
    }

    public void setStart(int start) {
        this.start = start;
    }

    public int getStep() {
        return step;
    }

    public void setStep(int step) {
        this.step = step;
    }

    public String getDays() {
        return days;
    }

    public void setDays(String days) {
        this.days = days;
    }

    public String getBid() {
        return bid;
    }

    public void setBid(String bid) {
        this.bid = bid;
    }

    public String getFloorno() {
        return floorno;
    }

    public void setFloorno(String floorno) {
        this.floorno = floorno;
    }

    public String getRoomno() {
        return roomno;
    }

    public void setRoomno(String roomno) {
        this.roomno = roomno;
    }

    public String getByone() {
        return byone;
    }

    public void setByone(String byone) {
        this.byone = byone;
    }

    public String getBytwo() {
        return bytwo;
    }

    public void setBytwo(String bytwo) {
        this.bytwo = bytwo;
    }

    public String getRoomname() {
        return roomname;
    }

    public void setRoomname(String roomname) {
        this.roomname = roomname;
    }



}
