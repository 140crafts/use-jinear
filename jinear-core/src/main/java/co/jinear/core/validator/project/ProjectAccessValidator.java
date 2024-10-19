package co.jinear.core.validator.project;

import co.jinear.core.exception.NoAccessException;
import co.jinear.core.exception.NotFoundException;
import co.jinear.core.model.dto.project.AccountProjectPermissionFlags;
import co.jinear.core.model.dto.project.ProjectDto;
import co.jinear.core.model.dto.project.ProjectFeedSettingsDto;
import co.jinear.core.model.dto.project.ProjectTeamDto;
import co.jinear.core.model.dto.workspace.WorkspaceMemberDto;
import co.jinear.core.service.project.ProjectRetrieveService;
import co.jinear.core.validator.team.TeamAccessValidator;
import co.jinear.core.validator.workspace.WorkspaceValidator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import static co.jinear.core.model.enumtype.workspace.WorkspaceAccountRoleType.ADMIN;
import static co.jinear.core.model.enumtype.workspace.WorkspaceAccountRoleType.OWNER;

@Slf4j
@Component
@RequiredArgsConstructor
public class ProjectAccessValidator {

    private final ProjectRetrieveService projectRetrieveService;
    private final TeamAccessValidator teamAccessValidator;
    private final WorkspaceValidator workspaceValidator;

    /*
     * For workspace members. Not for guests!
     * */
    public void validateHasExplicitAccess(String projectId, String accountId) {
        log.info("Validate has explicit access has started. projectId: {}, accountId: {}", projectId, accountId);
        ProjectDto projectDto = projectRetrieveService.retrieve(projectId);
        if (!workspaceValidator.isWorkspaceAdminOrOwner(accountId, projectDto.getWorkspaceId())) {
            validateAccountIsProjectTeamsMember(accountId, projectDto);
        }
    }

    /*
     * For workspace members. Not for guests!
     * */
    public void validateHasExplicitAdminAccess(String projectId, String accountId) {
        log.info("Validate has explicit access has started. projectId: {}, accountId: {}", projectId, accountId);
        ProjectDto projectDto = projectRetrieveService.retrieve(projectId);
        if (!workspaceValidator.isWorkspaceAdminOrOwner(accountId, projectDto.getWorkspaceId())) {
            validateAccountIsProjectTeamsAdmin(accountId, projectDto);
        }
    }

    public void validateWorkspaceAdminOrOwner(String projectId, String accountId) {
        log.info("Validate has explicit access has started. projectId: {}, accountId: {}", projectId, accountId);
        ProjectDto projectDto = projectRetrieveService.retrieve(projectId);
        workspaceValidator.validateWorkspaceRoles(accountId, projectDto.getWorkspaceId(), List.of(OWNER, ADMIN));
    }

    public void validateProjectAndWorkspaceIsInSameWorkspace(String projectId, String workspaceId) {
        if (!projectRetrieveService.checkExistsByProjectIdAndWorkspaceId(projectId, workspaceId)) {
            throw new NotFoundException();
        }
    }

    public void validateProjectLead(String projectId, String accountId) {
        log.info("Validate project lead has started. projectId: {}, accountId: {}", projectId, accountId);
        ProjectDto projectDto = projectRetrieveService.retrieve(projectId);
        String leadAccountId = Optional.of(projectDto)
                .map(ProjectDto::getLeadWorkspaceMember)
                .map(WorkspaceMemberDto::getAccountId)
                .orElse(null);
        if (Objects.nonNull(leadAccountId) && !StringUtils.equalsIgnoreCase(leadAccountId, accountId)) {
            throw new NoAccessException();
        }
    }

    public void validateProjectWorkspaceMember(String projectId, String accountId) {
        log.info("Validate project workspace member has started. projectId: {}, accountId: {}", projectId, accountId);
        ProjectDto projectDto = projectRetrieveService.retrieve(projectId);
        String workspaceId = projectDto.getWorkspaceId();
        workspaceValidator.validateHasAccess(accountId, workspaceId);
    }

