package co.jinear.core.service.team;

import co.jinear.core.exception.BusinessException;
import co.jinear.core.model.dto.team.TeamDto;
import co.jinear.core.model.dto.workspace.WorkspaceDto;
import co.jinear.core.model.entity.team.Team;
import co.jinear.core.model.enumtype.team.TeamJoinMethodType;
import co.jinear.core.model.vo.team.TeamInitializeVo;
import co.jinear.core.repository.TeamRepository;
import co.jinear.core.service.team.member.TeamMemberService;
import co.jinear.core.service.workspace.WorkspaceRetrieveService;
import co.jinear.core.system.NormalizeHelper;
import co.jinear.core.validator.team.TeamValidator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class TeamInitializeService {

    private final TeamRepository teamRepository;
    private final TeamValidator teamValidator;
    private final TeamMemberService teamMemberService;
    private final WorkspaceRetrieveService workspaceRetrieveService;
    private final TeamRetrieveService teamRetrieveService;
    private final ModelMapper modelMapper;

    public TeamDto initializeTeam(TeamInitializeVo teamInitializeVo) {
        sanitizeTag(teamInitializeVo);
        log.info("Initialize team has started. teamInitializeVo: {}", teamInitializeVo);
        validatePersonalWorkspaceTeamLimit(teamInitializeVo.getWorkspaceId());
        validateTeamNameIsNotUsedInWorkspace(teamInitializeVo);
        validateTeamTagIsNotUsedInWorkspace(teamInitializeVo);
        Team team = modelMapper.map(teamInitializeVo, Team.class);
        Team saved = teamRepository.saveAndFlush(team);
        checkAndSyncMembersWithWorkspace(saved);
        log.info("Initialize team has finished. teamId: {}", saved.getTeamId());
        return modelMapper.map(saved, TeamDto.class);
    }

    private void sanitizeTag(TeamInitializeVo teamInitializeVo) {
        log.info("Sanitize tag has started teamInitializeVo: {}", teamInitializeVo);
        Optional.of(teamInitializeVo)
                .map(TeamInitializeVo::getTag)
                .map(NormalizeHelper::normalizeStrictly)
                .ifPresent(teamInitializeVo::setTag);
    }

    private void validatePersonalWorkspaceTeamLimit(String workspaceId) {
        log.info("Validate personal workspace limit has started.");
        WorkspaceDto workspaceDto = workspaceRetrieveService.retrieveWorkspaceWithId(workspaceId);
        if (workspaceDto.isPersonal()) {
            List<TeamDto> teamDtoList = teamRetrieveService.retrieveWorkspaceTeams(workspaceId);
            if (!teamDtoList.isEmpty()) {
                throw new BusinessException();
            }
        }
    }

    private void checkAndSyncMembersWithWorkspace(Team team) {
        if (TeamJoinMethodType.SYNC_MEMBERS_WITH_WORKSPACE.equals(team.getJoinMethod())) {
            log.info("Sync members with workspace has started.");
            teamMemberService.addAllFromWorkspace(team.getTeamId());
        }
    }

    private void validateTeamNameIsNotUsedInWorkspace(TeamInitializeVo teamInitializeVo) {
        log.info("Validating team name is not used in workspace before.");
        teamValidator.validateTeamNameIsNotUsedInWorkspace(teamInitializeVo.getName(), teamInitializeVo.getWorkspaceId());
    }

    private void validateTeamTagIsNotUsedInWorkspace(TeamInitializeVo teamInitializeVo) {
        log.info("Validating team tag is not used in workspace before.");
        teamValidator.validateTeamTagIsNotUsedInWorkspace(teamInitializeVo.getTag(), teamInitializeVo.getWorkspaceId());
    }
}
