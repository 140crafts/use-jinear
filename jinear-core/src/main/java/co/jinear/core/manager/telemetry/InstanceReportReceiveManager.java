package co.jinear.core.manager.telemetry;

import co.jinear.core.config.properties.TelemetryProperties;
import co.jinear.core.converter.telemetry.InstanceReportVoConverter;
import co.jinear.core.exception.NotFoundException;
import co.jinear.core.model.dto.telemetry.InstanceReportResultDto;
import co.jinear.core.model.request.telemetry.InstanceReportRequest;
import co.jinear.core.model.response.telemetry.InstanceReportResponse;
import co.jinear.core.service.management.InstanceInfoService;
import co.jinear.core.service.telemetry.InstanceReportService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class InstanceReportReceiveManager {

    private final TelemetryProperties telemetryProperties;
    private final InstanceReportService instanceReportService;
    private final InstanceInfoService instanceInfoService;
    private final InstanceReportVoConverter instanceReportVoConverter;

    public InstanceReportResponse receive(InstanceReportRequest instanceReportRequest) {
        log.info("Receive instance report has started. version: {}", instanceReportRequest.getVersion());
        if (!Boolean.TRUE.equals(telemetryProperties.getReceiverEnabled())) {
            throw new NotFoundException();
        }
        instanceReportService.upsert(instanceReportVoConverter.map(instanceReportRequest));
        String latestVersion = instanceInfoService.recordReleaseVersion(telemetryProperties.getVersion());
        return mapResponse(latestVersion);
    }

    private InstanceReportResponse mapResponse(String latestVersion) {
        InstanceReportResultDto instanceReportResultDto = new InstanceReportResultDto();
        instanceReportResultDto.setLatestVersion(latestVersion);
        InstanceReportResponse instanceReportResponse = new InstanceReportResponse();
        instanceReportResponse.setInstanceReportResultDto(instanceReportResultDto);
        return instanceReportResponse;
    }
}
