package com.raghu.adslite.restservice;


import com.raghu.adslite.objects.AdRequest;
import com.raghu.adslite.objects.AdResponse;
import org.springframework.web.bind.annotation.RequestBody;

public interface AdsLiteService {
        public AdResponse getAds(@RequestBody AdRequest adRequest);
}
