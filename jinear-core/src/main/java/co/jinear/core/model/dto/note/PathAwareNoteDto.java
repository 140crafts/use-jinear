package co.jinear.core.model.dto.note;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PathAwareNoteDto extends NoteDto {

    private NotePathDto notePath;
}
