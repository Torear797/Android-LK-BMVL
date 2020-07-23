package com.bmvl.lk.Rest.UserInfo;

import java.util.Map;

public class OrderInfo {
    private Short OD_ID;
    private String OD_Adres;
    private String OD_Email;
    private String FIO;

    public String getURL_SCAN_FILE() {
        return URL_SCAN_FILE;
    }

    public void setURL_SCAN_FILE(String URL_SCAN_FILE) {
        this.URL_SCAN_FILE = URL_SCAN_FILE;
    }

    private String URL_SCAN_FILE;

    private Map<Short, String[]> fieldValues;

    public OrderInfo(Short OD_ID, String value, Map<Short, String[]> fieldValues, boolean isFIO) {
        this.OD_ID = OD_ID;

        if(isFIO)
            this.FIO = value;
        else
        this.OD_Adres = value;

        this.fieldValues = fieldValues;
    }

    public OrderInfo(Short OD_ID, String OD_Adres, String OD_Email, Map<Short, String[]> fieldValues) {
        this.OD_ID = OD_ID;
        this.OD_Adres = OD_Adres;
        this.OD_Email = OD_Email;
        this.fieldValues = fieldValues;
    }

    public Short getOD_ID() {
        return OD_ID;
    }

    public Map<Short, String[]> getFieldValues() {
        return fieldValues;
    }

    public String getOD_Adres() {
        return OD_Adres;
    }

    public String getOD_Email() {
        return OD_Email;
    }

    public String getFIO() {
        return FIO;
    }
}