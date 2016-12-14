package com.toplion.cplusschool.Bean;

import java.io.Serializable;

/**
 * Created by wangshengbo
 * on 2016/5/4.
 * @des 报修bean
 */
public class RepairInfoBean implements Serializable{
    /**
     *   "OISTATUS": 2,
     "NRIQUESTIONTYPE": 28,
     "NRIADDRESS": "508",
     "OIRTIME": 0,
     "OIID": 271,
     "NRIINTERNETCASE": "201;不正常",
     "IUIPHONE": "13963287412",
     "NRIFAULTDESCRIPTION": "xxxc",
     "SHDID": 26,
     "OICLOSETIME": "Jul 4, 2016 3:45:47 AM",
     "NRIOFACCESS": "302;无线",
     "CANAME": "和平校区",
     "NRIFACILITYTYPE": "101;电脑",
     "OIRINAME": "王",
     "CAID": 41,
     "OINUMBER": "GDsdjzu14676326300987116",
     "DXTITLE": "某个网址/网页打不开",
     "OIRIPHONE": "15325483215",
     "IUINAME": "user123",
     "ADDRESS": "和平校区 实验楼 508",
     "OICREATETIME": "Jul 4, 2016 3:45:47 AM",
     "SHDNAME": "实验楼"
     */
    private int OIID;//id
    private String DXTITLE;//故障现象
    private int OISTATUS;//状态
    private String OINUMBER;//工单号
    private String OICREATETIME;//订单创建时间
    private long OIRTIME;//催单时间
    //报修单详情字段
    private String NRIINTERNETCASE;//周围上网网速
    private String NRIINTERNETCASEID;//周围上网网速id
    private String NRIOFACCESS;//上网方式
    private String NRIOFACCESSID;//上网方式id
    private String NRIFACILITYTYPE;//设备类型
    private String NRIFACILITYTYPEID;//设备类型id
    private int NRIQUESTIONTYPE;//故障现象id
    private String NRIFAULTDESCRIPTION;//故障描述
    private int CAID;//区域id
    private String CANAME;//区域
    private String OIRINAME;//报修人
    private String OIRIPHONE;//报修人手机号
    private String ADDRESS;//地点
    private String SHDNAME;//楼号
    private int SHDID;//楼号id
    private String NRIADDRESS;//房间号

    public int getOIID() {
        return OIID;
    }

    public void setOIID(int OIID) {
        this.OIID = OIID;
    }

    public String getDXTITLE() {
        return DXTITLE;
    }

    public void setDXTITLE(String DXTITLE) {
        this.DXTITLE = DXTITLE;
    }

    public int getOISTATUS() {
        return OISTATUS;
    }

    public void setOISTATUS(int OISTATUS) {
        this.OISTATUS = OISTATUS;
    }

    public String getOINUMBER() {
        return OINUMBER;
    }

    public void setOINUMBER(String OINUMBER) {
        this.OINUMBER = OINUMBER;
    }

    public String getOICREATETIME() {
        return OICREATETIME;
    }

    public void setOICREATETIME(String OICREATETIME) {
        this.OICREATETIME = OICREATETIME;
    }

    public long getOIRTIME() {
        return OIRTIME;
    }

    public void setOIRTIME(long OIRTIME) {
        this.OIRTIME = OIRTIME;
    }

    public int getNRIQUESTIONTYPE() {
        return NRIQUESTIONTYPE;
    }

    public void setNRIQUESTIONTYPE(int NRIQUESTIONTYPE) {
        this.NRIQUESTIONTYPE = NRIQUESTIONTYPE;
    }

    public String getNRIINTERNETCASE() {
        return NRIINTERNETCASE;
    }

    public void setNRIINTERNETCASE(String NRIINTERNETCASE) {
        this.NRIINTERNETCASE = NRIINTERNETCASE;
    }

    public String getNRIINTERNETCASEID() {
        return NRIINTERNETCASEID;
    }

    public void setNRIINTERNETCASEID(String NRIINTERNETCASEID) {
        this.NRIINTERNETCASEID = NRIINTERNETCASEID;
    }

    public String getNRIOFACCESS() {
        return NRIOFACCESS;
    }

    public void setNRIOFACCESS(String NRIOFACCESS) {
        this.NRIOFACCESS = NRIOFACCESS;
    }

    public String getNRIOFACCESSID() {
        return NRIOFACCESSID;
    }

    public void setNRIOFACCESSID(String NRIOFACCESSID) {
        this.NRIOFACCESSID = NRIOFACCESSID;
    }

    public String getNRIFACILITYTYPE() {
        return NRIFACILITYTYPE;
    }

    public void setNRIFACILITYTYPE(String NRIFACILITYTYPE) {
        this.NRIFACILITYTYPE = NRIFACILITYTYPE;
    }

    public String getNRIFACILITYTYPEID() {
        return NRIFACILITYTYPEID;
    }

    public void setNRIFACILITYTYPEID(String NRIFACILITYTYPEID) {
        this.NRIFACILITYTYPEID = NRIFACILITYTYPEID;
    }

    public String getNRIFAULTDESCRIPTION() {
        return NRIFAULTDESCRIPTION;
    }

    public void setNRIFAULTDESCRIPTION(String NRIFAULTDESCRIPTION) {
        this.NRIFAULTDESCRIPTION = NRIFAULTDESCRIPTION;
    }

    public int getCAID() {
        return CAID;
    }

    public void setCAID(int CAID) {
        this.CAID = CAID;
    }

    public String getCANAME() {
        return CANAME;
    }

    public void setCANAME(String CANAME) {
        this.CANAME = CANAME;
    }

    public String getOIRINAME() {
        return OIRINAME;
    }

    public void setOIRINAME(String OIRINAME) {
        this.OIRINAME = OIRINAME;
    }

    public String getOIRIPHONE() {
        return OIRIPHONE;
    }

    public void setOIRIPHONE(String OIRIPHONE) {
        this.OIRIPHONE = OIRIPHONE;
    }

    public String getADDRESS() {
        return ADDRESS;
    }

    public void setADDRESS(String ADDRESS) {
        this.ADDRESS = ADDRESS;
    }

    public String getSHDNAME() {
        return SHDNAME;
    }

    public void setSHDNAME(String SHDNAME) {
        this.SHDNAME = SHDNAME;
    }

    public int getSHDID() {
        return SHDID;
    }

    public void setSHDID(int SHDID) {
        this.SHDID = SHDID;
    }

    public String getNRIADDRESS() {
        return NRIADDRESS;
    }

    public void setNRIADDRESS(String NRIADDRESS) {
        this.NRIADDRESS = NRIADDRESS;
    }
}
