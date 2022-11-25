package co.jinear.core.model.dto.task;

import co.jinear.core.model.dto.BaseDto;
import co.jinear.core.model.dto.account.PlainAccountDto;
import co.jinear.core.model.entity.topic.Topic;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.ZonedDateTime;

@Getter
@Setter
@ToString(callSuper = true)
public class TaskDto extends BaseDto {

    private String taskId;
    private String topicId;
    private String workspaceId;
    private String ownerId;
    private ZonedDateTime assignedDate;
    private ZonedDateTime dueDate;
    private Integer tagNo;
    private String title;
    private String description;
    private TaskDto mainTask;
    private Topic topic;
    private PlainAccountDto account;
}
