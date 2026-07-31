package co.jinear.core.model.dto.notetag;

import co.jinear.core.model.dto.BaseDto;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class NoteTagDto extends BaseDto {

    private String noteTagId;
    private String notebookId;
    private String workspaceId;
    private String name;
    private String color;
}
