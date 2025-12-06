package co.jinear.core.converter.material;

import co.jinear.core.model.request.material.MaterialInitializeFolderRequest;
import co.jinear.core.model.vo.material.MaterialFolderInitializeVo;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface MaterialInitializeFolderRequestToVoConverter {

    MaterialFolderInitializeVo convert(MaterialInitializeFolderRequest materialInitializeFolderRequest, String workspaceId, String ownerId);
}
