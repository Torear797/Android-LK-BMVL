package com.bmvl.lk.Rest.Order;

import com.google.gson.Gson;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

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
   // private Map<Short, ProbyRest> proby = new HashMap<>();
    private TreeMap<Short, ProbyRest> proby = new TreeMap<>();

    public void addProb(Short key, ProbyRest newProb){
        this.proby.put(key, newProb);
    }


    public SendOrder(byte type_id) {
        this.type_id = (byte)(type_id + 1);

        this.ecp = "";
        this.id = 0;
        this.enableNotifications = 0;
        this.sendOrder = 0;
        this.pattern = 0;
    }

    public byte getType_id() {
        return (byte)(type_id - 1);
    }

    public void setFields(Map<Short, String> fields) {
        this.fields = fields;
    }

    public String getJsonOrder(){
        Gson gson = new Gson();
        return gson.toJson(this);
    }

    public TreeMap<Short, ProbyRest> getProby() {
        return this.proby;
    }

    public void setEnableNotifications(byte enableNotifications) {
        this.enableNotifications = enableNotifications;
    }
}
