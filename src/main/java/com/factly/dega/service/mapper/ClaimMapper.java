package com.factly.dega.service.mapper;

import com.factly.dega.domain.*;
import com.factly.dega.service.dto.ClaimDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity Claim and its DTO ClaimDTO.
 */
@Mapper(componentModel = "spring", uses = {})
public interface ClaimMapper extends EntityMapper<ClaimDTO, Claim> {



    default Claim fromId(String id) {
        if (id == null) {
            return null;
        }
        Claim claim = new Claim();
        claim.setId(id);
        return claim;
    }
}
