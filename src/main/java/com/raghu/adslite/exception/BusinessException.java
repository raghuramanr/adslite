package com.raghu.adslite.exception;

import com.raghu.adslite.enums.CampaignValidationStatus;

public class BusinessException extends Exception {
    CampaignValidationStatus campaignValidationStatus;
    public BusinessException(CampaignValidationStatus campaignValidationStatus, String message) {
        super(message);
        this.campaignValidationStatus = campaignValidationStatus;
    }

    public CampaignValidationStatus getCampaignValidationStatus() {
        return campaignValidationStatus;
    }
}
