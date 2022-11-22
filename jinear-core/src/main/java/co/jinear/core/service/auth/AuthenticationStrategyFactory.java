package co.jinear.core.service.auth;

import co.jinear.core.exception.auth.NotSupportedAuthStrategyException;
import co.jinear.core.model.enumtype.auth.ProviderType;
import org.springframework.stereotype.Component;

import java.util.EnumMap;
import java.util.List;
import java.util.Optional;

@Component
public class AuthenticationStrategyFactory {

    private final EnumMap<ProviderType, AuthenticationStrategy> strategyMap = new EnumMap<>(ProviderType.class);

    public AuthenticationStrategyFactory(List<AuthenticationStrategy> strategies) {
        strategies.forEach(strategy -> strategyMap.put(strategy.getType(), strategy));
    }

    public AuthenticationStrategy getStrategy(ProviderType type) {
        return Optional.ofNullable(strategyMap.get(type)).orElseThrow(NotSupportedAuthStrategyException::new);
    }
}
