package com.bmvl.lk.Rest;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class AnswerCopyOrder extends StandardAnswer{

    @SerializedName("orderId")
    @Expose
    private short orderId;

    public short getOrderId() {
        return orderId;
    }
}