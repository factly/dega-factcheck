package com.factly.dega.domain.claimsearch;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "publisher",
    "url",
    "title",
    "reviewDate",
    "textualRating",
    "languageCode"
})
public class ClaimReview {

    @JsonProperty("publisher")
    private Publisher publisher;
    @JsonProperty("url")
    private String url;
    @JsonProperty("title")
    private String title;
    @JsonProperty("reviewDate")
    private String reviewDate;
    @JsonProperty("textualRating")
    private String textualRating;
    @JsonProperty("languageCode")
    private String languageCode;

    @JsonProperty("publisher")
    public Publisher getPublisher() {
        return publisher;
    }

    @JsonProperty("publisher")
    public void setPublisher(Publisher publisher) {
        this.publisher = publisher;
    }

    @JsonProperty("url")
    public String getUrl() {
        return url;
    }

    @JsonProperty("url")
    public void setUrl(String url) {
        this.url = url;
    }

    @JsonProperty("title")
    public String getTitle() {
        return title;
    }

    @JsonProperty("title")
    public void setTitle(String title) {
        this.title = title;
    }

    @JsonProperty("reviewDate")
    public String getReviewDate() {
        return reviewDate;
    }

    @JsonProperty("reviewDate")
    public void setReviewDate(String reviewDate) {
        this.reviewDate = reviewDate;
    }

    @JsonProperty("textualRating")
    public String getTextualRating() {
        return textualRating;
    }

    @JsonProperty("textualRating")
    public void setTextualRating(String textualRating) {
        this.textualRating = textualRating;
    }

    @JsonProperty("languageCode")
    public String getLanguageCode() {
        return languageCode;
    }

    @JsonProperty("languageCode")
    public void setLanguageCode(String languageCode) {
        this.languageCode = languageCode;
    }

}
