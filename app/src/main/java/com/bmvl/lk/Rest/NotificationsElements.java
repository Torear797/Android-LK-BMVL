package com.bmvl.lk.Rest;

import com.bmvl.lk.models.Notifications;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class NotificationsElements {
    @SerializedName("items")
    @Expose
    private List<Notifications> notifications;

    @SerializedName("first")
    @Expose
    private int first;

    @SerializedName("before")
    @Expose
    private int before;

    @SerializedName("current")
    @Expose
    private int current;

    @SerializedName("last")
    @Expose
    private int last;

    @SerializedName("next")
    @Expose
    private int next;

    @SerializedName("total_pages")
    @Expose
    private int total_pages;

    @SerializedName("total_items")
    @Expose
    private int total_items;

    @SerializedName("limit")
    @Expose
    private int limit;

    public List<Notifications> getNotifications() {
        return notifications;
    }
}
