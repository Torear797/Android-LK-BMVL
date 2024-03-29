package com.bmvl.lk.data.models;

public class Notifications {
    private int id;
    private int user_id;
    private String date;
    private int order_id;
    private String order_type;
    private int status;
    private String event;

    public Notifications(int id, int user_id, String date, int order_id, String order_type, int status, String event) {
        this.id = id;
        this.user_id = user_id;
        this.date = date;
        this.order_id = order_id;
        this.order_type = order_type;
        this.status = status;
        this.event = event;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public int getOrder_id() {
        return order_id;
    }

    public void setOrder_id(int order_id) {
        this.order_id = order_id;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getEvent() {
        return event;
    }

    public String getOrder_type() {
        return order_type;
    }
}