package co.jinear.core.converter.note;

import co.jinear.core.model.vo.note.NoteCreateVo;
import co.jinear.core.model.vo.note.NoteInitializeVo;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface NoteCreateVoConverter {

    @Mapping(target = "conversationId", ignore = true)
    NoteInitializeVo convert(NoteCreateVo vo, String ownerId, String bodyState);
}
