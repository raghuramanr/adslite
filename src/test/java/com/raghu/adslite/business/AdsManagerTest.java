package com.raghu.adslite.business;

import com.raghu.adslite.exception.BusinessException;
import com.raghu.adslite.objects.AdResponse;
import com.raghu.adslite.objects.CampaignResponse;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.platform.commons.util.StringUtils;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.util.Assert;

import java.util.Arrays;
import java.util.List;

@ExtendWith(MockitoExtension.class)
public class AdsManagerTest {

    @InjectMocks
    private CampaignManager campaignManager;

    @InjectMocks
    private AdsManager adsManager;

    @InjectMocks
    private ImpressionRecorder impressionRecorder;

    private static String keyword_1 = "iphone";
    private static String keyword_2 = "5G";

    private static String DOMAIN_URL="http://localhost:8000/";
    @Test
    void contextLoads() {
    }

    @Test
    void AdsSuccessTest() throws BusinessException {
        List<String> keywords = Arrays.asList(keyword_1, keyword_2);
        campaignManager.create(1, 10, keywords, 1642493821, 1742580221);
        AdResponse adResponse = adsManager.getAd(keywords);
        Assert.isTrue(adResponse.getCampaignId() > 0, "Campaign id valid.");
        boolean status = impressionRecorder.recordImpression(adResponse.getImpressionUrl().replace(DOMAIN_URL, ""));
        Assert.isTrue(status, "Impression recorded successfully");
    }

    @Test
    void demoTest() throws BusinessException {
        adsManager.clearImpressionShortUrlData();
        campaignManager.clearCache();
        List<String> keywords = Arrays.asList("iphone", "5G");
        CampaignResponse campaignResponse_1 = campaignManager.create(1, 20, keywords, 1642493821, 1742580221);
        Assert.isTrue(campaignResponse_1.getCampaignId() > 0, "Campaign id valid.");
        long campaignId_1 = campaignResponse_1.getCampaignId();

        CampaignResponse campaignResponse_2 = campaignManager.create(1, 10, keywords, 1642493821, 1742580221);
        Assert.isTrue(campaignResponse_2.getCampaignId() > 0, "Campaign id valid.");
        long campaignId_2 = campaignResponse_2.getCampaignId();

        AdResponse adResponse = adsManager.getAd(Arrays.asList("iphone"));
        Assert.isTrue(adResponse.getCampaignId() == campaignId_1, "Ad Campaign id valid.");
        String shortUrl_1 = adResponse.getImpressionUrl().replace(DOMAIN_URL, "");
        Assert.isTrue(!StringUtils.isBlank(shortUrl_1), "Ad shorturl_1 valid.");

        boolean recordStatus = true;
        try {
            recordStatus = impressionRecorder.recordImpression(shortUrl_1);
        } catch (Exception ex) {
            recordStatus = false;
        }
        Assert.isTrue(recordStatus, "Impression recorded successfully");

        adResponse = adsManager.getAd(Arrays.asList("Android"));
        Assert.isNull(adResponse, "No matching ad.");

        adResponse = adsManager.getAd(Arrays.asList("iphone", "5G"));
        Assert.isTrue(adResponse.getCampaignId() == campaignId_2, "Ad Campaign 2 id valid.");
        String shortUrl_2 = adResponse.getImpressionUrl().replace(DOMAIN_URL, "");;
        Assert.isTrue(!StringUtils.isBlank(shortUrl_2), "Ad shorturl_2 valid.");

        recordStatus = true;
        try {
            recordStatus = impressionRecorder.recordImpression(shortUrl_2);
        } catch (Exception ex) {
            recordStatus = false;
        }
        Assert.isTrue(recordStatus, "Impression for campaign_2 recorded successfully");

        adResponse = adsManager.getAd(Arrays.asList("iphone"));
        Assert.isNull(adResponse, "Impression capped out.");

    }

    private CampaignResponse createCampaign(long maxImpression, float cpm, List<String> targetKeywords, long startTimestamp, long endTimestamp) throws BusinessException {
        return campaignManager.create(maxImpression, cpm, targetKeywords, startTimestamp, endTimestamp);
    }
}
