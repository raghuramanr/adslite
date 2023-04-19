package com.raghu.adslite.objects;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public class AdRequest {
    @JsonProperty ("keywords")
    private List<String> keywords;

    public List<String> getKeywords() {
        return keywords;
    }

    public void setKeywords(List<String> keywords) {
        this.keywords = keywords;
    }

    @Override
    public String toString() {
        return "AdRequest{" +
                "keywords=" + keywords +
                '}';
    }
}
