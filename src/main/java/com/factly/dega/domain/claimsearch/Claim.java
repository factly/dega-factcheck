package com.factly.dega.domain.claimsearch;

import java.util.List;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "text",
    "claimant",
    "claimDate",
    "claimReview"
})
public class Claim {

    @JsonProperty("text")
    private String text;
    @JsonProperty("claimant")
    private String claimant;
    @JsonProperty("claimDate")
    private String claimDate;
    @JsonProperty("claimReview")
    private List<ClaimReview> claimReview = null;

    @JsonProperty("text")
    public String getText() {
        return text;
    }

    @JsonProperty("text")
    public void setText(String text) {
        this.text = text;
    }

    @JsonProperty("claimant")
    public String getClaimant() {
        return claimant;
    }

    @JsonProperty("claimant")
    public void setClaimant(String claimant) {
        this.claimant = claimant;
    }

    @JsonProperty("claimDate")
    public String getClaimDate() {
        return claimDate;
    }

    @JsonProperty("claimDate")
    public void setClaimDate(String claimDate) {
        this.claimDate = claimDate;
    }

    @JsonProperty("claimReview")
    public List<ClaimReview> getClaimReview() {
        return claimReview;
    }

    @JsonProperty("claimReview")
    public void setClaimReview(List<ClaimReview> claimReview) {
        this.claimReview = claimReview;
    }

}
