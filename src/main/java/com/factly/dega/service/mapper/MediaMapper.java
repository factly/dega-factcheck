package com.factly.dega.service.mapper;

import com.factly.dega.domain.*;
import com.factly.dega.service.dto.MediaDTO;

import com.factly.dega.service.mapper.EntityMapper;
import org.mapstruct.*;

/**
 * Mapper for the entity Media and its DTO MediaDTO.
 */
@Mapper(componentModel = "spring", uses = {})
public interface MediaMapper extends EntityMapper<MediaDTO, FactcheckMedia> {

    default FactcheckMedia fromId(String id) {
        if (id == null) {
            return null;
        }
        FactcheckMedia media = new FactcheckMedia();
        media.setId(id);
        return media;
    }
}
