package com.raghu.adslite.validator;

import com.raghu.adslite.enums.CampaignValidationStatus;

public class MaxImpressionValidator implements Validator {

    long maxImpressions;
    public MaxImpressionValidator(long maxImpressions) {
        this.maxImpressions = maxImpressions;
    }

    @Override
    public CampaignValidationStatus validate() {
        if (maxImpressions <= 0) {
            return CampaignValidationStatus.MAX_IMPRESSIONS_INVALID;
        }
        return CampaignValidationStatus.SUCCESS;
    }
}
