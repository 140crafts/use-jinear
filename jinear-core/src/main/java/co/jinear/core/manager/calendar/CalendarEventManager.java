package co.jinear.core.manager.calendar;

import co.jinear.core.converter.calendar.CalendarEventFilterRequestToTaskSearchFilterVoConverter;
import co.jinear.core.converter.calendar.CalendarEventsToICalConverter;
import co.jinear.core.converter.calendar.TaskDtoToCalendarEventDtoMapper;
import co.jinear.core.exception.NoAccessException;
import co.jinear.core.exception.NotValidException;
import co.jinear.core.model.dto.calendar.CalendarEventDto;
import co.jinear.core.model.dto.calendar.CalendarShareKeyDto;
import co.jinear.core.model.dto.task.TaskDto;
import co.jinear.core.model.dto.team.TeamDto;
import co.jinear.core.model.dto.team.member.TeamMemberDto;
import co.jinear.core.model.enumtype.team.TeamMemberRoleType;
import co.jinear.core.model.request.calendar.CalendarEventFilterRequest;
import co.jinear.core.model.response.BaseResponse;
import co.jinear.core.model.response.calendar.CalendarEventListingResponse;
import co.jinear.core.model.response.calendar.CalendarShareableKeyResponse;
import co.jinear.core.model.vo.calendar.CalendarEventSearchFilterVo;
import co.jinear.core.model.vo.task.TaskSearchFilterVo;
import co.jinear.core.service.SessionInfoService;
import co.jinear.core.service.calendar.CalendarExternalEventRetrieveService;
import co.jinear.core.service.calendar.CalendarShareKeyService;
import co.jinear.core.service.task.TaskListingService;
import co.jinear.core.service.team.member.TeamMemberRetrieveService;
import co.jinear.core.validator.workspace.WorkspaceValidator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.logging.log4j.util.Strings;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;

import java.time.ZonedDateTime;
import java.util.*;
import java.util.stream.Stream;

import static co.jinear.core.model.enumtype.team.TeamTaskVisibilityType.OWNER_ASSIGNEE_AND_ADMINS;

@Slf4j
@Service
@RequiredArgsConstructor
public class CalendarEventManager {

    private final SessionInfoService sessionInfoService;
    private final WorkspaceValidator workspaceValidator;
    private final TeamMemberRetrieveService teamMemberRetrieveService;
    private final CalendarEventFilterRequestToTaskSearchFilterVoConverter calendarEventFilterRequestToTaskSearchFilterVoConverter;
    private final TaskListingService taskListingService;
    private final TaskDtoToCalendarEventDtoMapper taskDtoToCalendarEventDtoMapper;
    private final CalendarExternalEventRetrieveService calendarExternalEventRetrieveService;
    private final CalendarShareKeyService calendarShareKeyService;
    private final CalendarEventsToICalConverter calendarEventsToICalConverter;

    public CalendarEventListingResponse filterCalendarEvents(CalendarEventFilterRequest calendarEventFilterRequest) {
        String currentAccount = sessionInfoService.currentAccountId();
        validateWorkspaceAccess(currentAccount, calendarEventFilterRequest.getWorkspaceId());
        validateTeamIdListHasNoBlankElement(calendarEventFilterRequest);

        List<TeamMemberDto> memberships = retrieveMemberships(calendarEventFilterRequest, currentAccount);
        validateAccountMembershipsInRequestedTeams(calendarEventFilterRequest, memberships);
        validateTeamTaskVisibilityAndMemberRoleForAll(memberships);

        CalendarEventSearchFilterVo calendarEventSearchFilterVo = calendarEventFilterRequestToTaskSearchFilterVoConverter.convert(calendarEventFilterRequest, memberships);

        List<CalendarEventDto> all = new ArrayList<>();
        List<CalendarEventDto> taskCalendarEventDtos = retrieveTaskCalendarEvents(calendarEventSearchFilterVo);
        List<CalendarEventDto> externalCalendarOriginatedEvents = calendarExternalEventRetrieveService.retrieveExternalCalendarTasks(currentAccount, calendarEventSearchFilterVo);
        all.addAll(taskCalendarEventDtos);
        all.addAll(externalCalendarOriginatedEvents);

        CalendarEventListingResponse calendarEventListingResponse = new CalendarEventListingResponse();
        calendarEventListingResponse.setCalendarEventDtoList(all);
        return calendarEventListingResponse;
    }

    public CalendarShareableKeyResponse retrieveShareableKey(String workspaceId) {
        String currentAccount = sessionInfoService.currentAccountId();
        validateWorkspaceAccess(currentAccount, workspaceId);
        log.info("Retrieve shareable key has started. currentAccount: {}, workspaceId: {}", currentAccount, workspaceId);
        CalendarShareKeyDto calendarShareKeyDto = calendarShareKeyService.retrieveShareableKey(currentAccount, workspaceId);
        return mapResponse(calendarShareKeyDto);
    }

