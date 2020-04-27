package com.bmvl.lk.Rest.Order;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class OrdersAnswer {
    @SerializedName("status")
    @Expose
    private short  status;

    @SerializedName("text")
    @Expose
    private String text;

    @SerializedName("orders")
    @Expose
    private OrdersElements orders;

    public OrdersElements getOrders() {
        return orders;
    }
}
