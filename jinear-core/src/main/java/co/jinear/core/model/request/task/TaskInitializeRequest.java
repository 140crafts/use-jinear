package co.jinear.core.model.request.task;

import co.jinear.core.model.request.BaseRequest;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.checkerframework.checker.nullness.qual.Nullable;

import javax.validation.constraints.NotBlank;
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
    private ZonedDateTime assignedDate;
    @Nullable
    private ZonedDateTime dueDate;
    @NotBlank
    private String title;
    @Nullable
    private String description;
}
