package com.raghu.adslite.validator;

import com.raghu.adslite.enums.CampaignValidationStatus;

import java.util.List;
import java.util.Optional;
import java.util.regex.Pattern;

public class KeywordValidator implements Validator {

    List<String> keywords;
    public KeywordValidator(List<String> keywords) {
        this.keywords = keywords;
    }

    @Override
    public CampaignValidationStatus validate() {
        if (keywords == null || keywords.isEmpty()) {
            return CampaignValidationStatus.KEYOWRD_INVALID;
        }
        // Alphanumeric, spaces, _, -, and .
        // Check for any invalid keyword
        Optional<String> foundInvalid = keywords.stream()
                .filter(keyword -> !Pattern.matches("[a-zA-Z0-9 \\-_\\.]+", keyword))
                .findAny();
        return foundInvalid.isPresent() ? CampaignValidationStatus.KEYOWRD_INVALID : CampaignValidationStatus.SUCCESS;
    }
}
