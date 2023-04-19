package com.raghu.adslite.objects;

public class ShortUrlData {

    private long shortUrlId;

    private long campaignId;

    // Impression delivery time in epoch.
    // Can be used to detect if the impression is too old.
    private long impressionTime;

    private ShortUrlData(Builder builder) {
        this.shortUrlId = builder.shortUrlId;
        this.campaignId = builder.campaignId;
        this.impressionTime = builder.impressionTime;
    }


    public long getShortUrlId() {
        return shortUrlId;
    }

    public long getCampaignId() {
        return campaignId;
    }

    public long getImpressionTime() {
        return impressionTime;
    }

    public static class Builder {
        private long shortUrlId;
        private long campaignId;
        private long impressionTime;

        public Builder(long shortUrlId, long campaignId, long impressionTime) {
            this.shortUrlId = shortUrlId;
            this.campaignId = campaignId;
            this.impressionTime = impressionTime;
        }

        public ShortUrlData build() {
            return new ShortUrlData(this);
        }
    }
    @Override
    public String toString() {
        return "ShortUrlData{" +
                "shortUrlId=" + shortUrlId +
                ", campaignId=" + campaignId +
                ", impressionTime=" + impressionTime +
                '}';
    }
}
