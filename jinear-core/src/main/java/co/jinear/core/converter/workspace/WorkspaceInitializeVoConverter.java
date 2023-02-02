package co.jinear.core.converter.workspace;

import co.jinear.core.model.request.workspace.WorkspaceInitializeRequest;
import co.jinear.core.model.vo.workspace.WorkspaceInitializeVo;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface WorkspaceInitializeVoConverter {

//    @Mapping(source = "ownerId",target = "")
    WorkspaceInitializeVo map(WorkspaceInitializeRequest workspaceInitializeRequest,String ownerId);
}
