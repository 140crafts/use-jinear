package co.jinear.core.model.dto.task;

import co.jinear.core.model.dto.BaseDto;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString(callSuper = true)
public class TaskBoardEntryDto extends BaseDto {

    private String taskBoardEntryId;
    private String taskBoardId;
    private String taskId;
    private Integer order;
    private TaskDto task;
}
