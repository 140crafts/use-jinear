package co.jinear.core.manager.calendar;

import co.jinear.core.converter.calendar.IntegrationExternalCalendarSourceListDtoMapper;
import co.jinear.core.model.dto.calendar.ExternalCalendarSourceDto;
import co.jinear.core.model.dto.calendar.IntegrationExternalCalendarSourceListDto;
import co.jinear.core.model.dto.integration.IntegrationInfoDto;
import co.jinear.core.model.dto.integration.IntegrationScopeDto;
import co.jinear.core.model.enumtype.integration.IntegrationScopeType;
import co.jinear.core.model.response.calendar.CalendarSourceListResponse;
import co.jinear.core.service.SessionInfoService;
import co.jinear.core.service.integration.IntegrationScopeRetrieveService;
import co.jinear.core.service.integration.calendar.IntegrationCalendarRetrieveStrategy;
import co.jinear.core.service.integration.calendar.IntegrationCalendarRetrieveStrategyFactory;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class CalendarIntegrationManager {

    private final SessionInfoService sessionInfoService;
    private final IntegrationScopeRetrieveService integrationScopeRetrieveService;
    private final IntegrationCalendarRetrieveStrategyFactory integrationCalendarRetrieveStrategyFactory;
    private final IntegrationExternalCalendarSourceListDtoMapper integrationExternalCalendarSourceListDtoMapper;

    public CalendarSourceListResponse listIntegrationSources() {
        String accountId = sessionInfoService.currentAccountId();
        List<IntegrationExternalCalendarSourceListDto> integrationExternalCalendarSourceListDtos = integrationScopeRetrieveService.retrieveIntegrationsWithScope(accountId, IntegrationScopeType.CALENDAR).stream()
                .map(IntegrationScopeDto::getIntegrationInfo)
                .map(this::retrieveAndMapIntegrationCalendarSources)
                .toList();
        return mapResponse(integrationExternalCalendarSourceListDtos);
    }

    private CalendarSourceListResponse mapResponse(List<IntegrationExternalCalendarSourceListDto> integrationExternalCalendarSourceListDtos) {
        CalendarSourceListResponse calendarSourceListResponse = new CalendarSourceListResponse();
        calendarSourceListResponse.setIntegrationExternalCalendarSourceListDtos(integrationExternalCalendarSourceListDtos);
        return calendarSourceListResponse;
    }

    private IntegrationExternalCalendarSourceListDto retrieveAndMapIntegrationCalendarSources(IntegrationInfoDto integrationInfoDto) {
        IntegrationCalendarRetrieveStrategy integrationCalendarRetrieveStrategy = integrationCalendarRetrieveStrategyFactory.getStrategy(integrationInfoDto.getProvider());
        List<ExternalCalendarSourceDto> calendarSourceDtos = integrationCalendarRetrieveStrategy.retrieveCalendarSources(integrationInfoDto);
        return integrationExternalCalendarSourceListDtoMapper.map(integrationInfoDto, calendarSourceDtos);
    }
}
