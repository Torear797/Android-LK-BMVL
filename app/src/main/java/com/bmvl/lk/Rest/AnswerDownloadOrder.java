package com.bmvl.lk.Rest;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class AnswerDownloadOrder {
    @SerializedName("status")
    @Expose
    private short status;

    @SerializedName("text")
    @Expose
    private String text;

    @SerializedName("docx")
    @Expose
    private String docx;

    public short getStatus() {
        return status;
    }

    public String getDocx() {
        return docx;
    }
}
