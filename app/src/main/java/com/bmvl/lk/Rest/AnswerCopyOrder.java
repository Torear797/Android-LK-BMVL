package com.bmvl.lk.Rest;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class AnswerCopyOrder {
    @SerializedName("status")
    @Expose
    private short status;

    @SerializedName("text")
    @Expose
    private String text;

    @SerializedName("orderId")
    @Expose
    private short orderId;

    public short getStatus() {
        return status;
    }

    public short getOrderId() {
        return orderId;
    }
}
