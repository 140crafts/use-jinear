package co.jinear.core.model.vo.task;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.ZonedDateTime;

@Getter
@Setter
@ToString
public class InitializeTaskListVo {

    private String workspaceId;
    private String teamId;
    private String ownerId;
    private String title;
    private ZonedDateTime dueDate;
}
