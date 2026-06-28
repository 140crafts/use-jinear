package co.jinear.core.converter.notetag;

import co.jinear.core.model.request.notetag.NoteTagUpdateRequest;
import co.jinear.core.model.vo.notetag.NoteTagUpdateVo;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface NoteTagUpdateRequestToVoConverter {

    NoteTagUpdateVo convert(NoteTagUpdateRequest request);
}
