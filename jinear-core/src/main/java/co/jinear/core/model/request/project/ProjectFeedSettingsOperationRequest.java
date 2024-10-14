package co.jinear.core.model.request.project;

import co.jinear.core.model.enumtype.project.ProjectFeedAccessType;
import co.jinear.core.model.enumtype.project.ProjectPostCommentPolicyType;
import co.jinear.core.model.enumtype.project.ProjectPostInitializeAccessType;
import co.jinear.core.model.request.BaseRequest;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;
import org.checkerframework.checker.nullness.qual.Nullable;

@Getter
@Setter
public class ProjectFeedSettingsOperationRequest extends BaseRequest {

    @NotBlank
    private String projectId;
    @Nullable
    private String projectTitle;
    @Nullable
    private ProjectFeedAccessType projectFeedAccessType;
    @Nullable
    private ProjectPostInitializeAccessType projectPostInitializeAccessType;
    @Nullable
    private ProjectPostCommentPolicyType projectPostCommentPolicyType;
    @Nullable
    private String info;
    @Nullable
    private String infoWebsiteUrl;
}
