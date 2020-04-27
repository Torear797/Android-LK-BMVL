package com.bmvl.lk.Rest.Order;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;
import java.util.Map;

public class SendOrder {
    @SerializedName("ecp")
    @Expose
    private String ecp;

    @SerializedName("type_id")
    @Expose
    private byte type_id;

    @SerializedName("id")
    @Expose
    private byte id;

    @SerializedName("enableNotifications")
    @Expose
    private byte enableNotifications;

    @SerializedName("sendOrder")
    @Expose
    private byte sendOrder;

    @SerializedName("pattern")
    @Expose
    private byte pattern;

    @SerializedName("fields")
    @Expose
    private Map<Short, String> fields;


    @SerializedName("proby")
    @Expose
    private Map<Short, ProbyRest> proby;

    public SendOrder(byte type_id, Map<Short, String> fields) {
        this.type_id = type_id;

        this.ecp = "";
        this.id = 0;
        this.enableNotifications = 0;
        this.sendOrder = 0;
        this.pattern = 0;

        this.fields = fields;
    }
}
