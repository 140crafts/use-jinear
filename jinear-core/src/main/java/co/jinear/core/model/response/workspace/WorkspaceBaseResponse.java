package co.jinear.core.model.response.workspace;

import co.jinear.core.model.dto.workspace.WorkspaceDto;
import co.jinear.core.model.response.BaseResponse;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString(callSuper = true)
public class WorkspaceBaseResponse extends BaseResponse {
    @JsonProperty("data")
    private WorkspaceDto workspace;
}
