package co.jinear.core.service.integration.calendar;

import co.jinear.core.model.dto.calendar.CalendarEventDto;
import co.jinear.core.model.dto.calendar.ExternalCalendarSourceDto;
import co.jinear.core.model.dto.integration.IntegrationInfoDto;
import co.jinear.core.model.enumtype.integration.IntegrationProvider;
import co.jinear.core.model.vo.calendar.InitializeExternalEventVo;
import co.jinear.core.model.vo.calendar.MoveExternalEventVo;
import co.jinear.core.model.vo.calendar.UpdateExternalEventDatesVo;
import co.jinear.core.model.vo.calendar.UpdateExternalEventTitleDescriptionVo;
import co.jinear.core.system.gcloud.googleapis.model.calendar.request.RetrieveEventListRequest;

import java.util.List;

public interface IntegrationCalendarRetrieveStrategy {

    IntegrationProvider getProvider();

    ExternalCalendarSourceDto retrieveCalendarSource(IntegrationInfoDto integrationInfoDto, String externalCalendarSourceId);

    List<ExternalCalendarSourceDto> retrieveCalendarSources(IntegrationInfoDto integrationInfoDto);

    List<CalendarEventDto> retrieveCalendarEvents(IntegrationInfoDto integrationInfoDto, RetrieveEventListRequest retrieveEventListRequest);

    CalendarEventDto retrieveCalendarEvent(IntegrationInfoDto integrationInfoDto, String calendarSourceId, String eventId);

    void insertCalendarEvent(IntegrationInfoDto integrationInfoDto, InitializeExternalEventVo initializeExternalEventVo);

    void moveCalendarEvent(IntegrationInfoDto integrationInfoDto, MoveExternalEventVo moveExternalEventVo);

    void updateCalendarEventDates(IntegrationInfoDto integrationInfoDto, UpdateExternalEventDatesVo updateExternalEventDatesVo);

    void updateCalendarEventTitleDescription(IntegrationInfoDto integrationInfoDto, UpdateExternalEventTitleDescriptionVo updateExternalEventTitleDescriptionVo);

    void deleteCalendarEvent(IntegrationInfoDto integrationInfoDto, String calendarSourceId, String eventId);
}
