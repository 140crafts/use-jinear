package co.jinear.core.model.dto.project;

import co.jinear.core.model.dto.BaseDto;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PublicProjectDto extends BaseDto {

    private String projectId;
    private String workspaceId;
    private String title;
    private Boolean archived;
}
