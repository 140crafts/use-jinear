package co.jinear.core.model.response.topic;

import co.jinear.core.model.dto.task.TaskSubscriptionDto;
import co.jinear.core.model.response.BaseResponse;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TaskSubscriptionResponse extends BaseResponse{
    @JsonProperty("data")
    private TaskSubscriptionDto taskSubscriptionDto;
}
