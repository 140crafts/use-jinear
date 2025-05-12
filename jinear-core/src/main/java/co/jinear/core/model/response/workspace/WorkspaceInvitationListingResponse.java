package co.jinear.core.model.response.workspace;

import co.jinear.core.model.dto.PageDto;
import co.jinear.core.model.dto.workspace.WorkspaceInvitationDto;
import co.jinear.core.model.response.BaseResponse;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class WorkspaceInvitationListingResponse extends BaseResponse {

    @JsonProperty("data")
    private PageDto<WorkspaceInvitationDto> workspaceInvitationDtoList;
}
