package com.bmvl.lk.Rest.UserInfo;

import com.bmvl.lk.Rest.StandardAnswer;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class UserAccess extends StandardAnswer {
    @SerializedName("token")
    @Expose
    private String token;

    @SerializedName("exp")
    @Expose
    private String exp;

    @SerializedName("user_id")
    @Expose
    private String user_id;

    public UserAccess(String token, String exp, String user_id) {
        this.token = token;
        this.exp = exp;
        this.user_id = user_id;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getExp() {
        return exp;
    }

    public void setExp(String exp) {
        this.exp = exp;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }
}