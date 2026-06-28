package co.jinear.core.model.dto.note;

import co.jinear.core.model.dto.PageDto;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class NoteHierarchyDto {

    private PathAwareNoteDto note;
    private PageDto<NoteDto> children;
}
