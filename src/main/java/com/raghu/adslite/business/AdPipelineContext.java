package com.raghu.adslite.business;

import com.raghu.adslite.AdProcessorResponseState;
import com.raghu.adslite.data.CampaignCache;

import java.util.Set;

public class AdPipelineContext {
    private Set<Long> ids;

    private AdProcessorResponseState adProcessorResponseState = AdProcessorResponseState.STARTED;

    public AdPipelineContext() {
        this.ids = CampaignCache.getActiveContextCampaignIds();
        if (this.ids == null || this.ids.isEmpty()) {
            adProcessorResponseState = AdProcessorResponseState.NO_DATA;
        }

    }

    public Set<Long> getIds() {
        return ids;
    }

    public void setIds(Set<Long> ids) {
        this.ids = ids;
    }

    public AdProcessorResponseState getAdProcessorResponseState() {
        return adProcessorResponseState;
    }

    public void setAdProcessorResponseState(AdProcessorResponseState adProcessorResponseState) {
        this.adProcessorResponseState = adProcessorResponseState;
    }

}
