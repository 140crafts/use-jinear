package co.jinear.core.model.request.note;

import co.jinear.core.model.request.BaseRequest;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;
import org.checkerframework.checker.nullness.qual.Nullable;

@Getter
@Setter
public class NoteMoveRequest extends BaseRequest {

    @NotBlank
    private String noteId;
    @Nullable
    private String parentNoteId;
}
