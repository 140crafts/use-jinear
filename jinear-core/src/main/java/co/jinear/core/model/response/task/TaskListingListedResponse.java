package co.jinear.core.model.response.task;

import co.jinear.core.model.dto.task.TaskDto;
import co.jinear.core.model.response.BaseResponse;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

@Getter
@Setter
@ToString(callSuper = true)
public class TaskListingListedResponse extends BaseResponse {
    @JsonProperty("data")
    private List<TaskDto> taskDtoList;
}
