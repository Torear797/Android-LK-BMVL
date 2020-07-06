package com.bmvl.lk.Rest;

import com.bmvl.lk.data.models.LoggedInUser;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.Map;

public class AnswerOrderNew extends StandardAnswer {

    @SerializedName("defaultFields")
    @Expose
    private Map<Short, String> defaultFields;

    @SerializedName("userInfo")
    @Expose
    private LoggedInUser UserInfo;

    public Map<Short, String[]> getFieldValues() {
        return fieldValues;
    }

    private Map<Short, String[]> fieldValues;

    public Map<Short, String> getDefaultFields() {
        return defaultFields;
    }

    public LoggedInUser getUserInfo() {
        return UserInfo;
    }

}
