package com.bmvl.lk.Rest;

import com.bmvl.lk.models.Notifications;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class NotificationsAnswer {
    @SerializedName("status")
    @Expose
    private short  status;

    @SerializedName("text")
    @Expose
    private String text;

    @SerializedName("notifications")
    @Expose
    private NotificationsElements notifications;


    public NotificationsElements getNotifications() {
        return notifications;
    }

    public short getStatus() {
        return status;
    }

    public String getText() {
        return text;
    }
}
