package com.bmvl.lk.Rest.Notify;

import com.bmvl.lk.Rest.StandardAnswer;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class NotificationsAnswer extends StandardAnswer {
    @SerializedName("notifications")
    @Expose
    private NotificationsElements notifications;

    public NotificationsElements getNotifications() {
        return notifications;
    }

}
