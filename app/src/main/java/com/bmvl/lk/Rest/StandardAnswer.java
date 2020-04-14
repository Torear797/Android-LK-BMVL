package com.bmvl.lk.Rest;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class StandardAnswer {
    @SerializedName("status")
    @Expose
    private short  status;

    @SerializedName("text")
    @Expose
    private String text;

    public short getStatus() {
        return status;
    }

    public String getText() {
        return text;
    }
}
