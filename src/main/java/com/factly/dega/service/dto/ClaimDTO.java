package com.factly.dega.service.dto;

import java.time.LocalDate;
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

    @NotNull
    private String clientId;

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
            "}";
    }
}
