package com.raghu.adslite.business;

import com.raghu.adslite.enums.CampaignValidationStatus;
import com.raghu.adslite.exception.BusinessException;
import com.raghu.adslite.objects.Campaign;
import com.raghu.adslite.objects.CampaignResponse;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.util.Assert;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@ExtendWith(MockitoExtension.class)
public class CampaignManagerTest {

    @InjectMocks
    private CampaignManager campaignManager;

    @Test
    void contextLoads() {
    }

    @Test
    void campaignCreateSuccessTest() throws BusinessException {
        List<String> keywords = Arrays.asList("iphone", "5G");
        CampaignResponse campaignResponse = campaignManager.create(1, 10, keywords, 1642493821, 1742580221);
        Assert.isTrue(campaignResponse.getCampaignId() > 0, "Campaign id valid.");
    }


    @Test
    void maxImpressionInvalidTest() {
        Campaign campaign = new Campaign();
        List<String> keywords = new ArrayList<>();
        keywords.add("keyword1");
        boolean foundBusinessException = false;
        CampaignValidationStatus status = null;
        try {
            campaignManager.create(0, 10, keywords, 1642493821, 1742580221);
        } catch (Exception ex) {
            if (ex instanceof BusinessException) {
                foundBusinessException = true;
                status = ((BusinessException) ex).getCampaignValidationStatus();
            }
        }
        Assert.isTrue(foundBusinessException, "BusinessException thrown");
        Assert.isTrue(status == CampaignValidationStatus.MAX_IMPRESSIONS_INVALID, "Campaign validation status is not SUCCESS");
    }

    @Test
    void cpmInvalidTest() {
        Campaign campaign = new Campaign();
        List<String> keywords = new ArrayList<>();
        keywords.add("keyword1");
        boolean foundBusinessException = false;
        CampaignValidationStatus status = null;
        try {
            campaignManager.create(1, -1, keywords, 1642493821, 1742580221);
        } catch (Exception ex) {
            if (ex instanceof BusinessException) {
                foundBusinessException = true;
                status = ((BusinessException) ex).getCampaignValidationStatus();
            }
        }
        Assert.isTrue(foundBusinessException, "BusinessException thrown");
        Assert.isTrue(status == CampaignValidationStatus.CPM_INVALID, "Campaign validation status is not SUCCESS");
    }

    @Test
    void keywordsNullTest() {
        Campaign campaign = new Campaign();
        List<String> keywords = new ArrayList<>();
        keywords.add("keyword1");
        boolean foundBusinessException = false;
        CampaignValidationStatus status = null;
        try {
            campaignManager.create(1, 20, null, 1642493821, 1742580221);
        } catch (Exception ex) {
            if (ex instanceof BusinessException) {
                foundBusinessException = true;
                status = ((BusinessException) ex).getCampaignValidationStatus();
            }
        }
        Assert.isTrue(foundBusinessException, "BusinessException thrown");
        Assert.isTrue(status == CampaignValidationStatus.KEYOWRD_INVALID, "Campaign validation status is not SUCCESS");
    }

    @Test
    void keywordsEmptyTest() {
        List<String> keywords = new ArrayList<>();
        boolean foundBusinessException = false;
        CampaignValidationStatus status = null;
        try {
            campaignManager.create(1, 20, keywords, 1642493821, 1742580221);
        } catch (Exception ex) {
            if (ex instanceof BusinessException) {
                foundBusinessException = true;
                status = ((BusinessException) ex).getCampaignValidationStatus();
            }
        }
        Assert.isTrue(foundBusinessException, "BusinessException thrown");
        Assert.isTrue(status == CampaignValidationStatus.KEYOWRD_INVALID, "Campaign validation status is not SUCCESS");
    }

    @Test
    void keywordsInvalidCharsTest() {
        List<String> keywords = new ArrayList<>();
        keywords.add("#$abc");
        boolean foundBusinessException = false;
        CampaignValidationStatus status = null;
        try {
            campaignManager.create(1, 20, keywords, 1642493821, 1742580221);
        } catch (Exception ex) {
            if (ex instanceof BusinessException) {
                foundBusinessException = true;
                status = ((BusinessException) ex).getCampaignValidationStatus();
            }
        }
        Assert.isTrue(foundBusinessException, "BusinessException thrown");
        Assert.isTrue(status == CampaignValidationStatus.KEYOWRD_INVALID, "Campaign validation status is not SUCCESS");
    }

    @Test
    void startTimeInvalidTest() {
        List<String> keywords = new ArrayList<>();
        keywords.add("abc");
        boolean foundBusinessException = false;
        CampaignValidationStatus status = null;
        try {
            campaignManager.create(1, 20, keywords, 0, 1742580221);
        } catch (Exception ex) {
            if (ex instanceof BusinessException) {
                foundBusinessException = true;
                status = ((BusinessException) ex).getCampaignValidationStatus();
            }
        }
        Assert.isTrue(foundBusinessException, "BusinessException thrown");
        Assert.isTrue(status == CampaignValidationStatus.TIMESTAMP_INVALID, "Campaign validation status is not SUCCESS");
    }

    @Test
    void endTimeInvalidTest() {
        List<String> keywords = new ArrayList<>();
        keywords.add("abc");
        boolean foundBusinessException = false;
        CampaignValidationStatus status = null;
        try {
            campaignManager.create(1, 20, keywords, 1642493821, 0);
        } catch (Exception ex) {
            if (ex instanceof BusinessException) {
                foundBusinessException = true;
                status = ((BusinessException) ex).getCampaignValidationStatus();
            }
        }
        Assert.isTrue(foundBusinessException, "BusinessException thrown");
        Assert.isTrue(status == CampaignValidationStatus.TIMESTAMP_INVALID, "Campaign validation status is not SUCCESS");
    }

    @Test
    void endTimeLessThanStartInvalidTest() {
        List<String> keywords = new ArrayList<>();
        keywords.add("abc");
        boolean foundBusinessException = false;
        CampaignValidationStatus status = null;
        try {
            campaignManager.create(1, 20, keywords, 1742580221, 1642493821);
        } catch (Exception ex) {
            if (ex instanceof BusinessException) {
                foundBusinessException = true;
                status = ((BusinessException) ex).getCampaignValidationStatus();
            }
        }
        Assert.isTrue(foundBusinessException, "BusinessException thrown");
        Assert.isTrue(status == CampaignValidationStatus.TIMESTAMP_INVALID, "Campaign validation status is not SUCCESS");
    }

}
