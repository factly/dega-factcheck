package com.factly.dega.service.mapper;

import com.factly.dega.domain.*;
import com.factly.dega.service.dto.ClaimantDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity Claimant and its DTO ClaimantDTO.
 */
@Mapper(componentModel = "spring", uses = {})
public interface ClaimantMapper extends EntityMapper<ClaimantDTO, Claimant> {


    @Mapping(target = "claims", ignore = true)
    Claimant toEntity(ClaimantDTO claimantDTO);

    default Claimant fromId(String id) {
        if (id == null) {
            return null;
        }
        Claimant claimant = new Claimant();
        claimant.setId(id);
        return claimant;
    }
}
