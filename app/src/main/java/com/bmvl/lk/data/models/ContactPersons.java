package com.bmvl.lk.data.models;

public class ContactPersons {
    private int value;
    private String text;

    public ContactPersons(int value, String text) {
        this.value = value;
        this.text = text;
    }

    public int getValue() {
        return value;
    }

    public String getText() {
        if (text != null)
            return text;
        else return "null";
    }
}
