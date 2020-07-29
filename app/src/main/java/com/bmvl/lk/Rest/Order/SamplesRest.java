package com.bmvl.lk.Rest.Order;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

public class SamplesRest implements Serializable {
    @SerializedName("id")
    @Expose
    private short id;

    @SerializedName("price")
    @Expose
    private double price;

    @SerializedName("fields")
    @Expose
    private Map<Short, String> fields;

    @SerializedName("researches")
    @Expose
    private TreeMap<Short, ResearchRest> researches;

    public SamplesRest(short id) {
        this.id = id;
        this.price = 0;
        this.fields = new HashMap<>();
        this.researches = new TreeMap<>();
    }

    public void DeleteSamplesFields() {
        this.fields = null;
    }

    public TreeMap<Short, ResearchRest> getResearches() {
        return this.researches;
    }

    public short getId() {
        return id;
    }

    public double getPrice() {
        RecalculatePrice();
        return price;
    }

    public void RecalculatePrice(){
        this.price = 0;
        for(Map.Entry<Short, ResearchRest> entry : researches.entrySet()) {
            this.price += entry.getValue().getPrice();
        }
    }

    public Map<Short, String> getFields() {
        return fields;
    }

    public void setData(Map<Short, String> fields, TreeMap<Short, ResearchRest> researches) {
        this.fields = fields;
        this.researches = researches;
    }
}
