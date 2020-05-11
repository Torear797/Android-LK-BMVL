package com.bmvl.lk.Rest;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class AnswerDownloadOrder extends StandardAnswer {

    @SerializedName("docx")
    @Expose
    private String docx;

    public String getDocx() {
        return docx;
    }
}