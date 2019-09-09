package com.factly.dega.service.mapper;

import com.factly.dega.domain.*;
import com.factly.dega.service.dto.RatingDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity Rating and its DTO RatingDTO.
 */
@Mapper(componentModel = "spring", uses = {MediaMapper.class})
public interface RatingMapper extends EntityMapper<RatingDTO, Rating> {


    @Mapping(target = "claims", ignore = true)
    @Mapping(source = "media.id", target = "media")
    Rating toEntity(RatingDTO ratingDTO);

    @Mapping(source = "media", target = "media")
    RatingDTO toDto(Rating rating);

    default Rating fromId(String id) {
        if (id == null) {
            return null;
        }
        Rating rating = new Rating();
        rating.setId(id);
        return rating;
    }
}
