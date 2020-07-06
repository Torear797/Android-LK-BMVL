package com.bmvl.lk.Rest.UserInfo;

import java.util.Map;

public class OrderInfo {
    private Short OD_ID;
    private String OD_Value;

    private Map<Short, String[]> fieldValues;

    public void setOD_ID(Short OD_ID) {
        this.OD_ID = OD_ID;
    }

    public void setOD_Value(String OD_Value) {
        this.OD_Value = OD_Value;
    }

    public void setFieldValues(Map<Short, String[]> fieldValues) {
        this.fieldValues = fieldValues;
    }

    public OrderInfo(Short OD_ID, String OD_Value, Map<Short, String[]> fields) {
        this.OD_ID = OD_ID;
        this.OD_Value = OD_Value;
        this.fieldValues = fields;
    }

    public Short getOD_ID() {
        return OD_ID;
    }

    public String getOD_Value() {
        return OD_Value;
    }

    public Map<Short, String[]> getFieldValues() {
        return fieldValues;
    }
}
