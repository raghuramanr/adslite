package com.raghu.adslite.business;

import com.raghu.adslite.data.CampaignCache;
import com.raghu.adslite.enums.CampaignValidationStatus;
import com.raghu.adslite.exception.BusinessException;
import com.raghu.adslite.objects.Campaign;
import com.raghu.adslite.objects.CampaignResponse;
import com.raghu.adslite.utils.SequenceGenerator;
import com.raghu.adslite.validator.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

@Component
public class CampaignManager {

    public CampaignResponse create(long maxImpression, float cpm, List<String> targetKeywords, long startTimestamp, long endTimestamp) throws BusinessException {

        // Add validators and execute them in order.
        Queue<Validator> validators = new LinkedList<>();
        validators.add(new TimestampValidator(startTimestamp, endTimestamp));
        validators.add(new KeywordValidator(targetKeywords));
        validators.add(new MaxImpressionValidator(maxImpression));
        validators.add(new CpmValidator(cpm));

        CampaignValidationStatus validationStatus = CampaignValidationStatus.SUCCESS;
        while (!validators.isEmpty() && validationStatus == CampaignValidationStatus.SUCCESS) {
            Validator processor = validators.poll();
            validationStatus = processor.validate();
        }
        if (validationStatus != CampaignValidationStatus.SUCCESS) {
            throw new BusinessException(validationStatus, validationStatus.toString());
        }
        Campaign campaign = new Campaign();
        SequenceGenerator sequenceGenerator = new SequenceGenerator();
        campaign.setCampaignId(sequenceGenerator.nextId());
        campaign.setMaxImpression(maxImpression);
        campaign.setCpm(cpm);
        campaign.setTargetKeywords(targetKeywords);
        campaign.setStartTimestamp(startTimestamp);
        campaign.setEndTimestamp(endTimestamp);
        CampaignCache.addCampaign(campaign);
        CampaignResponse campaignResponse = new CampaignResponse();
        campaignResponse.setCampaignId(campaign.getCampaignId());
        return campaignResponse;
    }

    // Used only for UT purposes.
    // Handy util to clear cache prior to UT testing.
    public void clearCache() {
        CampaignCache.clearCampaignData();
    }
}
