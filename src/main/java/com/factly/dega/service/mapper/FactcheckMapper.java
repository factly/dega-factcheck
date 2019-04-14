package com.factly.dega.service.mapper;

import com.factly.dega.domain.*;
import com.factly.dega.service.dto.FactcheckDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity Factcheck and its DTO FactcheckDTO.
 */
@Mapper(componentModel = "spring", uses = {ClaimMapper.class, StatusMapper.class})
public interface FactcheckMapper extends EntityMapper<FactcheckDTO, Factcheck> {

    @Mapping(source = "status.id", target = "statusID")
    @Mapping(source = "status.name", target = "statusName")
    FactcheckDTO toDto(Factcheck factcheck);

    @Mapping(source = "statusID", target = "status")
    Factcheck toEntity(FactcheckDTO factcheckDTO);

    default Factcheck fromId(String id) {
        if (id == null) {
            return null;
        }
        Factcheck factcheck = new Factcheck();
        factcheck.setId(id);
        return factcheck;
    }
}
