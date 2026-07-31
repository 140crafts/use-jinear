package co.jinear.core.converter.notebook;

import co.jinear.core.model.entity.note.Notebook;
import co.jinear.core.model.vo.notebook.NotebookInitializeVo;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface NotebookEntityConverter {

    Notebook convert(NotebookInitializeVo notebookInitializeVo);
}
