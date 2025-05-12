package co.jinear.core.model.vo.workspace;

import co.jinear.core.model.dto.team.member.TeamMemberDto;
import co.jinear.core.model.enumtype.FilterSort;
import co.jinear.core.model.enumtype.team.TeamTaskVisibilityType;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;
import java.util.Map;

import static co.jinear.core.model.enumtype.FilterSort.IDATE_DESC;
import static co.jinear.core.repository.WorkspaceActivityFilterRepository.FILTER_PAGE_SIZE;

@Getter
@Setter
@ToString
public class WorkspaceActivityFilterVo {

    private int page = 0;
    private int size = FILTER_PAGE_SIZE;
    private String workspaceId;
    private Map<TeamTaskVisibilityType, List<TeamMemberDto>> teamMemberMap;
    private List<String> taskIds;
    private List<String> ownerIds;
    private List<String> assigneeIds;
    private FilterSort taskFilterSort = IDATE_DESC;
}
