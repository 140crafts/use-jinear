package co.jinear.core.service.workspace;

import co.jinear.core.converter.workspace.WorkspaceDisplayPreferenceConverter;
import co.jinear.core.exception.NotFoundException;
import co.jinear.core.model.dto.team.TeamDto;
import co.jinear.core.model.dto.team.member.TeamMemberDto;
import co.jinear.core.model.dto.workspace.WorkspaceDisplayPreferenceDto;
import co.jinear.core.model.entity.workspace.WorkspaceDisplayPreference;
import co.jinear.core.model.enumtype.workspace.WorkspaceAccountRoleType;
import co.jinear.core.repository.WorkspaceDisplayPreferenceRepository;
import co.jinear.core.service.team.TeamRetrieveService;
import co.jinear.core.service.team.member.TeamMemberRetrieveService;
import co.jinear.core.service.workspace.member.WorkspaceMemberRetrieveService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class WorkspaceDisplayPreferenceService {

    private final WorkspaceDisplayPreferenceRepository workspaceDisplayPreferenceRepository;
    private final TeamRetrieveService teamRetrieveService;
    private final WorkspaceDisplayPreferenceConverter workspaceDisplayPreferenceConverter;
    private final WorkspaceMemberRetrieveService workspaceMemberRetrieveService;
    private final TeamMemberRetrieveService teamMemberRetrieveService;

    public WorkspaceDisplayPreferenceDto retrieveAccountPreferredWorkspace(String accountId) {
        log.info("Retrieve account preferred workspace has started. accountId: {}", accountId);
        return retrieveEntityByAccountId(accountId)
                .map(workspaceDisplayPreferenceConverter::map)
                .map(this::retrieveAndSetWorkspaceMemberRole)
                .map(this::retrieveAndSetTeamMemberRole)
                .orElseThrow(NotFoundException::new);
    }

    public Optional<WorkspaceDisplayPreferenceDto> retrieveAccountPreferredWorkspaceOptional(String accountId) {
        log.info("Retrieve account preferred workspace has started. accountId: {}", accountId);
        return retrieveEntityByAccountId(accountId)
                .map(workspaceDisplayPreferenceConverter::map)
                .map(this::retrieveAndSetWorkspaceMemberRole)
                .map(this::retrieveAndSetTeamMemberRole);
    }

    public void setAccountPreferredWorkspace(String accountId, String workspaceId) {
        log.info("Set account preferred workspace has started. accountId: {}, workspaceId: {}", accountId, workspaceId);
        WorkspaceDisplayPreference workspaceDisplayPreference = retrieveEntityByAccountId(accountId)
                .orElseGet(() -> initializeDefault(accountId, workspaceId));
        changeTeamIdIfWorkspaceHasChanged(workspaceDisplayPreference, workspaceId);
        workspaceDisplayPreference.setAccountId(accountId);
        workspaceDisplayPreference.setPreferredWorkspaceId(workspaceId);
        workspaceDisplayPreferenceRepository.saveAndFlush(workspaceDisplayPreference);
        log.info("Account preferred workspace has been set.");
    }

    public WorkspaceDisplayPreferenceDto setAccountPreferredTeamId(String accountId, String teamId) {
        log.info("Set account preferred teamId has started. accountId: {}, teamId: {}", accountId, teamId);
        WorkspaceDisplayPreference workspaceDisplayPreference = retrieveEntityByAccountId(accountId)
                .orElseThrow(NotFoundException::new);
        TeamDto teamDto = teamRetrieveService.retrieveTeam(teamId);
        workspaceDisplayPreference.setPreferredWorkspaceId(teamDto.getWorkspaceId());
        workspaceDisplayPreference.setPreferredTeamId(teamId);
        WorkspaceDisplayPreference saved = workspaceDisplayPreferenceRepository.save(workspaceDisplayPreference);
        log.info("Account preferred team and workspace has been set.");
        WorkspaceDisplayPreferenceDto workspaceDisplayPreferenceDto = workspaceDisplayPreferenceConverter.map(saved);
        return retrieveAndSetWorkspaceMemberRole(workspaceDisplayPreferenceDto);
    }

    private Optional<WorkspaceDisplayPreference> retrieveEntityByAccountId(String accountId) {
        return workspaceDisplayPreferenceRepository.findByAccountIdAndPassiveIdIsNull(accountId);
    }

    private WorkspaceDisplayPreference initializeDefault(String accountId, String workspaceId) {
        WorkspaceDisplayPreference workspaceDisplayPreference = new WorkspaceDisplayPreference();
        workspaceDisplayPreference.setAccountId(accountId);
        workspaceDisplayPreference.setPreferredWorkspaceId(workspaceId);
        return workspaceDisplayPreference;
    }

    private void changeTeamIdIfWorkspaceHasChanged(WorkspaceDisplayPreference workspaceDisplayPreference, String workspaceId) {
        if (!workspaceDisplayPreference.getPreferredWorkspaceId().equals(workspaceId)) {
            log.info("Preferred workspace is different from current one. Retrieve and set account's first team in this workspace as preferred team if present.");
            teamMemberRetrieveService.retrieveAllTeamMembershipsOfAnAccount(workspaceDisplayPreference.getAccountId(), workspaceId)
                    .stream()
                    .findFirst()
                    .map(TeamMemberDto::getTeamId)
                    .ifPresent(workspaceDisplayPreference::setPreferredTeamId);
        }
    }

    private WorkspaceDisplayPreferenceDto retrieveAndSetWorkspaceMemberRole(WorkspaceDisplayPreferenceDto workspaceDisplayPreferenceDto) {
        WorkspaceAccountRoleType workspaceAccountRoleType = workspaceMemberRetrieveService.retrieveAccountWorkspaceRole(workspaceDisplayPreferenceDto.getAccountId(), workspaceDisplayPreferenceDto.getPreferredWorkspaceId());
        workspaceDisplayPreferenceDto.setWorkspaceRole(workspaceAccountRoleType);
        return workspaceDisplayPreferenceDto;
    }

    private WorkspaceDisplayPreferenceDto retrieveAndSetTeamMemberRole(WorkspaceDisplayPreferenceDto workspaceDisplayPreferenceDto) {
        teamMemberRetrieveService.retrieve(workspaceDisplayPreferenceDto.getAccountId(), workspaceDisplayPreferenceDto.getPreferredTeamId())
                .map(TeamMemberDto::getRole)
                .ifPresent(workspaceDisplayPreferenceDto::setTeamRole);
        return workspaceDisplayPreferenceDto;
    }
}
