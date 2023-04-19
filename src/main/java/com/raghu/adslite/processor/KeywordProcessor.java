package com.raghu.adslite.processor;

import com.raghu.adslite.business.AdPipelineContext;
import com.raghu.adslite.data.CampaignCache;

import java.util.List;
import java.util.Set;

public class KeywordProcessor extends AbstractProcessor {

    List<String> keywords;
    public KeywordProcessor(AdPipelineContext adPipelineContext, List<String> keywords) {
        this.adPipelineContext = adPipelineContext;
        this.keywords = keywords;
    }

    @Override
    public void resolve() {
        Set<Long> resultIds = adPipelineContext.getIds();
        resultIds.removeIf(id -> !containsAnyKeyword(keywords, CampaignCache.getCampaign(id).getTargetKeywords()));
        evaluateResponseState(resultIds);
    }

    private boolean containsAnyKeyword(List<String> requestKeywords, List<String> campaignKeywords) {
        for (String req : requestKeywords) {
            if (campaignKeywords.contains(req)) {
                return true;
            }
        }
        return false;
    }
}
