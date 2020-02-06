package com.bmvl.lk.models;

public class OrderData {
    private int id;
    private int order_id;
    private int column_id;
    private int value;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getOrder_id() {
        return order_id;
    }

    public void setOrder_id(int order_id) {
        this.order_id = order_id;
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
