package co.jinear.core.converter.workspace;

import co.jinear.core.model.dto.workspace.WorkspaceSettingDto;
import co.jinear.core.model.entity.workspace.WorkspaceSetting;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface WorkspaceSettingConverter {
    WorkspaceSettingDto map(WorkspaceSetting workspaceSetting);
}
