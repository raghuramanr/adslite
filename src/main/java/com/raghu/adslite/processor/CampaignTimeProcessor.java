package com.raghu.adslite.processor;

import com.raghu.adslite.business.AdPipelineContext;
import com.raghu.adslite.data.CampaignCache;
import com.raghu.adslite.objects.Campaign;

import java.util.*;
import java.util.stream.Collectors;

public class CampaignTimeProcessor extends AbstractProcessor {
    public CampaignTimeProcessor(AdPipelineContext adPipelineContext) {
        this.adPipelineContext = adPipelineContext;
    }

    @Override
    public void resolve() {
        Set<Long> resultIds = adPipelineContext.getIds();
        List<Campaign> campaigns = resultIds.stream()
                .map(id -> CampaignCache.getCampaign(id))
                .collect(Collectors.toList());
        Collections.sort(campaigns,
                Comparator.comparing(campaign -> campaign.getEndTimestamp()));
        Set<Long> newIds = new TreeSet<>();
        newIds.add(campaigns.get(0).getCampaignId());
        float earliestEndTime =  campaigns.get(0).getEndTimestamp();
        for (int i = 1; i < campaigns.size(); i++) {
            if (Float.compare(campaigns.get(i).getEndTimestamp(), earliestEndTime) == 0) {
                newIds.add(campaigns.get(i).getCampaignId());
            } else {
                break;
            }
        }
        adPipelineContext.setIds(newIds);
        evaluateResponseState(newIds);
    }
}
