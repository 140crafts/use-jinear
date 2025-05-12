package co.jinear.core.model.response.workspace;

import co.jinear.core.model.dto.workspace.WorkspaceActivityDto;
import co.jinear.core.model.response.BaseResponse;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class WorkspaceActivityResponse extends BaseResponse {

    @JsonProperty("data")
    WorkspaceActivityDto workspaceActivityDto;
}
