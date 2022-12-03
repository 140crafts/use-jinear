package co.jinear.core.model.dto.workspace;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class WorkspaceDisplayPreferenceDto {

    private String account_id;
    private String preferredWorkspaceId;
    private String preferredTeamId;
}
