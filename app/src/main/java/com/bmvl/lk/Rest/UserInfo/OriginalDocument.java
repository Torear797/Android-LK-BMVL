package com.bmvl.lk.Rest.UserInfo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class OriginalDocument {
    @SerializedName("52")
    @Expose
    private String field52;

    @SerializedName("63")
    @Expose
    private List<String> field63 = new ArrayList<>();

    @SerializedName("64")
    @Expose
    private String field64;

    public OriginalDocument() {
        this.field64 = "";
        this.field52 = "";
        setEmptyField63();
    }

    public void setField52(String field52) {
        this.field52 = field52;
    }

    public void setField63(List<String> field63) {
        this.field63 = field63;
    }

    public void setField63_0(String text){
        field63.set(0,text);
    }

    public void setField63_1(String text){
        field63.set(1,text);
    }

    public void setField64(String field64) {
        this.field64 = field64;
    }

    private void setEmptyField63(){
        field63.add("");
        field63.add("");
    }
}
