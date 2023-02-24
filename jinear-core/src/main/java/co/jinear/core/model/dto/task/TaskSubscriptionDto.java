package co.jinear.core.model.dto.task;

import co.jinear.core.model.dto.BaseDto;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class TaskSubscriptionDto  extends BaseDto {

    private String taskSubscriptionId;
    private String taskId;
    private String accountId;
}
