package co.jinear.core.model.vo.task;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class TaskAssigneeUpdateVo {

    private String taskId;
    private String assigneeId;
}
