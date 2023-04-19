package com.raghu.adslite.restservice.impl;

import com.raghu.adslite.business.AdsManager;
import com.raghu.adslite.business.CampaignManager;
import com.raghu.adslite.business.ImpressionRecorder;
import com.raghu.adslite.enums.CampaignValidationStatus;
import com.raghu.adslite.exception.BusinessException;
import com.raghu.adslite.objects.AdRequest;
import com.raghu.adslite.objects.AdResponse;
import com.raghu.adslite.objects.Campaign;
import com.raghu.adslite.objects.CampaignResponse;
import org.json.JSONObject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.util.Assert;

import java.util.Arrays;

@ExtendWith(MockitoExtension.class)
public class AdsLiteServiceImplTest {
    @InjectMocks
    private AdsLiteServiceImpl adsLiteServiceImpl;

    @Mock
    private AdsManager adsManager;

    @Mock
    private CampaignManager campaignManager;

    @Mock
    private ImpressionRecorder impressionRecorder;

    private String SHORT_URL = "http://localhost:8000/abc";

    @BeforeEach
    public void setup() {
        //if we don't call below, we will get NullPointerException
        MockitoAnnotations.initMocks(this);
    }
    @Test
    void contextLoads() {

    }

    @Test
    void getAdsCampaignSuccessTest() throws BusinessException {
        AdRequest adRequest = new AdRequest();
        adRequest.setKeywords(Arrays.asList("iphone", "5G"));
        AdResponse adResponse = new AdResponse();
        adResponse.setCampaignId(1001L);
        adResponse.setImpressionUrl("http://localhost:8000/");
        Mockito.when(adsManager.getAd(adRequest.getKeywords())).thenReturn(adResponse);

        ResponseEntity<Object> adResponseResponseEntity = adsLiteServiceImpl.getAds(adRequest);
        Object object = adResponseResponseEntity.getBody();
        Assert.isTrue(object instanceof AdResponse, "Valid AsResponse");
        AdResponse adResponseActual = (AdResponse) object;
        HttpStatusCode httpStatus = adResponseResponseEntity.getStatusCode();
        Assert.isTrue(httpStatus.equals(HttpStatusCode.valueOf(200)), "Bad Request");

        Assert.isTrue(adResponseActual.getCampaignId() == adResponse.getCampaignId(), "Campaign id valid.");
        Assert.isTrue(adResponseActual.getImpressionUrl().equals(adResponse.getImpressionUrl()), "Impression url valid.");
    }

    @Test
    void getAdsNoCampaignSuccessTest() throws BusinessException {
        AdRequest adRequest = new AdRequest();
        adRequest.setKeywords(Arrays.asList("iphone", "5G"));
        Mockito.when(adsManager.getAd(adRequest.getKeywords())).thenReturn(null);
        ResponseEntity<Object> adResponseResponseEntity = adsLiteServiceImpl.getAds(adRequest);
        Object object = adResponseResponseEntity.getBody();
        Assert.isTrue(object instanceof JSONObject, "Valid AsResponse");
        JSONObject adResponseActual = (JSONObject) object;
        Assert.isTrue(adResponseActual.isEmpty(), "No campaign.");

        HttpStatusCode httpStatus = adResponseResponseEntity.getStatusCode();
        Assert.isTrue(httpStatus.equals(HttpStatusCode.valueOf(200)), "Success");
    }

    @Test
    void getAdsBusinessExceptionTest() throws BusinessException {
        AdRequest adRequest = new AdRequest();
        adRequest.setKeywords(Arrays.asList("iphone@", "5G"));
        Mockito.when(adsManager.getAd(adRequest.getKeywords())).thenThrow(new BusinessException(CampaignValidationStatus.KEYOWRD_INVALID, "Error"));
        ResponseEntity<Object> adResponseResponseEntity = adsLiteServiceImpl.getAds(adRequest);

        HttpStatusCode httpStatus = adResponseResponseEntity.getStatusCode();
        Assert.isTrue(httpStatus.equals(HttpStatusCode.valueOf(400)), "Bad Request");
        Object objectActual = adResponseResponseEntity.getBody();
        Assert.isTrue(objectActual instanceof JSONObject, "JSONObject.");
        Assert.isTrue(((JSONObject) objectActual).isEmpty(), "No body.");
    }

