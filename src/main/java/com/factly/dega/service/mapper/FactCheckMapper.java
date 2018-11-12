package com.factly.dega.service.mapper;

import com.factly.dega.domain.*;
import com.factly.dega.service.dto.FactCheckDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity FactCheck and its DTO FactCheckDTO.
 */
@Mapper(componentModel = "spring", uses = {ClaimMapper.class})
public interface FactCheckMapper extends EntityMapper<FactCheckDTO, FactCheck> {



    default FactCheck fromId(String id) {
        if (id == null) {
            return null;
        }
        FactCheck factCheck = new FactCheck();
        factCheck.setId(id);
        return factCheck;
    }
}
