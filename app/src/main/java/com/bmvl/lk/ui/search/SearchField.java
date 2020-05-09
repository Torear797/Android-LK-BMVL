package com.bmvl.lk.ui.search;

import android.graphics.drawable.Drawable;

public class SearchField {
    private String value;
    private String Hint;
    private int InputType;
    private boolean spener;
    private int entries;
    private Drawable Icon;
    private boolean Data;
    private boolean DoubleColumn;

    public SearchField(String value, String hint, int inputType, boolean doubleColumn) {
        this.value = value;
        Hint = hint;
        InputType = inputType;
        DoubleColumn = doubleColumn;
    }

    public SearchField(String value, String hint, boolean spener, int entries) {
        this.value = value;
        Hint = hint;
        this.spener = spener;
        this.entries = entries;
    }

    public SearchField(String value, String hint, int inputType, Drawable icon, boolean data) {
        this.value = value;
        Hint = hint;
        InputType = inputType;
        Icon = icon;
        Data = data;
    }

    public SearchField(String value, String hint, boolean spener, int entries, boolean doubleColumn) {
        this.value = value;
        Hint = hint;
        this.spener = spener;
        this.entries = entries;
        DoubleColumn = doubleColumn;
    }

    public boolean isDoubleColumn() {
        return DoubleColumn;
    }

    public void setDoubleColumn(boolean doubleColumn) {
        DoubleColumn = doubleColumn;
    }


    public SearchField(String value, String hint, int inputType) {
        this.value = value;
        Hint = hint;
        InputType = inputType;
    }


    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
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

    public boolean isSpener() {
        return spener;
    }

    public void setSpener(boolean spener) {
        this.spener = spener;
    }

    public Drawable getIcon() {
        return Icon;
    }

    public void setIcon(Drawable icon) {
        Icon = icon;
    }

    public boolean isData() {
        return Data;
    }

    public void setData(boolean data) {
        Data = data;
    }

    public int getEntries() {
        return entries;
    }

    public void setEntries(int entries) {
        this.entries = entries;
    }
}
