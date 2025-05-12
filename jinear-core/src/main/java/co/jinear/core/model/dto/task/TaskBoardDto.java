package co.jinear.core.model.dto.task;

import co.jinear.core.model.dto.BaseDto;
import co.jinear.core.model.enumtype.task.TaskBoardStateType;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.ZonedDateTime;

@Getter
@Setter
@ToString(callSuper = true)
public class TaskBoardDto extends BaseDto {

    private String taskBoardId;
    private String workspaceId;
    private String teamId;
    private String ownerId;
    private String title;
    private ZonedDateTime dueDate;
    private TaskBoardStateType state;
}
