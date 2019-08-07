package com.factly.dega.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Field;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.DBRef;
import javax.validation.constraints.*;

import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;

/**
 * A Rating.
 */
@Document(collection = "rating")
@org.springframework.data.elasticsearch.annotations.Document(indexName = "rating")
public class Rating implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    private String id;

    @NotNull
    @Field("name")
    private String name;

    @NotNull
    @Min(value = 1)
    @Field("numeric_value")
    private Integer numericValue;

    @Field("icon_url")
    private String iconURL;

    @Field("is_default")
    private Boolean isDefault;

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

    @NotNull
    @Field("description")
    private String description;

    @DBRef
    @Field("claim")
    private Set<Claim> claims = new HashSet<>();

    @DBRef(db="core")
    @Field("media")
    private Media media;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public Rating name(String name) {
        this.name = name;
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getNumericValue() {
        return numericValue;
    }

    public Rating numericValue(Integer numericValue) {
        this.numericValue = numericValue;
        return this;
    }

    public void setNumericValue(Integer numericValue) {
        this.numericValue = numericValue;
    }

    public String getIconURL() {
        return iconURL;
    }

    public Rating iconURL(String iconURL) {
        this.iconURL = iconURL;
        return this;
    }

    public void setIconURL(String iconURL) {
        this.iconURL = iconURL;
    }

    public Boolean isIsDefault() {
        return isDefault;
    }

    public Rating isDefault(Boolean isDefault) {
        this.isDefault = isDefault;
        return this;
    }

    public void setIsDefault(Boolean isDefault) {
        this.isDefault = isDefault;
    }

    public String getClientId() {
        return clientId;
    }

    public Rating clientId(String clientId) {
        this.clientId = clientId;
        return this;
    }

    public void setClientId(String clientId) {
        this.clientId = clientId;
    }

    public String getSlug() {
        return slug;
    }

    public Rating slug(String slug) {
        this.slug = slug;
        return this;
    }

    public void setSlug(String slug) {
        this.slug = slug;
    }

    public ZonedDateTime getCreatedDate() {
        return createdDate;
    }

    public Rating createdDate(ZonedDateTime createdDate) {
        this.createdDate = createdDate;
        return this;
    }

    public void setCreatedDate(ZonedDateTime createdDate) {
        this.createdDate = createdDate;
    }

    public ZonedDateTime getLastUpdatedDate() {
        return lastUpdatedDate;
    }

    public Rating lastUpdatedDate(ZonedDateTime lastUpdatedDate) {
        this.lastUpdatedDate = lastUpdatedDate;
        return this;
    }

    public void setLastUpdatedDate(ZonedDateTime lastUpdatedDate) {
        this.lastUpdatedDate = lastUpdatedDate;
    }

    public String getDescription() {
        return description;
    }

    public Rating description(String description) {
        this.description = description;
        return this;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Set<Claim> getClaims() {
        return claims;
    }

    public Rating claims(Set<Claim> claims) {
        this.claims = claims;
        return this;
    }

    public Rating addClaim(Claim claim) {
        this.claims.add(claim);
        claim.setRating(this);
        return this;
    }

    public Rating removeClaim(Claim claim) {
        this.claims.remove(claim);
        claim.setRating(null);
        return this;
    }

    public void setClaims(Set<Claim> claims) {
        this.claims = claims;
    }

    public Media getMedia() {
        return media;
    }

    public void setMedia(Media media) {
        this.media = media;
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
        Rating rating = (Rating) o;
        if (rating.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), rating.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "Rating{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", numericValue=" + getNumericValue() +
            ", iconURL='" + getIconURL() + "'" +
            ", isDefault='" + isIsDefault() + "'" +
            ", clientId='" + getClientId() + "'" +
            ", slug='" + getSlug() + "'" +
            ", createdDate='" + getCreatedDate() + "'" +
            ", lastUpdatedDate='" + getLastUpdatedDate() + "'" +
            ", description='" + getDescription() + "'" +
            "}";
    }
}
