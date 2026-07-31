package co.jinear.core.converter.note;

import co.jinear.core.model.entity.note.Note;
import co.jinear.core.model.vo.note.NoteInitializeVo;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface NoteEntityConverter {

    @Mapping(target = "richTextId", ignore = true)
    Note convert(NoteInitializeVo noteInitializeVo);
}
