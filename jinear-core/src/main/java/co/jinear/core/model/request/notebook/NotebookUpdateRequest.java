package co.jinear.core.model.request.notebook;

import co.jinear.core.model.enumtype.notebook.NotebookVisibilityType;
import co.jinear.core.model.request.BaseRequest;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;
import org.checkerframework.checker.nullness.qual.Nullable;

@Getter
@Setter
public class NotebookUpdateRequest extends BaseRequest {

    @NotBlank
    private String notebookId;
    @NotBlank
    private String title;
    @Nullable
    private String description;
    @Nullable
    private NotebookVisibilityType visibility;
}
