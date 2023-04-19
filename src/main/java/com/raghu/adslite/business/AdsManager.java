package com.raghu.adslite.business;

import com.raghu.adslite.AdProcessorResponseState;
import com.raghu.adslite.data.AdDataCache;
import com.raghu.adslite.exception.BusinessException;
import com.raghu.adslite.objects.AdResponse;
import com.raghu.adslite.objects.ShortUrlData;
import com.raghu.adslite.processor.*;
import com.raghu.adslite.utils.SequenceGenerator;
import com.raghu.adslite.utils.ShortUrlGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

/* Performs /adddecision task. Create PipelineContext and runs the AdPipeline with required processors.
* If successful, returns AdResponse with campaignId and impressionUrl.
 */
@Component
public class AdsManager {

    // TODO: Move to application.properties
    private static String DOMAIN_URL = "http://localhost:8000/";

    public AdResponse getAd(List<String> keywords) throws BusinessException {
        AdPipelineContext adPipelineContext = new AdPipelineContext();
        // Create queue for processors and execute them in order to
        // filter messages in each pipeline task.
        Queue<Processor> processors = new LinkedList<>();
        processors.add(new KeywordProcessor(adPipelineContext, keywords));
        processors.add(new CappingProcessor(adPipelineContext));
        processors.add(new CpmProcessor(adPipelineContext));
        processors.add(new CampaignTimeProcessor(adPipelineContext));
        processors.add(new CampaignIdValueProcessor(adPipelineContext));
        while (!processors.isEmpty() && adStateIncomplete(adPipelineContext)) {
            Processor processor = processors.poll();
            // resolve and compute AdProcessorResponseState
            processor.resolve();
        }
        // return campaignId, short url
        if (adPipelineContext.getAdProcessorResponseState() == AdProcessorResponseState.SUCCESS) {
            long resultCampaignId = adPipelineContext.getIds().iterator().next();
            AdResponse adResponse = new AdResponse();
            adResponse.setCampaignId(resultCampaignId);
            adResponse.setImpressionUrl(
                    String.join("",
                            DOMAIN_URL,
                            generateShortUrl(resultCampaignId)));
            return adResponse;
        } else if (adPipelineContext.getAdProcessorResponseState() == AdProcessorResponseState.NO_DATA) {
            // No data found. Return null.
            return null;
        }
        // Any other state is unexpected. Throw an exception and
        // return HttpStatus 500.
        throw new RuntimeException("Unhandled state received: " + adPipelineContext.getAdProcessorResponseState());
    }

    private boolean adStateIncomplete(final AdPipelineContext adPipelineContext) {
        AdProcessorResponseState adState = adPipelineContext.getAdProcessorResponseState();
        return adState == AdProcessorResponseState.RESOLVE_CAMPAIGNS_CONFLICT || adState == AdProcessorResponseState.STARTED;
    }

    private String generateShortUrl(long resultCampaignId) {
        SequenceGenerator sequenceGenerator = new SequenceGenerator();
        long shortUrlId = sequenceGenerator.nextId();
        String shortUrl = ShortUrlGenerator.idToShortURL(shortUrlId);
        ShortUrlData shortUrlData = new ShortUrlData.Builder(
                shortUrlId, resultCampaignId, System.currentTimeMillis()/1000)
                .build();
        AdDataCache.addShortUrl(shortUrl, shortUrlData);
        return shortUrl;
    }

    // Used only for UT purposes.
    // Handy util to clear cache prior to UT testing.
    public void clearImpressionShortUrlData() {
        AdDataCache.clearTrackingData();
    }
}
