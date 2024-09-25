package co.jinear.core.model.vo.project;

import co.jinear.core.model.enumtype.project.ProjectFeedAccessType;
import co.jinear.core.model.enumtype.project.ProjectPostInitializeAccessType;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class ProjectFeedSettingsInitializeVo {

    private String projectId;
    private String projectTitle;
    private ProjectFeedAccessType projectFeedAccessType;
    private ProjectPostInitializeAccessType projectPostInitializeAccessType;
}
