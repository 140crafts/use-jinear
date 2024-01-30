package co.jinear.core.service.calendar;

import co.jinear.core.model.dto.calendar.CalendarDto;
import co.jinear.core.model.dto.calendar.ExternalCalendarSourceDto;
import co.jinear.core.model.dto.integration.IntegrationInfoDto;
import co.jinear.core.service.integration.calendar.IntegrationCalendarRetrieveStrategy;
import co.jinear.core.service.integration.calendar.IntegrationCalendarRetrieveStrategyFactory;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class CalendarExternalSourceRetrieveService {

    private final IntegrationCalendarRetrieveStrategyFactory integrationCalendarRetrieveStrategyFactory;

    public CalendarDto retrieveAndSetExternalCalendarSources(CalendarDto calendarDto) {
        log.info("Retrieve and set external calendar sources has started. calendarId: {}", calendarDto.getCalendarId());
        Optional.of(calendarDto)
                .map(CalendarDto::getIntegrationInfo)
                .map(this::retrieveIntegrationCalendarSources)
                .ifPresent(calendarDto::setCalendarSources);
        return calendarDto;
    }

    private List<ExternalCalendarSourceDto> retrieveIntegrationCalendarSources(IntegrationInfoDto integrationInfoDto) {
        IntegrationCalendarRetrieveStrategy integrationCalendarRetrieveStrategy = integrationCalendarRetrieveStrategyFactory.getStrategy(integrationInfoDto.getProvider());
        return integrationCalendarRetrieveStrategy.retrieveCalendarSources(integrationInfoDto);
    }
}
