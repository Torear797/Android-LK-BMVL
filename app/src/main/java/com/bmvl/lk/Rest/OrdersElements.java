package com.bmvl.lk.Rest;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class OrdersElements {
    @SerializedName("items")
    @Expose
    private List<com.bmvl.lk.data.models.Orders> Orders;

    @SerializedName("first")
    @Expose
    private int first;

    @SerializedName("before")
    @Expose
    private int before;

    @SerializedName("current")
    @Expose
    private byte current;

    @SerializedName("last")
    @Expose
    private int last;

    @SerializedName("next")
    @Expose
    private byte next;

    @SerializedName("total_pages")
    @Expose
    private byte total_pages;

    @SerializedName("total_items")
    @Expose
    private int total_items;

    @SerializedName("limit")
    @Expose
    private int limit;

    public List<com.bmvl.lk.data.models.Orders> getOrders() {
        return Orders;
    }

    public byte getCurrent() {
        return current;
    }

    public byte getTotal_pages() {
        return total_pages;
    }
}
