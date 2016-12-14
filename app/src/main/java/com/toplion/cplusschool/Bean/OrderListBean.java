package com.toplion.cplusschool.Bean;

import java.util.List;

/**
 * Created by wangshengbo on 2016/5/10.
 * 订单列表Bean
 */
public class OrderListBean {
    private List<OrderBean> userOrderInfo;

    public List<OrderBean> getUserOrderInfo() {
        return userOrderInfo;
    }

    public void setUserOrderInfo(List<OrderBean> userOrderInfo) {
        this.userOrderInfo = userOrderInfo;
    }
}
