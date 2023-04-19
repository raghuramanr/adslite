package com.raghu.adslite.validator;

import com.raghu.adslite.enums.CampaignValidationStatus;

public class CpmValidator implements Validator {

    float cpm;
    public CpmValidator(float cpm) {
        this.cpm = cpm;
    }

    @Override
    public CampaignValidationStatus validate() {
        if (cpm <= 0) {
            return CampaignValidationStatus.CPM_INVALID;
        }
        return CampaignValidationStatus.SUCCESS;
    }
}
