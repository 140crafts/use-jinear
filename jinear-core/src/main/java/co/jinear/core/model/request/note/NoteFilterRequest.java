package co.jinear.core.model.request.note;

import co.jinear.core.model.request.BaseRequest;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import org.checkerframework.checker.nullness.qual.Nullable;

@Getter
@Setter
public class NoteFilterRequest extends BaseRequest {

    private int page = 0;
    @NotNull
    private String workspaceId;
    @Nullable
    private String notebookId;
    @Nullable
    private String parentNoteId;
    @Nullable
    private String noteId;
}
