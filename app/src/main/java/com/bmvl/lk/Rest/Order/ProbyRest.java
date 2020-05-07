package com.bmvl.lk.Rest.Order;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

public class ProbyRest implements Serializable {
    @SerializedName("id")
    @Expose
    private short id;

    @SerializedName("fields")
    @Expose
    private Map<String, String> fields  = new HashMap<>();

    @SerializedName("samples")
    @Expose
    private TreeMap<Short, SamplesRest> samples = new TreeMap<>();

    public void addSample(Short key, SamplesRest newSample) {
        this.samples.put(key, newSample);
    }

    public ProbyRest(short id) {
        this.id = id;
    }

    public void setFields(Map<String, String> fields) {
        this.fields = fields;
    }

    public short getId() {
        return id;
    }

    public TreeMap<Short, SamplesRest> getSamples() {
        return this.samples;
    }

    public Map<String, String> getFields() {
        return fields;
    }

    public void setSamples(TreeMap<Short, SamplesRest> samples) {
        this.samples = samples;
    }

    public boolean isResearchCorrect(){
       //if(samples.size() <=0 || samples.get(sample_id).getResearches().size() <=0) return false;
        int sampleCount = samples.size();
        if(sampleCount <= 0) return false;
        for(short i = 0; i < sampleCount; i++){
            if(samples.get(getPositionKey(i)).getResearches().size() <=0) return false;
        }
        return true;
    }
    private Short getPositionKey(int position) {
        if(samples.size() > 0)
            return new ArrayList<Short>(samples.keySet()).get(position);
        else return 0;
    }
}
