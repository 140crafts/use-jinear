package co.jinear.core.model.vo.task;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.ZonedDateTime;
import java.util.List;

@Getter
@Setter
@ToString
public class SearchIntersectingTasksFromWorkspaceVo {
    private String workspaceId;
    private List<String> teamIdList;
    private ZonedDateTime timespanStart;
    private ZonedDateTime timespanEnd;
}
