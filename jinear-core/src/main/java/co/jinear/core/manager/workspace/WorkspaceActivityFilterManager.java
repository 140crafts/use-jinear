package co.jinear.core.manager.workspace;

import co.jinear.core.converter.workspace.WorkspaceActivityFilterRequestConverter;
import co.jinear.core.exception.NoAccessException;
import co.jinear.core.exception.NotValidException;
import co.jinear.core.model.dto.PageDto;
import co.jinear.core.model.dto.team.TeamDto;
import co.jinear.core.model.dto.team.member.TeamMemberDto;
import co.jinear.core.model.dto.workspace.WorkspaceActivityDto;
import co.jinear.core.model.enumtype.team.TeamMemberRoleType;
import co.jinear.core.model.request.workspace.WorkspaceActivityFilterRequest;
import co.jinear.core.model.response.workspace.WorkspaceActivityListResponse;
import co.jinear.core.model.vo.workspace.WorkspaceActivityFilterVo;
import co.jinear.core.service.SessionInfoService;
import co.jinear.core.service.team.member.TeamMemberRetrieveService;
import co.jinear.core.service.workspace.activity.WorkspaceActivityFilterService;
import co.jinear.core.validator.workspace.WorkspaceValidator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.logging.log4j.util.Strings;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import static co.jinear.core.model.enumtype.team.TeamTaskVisibilityType.OWNER_ASSIGNEE_AND_ADMINS;

@Slf4j
@Service
@RequiredArgsConstructor
public class WorkspaceActivityFilterManager {

    private final SessionInfoService sessionInfoService;
    private final WorkspaceValidator workspaceValidator;
    private final TeamMemberRetrieveService teamMemberRetrieveService;
    private final WorkspaceActivityFilterRequestConverter workspaceActivityFilterRequestConverter;
    private final WorkspaceActivityFilterService workspaceActivityFilterService;

    public WorkspaceActivityListResponse filterActivities(WorkspaceActivityFilterRequest workspaceActivityFilterRequest) {
        String currentAccount = sessionInfoService.currentAccountId();
        validateWorkspaceAccess(currentAccount, workspaceActivityFilterRequest.getWorkspaceId());

        validateTeamIdListHasNoBlankElement(workspaceActivityFilterRequest);
        List<TeamMemberDto> memberships = retrieveMemberships(workspaceActivityFilterRequest, currentAccount);
        validateAccountMembershipsInRequestedTeams(workspaceActivityFilterRequest, memberships);
        validateTeamTaskVisibilityAndMemberRoleForAll(memberships);
        WorkspaceActivityFilterVo workspaceActivityFilterVo = workspaceActivityFilterRequestConverter.convert(workspaceActivityFilterRequest, memberships);
        Page<WorkspaceActivityDto> workspaceActivityDtoPage = workspaceActivityFilterService.filterActivities(workspaceActivityFilterVo);
        return mapResponse(workspaceActivityDtoPage);
    }

    private List<TeamMemberDto> retrieveMemberships(WorkspaceActivityFilterRequest workspaceActivityFilterRequest, String currentAccount) {
        String workspaceId = workspaceActivityFilterRequest.getWorkspaceId();
        return Optional.of(workspaceActivityFilterRequest)
                .map(WorkspaceActivityFilterRequest::getTeamIdList)
                .filter(teamIds -> !teamIds.isEmpty())
                .map(teamIds -> teamMemberRetrieveService.retrieveMemberships(workspaceId, currentAccount, teamIds))
                .orElseGet(() -> teamMemberRetrieveService.retrieveAllTeamMembershipsOfAnAccount(currentAccount, workspaceId));

    }

    private void validateWorkspaceAccess(String accountId, String workspaceId) {
        workspaceValidator.validateHasAccess(accountId, workspaceId);
    }

    private void validateTeamTaskVisibilityAndMemberRoleForAll(List<TeamMemberDto> memberships) {
        memberships.forEach(this::validateTeamTaskVisibilityAndMemberRole);
    }

    private void validateAccountMembershipsInRequestedTeams(WorkspaceActivityFilterRequest workspaceActivityFilterRequest, List<TeamMemberDto> memberships) {
        List<String> teamIdList = workspaceActivityFilterRequest.getTeamIdList();
        if (Objects.nonNull(teamIdList) && memberships.size() != teamIdList.size()) {
            throw new NoAccessException();
        }
    }

    private void validateTeamTaskVisibilityAndMemberRole(TeamMemberDto teamMemberDto) {
        TeamDto teamDto = teamMemberDto.getTeam();
        TeamMemberRoleType role = teamMemberDto.getRole();

        if (OWNER_ASSIGNEE_AND_ADMINS.equals(teamDto.getTaskVisibility()) && !List.of(TeamMemberRoleType.ADMIN, TeamMemberRoleType.MEMBER).contains(role)) {
            throw new NoAccessException();
        }
    }

    private void validateTeamIdListHasNoBlankElement(WorkspaceActivityFilterRequest workspaceActivityFilterRequest) {
        Optional.of(workspaceActivityFilterRequest)
                .map(WorkspaceActivityFilterRequest::getTeamIdList)
                .map(Collection::stream)
                .filter(teamIdStream -> teamIdStream.anyMatch(Strings::isBlank))
                .ifPresent(teamIdStream -> {
                    throw new NotValidException();
                });
    }

    private WorkspaceActivityListResponse mapResponse(Page<WorkspaceActivityDto> workspaceActivityDtoPage) {
        WorkspaceActivityListResponse workspaceActivityListResponse = new WorkspaceActivityListResponse();
        workspaceActivityListResponse.setWorkspaceActivityDtoPageDto(new PageDto<>(workspaceActivityDtoPage));
        return workspaceActivityListResponse;
    }
}
