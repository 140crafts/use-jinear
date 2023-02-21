package co.jinear.core.service.reminder.process.strategy;

import co.jinear.core.exception.BusinessException;
import co.jinear.core.model.enumtype.reminder.ReminderType;
import org.springframework.stereotype.Component;

import java.util.EnumMap;
import java.util.List;
import java.util.Optional;

@Component
public class ReminderJobProcessStrategyFactory {

    private final EnumMap<ReminderType, ReminderJobProcessStrategy> strategyMap = new EnumMap<>(ReminderType.class);

    public ReminderJobProcessStrategyFactory(List<ReminderJobProcessStrategy> strategies) {
        strategies.forEach(strategy -> strategyMap.put(strategy.getType(), strategy));
    }

    public ReminderJobProcessStrategy getStrategy(ReminderType type) {
        return Optional.ofNullable(strategyMap.get(type)).orElseThrow(BusinessException::new);
    }
}
