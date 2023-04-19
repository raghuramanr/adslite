package com.raghu.adslite.data;

import com.raghu.adslite.objects.AdImpression;
import com.raghu.adslite.objects.ShortUrlData;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

public class AdDataCache {

    // Store Map<campaignId, AdImpression>
    private static Map<Long, AdImpression> campaignIdImpressionCountMap = new ConcurrentHashMap<>();

    // Store Map<shortUrlString, ShortUrlData>
    private static Map<String, ShortUrlData> shortUrlCampaignShortUrlMap = new ConcurrentHashMap<>();


    public static long addImpression(long campaignId) {
        campaignIdImpressionCountMap.putIfAbsent(campaignId,
                new AdImpression.Builder(campaignId, new AtomicLong()).build());
        AtomicLong count = campaignIdImpressionCountMap.get(campaignId).getCount();
        return count.incrementAndGet();
    }

    public static AdImpression getImpressionCount(Long campaignId) {
        return campaignIdImpressionCountMap.get(campaignId);
    }

    public static ShortUrlData getShortUrl(String shortUrl) {
        return shortUrlCampaignShortUrlMap.get(shortUrl);
    }

    public static void addShortUrl(String shortUrl, ShortUrlData shortUrlData) {
        shortUrlCampaignShortUrlMap.put(shortUrl, shortUrlData);
    }

    public static void clearTrackingData() {
        campaignIdImpressionCountMap.clear();
        shortUrlCampaignShortUrlMap.clear();
    }
}
