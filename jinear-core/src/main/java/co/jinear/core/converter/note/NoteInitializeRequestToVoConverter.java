package co.jinear.core.converter.note;

import co.jinear.core.model.request.note.NoteInitializeRequest;
import co.jinear.core.model.vo.note.NoteInitializeVo;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface NoteInitializeRequestToVoConverter {

    NoteInitializeVo convert(NoteInitializeRequest request, String workspaceId, String ownerId);
}
