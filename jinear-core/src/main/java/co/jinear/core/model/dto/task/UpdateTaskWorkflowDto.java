package co.jinear.core.model.dto.task;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UpdateTaskWorkflowDto {

    private String remindersPassiveId;
    private TaskDto taskDto;
}
