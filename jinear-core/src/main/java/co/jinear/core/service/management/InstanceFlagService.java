package co.jinear.core.service.management;

import co.jinear.core.exception.BusinessException;
import co.jinear.core.exception.NotFoundException;
import co.jinear.core.model.entity.management.InstanceFlag;
import co.jinear.core.model.enumtype.management.InstanceFlagType;
import co.jinear.core.repository.management.InstanceFlagRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class InstanceFlagService {

    private final InstanceFlagRepository instanceFlagRepository;

    public Map<InstanceFlagType, Object> retrieveAll() {
        log.info("List flags has started.");
        return instanceFlagRepository.findAllByPassiveIdIsNullOrderByInstanceFlagId()
                .stream()
                .collect(Collectors.toMap(InstanceFlag::getFlagType, instanceFlag -> instanceFlag.getFlagType().parse(instanceFlag.getFlagValue())));
    }

    @Transactional
    public void set(InstanceFlagType instanceFlagType, Object value) {
        log.info("Set instance flag has started. instanceFlagType: {}, value: {}", instanceFlagType, value);
        InstanceFlag instanceFlag = retrieve(instanceFlagType);
        instanceFlag.setFlagValue(instanceFlagType.format(value));
        instanceFlagRepository.save(instanceFlag);
    }

    /**
     * A non throwing read for callers that want to describe the instance rather than
     * refuse a request. A missing row reads as off.
     */
    public boolean isEnabled(InstanceFlagType instanceFlagType) {
        return instanceFlagRepository.findFirstByFlagType(instanceFlagType)
                .map(instanceFlag -> Boolean.TRUE.equals(instanceFlagType.parse(instanceFlag.getFlagValue())))
                .orElse(Boolean.FALSE);
    }

    public void validateFlagValueMatches(InstanceFlagType instanceFlagType, Object value) {
        log.info("Validate flag value matches has started. instanceFlagType: {}, checking agains value: {}", instanceFlagType, value);
        InstanceFlag instanceFlag = retrieve(instanceFlagType);
        String formatted = instanceFlagType.format(value);
        if (!instanceFlag.getFlagValue().equalsIgnoreCase(formatted)) {
            throw new BusinessException("common.error.system.validation-fail");
        }
    }

    private InstanceFlag retrieve(InstanceFlagType instanceFlagType) {
        return instanceFlagRepository.findFirstByFlagType(instanceFlagType)
                .orElseThrow(NotFoundException::new);
    }
}
