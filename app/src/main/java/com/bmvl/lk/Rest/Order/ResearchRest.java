package com.bmvl.lk.Rest.Order;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.Map;

public class ResearchRest {
    @SerializedName("id")
    @Expose
    private short id;

    @SerializedName("price")
    @Expose
    private String price;

    @SerializedName("researches")
    @Expose
    private Map<Short, ResearchRest> researches;
}
