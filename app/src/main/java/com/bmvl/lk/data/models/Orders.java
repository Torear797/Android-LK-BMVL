package com.bmvl.lk.data.models;

public class Orders {
    private int id;
    private String id1c;
    private int user_id;
    private int type_id;
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

    public Orders(int id, int user_id, int type_id, int status_id, String date) {
        this.id = id;
        this.user_id = user_id;
        this.type_id = type_id;
        this.status_id = status_id;
        this.date = date;
    }

    public Orders(int id, String id1c, int user_id, int type_id, int status_id, String number, String ecp, String date, String note, String ecp_note, double price, int deleted, int from1c, int send_to_1c, String act_of_selection) {
        this.id = id;
        this.id1c = id1c;
        this.user_id = user_id;
        this.type_id = type_id;
        this.status_id = status_id;
        this.number = number;
        this.ecp = ecp;
        this.date = date;
        this.note = note;
        this.ecp_note = ecp_note;
        this.price = price;
        this.deleted = deleted;
        this.from1c = from1c;
        this.send_to_1c = send_to_1c;
        this.act_of_selection = act_of_selection;
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

    public int getType_id() {
        return type_id;
    }

    public int getStatus_id() {
        return status_id;
    }

    public String getDate() {
        return date;
    }
}