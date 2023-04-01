package co.jinear.core.service.team.member;

import co.jinear.core.converter.team.TeamMemberConverter;
import co.jinear.core.model.dto.team.member.TeamMemberDto;
import co.jinear.core.repository.TeamMemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class TeamMemberRetrieveService {

    private final TeamMemberRepository teamMemberRepository;
    private final TeamMemberConverter teamMemberConverter;

    public Optional<TeamMemberDto> retrieve(String accountId, String teamId) {
        log.info("Retrieve team member has started. accountId: {}, teamId: {}", accountId, teamId);
        return teamMemberRepository.findByAccountIdAndTeamIdAndPassiveIdIsNull(accountId, teamId)
                .map(teamMemberConverter::map);
    }

    public List<TeamMemberDto> retrieveAllTeamMembershipsOfAnAccount(String accountId, String workspaceId) {
        log.info("Retrieve all team memberships of an account has started. accountId: {}, workspaceId: {}", accountId, workspaceId);
        return teamMemberRepository.findAllByAccountIdAndWorkspaceIdAndPassiveIdIsNullAndTeam_PassiveIdIsNull(accountId, workspaceId).stream()
                .map(teamMemberConverter::map)
                .toList();
    }

    public boolean isAccountTeamMember(String accountId, String teamId) {
        log.info("Is account team member has started. accountId: {}, teamId: {}", accountId, teamId);
        return teamMemberRepository.countAllByAccountIdAndTeamIdAndPassiveIdIsNull(accountId, teamId) > 0L;
    }
}
