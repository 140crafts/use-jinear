package co.jinear.core.model.dto.note;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@Builder
public class NotePathDto {
    private String noteId;
    private List<NotePathItemDto> path;
    private String fullPath;
}
