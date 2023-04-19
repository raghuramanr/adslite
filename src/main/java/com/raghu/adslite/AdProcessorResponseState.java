package com.raghu.adslite;

/* AdProcessorResponseState maintained in AdPipelineContext
    Set based campaignId count in each *Processor processing.
    resultIds count =1 will be SUCCESS and that campaign will be returned.
    RESOLVE_CAMPAIGNS_CONFLICT is set when count > 1 for further
    processing.
    NO_DATA is set when no campaignId is found and will exit with
 */
public enum AdProcessorResponseState {
    STARTED,
    NO_DATA,
    RESOLVE_CAMPAIGNS_CONFLICT,
    SUCCESS
}
