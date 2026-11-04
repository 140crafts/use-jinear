package co.jinear.core.model.request.note;

import co.jinear.core.model.request.BaseRequest;
import lombok.Getter;
import lombok.Setter;
import org.checkerframework.checker.nullness.qual.Nullable;

@Getter
@Setter
public class NoteListRequest extends BaseRequest {

    @Nullable
    private String notebookId;
    @Nullable
    private String parentNoteId;
}
