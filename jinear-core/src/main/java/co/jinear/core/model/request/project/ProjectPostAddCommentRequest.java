package co.jinear.core.model.request.project;

import co.jinear.core.model.request.BaseRequest;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;
import org.checkerframework.checker.nullness.qual.Nullable;

@Getter
@Setter
public class ProjectPostAddCommentRequest extends BaseRequest {

    @NotBlank
    private String projectId;
    @NotBlank
    private String postId;
    @NotBlank
    private String body;
    @Nullable
    private String quoteId;
}
