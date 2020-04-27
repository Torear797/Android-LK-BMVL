package com.bmvl.lk.Rest.UserInfo;

public class OrderInfo {
    private Short OD_ID;
    private String OD_Value;


    public OrderInfo(Short OD_ID, String OD_Value) {
        this.OD_ID = OD_ID;
        this.OD_Value = OD_Value;
    }

    public Short getOD_ID() {
        return OD_ID;
    }

    public String getOD_Value() {
        return OD_Value;
    }
}
