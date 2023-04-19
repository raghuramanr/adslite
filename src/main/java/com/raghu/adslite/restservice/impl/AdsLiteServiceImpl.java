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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@SpringBootApplication
@ComponentScan({"com.raghu.adslite"})
@RestController
public class AdsLiteServiceImpl
{
    @Autowired
    private AdsManager adsManager;

    @Autowired
    private CampaignManager campaignManager;

    @Autowired
    private ImpressionRecorder impressionRecorder;

    @PostMapping(value = "/addecision")
    public ResponseEntity<Object> getAds(@RequestBody AdRequest adRequest)
    {
        try {
            AdResponse adResponse = adsManager.getAd(adRequest.getKeywords());
            if (adResponse == null) {
                return new ResponseEntity<>(HttpStatus.OK);
            } else return new ResponseEntity<>(adResponse,HttpStatus.OK);
        } catch (BusinessException e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    @PostMapping(value = "/campaign")
    public ResponseEntity<CampaignResponse> createCampaign(@RequestBody Campaign campaign)
    {
        try {
            if (campaign == null) {
                throw new BusinessException(CampaignValidationStatus.CAMPAIGN_IS_NULL, "Campaign is null");
            }
            CampaignResponse campaignResponse = campaignManager.create(campaign.getMaxImpression(), campaign.getCpm(), campaign.getTargetKeywords(), campaign.getStartTimestamp(), campaign.getEndTimestamp());
            return new ResponseEntity<>(campaignResponse,HttpStatus.CREATED);
        } catch (BusinessException e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    @GetMapping("/{shortUrl}")
    public ResponseEntity<Object> record(@PathVariable String shortUrl)
    {
        try {
            if (shortUrl == null || shortUrl.isEmpty()) {
                throw new BusinessException(CampaignValidationStatus.SHORT_URL_IS_NULL, "shortUrl is null or empty.");
            }
            boolean status = impressionRecorder.recordImpression(shortUrl);
            if (!status) {
                throw new BusinessException(CampaignValidationStatus.IMPRESSION_NOT_FOUND, "Impression not recorded.");
            }
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (BusinessException e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }
}