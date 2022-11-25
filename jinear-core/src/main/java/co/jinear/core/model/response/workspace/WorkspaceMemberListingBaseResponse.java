package co.jinear.core.model.response.workspace;

import co.jinear.core.model.dto.PageDto;
import co.jinear.core.model.dto.workspace.WorkspaceMemberDto;
import co.jinear.core.model.response.BaseResponse;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString(callSuper = true)
public class WorkspaceMemberListingBaseResponse extends BaseResponse {
    @JsonProperty("data")
    private PageDto<WorkspaceMemberDto> workspaceMemberDtoPage;
}
