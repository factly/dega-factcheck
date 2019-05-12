package com.factly.dega.web.rest;

import com.factly.dega.domain.claimsearch.ClaimSearch;
import com.factly.dega.service.ClaimSearchService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class ClaimSearchResource {
    private final ClaimSearchService claimSearchService;

    public ClaimSearchResource(ClaimSearchService claimSearchService) {
        this.claimSearchService = claimSearchService;
    }

    @GetMapping("/claimsearch")
    public ClaimSearch claimSearch(@RequestParam("query") String query) {
        return claimSearchService.claimSearch(query);


    }

}
