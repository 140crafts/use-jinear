package co.jinear.core.model.dto.note;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class NoteEditResultDto {

    private String noteId;
    private String url;
    private boolean changed;
}
