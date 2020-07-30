package com.bmvl.lk.data.models;

public class ItemNotify {
    private String Name;
    private String ColumnId;
    private boolean LK;
    private boolean Email;
    private boolean SMS;
    private byte type;

    public ItemNotify(String name, String columnId, boolean LK, boolean email, boolean SMS) {
        Name = name;
        ColumnId = columnId;
        this.LK = LK;
        Email = email;
        this.SMS = SMS;
        this.type = 0;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getColumnId() {
        return ColumnId;
    }

    public boolean isLK() {
        return LK;
    }

    public void setLK(boolean LK) {
        this.LK = LK;
    }

    public boolean isEmail() {
        return Email;
    }

    public void setEmail(boolean email) {
        Email = email;
    }

    public boolean isSMS() {
        return SMS;
    }

    public void setSMS(boolean SMS) {
        this.SMS = SMS;
    }

    public byte getType() {
        return type;
    }

    public void setType(byte type) {
        this.type = type;
    }
}
