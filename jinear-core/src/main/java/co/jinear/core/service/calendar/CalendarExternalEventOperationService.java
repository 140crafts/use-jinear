package co.jinear.core.service.calendar;

import co.jinear.core.model.dto.integration.IntegrationInfoDto;
import co.jinear.core.model.vo.calendar.InitializeExternalEventVo;
import co.jinear.core.model.vo.calendar.MoveExternalEventVo;
import co.jinear.core.model.vo.calendar.UpdateExternalEventDatesVo;
import co.jinear.core.model.vo.calendar.UpdateExternalEventTitleDescriptionVo;
import co.jinear.core.service.integration.calendar.IntegrationCalendarRetrieveStrategy;
import co.jinear.core.service.integration.calendar.IntegrationCalendarRetrieveStrategyFactory;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class CalendarExternalEventOperationService {

    private final IntegrationCalendarRetrieveStrategyFactory integrationCalendarRetrieveStrategyFactory;

    public void insertCalendarEvent(IntegrationInfoDto integrationInfoDto, InitializeExternalEventVo initializeExternalEventVo) {
        log.info("Insert calendar event has started. initializeExternalEventVo: {}", initializeExternalEventVo);
        IntegrationCalendarRetrieveStrategy integrationCalendarRetrieveStrategy = integrationCalendarRetrieveStrategyFactory.getStrategy(integrationInfoDto.getProvider());
        integrationCalendarRetrieveStrategy.insertCalendarEvent(integrationInfoDto, initializeExternalEventVo);
    }

    public void moveCalendarEvent(IntegrationInfoDto integrationInfoDto, MoveExternalEventVo moveExternalEventVo) {
        log.info("Move calendar event has started. moveExternalEventVo: {}", moveExternalEventVo);
        IntegrationCalendarRetrieveStrategy integrationCalendarRetrieveStrategy = integrationCalendarRetrieveStrategyFactory.getStrategy(integrationInfoDto.getProvider());
        integrationCalendarRetrieveStrategy.moveCalendarEvent(integrationInfoDto, moveExternalEventVo);
    }

    public void updateCalendarEventDates(IntegrationInfoDto integrationInfoDto, UpdateExternalEventDatesVo updateExternalEventDatesVo) {
        log.info("Update calendar event dates has started. updateExternalEventDatesVo: {}", updateExternalEventDatesVo);
        IntegrationCalendarRetrieveStrategy integrationCalendarRetrieveStrategy = integrationCalendarRetrieveStrategyFactory.getStrategy(integrationInfoDto.getProvider());
        integrationCalendarRetrieveStrategy.updateCalendarEventDates(integrationInfoDto, updateExternalEventDatesVo);
    }

    public void updateCalendarEventTitleDescription(IntegrationInfoDto integrationInfoDto, UpdateExternalEventTitleDescriptionVo updateExternalEventTitleDescriptionVo) {
        log.info("Update calendar event title and description has started. updateExternalEventTitleDescriptionVo: {}", updateExternalEventTitleDescriptionVo);
        IntegrationCalendarRetrieveStrategy integrationCalendarRetrieveStrategy = integrationCalendarRetrieveStrategyFactory.getStrategy(integrationInfoDto.getProvider());
        integrationCalendarRetrieveStrategy.updateCalendarEventTitleDescription(integrationInfoDto, updateExternalEventTitleDescriptionVo);
    }

    public void deleteCalendarEvent(IntegrationInfoDto integrationInfoDto, String calendarSourceId, String eventId) {
        log.info("Delete calendar event has started. calendarSourceId: {}, eventId: {}", calendarSourceId, eventId);
        IntegrationCalendarRetrieveStrategy integrationCalendarRetrieveStrategy = integrationCalendarRetrieveStrategyFactory.getStrategy(integrationInfoDto.getProvider());
        integrationCalendarRetrieveStrategy.deleteCalendarEvent(integrationInfoDto, calendarSourceId, eventId);
    }
}
