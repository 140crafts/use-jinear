package co.jinear.core.converter.integration;

import co.jinear.core.model.dto.integration.IntegrationInfoDto;
import co.jinear.core.model.entity.integration.IntegrationInfo;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface IntegrationInfoDtoConverter {

    IntegrationInfoDto map(IntegrationInfo integrationInfo);
}