    @Test
    void getAdsInternalServiceErrorTest() throws BusinessException {
        AdRequest adRequest = new AdRequest();
        adRequest.setKeywords(Arrays.asList("iphone@", "5G"));
        Mockito.when(adsManager.getAd(adRequest.getKeywords())).thenThrow(new RuntimeException("Error"));
        ResponseEntity<Object> adResponseResponseEntity = adsLiteServiceImpl.getAds(adRequest);

        HttpStatusCode httpStatus = adResponseResponseEntity.getStatusCode();
        Assert.isTrue(httpStatus.equals(HttpStatusCode.valueOf(500)), "Internal service error.");
        Assert.isNull(adResponseResponseEntity.getBody(), "Error.");
    }

    @Test
    void createCampaignSuccessTest() throws BusinessException {
        Campaign campaign = new Campaign();
        campaign.setCpm(10.0f);
        campaign.setTargetKeywords(Arrays.asList("iphone", "5G"));
        campaign.setMaxImpression(10);
        campaign.setStartTimestamp(1000L);
        campaign.setEndTimestamp(2000L);

        CampaignResponse campaignResponse = new CampaignResponse();
        campaignResponse.setCampaignId(1001L);

        Mockito.when(campaignManager.create(campaign.getMaxImpression(), campaign.getCpm(), campaign.getTargetKeywords(), campaign.getStartTimestamp(), campaign.getEndTimestamp())).thenReturn(campaignResponse);
        ResponseEntity<Object> campaignResponseResponseEntity = adsLiteServiceImpl.createCampaign(campaign);
        Object object = campaignResponseResponseEntity.getBody();
        Assert.isTrue(object instanceof CampaignResponse, "Valid response");
        CampaignResponse campaignResponseActual = (CampaignResponse) object;

        HttpStatusCode httpStatus = campaignResponseResponseEntity.getStatusCode();
        Assert.isTrue(httpStatus.equals(HttpStatusCode.valueOf(201)), "Success");
        Assert.isTrue(campaignResponseActual.getCampaignId() == campaignResponse.getCampaignId(), "No campaign.");
    }

    @Test
    void createCampaignForNullInputTest() {
        ResponseEntity<Object> campaignResponseResponseEntity = adsLiteServiceImpl.createCampaign(null);
        Object object = campaignResponseResponseEntity.getBody();
        Assert.isTrue(object instanceof JSONObject, "Valid response");
        HttpStatusCode httpStatus = campaignResponseResponseEntity.getStatusCode();
        Assert.isTrue(httpStatus.equals(HttpStatusCode.valueOf(400)), "Success");
        Assert.isTrue(((JSONObject) object).isEmpty(), "No campaign.");
    }

    @Test
    void createCampaignBadRequestTest() throws BusinessException {
        Campaign campaign = new Campaign();
        campaign.setCpm(0f);
        campaign.setTargetKeywords(Arrays.asList("iphone", "5G"));
        campaign.setMaxImpression(10);
        campaign.setStartTimestamp(1000L);
        campaign.setEndTimestamp(2000L);

        CampaignResponse campaignResponse = new CampaignResponse();
        campaignResponse.setCampaignId(1001L);

        Mockito.when(campaignManager.create(campaign.getMaxImpression(), campaign.getCpm(), campaign.getTargetKeywords(), campaign.getStartTimestamp(), campaign.getEndTimestamp())).thenThrow(new BusinessException(CampaignValidationStatus.CPM_INVALID, "Error"));
        ResponseEntity<Object> campaignResponseResponseEntity = adsLiteServiceImpl.createCampaign(campaign);
        Object object = campaignResponseResponseEntity.getBody();
        Assert.isTrue(object instanceof JSONObject, "Valid response");
        HttpStatusCode httpStatus = campaignResponseResponseEntity.getStatusCode();
        Assert.isTrue(httpStatus.equals(HttpStatusCode.valueOf(400)), "Success");
        Assert.isTrue(((JSONObject) object).isEmpty(), "No campaign.");
    }

