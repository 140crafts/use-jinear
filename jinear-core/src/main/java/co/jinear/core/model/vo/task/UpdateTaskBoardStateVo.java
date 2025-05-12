package co.jinear.core.model.vo.task;

import co.jinear.core.model.enumtype.task.TaskBoardStateType;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class UpdateTaskBoardStateVo {

    private String taskBoardId;
    private TaskBoardStateType state;
}
