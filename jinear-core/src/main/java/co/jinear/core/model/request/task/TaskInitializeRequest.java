package co.jinear.core.model.request.task;

import co.jinear.core.model.request.BaseRequest;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.checkerframework.checker.nullness.qual.Nullable;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.ZonedDateTime;

@Getter
@Setter
@ToString(callSuper = true)
public class TaskInitializeRequest extends BaseRequest {

    @NotBlank
    private String workspaceId;

    @NotBlank
    private String teamId;

    @Nullable
    private String topicId;

    @Nullable
    private String assignedTo;

    @Nullable
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    private ZonedDateTime assignedDate;

    @Nullable
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    private ZonedDateTime dueDate;

    @Nullable
    private Boolean hasPreciseAssignedDate;

    @Nullable
    private Boolean hasPreciseDueDate;

    @NotBlank
    private String title;

    @Nullable
    private String description;

    @Nullable
    private String subTaskOf;

    @Nullable
    private String boardId;
}
