package com.raghu.adslite.objects;

import com.fasterxml.jackson.annotation.JsonProperty;

public class AdResponse {

    @JsonProperty ("campaign_id")
    private long campaignId;

    @JsonProperty ("impression_url")
    private String impressionUrl;

    public long getCampaignId() {
        return campaignId;
    }

    public void setCampaignId(long campaignId) {
        this.campaignId = campaignId;
    }

    public String getImpressionUrl() {
        return impressionUrl;
    }

    public void setImpressionUrl(String impressionUrl) {
        this.impressionUrl = impressionUrl;
    }

    @Override
    public String toString() {
        return "AdResponse{" +
                "campaignId=" + campaignId +
                ", impressionUrl='" + impressionUrl + '\'' +
                '}';
    }
}
