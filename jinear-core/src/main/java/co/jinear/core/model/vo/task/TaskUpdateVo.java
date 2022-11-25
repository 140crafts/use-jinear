package co.jinear.core.model.vo.task;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.ZonedDateTime;

@Getter
@Setter
@ToString(callSuper = true)
public class TaskUpdateVo {
    private String taskId;
    private String topicId;
    private ZonedDateTime assignedDate;
    private ZonedDateTime dueDate;
    private String title;
    private String description;
}
