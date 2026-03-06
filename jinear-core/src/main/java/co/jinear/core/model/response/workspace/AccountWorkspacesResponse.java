package co.jinear.core.model.response.workspace;

import co.jinear.core.model.dto.workspace.DetailedWorkspaceMemberDto;
import co.jinear.core.model.response.BaseResponse;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

@Getter
@Setter
@ToString(callSuper = true)
public class AccountWorkspacesResponse extends BaseResponse {
    @JsonProperty("data")
    private List<DetailedWorkspaceMemberDto> workspaces;
}
