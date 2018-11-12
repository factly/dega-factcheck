package com.factly.dega.service.mapper;

import com.factly.dega.domain.*;
import com.factly.dega.service.dto.ClaimDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity Claim and its DTO ClaimDTO.
 */
@Mapper(componentModel = "spring", uses = {RatingMapper.class, ClaimantMapper.class})
public interface ClaimMapper extends EntityMapper<ClaimDTO, Claim> {

    @Mapping(source = "rating.id", target = "ratingId")
    @Mapping(source = "rating.name", target = "ratingName")
    @Mapping(source = "claimant.id", target = "claimantId")
    @Mapping(source = "claimant.name", target = "claimantName")
    ClaimDTO toDto(Claim claim);

    @Mapping(source = "ratingId", target = "rating")
    @Mapping(source = "claimantId", target = "claimant")
    @Mapping(target = "factChecks", ignore = true)
    Claim toEntity(ClaimDTO claimDTO);

    default Claim fromId(String id) {
        if (id == null) {
            return null;
        }
        Claim claim = new Claim();
        claim.setId(id);
        return claim;
    }
}
