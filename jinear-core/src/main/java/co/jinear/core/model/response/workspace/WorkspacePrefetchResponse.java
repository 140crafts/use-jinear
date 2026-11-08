package co.jinear.core.model.response.workspace;

import co.jinear.core.model.dto.workspace.WorkspacePrefetchDto;
import co.jinear.core.model.response.BaseResponse;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class WorkspacePrefetchResponse extends BaseResponse {

    @JsonProperty("data")
    private WorkspacePrefetchDto workspacePrefetchDto;
}
