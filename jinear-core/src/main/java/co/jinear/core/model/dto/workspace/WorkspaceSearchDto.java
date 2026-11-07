package co.jinear.core.model.dto.workspace;

import co.jinear.core.model.dto.PageDto;
import co.jinear.core.model.dto.material.MaterialDto;
import co.jinear.core.model.dto.note.NoteDto;
import co.jinear.core.model.dto.task.TaskDto;
import lombok.Getter;
import lombok.Setter;
import org.checkerframework.checker.nullness.qual.Nullable;

@Getter
@Setter
public class WorkspaceSearchDto {

    @Nullable
    private PageDto<TaskDto> tasks;
    @Nullable
    private PageDto<NoteDto> notes;
    @Nullable
    private PageDto<MaterialDto> materials;
}
