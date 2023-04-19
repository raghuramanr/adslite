package com.raghu.adslite.objects;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public class Campaign {

    @JsonProperty ("campaign_id")
    private Long campaignId;

    @JsonProperty ("max_impression")
    private long maxImpression;

    @JsonProperty ("cpm")
    private float cpm;

    @JsonProperty ("target_keywords")
    private List<String> targetKeywords;

    @JsonProperty ("start_timestamp")
    private long startTimestamp;

    @JsonProperty ("end_timestamp")
    private long endTimestamp;

    public Long getCampaignId() {
        return campaignId;
    }


    public void setCampaignId(Long campaignId) {
        this.campaignId = campaignId;
    }

    public long getMaxImpression() {
        return maxImpression;
    }

    public void setMaxImpression(long maxImpression) {
        this.maxImpression = maxImpression;
    }

    public float getCpm() {
        return cpm;
    }

    public void setCpm(float cpm) {
        this.cpm = cpm;
    }

    public List<String> getTargetKeywords() {
        return targetKeywords;
    }

    public void setTargetKeywords(List<String> targetKeywords) {
        this.targetKeywords = targetKeywords;
    }

    public long getStartTimestamp() {
        return startTimestamp;
    }

    public void setStartTimestamp(long startTimestamp) {
        this.startTimestamp = startTimestamp;
    }

    public long getEndTimestamp() {
        return endTimestamp;
    }

    public void setEndTimestamp(long endTimestamp) {
        this.endTimestamp = endTimestamp;
    }

    @Override
    public String toString() {
        return "Campaign{" +
                "campaignId=" + campaignId +
                ", maxImpressions=" + maxImpression +
                ", cpm=" + cpm +
                ", targetKeywords=" + targetKeywords +
                ", startTimestamp=" + startTimestamp +
                ", endTimestamp=" + endTimestamp +
                '}';
    }
}
