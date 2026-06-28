package co.jinear.core.converter.notebook;

import co.jinear.core.model.request.notebook.NotebookUpdateRequest;
import co.jinear.core.model.vo.notebook.NotebookUpdateVo;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface NotebookUpdateRequestToVoConverter {

    NotebookUpdateVo convert(NotebookUpdateRequest request);
}
