package com.bmvl.lk.Rest;

public class SuggestionType {
    private String value;
    private short id;
    private double price;
    private byte inAccreditationArea;

    public String getValue() {
        return value;
    }

    public short getId() {
        return id;
    }

    public double getPrice() {
        return price;
    }

    public byte getInAccreditationArea() {
        return inAccreditationArea;
    }

    public void setInAccreditationArea(byte inAccreditationArea) {
        this.inAccreditationArea = inAccreditationArea;
    }
}
