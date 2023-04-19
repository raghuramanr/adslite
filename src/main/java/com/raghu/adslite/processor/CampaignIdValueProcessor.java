package com.raghu.adslite.processor;

import com.raghu.adslite.business.AdPipelineContext;

import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class CampaignIdValueProcessor extends AbstractProcessor {
    public CampaignIdValueProcessor(AdPipelineContext adPipelineContext) {
        this.adPipelineContext = adPipelineContext;
    }

    @Override
    public void resolve() {
        Set<Long> resultIds = adPipelineContext.getIds();
        List<Long> resultIdsList = resultIds.stream().collect(Collectors.toList());
        Collections.sort(resultIdsList);
        resultIds.removeIf(id -> id != resultIdsList.get(0));
        evaluateResponseState(resultIds);
    }
}
