package co.jinear.core.model.request.project;

import co.jinear.core.model.enumtype.project.ProjectFeedAccessType;
import co.jinear.core.model.enumtype.project.ProjectPostInitializeAccessType;
import co.jinear.core.model.request.BaseRequest;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ProjectFeedSettingsOperationRequest extends BaseRequest {

    private String projectId;
    private String projectTitle;
    private ProjectFeedAccessType projectFeedAccessType;
    private ProjectPostInitializeAccessType projectPostInitializeAccessType;
    private String info;
    private String infoWebsiteUrl;
}
