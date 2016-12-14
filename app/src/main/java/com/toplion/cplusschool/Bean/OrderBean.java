package com.toplion.cplusschool.Bean;

import java.io.Serializable;

/**
 * Created by wangshengbo on 2016/5/10.
 * 订单bean
 */
public class OrderBean implements Serializable{
    private String loginName;//用户名
    private String orderId;//订单id
    private String orderOriginTemplateName;//订单编号
    private String orderOriginPackageName;//sam套餐
    private String orderTemplateName;//用户类型
    private String orderPackageName;//服务器套餐
    private String orderFee;//价格
    private String orderCreateTime;
    private String orderOutdateTime;
    private String orderClientPackagePeriod;
    private String orderPayString;//微信支付字符串
    private String orderState;//订单状态
    private String orderFunction;//支付方式 alipay:支付宝;wxpay:微信

    public String getLoginName() {
        return loginName;
    }

    public void setLoginName(String loginName) {
        this.loginName = loginName;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getOrderOriginTemplateName() {
        return orderOriginTemplateName;
    }

    public void setOrderOriginTemplateName(String orderOriginTemplateName) {
        this.orderOriginTemplateName = orderOriginTemplateName;
    }

    public String getOrderOriginPackageName() {
        return orderOriginPackageName;
    }

    public void setOrderOriginPackageName(String orderOriginPackageName) {
        this.orderOriginPackageName = orderOriginPackageName;
    }

    public String getOrderTemplateName() {
        return orderTemplateName;
    }

    public void setOrderTemplateName(String orderTemplateName) {
        this.orderTemplateName = orderTemplateName;
    }

    public String getOrderPackageName() {
        return orderPackageName;
    }

    public void setOrderPackageName(String orderPackageName) {
        this.orderPackageName = orderPackageName;
    }

    public String getOrderFee() {
        return orderFee;
    }

    public void setOrderFee(String orderFee) {
        this.orderFee = orderFee;
    }

    public String getOrderCreateTime() {
        return orderCreateTime;
    }

    public void setOrderCreateTime(String orderCreateTime) {
        this.orderCreateTime = orderCreateTime;
    }

    public String getOrderOutdateTime() {
        return orderOutdateTime;
    }

    public void setOrderOutdateTime(String orderOutdateTime) {
        this.orderOutdateTime = orderOutdateTime;
    }

    public String getOrderClientPackagePeriod() {
        return orderClientPackagePeriod;
    }

    public void setOrderClientPackagePeriod(String orderClientPackagePeriod) {
        this.orderClientPackagePeriod = orderClientPackagePeriod;
    }

    public String getOrderPayString() {
        return orderPayString;
    }

    public void setOrderPayString(String orderPayString) {
        this.orderPayString = orderPayString;
    }

    public String getOrderState() {
        return orderState;
    }

    public void setOrderState(String orderState) {
        this.orderState = orderState;
    }

    public String getOrderFunction() {
        return orderFunction;
    }

    public void setOrderFunction(String orderFunction) {
        this.orderFunction = orderFunction;
    }
}
