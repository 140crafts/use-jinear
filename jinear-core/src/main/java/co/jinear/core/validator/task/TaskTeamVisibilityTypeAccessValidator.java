package co.jinear.core.validator.task;

import co.jinear.core.exception.NoAccessException;
import co.jinear.core.model.dto.team.TeamDto;
import co.jinear.core.model.dto.team.member.TeamMemberDto;
import co.jinear.core.model.enumtype.team.TeamMemberRoleType;
import co.jinear.core.model.enumtype.team.TeamTaskVisibilityType;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import static co.jinear.core.model.enumtype.team.TeamMemberRoleType.ADMIN;
import static co.jinear.core.model.enumtype.team.TeamTaskVisibilityType.OWNER_ASSIGNEE_AND_ADMINS;

@Slf4j
@Component
@RequiredArgsConstructor
public class TaskTeamVisibilityTypeAccessValidator {

    public void validateTaskAccess(String currentAccountId, TeamMemberDto teamMemberDto, String ownerId, String assignedTo) {
        log.info("Validating task access. currentAccountId: {}, ownerId: {}, assignedTo: {}", currentAccountId, ownerId, assignedTo);
        if (!hasTaskAccess(currentAccountId, teamMemberDto, ownerId, assignedTo)) {
            throw new NoAccessException();
        }
    }

    public boolean hasTaskAccess(String currentAccountId, TeamMemberDto teamMemberDto, String ownerId, String assignedTo) {
        TeamDto teamDto = teamMemberDto.getTeam();
        TeamTaskVisibilityType taskVisibility = teamDto.getTaskVisibility();
        TeamMemberRoleType role = teamMemberDto.getRole();
        log.info("Has task access control started. currentAccountId: {}, teamId: {}, taskVisibility: {}, role:{}, ownerId: {}, assignedTo: {}", currentAccountId, teamMemberDto.getTeamId(), role, taskVisibility, ownerId, assignedTo);
        if (OWNER_ASSIGNEE_AND_ADMINS.equals(taskVisibility)) {
            return ADMIN.equals(role) || (currentAccountId.equalsIgnoreCase(ownerId) || currentAccountId.equalsIgnoreCase(assignedTo));
        }
        return Boolean.TRUE;
    }
}
