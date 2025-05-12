package co.jinear.core.converter.workspace;

import co.jinear.core.converter.team.TeamDtoConverter;
import co.jinear.core.model.dto.workspace.WorkspaceDisplayPreferenceDto;
import co.jinear.core.model.entity.workspace.WorkspaceDisplayPreference;
import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", injectionStrategy = InjectionStrategy.CONSTRUCTOR, uses = {TeamDtoConverter.class})
public interface WorkspaceDisplayPreferenceConverter {

    @Mapping(source = "workspace.username.username",target = "workspace.username")
    WorkspaceDisplayPreferenceDto map(WorkspaceDisplayPreference workspaceDisplayPreference);
}
