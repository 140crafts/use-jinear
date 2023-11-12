package co.jinear.core.service.team;

import co.jinear.core.converter.team.TeamConverter;
import co.jinear.core.exception.BusinessException;
import co.jinear.core.model.dto.team.TeamDto;
import co.jinear.core.model.entity.team.Team;
import co.jinear.core.model.enumtype.localestring.LocaleStringType;
import co.jinear.core.model.enumtype.localestring.LocaleType;
import co.jinear.core.model.enumtype.team.TeamJoinMethodType;
import co.jinear.core.model.enumtype.team.TeamMemberRoleType;
import co.jinear.core.model.enumtype.team.TeamWorkflowStateGroup;
import co.jinear.core.model.vo.team.TeamInitializeVo;
import co.jinear.core.model.vo.team.member.TeamMemberAddVo;
import co.jinear.core.model.vo.team.workflow.InitializeTeamWorkflowStatusVo;
import co.jinear.core.repository.TeamRepository;
import co.jinear.core.service.mail.LocaleStringService;
import co.jinear.core.service.team.member.TeamMemberService;
import co.jinear.core.service.team.member.TeamMemberSyncService;
import co.jinear.core.service.team.workflow.TeamWorkflowStatusService;
import co.jinear.core.system.NormalizeHelper;
import co.jinear.core.validator.team.TeamValidator;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Objects;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class TeamInitializeService {

    private final TeamRepository teamRepository;
    private final TeamValidator teamValidator;
    private final TeamWorkflowStatusService teamWorkflowStatusService;
    private final LocaleStringService localeStringService;
    private final TeamMemberSyncService teamMemberSyncService;
    private final TeamConverter teamConverter;
    private final TeamMemberService teamMemberService;

    @Transactional
    public TeamDto initializeTeam(TeamInitializeVo teamInitializeVo) {
        sanitizeTagAndUsername(teamInitializeVo);
        log.info("Initialize team has started. teamInitializeVo: {}", teamInitializeVo);
        validateTeamNameIsNotUsedInWorkspace(teamInitializeVo);
        validateTeamTagIsNotUsedInWorkspace(teamInitializeVo);
        validateTeamUsernameIsNotUsedInWorkspace(teamInitializeVo);
        Team team = teamConverter.map(teamInitializeVo);
        Team saved = teamRepository.saveAndFlush(team);
        checkAndSyncMembersWithWorkspace(saved);
        initializeDefaultWorkflow(saved.getTeamId(), saved.getWorkspaceId(), teamInitializeVo.getLocale());
        log.info("Initialize team has finished. teamId: {}", saved.getTeamId());
        return teamConverter.map(saved);
    }

    private void sanitizeTagAndUsername(TeamInitializeVo teamInitializeVo) {
        log.info("Sanitize tag and username has started teamInitializeVo: {}", teamInitializeVo);
        if (Objects.isNull(teamInitializeVo.getTag())) {
            throw new BusinessException();
        }

        Optional.of(teamInitializeVo)
                .map(TeamInitializeVo::getTag)
                .map(NormalizeHelper::normalizeUsernameReplaceSpaces)
                .ifPresent(teamInitializeVo::setTag);

        if (Objects.isNull(teamInitializeVo.getUsername())) {
            teamInitializeVo.setUsername(teamInitializeVo.getUsername());
        }

        Optional.of(teamInitializeVo)
                .map(TeamInitializeVo::getUsername)
                .map(NormalizeHelper::normalizeUsernameReplaceSpaces)
                .ifPresent(teamInitializeVo::setUsername);
    }

    private void checkAndSyncMembersWithWorkspace(Team team) {
        assignInitializedByAccountToTeam(team);
        if (TeamJoinMethodType.SYNC_MEMBERS_WITH_WORKSPACE.equals(team.getJoinMethod())) {
            teamMemberSyncService.syncTeamMembersWithWorkspace(team.getTeamId(), team.getInitializedBy());
        }
    }

    private void assignInitializedByAccountToTeam(Team team) {
        TeamMemberAddVo teamMemberAddVo = new TeamMemberAddVo();
        teamMemberAddVo.setAccountId(team.getInitializedBy());
        teamMemberAddVo.setTeamId(team.getTeamId());
        teamMemberAddVo.setRole(TeamMemberRoleType.ADMIN);
        teamMemberService.addTeamMember(teamMemberAddVo);
    }

    private void validateTeamNameIsNotUsedInWorkspace(TeamInitializeVo teamInitializeVo) {
        log.info("Validating team name is not used in workspace before.");
        teamValidator.validateTeamNameIsNotUsedInWorkspace(teamInitializeVo.getName(), teamInitializeVo.getWorkspaceId());
    }

    private void validateTeamTagIsNotUsedInWorkspace(TeamInitializeVo teamInitializeVo) {
        log.info("Validating team tag is not used in workspace before.");
        teamValidator.validateTeamTagIsNotUsedInWorkspace(teamInitializeVo.getTag(), teamInitializeVo.getWorkspaceId());
    }

    private void validateTeamUsernameIsNotUsedInWorkspace(TeamInitializeVo teamInitializeVo) {
        log.info("Validating team username is not used in workspace before.");
        String teamUsernameCandid = Optional.of(teamInitializeVo)
                .map(TeamInitializeVo::getUsername)
                .orElseThrow(BusinessException::new);
        teamValidator.validateTeamUsernameIsNotUsedInWorkspace(teamUsernameCandid, teamInitializeVo.getWorkspaceId());
    }

    private void initializeDefaultWorkflow(String teamId, String workspaceId, LocaleType locale) {
        log.info("Initialize default team workflow statuses has started for teamId: {}, locale: {}", teamId, locale);
        teamWorkflowStatusService.initializeTeamWorkflowStatus(new InitializeTeamWorkflowStatusVo(
                teamId,
                workspaceId,
                TeamWorkflowStateGroup.BACKLOG,
                localeStringService.retrieveLocalString(LocaleStringType.TEAM_WORKFLOW_STATUS_BACKLOG, locale)));
        teamWorkflowStatusService.initializeTeamWorkflowStatus(new InitializeTeamWorkflowStatusVo(
                teamId,
                workspaceId,
                TeamWorkflowStateGroup.NOT_STARTED,
                localeStringService.retrieveLocalString(LocaleStringType.TEAM_WORKFLOW_STATUS_NOT_STARTED, locale)));
        teamWorkflowStatusService.initializeTeamWorkflowStatus(new InitializeTeamWorkflowStatusVo(
                teamId,
                workspaceId,
                TeamWorkflowStateGroup.STARTED,
                localeStringService.retrieveLocalString(LocaleStringType.TEAM_WORKFLOW_STATUS_STARTED, locale)));
        teamWorkflowStatusService.initializeTeamWorkflowStatus(new InitializeTeamWorkflowStatusVo(
                teamId,
                workspaceId,
                TeamWorkflowStateGroup.COMPLETED,
                localeStringService.retrieveLocalString(LocaleStringType.TEAM_WORKFLOW_STATUS_COMPLETED, locale)));
        teamWorkflowStatusService.initializeTeamWorkflowStatus(new InitializeTeamWorkflowStatusVo(
                teamId,
                workspaceId,
                TeamWorkflowStateGroup.CANCELLED,
                localeStringService.retrieveLocalString(LocaleStringType.TEAM_WORKFLOW_STATUS_CANCELLED, locale)));
        log.info("Initialize default team workflow statuses has finished for teamId: {}, locale: {}", teamId, locale);
    }


}