    public String exportWorkspaceCalendarEvents(String shareableKey) {
        CalendarShareKeyDto calendarShareKeyDto = calendarShareKeyService.retrieveShareableKey(shareableKey);
        validateWorkspaceAccess(calendarShareKeyDto.getAccountId(), calendarShareKeyDto.getWorkspaceId());
        log.info("Export ics has started. calendarShareKeyDto: {}", calendarShareKeyDto);

        String currentAccount = calendarShareKeyDto.getAccountId();
        String workspaceId = calendarShareKeyDto.getWorkspaceId();
        ZonedDateTime start = ZonedDateTime.now().minusYears(1);
        ZonedDateTime end = ZonedDateTime.now().plusYears(1);

        List<TeamMemberDto> memberships = teamMemberRetrieveService.retrieveAllTeamMembershipsOfAnAccount(currentAccount, workspaceId);
        validateTeamTaskVisibilityAndMemberRoleForAll(memberships);
        CalendarEventSearchFilterVo calendarEventSearchFilterVo = calendarEventFilterRequestToTaskSearchFilterVoConverter.convert(workspaceId, start, end, memberships);
        List<CalendarEventDto> taskCalendarEventDtos = retrieveTaskCalendarEvents(calendarEventSearchFilterVo);

        return calendarEventsToICalConverter.getICalendarString(workspaceId, taskCalendarEventDtos);
    }

    public BaseResponse refreshShareableKey(String workspaceId) {
        String currentAccount = sessionInfoService.currentAccountId();
        validateWorkspaceAccess(currentAccount, workspaceId);
        log.info("Refresh shareable key has started. currentAccount: {}, workspaceId: {}", currentAccount, workspaceId);
        calendarShareKeyService.refreshShareableKey(currentAccount, workspaceId);
        return new BaseResponse();
    }

    private List<CalendarEventDto> retrieveTaskCalendarEvents(TaskSearchFilterVo taskSearchFilterVo) {
        Page<TaskDto> taskDtoPage = taskListingService.filterTasks(taskSearchFilterVo);
        return Optional.of(taskDtoPage)
                .map(Slice::getContent)
                .map(Collection::stream)
                .map(taskDtoStream -> taskDtoStream.map(taskDtoToCalendarEventDtoMapper::map))
                .map(Stream::toList)
                .orElseGet(Collections::emptyList);
    }

    private void validateWorkspaceAccess(String accountId, String workspaceId) {
        workspaceValidator.validateHasAccess(accountId, workspaceId);
    }

    private void validateTeamIdListHasNoBlankElement(CalendarEventFilterRequest calendarEventFilterRequest) {
        Optional.of(calendarEventFilterRequest)
                .map(CalendarEventFilterRequest::getTeamIdList)
                .map(Collection::stream)
                .filter(teamIdStream -> teamIdStream.anyMatch(Strings::isBlank))
                .ifPresent(teamIdStream -> {
                    throw new NotValidException();
                });
    }

    private List<TeamMemberDto> retrieveMemberships(CalendarEventFilterRequest calendarEventFilterRequest, String currentAccount) {
        String workspaceId = calendarEventFilterRequest.getWorkspaceId();
        return Optional.of(calendarEventFilterRequest)
                .map(CalendarEventFilterRequest::getTeamIdList)
                .filter(teamIds -> !teamIds.isEmpty())
                .map(teamIds -> teamMemberRetrieveService.retrieveMemberships(workspaceId, currentAccount, teamIds))
                .orElseGet(() -> teamMemberRetrieveService.retrieveAllTeamMembershipsOfAnAccount(currentAccount, workspaceId));

    }

    private void validateAccountMembershipsInRequestedTeams(CalendarEventFilterRequest calendarEventFilterRequest, List<TeamMemberDto> memberships) {
        List<String> teamIdList = calendarEventFilterRequest.getTeamIdList();
        if (Objects.nonNull(teamIdList) && memberships.size() != teamIdList.size()) {
            throw new NoAccessException();
        }
    }

    private void validateTeamTaskVisibilityAndMemberRoleForAll(List<TeamMemberDto> memberships) {
        memberships.forEach(this::validateTeamTaskVisibilityAndMemberRole);
    }

    private void validateTeamTaskVisibilityAndMemberRole(TeamMemberDto teamMemberDto) {
        TeamDto teamDto = teamMemberDto.getTeam();
        TeamMemberRoleType role = teamMemberDto.getRole();

        if (OWNER_ASSIGNEE_AND_ADMINS.equals(teamDto.getTaskVisibility()) && !List.of(TeamMemberRoleType.ADMIN, TeamMemberRoleType.MEMBER).contains(role)) {
            throw new NoAccessException();
        }
    }

    private CalendarShareableKeyResponse mapResponse(CalendarShareKeyDto calendarShareKeyDto) {
        CalendarShareableKeyResponse calendarShareableKeyResponse = new CalendarShareableKeyResponse();
        calendarShareableKeyResponse.setCalendarShareKeyDto(calendarShareKeyDto);
        return calendarShareableKeyResponse;
    }
}
