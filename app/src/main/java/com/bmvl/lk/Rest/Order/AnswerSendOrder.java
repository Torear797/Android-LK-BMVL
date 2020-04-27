package com.bmvl.lk.Rest.Order;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class AnswerSendOrder {
    @SerializedName("status")
    @Expose
    private short  status;

    @SerializedName("text")
    @Expose
    private String text;

    @SerializedName("order_id")
    @Expose
    private String order_id;

    public short getStatus() {
        return status;
    }
}
