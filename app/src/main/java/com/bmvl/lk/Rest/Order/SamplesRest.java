package com.bmvl.lk.Rest.Order;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.Map;
import java.util.TreeMap;

public class SamplesRest {
    @SerializedName("id")
    @Expose
    private short id;

    @SerializedName("fields")
    @Expose
    private Map<Short, String> fields;

    @SerializedName("researches")
    @Expose
   // private Map<Short, ResearchRest> researches = new HashMap<>();
    private TreeMap<Short, ResearchRest> researches = new TreeMap<>();

    public SamplesRest(short id) {
        this.id = id;
    }

    public TreeMap<Short, ResearchRest> getResearches() {
        return this.researches;
    }
}
