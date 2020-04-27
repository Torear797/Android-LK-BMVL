package com.bmvl.lk.Rest.Order;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.Map;

public class ProbyRest {
    @SerializedName("id")
    @Expose
    private short id;

    @SerializedName("fields")
    @Expose
    private Map<Short, String> fields;

    @SerializedName("samples")
    @Expose
    private Map<Short, SamplesRest> samples;
}
