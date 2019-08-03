package com.factly.dega.domain;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Field;

import javax.validation.constraints.NotNull;
import java.time.ZonedDateTime;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

/**
 * Created by ntalla on 8/2/19.
 */
public class FactcheckMedia {

    private static final long serialVersionUID = 1L;
    @Id
    private String id;
    @NotNull
    @Field("name")
    private String name;
    @NotNull
    @Field("type")
    private String type;
    @NotNull
    @Field("url")
    private String url;
    @Field("file_size")
    private String fileSize;
    @Field("dimensions")
    private String dimensions;
    @Field("title")
    private String title;
    @Field("caption")
    private String caption;
    @Field("alt_text")
    private String altText;
    @Field("description")
    private String description;
    @NotNull
    @Field("uploaded_by")
    private String uploadedBy;
    @NotNull
    @Field("published_date")
    private ZonedDateTime publishedDate;
    @NotNull
    @Field("last_updated_date")
    private ZonedDateTime lastUpdatedDate;
    @NotNull
    @Field("slug")
    private String slug;
    @NotNull
    @Field("client_id")
    private String clientId;
    @NotNull
    @Field("created_date")
    private ZonedDateTime createdDate;
    @Field("relative_url")
    private String relativeURL;
    @Field("source_url")
    private String sourceURL;

    public String getId() {
        return this.id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return this.name;
    }

    public FactcheckMedia name(String name) {
        this.name = name;
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return this.type;
    }

    public FactcheckMedia type(String type) {
        this.type = type;
        return this;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getUrl() {
        return this.url;
    }

    public FactcheckMedia url(String url) {
        this.url = url;
        return this;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getFileSize() {
        return this.fileSize;
    }

    public FactcheckMedia fileSize(String fileSize) {
        this.fileSize = fileSize;
        return this;
    }

    public void setFileSize(String fileSize) {
        this.fileSize = fileSize;
    }

    public String getDimensions() {
        return this.dimensions;
    }

    public FactcheckMedia dimensions(String dimensions) {
        this.dimensions = dimensions;
        return this;
    }

    public void setDimensions(String dimensions) {
        this.dimensions = dimensions;
    }

    public String getTitle() {
        return this.title;
    }

    public FactcheckMedia title(String title) {
        this.title = title;
        return this;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getCaption() {
        return this.caption;
    }

    public FactcheckMedia caption(String caption) {
        this.caption = caption;
        return this;
    }

    public void setCaption(String caption) {
        this.caption = caption;
    }

    public String getAltText() {
        return this.altText;
    }

    public FactcheckMedia altText(String altText) {
        this.altText = altText;
        return this;
    }

    public void setAltText(String altText) {
        this.altText = altText;
    }

    public String getDescription() {
        return this.description;
    }

    public FactcheckMedia description(String description) {
        this.description = description;
        return this;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getUploadedBy() {
        return this.uploadedBy;
    }

    public FactcheckMedia uploadedBy(String uploadedBy) {
        this.uploadedBy = uploadedBy;
        return this;
    }

    public void setUploadedBy(String uploadedBy) {
        this.uploadedBy = uploadedBy;
    }

    public ZonedDateTime getPublishedDate() {
        return this.publishedDate;
    }

    public FactcheckMedia publishedDate(ZonedDateTime publishedDate) {
        this.publishedDate = publishedDate;
        return this;
    }

    public void setPublishedDate(ZonedDateTime publishedDate) {
        this.publishedDate = publishedDate;
    }

    public ZonedDateTime getLastUpdatedDate() {
        return this.lastUpdatedDate;
    }

    public FactcheckMedia lastUpdatedDate(ZonedDateTime lastUpdatedDate) {
        this.lastUpdatedDate = lastUpdatedDate;
        return this;
    }

    public void setLastUpdatedDate(ZonedDateTime lastUpdatedDate) {
        this.lastUpdatedDate = lastUpdatedDate;
    }

    public String getSlug() {
        return this.slug;
    }

    public FactcheckMedia slug(String slug) {
        this.slug = slug;
        return this;
    }

    public void setSlug(String slug) {
        this.slug = slug;
    }

    public String getClientId() {
        return this.clientId;
    }

    public FactcheckMedia clientId(String clientId) {
        this.clientId = clientId;
        return this;
    }

    public void setClientId(String clientId) {
        this.clientId = clientId;
    }

    public ZonedDateTime getCreatedDate() {
        return this.createdDate;
    }

    public FactcheckMedia createdDate(ZonedDateTime createdDate) {
        this.createdDate = createdDate;
        return this;
    }

    public void setCreatedDate(ZonedDateTime createdDate) {
        this.createdDate = createdDate;
    }

    public String getRelativeURL() {
        return this.relativeURL;
    }

    public FactcheckMedia relativeURL(String relativeURL) {
        this.relativeURL = relativeURL;
        return this;
    }

    public void setRelativeURL(String relativeURL) {
        this.relativeURL = relativeURL;
    }

    public String getSourceURL() {
        return this.sourceURL;
    }

    public FactcheckMedia sourceURL(String sourceURL) {
        this.sourceURL = sourceURL;
        return this;
    }

    public void setSourceURL(String sourceURL) {
        this.sourceURL = sourceURL;
    }

    public boolean equals(Object o) {
        if(this == o) {
            return true;
        } else if(o != null && this.getClass() == o.getClass()) {
            FactcheckMedia media = (FactcheckMedia)o;
            return media.getId() != null && this.getId() != null? Objects.equals(this.getId(), media.getId()):false;
        } else {
            return false;
        }
    }

    public int hashCode() {
        return Objects.hashCode(this.getId());
    }

    public String toString() {
        return "Media{id=" + this.getId() + ", name='" + this.getName() + "', type='" + this.getType() + "', url='" + this.getUrl() + "', fileSize='" + this.getFileSize() + "', dimensions='" + this.getDimensions() + "', title='" + this.getTitle() + "', caption='" + this.getCaption() + "', altText='" + this.getAltText() + "', description='" + this.getDescription() + "', uploadedBy='" + this.getUploadedBy() + "', publishedDate='" + this.getPublishedDate() + "', lastUpdatedDate='" + this.getLastUpdatedDate() + "', slug='" + this.getSlug() + "', clientId='" + this.getClientId() + "', createdDate='" + this.getCreatedDate() + "', relativeURL='" + this.getRelativeURL() + "', sourceURL='" + this.getSourceURL() + "'}";
    }
}

