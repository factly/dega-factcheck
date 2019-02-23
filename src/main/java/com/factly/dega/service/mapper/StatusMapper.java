package com.factly.dega.service.mapper;

import com.factly.dega.domain.Status;
import com.factly.dega.service.dto.StatusDTO;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring", uses = {})
public interface StatusMapper extends EntityMapper<StatusDTO, Status> {
    default Status fromId(String id) {
        if (id == null) {
            return null;
        }
        Status status = new Status();
        status.setId(id);
        return status;
    }
}
