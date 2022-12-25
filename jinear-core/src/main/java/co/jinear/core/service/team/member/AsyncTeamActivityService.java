package co.jinear.core.service.team.member;

import co.jinear.core.model.dto.team.member.TeamMemberDto;
import co.jinear.core.model.vo.workspace.WorkspaceActivityCreateVo;
import co.jinear.core.service.workspace.WorkspaceActivityService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.List;

import static co.jinear.core.model.enumtype.workspace.WorkspaceActivityType.MEMBER_JOIN;

@Slf4j
@Service
@RequiredArgsConstructor
public class AsyncTeamActivityService {

    private final WorkspaceActivityService workspaceActivityService;

    public void initializeTeamMemberAddedActivity() {

    }

    @Async
    public void initializeTeamMemberAddedBatchActivity(String initializedBy, List<TeamMemberDto> teamMemberDtoList) {
        log.info("Initialize team member added batch activity started. initializedBy: {}", initializedBy);
        teamMemberDtoList.stream()
                .forEach(teamMemberDto ->
                        initializeActivity(initializedBy, teamMemberDto));
    }

    private void initializeActivity(String initializedBy, TeamMemberDto teamMemberDto) {
        WorkspaceActivityCreateVo vo = WorkspaceActivityCreateVo
                .builder()
                .workspaceId(teamMemberDto.getWorkspaceId())
                .teamId(teamMemberDto.getTeamId())
                .performedBy(initializedBy)
                .relatedObjectId(teamMemberDto.getAccountId())
                .type(MEMBER_JOIN)
                .build();
        workspaceActivityService.createWorkspaceActivity(vo);
    }


}
