package com.bmvl.lk.data;

import android.graphics.drawable.Drawable;

public class Field {

    public Field(byte type, int column_id, String hint) {
        Type = type;
        this.column_id = column_id;
        Hint = hint;
    }

    private byte Type;

    public Field(byte type, int column_id) {
        Type = type;
        this.column_id = column_id;
    }

    private String[] SpinerData;

    public Field(byte type, int entries, int column_id, String hint) {
        Type = type;
        this.entries = entries;
        this.column_id = column_id;
        Hint = hint;
    }

    public Field(byte type, String[] Data, int column_id, String hint) {
        Type = type;
        this.SpinerData = Data;
        this.entries = -1;
        this.column_id = column_id;
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

    public Field(byte type, int column_id, String hint, int inputType) {
        Type = type;
        this.column_id = column_id;
        Hint = hint;
        InputType = inputType;
    }

    public int getEntries() {
        return entries;
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

    public Field(int column_id, boolean DoubleSize, String hint, int inputType) {
        this.Type = 0;
        this.column_id = column_id;
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

    public String getHint() {
        return Hint;
    }

    public void setHint(String hint) {
        Hint = hint;
    }

    public int getInputType() {
        return InputType;
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

    public String[] getSpinerData() {
        return SpinerData;
    }
}