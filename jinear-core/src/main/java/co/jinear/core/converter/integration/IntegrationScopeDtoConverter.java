package co.jinear.core.converter.integration;

import co.jinear.core.model.dto.integration.IntegrationScopeDto;
import co.jinear.core.model.entity.integration.IntegrationScope;
import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring", injectionStrategy = InjectionStrategy.CONSTRUCTOR, uses = IntegrationInfoDtoConverter.class)
public interface IntegrationScopeDtoConverter {

    IntegrationScopeDto map(IntegrationScope integrationScope);
}
