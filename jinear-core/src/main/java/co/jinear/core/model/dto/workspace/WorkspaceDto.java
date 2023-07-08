package co.jinear.core.model.dto.workspace;

import co.jinear.core.model.dto.BaseDto;
import co.jinear.core.model.dto.media.MediaDto;
import co.jinear.core.model.enumtype.workspace.WorkspaceTier;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class WorkspaceDto extends BaseDto {
    private String workspaceId;
    private String title;
    private String description;
    private Boolean isPersonal;
    private WorkspaceTier tier;
    private String username;
    private WorkspaceSettingDto settings;
    private MediaDto profilePicture;
}
