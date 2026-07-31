package co.jinear.core.model.dto.notetag;

import co.jinear.core.model.dto.BaseDto;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class NoteTagAssignmentDto extends BaseDto {

    private String noteTagAssignmentId;
    private String noteId;
    private String noteTagId;
    private NoteTagDto noteTag;
}
