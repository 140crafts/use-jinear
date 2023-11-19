package co.jinear.core.converter.integration;

import co.jinear.core.model.dto.integration.IntegrationScopeDto;
import co.jinear.core.model.entity.integration.IntegrationScope;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface IntegrationScopeDtoConverter {

    IntegrationScopeDto map(IntegrationScope integrationScope);
}