    @Test
    void createCampaignInternalErrorTest() throws BusinessException {
        Campaign campaign = new Campaign();
        campaign.setCpm(10.0f);
        campaign.setTargetKeywords(Arrays.asList("iphone", "5G"));
        campaign.setMaxImpression(10);
        campaign.setStartTimestamp(1000L);
        campaign.setEndTimestamp(2000L);

        CampaignResponse campaignResponse = new CampaignResponse();
        campaignResponse.setCampaignId(1001L);

        Mockito.when(campaignManager.create(campaign.getMaxImpression(), campaign.getCpm(), campaign.getTargetKeywords(), campaign.getStartTimestamp(), campaign.getEndTimestamp())).thenThrow(new RuntimeException("Error"));
        ResponseEntity<Object> campaignResponseResponseEntity = adsLiteServiceImpl.createCampaign(campaign);
        Object object = campaignResponseResponseEntity.getBody();
        HttpStatusCode httpStatus = campaignResponseResponseEntity.getStatusCode();
        Assert.isTrue(httpStatus.equals(HttpStatusCode.valueOf(500)), "Error");
        Assert.isNull(object, "Null response.");
    }

    @Test
    void recordImpressionSuccessTest() throws BusinessException {
        Mockito.when(impressionRecorder.recordImpression(SHORT_URL)).thenReturn(true);
        ResponseEntity<Object> responseEntity = adsLiteServiceImpl.record(SHORT_URL);
        Object objectActual = responseEntity.getBody();
        HttpStatusCode httpStatus = responseEntity.getStatusCode();
        Assert.isTrue(httpStatus.equals(HttpStatusCode.valueOf(200)), "Success");
        Assert.isTrue(objectActual instanceof JSONObject, "JSONObject.");
        Assert.isTrue(((JSONObject) objectActual).isEmpty(), "No body.");
    }

    @Test
    void recordImpressionFailTest() throws BusinessException {
        Mockito.when(impressionRecorder.recordImpression(SHORT_URL)).thenReturn(false);
        ResponseEntity<Object> responseEntity = adsLiteServiceImpl.record(SHORT_URL);
        Object objectActual = responseEntity.getBody();
        HttpStatusCode httpStatus = responseEntity.getStatusCode();
        Assert.isTrue(httpStatus.equals(HttpStatusCode.valueOf(404)), "Success");
        Assert.isNull(objectActual, "No body.");
    }

    @Test
    void recordImpressionBusinessExceptionTest() throws BusinessException {
        Mockito.when(impressionRecorder.recordImpression(SHORT_URL)).thenThrow(new BusinessException(CampaignValidationStatus.SHORT_URL_NOT_FOUND, "Error"));
        ResponseEntity<Object> responseEntity = adsLiteServiceImpl.record(SHORT_URL);
        Object objectActual = responseEntity.getBody();
        HttpStatusCode httpStatus = responseEntity.getStatusCode();
        Assert.isTrue(httpStatus.equals(HttpStatusCode.valueOf(400)), "Success");
        Assert.isTrue(objectActual instanceof JSONObject, "JSONObject.");
        Assert.isTrue(((JSONObject) objectActual).isEmpty(), "No body.");
    }

    @Test
    void recordImpressionInternalErrorTest() throws BusinessException {
        Mockito.when(impressionRecorder.recordImpression(SHORT_URL)).thenThrow(new RuntimeException("Error"));
        ResponseEntity<Object> responseEntity = adsLiteServiceImpl.record(SHORT_URL);
        Object objectActual = responseEntity.getBody();
        HttpStatusCode httpStatus = responseEntity.getStatusCode();
        Assert.isTrue(httpStatus.equals(HttpStatusCode.valueOf(500)), "Success");
        Assert.isNull(objectActual, "No body.");
    }

}
