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
 * A Claimant.
 */
@Document(collection = "claimant")
@org.springframework.data.elasticsearch.annotations.Document(indexName = "claimant")
public class Claimant implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    private String id;

    @NotNull
    @Field("name")
    private String name;

    @Field("tag_line")
    private String tagLine;

    @Field("description")
    private String description;

    @Field("image_url")
    private String imageURL;

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
    @Field("claim")
    private Set<Claim> claims = new HashSet<>();
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

    public Claimant name(String name) {
        this.name = name;
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTagLine() {
        return tagLine;
    }

    public Claimant tagLine(String tagLine) {
        this.tagLine = tagLine;
        return this;
    }

    public void setTagLine(String tagLine) {
        this.tagLine = tagLine;
    }

    public String getDescription() {
        return description;
    }

    public Claimant description(String description) {
        this.description = description;
        return this;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getImageURL() {
        return imageURL;
    }

    public Claimant imageURL(String imageURL) {
        this.imageURL = imageURL;
        return this;
    }

    public void setImageURL(String imageURL) {
        this.imageURL = imageURL;
    }

    public String getClientId() {
        return clientId;
    }

    public Claimant clientId(String clientId) {
        this.clientId = clientId;
        return this;
    }

    public void setClientId(String clientId) {
        this.clientId = clientId;
    }

    public String getSlug() {
        return slug;
    }

    public Claimant slug(String slug) {
        this.slug = slug;
        return this;
    }

    public void setSlug(String slug) {
        this.slug = slug;
    }

    public ZonedDateTime getCreatedDate() {
        return createdDate;
    }

    public Claimant createdDate(ZonedDateTime createdDate) {
        this.createdDate = createdDate;
        return this;
    }

    public void setCreatedDate(ZonedDateTime createdDate) {
        this.createdDate = createdDate;
    }

    public ZonedDateTime getLastUpdatedDate() {
        return lastUpdatedDate;
    }

    public Claimant lastUpdatedDate(ZonedDateTime lastUpdatedDate) {
        this.lastUpdatedDate = lastUpdatedDate;
        return this;
    }

    public void setLastUpdatedDate(ZonedDateTime lastUpdatedDate) {
        this.lastUpdatedDate = lastUpdatedDate;
    }

    public Set<Claim> getClaims() {
        return claims;
    }

    public Claimant claims(Set<Claim> claims) {
        this.claims = claims;
        return this;
    }

    public Claimant addClaim(Claim claim) {
        this.claims.add(claim);
        claim.setClaimant(this);
        return this;
    }

    public Claimant removeClaim(Claim claim) {
        this.claims.remove(claim);
        claim.setClaimant(null);
        return this;
    }

    public void setClaims(Set<Claim> claims) {
        this.claims = claims;
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
        Claimant claimant = (Claimant) o;
        if (claimant.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), claimant.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "Claimant{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", tagLine='" + getTagLine() + "'" +
            ", description='" + getDescription() + "'" +
            ", imageURL='" + getImageURL() + "'" +
            ", clientId='" + getClientId() + "'" +
            ", slug='" + getSlug() + "'" +
            ", createdDate='" + getCreatedDate() + "'" +
            ", lastUpdatedDate='" + getLastUpdatedDate() + "'" +
            "}";
    }
}
