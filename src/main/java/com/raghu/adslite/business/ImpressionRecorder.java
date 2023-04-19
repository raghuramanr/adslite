package com.raghu.adslite.business;

import com.raghu.adslite.data.AdDataCache;
import com.raghu.adslite.enums.CampaignValidationStatus;
import com.raghu.adslite.exception.BusinessException;
import com.raghu.adslite.objects.ShortUrlData;
import org.springframework.stereotype.Component;

@Component
public class ImpressionRecorder {

    // Enhancement feature. recordImpression arrives after 5 minutes,
    // reject the recording in cache.
    public static long IMPRESSION_EXPIRY_IN_SECONDS = 900;
    public boolean recordImpression(String shortUrl) throws BusinessException {
        if (shortUrl == null || shortUrl.isEmpty()) {
            throw new BusinessException(CampaignValidationStatus.SHORT_URL_IS_NULL, "Short URL is null");
        }
        ShortUrlData shortUrlData = AdDataCache.getShortUrl(shortUrl);
        if (shortUrlData == null) {
            throw new BusinessException(CampaignValidationStatus.SHORT_URL_NOT_FOUND, "Short URL not found");
        }
        // Tampering by bot.
        // Check if impression is delayed by IMPRESSION_EXPIRY_IN_SECONDS. Can happen if bots may try to reuse short url.
        //
        if (shortUrlData.getImpressionTime() + IMPRESSION_EXPIRY_IN_SECONDS < System.currentTimeMillis()/1000) {
            throw new BusinessException(CampaignValidationStatus.SHORT_URL_EXPIRED, "Short URL expired.");
        }
        long count = AdDataCache.addImpression(shortUrlData.getCampaignId());
        System.out.println("Impression count: " + count + " for campaignId: " + shortUrlData.getCampaignId());
        return true;
    }

    // Used only for UT purposes.
    // Handy util to clear cache prior to UT testing.
    public void clearImpressionShortUrlData() {
        AdDataCache.clearTrackingData();
    }

}
