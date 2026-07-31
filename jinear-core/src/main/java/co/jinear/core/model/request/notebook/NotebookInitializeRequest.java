package co.jinear.core.model.request.notebook;

import co.jinear.core.model.enumtype.notebook.NotebookVisibilityType;
import co.jinear.core.model.request.BaseRequest;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import org.checkerframework.checker.nullness.qual.Nullable;

@Getter
@Setter
public class NotebookInitializeRequest extends BaseRequest {

    @NotBlank
    private String title;
    @Nullable
    private String description;
    @NotNull
    private NotebookVisibilityType visibility = NotebookVisibilityType.PUBLIC_WITHIN_WORKSPACE;
}
