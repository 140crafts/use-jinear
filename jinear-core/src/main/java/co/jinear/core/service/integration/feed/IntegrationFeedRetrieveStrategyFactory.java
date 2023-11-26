package co.jinear.core.service.integration.feed;

import co.jinear.core.exception.BusinessException;
import co.jinear.core.model.enumtype.integration.IntegrationProvider;
import org.springframework.stereotype.Component;

import java.util.EnumMap;
import java.util.List;
import java.util.Optional;

@Component
public class IntegrationFeedRetrieveStrategyFactory {

    private final EnumMap<IntegrationProvider, IntegrationFeedRetrieveStrategy> strategyMap = new EnumMap<>(IntegrationProvider.class);

    public IntegrationFeedRetrieveStrategyFactory(List<IntegrationFeedRetrieveStrategy> strategies) {
        strategies.forEach(strategy -> strategyMap.put(strategy.getProvider(), strategy));
    }

    public IntegrationFeedRetrieveStrategy getStrategy(IntegrationProvider type) {
        return Optional.ofNullable(strategyMap.get(type)).orElseThrow(BusinessException::new);
    }
}
