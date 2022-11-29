package co.jinear.core.service.team.member;

import co.jinear.core.model.dto.team.member.TeamMemberDto;
import co.jinear.core.repository.TeamMemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class TeamMemberRetrieveService {

    private final TeamMemberRepository teamMemberRepository;
    private final ModelMapper modelMapper;

    public List<TeamMemberDto> retrieveAllTeamMembershipsOfAnAccount(String accountId, String workspaceId) {
        log.info("Retrieve all team memberships of an account has started. accountId: {}, workspaceId: {}", accountId, workspaceId);
        return teamMemberRepository.findAllByAccountIdAndWorkspaceIdAndPassiveIdIsNull(accountId, workspaceId).stream()
                .map(teamMember -> modelMapper.map(teamMember, TeamMemberDto.class))
                .toList();
    }

    public boolean isAccountTeamMember(String accountId, String teamId) {
        log.info("Is account team member has started. accountId: {}, teamId: {}", accountId, teamId);
        return teamMemberRepository.countAllByAccountIdAndTeamIdAndPassiveIdIsNull(accountId, teamId) > 0L;
    }
}
