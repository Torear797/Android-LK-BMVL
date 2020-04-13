package com.bmvl.lk.Rest;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class StandardAnswer {
    @SerializedName("success")
    @Expose
    private boolean success;

    @SerializedName("error")
    @Expose
    private String error;

    public boolean isSuccess() {
        return success;
    }

    public String getError() {
        return error;
    }
}
