package com.bmvl.lk.data.models;

public class OrderStatuses {
    private int id;
    private String name;
    private String btn_сlass_name;
    private int allowed_edit;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getBtn_сlass_name() {
        return btn_сlass_name;
    }

    public void setBtn_сlass_name(String btn_сlass_name) {
        this.btn_сlass_name = btn_сlass_name;
    }

    public int getAllowed_edit() {
        return allowed_edit;
    }

    public void setAllowed_edit(int allowed_edit) {
        this.allowed_edit = allowed_edit;
    }
}
