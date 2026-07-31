package co.jinear.core.converter.notetag;

import co.jinear.core.model.request.notetag.NoteTagInitializeRequest;
import co.jinear.core.model.vo.notetag.NoteTagInitializeVo;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface NoteTagInitializeRequestToVoConverter {

    NoteTagInitializeVo convert(NoteTagInitializeRequest request, String workspaceId);
}
