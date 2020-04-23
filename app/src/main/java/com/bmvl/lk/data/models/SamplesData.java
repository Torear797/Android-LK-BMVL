package com.bmvl.lk.data.models;

public class SamplesData {
    private int id;
    private int sample_id;
    private int column_id;
    private String value;

    public SamplesData(int id, int sample_id) {
        this.id = id;
        this.sample_id = sample_id;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getSample_id() {
        return sample_id;
    }

    public void setSample_id(int sample_id) {
        this.sample_id = sample_id;
    }

    public int getColumn_id() {
        return column_id;
    }

    public void setColumn_id(int column_id) {
        this.column_id = column_id;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
