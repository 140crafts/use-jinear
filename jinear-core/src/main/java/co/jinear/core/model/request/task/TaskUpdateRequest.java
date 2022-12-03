package co.jinear.core.model.request.task;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.checkerframework.checker.nullness.qual.Nullable;

import javax.validation.constraints.NotBlank;
import java.time.ZonedDateTime;

@Getter
@Setter
@ToString(callSuper = true)
public class TaskUpdateRequest {
    private String taskId;
    private String topicId;
    @Nullable
    private String assignedTo;
    @Nullable
    private ZonedDateTime assignedDate;
    @Nullable
    private ZonedDateTime dueDate;
    @NotBlank
    private String title;
    @Nullable
    private String description;
}
