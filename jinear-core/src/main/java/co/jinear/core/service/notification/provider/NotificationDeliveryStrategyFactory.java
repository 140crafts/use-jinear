package co.jinear.core.service.notification.provider;

import co.jinear.core.exception.auth.NotSupportedAuthStrategyException;
import co.jinear.core.model.enumtype.notification.NotificationProviderType;
import org.springframework.stereotype.Component;

import java.util.EnumMap;
import java.util.List;
import java.util.Optional;

@Component
public class NotificationDeliveryStrategyFactory {

    private final EnumMap<NotificationProviderType, NotificationDeliveryStrategy> strategyMap = new EnumMap<>(NotificationProviderType.class);

    public NotificationDeliveryStrategyFactory(List<NotificationDeliveryStrategy> strategies) {
        strategies.forEach(strategy -> strategyMap.put(strategy.getProviderType(), strategy));
    }

    public NotificationDeliveryStrategy getStrategy(NotificationProviderType  type) {
        return Optional.ofNullable(strategyMap.get(type)).orElseThrow(NotSupportedAuthStrategyException::new);
    }
}
