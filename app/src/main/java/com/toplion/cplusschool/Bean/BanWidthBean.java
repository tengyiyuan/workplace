package com.toplion.cplusschool.Bean;

import java.io.Serializable;

/**
 * Created by wang
 * on 2016/8/24.
 */
public class BanWidthBean implements Serializable{
    /**
     *    本次包月优惠价55|b1|包月|55|1,
     */
    private String banWidth;//套餐
    private String banType;// 包月：0  包学期：1
    private String price;//价格
    private String isDiscount;//无：0  有：1
    private String banWidthWZ;//完整字符串

    public String getBanWidth() {
        return banWidth;
    }

    public void setBanWidth(String banWidth) {
        this.banWidth = banWidth;
    }

    public String getBanType() {
        return banType;
    }

    public void setBanType(String banType) {
        this.banType = banType;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getIsDiscount() {
        return isDiscount;
    }

    public void setIsDiscount(String isDiscount) {
        this.isDiscount = isDiscount;
    }

    public String getBanWidthWZ() {
        return banWidthWZ;
    }

    public void setBanWidthWZ(String banWidthWZ) {
        this.banWidthWZ = banWidthWZ;
    }
}
