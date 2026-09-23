package co.jinear.core.service.management;

import co.jinear.core.converter.management.InstanceInfoDtoConverter;
import co.jinear.core.model.dto.management.InstanceInfoDto;
import co.jinear.core.model.entity.management.InstanceInfo;
import co.jinear.core.repository.management.InstanceInfoRepository;
import co.jinear.core.system.util.ReleaseVersionHelper;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;

import java.time.ZonedDateTime;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class InstanceInfoService {

    private static final long POSTGRES_MAJOR_VERSION_DIVISOR = 10000L;

    private final InstanceInfoRepository instanceInfoRepository;
    private final InstanceInfoDtoConverter instanceInfoDtoConverter;

    public InstanceInfoDto retrieve() {
        log.info("Retrieve instance info has started.");
        return instanceInfoDtoConverter.map(retrieveOrInitialize());
    }

    @Transactional
    public void recordCheck(String latestVersion) {
        log.info("Record check has started. latestVersion: {}", latestVersion);
        InstanceInfo instanceInfo = retrieveOrInitialize();
        instanceInfo.setLastCheckDate(ZonedDateTime.now());
        applyIfNewerRelease(instanceInfo, latestVersion);
        instanceInfoRepository.save(instanceInfo);
    }

    @Transactional
    public String recordReleaseVersion(String version) {
        InstanceInfo instanceInfo = retrieveOrInitialize();
        if (applyIfNewerRelease(instanceInfo, version)) {
            log.info("Latest known release version is updated. version: {}", version);
            instanceInfoRepository.save(instanceInfo);
        }
        return instanceInfo.getLatestKnownVersion();
    }

    public String retrieveDatabaseMajorVersion() {
        try {
            String serverVersionNumber = instanceInfoRepository.retrieveServerVersionNumber();
            return String.valueOf(Long.parseLong(serverVersionNumber) / POSTGRES_MAJOR_VERSION_DIVISOR);
        } catch (DataAccessException | NumberFormatException exception) {
            log.warn("Retrieve database major version has failed. reason: {}", exception.getMessage());
            return null;
        }
    }

    private InstanceInfo retrieveOrInitialize() {
        return instanceInfoRepository.findFirstByPassiveIdIsNullOrderByCreatedDateAsc()
                .orElseGet(this::initialize);
    }

    private InstanceInfo initialize() {
        log.info("Initializing instance info.");
        InstanceInfo instanceInfo = new InstanceInfo();
        instanceInfo.setTelemetryInstanceId(UUID.randomUUID().toString());
        return instanceInfoRepository.save(instanceInfo);
    }

    private boolean applyIfNewerRelease(InstanceInfo instanceInfo, String version) {
        if (!ReleaseVersionHelper.isRelease(version)) {
            return false;
        }
        String latestKnownVersion = instanceInfo.getLatestKnownVersion();
        if (!ReleaseVersionHelper.isRelease(latestKnownVersion) || ReleaseVersionHelper.isNewer(version, latestKnownVersion)) {
            instanceInfo.setLatestKnownVersion(version);
            return true;
        }
        return false;
    }
}
