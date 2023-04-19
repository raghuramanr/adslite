package com.raghu.adslite.data;

import com.raghu.adslite.objects.Campaign;

import java.util.Map;
import java.util.Set;
import java.util.TreeSet;
import java.util.concurrent.ConcurrentHashMap;

public class CampaignCache {

    private static Map<Long, Campaign> campaignCacheMap = new ConcurrentHashMap<>();

    public static boolean addCampaign(final Campaign campaign) {
        if (campaignCacheMap.get(campaign.getCampaignId()) != null) {
            return false;
        }
        campaignCacheMap.put(campaign.getCampaignId(), campaign);
        return true;
    }

    public static Campaign getCampaign(Long campaignId) {
        return campaignCacheMap.get(campaignId);
    }

    public static Set<Long> getActiveContextCampaignIds() {
        Set<Long> sortedSet = new TreeSet<>();
        for (Map.Entry<Long, Campaign> entry : campaignCacheMap.entrySet()) {
            long currTime = System.currentTimeMillis() / 1000;
            Campaign campaign = entry.getValue();
            if (campaign.getStartTimestamp() <= currTime && campaign.getEndTimestamp() > currTime) {
                sortedSet.add(entry.getKey());
            }
        }
        return sortedSet;
    }

    public static void clearCampaignData() {
        campaignCacheMap.clear();
    }

}