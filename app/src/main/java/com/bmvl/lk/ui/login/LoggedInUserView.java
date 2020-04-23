package com.bmvl.lk.ui.login;

import com.bmvl.lk.Rest.UserAccess;
import com.bmvl.lk.data.models.LoggedInUser;

class LoggedInUserView {

    private UserAccess AccessData;
    private LoggedInUser UserInfo;

    public LoggedInUserView(UserAccess accessData, LoggedInUser userInfo) {
        AccessData = accessData;
        UserInfo = userInfo;
    }

    public UserAccess getAccessData() {
        return AccessData;
    }

    public LoggedInUser getUserInfo() {
        return UserInfo;
    }
}