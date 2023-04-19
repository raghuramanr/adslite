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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Iterator;

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
                JSONObject jsonObject = new JSONObject();
                return new ResponseEntity<>(jsonObject, HttpStatus.OK);
            } else return new ResponseEntity<>(adResponse,HttpStatus.OK);
        } catch (BusinessException e) {
            JSONObject jsonObject = new JSONObject();
            return new ResponseEntity<>(jsonObject, HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    @PostMapping(value = "/campaign")
    public ResponseEntity<Object> createCampaign(@RequestBody Campaign campaign)
    {
        try {
            if (campaign == null) {
                JSONObject jsonObject = new JSONObject();
                return new ResponseEntity<>(jsonObject,HttpStatus.BAD_REQUEST);
            }
            CampaignResponse campaignResponse = campaignManager.create(campaign.getMaxImpression(), campaign.getCpm(), campaign.getTargetKeywords(), campaign.getStartTimestamp(), campaign.getEndTimestamp());
            return new ResponseEntity<>(campaignResponse,HttpStatus.CREATED);
        } catch (BusinessException e) {
            JSONObject jsonObject = new JSONObject();
            return new ResponseEntity<>(jsonObject, HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    @GetMapping("/{shortUrl}")
    public ResponseEntity<Object> record(@PathVariable String shortUrl)
    {
        try {
            boolean status = impressionRecorder.recordImpression(shortUrl);
            if (!status) {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
            JSONObject jsonObject = new JSONObject();
            return new ResponseEntity<>(jsonObject, HttpStatus.OK);
        } catch (BusinessException e) {
            JSONObject jsonObject = new JSONObject();
            return new ResponseEntity<>(jsonObject, HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }
}