    public AccountProjectPermissionFlags retrieveAccountProjectPermissionFlags(String accountId, String projectId) {
        log.info("Retrieve account project permission flags has started. projectId: {}, accountId: {}", projectId, accountId);
        ProjectDto projectDto = projectRetrieveService.retrieve(projectId);
        boolean isAnonymousUser = StringUtils.equalsIgnoreCase("anonymousUser", accountId);
        boolean isAccountWorkspaceAdminOrOwner = !isAnonymousUser && workspaceValidator.isWorkspaceAdminOrOwner(accountId, projectDto.getWorkspaceId());
        boolean isAccountIsProjectTeamsMember = !isAnonymousUser && isAccountIsProjectTeamsMember(accountId, projectDto);
        boolean isAccountIsProjectTeamsAdmin = !isAnonymousUser && isAccountIsProjectTeamsAdmin(accountId, projectDto);
        boolean isAccountProjectLead = !isAnonymousUser && Optional.of(projectDto).map(ProjectDto::getLeadWorkspaceMember).map(WorkspaceMemberDto::getAccountId).map(leadAccountId -> StringUtils.equalsIgnoreCase(leadAccountId, accountId)).orElse(Boolean.FALSE);
        boolean isWorkspaceMember = !isAnonymousUser && workspaceValidator.isAccountWorkspaceMember(accountId, projectDto.getWorkspaceId());

        boolean canInitializePost = !isAnonymousUser && Optional.of(projectDto)
                .map(ProjectDto::getProjectFeedSettings)
                .map(ProjectFeedSettingsDto::getProjectPostInitializeAccessType)
                .map(projectPostInitializeAccessType -> switch (projectPostInitializeAccessType) {
                    case WORKSPACE_ADMINS -> isAccountWorkspaceAdminOrOwner;
                    case PROJECT_LEAD -> isAccountWorkspaceAdminOrOwner || isAccountProjectLead;
                    case PROJECT_TEAM_ADMINS -> isAccountWorkspaceAdminOrOwner || isAccountIsProjectTeamsAdmin;
                    case PROJECT_TEAM_MEMBERS -> isAccountWorkspaceAdminOrOwner || isAccountIsProjectTeamsMember;
                    case WORKSPACE_MEMBERS -> isAccountWorkspaceAdminOrOwner || isWorkspaceMember;
                })
                .orElse(Boolean.FALSE);

        boolean canComment = !isAnonymousUser && Optional.of(projectDto)
                .map(ProjectDto::getProjectFeedSettings)
                .map(ProjectFeedSettingsDto::getProjectPostCommentPolicyType)
                .map(projectPostCommentPolicyType -> switch (projectPostCommentPolicyType) {
                    case WORKSPACE_ADMINS -> isAccountWorkspaceAdminOrOwner;
                    case PROJECT_LEAD -> isAccountWorkspaceAdminOrOwner || isAccountProjectLead;
                    case PROJECT_TEAM_ADMINS -> isAccountWorkspaceAdminOrOwner || isAccountIsProjectTeamsAdmin;
                    case PROJECT_TEAM_MEMBERS -> isAccountWorkspaceAdminOrOwner || isAccountIsProjectTeamsMember;
                    case WORKSPACE_MEMBERS -> isAccountWorkspaceAdminOrOwner || isWorkspaceMember;
                    case ANY_LOGGED_IN_USER -> Boolean.TRUE;
                })
                .orElse(Boolean.FALSE);

        return AccountProjectPermissionFlags.builder()
                .isAccountWorkspaceAdminOrOwner(isAccountWorkspaceAdminOrOwner)
                .isAccountIsProjectTeamsMember(isAccountIsProjectTeamsMember)
                .isAccountIsProjectTeamsAdmin(isAccountIsProjectTeamsAdmin)
                .canInitializePost(canInitializePost)
                .canComment(canComment)
                .build();
    }

    private void validateAccountIsProjectTeamsMember(String accountId, ProjectDto projectDto) {
        if (!isAccountIsProjectTeamsMember(accountId, projectDto)) {
            throw new NoAccessException();
        }
    }

    private boolean isAccountIsProjectTeamsMember(String accountId, ProjectDto projectDto) {
        return Optional.of(projectDto)
                .map(ProjectDto::getProjectTeams)
                .map(Collection::stream)
                .map(projectTeamDtoStream -> projectTeamDtoStream
                        .map(ProjectTeamDto::getTeamId)
                        .map(teamId -> teamAccessValidator.checkTeamAccess(accountId, projectDto.getWorkspaceId(), teamId))
                        .reduce(false, (accumulatedAccess, current) -> accumulatedAccess || current)
                )
                .filter(Boolean.TRUE::equals)
                .orElse(Boolean.FALSE);
    }

    private void validateAccountIsProjectTeamsAdmin(String accountId, ProjectDto projectDto) {
        if (!isAccountIsProjectTeamsAdmin(accountId, projectDto)) {
            throw new NoAccessException();
        }
    }

    private boolean isAccountIsProjectTeamsAdmin(String accountId, ProjectDto projectDto) {
        return Optional.of(projectDto)
                .map(ProjectDto::getProjectTeams)
                .map(Collection::stream)
                .map(projectTeamDtoStream -> projectTeamDtoStream
                        .map(ProjectTeamDto::getTeamId)
                        .map(teamId -> teamAccessValidator.checkTeamAdminAccess(accountId, projectDto.getWorkspaceId(), teamId))
                        .reduce(false, (accumulatedAccess, current) -> accumulatedAccess || current)
                )
                .filter(Boolean.TRUE::equals)
                .orElse(Boolean.FALSE);
    }
}
