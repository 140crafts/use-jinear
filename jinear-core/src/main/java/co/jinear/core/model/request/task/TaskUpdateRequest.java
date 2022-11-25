package co.jinear.core.model.request.task;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.validation.constraints.NotBlank;
import java.time.ZonedDateTime;

@Getter
@Setter
@ToString(callSuper = true)
public class TaskUpdateRequest {
    private String taskId;
    private String topicId;
    private ZonedDateTime assignedDate;
    private ZonedDateTime dueDate;
    @NotBlank
    private String title;
    private String description;
}
