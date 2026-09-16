package co.jinear.core.service.telemetry;

import co.jinear.core.model.dto.telemetry.InstanceReportResultDto;
import co.jinear.core.model.request.telemetry.InstanceReportRequest;
import co.jinear.core.model.response.telemetry.InstanceReportResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class TelemetryApiClient {

    private static final String INSTANCE_REPORT = "/v1/instance-report";

    private final RestTemplate telemetryRestTemplate;

    public Optional<InstanceReportResultDto> sendReport(InstanceReportRequest instanceReportRequest) {
        log.info("Send report has started. usageShared: {}", instanceReportRequest.getUsage() != null);
        InstanceReportResponse instanceReportResponse = telemetryRestTemplate.postForObject(INSTANCE_REPORT, instanceReportRequest, InstanceReportResponse.class);
        return Optional.ofNullable(instanceReportResponse)
                .map(InstanceReportResponse::getInstanceReportResultDto);
    }
}
