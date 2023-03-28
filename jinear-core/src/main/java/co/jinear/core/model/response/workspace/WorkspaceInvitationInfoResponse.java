package co.jinear.core.model.response.workspace;

import co.jinear.core.model.dto.workspace.WorkspaceInvitationInfoDto;
import co.jinear.core.model.response.BaseResponse;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class WorkspaceInvitationInfoResponse extends BaseResponse {

    @JsonProperty("data")
    private WorkspaceInvitationInfoDto workspaceInvitationInfoDto;
}
