package com.bmvl.lk.Rest;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class BodySendOrder {

    @SerializedName("token")
    @Expose
    private String token;

    @SerializedName("order")
    @Expose
    private SendOrder order;

    public BodySendOrder(String token, SendOrder order) {
        this.token = token;
        this.order = order;
    }
}
