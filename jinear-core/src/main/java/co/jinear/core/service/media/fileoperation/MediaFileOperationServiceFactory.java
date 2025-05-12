package co.jinear.core.service.media.fileoperation;

import co.jinear.core.exception.BusinessException;
import co.jinear.core.model.enumtype.media.MediaFileProviderType;
import org.springframework.stereotype.Component;

import java.util.EnumMap;
import java.util.List;
import java.util.Optional;

@Component
public class MediaFileOperationServiceFactory {

    private final EnumMap<MediaFileProviderType, MediaFileOperationStrategy> strategyMap = new EnumMap<>(MediaFileProviderType.class);

    public MediaFileOperationServiceFactory(List<MediaFileOperationStrategy> strategies) {
        strategies.forEach(strategy -> strategyMap.put(strategy.getType(), strategy));
    }

    public MediaFileOperationStrategy getStrategy(MediaFileProviderType type) {
        return Optional.ofNullable(strategyMap.get(type)).orElseThrow(BusinessException::new);
    }
}
