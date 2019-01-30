package com.factly.dega.domain;

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
 * A Factcheck.
 */
@Document(collection = "factcheck")
@org.springframework.data.elasticsearch.annotations.Document(indexName = "factcheck")
public class Factcheck implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    private String id;

    @NotNull
    @Field("title")
    private String title;

    @NotNull
    @Field("client_id")
    private String clientId;

    @Field("introduction")
    private String introduction;

    @NotNull
    @Field("summary")
    private String summary;

    @Field("excerpt")
    private String excerpt;

    @NotNull
    @Field("published_date")
    private ZonedDateTime publishedDate;

    @NotNull
    @Field("last_updated_date")
    private ZonedDateTime lastUpdatedDate;

    @Field("featured")
    private Boolean featured;

    @Field("sticky")
    private Boolean sticky;

    @Field("updates")
    private String updates;

    @NotNull
    @Field("slug")
    private String slug;

    @Field("password")
    private String password;

    @Field("featured_media")
    private String featuredMedia;

    @Field("sub_title")
    private String subTitle;

    @NotNull
    @Field("created_date")
    private ZonedDateTime createdDate;

    @DBRef
    @Field("claims")
    private Set<Claim> claims = new HashSet<>();

    @DBRef(db="core")
    @Field("tags")
    private Set<Tag> tags = new HashSet<>();

    @DBRef(db="core")
    @Field("categories")
    private Set<Category> categories = new HashSet<>();

    @DBRef(db="core")
    @Field("degaUsers")
    private Set<DegaUser> degaUsers = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public Factcheck title(String title) {
        this.title = title;
        return this;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getClientId() {
        return clientId;
    }

    public Factcheck clientId(String clientId) {
        this.clientId = clientId;
        return this;
    }

    public void setClientId(String clientId) {
        this.clientId = clientId;
    }

    public String getIntroduction() {
        return introduction;
    }

    public Factcheck introduction(String introduction) {
        this.introduction = introduction;
        return this;
    }

    public void setIntroduction(String introduction) {
        this.introduction = introduction;
    }

    public String getSummary() {
        return summary;
    }

    public Factcheck summary(String summary) {
        this.summary = summary;
        return this;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public String getExcerpt() {
        return excerpt;
    }

    public Factcheck excerpt(String excerpt) {
        this.excerpt = excerpt;
        return this;
    }

    public void setExcerpt(String excerpt) {
        this.excerpt = excerpt;
    }

    public ZonedDateTime getPublishedDate() {
        return publishedDate;
    }

    public Factcheck publishedDate(ZonedDateTime publishedDate) {
        this.publishedDate = publishedDate;
        return this;
    }

    public void setPublishedDate(ZonedDateTime publishedDate) {
        this.publishedDate = publishedDate;
    }

    public ZonedDateTime getLastUpdatedDate() {
        return lastUpdatedDate;
    }

    public Factcheck lastUpdatedDate(ZonedDateTime lastUpdatedDate) {
        this.lastUpdatedDate = lastUpdatedDate;
        return this;
    }

    public void setLastUpdatedDate(ZonedDateTime lastUpdatedDate) {
        this.lastUpdatedDate = lastUpdatedDate;
    }

    public Boolean isFeatured() {
        return featured;
    }

    public Factcheck featured(Boolean featured) {
        this.featured = featured;
        return this;
    }

    public void setFeatured(Boolean featured) {
        this.featured = featured;
    }

    public Boolean isSticky() {
        return sticky;
    }

    public Factcheck sticky(Boolean sticky) {
        this.sticky = sticky;
        return this;
    }

    public void setSticky(Boolean sticky) {
        this.sticky = sticky;
    }

    public String getUpdates() {
        return updates;
    }

    public Factcheck updates(String updates) {
        this.updates = updates;
        return this;
    }

    public void setUpdates(String updates) {
        this.updates = updates;
    }

    public String getSlug() {
        return slug;
    }

    public Factcheck slug(String slug) {
        this.slug = slug;
        return this;
    }

    public void setSlug(String slug) {
        this.slug = slug;
    }

    public String getPassword() {
        return password;
    }

    public Factcheck password(String password) {
        this.password = password;
        return this;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getFeaturedMedia() {
        return featuredMedia;
    }

    public Factcheck featuredMedia(String featuredMedia) {
        this.featuredMedia = featuredMedia;
        return this;
    }

    public void setFeaturedMedia(String featuredMedia) {
        this.featuredMedia = featuredMedia;
    }

    public String getSubTitle() {
        return subTitle;
    }

    public Factcheck subTitle(String subTitle) {
        this.subTitle = subTitle;
        return this;
    }

    public void setSubTitle(String subTitle) {
        this.subTitle = subTitle;
    }

    public ZonedDateTime getCreatedDate() {
        return createdDate;
    }

    public Factcheck createdDate(ZonedDateTime createdDate) {
        this.createdDate = createdDate;
        return this;
    }

    public void setCreatedDate(ZonedDateTime createdDate) {
        this.createdDate = createdDate;
    }

    public Set<Claim> getClaims() {
        return claims;
    }

    public Factcheck claims(Set<Claim> claims) {
        this.claims = claims;
        return this;
    }

    public Factcheck addClaim(Claim claim) {
        this.claims.add(claim);
        claim.getFactchecks().add(this);
        return this;
    }

    public Factcheck removeClaim(Claim claim) {
        this.claims.remove(claim);
        claim.getFactchecks().remove(this);
        return this;
    }

    public void setClaims(Set<Claim> claims) {
        this.claims = claims;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    public Set<Category> getCategories() {
        return categories;
    }

    public void setCategories(Set<Category> categories) {
        this.categories = categories;
    }

    public Set<Tag> getTags() {
        return tags;
    }

    public void setTags(Set<Tag> tags) {
        this.tags = tags;
    }

    public Set<DegaUser> getDegaUsers() {
        return degaUsers;
    }

    public void setDegaUsers(Set<DegaUser> degaUsers) {
        this.degaUsers = degaUsers;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Factcheck factcheck = (Factcheck) o;
        if (factcheck.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), factcheck.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "Factcheck{" +
            "id=" + getId() +
            ", title='" + getTitle() + "'" +
            ", clientId='" + getClientId() + "'" +
            ", introduction='" + getIntroduction() + "'" +
            ", summary='" + getSummary() + "'" +
            ", excerpt='" + getExcerpt() + "'" +
            ", publishedDate='" + getPublishedDate() + "'" +
            ", lastUpdatedDate='" + getLastUpdatedDate() + "'" +
            ", featured='" + isFeatured() + "'" +
            ", sticky='" + isSticky() + "'" +
            ", updates='" + getUpdates() + "'" +
            ", slug='" + getSlug() + "'" +
            ", password='" + getPassword() + "'" +
            ", featuredMedia='" + getFeaturedMedia() + "'" +
            ", subTitle='" + getSubTitle() + "'" +
            ", createdDate='" + getCreatedDate() + "'" +
            "}";
    }
}
