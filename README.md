# **adslite**
Simple in-memory adserver implementation


## **In memory data store**
- To support multithreading, ConcurrentHashMaps are used to store generated data. ConcurrentHashMap locks at record level and not the whole object. And is threadsafe. ConcurrentHashMaps is better than using Synchonized block for performance reasons.

### Campaign data
- ConcurrentHashMap is used to store generated campaign. Map of campaignId and Campaign is used.
- private static Map<Long, Campaign> campaignCacheMap = new ConcurrentHashMap<>();

### AdDataCache
Following maps are used.
- private static Map<Long, AdImpression> campaignIdImpressionCountMap = new ConcurrentHashMap<>();
- private static Map<String, ShortUrlData> shortUrlCampaignShortUrlMap = new HashMap<>();


## CampaignManager
 - Creates new campaign.
 - Creates validator for each of the input parameters and executes them. All validation falure states are listed in CampaignValidationStatus.
 - New validators can be added without modifying existing validator.
 - CampaignId generated is long 64 bit unique integer. Using random sequence number can cause collission. Hence 64 bit UniqueId SequenceGeneratorn is used.
 - Adds created Campaign into campaignCacheMap

## AdsManager
  - Returns single Ad and its impression url
  - Creates mutliple processors and executes them in sequence. Use AdPipelineContext for request session. AdProcessorResponseState is evaluated in each processor. If state = RESOLVE_CAMPAIGNS_CONFLICT, next processor will be processed.
  - New processors can be added without modifying existing processors.
  - Impression url uses shortUrl. shortUrl is generated using SequenceGenerator. This will avoid collisions.
  - Adds shortUrl into shortUrlCampaignShortUrlMap.

## ImpressionRecorder
  - Records impression in data store.
  - Extracts campaignId from shortUrlCampaignShortUrlMap.
  - Adds campaignId into campaignIdImpressionCountMap.
  - Tampering by bot. An expiry of 5 minutes is set, to detect stale requests. Impression requests arriving 5 minutes after creation, will be rejected with SHORT_URL_EXPIRED error.


## Notes:
1. SequenceGenerator is one type of implementation. Due to lack of time, sourced it is sourced from net.
SequenceGenerator can be improved to use it as a singleton. Currently an instance is created for every request.
SequenceGenerator serves the purpose since it avoids collisions.

2. Both campaignId and shortUrl id uses SequenceGenerator.

3. Impression recording should arrive within few seconds. Added a feature to reject if they arrive after 15 minutes.
IMPRESSION_EXPIRY_IN_SECONDS(=900) can be changed to test with low value.

4. Since this is a in-memory implemenation. Unit tests can serve as functional tests. All test cases in requirements document is implemented in a single unit test. See AdsManagerTest::demoTest

5. Code coverage - Line coverage 82%, class coverage 93%, method coverage 83%. Actual coverage could be higher if POJOs can be excluded.
   Overall coverage report https://github.com/raghuramanr/adslite/blob/main/CoverageReportSummary.pdf
   For detailed view - Open coverageReport/index.html in your local machine.
   Current report is available in https://github.com/raghuramanr/adslite/htmlreport/index.html

6. Snapshots of AdServer commands and results. https://github.com/raghuramanr/adslite/blob/main/RequirementsCommandsAndResults.png

7. POST "http://localhost:8000/campaign" return 201 (CREATED HTTP_STATUS) when SUCCESSFUL.

## Potential for further improvements


## Setup instructions
1. Install jdk-19 and maven 3.9.1
2. cd to your parent project dir.
3. git clone https://github.com/raghuramanr/adslite.git
4. cd adslite
5. mvn clean install
6. Start server on port 8000
   ./mvnw spring-boot:run

   
## Prerequisites

1. Apache Maven 3.9.1 (2e178502fcdbffc201671fb2537d0cb4b4cc58f8)
2. Java version: 19.0.2, vendor: Oracle Corporation, runtime: /Library/Java/JavaVirtualMachines/jdk-19.jdk/Contents/Home
