package co.jinear.core.manager.calendar;

import co.jinear.core.converter.calendar.CalendarEventInitializeRequestToVoConverter;
import co.jinear.core.converter.calendar.CalendarEventMoveRequestToVoConverter;
import co.jinear.core.converter.calendar.UpdateExternalEventDatesRequestToVoConverter;
import co.jinear.core.converter.calendar.UpdateExternalEventTitleDescriptionToVoConverter;
import co.jinear.core.model.dto.calendar.CalendarDto;
import co.jinear.core.model.dto.integration.IntegrationInfoDto;
import co.jinear.core.model.request.calendar.CalendarEventDateUpdateRequest;
import co.jinear.core.model.request.calendar.CalendarEventInitializeRequest;
import co.jinear.core.model.request.calendar.CalendarEventMoveRequest;
import co.jinear.core.model.request.calendar.CalendarEventTitleDescriptionUpdateRequest;
import co.jinear.core.model.response.BaseResponse;
import co.jinear.core.model.vo.calendar.InitializeExternalEventVo;
import co.jinear.core.model.vo.calendar.MoveExternalEventVo;
import co.jinear.core.model.vo.calendar.UpdateExternalEventDatesVo;
import co.jinear.core.model.vo.calendar.UpdateExternalEventTitleDescriptionVo;
import co.jinear.core.service.SessionInfoService;
import co.jinear.core.service.calendar.CalendarExternalEventOperationService;
import co.jinear.core.service.calendar.CalendarRetrieveService;
import co.jinear.core.validator.calendar.CalendarAccessValidator;
import co.jinear.core.validator.workspace.WorkspaceValidator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class CalendarEventUpdateManager {

    private final SessionInfoService sessionInfoService;
    private final CalendarRetrieveService calendarRetrieveService;
    private final WorkspaceValidator workspaceValidator;
    private final CalendarExternalEventOperationService calendarExternalEventUpdateService;
    private final UpdateExternalEventDatesRequestToVoConverter updateExternalEventDatesRequestToVoConverter;
    private final UpdateExternalEventTitleDescriptionToVoConverter updateExternalEventTitleDescriptionToVoConverter;
    private final CalendarEventInitializeRequestToVoConverter calendarEventInitializeRequestToVoConverter;
    private final CalendarEventMoveRequestToVoConverter calendarEventMoveRequestToVoConverter;
    private final CalendarAccessValidator calendarAccessValidator;

    public BaseResponse insertEvent(CalendarEventInitializeRequest calendarEventInitializeRequest) {
        String currentAccountId = sessionInfoService.currentAccountId();
        CalendarDto calendarDto = calendarRetrieveService.retrieveCalendar(calendarEventInitializeRequest.getCalendarId());
        workspaceValidator.validateHasAccess(currentAccountId, calendarDto.getWorkspaceId());
        calendarAccessValidator.validateHasCalendarAccess(currentAccountId, calendarDto.getCalendarId());
        log.info("Insert event has started.");

        IntegrationInfoDto integrationInfoDto = calendarDto.getIntegrationInfo();
        InitializeExternalEventVo initializeExternalEventVo = calendarEventInitializeRequestToVoConverter.convert(calendarEventInitializeRequest);
        calendarExternalEventUpdateService.insertCalendarEvent(integrationInfoDto, initializeExternalEventVo);

        return new BaseResponse();
    }

    public BaseResponse moveEvent(CalendarEventMoveRequest calendarEventMoveRequest) {
        String currentAccountId = sessionInfoService.currentAccountId();
        CalendarDto calendarDto = calendarRetrieveService.retrieveCalendar(calendarEventMoveRequest.getCalendarId());
        workspaceValidator.validateHasAccess(currentAccountId, calendarDto.getWorkspaceId());
        calendarAccessValidator.validateHasCalendarAccess(currentAccountId, calendarDto.getCalendarId());
        log.info("Move event has started.");

        IntegrationInfoDto integrationInfoDto = calendarDto.getIntegrationInfo();
        MoveExternalEventVo moveExternalEventVo = calendarEventMoveRequestToVoConverter.convert(calendarEventMoveRequest);
        calendarExternalEventUpdateService.moveCalendarEvent(integrationInfoDto, moveExternalEventVo);

        return new BaseResponse();
    }

    public BaseResponse updateEventDate(CalendarEventDateUpdateRequest calendarEventDateUpdateRequest) {
        String currentAccountId = sessionInfoService.currentAccountId();
        CalendarDto calendarDto = calendarRetrieveService.retrieveCalendar(calendarEventDateUpdateRequest.getCalendarId());
        workspaceValidator.validateHasAccess(currentAccountId, calendarDto.getWorkspaceId());
        calendarAccessValidator.validateHasCalendarAccess(currentAccountId, calendarDto.getCalendarId());
        log.info("Update event dates has started.");

        IntegrationInfoDto integrationInfoDto = calendarDto.getIntegrationInfo();
        UpdateExternalEventDatesVo updateExternalEventDatesVo = updateExternalEventDatesRequestToVoConverter.convert(calendarEventDateUpdateRequest);
        calendarExternalEventUpdateService.updateCalendarEventDates(integrationInfoDto, updateExternalEventDatesVo);

        return new BaseResponse();
    }

    public BaseResponse updateTitleAndDescription(CalendarEventTitleDescriptionUpdateRequest calendarEventTitleDescriptionUpdateRequest) {
        String currentAccountId = sessionInfoService.currentAccountId();
        CalendarDto calendarDto = calendarRetrieveService.retrieveCalendar(calendarEventTitleDescriptionUpdateRequest.getCalendarId());
        workspaceValidator.validateHasAccess(currentAccountId, calendarDto.getWorkspaceId());
        calendarAccessValidator.validateHasCalendarAccess(currentAccountId, calendarDto.getCalendarId());
        log.info("Update event title and description has started.");

        IntegrationInfoDto integrationInfoDto = calendarDto.getIntegrationInfo();
        UpdateExternalEventTitleDescriptionVo updateExternalEventTitleDescriptionVo = updateExternalEventTitleDescriptionToVoConverter.converter(calendarEventTitleDescriptionUpdateRequest);
        calendarExternalEventUpdateService.updateCalendarEventTitleDescription(integrationInfoDto, updateExternalEventTitleDescriptionVo);

        return new BaseResponse();
    }
}
