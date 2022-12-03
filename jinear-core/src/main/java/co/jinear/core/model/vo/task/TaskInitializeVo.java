package co.jinear.core.model.vo.task;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.ZonedDateTime;

@Getter
@Setter
@ToString
public class TaskInitializeVo {

    private String topicId;
    private String workspaceId;
    private String teamId;
    private String ownerId;
    private String assignedTo;
    private ZonedDateTime assignedDate;
    private ZonedDateTime dueDate;
    private String title;
    private String description;
}
