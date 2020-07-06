package com.bmvl.lk.Rest;

public class Material {
    private short id;
    private String parent;
    private String text;

    public short getId() {
        return id;
    }

    public void setId(short id) {
        this.id = id;
    }

    public String getParent() {
        return parent;
    }

    public void setParent(String parent) {
        this.parent = parent;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
}
