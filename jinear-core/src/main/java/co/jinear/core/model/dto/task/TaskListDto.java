package co.jinear.core.model.dto.task;

import co.jinear.core.model.dto.BaseDto;
import co.jinear.core.model.enumtype.task.TaskListStateType;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.ZonedDateTime;

@Getter
@Setter
@ToString(callSuper = true)
public class TaskListDto extends BaseDto {

    private String taskListId;
    private String workspaceId;
    private String teamId;
    private String ownerId;
    private String title;
    private ZonedDateTime dueDate;
    private TaskListStateType state;
}
