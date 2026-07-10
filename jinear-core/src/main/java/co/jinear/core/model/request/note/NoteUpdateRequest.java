package co.jinear.core.model.request.note;

import co.jinear.core.model.request.BaseRequest;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import org.checkerframework.checker.nullness.qual.Nullable;

@Getter
@Setter
public class NoteUpdateRequest extends BaseRequest {

    @NotBlank
    private String noteId;
    @NotNull
    private String title;
}
