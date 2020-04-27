package com.bmvl.lk.ui.login;

import com.bmvl.lk.Rest.UserInfo.OrderInfo;
import com.bmvl.lk.Rest.UserInfo.UserAccess;
import com.bmvl.lk.data.models.LoggedInUser;

class LoggedInUserView {

    private UserAccess AccessData;
    private LoggedInUser UserInfo;
    private OrderInfo OrderInfo;

    public LoggedInUserView(UserAccess accessData, LoggedInUser userInfo, com.bmvl.lk.Rest.UserInfo.OrderInfo orderInfo) {
        AccessData = accessData;
        UserInfo = userInfo;
        OrderInfo = orderInfo;
    }


    public UserAccess getAccessData() {
        return AccessData;
    }

    public LoggedInUser getUserInfo() {
        return UserInfo;
    }

    public com.bmvl.lk.Rest.UserInfo.OrderInfo getOrderInfo() {
        return OrderInfo;
    }
}