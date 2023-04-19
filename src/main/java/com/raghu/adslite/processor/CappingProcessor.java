package com.raghu.adslite.processor;

import com.raghu.adslite.business.AdPipelineContext;
import com.raghu.adslite.data.AdDataCache;
import com.raghu.adslite.data.CampaignCache;
import com.raghu.adslite.objects.AdImpression;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class CappingProcessor extends AbstractProcessor {
    public CappingProcessor(AdPipelineContext adPipelineContext) {
        this.adPipelineContext = adPipelineContext;
    }

    @Override
    public void resolve() {
        Set<Long> resultIds = adPipelineContext.getIds();
        List<AdImpression> adImpressions = resultIds.stream()
                .map(id -> AdDataCache.getImpressionCount(id))
                .filter(ic -> ic != null)
                .collect(Collectors.toList());
        Collections.sort(adImpressions,
                Comparator.comparing(impression -> impression.getCount().longValue()));
        resultIds.removeIf(id ->
                AdDataCache.getImpressionCount(id) != null &&
                        AdDataCache.getImpressionCount(id).getCount().longValue() >= CampaignCache.getCampaign(id).getMaxImpression());
        evaluateResponseState(resultIds);
    }
}
