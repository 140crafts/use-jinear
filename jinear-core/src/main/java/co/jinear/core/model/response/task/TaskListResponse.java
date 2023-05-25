package co.jinear.core.model.response.task;

import co.jinear.core.model.dto.task.TaskListDto;
import co.jinear.core.model.response.BaseResponse;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString(callSuper = true)
public class TaskListResponse extends BaseResponse {
    @JsonProperty("data")
    private TaskListDto taskListDto;
}
