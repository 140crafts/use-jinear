package co.jinear.core.converter.notetag;

import co.jinear.core.model.entity.note.NoteTag;
import co.jinear.core.model.vo.notetag.NoteTagInitializeVo;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface NoteTagEntityConverter {

    NoteTag convert(NoteTagInitializeVo noteTagInitializeVo);
}
