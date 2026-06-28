package co.jinear.core.converter.note;

import co.jinear.core.model.request.note.NoteUpdateRequest;
import co.jinear.core.model.vo.note.NoteUpdateVo;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface NoteUpdateRequestToVoConverter {

    NoteUpdateVo convert(NoteUpdateRequest request);
}
