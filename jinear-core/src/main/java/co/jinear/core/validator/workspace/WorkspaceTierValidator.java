package co.jinear.core.validator.workspace;

import co.jinear.core.exception.NoAccessException;
import co.jinear.core.exception.workspace.WorkspaceExceedsTierLimitsException;
import co.jinear.core.model.dto.team.TeamDto;
import co.jinear.core.model.dto.workspace.WorkspaceDto;
import co.jinear.core.model.enumtype.workspace.WorkspaceTier;
import co.jinear.core.service.team.TeamRetrieveService;
import co.jinear.core.service.workspace.WorkspaceRetrieveService;
import co.jinear.core.service.workspace.member.WorkspaceInvitationListingService;
import co.jinear.core.service.workspace.member.WorkspaceMemberListingService;
import com.google.common.primitives.Longs;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Objects;
import java.util.Optional;

@Component
@Slf4j
@RequiredArgsConstructor
public class WorkspaceTierValidator {

    private static final Long BASIC_TIER_MAX_MEMBER_COUNT = 3L;

    private final TeamRetrieveService teamRetrieveService;
    private final WorkspaceRetrieveService workspaceRetrieveService;
    private final WorkspaceMemberListingService workspaceMemberListingService;
    private final WorkspaceInvitationListingService workspaceInvitationListingService;

    public void validateWorkspaceTier(String workspaceId, WorkspaceTier workspaceTier) {
        log.info("Validate workspace tier has started. workspaceId: {}, workspaceTier: {}", workspaceId, workspaceTier);
        WorkspaceDto workspaceDto = workspaceRetrieveService.retrieveWorkspaceWithId(workspaceId);
        validateWorkspaceTier(workspaceDto, workspaceTier);
    }

    public void validateWorkspaceTier(WorkspaceDto workspaceDto, WorkspaceTier workspaceTier) {
        log.info("Validate workspace tier has started. workspaceId: {}, workspaceTier: {}", workspaceDto.getWorkspaceId(), workspaceTier);
        if (Objects.isNull(workspaceDto.getTier()) || !workspaceTier.equals(workspaceDto.getTier())) {
            throw new NoAccessException();
        }
    }

    public void validateWorkspaceCanAddMember(String workspaceId) {
        log.info("Validate workspace can add member has started. workspaceId: {}", workspaceId);
        WorkspaceDto workspaceDto = workspaceRetrieveService.retrieveWorkspaceWithId(workspaceId);
        validateWorkspaceCanAddMember(workspaceDto);
    }

    public void validateWorkspaceCanAddMember(WorkspaceDto workspaceDto) {
        log.info("Validate workspace can add member has started. workspaceDto: {}", workspaceDto);
        if (WorkspaceTier.BASIC.equals(workspaceDto.getTier())) {
            Optional.of(workspaceDto)
                    .map(WorkspaceDto::getWorkspaceId)
                    .map(this::retrieveActiveMemberAndInvitationCount)
                    .filter(workspaceMemberCount -> Longs.compare(BASIC_TIER_MAX_MEMBER_COUNT, workspaceMemberCount) > 0)
                    .orElseThrow(WorkspaceExceedsTierLimitsException::new);
        }
    }

    public void validateTeamsWorkspaceHasAdvancedTeamTaskVisibilityTypesAccess(String teamId) {
        log.info("Validate team's workspace has advanced team task visibility access has started. teamId: {}", teamId);
        TeamDto teamDto = teamRetrieveService.retrieveTeam(teamId);
        validateWorkspaceHasAdvancedTeamTaskVisibilityTypesAccess(teamDto.getWorkspaceId());
    }

    public void validateWorkspaceHasAdvancedTeamTaskVisibilityTypesAccess(String workspaceId) {
        log.info("Validate workspace has advanced team task visibility access has started. workspaceId: {}", workspaceId);
        WorkspaceDto workspaceDto = workspaceRetrieveService.retrieveWorkspaceWithId(workspaceId);
        validateWorkspaceHasAdvancedTeamTaskVisibilityTypesAccess(workspaceDto);
    }

    public void validateWorkspaceHasAdvancedTeamTaskVisibilityTypesAccess(WorkspaceDto workspaceDto) {
        if (WorkspaceTier.BASIC.equals(workspaceDto.getTier())) {
            throw new WorkspaceExceedsTierLimitsException();
        }
    }

    private long retrieveActiveMemberAndInvitationCount(String workspaceId) {
        Long activeMemberCount = workspaceMemberListingService.workspaceActiveMemberCount(workspaceId);
        Long activeInvitationCount = workspaceInvitationListingService.workspaceActiveMemberInvitationCount(workspaceId);
        log.info("activeMemberCount: {}, activeInvitationCount: {}", activeMemberCount, activeInvitationCount);
        return activeMemberCount + activeInvitationCount;
    }
}
