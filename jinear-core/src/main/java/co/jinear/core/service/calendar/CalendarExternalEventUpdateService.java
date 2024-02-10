package co.jinear.core.service.calendar;

import co.jinear.core.model.dto.integration.IntegrationInfoDto;
import co.jinear.core.model.vo.calendar.UpdateExternalEventDatesVo;
import co.jinear.core.service.integration.calendar.IntegrationCalendarRetrieveStrategy;
import co.jinear.core.service.integration.calendar.IntegrationCalendarRetrieveStrategyFactory;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class CalendarExternalEventUpdateService {

    private final IntegrationCalendarRetrieveStrategyFactory integrationCalendarRetrieveStrategyFactory;

    public void updateCalendarEventDates(IntegrationInfoDto integrationInfoDto, UpdateExternalEventDatesVo updateExternalEventDatesVo) {
        log.info("Update calendar event dates has started. updateExternalEventDatesVo: {}", updateExternalEventDatesVo);
        IntegrationCalendarRetrieveStrategy integrationCalendarRetrieveStrategy = integrationCalendarRetrieveStrategyFactory.getStrategy(integrationInfoDto.getProvider());
        integrationCalendarRetrieveStrategy.updateCalendarEventDates(integrationInfoDto, updateExternalEventDatesVo);
    }
}
