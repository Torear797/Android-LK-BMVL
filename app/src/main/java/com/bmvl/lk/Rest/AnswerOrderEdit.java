package com.bmvl.lk.Rest;

import com.bmvl.lk.Rest.Order.ProbyRest;
import com.bmvl.lk.Rest.Order.SendOrder;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.Map;
import java.util.TreeMap;

public class AnswerOrderEdit extends StandardAnswer {
    @SerializedName("order")
    @Expose
    private SendOrder order;

    public boolean isReadonly() {
        return readonly;
    }

    private boolean readonly;

    public Map<Short, String> getOrderFields() {
        return order.getFields();
    }

    public TreeMap<Short, ProbyRest> getProby() {
        return this.order.getProby();
    }
}