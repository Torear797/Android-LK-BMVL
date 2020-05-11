package com.bmvl.lk.Rest.Order;

import com.bmvl.lk.Rest.StandardAnswer;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class OrdersAnswer extends StandardAnswer {
    @SerializedName("orders")
    @Expose
    private OrdersElements orders;

    public OrdersElements getOrders() {
        return orders;
    }
}
