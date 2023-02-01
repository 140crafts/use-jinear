package co.jinear.core.converter.workspace;

import co.jinear.core.model.dto.workspace.WorkspaceDto;
import co.jinear.core.model.entity.workspace.Workspace;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface WorkspaceDtoConverter {

    @Mapping(source = "username.username", target = "username")
    WorkspaceDto map(Workspace workspace);
}
