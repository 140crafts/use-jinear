package co.jinear.core.model.dto.note;

import lombok.Getter;
import lombok.Setter;
import org.checkerframework.checker.nullness.qual.Nullable;

@Getter
@Setter
public class PathAwareNoteDto extends NoteDto {

    @Nullable
    private NotePathDto notePath;
}
