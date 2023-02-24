package co.jinear.core.model.vo.task;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class TaskSubscriptionVo {

    private String taskSubscriptionId;
    private String taskId;
    private String accountId;
}
