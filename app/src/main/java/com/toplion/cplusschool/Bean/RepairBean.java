package com.toplion.cplusschool.Bean;

import java.io.Serializable;

/**
 * Created by wangshengbo on 2016/5/4.
 * @des 报修bean
 */
public class RepairBean implements Serializable{
    private String faultId;//id
    private String faultState;//报修状态
    private String createTime;//创建时间
    private String questionType;//故障现象
    private String osType;//设备类型
    private String netFunction;//上网方式
    private String netspeed;//周围上网速度
    private String areaStr;//区域
    private String floorNumber;//楼号
    private String faultAddress;//具体房间号
    private String faultDes;//描述
    private String repairName;//报修人
    private String repairPhone;//报修电话
    private String serviceName;
    private String servicePhone;
    private String solveTime;
    private String username;//用户名
    private String repairNumber;//工单号

    public String getFaultId() {
        return faultId;
    }

    public void setFaultId(String faultId) {
        this.faultId = faultId;
    }

    public String getFaultState() {
        return faultState;
    }

    public void setFaultState(String faultState) {
        this.faultState = faultState;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String getQuestionType() {
        return questionType;
    }

    public void setQuestionType(String questionType) {
        this.questionType = questionType;
    }

    public String getOsType() {
        return osType;
    }

    public void setOsType(String osType) {
        this.osType = osType;
    }

    public String getNetFunction() {
        return netFunction;
    }

    public void setNetFunction(String netFunction) {
        this.netFunction = netFunction;
    }

    public String getNetspeed() {
        return netspeed;
    }

    public void setNetspeed(String netspeed) {
        this.netspeed = netspeed;
    }

    public String getAreaStr() {
        return areaStr;
    }

    public void setAreaStr(String areaStr) {
        this.areaStr = areaStr;
    }

    public String getFloorNumber() {
        return floorNumber;
    }

    public void setFloorNumber(String floorNumber) {
        this.floorNumber = floorNumber;
    }

    public String getFaultAddress() {
        return faultAddress;
    }

    public void setFaultAddress(String faultAddress) {
        this.faultAddress = faultAddress;
    }

    public String getFaultDes() {
        return faultDes;
    }

    public void setFaultDes(String faultDes) {
        this.faultDes = faultDes;
    }

    public String getRepairName() {
        return repairName;
    }

    public void setRepairName(String repairName) {
        this.repairName = repairName;
    }

    public String getRepairPhone() {
        return repairPhone;
    }

    public void setRepairPhone(String repairPhone) {
        this.repairPhone = repairPhone;
    }

    public String getServiceName() {
        return serviceName;
    }

    public void setServiceName(String serviceName) {
        this.serviceName = serviceName;
    }

    public String getServicePhone() {
        return servicePhone;
    }

    public void setServicePhone(String servicePhone) {
        this.servicePhone = servicePhone;
    }

    public String getSolveTime() {
        return solveTime;
    }

    public void setSolveTime(String solveTime) {
        this.solveTime = solveTime;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getRepairNumber() {
        return repairNumber;
    }

    public void setRepairNumber(String repairNumber) {
        this.repairNumber = repairNumber;
    }
}
