package co.jinear.core.model.vo.task;

import co.jinear.core.model.enumtype.task.TaskListStateType;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class UpdateTaskListStateVo {

    private String taskListId;
    private TaskListStateType state;
}
