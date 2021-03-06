package com.factly.dega.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Field;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.DBRef;
import javax.validation.constraints.*;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.ZonedDateTime;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;

/**
 * A Claim.
 */
@Document(collection = "claim")
@org.springframework.data.elasticsearch.annotations.Document(indexName = "claim")
public class Claim implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    private String id;

    @NotNull
    @Field("claim")
    private String claim;

    @NotNull
    @Field("description")
    private String description;

    @Field("claim_date")
    private LocalDate claimDate;

    @NotNull
    @Field("claim_source")
    private String claimSource;

    @NotNull
    @Field("checked_date")
    private LocalDate checkedDate;

    @NotNull
    @Field("review_sources")
    private String reviewSources;

    @NotNull
    @Field("review")
    private String review;

    @Field("review_tag_line")
    private String reviewTagLine;

    @NotNull
    @Field("client_id")
    private String clientId;

    @NotNull
    @Field("slug")
    private String slug;

    @NotNull
    @Field("created_date")
    private ZonedDateTime createdDate;

    @NotNull
    @Field("last_updated_date")
    private ZonedDateTime lastUpdatedDate;

    @DBRef
    @Field("rating")
    @JsonIgnoreProperties("claims")
    private Rating rating;

    @DBRef
    @Field("claimant")
    @JsonIgnoreProperties("claims")
    private Claimant claimant;

    @DBRef
    @Field("factchecks")
    @JsonIgnore
    private Set<Factcheck> factchecks = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getClaim() {
        return claim;
    }

    public Claim claim(String claim) {
        this.claim = claim;
        return this;
    }

    public void setClaim(String claim) {
        this.claim = claim;
    }

    public String getDescription() {
        return description;
    }

    public Claim description(String description) {
        this.description = description;
        return this;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public LocalDate getClaimDate() {
        return claimDate;
    }

    public Claim claimDate(LocalDate claimDate) {
        this.claimDate = claimDate;
        return this;
    }

    public void setClaimDate(LocalDate claimDate) {
        this.claimDate = claimDate;
    }

    public String getClaimSource() {
        return claimSource;
    }

    public Claim claimSource(String claimSource) {
        this.claimSource = claimSource;
        return this;
    }

    public void setClaimSource(String claimSource) {
        this.claimSource = claimSource;
    }

    public LocalDate getCheckedDate() {
        return checkedDate;
    }

    public Claim checkedDate(LocalDate checkedDate) {
        this.checkedDate = checkedDate;
        return this;
    }

    public void setCheckedDate(LocalDate checkedDate) {
        this.checkedDate = checkedDate;
    }

    public String getReviewSources() {
        return reviewSources;
    }

    public Claim reviewSources(String reviewSources) {
        this.reviewSources = reviewSources;
        return this;
    }

    public void setReviewSources(String reviewSources) {
        this.reviewSources = reviewSources;
    }

    public String getReview() {
        return review;
    }

    public Claim review(String review) {
        this.review = review;
        return this;
    }

    public void setReview(String review) {
        this.review = review;
    }

    public String getReviewTagLine() {
        return reviewTagLine;
    }

    public Claim reviewTagLine(String reviewTagLine) {
        this.reviewTagLine = reviewTagLine;
        return this;
    }

    public void setReviewTagLine(String reviewTagLine) {
        this.reviewTagLine = reviewTagLine;
    }

    public String getClientId() {
        return clientId;
    }

    public Claim clientId(String clientId) {
        this.clientId = clientId;
        return this;
    }

    public void setClientId(String clientId) {
        this.clientId = clientId;
    }

    public String getSlug() {
        return slug;
    }

    public Claim slug(String slug) {
        this.slug = slug;
        return this;
    }

    public void setSlug(String slug) {
        this.slug = slug;
    }

    public ZonedDateTime getCreatedDate() {
        return createdDate;
    }

    public Claim createdDate(ZonedDateTime createdDate) {
        this.createdDate = createdDate;
        return this;
    }

    public void setCreatedDate(ZonedDateTime createdDate) {
        this.createdDate = createdDate;
    }

    public ZonedDateTime getLastUpdatedDate() {
        return lastUpdatedDate;
    }

    public Claim lastUpdatedDate(ZonedDateTime lastUpdatedDate) {
        this.lastUpdatedDate = lastUpdatedDate;
        return this;
    }

    public void setLastUpdatedDate(ZonedDateTime lastUpdatedDate) {
        this.lastUpdatedDate = lastUpdatedDate;
    }

    public Rating getRating() {
        return rating;
    }

    public Claim rating(Rating rating) {
        this.rating = rating;
        return this;
    }

    public void setRating(Rating rating) {
        this.rating = rating;
    }

    public Claimant getClaimant() {
        return claimant;
    }

    public Claim claimant(Claimant claimant) {
        this.claimant = claimant;
        return this;
    }

    public void setClaimant(Claimant claimant) {
        this.claimant = claimant;
    }

    public Set<Factcheck> getFactchecks() {
        return factchecks;
    }

    public Claim factchecks(Set<Factcheck> factchecks) {
        this.factchecks = factchecks;
        return this;
    }

    public Claim addFactcheck(Factcheck factcheck) {
        this.factchecks.add(factcheck);
        factcheck.getClaims().add(this);
        return this;
    }

    public Claim removeFactcheck(Factcheck factcheck) {
        this.factchecks.remove(factcheck);
        factcheck.getClaims().remove(this);
        return this;
    }

    public void setFactchecks(Set<Factcheck> factchecks) {
        this.factchecks = factchecks;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Claim claim = (Claim) o;
        if (claim.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), claim.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "Claim{" +
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
            "}";
    }
}
