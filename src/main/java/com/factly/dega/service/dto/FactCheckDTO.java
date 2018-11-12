package com.factly.dega.service.dto;

import java.time.ZonedDateTime;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;

/**
 * A DTO for the FactCheck entity.
 */
public class FactCheckDTO implements Serializable {

    private String id;

    @NotNull
    private String title;

    @NotNull
    private String clientId;

    private String introduction;

    @NotNull
    private String summary;

    private String excerpt;

    @NotNull
    private ZonedDateTime publishedDate;

    @NotNull
    private ZonedDateTime publishedDateGMT;

    @NotNull
    private ZonedDateTime lastUpdatedDate;

    @NotNull
    private ZonedDateTime lastUpdatedDateGMT;

    private Boolean featured;

    private Boolean sticky;

    private String updates;

    @NotNull
    private String slug;

    private String password;

    private String featuredMedia;

    private String subTitle;

    private Set<ClaimDTO> claims = new HashSet<>();

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getClientId() {
        return clientId;
    }

    public void setClientId(String clientId) {
        this.clientId = clientId;
    }

    public String getIntroduction() {
        return introduction;
    }

    public void setIntroduction(String introduction) {
        this.introduction = introduction;
    }

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public String getExcerpt() {
        return excerpt;
    }

    public void setExcerpt(String excerpt) {
        this.excerpt = excerpt;
    }

    public ZonedDateTime getPublishedDate() {
        return publishedDate;
    }

    public void setPublishedDate(ZonedDateTime publishedDate) {
        this.publishedDate = publishedDate;
    }

    public ZonedDateTime getPublishedDateGMT() {
        return publishedDateGMT;
    }

    public void setPublishedDateGMT(ZonedDateTime publishedDateGMT) {
        this.publishedDateGMT = publishedDateGMT;
    }

    public ZonedDateTime getLastUpdatedDate() {
        return lastUpdatedDate;
    }

    public void setLastUpdatedDate(ZonedDateTime lastUpdatedDate) {
        this.lastUpdatedDate = lastUpdatedDate;
    }

    public ZonedDateTime getLastUpdatedDateGMT() {
        return lastUpdatedDateGMT;
    }

    public void setLastUpdatedDateGMT(ZonedDateTime lastUpdatedDateGMT) {
        this.lastUpdatedDateGMT = lastUpdatedDateGMT;
    }

    public Boolean isFeatured() {
        return featured;
    }

    public void setFeatured(Boolean featured) {
        this.featured = featured;
    }

    public Boolean isSticky() {
        return sticky;
    }

    public void setSticky(Boolean sticky) {
        this.sticky = sticky;
    }

    public String getUpdates() {
        return updates;
    }

    public void setUpdates(String updates) {
        this.updates = updates;
    }

    public String getSlug() {
        return slug;
    }

    public void setSlug(String slug) {
        this.slug = slug;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getFeaturedMedia() {
        return featuredMedia;
    }

    public void setFeaturedMedia(String featuredMedia) {
        this.featuredMedia = featuredMedia;
    }

    public String getSubTitle() {
        return subTitle;
    }

    public void setSubTitle(String subTitle) {
        this.subTitle = subTitle;
    }

    public Set<ClaimDTO> getClaims() {
        return claims;
    }

    public void setClaims(Set<ClaimDTO> claims) {
        this.claims = claims;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        FactCheckDTO factCheckDTO = (FactCheckDTO) o;
        if (factCheckDTO.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), factCheckDTO.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "FactCheckDTO{" +
            "id=" + getId() +
            ", title='" + getTitle() + "'" +
            ", clientId='" + getClientId() + "'" +
            ", introduction='" + getIntroduction() + "'" +
            ", summary='" + getSummary() + "'" +
            ", excerpt='" + getExcerpt() + "'" +
            ", publishedDate='" + getPublishedDate() + "'" +
            ", publishedDateGMT='" + getPublishedDateGMT() + "'" +
            ", lastUpdatedDate='" + getLastUpdatedDate() + "'" +
            ", lastUpdatedDateGMT='" + getLastUpdatedDateGMT() + "'" +
            ", featured='" + isFeatured() + "'" +
            ", sticky='" + isSticky() + "'" +
            ", updates='" + getUpdates() + "'" +
            ", slug='" + getSlug() + "'" +
            ", password='" + getPassword() + "'" +
            ", featuredMedia='" + getFeaturedMedia() + "'" +
            ", subTitle='" + getSubTitle() + "'" +
            "}";
    }
}
