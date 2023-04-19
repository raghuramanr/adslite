package com.raghu.adslite.validator;

import com.raghu.adslite.enums.CampaignValidationStatus;

public class TimestampValidator implements Validator {

    long startTimestamp;
    long endTimestamp;
    public TimestampValidator(long startTimestamp, long endTimestamp) {
        this.startTimestamp = startTimestamp;
        this.endTimestamp = endTimestamp;
    }

    @Override
    public CampaignValidationStatus validate() {
        if (startTimestamp <= 0 || endTimestamp <= 0 || startTimestamp > endTimestamp) {
            return CampaignValidationStatus.TIMESTAMP_INVALID;
        }
        return CampaignValidationStatus.SUCCESS;
    }
}
