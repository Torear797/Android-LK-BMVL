package com.bmvl.lk.data.models;

import java.util.Date;

public class Pattern {
    private int id;
    private String PatternName;
    private String TypePattern;
    private String CreateDate;

    public Pattern(int id, String patternName, String typePattern, String createDate) {
        this.id = id;
        PatternName = patternName;
        TypePattern = typePattern;
        CreateDate = createDate;
    }

    public String getPatternName() {
        return PatternName;
    }

    public void setPatternName(String patternName) {
        PatternName = patternName;
    }

    public String getTypePattern() {
        return TypePattern;
    }

    public void setTypePattern(String typePattern) {
        TypePattern = typePattern;
    }

    public String getCreateDate() {
        return CreateDate;
    }

    public void setCreateDate(String createDate) {
        CreateDate = createDate;
    }
}
