package co.jinear.core.manager.task;

import co.jinear.core.converter.task.TaskFilterRequestConverter;
import co.jinear.core.exception.NoAccessException;
import co.jinear.core.exception.NotValidException;
import co.jinear.core.model.dto.PageDto;
import co.jinear.core.model.dto.task.TaskDto;
import co.jinear.core.model.dto.team.TeamDto;
import co.jinear.core.model.dto.team.member.TeamMemberDto;
import co.jinear.core.model.enumtype.team.TeamMemberRoleType;
import co.jinear.core.model.request.task.TaskFilterRequest;
import co.jinear.core.model.response.task.TaskListingListedResponse;
import co.jinear.core.model.response.task.TaskListingPaginatedResponse;
import co.jinear.core.model.vo.task.TaskSearchFilterVo;
import co.jinear.core.service.SessionInfoService;
import co.jinear.core.service.calendar.CalendarEventRetrieveService;
import co.jinear.core.service.task.TaskListingService;
import co.jinear.core.service.team.member.TeamMemberRetrieveService;
import co.jinear.core.validator.workspace.WorkspaceValidator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.logging.log4j.util.Strings;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import java.util.*;

import static co.jinear.core.model.enumtype.team.TeamTaskVisibilityType.OWNER_ASSIGNEE_AND_ADMINS;

@Slf4j
@Service
@RequiredArgsConstructor
public class TaskListingManager {

    private final SessionInfoService sessionInfoService;
    private final WorkspaceValidator workspaceValidator;
    private final TaskListingService taskListingService;
    private final TeamMemberRetrieveService teamMemberRetrieveService;
    private final TaskFilterRequestConverter taskFilterRequestConverter;
    private final CalendarEventRetrieveService calendarEventRetrieveService;

    public TaskListingPaginatedResponse filterTasks(TaskFilterRequest taskFilterRequest) {
        Page<TaskDto> taskDtoPage = validateAndFilter(taskFilterRequest);
        return mapResponse(taskDtoPage);
    }

    public TaskListingListedResponse listedFilter(TaskFilterRequest taskFilterRequest) {
        Page<TaskDto> taskDtoPage = validateAndFilter(taskFilterRequest);
        List<TaskDto> allTasks = new ArrayList<>(taskDtoPage.getContent());
        List<TaskDto> calendarTasks = retrieveCalendarEvents(taskFilterRequest);
        allTasks.addAll(calendarTasks);

        return mapResponse(allTasks);
    }

    private Page<TaskDto> validateAndFilter(TaskFilterRequest taskFilterRequest) {
        String currentAccount = sessionInfoService.currentAccountId();
        validateWorkspaceAccess(currentAccount, taskFilterRequest.getWorkspaceId());

        validateTeamIdListHasNoBlankElement(taskFilterRequest);
        List<TeamMemberDto> memberships = retrieveMemberships(taskFilterRequest, currentAccount);
        validateAccountMembershipsInRequestedTeams(taskFilterRequest, memberships);
        validateTeamTaskVisibilityAndMemberRoleForAll(memberships);
        TaskSearchFilterVo taskSearchFilterVo = taskFilterRequestConverter.convert(taskFilterRequest, memberships);
        return taskListingService.filterTasks(taskSearchFilterVo);
    }

    private List<TaskDto> retrieveCalendarEvents(TaskFilterRequest taskFilterRequest) {
        if (Objects.nonNull(taskFilterRequest.getTimespanStart()) && Objects.nonNull(taskFilterRequest.getTimespanEnd())) {
            String currentAccount = sessionInfoService.currentAccountId();
            TaskSearchFilterVo taskSearchFilterVo = taskFilterRequestConverter.convert(taskFilterRequest);
            return calendarEventRetrieveService.retrieveCalendarTasks(currentAccount, taskSearchFilterVo);
        }
        return Collections.emptyList();
    }

    private List<TeamMemberDto> retrieveMemberships(TaskFilterRequest taskFilterRequest, String currentAccount) {
        String workspaceId = taskFilterRequest.getWorkspaceId();
        return Optional.of(taskFilterRequest)
                .map(TaskFilterRequest::getTeamIdList)
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

    private void validateAccountMembershipsInRequestedTeams(TaskFilterRequest taskFilterRequest, List<TeamMemberDto> memberships) {
        List<String> teamIdList = taskFilterRequest.getTeamIdList();
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

    private void validateTeamIdListHasNoBlankElement(TaskFilterRequest taskFilterRequest) {
        Optional.of(taskFilterRequest)
                .map(TaskFilterRequest::getTeamIdList)
                .map(Collection::stream)
                .filter(teamIdStream -> teamIdStream.anyMatch(Strings::isBlank))
                .ifPresent(teamIdStream -> {
                    throw new NotValidException();
                });
    }

    private TaskListingPaginatedResponse mapResponse(Page<TaskDto> taskDtoPage) {
        TaskListingPaginatedResponse taskListingPaginatedResponse = new TaskListingPaginatedResponse();
        taskListingPaginatedResponse.setTaskDtoPage(new PageDto<>(taskDtoPage));
        return taskListingPaginatedResponse;
    }

    private TaskListingListedResponse mapResponse(List<TaskDto> allTasks) {
        TaskListingListedResponse taskListingListedResponse = new TaskListingListedResponse();
        taskListingListedResponse.setTaskDtoList(allTasks);
        return taskListingListedResponse;
    }
}
