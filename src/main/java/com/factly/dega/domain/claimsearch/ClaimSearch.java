package com.factly.dega.domain.claimsearch;

import java.util.List;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "claims",
    "nextPageToken"
})
public class ClaimSearch {

    @JsonProperty("claims")
    private List<Claim> claims = null;
    @JsonProperty("nextPageToken")
    private String nextPageToken;

    @JsonProperty("claims")
    public List<Claim> getClaims() {
        return claims;
    }

    @JsonProperty("claims")
    public void setClaims(List<Claim> claims) {
        this.claims = claims;
    }

    @JsonProperty("nextPageToken")
    public String getNextPageToken() {
        return nextPageToken;
    }

    @JsonProperty("nextPageToken")
    public void setNextPageToken(String nextPageToken) {
        this.nextPageToken = nextPageToken;
    }

}
