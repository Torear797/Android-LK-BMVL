package com.bmvl.lk.ui.Create_Order;

public class Field {
    private int id;
    private int column_id;

    private String value;
    private boolean Doublesize;
    private String Hint;
    private int InputType;


    public Field(int id, int column_id, String value, boolean DoubleSize, String hint, int inputType) {
        this.id = id;
        this.column_id = column_id;
        this.value = value;
        Doublesize = DoubleSize;
        Hint = hint;
        InputType = inputType;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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
}
