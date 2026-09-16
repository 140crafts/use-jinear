package co.jinear.core.manager.management;

import co.jinear.core.config.properties.TelemetryProperties;
import co.jinear.core.model.dto.management.InstanceInfoDto;
import co.jinear.core.model.dto.management.InstanceStatusDto;
import co.jinear.core.model.response.management.InstanceInfoResponse;
import co.jinear.core.service.SessionInfoService;
import co.jinear.core.service.management.InstanceInfoService;
import co.jinear.core.system.util.ReleaseVersionHelper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class InstanceInfoRetrieveManager {

    private final SessionInfoService sessionInfoService;
    private final InstanceInfoService instanceInfoService;
    private final TelemetryProperties telemetryProperties;

    public InstanceInfoResponse retrieve() {
        String accountId = sessionInfoService.currentAccountId();
        log.info("Retrieve instance info has started. accountId: {}", accountId);
        InstanceInfoDto instanceInfoDto = instanceInfoService.retrieve();
        return mapResponse(instanceInfoDto);
    }

    private InstanceInfoResponse mapResponse(InstanceInfoDto instanceInfoDto) {
        boolean sendingAllowed = !telemetryProperties.isDoNotTrackRequested();
        String version = telemetryProperties.getVersion();
        InstanceStatusDto instanceStatusDto = new InstanceStatusDto();
        instanceStatusDto.setVersion(version);
        instanceStatusDto.setLatestVersion(instanceInfoDto.getLatestKnownVersion());
        instanceStatusDto.setUpdateAvailable(ReleaseVersionHelper.isNewer(instanceInfoDto.getLatestKnownVersion(), version));
        instanceStatusDto.setUpdateCheckEnabled(sendingAllowed && Boolean.TRUE.equals(telemetryProperties.getUpdateCheckEnabled()));
        instanceStatusDto.setUsageReportEnabled(sendingAllowed && Boolean.TRUE.equals(telemetryProperties.getUsageReportEnabled()));
        instanceStatusDto.setLastCheckDate(instanceInfoDto.getLastCheckDate());
        InstanceInfoResponse instanceInfoResponse = new InstanceInfoResponse();
        instanceInfoResponse.setInstanceStatusDto(instanceStatusDto);
        return instanceInfoResponse;
    }
}
