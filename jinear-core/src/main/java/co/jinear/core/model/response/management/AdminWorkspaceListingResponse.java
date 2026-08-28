package co.jinear.core.model.response.management;

import co.jinear.core.model.dto.PageDto;
import co.jinear.core.model.dto.workspace.WorkspaceDto;
import co.jinear.core.model.response.BaseResponse;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AdminWorkspaceListingResponse extends BaseResponse {

    @JsonProperty("data")
    private PageDto<WorkspaceDto> workspaceDtoPage;
}
