package com.factly.dega.service.impl;

import com.factly.dega.domain.claimsearch.ClaimSearch;
import com.factly.dega.service.ClaimSearchService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

@Service
public class ClaimSearchServiceImpl implements ClaimSearchService {

    private final String claimSearchURL;
    private final String apikey;

    public ClaimSearchServiceImpl(@Value("${dega.claimsearch.url}") String claimSearchURL, @Value("${dega.claimsearch.apikey}") String apikey) {
        this.claimSearchURL = claimSearchURL;
        this.apikey = apikey;

    }

    @Override
    public ClaimSearch claimSearch(String querystring) {
        RestTemplate claimSearchRestTemplate = new RestTemplate();
        String url = claimSearchURL + "?query=" + querystring + "&key=" + apikey;
        try {
            ResponseEntity<ClaimSearch> claimsearch = claimSearchRestTemplate.getForEntity(url, ClaimSearch.class);
            return claimsearch.getBody();
        } catch (HttpClientErrorException ex) {
            return null;

        }
    }
}
