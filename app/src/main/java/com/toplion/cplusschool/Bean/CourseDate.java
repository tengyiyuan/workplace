package com.toplion.cplusschool.Bean;

import com.ab.db.orm.annotation.Column;
import com.ab.db.orm.annotation.Id;
import com.ab.db.orm.annotation.Table;

import java.io.Serializable;

//上课时间表
@Table(name = "CourseDate")
public class CourseDate implements Serializable {
    // ID @Id主键,int类型,数据库建表时此字段会设为自增长
    @Id
    @Column(name = "_id")
    private int _id;
    // 结束时间
    @Column(name = "jssj")
    private String jssj;

    @Column(name = "wid")
    private String wid;

    @Column(name = "jcdm")
    private String jcdm;

    @Column(name = "bz")
    private String bz;
    // 开始时间
    @Column(name = "kssj")
    private String kssj;
    // 节次名称
    @Column(name = "jcmc")
    private String jcmc;
    @Column(name = "jclb")
    private String jclb;
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

    public String getBytwo() {
        return bytwo;
    }

    public void setBytwo(String bytwo) {
        this.bytwo = bytwo;
    }

    public String getJssj() {
        return jssj;
    }

    public void setJssj(String jssj) {
        this.jssj = jssj;
    }

    public String getWid() {
        return wid;
    }

    public void setWid(String wid) {
        this.wid = wid;
    }

    public String getJcdm() {
        return jcdm;
    }

    public void setJcdm(String jcdm) {
        this.jcdm = jcdm;
    }

    public String getBz() {
        return bz;
    }

    public void setBz(String bz) {
        this.bz = bz;
    }

    public String getKssj() {
        return kssj;
    }

    public void setKssj(String kssj) {
        this.kssj = kssj;
    }

    public String getJcmc() {
        return jcmc;
    }

    public void setJcmc(String jcmc) {
        this.jcmc = jcmc;
    }

    public String getJclb() {
        return jclb;
    }

    public void setJclb(String jclb) {
        this.jclb = jclb;
    }

    public String getByone() {
        return byone;
    }

    public void setByone(String byone) {
        this.byone = byone;
    }
}
