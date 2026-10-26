package co.jinear.core.model.dto.workspace;

import co.jinear.core.model.response.note.NotePaginatedResponse;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class WorkspacePrefetchNotebookDto {

    private String notebookId;
    private NotePaginatedResponse notes;
}
