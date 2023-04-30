package co.jinear.core.model.response.task;

import co.jinear.core.model.dto.task.TaskSubscriptionDto;
import co.jinear.core.model.request.BaseRequest;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class TaskSubscribersListingResponse extends BaseRequest {

    @JsonProperty("data")
    List<TaskSubscriptionDto> taskSubscriptionDtoList;
}
