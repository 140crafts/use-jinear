package co.jinear.core.model.dto.task;

import co.jinear.core.model.dto.BaseDto;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString(callSuper = true)
public class TaskListEntryDto extends BaseDto {

    private String taskListEntryId;
    private String taskListId;
    private String taskId;
    private Integer order;
    private TaskDto task;
}
