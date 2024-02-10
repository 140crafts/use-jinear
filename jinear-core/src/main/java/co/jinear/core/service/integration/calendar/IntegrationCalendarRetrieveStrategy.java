package co.jinear.core.service.integration.calendar;

import co.jinear.core.model.dto.calendar.CalendarEventDto;
import co.jinear.core.model.dto.calendar.ExternalCalendarSourceDto;
import co.jinear.core.model.dto.integration.IntegrationInfoDto;
import co.jinear.core.model.enumtype.integration.IntegrationProvider;
import co.jinear.core.model.vo.calendar.UpdateExternalEventDatesVo;
import co.jinear.core.system.gcloud.googleapis.model.calendar.request.RetrieveEventListRequest;

import java.util.List;

public interface IntegrationCalendarRetrieveStrategy {

    IntegrationProvider getProvider();

    ExternalCalendarSourceDto retrieveCalendarSource(IntegrationInfoDto integrationInfoDto, String externalCalendarSourceId);

    List<ExternalCalendarSourceDto> retrieveCalendarSources(IntegrationInfoDto integrationInfoDto);

    List<CalendarEventDto> retrieveCalendarEvents(IntegrationInfoDto integrationInfoDto, RetrieveEventListRequest retrieveEventListRequest);

    CalendarEventDto retrieveCalendarEvent(IntegrationInfoDto integrationInfoDto, String calendarSourceId, String eventId);

    void updateCalendarEventDates(IntegrationInfoDto integrationInfoDto, UpdateExternalEventDatesVo updateExternalEventDatesVo);
}
