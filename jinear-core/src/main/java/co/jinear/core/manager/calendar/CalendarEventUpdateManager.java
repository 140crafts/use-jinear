package co.jinear.core.manager.calendar;

import co.jinear.core.converter.calendar.UpdateExternalEventDatesRequestToVoConverter;
import co.jinear.core.converter.calendar.UpdateExternalEventTitleDescriptionToVoConverter;
import co.jinear.core.model.dto.calendar.CalendarDto;
import co.jinear.core.model.dto.integration.IntegrationInfoDto;
import co.jinear.core.model.request.calendar.CalendarEventDateUpdateRequest;
import co.jinear.core.model.request.calendar.CalendarEventTitleDescriptionUpdateRequest;
import co.jinear.core.model.response.BaseResponse;
import co.jinear.core.model.vo.calendar.UpdateExternalEventDatesVo;
import co.jinear.core.model.vo.calendar.UpdateExternalEventTitleDescriptionVo;
import co.jinear.core.service.SessionInfoService;
import co.jinear.core.service.calendar.CalendarExternalEventUpdateService;
import co.jinear.core.service.calendar.CalendarRetrieveService;
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
    private final CalendarExternalEventUpdateService calendarExternalEventUpdateService;
    private final UpdateExternalEventDatesRequestToVoConverter updateExternalEventDatesRequestToVoConverter;
    private final UpdateExternalEventTitleDescriptionToVoConverter updateExternalEventTitleDescriptionToVoConverter;

    public BaseResponse updateEventDate(CalendarEventDateUpdateRequest calendarEventDateUpdateRequest) {
        String currentAccountId = sessionInfoService.currentAccountId();
        CalendarDto calendarDto = calendarRetrieveService.retrieveCalendar(calendarEventDateUpdateRequest.getCalendarId());
        workspaceValidator.validateHasAccess(currentAccountId, calendarDto.getWorkspaceId());
        //TODO: cgds-302 add calendar member validation
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
        //TODO: cgds-302 add calendar member validation
        log.info("Update event title and description has started.");

        IntegrationInfoDto integrationInfoDto = calendarDto.getIntegrationInfo();
        UpdateExternalEventTitleDescriptionVo updateExternalEventTitleDescriptionVo = updateExternalEventTitleDescriptionToVoConverter.converter(calendarEventTitleDescriptionUpdateRequest);
        calendarExternalEventUpdateService.updateCalendarEventTitleDescription(integrationInfoDto, updateExternalEventTitleDescriptionVo);

        return new BaseResponse();
    }
}
