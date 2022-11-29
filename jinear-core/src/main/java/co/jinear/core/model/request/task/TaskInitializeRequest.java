package co.jinear.core.model.request.task;

import co.jinear.core.model.request.BaseRequest;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.validation.constraints.NotBlank;
import java.time.ZonedDateTime;

@Getter
@Setter
@ToString(callSuper = true)
public class TaskInitializeRequest extends BaseRequest {

    @NotBlank
    private String workspaceId;
    private String teamId;
    private String topicId;
    private ZonedDateTime assignedDate;
    private ZonedDateTime dueDate;
    @NotBlank
    private String title;
    private String description;
}
