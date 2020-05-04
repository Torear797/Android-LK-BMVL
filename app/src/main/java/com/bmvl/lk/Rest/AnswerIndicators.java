package com.bmvl.lk.Rest;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class AnswerIndicators {
    @SerializedName("suggestions")
    @Expose
    private List<Suggestion> suggestions;

    public List<Suggestion> getSuggestions() {
        return suggestions;
    }
}
