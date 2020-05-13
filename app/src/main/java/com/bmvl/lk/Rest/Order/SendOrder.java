package com.bmvl.lk.Rest.Order;

import com.google.gson.Gson;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

public class SendOrder implements Serializable{
    @SerializedName("ecp")
    @Expose
    private String ecp;

    @SerializedName("type_id")
    @Expose
    private byte type_id;

    @SerializedName("id")
    @Expose
    private int id;

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
    private Map<Short, String> fields = new HashMap<>();

    @SerializedName("proby")
    @Expose
    private TreeMap<Short, ProbyRest> proby = new TreeMap<>();

    public void addProb(Short key, ProbyRest newProb) {
        this.proby.put(key, newProb);
    }

    public void DeleteProb(){
        this.proby = null;
    }


    public SendOrder(byte type_id) {
        this.type_id = type_id;

        this.ecp = "";
        this.id = 0;
        this.enableNotifications = 0;
        this.sendOrder = 0;
        this.pattern = 0;
    }

    public byte getType_id() {
        return type_id;
    }

    public void setFields(Map<Short, String> fields) {
        this.fields = fields;
    }

    public String getJsonOrder() {
        Gson gson = new Gson();
        return gson.toJson(this);
    }

    public TreeMap<Short, ProbyRest> getProby() {
        return this.proby;
    }

    public Map<Short, String> getFields() {
        return fields;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setProby(TreeMap<Short, ProbyRest> proby) {
        this.proby = proby;
    }

    public void setPattern(byte pattern) {
        this.pattern = pattern;
    }
}
