package com.bmvl.lk.Rest.Order;

import com.bmvl.lk.Rest.StandardAnswer;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class AnswerSendOrder extends StandardAnswer {

    @SerializedName("order_id")
    @Expose
    private String order_id;
}
