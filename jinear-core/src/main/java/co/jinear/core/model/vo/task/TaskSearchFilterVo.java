package co.jinear.core.model.vo.task;


import co.jinear.core.model.enumtype.task.TaskFilterSort;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.ZonedDateTime;
import java.util.List;

import static co.jinear.core.model.enumtype.task.TaskFilterSort.IDATE_DESC;

@Getter
@Setter
@ToString
public class TaskSearchFilterVo {

    private int page = 0;
    private String workspaceId;
    private List<String> teamIdList;
    private List<String> topicIds;
    private List<String> ownerIds;
    private List<String> assigneeIds;
    private List<String> workflowStatusIdList;
    private ZonedDateTime timespanStart;
    private ZonedDateTime timespanEnd;
    private TaskFilterSort taskFilterSort = IDATE_DESC;
}
