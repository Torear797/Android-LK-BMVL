package com.bmvl.lk.models;

public class ProbyData {
    private int id;
    private int proby_id;
    private int column_id;
    private int value;

    public ProbyData(int id, int proby_id, int column_id, int value) {
        this.id = id;
        this.proby_id = proby_id;
        this.column_id = column_id;
        this.value = value;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getProby_id() {
        return proby_id;
    }

    public void setProby_id(int proby_id) {
        this.proby_id = proby_id;
    }

    public int getColumn_id() {
        return column_id;
    }

    public void setColumn_id(int column_id) {
        this.column_id = column_id;
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }
}
