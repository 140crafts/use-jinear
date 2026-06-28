package co.jinear.core.model.dto.note;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class NotePathItemDto {

    private String noteId;
    private String parentNoteId;
    private String title;
}
