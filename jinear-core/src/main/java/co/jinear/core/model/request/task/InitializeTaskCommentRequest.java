package co.jinear.core.model.request.task;

import co.jinear.core.model.request.BaseRequest;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import org.checkerframework.checker.nullness.qual.Nullable;

@Getter
@Setter
public class InitializeTaskCommentRequest extends BaseRequest {

    @NotNull
    private String taskId;
    @Nullable
    private String quoteCommentId;
    @NotBlank
    private String comment;
}
