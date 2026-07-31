package co.jinear.core.converter.notebook;

import co.jinear.core.model.request.notebook.NotebookInitializeRequest;
import co.jinear.core.model.vo.notebook.NotebookInitializeVo;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface NotebookInitializeRequestToVoConverter {

    NotebookInitializeVo convert(NotebookInitializeRequest request, String workspaceId, String ownerId);
}
