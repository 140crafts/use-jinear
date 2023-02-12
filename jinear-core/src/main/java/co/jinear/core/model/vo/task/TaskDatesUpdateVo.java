package co.jinear.core.model.vo.task;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.ZonedDateTime;

@Getter
@Setter
@ToString
public class TaskDatesUpdateVo {

    private String taskId;
    private ZonedDateTime assignedDate;
    private ZonedDateTime dueDate;
}
