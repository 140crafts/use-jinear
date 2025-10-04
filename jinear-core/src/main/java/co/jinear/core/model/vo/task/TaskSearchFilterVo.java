package co.jinear.core.model.vo.task;


import co.jinear.core.model.dto.team.member.TeamMemberDto;
import co.jinear.core.model.enumtype.FilterSort;
import co.jinear.core.model.enumtype.team.TeamTaskVisibilityType;
import co.jinear.core.model.enumtype.team.TeamWorkflowStateGroup;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.ZonedDateTime;
import java.util.List;
import java.util.Map;

import static co.jinear.core.model.enumtype.FilterSort.IDATE_DESC;

@Getter
@Setter
@ToString
public class TaskSearchFilterVo {

    private int page = 0;
    private int size = 250;
    private String workspaceId;
    private Map<TeamTaskVisibilityType, List<TeamMemberDto>> teamMemberMap;
    private List<String> excludingTeamIdList;
    private List<String> topicIds;
    private List<String> ownerIds;
    private List<String> assigneeIds;
    private List<String> workflowStatusIdList;
    private List<TeamWorkflowStateGroup> workflowStateGroups;
    private ZonedDateTime timespanStart;
    private ZonedDateTime timespanEnd;
    private FilterSort sort = IDATE_DESC;
    private List<String> projectIds;
    private List<String> milestoneIds;
    private List<String> taskboardIds;
}
