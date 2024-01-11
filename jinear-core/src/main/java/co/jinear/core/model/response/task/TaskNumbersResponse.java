package co.jinear.core.model.response.task;

import co.jinear.core.model.dto.task.TaskAnalyticNumbersDto;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TaskNumbersResponse {

    @JsonProperty("data")
    private TaskAnalyticNumbersDto taskAnalyticNumbersDto;
}
