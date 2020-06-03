package com.bmvl.lk.Rest;

import com.bmvl.lk.data.models.LoggedInUser;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.Map;

public class AnswerOrderNew extends StandardAnswer{

    @SerializedName("defaultFields")
    @Expose
   // private Map<Short, String> defaultFields;
    private Map<Short, String> defaultFields;

    @SerializedName("userInfo")
    @Expose
    private LoggedInUser UserInfo;

//    @SerializedName("fieldValues")
//    @Expose
//  //  private Map<Short, String> fieldValues;
//    private List<PairData> fieldValues;

    public Map getDefaultFields() {
        return defaultFields;
    }

    public LoggedInUser getUserInfo() {
        return UserInfo;
    }

//    public List<PairData> getDefaultFields() {
//        return defaultFields;
//    }
}
