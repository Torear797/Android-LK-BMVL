package com.bmvl.lk.Rest.Order;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

public class SamplesRest {
    @SerializedName("id")
    @Expose
    private short id;

    @SerializedName("price")
    @Expose
    private String price;

    @SerializedName("fields")
    @Expose
    private Map<Short, String> fields = new HashMap<>();

    @SerializedName("researches")
    @Expose
    private TreeMap<Short, ResearchRest> researches = new TreeMap<>();

    public SamplesRest(short id) {
        this.id = id;
    }

    public TreeMap<Short, ResearchRest> getResearches() {
        return this.researches;
    }

    public short getId() {
        return id;
    }

    public String getPrice() {
        return price;
    }

    public Map<Short, String> getFields() {
        return fields;
    }

    public void setData(Map<Short, String> fields, TreeMap<Short, ResearchRest> researches) {
        this.fields = fields;
        this.researches = researches;
    }
}
