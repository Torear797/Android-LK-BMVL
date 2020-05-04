package com.bmvl.lk.ui.Create_Order;

import android.graphics.drawable.Drawable;

public class Field {

    private byte Type;

    public Field(byte type, int entries, int column_id, String value, String hint) {
        Type = type;
        this.entries = entries;
        this.column_id = column_id;
        this.value = value;
        Hint = hint;
    }

    public Field(byte type, int column_id, String value, String hint) {
        Type = type;
        this.column_id = column_id;
        this.value = value;
        Hint = hint;
    }

    public Field(byte type) {
        Type = type;
    }

    public int getEntries() {
        return entries;
    }

    public void setEntries(int entries) {
        this.entries = entries;
    }

    private int entries;

    private int column_id;
    private String value;
    private boolean Doublesize;
    private String Hint;
    private int InputType;

    public Field(int column_id, String value, String hint, int inputType, Drawable icon, boolean data) {
        this.Type = 0;
        this.column_id = column_id;
        this.value = value;
        Hint = hint;
        InputType = inputType;
        Icon = icon;
        Data = data;
    }

    private Drawable Icon;

    public boolean isData() {
        return Data;
    }

    public void setData(boolean data) {
        Data = data;
    }

    private boolean Data;

    public Field(int column_id, String value, boolean doublesize, String hint, int inputType, Drawable icon) {
        this.Type = 0;
        this.column_id = column_id;
        this.value = value;
        Doublesize = doublesize;
        Hint = hint;
        InputType = inputType;
        Icon = icon;
    }

    public Field(int column_id, String value, boolean DoubleSize, String hint, int inputType) {
        this.Type = 0;
        this.column_id = column_id;
        this.value = value;
        Doublesize = DoubleSize;
        Hint = hint;
        InputType = inputType;
    }

    public Field(int column_id, String value, String hint, int inputType) {
        this.Type = 0;
        this.column_id = column_id;
        this.value = value;
        Hint = hint;
        InputType = inputType;
    }

    public Field(int column_id, String value, String hint, int inputType, Drawable icon) {
        this.Type = 0;
        this.column_id = column_id;
        this.value = value;
        Hint = hint;
        InputType = inputType;
        Icon = icon;
    }

    public int getColumn_id() {
        return column_id;
    }

    public void setColumn_id(int column_id) {
        this.column_id = column_id;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public boolean isDoubleSize() {
        return Doublesize;
    }

    public void setDoubleSize(boolean enabled) {
        Doublesize = enabled;
    }

    public String getHint() {
        return Hint;
    }

    public void setHint(String hint) {
        Hint = hint;
    }

    public int getInputType() {
        return InputType;
    }

    public void setInputType(int inputType) {
        InputType = inputType;
    }

    public boolean isDoublesize() {
        return Doublesize;
    }

    public void setDoublesize(boolean doublesize) {
        Doublesize = doublesize;
    }

    public Drawable getIcon() {
        return Icon;
    }

    public void setIcon(Drawable icon) {
        Icon = icon;
    }

    public byte getType() {
        return Type;
    }

    public void setType(byte type) {
        Type = type;
    }
}