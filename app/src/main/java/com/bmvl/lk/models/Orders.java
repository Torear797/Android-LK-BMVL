package com.bmvl.lk.models;

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

    public Orders(int id, int user_id, int type_id, String date) {
        this.id = id;
        this.user_id = user_id;
        this.type_id = type_id;
        this.date = date;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getId1c() {
        return id1c;
    }

    public void setId1c(String id1c) {
        this.id1c = id1c;
    }

    public int getUser_id() {
        return user_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }

    public int getType_id() {
        return type_id;
    }

    public void setType_id(int type_id) {
        this.type_id = type_id;
    }

    public int getStatus_id() {
        return status_id;
    }

    public void setStatus_id(int status_id) {
        this.status_id = status_id;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getEcp() {
        return ecp;
    }

    public void setEcp(String ecp) {
        this.ecp = ecp;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public String getEcp_note() {
        return ecp_note;
    }

    public void setEcp_note(String ecp_note) {
        this.ecp_note = ecp_note;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public int getDeleted() {
        return deleted;
    }

    public void setDeleted(int deleted) {
        this.deleted = deleted;
    }

    public int getFrom1c() {
        return from1c;
    }

    public void setFrom1c(int from1c) {
        this.from1c = from1c;
    }

    public int getSend_to_1c() {
        return send_to_1c;
    }

    public void setSend_to_1c(int send_to_1c) {
        this.send_to_1c = send_to_1c;
    }

    public String getAct_of_selection() {
        return act_of_selection;
    }

    public void setAct_of_selection(String act_of_selection) {
        this.act_of_selection = act_of_selection;
    }
}
