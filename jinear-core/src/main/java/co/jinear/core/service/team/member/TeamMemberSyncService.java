package co.jinear.core.service.team.member;

import co.jinear.core.model.dto.team.member.TeamMemberDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class TeamMemberSyncService {

    private final TeamMemberService teamMemberService;
    private final AsyncTeamActivityService asyncTeamActivityService;

    public void syncTeamMembersWithWorkspace(String teamId, String initializedBy) {
        log.info("Sync members with workspace has started.");
        List<TeamMemberDto> teamMemberDtoList = teamMemberService.addAllFromWorkspace(teamId, List.of(initializedBy));
        asyncTeamActivityService.initializeTeamMemberAddedBatchActivity(initializedBy, teamMemberDtoList);
    }
}
