package com.bmvl.lk.Rest.Order;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

public class ProbyRest {
    @SerializedName("id")
    @Expose
    private short id;

    @SerializedName("fields")
    @Expose
    private Map<Short, String> fields;

    @SerializedName("samples")
    @Expose
   // private Map<Short, SamplesRest> samples = new HashMap<>();
    private TreeMap<Short, SamplesRest> samples = new TreeMap<>();


    public void addSample(Short key, SamplesRest newSample){
        this.samples.put(key,newSample);
    }

    public ProbyRest(short id) {
        this.id = id;
    }

    public void setFields(Map<Short, String> fields) {
        this.fields = fields;
    }

    public short getId() {
        return id;
    }

    public Map<Short, SamplesRest> getSamples() {
        return this.samples;
    }

    public Map<Short, String> getFields() {
        return fields;
    }

    public void setSamples(TreeMap<Short, SamplesRest> samples) {
        this.samples = samples;
    }
}
