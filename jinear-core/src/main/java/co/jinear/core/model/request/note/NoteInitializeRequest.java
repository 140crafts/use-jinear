package co.jinear.core.model.request.note;

import co.jinear.core.model.request.BaseRequest;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import org.checkerframework.checker.nullness.qual.Nullable;

@Getter
@Setter
public class NoteInitializeRequest extends BaseRequest {

    @Nullable
    private String notebookId;
    @Nullable
    private String parentNoteId;
    @NotNull
    private String title;
    @NotBlank
    private String bodyState;
}
