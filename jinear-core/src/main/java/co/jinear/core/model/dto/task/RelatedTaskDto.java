package co.jinear.core.model.dto.task;

import co.jinear.core.model.dto.BaseDto;
import lombok.Getter;
import lombok.Setter;

import java.time.ZonedDateTime;

@Getter
@Setter
public class RelatedTaskDto extends BaseDto {

    private String taskId;
    private String topicId;
    private String workspaceId;
    private String teamId;
    private String ownerId;
    private String workflowStatusId;
    private String assignedTo;
    private ZonedDateTime assignedDate;
    private ZonedDateTime dueDate;
    private Integer teamTagNo;
    private Integer topicTagNo;
    private String title;
}
