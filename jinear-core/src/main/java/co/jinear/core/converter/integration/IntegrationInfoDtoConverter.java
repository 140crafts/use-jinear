package co.jinear.core.converter.integration;

import co.jinear.core.converter.google.GoogleUserInfoToDtoConverter;
import co.jinear.core.model.dto.integration.IntegrationInfoDto;
import co.jinear.core.model.entity.integration.IntegrationInfo;
import co.jinear.core.model.entity.integration.IntegrationScope;
import org.mapstruct.*;

import java.util.Optional;

@Mapper(componentModel = "spring", uses = GoogleUserInfoToDtoConverter.class, injectionStrategy = InjectionStrategy.CONSTRUCTOR)
public interface IntegrationInfoDtoConverter {

    @Mapping(target = "scopes", ignore = true)
    IntegrationInfoDto map(IntegrationInfo integrationInfo);

    @AfterMapping
    default void afterMap(@MappingTarget IntegrationInfoDto integrationInfoDto, IntegrationInfo integrationInfo) {
        mapScopes(integrationInfoDto, integrationInfo);
    }

    default void mapScopes(IntegrationInfoDto integrationInfoDto, IntegrationInfo integrationInfo) {
        Optional.of(integrationInfo)
                .map(IntegrationInfo::getScopes)
                .map(integrationScopes -> integrationScopes.stream().map(IntegrationScope::getScope).toList())
                .ifPresent(integrationInfoDto::setScopes);
    }
}
