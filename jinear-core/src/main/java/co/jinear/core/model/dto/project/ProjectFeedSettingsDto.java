package co.jinear.core.model.dto.project;

import co.jinear.core.model.dto.BaseDto;
import co.jinear.core.model.dto.richtext.RichTextDto;
import co.jinear.core.model.enumtype.project.ProjectFeedAccessType;
import co.jinear.core.model.enumtype.project.ProjectPostInitializeAccessType;
import lombok.Getter;
import lombok.Setter;

import java.time.ZonedDateTime;

@Getter
@Setter
public class ProjectFeedSettingsDto extends BaseDto {

    private String projectFeedSettingsId;
    private String projectId;
    private ProjectFeedAccessType projectFeedAccessType;
    private ProjectPostInitializeAccessType projectPostInitializeAccessType;
    private String infoRichTextId;
    private String infoWebsiteUrl;
    private String accessKey;
    private ZonedDateTime passwordExpires;
    private RichTextDto info;
}
