package co.jinear.core.validator.team;

import co.jinear.core.exception.NoAccessException;
import co.jinear.core.model.dto.team.TeamDto;
import co.jinear.core.model.enumtype.workspace.WorkspaceAccountRoleType;
import co.jinear.core.service.team.TeamRetrieveService;
import co.jinear.core.service.team.member.TeamMemberRetrieveService;
import co.jinear.core.service.workspace.member.WorkspaceMemberRetrieveService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Arrays;

import static co.jinear.core.model.enumtype.workspace.WorkspaceAccountRoleType.ADMIN;
import static co.jinear.core.model.enumtype.workspace.WorkspaceAccountRoleType.OWNER;

@Slf4j
@Component
@RequiredArgsConstructor
public class TeamAccessValidator {

    private final WorkspaceMemberRetrieveService workspaceMemberRetrieveService;
    private final TeamMemberRetrieveService teamMemberRetrieveService;
    private final TeamRetrieveService teamRetrieveService;

    public void validateTeamAccess(String accountId, String teamId) {
        TeamDto teamDto = teamRetrieveService.retrieveTeam(teamId);
        String workspaceId = teamDto.getWorkspaceId();
        validateTeamAccess(accountId, workspaceId, teamId);
    }

    public void validateTeamAccess(String accountId, String workspaceId, String teamId) {
        if (!isWorkspaceAdminOrOwner(accountId, workspaceId) || !isAccountTeamMember(accountId, teamId)) {
            throw new NoAccessException();
        }
    }

    private boolean isWorkspaceAdminOrOwner(String accountId, String workspaceId) {
        WorkspaceAccountRoleType workspaceAccountRoleType = workspaceMemberRetrieveService.retrieveAccountWorkspaceRole(accountId, workspaceId);
        return Arrays.asList(OWNER, ADMIN).contains(workspaceAccountRoleType);
    }

    public boolean isAccountTeamMember(String accountId, String teamId) {
        return teamMemberRetrieveService.isAccountTeamMember(accountId, teamId);
    }
}
