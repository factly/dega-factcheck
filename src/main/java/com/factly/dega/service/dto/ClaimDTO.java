package com.factly.dega.service.dto;

import java.time.LocalDate;
import java.time.ZonedDateTime;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the Claim entity.
 */
public class ClaimDTO implements Serializable {

    private String id;

    @NotNull
    private String claim;

    @NotNull
    private String description;

    private LocalDate claimDate;

    @NotNull
    private String claimSource;

    @NotNull
    private LocalDate checkedDate;

    @NotNull
    private String reviewSources;

    @NotNull
    private String review;

    private String reviewTagLine;

    private String clientId;

    @NotNull
    private String slug;

    private ZonedDateTime createdDate;

    private ZonedDateTime lastUpdatedDate;

    private String ratingId;

    private String ratingName;

    private String claimantId;

    private String claimantName;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getClaim() {
        return claim;
    }

    public void setClaim(String claim) {
        this.claim = claim;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public LocalDate getClaimDate() {
        return claimDate;
    }

    public void setClaimDate(LocalDate claimDate) {
        this.claimDate = claimDate;
    }

    public String getClaimSource() {
        return claimSource;
    }

    public void setClaimSource(String claimSource) {
        this.claimSource = claimSource;
    }

    public LocalDate getCheckedDate() {
        return checkedDate;
    }

    public void setCheckedDate(LocalDate checkedDate) {
        this.checkedDate = checkedDate;
    }

    public String getReviewSources() {
        return reviewSources;
    }

    public void setReviewSources(String reviewSources) {
        this.reviewSources = reviewSources;
    }

    public String getReview() {
        return review;
    }

    public void setReview(String review) {
        this.review = review;
    }

    public String getReviewTagLine() {
        return reviewTagLine;
    }

    public void setReviewTagLine(String reviewTagLine) {
        this.reviewTagLine = reviewTagLine;
    }

    public String getClientId() {
        return clientId;
    }

    public void setClientId(String clientId) {
        this.clientId = clientId;
    }

    public String getSlug() {
        return slug;
    }

    public void setSlug(String slug) {
        this.slug = slug;
    }

    public ZonedDateTime getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(ZonedDateTime createdDate) {
        this.createdDate = createdDate;
    }

    public ZonedDateTime getLastUpdatedDate() {
        return lastUpdatedDate;
    }

    public void setLastUpdatedDate(ZonedDateTime lastUpdatedDate) {
        this.lastUpdatedDate = lastUpdatedDate;
    }

    public String getRatingId() {
        return ratingId;
    }

    public void setRatingId(String ratingId) {
        this.ratingId = ratingId;
    }

    public String getRatingName() {
        return ratingName;
    }

    public void setRatingName(String ratingName) {
        this.ratingName = ratingName;
    }

    public String getClaimantId() {
        return claimantId;
    }

    public void setClaimantId(String claimantId) {
        this.claimantId = claimantId;
    }

    public String getClaimantName() {
        return claimantName;
    }

    public void setClaimantName(String claimantName) {
        this.claimantName = claimantName;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        ClaimDTO claimDTO = (ClaimDTO) o;
        if (claimDTO.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), claimDTO.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "ClaimDTO{" +
            "id=" + getId() +
            ", claim='" + getClaim() + "'" +
            ", description='" + getDescription() + "'" +
            ", claimDate='" + getClaimDate() + "'" +
            ", claimSource='" + getClaimSource() + "'" +
            ", checkedDate='" + getCheckedDate() + "'" +
            ", reviewSources='" + getReviewSources() + "'" +
            ", review='" + getReview() + "'" +
            ", reviewTagLine='" + getReviewTagLine() + "'" +
            ", clientId='" + getClientId() + "'" +
            ", slug='" + getSlug() + "'" +
            ", createdDate='" + getCreatedDate() + "'" +
            ", lastUpdatedDate='" + getLastUpdatedDate() + "'" +
            ", rating=" + getRatingId() +
            ", rating='" + getRatingName() + "'" +
            ", claimant=" + getClaimantId() +
            ", claimant='" + getClaimantName() + "'" +
            "}";
    }
}
