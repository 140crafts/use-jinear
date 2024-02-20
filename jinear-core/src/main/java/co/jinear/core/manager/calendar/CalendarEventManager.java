package co.jinear.core.manager.calendar;

import biweekly.Biweekly;
import biweekly.ICalendar;
import biweekly.component.VEvent;
import biweekly.property.DateEnd;
import biweekly.property.DateStart;
import biweekly.property.Description;
import biweekly.property.Summary;
import co.jinear.core.converter.calendar.CalendarEventFilterRequestToTaskSearchFilterVoConverter;
import co.jinear.core.converter.calendar.TaskDtoToCalendarEventDtoMapper;
import co.jinear.core.exception.NoAccessException;
import co.jinear.core.exception.NotValidException;
import co.jinear.core.model.dto.calendar.CalendarEventDto;
import co.jinear.core.model.dto.task.TaskDto;
import co.jinear.core.model.dto.team.TeamDto;
import co.jinear.core.model.dto.team.member.TeamMemberDto;
import co.jinear.core.model.enumtype.team.TeamMemberRoleType;
import co.jinear.core.model.request.calendar.CalendarEventFilterRequest;
import co.jinear.core.model.response.calendar.CalendarEventListingResponse;
import co.jinear.core.model.vo.calendar.CalendarEventSearchFilterVo;
import co.jinear.core.model.vo.task.TaskSearchFilterVo;
import co.jinear.core.service.SessionInfoService;
import co.jinear.core.service.calendar.CalendarExternalEventRetrieveService;
import co.jinear.core.service.task.TaskListingService;
import co.jinear.core.service.team.member.TeamMemberRetrieveService;
import co.jinear.core.validator.workspace.WorkspaceValidator;
import lombok.NonNull;
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

    public String exportIcs(String workspaceId) {
//        String currentAccount = sessionInfoService.currentAccountId();
        String currentAccount = "01gp94s0sk9q4g8g3m9jpsvd0t";
        validateWorkspaceAccess(currentAccount, workspaceId);
        log.info("Export ics has started. workspaceId: {}, currentAccount: {}", workspaceId, currentAccount);
        List<TeamMemberDto> memberships = teamMemberRetrieveService.retrieveAllTeamMembershipsOfAnAccount(currentAccount, workspaceId);
        validateTeamTaskVisibilityAndMemberRoleForAll(memberships);
        CalendarEventSearchFilterVo calendarEventSearchFilterVo = calendarEventFilterRequestToTaskSearchFilterVoConverter.convert(workspaceId, ZonedDateTime.now().minusYears(1), ZonedDateTime.now().plusYears(1), memberships);
        List<CalendarEventDto> taskCalendarEventDtos = retrieveTaskCalendarEvents(calendarEventSearchFilterVo);

        ICalendar ical = new ICalendar();
        taskCalendarEventDtos.forEach(calendarEventDto -> {
            VEvent event = new VEvent();
            Summary summary = event.setSummary(calendarEventDto.getTitle());
            event.setSummary(summary);
            if (calendarEventDto.getAssignedDate() != null) {
                DateStart dateStart = new DateStart(Date.from(calendarEventDto.getAssignedDate().toInstant()), calendarEventDto.getHasPreciseAssignedDate() == null ? Boolean.FALSE : calendarEventDto.getHasPreciseAssignedDate());
                event.setDateStart(dateStart);
            }
            if (calendarEventDto.getDueDate() != null) {
                DateEnd dateEnd = new DateEnd(Date.from(calendarEventDto.getDueDate().toInstant()), calendarEventDto.getHasPreciseDueDate() == null ? Boolean.FALSE : calendarEventDto.getHasPreciseDueDate());
                event.setDateEnd(dateEnd);
            }
            if (calendarEventDto.getDescription() != null) {
                Description description = new Description(calendarEventDto.getDescription().getValue());
                event.setDescription(description);
            }
            ical.addEvent(event);
        });
        return Biweekly.write(ical).go();
    }

    @NonNull
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
}
