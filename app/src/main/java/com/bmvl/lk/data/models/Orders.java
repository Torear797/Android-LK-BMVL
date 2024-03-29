package com.bmvl.lk.data.models;

public class Orders {
    private int id;
    private String id1c;
    private int user_id;
    private byte type_id;
    private int status_id;
    private String number;
    private String ecp;
    private String date;
    private String note;
    private String ecp_note;
    private double price;
    private int deleted;
    private int from1c;
    private int send_to_1c;
    private String act_of_selection;

    public Orders(int id, int user_id, byte type_id, int status_id, String date) {
        this.id = id;
        this.user_id = user_id;
        this.type_id = type_id;
        this.status_id = status_id;
        this.date = date;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getUser_id() {
        return user_id;
    }

    public byte getType_id() {
        return type_id;
    }

    public int getStatus_id() {
        return status_id;
    }

    public String getDate() {
        return date;
    }

    public String getAct_of_selection() {
        return act_of_selection;
    }

    public void setAct_of_selection(String act_of_selection) {
        this.act_of_selection = act_of_selection;
    }
}