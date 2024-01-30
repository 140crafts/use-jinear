package co.jinear.core.manager.calendar;

import co.jinear.core.converter.calendar.AddCalendarMemberVoConverter;
import co.jinear.core.exception.BusinessException;
import co.jinear.core.model.dto.PageDto;
import co.jinear.core.model.dto.calendar.CalendarDto;
import co.jinear.core.model.dto.calendar.CalendarMemberDto;
import co.jinear.core.model.response.BaseResponse;
import co.jinear.core.model.response.calendar.CalendarMemberListingResponse;
import co.jinear.core.model.response.calendar.CalendarMemberPaginatedResponse;
import co.jinear.core.model.vo.calendar.AddCalendarMemberVo;
import co.jinear.core.service.SessionInfoService;
import co.jinear.core.service.calendar.CalendarMemberOperationService;
import co.jinear.core.service.calendar.CalendarMemberRetrieveService;
import co.jinear.core.service.calendar.CalendarRetrieveService;
import co.jinear.core.service.passive.PassiveService;
import co.jinear.core.validator.calendar.CalendarAccessValidator;
import co.jinear.core.validator.workspace.WorkspaceValidator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class CalendarMemberManager {

    private final WorkspaceValidator workspaceValidator;
    private final SessionInfoService sessionInfoService;
    private final CalendarMemberRetrieveService calendarMemberRetrieveService;
    private final CalendarAccessValidator calendarAccessValidator;
    private final CalendarRetrieveService calendarRetrieveService;
    private final CalendarMemberOperationService calendarMemberOperationService;
    private final AddCalendarMemberVoConverter addCalendarMemberVoConverter;
    private final PassiveService passiveService;

    public CalendarMemberListingResponse retrieveMemberCalendars(String workspaceId) {
        String currentAccountId = sessionInfoService.currentAccountId();
        workspaceValidator.validateHasAccess(currentAccountId, workspaceId);
        log.info("Retrieve member calendars has started. currentAccountId: {}", currentAccountId);
        List<CalendarMemberDto> calendarMemberDtos = calendarMemberRetrieveService.retrieveAccountCalendarsIncludingExternalSources(currentAccountId, workspaceId);
        return mapResponse(calendarMemberDtos);
    }

    public CalendarMemberPaginatedResponse retrieveCalendarMembers(String calendarId, int page) {
        String currentAccountId = sessionInfoService.currentAccountId();
        calendarAccessValidator.validateHasCalendarAccess(currentAccountId, calendarId);
        log.info("Retrieve calendar members has started for calendarId: {}, page: {}", calendarId, page);
        Page<CalendarMemberDto> calendarMemberDtos = calendarMemberRetrieveService.findAllByCalendarIdAndPassiveIdIsNull(calendarId, page);
        return mapResponse(calendarMemberDtos);
    }

    public BaseResponse addCalendarMember(String calendarId, String accountId) {
        String currentAccountId = sessionInfoService.currentAccountId();
        calendarAccessValidator.validateHasAdminAccess(currentAccountId, calendarId);
        log.info("Add calendar member has started. currentAccountId: {}, calendarId: {}, accountId: {}", currentAccountId, calendarId, accountId);
        CalendarDto calendarDto = calendarRetrieveService.retrieveCalendar(calendarId);
        AddCalendarMemberVo addCalendarMemberVo = addCalendarMemberVoConverter.map(calendarDto, accountId);
        calendarMemberOperationService.addCalendarMember(addCalendarMemberVo);
        return new BaseResponse();
    }

    public BaseResponse kickCalendarMember(String calendarId, String accountId) {
        String currentAccountId = sessionInfoService.currentAccountId();
        calendarAccessValidator.validateHasAdminAccess(currentAccountId, calendarId);
        validateKickYourself(accountId, currentAccountId);
        log.info("Kick calendar member has started. currentAccountId: {}, calendarId: {}, accountId: {}", currentAccountId, calendarId, accountId);
        String passiveId = passiveService.createUserActionPassive();
        calendarMemberOperationService.removeCalendarMember(calendarId, accountId, passiveId);
        passiveService.assignOwnership(passiveId, accountId);
        return new BaseResponse();
    }

    private CalendarMemberPaginatedResponse mapResponse(Page<CalendarMemberDto> calendarMemberDtos) {
        CalendarMemberPaginatedResponse calendarMemberPaginatedResponse = new CalendarMemberPaginatedResponse();
        calendarMemberPaginatedResponse.setCalendarMemberDtos(new PageDto<>(calendarMemberDtos));
        return calendarMemberPaginatedResponse;
    }

    private CalendarMemberListingResponse mapResponse(List<CalendarMemberDto> calendarMemberDtos) {
        CalendarMemberListingResponse calendarMemberListingResponse = new CalendarMemberListingResponse();
        calendarMemberListingResponse.setCalendarMemberDtos(calendarMemberDtos);
        return calendarMemberListingResponse;
    }

    private void validateKickYourself(String accountId, String currentAccountId) {
        if (currentAccountId.equalsIgnoreCase(accountId)) {
            throw new BusinessException("calendar.members.manage.cannot-kick-yourself");
        }
    }
}
