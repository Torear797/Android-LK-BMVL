package com.bmvl.lk.Rest.UserInfo;

import com.bmvl.lk.data.models.LoggedInUser;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class UserInfoCall {

    @SerializedName("success")
    @Expose
    private String status;

    @SerializedName("userInfo")
    @Expose
    private LoggedInUser UserInfo;

    public UserInfoCall(String status, LoggedInUser userInfo) {
        this.status = status;
        UserInfo = userInfo;
    }

    public String getStatus() {
        return status;
    }

    public LoggedInUser getUserInfo() {
        return UserInfo;
    }
}
