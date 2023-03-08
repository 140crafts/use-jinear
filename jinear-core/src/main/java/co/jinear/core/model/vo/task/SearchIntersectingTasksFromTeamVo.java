package co.jinear.core.model.vo.task;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.ZonedDateTime;

@Getter
@Setter
@ToString
public class SearchIntersectingTasksFromTeamVo {
    private String workspaceId;
    private String teamId;
    private ZonedDateTime timespanStart;
    private ZonedDateTime timespanEnd;
}
