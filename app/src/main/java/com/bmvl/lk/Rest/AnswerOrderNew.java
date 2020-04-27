package com.bmvl.lk.Rest;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.Map;

public class AnswerOrderNew {
    @SerializedName("status")
    @Expose
    private short  status;

    @SerializedName("text")
    @Expose
    private String text;

    @SerializedName("defaultFields")
    @Expose
   // private Map<Short, String> defaultFields;
    private Map<Short, String> defaultFields;

//    @SerializedName("fieldValues")
//    @Expose
//  //  private Map<Short, String> fieldValues;
//    private List<PairData> fieldValues;

    public short getStatus() {
        return status;
    }

    public Map getDefaultFields() {
        return defaultFields;
    }

//    public List<PairData> getDefaultFields() {
//        return defaultFields;
//    }
}
