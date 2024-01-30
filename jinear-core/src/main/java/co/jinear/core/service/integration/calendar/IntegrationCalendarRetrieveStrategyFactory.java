package co.jinear.core.service.integration.calendar;

import co.jinear.core.exception.BusinessException;
import co.jinear.core.model.enumtype.integration.IntegrationProvider;
import org.springframework.stereotype.Component;

import java.util.EnumMap;
import java.util.List;
import java.util.Optional;

@Component
public class IntegrationCalendarRetrieveStrategyFactory {

    private final EnumMap<IntegrationProvider, IntegrationCalendarRetrieveStrategy> strategyMap = new EnumMap<>(IntegrationProvider.class);

    public IntegrationCalendarRetrieveStrategyFactory(List<IntegrationCalendarRetrieveStrategy> strategies) {
        strategies.forEach(strategy -> strategyMap.put(strategy.getProvider(), strategy));
    }

    public IntegrationCalendarRetrieveStrategy getStrategy(IntegrationProvider type) {
        return Optional.ofNullable(strategyMap.get(type)).orElseThrow(BusinessException::new);
    }
}
