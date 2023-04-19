package com.raghu.adslite.processor;

import com.raghu.adslite.AdProcessorResponseState;
import com.raghu.adslite.business.AdPipelineContext;

import java.util.Optional;
import java.util.Set;

public abstract class AbstractProcessor implements Processor {
    protected AdPipelineContext adPipelineContext = new AdPipelineContext();

    protected void evaluateResponseState(Set<Long> resultIds) {
        AdProcessorResponseState adProcessorResponseState = adPipelineContext.getAdProcessorResponseState();
        if (resultIds == null || resultIds.isEmpty()) {
            adProcessorResponseState = AdProcessorResponseState.NO_DATA;
        } else if (resultIds.size() == 1) {
            adProcessorResponseState = AdProcessorResponseState.SUCCESS;
        } else {
            adProcessorResponseState = AdProcessorResponseState.RESOLVE_CAMPAIGNS_CONFLICT;
        }
        adPipelineContext.setAdProcessorResponseState(adProcessorResponseState);
        System.out.println(this.getClass().getName() + "Response state: " + adProcessorResponseState + ", resultIds: " +
                Optional.ofNullable(resultIds).map(Set::size).orElse(0));

    }

}
