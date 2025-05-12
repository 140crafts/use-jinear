package co.jinear.core.model.response.task;

import co.jinear.core.model.dto.workspace.WorkspaceActivityDto;
import co.jinear.core.model.response.BaseResponse;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class TaskActivityRetrieveResponse extends BaseResponse {
    @JsonProperty("data")
    private List<WorkspaceActivityDto> taskActivities;
}
