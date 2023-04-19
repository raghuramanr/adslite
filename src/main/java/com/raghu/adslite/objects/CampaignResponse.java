package com.raghu.adslite.objects;

import com.fasterxml.jackson.annotation.JsonProperty;

public class CampaignResponse {
    @JsonProperty ("campaign_id")
    private long campaignId;

    public long getCampaignId() {
        return campaignId;
    }

    public void setCampaignId(long campaignId) {
        this.campaignId = campaignId;
    }
}
