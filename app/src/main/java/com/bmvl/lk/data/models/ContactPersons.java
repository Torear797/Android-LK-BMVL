package com.bmvl.lk.data.models;

public class ContactPersons {
    int value;
    String text;

    public int getValue() {
        return value;
    }

    public String getText() {
        if(text != null)
        return text;
        else return "null";
    }
}
