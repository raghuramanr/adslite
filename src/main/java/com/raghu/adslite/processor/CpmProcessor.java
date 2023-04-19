package com.raghu.adslite.processor;

import com.raghu.adslite.business.AdPipelineContext;
import com.raghu.adslite.data.CampaignCache;
import com.raghu.adslite.objects.Campaign;

import java.util.*;
import java.util.stream.Collectors;

public class CpmProcessor extends AbstractProcessor {
    public CpmProcessor(AdPipelineContext adPipelineContext) {
        this.adPipelineContext = adPipelineContext;
    }

    @Override
    public void resolve() {
        Set<Long> resultIds = adPipelineContext.getIds();
        List<Campaign> campaigns =
                resultIds.stream()
                        .map( id -> CampaignCache.getCampaign(id))
                        .collect(Collectors.toList());
        Collections.sort(campaigns, Comparator.comparing(Campaign::getCpm));
        Set<Long> newIds = new TreeSet<>();
        newIds.add(campaigns.get(campaigns.size()-1).getCampaignId());
        float maxCpm =  campaigns.get(campaigns.size()-1).getCpm();
        for (int i = campaigns.size()-2; i >=0; i--) {
            if (Float.compare(campaigns.get(i).getCpm(), maxCpm) == 0) {
                newIds.add(campaigns.get(i).getCampaignId());
            } else {
                break;
            }
        }
        adPipelineContext.setIds(newIds);
        evaluateResponseState(newIds);
    }
}
