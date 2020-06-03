package com.bmvl.lk.data.models;

public class Pattern extends Orders {

    public Pattern(int id, int user_id, byte type_id, int status_id, String date, String name) {
        super(id, user_id, type_id, status_id, date);
        this.patternName = name;
    }
    private String patternName;


    public String getPatternName() {
        return patternName;
    }
}
