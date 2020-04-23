package com.bmvl.lk.data.models;

public class Proby {
    private int id;
    private String protocol;
    private int order_id;
    private double price;

    public Proby(int id, String protocol, int order_id, double price) {
        this.id = id;
        this.protocol = protocol;
        this.order_id = order_id;
        this.price = price;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getProtocol() {
        return protocol;
    }

    public void setProtocol(String protocol) {
        this.protocol = protocol;
    }

    public int getOrder_id() {
        return order_id;
    }

    public void setOrder_id(int order_id) {
        this.order_id = order_id;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }
}
