package com.bmvl.lk.Rest;

import com.bmvl.lk.models.Notifications;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class NotificationsAnswer {
    @SerializedName("success")
    @Expose
    private boolean success;

    @SerializedName("notifications")
    @Expose
    private NotificationsElements notifications;


    public NotificationsElements getNotifications() {
        return notifications;
    }
}
