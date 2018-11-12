package com.factly.dega.service.dto;

import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the Claimant entity.
 */
public class ClaimantDTO implements Serializable {

    private String id;

    @NotNull
    private String name;

    private String tagLine;

    private String description;

    private String imageURL;

    @NotNull
    private String clientId;

    @NotNull
    private String slug;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTagLine() {
        return tagLine;
    }

    public void setTagLine(String tagLine) {
        this.tagLine = tagLine;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getImageURL() {
        return imageURL;
    }

    public void setImageURL(String imageURL) {
        this.imageURL = imageURL;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        ClaimantDTO claimantDTO = (ClaimantDTO) o;
        if (claimantDTO.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), claimantDTO.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "ClaimantDTO{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", tagLine='" + getTagLine() + "'" +
            ", description='" + getDescription() + "'" +
            ", imageURL='" + getImageURL() + "'" +
            ", clientId='" + getClientId() + "'" +
            ", slug='" + getSlug() + "'" +
            "}";
    }
}
